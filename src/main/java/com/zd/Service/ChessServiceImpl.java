package com.zd.Service;

import com.zd.Entity.Board;
import com.zd.Entity.GameState;
import com.zd.Entity.Move;
import com.zd.Entity.Piece;
import com.zd.Enum.Color;
import com.zd.Mapper.GameMapper;
import com.zd.Mapper.MoveMapper;
import com.zd.Rule.RuleEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class ChessServiceImpl implements ChessService{
    @Autowired
    GameMapper gameMapper;
    @Autowired
    MoveMapper moveMapper;
    @Autowired
    RuleEngine ruleEngine;
    @Override
    @Transactional
    public GameState initialize(String sessionId) {

        //检查是否存在游戏状态
        GameState existingGame = gameMapper.selectBySessionId(sessionId);

        if(existingGame != null){
            //删除当前游戏状态
            gameMapper.deleteGame(sessionId);
//            删除当前移动记录
            moveMapper.deleteMovesByGameId(existingGame.getId());

        }
        //新建游戏状态并初始化棋盘
        GameState gameState = new GameState(sessionId);
        gameState.getBoard().initializeBoard();

        log.info("gameState.BoardState为：{}",gameState.getBoardState());
        //保存游戏状态
        gameMapper.save(gameState);

        return gameState;
    }

    @Override
    public GameState getGameState(String sessionId) {
        GameState gameState = gameMapper.selectBySessionId(sessionId);

        if (gameState != null && gameState.getBoardState() != null) {
            Board board = new Board();
            board.deserialize(gameState.getBoardState());
            log.info("棋盘状态为：{}",gameState.getBoardState());
            gameState.setBoard(board);
        }
        return gameState;
    }

    @Override
    @Transactional
    //棋子移动
    public Move makeMove(String sessionId, int fromX, int fromY, int toX, int toY) {
        //获取并判断游戏状态
        GameState gameState = getGameState(sessionId);
        System.out.println();
        if(gameState == null || gameState.getBoard() == null)
            return null;

        //检查是否是当前玩家的棋子
        Board board = gameState.getBoard();
        Piece p = board.getPiece(fromX,fromY);
        if(p == null || p.getColor() != gameState.getCurrentPlayer())
            return null;

        //检查移动是否合法
        if(!ruleEngine.isValidMove(board,fromX,fromY,toX,toY))
            return null;
        //执行移动
        Piece capturedPiece = board.getPiece(toX,toY);
        board.movePiece(fromX,fromY,toX,toY);
        //记录移动
        Move move = new Move(fromX,fromY,toX,toY,p,capturedPiece);
        move.setPieceType(p.getType());
        move.setPieceColor(p.getColor());

        //设置moveIndex
        int currentMoveIndex = moveMapper.countMovesByGameId(gameState.getId());
        move.setMoveIndex(currentMoveIndex);
        board.addMoveToHistory(move);


        //拿到要移动的棋子
        Piece toPiece = gameState.getBoard().getPiece(toX,toY);
        //设置它的坐标为将要移动到的地方
        log.info("toPiece不为空：{}",toPiece);
        //棋子将要移动到的地方的有效路径
        List<int[]> toPath = ruleEngine.validPath(gameState.getBoard(), toPiece);
        // 在交换回合之前检查是否将军
        boolean isCheck = ruleEngine.isInCheck(gameState.getBoard(), gameState, toPath);
        // 将将军状态设置到Move对象中
        move.setCheck(isCheck);

        log.info("isCheck={}",isCheck);

        //交换回合
        gameState.switchPlayer();
        //更新数据库
        gameState.setBoardState(board.serialize());
        gameMapper.updateGame(gameState);
        //保存到数据库
        move.setGameId(gameState.getId());
        log.info("id:{}",move);
        moveMapper.insertMove(move);

        return move;
    }
    //实现悔棋方法
    @Override
    @Transactional
    public boolean undoMove(String sessionId) {
        GameState gameState = getGameState(sessionId);
        if(gameState == null){
            log.info("悔棋失败：没有找到游戏状态");
            return false;
        }

        //找到move_history中对应gameid的最后一个索引；
        Move lastmove = moveMapper.getLastMove(gameState.getId());
        if(lastmove == null){
            log.info("当前为第一步，没有可悔之棋");
            return false;
        }
        Board board =  gameState.getBoard();
        //将棋子移回原位置
        Piece movedPiece = board.getPiece(lastmove.getToX(), lastmove.getToY());

        if(movedPiece != null){
            board.setPiece(lastmove.getFromX(), lastmove.getFromY(), movedPiece);
            board.setPiece(lastmove.getToX(), lastmove.getToY(), null);
            //判断悔棋前是否吃子
            if(lastmove.getCapturedPieceColor() != null && lastmove.getCapturedPieceType() != null){
                Piece capturedPiece = new Piece(lastmove.getCapturedPieceType(),
                        lastmove.getCapturedPieceColor(),
                        lastmove.getToX(),
                        lastmove.getToY());
                board.setPiece(capturedPiece.getX(), capturedPiece.getY(), capturedPiece);
            }
        }
        else{
            log.info("目标位置没有找到棋子");
        }
        //删除最后一条记录
        Integer maxMoveIndex = moveMapper.getMaxIndex(gameState.getId());
        if(maxMoveIndex != null){
            moveMapper.deleteMovesByGameId(gameState.getId());
        }
        //切回上一个玩家的回合
        gameState.switchPlayer();
        // 更新数据库,更新gamestate表中棋盘的数据
        gameState.setBoardState(board.serialize());
        gameMapper.updateGame(gameState);
        //匹配中同步到双方
        return true;
    }


}

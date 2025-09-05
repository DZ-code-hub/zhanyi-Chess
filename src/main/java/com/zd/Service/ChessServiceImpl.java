package com.zd.Service;

import com.zd.Entity.Board;
import com.zd.Entity.GameState;
import com.zd.Mapper.GameMapper;
import com.zd.Mapper.MoveMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ChessServiceImpl implements ChessService{
    @Autowired
    GameMapper gameMapper;
   /* @Autowired
    MoveMapper;*/
    @Override
    @Transactional
    public GameState initialize(String sessionId) {

        //检查是否存在游戏状态
        GameState existingGame = gameMapper.selectBySessionId(sessionId);

        if(existingGame != null){
            //删除当前游戏状态
            gameMapper.deleteGame(sessionId);
            //删除当前移动记录
            //moveMapper.deleteMovesBySessionId(sessionId);

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


}

package com.zd.Rule;

import com.zd.Entity.Board;
import com.zd.Entity.GameState;
import com.zd.Entity.Piece;
import com.zd.Enum.Color;
import com.zd.Enum.PieceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RuleEngine {
    @Autowired
    RuleOfBoss ruleOfBoss;
    @Autowired
      RuleOfShi ruleOfShi;
    @Autowired
      RuleOfXiang ruleOfXiang;
    @Autowired
      RuleOfMa ruleOfMa;
    @Autowired
      RuleOfChe ruleOfChe;
    @Autowired
      RuleOfPao ruleOfPao;
    @Autowired
      RuleOfBing ruleOfBing;
     Color color;
    //判断移动是否有效
    public boolean isValidMove(Board board,int fromX, int fromY, int toX, int toY){
        //棋子不能出界
        if(fromX > 8 || fromX < 0 || fromY < 0 || toY > 9)
            return false;

        Piece piece = board.getPiece(fromX,fromY);
        if(piece == null)
            return false;

        color = piece.getColor();

        //如果目的地有己方棋子，返回false
        Piece targetPiece = board.getPiece(toX,toY);
        if(targetPiece != null && piece.getColor() == targetPiece.getColor())
            return false;

        switch (piece.getType()){
            case BOSS -> {
                 return ruleOfBoss.isValidBossMove(board,fromX,fromY,toX,toY,color);
            }
            case SHI -> {
                return ruleOfShi.isValidShiMove(board,fromX,fromY,toX,toY,color);
            }
            case XIANG -> {
                return ruleOfXiang.isValidXiangMove(board,fromX,fromY,toX,toY,color);
            }
            case MA -> {
                return ruleOfMa.isValidMaMove(board,fromX,fromY,toX,toY);
            }
            case CHE -> {
                return ruleOfChe.isValidCheMove(board,fromX,fromY,toX,toY);
            }
            case PAO -> {
                return ruleOfPao.isValidPaoMove(board,fromX,fromY,toX,toY);
            }
            case BING -> {
                return ruleOfBing.isValidBingMove(board,fromX,fromY,toX,toY,color);
            }
            default -> {
                return false;
            }
        }
    }

    //检查游戏是否结束
    public String checkWinner(GameState gameState){

        if(gameState == null || gameState.getBoard() == null)
            return null;

        boolean hasRedBoss = false;
        boolean hasBlackBoss = false;
        for(int x = 3; x <= 5; x++){
            for(int y = 0; y <= 9; y++){
                Piece p = gameState.getBoard().getPiece(x, y);
                if(p != null){
                    if(p.getColor() == Color.RED && p.getType() == PieceType.BOSS)
                        hasRedBoss = true;

                    if(p.getColor() == Color.BLACK && p.getType() == PieceType.BOSS)
                        hasBlackBoss = true;
                }

            }
        }
        if(!hasRedBoss && hasBlackBoss) return "BLACK_WIN";
        if(!hasBlackBoss && hasRedBoss) return "RED_WIN";
        return null;
    }

    //判断是否被将军，若被将则只能解将
    // 检查是否将军对方
    public boolean isInCheck(Board board, GameState gameState,List<int[]> toPath) {
        Color currentPlayer = gameState.getCurrentPlayer();
        //拿到对方老将的颜色
        Color BossColor = currentPlayer == Color.RED ? Color.BLACK : Color.RED;
        Piece diFangBoss = null;

        // 老将只能在九宫格内
        for (int x = 3; x <= 5; x++) {
            for (int y = 0; y <= 9; y++) {
                //当前位置有棋子
                if (board.getPiece(x, y) == null)
                    continue;
                if (board.getPiece(x, y).getType() == PieceType.BOSS && board.getPiece(x, y).getColor() == BossColor) {
                    diFangBoss = board.getPiece(x, y);
                    break;
                }
            }
        }

        // 添加空指针检查
        if (diFangBoss == null) {
            log.warn("未找到{}方的老将", BossColor);
            return false;
        }

        //拿到敌方老将的坐标位置
        int bossX = diFangBoss.getX();
        int bossY = diFangBoss.getY();

         for (int[] newPos : toPath) {
            if (newPos[0] == bossX && newPos[1] == bossY) {
                log.info(BossColor + "方正在被将军");
                log.info("toPath被调用");
                return true;
            }
        }

        Piece piece;
        for (int x = 0; x <= 8; x++) {
            for (int y = 0; y <= 9; y++) {
                //当前位置有棋子
                if (board.getPiece(x, y) != null && board.getPiece(x, y).getColor() == currentPlayer) {
                    piece = board.getPiece(x, y);

                        List<int[]> path = validPath(board, piece);

                        // 直接比较坐标而不是数组引用
                        for (int[] newPos : path) {
                            if (newPos[0] == bossX && newPos[1] == bossY) {
                                log.info(BossColor + "方正在被将军");
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

    //判断每一个棋子可以走到的位置
    public List<int[]> validPath(Board board,Piece piece){
        List<int[]> path = new ArrayList<>();
        for (int y = 0; y < 10; y++) {
            for ( int x = 0; x < 9; x++) {
                if (isValidMove(board, piece.getX(), piece.getY(), x, y)) {
                    //如果位置合法，就添加进集合
                    path.add(new int[]{x, y});
                }
            }
        }
        return path;
    }

}

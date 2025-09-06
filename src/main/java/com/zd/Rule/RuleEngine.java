package com.zd.Rule;

import com.zd.Entity.Board;
import com.zd.Entity.GameState;
import com.zd.Entity.Piece;
import com.zd.Enum.Color;
import com.zd.Enum.PieceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
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

}

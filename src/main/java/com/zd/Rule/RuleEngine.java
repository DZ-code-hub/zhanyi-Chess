package com.zd.Rule;

import com.zd.Entity.Board;
import com.zd.Entity.Piece;
import com.zd.Enum.Color;
import org.springframework.stereotype.Component;

@Component
public class RuleEngine {
     static RuleOfBoss ruleOfBoss;
     static RuleOfShi ruleOfShi;
     static RuleOfXiang ruleOfXiang;
     static RuleOfMa ruleOfMa;
     static RuleOfChe ruleOfChe;
     static RuleOfPao ruleOfPao;
     static RuleOfBing ruleOfBing;
     Color color;
    //判断移动是否有效
    public boolean isVaildMove(Board board,int fromX, int fromY, int toX, int toY){
        //棋子不能出界
        if(fromX > 8 || fromX < 0 || fromY < 0 || toY > 9)
            return false;

        Piece piece = board.getPiece(fromX,fromY);
        color = piece.getColor();

        if(piece == null)
            return false;

        //如果目的地有己方棋子，返回false
        if(piece.getColor() == board.getPiece(toX,toY).getColor())
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

}

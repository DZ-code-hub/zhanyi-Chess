package com.zd.Rule;

import com.zd.Entity.Board;
import com.zd.Entity.Piece;
import org.springframework.stereotype.Component;

@Component
public class RuleOfMa extends Piece {
    public boolean isValidMaMove(Board board, int fromX, int fromY, int toX, int toY) {
        // 马走日字
        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);
        if (!((dx == 1 && dy == 2) || (dx == 2 && dy == 1))) {
            return false;
        }

        // 检查马腿是否被堵
        int legX = fromX;
        int legY = fromY;

        if (dx == 1) { // 竖着走日字
            legY = fromY + (toY > fromY ? 1 : -1);
        } else { // 横着走日字
            legX = fromX + (toX > fromX ? 1 : -1);
        }

        return board.getPiece(legX, legY) == null;
    }
}

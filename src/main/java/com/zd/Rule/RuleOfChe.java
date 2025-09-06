package com.zd.Rule;

import com.zd.Entity.Board;
import com.zd.Entity.Piece;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RuleOfChe extends Piece {
    public boolean isValidCheMove(Board board, int fromX, int fromY, int toX, int toY) {
        // 车走直线
        if (fromX != toX && fromY != toY) {
            return false;
        }

        // 检查路径上是否有其他棋子
        if (fromX == toX) { // 竖着走
            int startY = Math.min(fromY, toY) + 1;
            int endY = Math.max(fromY, toY);
            for (int y = startY; y < endY; y++) {
                if (board.getPiece(fromX, y) != null) {
                    return false;
                }
            }
        } else { // 横着走
            int startX = Math.min(fromX, toX) + 1;
            int endX = Math.max(fromX, toX);
            for (int x = startX; x < endX; x++) {
                if (board.getPiece(x, fromY) != null) {
                    return false;
                }
            }
        }

        return true;
    }
}

package com.zd.Rule;

import com.zd.Entity.Board;
import com.zd.Entity.Piece;
import com.zd.Enum.Color;

public class RuleOfXiang extends Piece {
    public boolean isValidXiangMove(Board board, int fromX, int fromY, int toX, int toY, Color color) {
        // 象/相不能过河
        if (color == Color.RED && toY > 4) {
            return false;
        }
        if (color == Color.BLACK && toY < 5) {
            return false;
        }

        // 象/相走田字
        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);
        if (dx != 2 || dy != 2) {
            return false;
        }

        // 检查象眼是否被堵
        int eyeX = (fromX + toX) / 2;
        int eyeY = (fromY + toY) / 2;
        return board.getPiece(eyeX, eyeY) == null;
    }
}

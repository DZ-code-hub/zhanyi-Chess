package com.zd.Rule;

import com.zd.Entity.Board;
import com.zd.Entity.Piece;
import com.zd.Enum.Color;

public class RuleOfShi extends Piece {
    public boolean isValidShiMove(Board board, int fromX, int fromY, int toX, int toY, Color color) {
        // 士/仕只能在九宫格内移动
        if (color == Color.RED) {
            if (toX < 3 || toX > 5 || toY < 0 || toY > 2) {
                return false;
            }
        } else {
            if (toX < 3 || toX > 5 || toY < 7 || toY > 9) {
                return false;
            }
        }

        // 士/仕只能斜着移动一格
        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);
        return dx == 1 && dy == 1;
    }
}

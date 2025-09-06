package com.zd.Rule;

import com.zd.Entity.Board;
import com.zd.Entity.Piece;
import com.zd.Enum.Color;
import org.springframework.stereotype.Component;

@Component
public class RuleOfBing extends Piece {
    public boolean isValidBingMove(Board board, int fromX, int fromY, int toX, int toY, Color color) {
        // 兵/卒移动规则
        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);

        if (dx + dy != 1) { // 只能移动一格
            return false;
        }

        if (color == Color.RED) {
            if (fromY < 5) { // 未过河
                return toY == fromY + 1 && toX == fromX; // 只能前进
            } else { // 已过河
                return toY == fromY + 1 || (toY == fromY && dx == 1); // 可以前进或左右移动
            }
        } else {
            if (fromY > 4) { // 未过河
                return toY == fromY - 1 && toX == fromX; // 只能前进
            } else { // 已过河
                return toY == fromY - 1 || (toY == fromY && dx == 1); // 可以前进或左右移动
            }
        }
    }
}

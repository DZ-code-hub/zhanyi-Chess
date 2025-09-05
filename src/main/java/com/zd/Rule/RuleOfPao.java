package com.zd.Rule;

import com.zd.Entity.Board;
import com.zd.Entity.Piece;

public class RuleOfPao extends Piece {
    public boolean isValidPaoMove(Board board, int fromX, int fromY, int toX, int toY) {
        // 炮走直线
        if (fromX != toX && fromY != toY) {
            return false;
        }

        // 计算路径上的棋子数量
        int pieceCount = 0;
        if (fromX == toX) { // 竖着走
            int startY = Math.min(fromY, toY) + 1;
            int endY = Math.max(fromY, toY);
            for (int y = startY; y < endY; y++) {
                if (board.getPiece(fromX, y) != null) {
                    pieceCount++;
                }
            }
        } else { // 横着走
            int startX = Math.min(fromX, toX) + 1;
            int endX = Math.max(fromX, toX);
            for (int x = startX; x < endX; x++) {
                if (board.getPiece(x, fromY) != null) {
                    pieceCount++;
                }
            }
        }

        // 炮移动规则：无炮架不能吃子，有炮架必须吃子
        Piece targetPiece = board.getPiece(toX, toY);
        if (targetPiece == null) {
            return pieceCount == 0; // 移动时不能有炮架
        } else {
            return pieceCount == 1; // 吃子时必须有一个炮架
        }
    }
}

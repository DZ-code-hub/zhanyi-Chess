package com.zd.Entity;

import com.zd.Enum.Color;
import com.zd.Enum.PieceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Move {
    //id
    private Long id;
    //游戏id
    private Long gameId;

    //棋子当前位置
    private int fromX,fromY;
    //棋子被移动到的位置
    private int toX,toY;
    //被移动的棋子
    private Piece movedPiece;
    //被吃掉的棋子
    private Piece capturedPiece;
    //移动序号
    private int moveIndex;

    // 新增：用于数据库存储的字段

    private PieceType pieceType;

    private Color pieceColor;

    private PieceType capturedPieceType;

    private Color capturedPieceColor;


    public Move(int fromX, int fromY, int toX, int toY, Piece movedPiece, Piece capturedPiece) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
    }
}
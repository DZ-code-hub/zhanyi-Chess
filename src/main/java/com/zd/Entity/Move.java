package com.zd.Entity;

import lombok.Data;

@Data
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
}

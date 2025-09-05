package com.zd.Entity;

import com.zd.Enum.Color;
import com.zd.Enum.PieceType;
import lombok.Data;

@Data

public class Piece {
    //棋子颜色
    private Color color;
    //棋子类型
    private PieceType type;
    //棋子x坐标
    private int x;
    //棋子y坐标
    private int y;



}

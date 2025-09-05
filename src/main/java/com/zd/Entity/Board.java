package com.zd.Entity;

import lombok.Data;

import java.util.List;

@Data
public class Board {
    private int[][] grid = new int[9][10];
    private List<Move> moveHistory ;

}

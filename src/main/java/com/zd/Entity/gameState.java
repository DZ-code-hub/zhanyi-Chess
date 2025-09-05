package com.zd.Entity;

import com.zd.Enum.Color;
import lombok.Data;

@Data
public class gameState {
    //主键id
    private Long id;
    //会话id
    private String sessionId;
    //棋盘
    private Board board;
    //当前玩家（红/黑）
    private Color currentPlayer;
    //棋盘状态字符串
    private String boardState;
}

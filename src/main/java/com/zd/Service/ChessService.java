package com.zd.Service;

import com.zd.Entity.GameState;

public interface ChessService {
    //初始化游戏状态，即棋盘、棋子，设置阵营为红
    GameState initialize(String sessionId);

    GameState getGameState(String sessionId);
}

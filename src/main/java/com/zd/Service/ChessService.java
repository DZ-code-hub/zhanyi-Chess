package com.zd.Service;

import com.zd.Entity.GameState;
import com.zd.Entity.Move;

public interface ChessService {
    //初始化游戏状态，即棋盘、棋子，设置阵营为红
    GameState initialize(String sessionId);

    GameState getGameState(String sessionId);

    Move makeMove(String sessionId, int fromX, int fromY, int toX, int toY);

    boolean undoMove(String sessionId);
}

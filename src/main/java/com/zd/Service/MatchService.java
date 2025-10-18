package com.zd.Service;

import com.zd.Enum.Color;

import java.security.Principal;

public interface MatchService {
    //进入匹配队列
    String enqueue(String userId);
    //获取敌方玩家userId
    String getOpponent(String userId);
    //获取用户阵营
    Color getUserColor(String userId);
    //取消匹配
    void cancel(String userId);

    boolean leaveRoom(String gameId);
}

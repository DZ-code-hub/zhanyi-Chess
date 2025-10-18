package com.zd.Service;

import com.zd.Enum.Color;
import com.zd.Mapper.GameMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
@Slf4j
public class MathServiceImpl implements MatchService{
    public final Deque<String> queue = new ArrayDeque<>();
    public final Map<String,String> userToRoom = new HashMap<>();

    // 房间内红黑双方与用户颜色映射
    private final Map<String, String> roomRedUser = new HashMap<>();
    private final Map<String, String> roomBlackUser = new HashMap<>();
    public final Map<String,Color> userToColor = new HashMap<>();
    private final GameMapper gameMapper;

    public MathServiceImpl(GameMapper gameMapper) {
        this.gameMapper = gameMapper;
    }

    //进入队列
//    synchronized用于实现线程同步，确保在多线程环境下代码的线程安全性。
    public synchronized String enqueue(String userId){
        //如果在房间内，就直接返回房间
        if(userToRoom.containsKey(userId))
            return userToRoom.get(userId);

        //如果重复进入匹配队列，则只保留一个排队项
        queue.removeIf(u->u.equals(userId));

        // 入队
        queue.offerLast(userId);

        log.info("用户：{}入队",userId);

        // 至少需要两个不同用户才能配对
        if(queue.size() >= 2){
            //将最早入队的移除掉
            String a = queue.peekFirst();
            String b = null;

            for(String u : queue){
                if(!u.equals(a)){
                    b = u;
                    break;
                }
            }

            if(b != null){
                //将a和b从队列中移除
                queue.removeFirstOccurrence(a);
                queue.removeFirstOccurrence(b);
                //随机生成一个roomId
                String roomId = UUID.randomUUID().toString();

                userToRoom.put(a,roomId);
                userToRoom.put(b,roomId);

                roomRedUser.put(roomId,a);
                roomBlackUser.put(roomId,b);

                userToColor.put(a,Color.RED);
                userToColor.put(b,Color.BLACK);
                return roomId;
            }
        }

        return null;
    }

    @Override
    //查找对手userid
    public synchronized String getOpponent(String userId) {
       String room = userToRoom.get(userId);
       if(room == null)
           return null;

        for (Map.Entry<String,String> e : userToRoom.entrySet()) {
            if (!e.getKey().equals(userId) && room.equals(e.getValue())) return e.getKey();
        }
        return null;
    }

    @Override
    //获取该用户的阵营
    public synchronized Color getUserColor(String userId) {
        return userToColor.get(userId);
    }

    @Override
    //取消匹配，直接从匹配队列中移除
    public synchronized void cancel(String userId) {
        queue.removeIf(u -> u.equals(userId));
    }


    //删除gameState中对应session——id的数据
    @Override
    public boolean leaveRoom(String gameId) {
        if(gameId == null){
            log.info("gameId为空");
            return false;
        }
        //从数据库中删除这一局的游戏
        gameMapper.deleteGame(gameId);

        String a = roomRedUser.get(gameId);
        String b = roomBlackUser.get(gameId);

        //把Room中的两个玩家删除掉
        roomRedUser.remove(gameId);
        roomBlackUser.remove(gameId);

        userToRoom.remove(a);
        userToRoom.remove(b);

        userToColor.remove(a);
        userToColor.remove(b);
        return true;
    }

    public synchronized String getRedUser(String roomId) {
        return roomRedUser.get(roomId);
    }

    public synchronized String getBlackUser(String roomId) {
        return roomBlackUser.get(roomId);
    }
}

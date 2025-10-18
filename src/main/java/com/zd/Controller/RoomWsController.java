package com.zd.Controller;

import com.zd.Entity.GameState;
import com.zd.Entity.Move;
import com.zd.Enum.Color;
import com.zd.Service.ChessService;
import com.zd.Service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class RoomWsController {
    @Autowired
    ChessService chessService;
    @Autowired
    MatchService matchService;
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/room/{roomId}/move")
    //移动接口，一旦调用，广播给房间内的所有玩家
    public void move(@DestinationVariable String roomId,
                     @Payload Map<String, Object> body,
                     Principal principal) {
        //添加一个默认值0防止报错
        int fromX = (int) body.getOrDefault("fromX", 0);
        int fromY = (int) body.getOrDefault("fromY", 0);
        int toX = (int) body.getOrDefault("toX", 0);
        int toY = (int) body.getOrDefault("toY", 0);
        
        // 从消息体中获取mode和modePiece参数
        String mode = (String) body.getOrDefault("mode", "default");
        String modePiece = (String) body.getOrDefault("modePiece", null);
        
        // 添加日志输出
        log.info("WebSocket move - mode: {}, modePiece: {}", mode, modePiece);

        String userId = principal != null ? principal.getName() : null;
        GameState stateBefore = chessService.getGameState(roomId);
        Color playerColor = matchService.getUserColor(userId);

        Map<String, Object> reject = new HashMap<>();
        reject.put("type", "moveRejected");
        reject.put("roomId", roomId);

        if (userId == null || playerColor == null) {
            reject.put("message", "未分配阵营或未登录");
            messagingTemplate.convertAndSend("/topic/room/" + roomId, reject);
            return;
        }

        //回合校验
        if (stateBefore == null || stateBefore.getCurrentPlayer() != playerColor) {
            reject.put("message", "未到你的回合");
            messagingTemplate.convertAndSend("/topic/room/" + roomId, reject);
            return;
        }

        // 棋子颜色校验
        if (stateBefore.getBoard() == null || stateBefore.getBoard().getPiece(fromX, fromY) == null
                || stateBefore.getBoard().getPiece(fromX, fromY).getColor() != playerColor) {
            reject.put("message", "只能移动己方棋子");
            messagingTemplate.convertAndSend("/topic/room/" + roomId, reject);
            return;
        }

        //执行移动，拿到本次移动信息
        Move move = chessService.makeMove(roomId, fromX, fromY, toX, toY,mode, modePiece);
        //广播到房间内所有用户
        GameState state = chessService.getGameState(roomId);

        Map<String, Object> evt = new HashMap<>();
        evt.put("type", "moveMade");
        evt.put("roomId", roomId);
        evt.put("gameState", state);
        // 将军信息下发（makeMove 内已在交换回合前检查并写入 move.isCheck）
        if (move != null && move.isCheck()) {
            Color current = state != null ? state.getCurrentPlayer() : null;
            evt.put("isCheck", true);
            evt.put("checkMessage", current == Color.RED ? "红方被将军！" : "黑方被将军！");
        } else {
            evt.put("isCheck", false);
        }
        messagingTemplate.convertAndSend("/topic/room/" + roomId, evt);
    }
  //匹配中悔棋
    @MessageMapping("/room/{roomId}/undo")
    public void undo(@DestinationVariable String roomId) {
        boolean ok = chessService.undoMove(roomId);

        Map<String, Object> evt = new HashMap<>();
        if (ok) {
            GameState state = chessService.getGameState(roomId);
            evt.put("type", "undoApplied");
            evt.put("roomId", roomId);
            evt.put("gameState", state);
            messagingTemplate.convertAndSend("/topic/room/" + roomId, evt);
        } else {
            evt.put("type", "undoRejected");
            evt.put("roomId", roomId);
            evt.put("message", "没有可悔棋的记录或悔棋失败");
            messagingTemplate.convertAndSend("/topic/room/" + roomId, evt);
        }
    }

    //退出房间，即断开ws连接
    //路径
    @MessageMapping("/room/{roomId}/leave")
    public  Map<String,Object> tuiChuRoom(Principal principal){
        Map<String, Object> resp = new HashMap<>();
        if (principal == null) {
            resp.put("success", false);
            resp.put("message", "未登录");
            return resp;
        }

        String gameId = principal.getName();

        boolean success = matchService.leaveRoom(gameId);
        if(!success){
            resp.put("success",false);
            resp.put("message","退出房间失败");
        }
        else{
            resp.put("success",true);
            resp.put("message","退出房间成功");
        }
        return resp;
    }




}

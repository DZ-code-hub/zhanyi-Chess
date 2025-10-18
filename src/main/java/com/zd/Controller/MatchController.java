package com.zd.Controller;

import com.zd.Enum.Color;
import com.zd.Service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/match")
@Slf4j
public class MatchController {
    @Autowired
    private  MatchService matchService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @PostMapping(value = "/queue", produces = "application/json;charset=UTF-8")
    @ResponseBody
    //开始匹配接口
    public Map<String,Object> queue(Principal principal,
                                    @RequestParam(value = "clientId",required = false)String clientId){
        //获取clientId，若principal为空则使用clientId，若clientId为空则随机生成一个
        String userId = principal != null ? principal.getName() :
                        (clientId != null ? clientId : ("http-" +UUID.randomUUID()));
        log.info("userId:{}",userId);

        String roomId = matchService.enqueue(userId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("enqueued", true);

        if (roomId != null) {
            // 通知两位玩家配对成功
            String opponent = matchService.getOpponent(userId);
            // 带上每个用户的阵营
            Color myColor = matchService.getUserColor(userId);
            messagingTemplate.convertAndSendToUser(userId, "/queue/events", eventWithColor("matchFound", roomId, myColor));
            if (opponent != null) {
                Color oppColor = matchService.getUserColor(opponent);
                messagingTemplate.convertAndSendToUser(opponent, "/queue/events", eventWithColor("matchFound", roomId, oppColor));
            }
            resp.put("roomId", roomId);
        }
        log.info("没有进入房间");
        return resp;
    }

    @PostMapping(value = "/cancel", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> cancel(Principal principal, @org.springframework.web.bind.annotation.RequestParam(value = "clientId", required = false) String clientId) {
        String userId = principal != null ? principal.getName() : (clientId != null ? clientId : ("http-" + java.util.UUID.randomUUID()));
        matchService.cancel(userId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("canceled", true);
        log.info("取消匹配成功");
        return resp;
    }

    private Map<String, Object> event(String type, String roomId) {
        Map<String, Object> e = new HashMap<>();
        e.put("type", type);
        e.put("roomId", roomId);
        return e;
    }

    private Map<String, Object> eventWithColor(String type, String roomId, Color color) {
        Map<String, Object> e = event(type, roomId);
        if (color != null) {
            e.put("yourColor", color.name());
        }
        return e;
    }

    @GetMapping(value = "/my-color", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> myColor(Principal principal) {
        Map<String, Object> resp = new HashMap<>();
        if (principal == null) {
            resp.put("success", false);
            resp.put("message", "未登录");
            return resp;
        }

        Color color = matchService.getUserColor(principal.getName());
        if (color == null) {
            resp.put("success", false);
            resp.put("message", "未分配阵营");
        } else {
            resp.put("success", true);
            resp.put("yourColor", color.name());
        }
        return resp;
    }





}

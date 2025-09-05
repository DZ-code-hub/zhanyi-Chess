package com.zd.Controller;

import com.zd.Entity.GameState;
import com.zd.Service.ChessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/chess")
public class ChessController {

    @Autowired
    ChessService chessService;
    //初始化游戏
    @PostMapping("/initialize")
    public ResponseEntity<Map<String,Object>> initializeGame(@RequestParam String sessionId){
        log.info("进入单人练习模式，sessionId为:{}",sessionId);

        GameState gameState = chessService.initialize(sessionId);

        Map<String,Object> response = new HashMap<>();
        response.put("success",gameState!= null);
        response.put("gameState",chessService.getGameState(sessionId));

        log.info("初始化游戏success = ：{}",gameState != null);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/state")
    public ResponseEntity<Map<String,Object>> getGameState(@RequestParam String sessionId){
        GameState gameState = chessService.getGameState(sessionId);
        Map<String,Object> response = new HashMap<>();
        response.put("success",gameState!= null);
        response.put("gameState",gameState);

        log.info("获取游戏状态success = ：{}",gameState != null);

        return ResponseEntity.ok(response);

    }

}

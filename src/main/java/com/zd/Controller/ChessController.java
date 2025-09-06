package com.zd.Controller;

import com.zd.Entity.Board;
import com.zd.Entity.GameState;
import com.zd.Entity.Move;
import com.zd.Entity.Piece;
import com.zd.Enum.Color;
import com.zd.Mapper.MoveMapper;
import com.zd.Rule.RuleEngine;
import com.zd.Service.ChessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.FlashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/chess")
public class ChessController {

    @Autowired
    ChessService chessService;
    @Autowired
    RuleEngine ruleEngine;
    @Autowired
    MoveMapper moveMapper;

    //初始化游戏
    @PostMapping("/initialize")
    public ResponseEntity<Map<String, Object>> initializeGame(@RequestParam String sessionId) {
        log.info("进入单人练习模式，sessionId为:{}", sessionId);

        GameState gameState = chessService.initialize(sessionId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", gameState != null);
        response.put("gameState", chessService.getGameState(sessionId));

        log.info("初始化游戏success = ：{}", gameState != null);

        return ResponseEntity.ok(response);

    }

    //获取游戏状态
    @GetMapping("/state")
    public ResponseEntity<Map<String, Object>> getGameState(@RequestParam String sessionId) {
        GameState gameState = chessService.getGameState(sessionId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", gameState != null);
        response.put("gameState", gameState);

        log.info("获取游戏状态success = ：{}", gameState != null);

        return ResponseEntity.ok(response);

    }

    //判断合法的移动位置
    @GetMapping("/valid-moves")
    public ResponseEntity<Map<String, Object>> getValidMoves(
            @RequestParam String sessionId,
            @RequestParam int fromX,
            @RequestParam int fromY) {
        GameState gameState = chessService.getGameState(sessionId);
        Map<String, Object> response = new HashMap<>();
        //判断棋盘状态和游戏状态
        if (gameState == null || gameState.getBoard() == null) {
            response.put("success", false);
            response.put("moves", new int[0]);
            return ResponseEntity.ok(response);
        }

        //判断游戏是否结束
        String over = ruleEngine.checkWinner(gameState);
        if (over != null) {
            response.put("success", true);
            response.put("result", over);
            response.put("moves", new int[0]);
            return ResponseEntity.ok(response);
        }

        //判断是否为当前回合方棋子
        Piece piece = gameState.getBoard().getPiece(fromX, fromY);
        if (piece == null || piece.getColor() != gameState.getCurrentPlayer()) {
            response.put("success", true);
            response.put("moves", new int[0]);
            return ResponseEntity.ok(response);
        }

        //创建可移动位置的坐标集合
        List<int[]> moves = new ArrayList<>();
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 9; x++) {
                if (ruleEngine.isValidMove(gameState.getBoard(), fromX, fromY, x, y)) {
                    //如果位置合法，就添加进集合
                    moves.add(new int[]{x, y});
                }
            }
        }
        response.put("success", true);
        response.put("moves", moves);
        return ResponseEntity.ok(response);

    }

    //移动棋子方法
    @PostMapping("/move")
    public ResponseEntity<Map<String, Object>> makeMove(
            @RequestParam String sessionId,
            @RequestParam int fromX,
            @RequestParam int fromY,
            @RequestParam int toX,
            @RequestParam int toY) {


        Move move = chessService.makeMove(sessionId, fromX, fromY, toX, toY);
        GameState gameState = chessService.getGameState(sessionId);
        log.info("move:{}", move);

        Map<String, Object> response = new HashMap<>();
        if (move != null) {
            response.put("success", true);
            response.put("move", move);
            response.put("gameState", gameState);
            log.info("gameState为:{}", gameState);

            // 胜负判定：检查是否还有红帅或黑将
            String result = ruleEngine.checkWinner(gameState);
            if (result != null) {
                response.put("result", result);
            }
            // 从Move对象中获取将军状态（在交换回合之前已经检查过）
            boolean isCheck = move.isCheck();
            
            if (isCheck) {
                // 由于已经交换了回合，当前玩家是被将军的一方
                Color currentPlayer = gameState.getCurrentPlayer();
                response.put("isCheck", true);
                response.put("checkMessage", currentPlayer == Color.RED ? "红方被将军！" : "黑方被将军！");
            } else {
                response.put("isCheck", false);
            }


        } else {
            response.put("success", false);
            response.put("message", "Invalid move");
            response.put("isCheck", false);
        }

        return ResponseEntity.ok(response);
    }

    //前端调用悔棋接口
    @PostMapping("/undo")
    public ResponseEntity<Map<String, Object>> undoMove(@RequestParam String sessionId) {
        boolean success = chessService.undoMove(sessionId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        //判断是否悔棋成功并传回相应参数
        if (success) {
            response.put("message", "悔棋成功");
            response.put("gameState", chessService.getGameState(sessionId));
        } else {
            response.put("message", "悔棋失败");
            response.put("gameState", chessService.getGameState(sessionId));
        }
        return ResponseEntity.ok(response);
    }

}

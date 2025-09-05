package com.zd.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zd.Enum.Color;


public class GameState {
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

    public GameState() {
        this.board = new Board();
        this.currentPlayer = Color.RED;
    }

    public GameState(String sessionId) {
        this();
        this.sessionId = sessionId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @JsonIgnore
    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board;
    }

    // 前端期望的二维数组结构：gameState.board[y][x]
    @JsonProperty("board")
    public Piece[][] getBoardArray() {
        if (board == null || board.getGrid() == null) {
            return null;
        }
        // 将内部 grid[x][y] 转成前端期望的 [y][x]
        Piece[][] gridXY = board.getGrid();
        Piece[][] boardYX = new Piece[10][9];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 10; y++) {
                boardYX[y][x] = gridXY[x][y];
            }
        }
        return boardYX;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(Color currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    // 新增：获取棋盘状态字符串
    public String getBoardState() {
        if (board != null) {
            this.boardState = board.serialize();
        }
        return boardState;
    }

    // 新增：设置棋盘状态字符串
    public void setBoardState(String boardState) {
        this.boardState = boardState;
        if (board != null && boardState != null && !boardState.isEmpty()) {
            board.deserialize(boardState);
        }
    }

    //交换回合
    public void switchPlayer() {
        currentPlayer = (currentPlayer == Color.RED) ? Color.BLACK : Color.RED;
    }
}

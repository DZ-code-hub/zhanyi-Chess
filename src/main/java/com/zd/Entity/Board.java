package com.zd.Entity;

import com.zd.Enum.Color;
import com.zd.Enum.PieceType;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

@Data
public class Board {
    //生成二维数组表示棋盘坐标（统一为 grid[x][y]：x 列 [0..8]，y 行 [0..9]）
    private Piece[][] grid = new Piece[9][10];
    //生成一个列表存放棋子的移动历史，以便悔棋
    private List<Move> moveHistory ;

    //初始化棋盘,向默认的位置添加棋子对象
    public void initializeBoard(){
        // 使用 grid[x][y]
        // 红方
        grid[0][0] = new Piece(PieceType.CHE, Color.RED, 0, 0);
        grid[1][0] = new Piece(PieceType.MA, Color.RED, 1, 0);
        grid[2][0] = new Piece(PieceType.XIANG, Color.RED, 2, 0);
        grid[3][0] = new Piece(PieceType.SHI, Color.RED, 3, 0);
        grid[4][0] = new Piece(PieceType.BOSS, Color.RED, 4, 0);
        grid[5][0] = new Piece(PieceType.SHI, Color.RED, 5, 0);
        grid[6][0] = new Piece(PieceType.XIANG, Color.RED, 6, 0);
        grid[7][0] = new Piece(PieceType.MA, Color.RED, 7, 0);
        grid[8][0] = new Piece(PieceType.CHE, Color.RED, 8, 0);
        grid[1][2] = new Piece(PieceType.PAO, Color.RED, 1, 2);
        grid[7][2] = new Piece(PieceType.PAO, Color.RED, 7, 2);
        grid[0][3] = new Piece(PieceType.BING, Color.RED, 0, 3);
        grid[2][3] = new Piece(PieceType.BING, Color.RED, 2, 3);
        grid[4][3] = new Piece(PieceType.BING, Color.RED, 4, 3);
        grid[6][3] = new Piece(PieceType.BING, Color.RED, 6, 3);
        grid[8][3] = new Piece(PieceType.BING, Color.RED, 8, 3);

        // 黑方
        grid[0][9] = new Piece(PieceType.CHE, Color.BLACK, 0, 9);
        grid[1][9] = new Piece(PieceType.MA, Color.BLACK, 1, 9);
        grid[2][9] = new Piece(PieceType.XIANG, Color.BLACK, 2, 9);
        grid[3][9] = new Piece(PieceType.SHI, Color.BLACK, 3, 9);
        grid[4][9] = new Piece(PieceType.BOSS, Color.BLACK, 4, 9);
        grid[5][9] = new Piece(PieceType.SHI, Color.BLACK, 5, 9);
        grid[6][9] = new Piece(PieceType.XIANG, Color.BLACK, 6, 9);
        grid[7][9] = new Piece(PieceType.MA, Color.BLACK, 7, 9);
        grid[8][9] = new Piece(PieceType.CHE, Color.BLACK, 8, 9);
        grid[1][7] = new Piece(PieceType.PAO, Color.BLACK, 1, 7);
        grid[7][7] = new Piece(PieceType.PAO, Color.BLACK, 7, 7);
        grid[0][6] = new Piece(PieceType.BING, Color.BLACK, 0, 6);
        grid[2][6] = new Piece(PieceType.BING, Color.BLACK, 2, 6);
        grid[4][6] = new Piece(PieceType.BING, Color.BLACK, 4, 6);
        grid[6][6] = new Piece(PieceType.BING, Color.BLACK, 6, 6);
        grid[8][6] = new Piece(PieceType.BING, Color.BLACK, 8, 6);
    }
    //通过坐标获取棋子对象
    public Piece getPiece(int x, int y){
        if( x >= 0 && x < 9 && y >= 0 && y < 10){
            return grid[x][y];
        }
        return null;
    }
    //设置棋子
    public void setPiece(int x,int y,Piece piece){
        if( x >= 0 && x < 9 && y >= 0 && y < 10){
            grid[x][y] = piece;
            if(piece != null){
                piece.setX(x);
                piece.setY(y);
            }
        }
    }

    //移动棋子
    public void movePiece(int fromX,int fromY,int toX,int toY){
        //根据坐标获得棋子对象
        Piece piece = getPiece(fromX,fromY);
        if(piece != null){
            //将棋子移动到指定地方
            setPiece(toX,toY,piece);
            //原位置设置为空
            setPiece(fromX,fromY,null);
        }
    }

    //将移动内容添加到移动历史中
    public void addMoveToHistory(Move move){
        moveHistory.add(move);
    }

    //从移动历史中移除最后一步，即悔棋后后撤一步
    public Move removeLastMoveFromHistory() {
        if (!moveHistory.isEmpty()) {
            return moveHistory.remove(moveHistory.size() - 1);
        }
        return null;
    }
    //获取移动历史
    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    // 新增：用于序列化给前端的二维数组访问
    public Piece[][] getGrid() {
        return grid;
    }

    //将棋盘状态序列化为json格式字符串
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"pieces\":[");

        boolean firstPiece = true;
        // 遍历 grid[x][y]
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 10; y++) {
                Piece piece = grid[x][y];
                if (piece != null) {
                    if (!firstPiece) {
                        sb.append(",");
                    }
                    sb.append("{\"x\":").append(x)
                            .append(",\"y\":").append(y)
                            .append(",\"type\":\"").append(piece.getType()).append("\"")
                            .append(",\"color\":\"").append(piece.getColor()).append("\"}");
                    firstPiece = false;
                }
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    //从json字符串反序列化棋盘状态
    public void deserialize(String data) {
        // 从JSON字符串反序列化棋盘状态
        if (data == null || data.isEmpty()) {
            initializeBoard();
            return;
        }

        // 清空棋盘（grid[x][y]）
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 10; y++) {
                grid[x][y] = null;
            }
        }

        try {
            // 解析JSON数据格式: {"pieces":[{"x":0,"y":0,"type":"CHE","color":"RED"},...]}
            JSONObject json = new JSONObject(data);
            JSONArray pieces = json.getJSONArray("pieces");

            for (int i = 0; i < pieces.length(); i++) {
                JSONObject pieceJson = pieces.getJSONObject(i);
                int x = pieceJson.getInt("x");
                int y = pieceJson.getInt("y");
                PieceType type = PieceType.valueOf(pieceJson.getString("type"));
                Color color = Color.valueOf(pieceJson.getString("color"));

                // 确保坐标在有效范围内
                if (x >= 0 && x < 9 && y >= 0 && y < 10) {
                    Piece piece = new Piece(type, color, x, y);
                    grid[x][y] = piece;
                }
            }
        } catch (Exception e) {
            // 如果解析失败，初始化默认棋盘
            System.err.println("棋盘反序列化失败: " + e.getMessage());
            initializeBoard();
        }
    }


}

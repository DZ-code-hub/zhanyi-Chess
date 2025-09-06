package com.zd.Mapper;

import com.zd.Entity.Move;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MoveMapper {


    //通过gameId查询移动记录的数量
    @Select("select count(*) from move_history where game_id = #{gameId}")
     int countMovesByGameId(Long gameId);

    //通过gameId删除移动记录
    @Delete("delete from move_history where game_id = #{gameId}")
    void deleteMovesByGameId(Long gameId);

    //插入移动记录
    @Insert("INSERT INTO move_history(game_id, from_x, from_y, to_x, to_y, " +
            "piece_type, piece_color, captured_piece_type, captured_piece_color, move_index) " +
            "VALUES(#{gameId}, #{fromX}, #{fromY}, #{toX}, #{toY}, " +
            "#{pieceType}, #{pieceColor}, #{capturedPieceType}, #{capturedPieceColor}, #{moveIndex})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertMove(Move move);

    //获取最后一条移动数据
    @Select("SELECT * FROM move_history WHERE game_id = #{gameId} ORDER BY move_index DESC LIMIT 1")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "gameId", column = "game_id"),
            @Result(property = "fromX", column = "from_x"),
            @Result(property = "fromY", column = "from_y"),
            @Result(property = "toX", column = "to_x"),
            @Result(property = "toY", column = "to_y"),
            @Result(property = "pieceType", column = "piece_type", javaType = com.zd.Enum.PieceType.class),
            @Result(property = "pieceColor", column = "piece_color", javaType = com.zd.Enum.Color.class),
            @Result(property = "capturedPieceType", column = "captured_piece_type", javaType = com.zd.Enum.PieceType.class),
            @Result(property = "capturedPieceColor", column = "captured_piece_color", javaType = com.zd.Enum.Color.class),
            @Result(property = "moveIndex", column = "move_index")
    })
     Move getLastMove(Long gameId);

    //获取最后一条移记录的最大索引
    @Select("select max(move_index) from move_history where game_id = #{gameId}")

    Integer getMaxIndex(Long gameId);
}

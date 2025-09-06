package com.zd.Mapper;

import com.zd.Entity.GameState;
import org.apache.ibatis.annotations.*;

@Mapper
public interface GameMapper {

    @Select("select id,session_id,board_state,current_player from game_state where session_id = #{sessionId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "sessionId", column = "session_id"),
            @Result(property = "boardState", column = "board_state"),
            @Result(property = "currentPlayer", column = "current_player",
                    typeHandler = org.apache.ibatis.type.EnumTypeHandler.class)
    })
    GameState selectBySessionId(String sessionId);
    @Delete("delete from game_state where session_id = #{sessionId}")
    void deleteGame(String sessionId);

    @Insert("INSERT INTO game_state(session_id, board_state, current_player) " +
            "VALUES(#{sessionId}, #{boardState}, #{currentPlayer})" )
    void save(GameState gameState);

    @Update("update game_state set board_state = #{boardState},current_player = #{currentPlayer}" +
            " WHERE session_id = #{sessionId}")
    void updateGame(GameState gameState);
}

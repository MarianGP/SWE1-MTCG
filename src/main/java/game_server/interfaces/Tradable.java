package game_server.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;

public interface Tradable {
    String tradeCards(String wantedCardId, String offeredCardId, String username);
    String addNewTrade(String json, String username) throws JsonProcessingException, SQLException;
}

package game_server.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;

public interface RequestHandling {

    void handlePath() throws JsonProcessingException, SQLException;
    void signUp(String requestBody, String token) throws JsonProcessingException, SQLException;
    void singIn(String requestBody) throws JsonProcessingException;

    void showUserCards(String token);
    void showDeckCards(boolean formatJson);

    void tradeCards(String wantedCardId, String requestBody);
    void addTrade(String requestBody, String username) throws JsonProcessingException, SQLException;
    void showTrades();

    void startBattle(String token);
    void getScoreBoard();
}

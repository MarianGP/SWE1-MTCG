package game_server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.decks.CardStack;
import game.user.User;
import game_server.db.DbConnection;
import game_server.enums.HttpMethod;
import game_server.enums.StatusCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class RequestHandlerTest {
    @Mock
    String userJson = "{\"Username\":\"kienboec\", \"Password\":\"daniel\"}";
    DbConnection db = new DbConnection();

    User user = User.builder()
            .username("JohnDoe")
            .password("1234")
            .token("JohnDoe" + "-mtcgToken")
            .bio(":/")
            .image(":/")
            .coins(20)
            .elo(100)
            .stack(new CardStack())
            .build();

//    @Test
//    @DisplayName("POST: create user")
//    void testHandleUser() throws JsonProcessingException {
//        RequestHandler requestHandler = getHandler(HttpMethod.POST, userJson, "/users");
//        requestHandler.handleUser(userJson, HttpMethod.POST);
//        Assertions.assertTrue(requestHandler.getUserController().getAllUsers().containsKey("kienboec"));
//    }

    @Test
    @DisplayName("Show Scoreboard")
    void testScoreBoard() throws JsonProcessingException, SQLException {
        RequestHandler requestHandler =
                getHandler(HttpMethod.GET, "", "", "http://localhost:10001/score");
        requestHandler.loggedUserAction("score");
        System.out.println(requestHandler.getResponseBody());
    }

    @Test
    @DisplayName("Login")
    void testLogin() throws JsonProcessingException {
        String json = "{\n" +
                "    \"username\": \"marian\",\n" +
                "    \"password\": \"1234\"\n" +
                "}";
        RequestHandler requestHandler =
                getHandler(HttpMethod.POST, "", "", "http://localhost:10001/sessions");
        System.out.println(RequestHandler.getCredentials(json));
    }

    @Test
    void testJsonToClass() {
    }

    @Test
    void TestGetQueryMap() {
    }

    public RequestHandler getHandler(HttpMethod method, String authorization, String body, String path) {
        ArrayList<String> header = new ArrayList<>(
                List.of(
                        method + " " + path + " HTTP/1.1",
                        "Content-Type: text/plain",
                        "User-Agent: PostmanRuntime/7.26.5",
                        "Authorization: " + authorization,
                        "Accept: */*",
                        "Postman-Token: 2e0548fe-26e4-4c7e-9952-2d3e54ad8ed6",
                        "Host: 127.0.0.1:8000",
                        "Accept-Encoding: gzip, deflate, br",
                        "Connection: keep-alive",
                        "Content-Length: " + body.length(),
                        ""
                )
        );

        //mock requestContext
        HttpRequest requestContext = new HttpRequest(header);
        requestContext.setBody(body);

        return RequestHandler.builder()
                .requestContext(requestContext)
                .responseStatus(StatusCode.OK)
                .db(new DbConnection())
                .formatJson(true)
                .build();
    }
}
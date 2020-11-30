package game_server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import game_server.controller.UserController;
import game_server.enums.HttpMethod;
import game_server.enums.StatusCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

class RequestHandlerTest {
    @Mock
    String userJson = "{\"Username\":\"kienboec\", \"Password\":\"daniel\"}";


    @Test
    @DisplayName("POST: create user")
    void testHandleUser() throws JsonProcessingException {
        RequestHandler requestHandler = getHandler(HttpMethod.POST, userJson, "/users");
        requestHandler.handleUser(userJson, HttpMethod.POST);
        Assertions.assertTrue(requestHandler.getUserController().getAllUsers().containsKey("kienboec"));
    }

    @Test
    void testJsonToClass() {
    }

    @Test
    void TestGetQueryMap() {
    }


    public RequestHandler getHandler(HttpMethod method, String body, String path) {
        ArrayList<String> header = new ArrayList<>(
                List.of(
                        method + " " + path + " HTTP/1.1",
                        "Content-Type: text/plain",
                        "User-Agent: PostmanRuntime/7.26.5",
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
                .status(StatusCode.OK)
                .objectName("")
                .userController(new UserController())
                .build();
    }
}
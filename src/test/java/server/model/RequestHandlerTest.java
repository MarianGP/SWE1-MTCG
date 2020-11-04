package server.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.enums.HttpMethod;
import server.enums.StatusCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class RequestHandlerTest {
    @Mock
    RequestHandler requestHandler;

    @Test
    @DisplayName("check if message exists")
    public void checkIfOneMessageExists() {

        requestHandler = getHandler(HttpMethod.GET, "", "/messages");
        requestHandler.splitURL();

        Assertions.assertEquals(
                "The message exits",
                requestHandler.checkIfMessageExists(1, "1")
        );

        Assertions.assertEquals(
                "The message does not exist",
                requestHandler.checkIfMessageExists(1, "999")
        );

        Assertions.assertEquals(
                "The message list is empty",
                requestHandler.checkIfMessageExists(0, "1")
        );
    }

    @Test
    @DisplayName("GET Message: Exits && does not Exist")
    public void getMessage() {
        requestHandler = getHandler(HttpMethod.GET, "", "/messages");
        requestHandler.splitURL();
        Assertions.assertEquals(
                "Msg1",
                requestHandler.crudMessage("1")
        );

        Assertions.assertNull(requestHandler.crudMessage("999"));
    }

    @Test
    @DisplayName("DELETE - Edit Message: With body / With empty body")
    public void deleteMessage() {
        requestHandler = getHandler(HttpMethod.DELETE, "", "/messages/1");
        requestHandler.splitURL();

        Assertions.assertEquals(
                "The message was deleted",
                requestHandler.crudMessage("2")
        );

        requestHandler.getRequestContext().setMethod(HttpMethod.GET);
        Assertions.assertNull(requestHandler.crudMessage("2"));
    }

    @Test
    @DisplayName("PUT - Edit Message: With body / With empty body")
    public void updateMessage() {
        requestHandler = getHandler(HttpMethod.PUT, "New Msg2", "/messages/1");
        requestHandler.splitURL();

        Assertions.assertEquals(
                "The message was modified",
                requestHandler.crudMessage("2")
        );

        requestHandler.getRequestContext().setMethod(HttpMethod.GET);
        Assertions.assertEquals(
                "New Msg2",
                requestHandler.crudMessage("2")
        );
    }

    @Test
    @DisplayName("POST - Edit Message: With body / With empty body")
    public void addMessage() {
        requestHandler = getHandler(HttpMethod.POST, "Msg3", "/messages");
        requestHandler.splitURL();

        Assertions.assertEquals(
                "New message was created",
                requestHandler.addNewMessage()
        );

        requestHandler.getRequestContext().setMethod(HttpMethod.GET);
        Assertions.assertEquals(
                "New Msg2",
                requestHandler.crudMessage("2")
        );
    }

    @Test
    @DisplayName("Get all msg")
    public void getALL() {
        requestHandler = getHandler(HttpMethod.GET, "", "/messages");
        requestHandler.splitURL();

        Assertions.assertEquals(
                "1)  Msg1\r\n" +
                        "2)  Msg2\r\n",
                requestHandler.getAllMessages()
        );

    }

    @Test
    @DisplayName("No ")
    public void lalala() {
        requestHandler = getHandler(HttpMethod.GET, "", "/messages");
        requestHandler.splitURL();

        Assertions.assertEquals(
                "1)  Msg1\r\n" +
                        "2)  Msg2\r\n",
                requestHandler.getAllMessages()
        );

    }

    @Test
    @DisplayName("Error Handling")
    public void errorHandling() {
        requestHandler = getHandler(HttpMethod.GET, "", "/messages");
        requestHandler.splitURL();

        Assertions.assertEquals(
                "1)  Msg1\r\n" +
                        "2)  Msg2\r\n",
                requestHandler.getAllMessages()
        );

    }




    public RequestHandler getHandler(HttpMethod method, String body, String path) {
        Map<String, String> messages = new HashMap<>();
        messages.put("1", "Msg1");
        messages.put("2", "Msg2");

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

        HttpRequest requestContext = new HttpRequest(header);

//        Integer bodyLength = Integer.parseInt(requestContext.getBodyLength(), 10);
        requestContext.setBody(body);

        return RequestHandler.builder()
                .requestContext(requestContext)
                .status(StatusCode.OK)
                .objectsList(messages)
                .objectName("")
                .build();

    }
}





package server.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.enums.HttpMethod;
import server.enums.StatusCode;

import java.util.*;


class RequestHandlerTest {
    @Mock
    RequestHandler requestHandler;

    @Test
    @DisplayName("GET - getHandler() - exits, doesn't exist, list empty")
    public void checkIfOneMessageExists() {

        //arrange
        requestHandler = getHandler(HttpMethod.GET, "", "/messages"); //mock
        requestHandler.splitURL();

        //assert
        // * msg exists
        Assertions.assertEquals(
                "The message exits",
                requestHandler.checkIfMessageExists(1, "1")
        );

        // * get msg 1
        Assertions.assertEquals(
                "Msg1",
                requestHandler.handleOneMessage("1")
        );

        // * msg doesn't exist
        Assertions.assertEquals(
                "The message does not exist",
                requestHandler.checkIfMessageExists(1, "999")
        );

        // * msg doesn't exist
        Assertions.assertNull(requestHandler.handleOneMessage("999"));


        // * list size 0, list is empty
        Assertions.assertEquals(
                "The message list is empty",
                requestHandler.checkIfMessageExists(0, "1")
        );
    }

    @Test
    @DisplayName("DELETE - crudMessage() - Delete Message")
    public void deleteMessage() {
        requestHandler = getHandler(HttpMethod.DELETE, "", "/messages/1");
        requestHandler.splitURL();

        Assertions.assertEquals(
                "The message was deleted",
                requestHandler.handleOneMessage("2")
        );

        requestHandler.getRequestContext().setMethod(HttpMethod.GET);
        Assertions.assertNull(requestHandler.handleOneMessage("2"));
    }

    @Test
    @DisplayName("PUT - crudMessage() Edit Message: With body / With empty body")
    public void updateMessage() {
        requestHandler = getHandler(HttpMethod.PUT, "New Msg2", "/messages/1");
        requestHandler.splitURL();

        Assertions.assertEquals(
                "The message was modified",
                requestHandler.handleOneMessage("2")
        );

        requestHandler.getRequestContext().setMethod(HttpMethod.GET);
        Assertions.assertEquals(
                "New Msg2",
                requestHandler.handleOneMessage("2")
        );

        requestHandler = getHandler(HttpMethod.PUT, "", "/messages/1");
        requestHandler.splitURL();
        requestHandler.handleOneMessage("2");

        requestHandler.getRequestContext().setMethod(HttpMethod.GET);
        Assertions.assertNull(requestHandler.handleOneMessage("")
        );


    }

    @Test
    @DisplayName("POST - Add Message - requestHandler.addNewMessage()")
    public void addMessage() {
        requestHandler = getHandler(HttpMethod.POST, "Msg3", "/messages");
        requestHandler.splitURL();

        Assertions.assertEquals(
                "New message was created",
                requestHandler.addNewMessage()
        );

        requestHandler.getRequestContext().setMethod(HttpMethod.GET);
        Assertions.assertEquals(
                "Msg3",
                requestHandler.handleOneMessage("3")
        );
    }

    @Test
    @DisplayName("GET ALL - requestHandler.getAllMessages()")
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
    @DisplayName("Error Handling: NOTSUPPORTED, BADREQUEST, NOCONTENT")
    public void errorHandling() {
        requestHandler = getHandler(HttpMethod.NOTSUPPORTED, "", "/messages/1asd");
        requestHandler.splitURL();

        requestHandler.handleMessages();
        Assertions.assertEquals(
                StatusCode.BADREQUEST,
                requestHandler.getStatus()
        );

        requestHandler.handleOneMessage("1");
        Assertions.assertEquals(
                StatusCode.BADREQUEST,
                requestHandler.getStatus()
        );

        requestHandler.getRequestContext().setMethod(HttpMethod.GET);
        Assertions.assertEquals(
                "The message list is empty",
                requestHandler.checkIfMessageExists(0, "1")
        );

        Assertions.assertEquals(
                StatusCode.NOCONTENT,
                requestHandler.getStatus()
        );

        Assertions.assertEquals(
                "Usage: URL/table OR URL/table/{number}. Only existing tables allowed",
                requestHandler.getResponseBody()
        );

        Assertions.assertEquals(
                StatusCode.BADREQUEST,
                requestHandler.getStatus()
        );

    }

    @Test
    @DisplayName("Split Path: Invalid Path")
    public void splitPath() {
        AbstractMap.SimpleEntry<String, String> testPair;

        requestHandler = getHandler(HttpMethod.GET, "", "/messages/1www");
        requestHandler.splitURL();
        testPair = new AbstractMap.SimpleEntry<>("1www", "messages");

        Assertions.assertEquals(
                testPair,
                requestHandler.getPathPair()
        );

        Assertions.assertFalse(
                requestHandler.getRequestContext().getPath().matches(
                        "(/" + requestHandler.getPathPair().getValue() + "/)([0-9]+)(/?)"));

        requestHandler = getHandler(HttpMethod.GET, "", "/");
        requestHandler.splitURL();

        Assertions.assertNull(requestHandler.getPathPair());

        requestHandler = getHandler(HttpMethod.GET, "", "/messages/1");
        requestHandler.splitURL();
        testPair = new AbstractMap.SimpleEntry<>("1", "messages");

        Assertions.assertEquals(
                testPair,
                requestHandler.getPathPair()
        );

        requestHandler = getHandler(HttpMethod.GET, "", "/messages/1/6");
        requestHandler.splitURL();

        Assertions.assertNull(requestHandler.getPathPair());

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

        //mock requestContext
        HttpRequest requestContext = new HttpRequest(header);

        requestContext.setBody(body);

        return RequestHandler.builder()
                .requestContext(requestContext)
                .status(StatusCode.OK)
                .objectsList(messages)
                .objectName("")
                .build();
    }
}





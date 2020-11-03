package server.model;

import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import server.enums.HttpMethod;
import server.enums.StatusCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Builder
@RunWith(MockitoJUnitRunner.class)
class RequestHandlerTest {

    public HttpRequest requestContext;
    public RequestHandler requestHandler;
    public Map<String, String> messages;
    HttpMethod get;
    HttpMethod post;
    HttpMethod put;
    HttpMethod delete;

    @BeforeEach
    void Setup() {
        get = HttpMethod.GET;
        post = HttpMethod.POST;
        put = HttpMethod.PUT;
        delete = HttpMethod.DELETE;
    }


    @Test
    @DisplayName("check if message exists")
    void checkIfOneMessageExists() {
        initMock(get, "", "/messages");
        //Setting expectation
        when(requestHandler.checkIfMessageExists(1, "1"))
                .thenReturn("The message exits");

        when(requestHandler.checkIfMessageExists(1, "999"))
                .thenReturn("The message does not exist");

        when(requestHandler.checkIfMessageExists(0, "1"))
                .thenReturn("The message list is empty");


        verify(requestHandler.checkIfMessageExists(0, "1"));
        verify(requestHandler.checkIfMessageExists(0, "999"));
        verify(requestHandler.checkIfMessageExists(0, "1"));
    }



    public void initMock(HttpMethod method, String body, String path) {
        Map<String, String> messages = Map.of("1", "Msg1");



        ArrayList<String> header = new ArrayList<>(
                List.of(
                        "POST /messages HTTP/1.1",
                        "Content-Type: text/plain",
                        "User-Agent: PostmanRuntime/7.26.5",
                        "Accept: */*",
                        "Postman-Token: 2e0548fe-26e4-4c7e-9952-2d3e54ad8ed6",
                        "Host: 127.0.0.1:8000",
                        "Accept-Encoding: gzip, deflate, br",
                        "Connection: keep-alive",
                        "Content-Length: 22"
                )
        );

        requestContext = HttpRequest.builder()
                .body(body)
                .method(method)
                .version("Http/1.1")
                .path(path)
                .headerPairs(new HashMap<String, String>())
                .build()
        ;

        requestContext.setHeaderPair("Content-Length", "10");

        Integer bodyLength = Integer.parseInt(requestContext.getBodyLength(), 10);
        requestContext.setPath("/messages");
        requestContext.setMethod(get);

        requestHandler = RequestHandler.builder()
                .requestContext(requestContext)
                .status(StatusCode.OK)
                .objectsList(messages)
                .objectName(null)
                .build();

    }
}





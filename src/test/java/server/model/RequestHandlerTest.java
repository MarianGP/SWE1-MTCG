package server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

class RequestHandlerTest {
    @Mock
    ArrayList<String> header = new ArrayList<>();
    HttpRequest request = new HttpRequest(header);

    @BeforeEach



    @Test
    void handleRequest() {
    }

    @Test
    void handlePath() {
    }

    @Test
    void checkIfMessageExists() {
    }

    @Test
    void handleMessages() {
    }

    @Test
    void getAllMessages() {
    }

    @Test
    void addNewMessage() {
    }

    @Test
    void crudMessage() {
    }
}
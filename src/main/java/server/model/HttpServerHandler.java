package server.model;

import java.util.ArrayList;

public class HttpServerHandler {
    private HttpRequest request;
    private HttpResponse response;

    public HttpServerHandler(ArrayList<String> clientRequest, String body) {
        this.request = new HttpRequest(clientRequest);
        this.response = new HttpResponse(this.request, body);
    }

}

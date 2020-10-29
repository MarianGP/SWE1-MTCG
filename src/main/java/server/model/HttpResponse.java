package server.model;


import server.enums.StatusCode;

public class HttpResponse {

    //send()
    private HttpRequest request;
    private String response;
    private StatusCode status;

    public HttpResponse(HttpRequest request, String response, StatusCode status) {
        this.request = request;
        this.response = response;
        this.status = status;
    }

    public String getResponse() {
        return
            request.getVersion() + status.getCode() + status.getStatus() + "\r\n"
            + request.getHeaderPais()
            + "Content-Length: " + response.length()
            + "\r\n"
            + this.response
            ;
    }



}

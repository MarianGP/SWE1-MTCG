package server.model;


import lombok.AllArgsConstructor;
import server.enums.StatusCode;

@AllArgsConstructor
public class HttpResponse {

    private String version;
    private String response;
    private StatusCode status;

    public String getResponse() {
        return
            version + status.getCode() + status.getStatus() + "\r\n"
            + "Content-Length: " + response.length() + "\r\n"
            + "\r\n" // 2?
            + this.response
            ;
    }

}

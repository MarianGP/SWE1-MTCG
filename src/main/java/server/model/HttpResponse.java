package server.model;


import lombok.Builder;
import lombok.Getter;
import server.enums.StatusCode;

import java.util.Map;
import java.util.Set;

@Builder
@Getter
public class HttpResponse {

    private String version;
    private String response;
    private StatusCode status;
    private Map<String, String> reponseHeaderPairs;

    public String getResponse() {
        String len;

        if(!response.isEmpty()) {
            len = "Content-Length: " + response.length() + "\r\n";
        } else {
            len = "";
        }

        return
                this.version + " " + this.status.getCode() + " " + this.status.getStatus() + "\r\n"
                + len
                + "\r\n"
                + response + "\r\n"
                ;
    }

    public String getHeaders() {
        Set<Map.Entry<String, String>> entries = reponseHeaderPairs.entrySet();
        String result = "";
        for (Map.Entry<String, String> entry : entries) {
            result += entry.getKey() + ": " + entry.getValue() + "\r\n";
        }
        return result;
    }
}


package server.model;


import lombok.Builder;
import lombok.Getter;
import server.enums.StatusCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Builder
@Getter
public class HttpResponse {

    private final String version;
    private final String response;
    private final StatusCode status;
    private final Map<String, String> requestHeaderPairs;

    private Map<String, String> responseHeaderPairs;

    public String getResponse() {
        this.responseHeaderPairs = new HashMap<>();
        fillUpHeader();

        return
                this.version + " " + this.status.getCode() + " " + this.status.getStatus() + "\r\n"
                + getHeaderPairs()
                + "\r\n"
                + response + "\r\n"
                ;
    }

    public String getHeaderPairs() {
        Set<Map.Entry<String, String>> entries = this.responseHeaderPairs.entrySet();
        String result = "";
        for (Map.Entry<String, String> entry : entries) {
            result += entry.getKey() + ": " + entry.getValue() + "\r\n";
        }
        return result;
    }

    public void fillUpHeader() {
        if(!response.isEmpty()) {
            addHeaderPair("Content-Length", Integer.toString(response.length()));
            addHeaderPair("Content-Type", "text/plain");
        } else {
            addHeaderPair("Content-Length", "0");
        }

    }

    public void addHeaderPair(String key, String value) {
        this.responseHeaderPairs.put(key, value);
    }
}


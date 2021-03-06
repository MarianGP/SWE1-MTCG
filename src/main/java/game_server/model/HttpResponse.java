package game_server.model;


import game.user.User;
import game_server.enums.StatusCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Builder
@Getter
@Setter
public class HttpResponse {

    private final String version;
    private String response;
    private final StatusCode status;
    private String contentType;
    private final Map<String, String> requestHeaderPairs;
    private Map<String, String> responseHeaderPairs;
    private User player;
    private boolean startBattle;

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
        if(this.status == StatusCode.UNAUTHORIZED) {
            addHeaderPair("WWW-Authenticate", "Basic realm=\"User Visible Realm\", charset=\"UTF-8\"");
        }
        if(!response.isEmpty()) {
            addHeaderPair("Content-Length", Integer.toString(response.length()));
            addHeaderPair("Content-Type", contentType);
        } else {
            addHeaderPair("Content-Length", "0");
        }
    }

    public void addHeaderPair(String key, String value) {
        this.responseHeaderPairs.put(key, value);
    }
}


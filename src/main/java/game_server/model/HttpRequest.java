package game_server.model;

import lombok.Getter;
import lombok.Setter;
import game_server.enums.HttpMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Getter
@Setter

//RequestContext
public class HttpRequest {

    private HttpMethod method;
    private String path;
    private String version;
    private Map<String, String> headerPairs;
    private String body;

    private ArrayList<String> request; //testen

    public HttpRequest(ArrayList<String> request) {
        this.request = request;
        String[] line;
        line = request.get(0).split(" ", 3);
        try {
            this.method = HttpMethod.valueOf(line[0]);
        } catch (Exception e) {
            this.method = null;
        }

        this.path = line[1];
        this.version = line[2];
        this.headerPairs = new HashMap<String, String>();
        this.body = "";

        for (int i = 1; i < request.size()-1; i++) {
            line = request.get(i).split(": ", 2);
            headerPairs.put(line[0], line[1]);
        }
    }

    public int getBodyLength() {
        if(this.headerPairs.get("Content-Length") != null) {
            return Integer.parseInt(this.headerPairs.get("Content-Length"),10);
        }
        return 0;
    }

    // * might use in next UE
    public void setHeaderPair(String key, String value) {
        headerPairs.put(key, value);
    }
}

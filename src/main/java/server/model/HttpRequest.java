package server.model;

import lombok.Getter;
import lombok.Setter;
import server.enums.HttpMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Getter
@Setter

//RequestContext
public class HttpRequest {

    private HttpMethod method;
    private String path;
    private String version;
    private ArrayList<String> request;
    private Map<String, String> headerPairs;
    private String body;

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

    public String getHeader() {
        StringBuffer str = new StringBuffer();
        int i = 0;

        Set<Map.Entry<String, String>> entries = headerPairs.entrySet();
            for (Map.Entry<String, String> entry : entries) {
            str.append(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }

        return str.toString();
    }

    public String getBodyLength() {
        if (this.headerPairs.get("Content-Length") != null) {
            return this.headerPairs.get("Content-Length");
        } else {
            return null;
        }
    }

    public void setHeaderPair(String key, String value) {
        headerPairs.put(key, value);
    }
}

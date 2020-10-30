package server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import server.enums.HttpMethod;

import java.util.ArrayList;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter

//RequestContext
public class HttpRequest {
    //srv: received: GET /messages HTTP/1.1
    //srv: received: Host: localhost:8000
    //srv: received: User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0
    //srv: received: Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
    //srv: received: Accept-Language: es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3
    //srv: received: Accept-Encoding: gzip, deflate
    //srv: received: Connection: keep-alive
    //srv: received: Upgrade-Insecure-Requests: 1
    //srv: received: DNT: 1
    //srv: received: Sec-GPC: 1

    private HttpMethod method;
    private String path;
    private String version;
    private Map<String, Integer> headerPairs;
    private String body;

    public HttpRequest(ArrayList<String> request) {
        String[] line;
        line = request.get(0).split(" ");
        this.method = HttpMethod.valueOf(line[0]);
        this.path = line[1];
        this.version = line[2];

        for (int i = 1; i < request.size(); i++) {
            line = request.get(i).split(": ");
            headerPairs.put(line[0], Integer.parseInt(line[1]));
        }

        for (String word: line) {
            System.out.println(word + " - ");
        }
    }

    public String getHeaderPais() {
        StringBuffer str = new StringBuffer();

        for (String key: this.headerPairs.keySet()) {
            str.append(this.headerPairs.keySet());
            str.append(": ");
            str.append(this.headerPairs.get(key));
        }
        return str.toString();
    }

    public Integer getBodyLength() {
        return headerPairs.get("Content-Length");
    }

//    Set<Map.Entry<String, Integer>> entries = headerPairs.entrySet();
//        for (Map.Entry<String, Integer> entry : entries) {
//        str.append(entry.getKey() + ": " + entry.getValue() + "\r\n");
//    }
//      for (String i : map.keySet()) {
//      for (String i : map.values()) {




}

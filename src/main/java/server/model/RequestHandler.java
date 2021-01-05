package server.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import server.enums.HttpMethod;
import server.enums.StatusCode;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;

@Builder
@Getter
@Setter
public class RequestHandler {
    private StatusCode status; // response
    private HttpRequest requestContext;
    private Map<String, String> objectsList; // key, message
    private HttpResponse response;
    private String objectName; // user, message, package
    private AbstractMap.SimpleEntry<String, String> pathPair;

    public HttpResponse handleRequest() {
        String responseBody;

        //Http Method - Check
        if(this.requestContext.getMethod() == null) {
            this.status = StatusCode.BADREQUEST;
            responseBody = "Method not supported";
        } else {
            //Http Version - Check
            if(this.requestContext.getVersion().equals("HTTP/1.1")) {
                responseBody = getResponseBody();
            } else {
                setStatus(StatusCode.VERSIONNOTSUPPORTED);
                responseBody = "Version not supported";
            }
        }

        return HttpResponse.builder()
                .version(this.requestContext.getVersion())
                .response(responseBody)
                .status(this.status)
                .requestHeaderPairs(this.requestContext.getHeaderPairs())
                .build();
    }

    public String getResponseBody() {
        String message = null; //response body
        splitURL(); // * extract URL table (Value) and index (key)

        if( this.status != StatusCode.BADREQUEST ) {
            // * URL Path "/table/key"
            if ( this.requestContext.getPath().matches("(/" + this.pathPair.getValue() + "/)([0-9]+)(/?)") ) {
                message = checkIfMessageExists(this.objectsList.size(), this.pathPair.getKey());

                if (this.status != StatusCode.NOCONTENT) {
                    message = handleOneMessage(this.pathPair.getKey()); // ? GET, PUT, DELETE
                }
            // * URL Path "/table/ "messages"
            } else if (  this.requestContext.getPath().matches("(/" + pathPair.getValue() + ")(/?)") ) {
                message = handleMessages(); // ? GET: all entries || POST: create new entry

            // * Wrong URL Path"
            } else {
                setStatus(StatusCode.BADREQUEST);
            }
        }

        if(this.status == StatusCode.BADREQUEST) {
            message = "Usage: URL/table OR URL/table/{number}. Only existing tables allowed";
        }

        return message;
    }

    public void splitURL() {

        String[] allowedTables = {"users", "sessions", "transactions", "tradings", "score", "stats", "battles"}; //TODO: edit for next UE
        String[] pathParts = this.requestContext.getPath().split("/");

        if( pathParts.length >= 2 && pathParts.length <= 3
            && Arrays.asList(allowedTables).contains(pathParts[1])
        ) {

            this.objectName = pathParts[1].substring( 0, pathParts[1].length()-1 ); // messages -> message
            this.pathPair = new AbstractMap.SimpleEntry<>(pathParts.length > 2 ? pathParts[2] : null, pathParts[1]);

        } else {
            this.pathPair = null;
            this.status = StatusCode.BADREQUEST;
        }
    }

    public String checkIfMessageExists(int size, String key) {
        boolean exists = false;

        if(this.requestContext.getMethod() == HttpMethod.GET || this.requestContext.getMethod() == HttpMethod.PUT || this.requestContext.getMethod() == HttpMethod.DELETE) {
            if(size > 0) {
                if(!this.objectsList.containsKey(key)) {
                    this.status = StatusCode.NOCONTENT;
                    return "The " + this.objectName + " does not exist";
                } else  {
                    return "The " + this.objectName + " exits";
                }
            } else {
                this.status = StatusCode.NOCONTENT;
                return "The " + this.objectName + " list is empty";
            }
        } else {
            this.status = StatusCode.BADREQUEST;
            return "Usage: To add new " + this.objectName + " use POST Method and URL: /messages";
        }
    }

    public String handleMessages() {
        switch (this.requestContext.getMethod()) {
            case GET:
                return getAllMessages();
            case POST:
                return addNewMessage();
            default:
                setStatus(StatusCode.BADREQUEST);
                return "Bad Request: Only Methods Accepted: GET, POST";
        }
    }

    public String getAllMessages() {
        StringBuilder st =new StringBuilder();
        int count = 0;
        for (Map.Entry<String, String> entry : this.objectsList.entrySet()){
            st.append(entry.getKey());
            st.append(")  ");
            st.append(entry.getValue());
            st.append("\r\n");
            count++;
        }
        if(count == 0) {
            this.status = StatusCode.NOCONTENT;
            return "The " + this.objectName + " list is empty";
        } else {
            return st.toString();
        }
    }

    public String addNewMessage() {
        String body = this.requestContext.getBody();

        try {
            if(!body.isEmpty()) {
                this.objectsList.put(Integer.toString((this.objectsList.size() + 1)), body);
                setStatus(StatusCode.CREATED);
                return "New " + this.objectName + " was created";
            } else {
                setStatus(StatusCode.BADREQUEST);
                return "Couldn't create new "+ this.objectName +". Body was empty.";
            }

        } catch (Exception e){
            setStatus(StatusCode.INTERNALERROR);
            return "Internal Error";
        }
    }

    public String handleOneMessage(String key) {

        switch (this.requestContext.getMethod()) {
            case GET:
                return this.objectsList.get(key);
            case PUT:
                this.objectsList.put(key, this.requestContext.getBody());
                return "The " + this.objectName + " was modified";
            case DELETE:
                this.objectsList.remove(key);
                return "The " + this.objectName + " was deleted";
            default:
                setStatus(StatusCode.BADREQUEST);
                return "Bad Request: Only Methods Accepted: GET, PUT, DELETE, when indicating a " + this.objectName + " number/index in URL";
        }

    }


//    private void convertToJson() {
//        if(this.requestContext.getHeaderPairs().get("Content-Type").equals("application/json")) {
//            //convertToJson();
//            Gson g = new Gson();
//            //Player p = g.fromJson(this.requestContext.getBody(), Player.class);
//            //String str = g.toJson(p);
//        }
//    }
}




















//String[] allowedTables = {"messages", "users", "packages", "deck"}; //next UE, just ignore

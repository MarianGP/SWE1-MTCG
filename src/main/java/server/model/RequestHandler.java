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
    private StatusCode status;
    private HttpRequest requestContext;
    private Map<String, String> objectsList; // key, value
    private HttpResponse response;
    private String objectName; // user, message, package
    AbstractMap.SimpleEntry<String, String> pathPair;

    public HttpResponse handleRequest() {
        String responseBody;

        //Http Method - Check
        if(this.requestContext.getMethod() == null) {
            this.status = StatusCode.BADREQUEST;
            responseBody = "Method not supported";
        } else {
            //Http Version - Check
            if(this.requestContext.getVersion().equals("HTTP/1.1")) {
                responseBody = handlePath();
            } else {
                setStatus(StatusCode.VERSIONNOTSUPPORTED);
                responseBody = "Version not supported";
            }
        }

        return HttpResponse.builder()
                .version(this.requestContext.getVersion())
                .response(responseBody)
                .status(this.status)
                .build();
    }

    public String handlePath() {

        String message;
        String[] allowedTables = {"messages"}; //TODO: edit for next UE

        //URL table (Value) and index (key)
        splitURL();

        //URL Path "/table/key"
        if(     pathPair.getKey() != null
                && pathPair.getValue() != null
                && this.requestContext.getPath().matches("(/" + pathPair.getValue() + "/)([0-9]+)(/?)")
                && Arrays.asList(allowedTables).contains(pathPair.getValue())
        ){
            message = checkIfMessageExists(this.objectsList.size(),  pathPair.getKey());

            if(this.status != StatusCode.NOCONTENT) {
                message = crudMessage(pathPair.getKey());
            }

        //URL Path "/table/
        } else if(
                pathPair.getValue() != null
                && this.requestContext.getPath().matches("(/" + pathPair.getValue() + ")(/?)")
                && Arrays.asList(allowedTables).contains(pathPair.getValue())
        ) {
            message =  handleMessages();

        //URL Path "/another/Path"
        } else {
            setStatus(StatusCode.BADREQUEST);
            message =  "Usage: URL/table OR URL/table/{number}. Only existing tables allowed";
        }
        return message;
    }

    public void splitURL() {

        String[] pathParts = this.requestContext.getPath().split("/");
        StringBuffer st = new StringBuffer();

        for (int i = 0; i < pathParts[1].length() -1; i++) {
            st.append(pathParts[1].charAt(i));
        }

        this.objectName = st.toString();

        String key = pathParts.length > 2 ? pathParts[2] : null;
        String table = pathParts[1];

        this.pathPair = new AbstractMap.SimpleEntry<>(key, table);
    }

    public String checkIfMessageExists(int size, String key) {
        boolean exists = false;

        if(this.requestContext.getMethod() != HttpMethod.POST) {
            if(size > 0) {
                for (int i = 0; i < this.objectsList.size(); i++) { //check if key exists
                    if(this.objectsList.get(key) != null) {
                        exists = true;
                    }
                }

                if(!exists) {
                    this.status = StatusCode.NOCONTENT;
                    return "The " + this.objectName + " does not exist";
                }
            } else {
                this.status = StatusCode.NOCONTENT;
                return "The " + this.objectName + " list is empty";
            }
            return "The " + this.objectName + " exits";
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
                return "Bad Request: Only Methods Accepted: GET, PUT, DELETE";
        }
    }

    public String getAllMessages() {
        StringBuffer st =new StringBuffer();
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

    public String crudMessage(String key) {

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
                return "Bad Request: Only Methods Accepted: GET, PUT, DELETE, when indicating a " + this.objectName + " number/index";
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

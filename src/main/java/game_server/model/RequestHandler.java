package game_server.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.user.Credentials;
import game.user.User;
import game_server.controller.UserController;
import game_server.db.PostgreSQLJDBC;
import game_server.enums.HttpMethod;
import game_server.enums.StatusCode;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Builder
@Data
public class RequestHandler {
    private StatusCode status; // response
    private HttpRequest requestContext;
    private Map<String, String> objectsList; // key, message
    private String responseBody;
    private HttpResponse response;
    private String objectName;
    private String path[];
    private UserController userController;

    public HttpResponse handleRequest() throws ClassNotFoundException {

        if(HttpMethod.listOfMethods.contains(requestContext.getMethod())) {
            this.status = StatusCode.BADREQUEST;
            setResponseBody("Method not supported");
        } else {
            if(this.requestContext.getVersion().equals("HTTP/1.1")) {
                handlePath();
            } else {
                setStatus(StatusCode.VERSIONNOTSUPPORTED);
                setResponseBody("Version not supported");
            }
        }

        return HttpResponse.builder()
                .version(this.requestContext.getVersion())
                .response(this.responseBody)
                .status(this.status)
                .requestHeaderPairs(this.requestContext.getHeaderPairs())
                .build();
    }

    public void handlePath() throws ClassNotFoundException {
        this.objectName = splitURL(this.requestContext.getPath());

        if( this.status != StatusCode.BADREQUEST ) {
            if ( !this.path[2].isEmpty() && this.requestContext.getPath().matches("(/" + this.path[1] + "/)("+ this.path[2] +")(/?)" )) {  // * URL Path "/table/table"

            } else if ( this.requestContext.getPath().matches("(/" + this.path[1] + ")(/?)") ) {  // * URL Path "/table
                handleObject(this.path[1]);
            } else {// * Wrong URL Path"
                setStatus(StatusCode.BADREQUEST);
            }
        }

        if(this.status == StatusCode.BADREQUEST) {
            setResponseBody("Usage: URL/table OR URL/table/{number}. Only existing tables allowed");
        }
        setResponseBody("CHANGE ME LATER"); // TODO: CHANGE LATER
    }

    public String splitURL(String fullPath) {
        String[] allowedTables = {"users", "sessions", "transactions", "tradings", "score", "stats", "battles"};

        this.path = fullPath.split("/");

        if( this.path.length >= 2 && Arrays.asList(allowedTables).contains(this.path[1]) ) {
            if(this.requestContext.getPath().contains("\\?")) {
                handleUrlParameters(fullPath.split("\\?")[1]);
            }
             return Character.toUpperCase(this.path[1].charAt(0)) + this.path[1].substring( 1, this.path[1].length()-1 ); // messages -> message

        } else {
            this.path = null;
            this.status = StatusCode.BADREQUEST;
            return null;
        }
    }

    public void handleUser(String requestBody, HttpMethod method) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); // also private attributes
        Credentials credentials = objectMapper.readValue(requestBody, Credentials.class);

        switch(method) {
            case POST:
                addNewUser(credentials);
                break;
            case GET:
                logUserIn(credentials);
                break;
            default:
                this.status = StatusCode.BADREQUEST;
        }
    }

    private void addNewUser(Credentials credentials){
        try {
            User newUser = User.builder()
                    .username(credentials.getUsername())
                    .password(credentials.getPassword())
                    .token(credentials.getUsername() + "-mtcgToken")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.responseBody = userController.addNewUser(credentials);
    }

    private void logUserIn(Credentials credentials) {
        this.responseBody = userController.getUser(credentials);
    }

    public void handleObject(String table) throws ClassNotFoundException {

        switch (table) {
            case "users":
//                handleUser(requestContext.getBody(), requestContext.getMethod());
                break;
            case "sessions":
                break;
            case "transactions":
                break;
            case "score":
                break;
            case "stats":
                break;
            case "battles":
                break;
            default:
                this.status = StatusCode.BADREQUEST;
                break;
        }

    }

    public void handleUrlParameters(String parameters) {
        Map<String, String> map = getQueryMap(requestContext.getPath());
    }

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();

        for (String param : params) {
            if(param.length() < 2) return null; // ! control later
            String key = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(key, value);
        }
        return map;
    }

    public boolean checkIfExists(int size, String key) { // ? PUT INTO ANOTHER INTERFACE LIKE: searchable
        if(this.requestContext.getMethod() == HttpMethod.GET || this.requestContext.getMethod() == HttpMethod.PUT || this.requestContext.getMethod() == HttpMethod.DELETE) {
            if(size > 0) {
                if(!this.objectsList.containsKey(key)) {
                    this.status = StatusCode.NOCONTENT;
                    responseBody = "The " + this.objectName + " does not exist";
                } else  {
                    responseBody = "The " + this.objectName + " exits";
                    return true;
                }
            } else {
                this.status = StatusCode.NOCONTENT;
                responseBody = "The " + this.objectName + " list is empty";
            }
        } else {
            this.status = StatusCode.BADREQUEST;
            responseBody = "Usage: To add new " + this.objectName + " use POST Method and URL: /messages";
        }
        return false;
    }

    public String modifyTable() {
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
        StringBuilder st = new StringBuilder();
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

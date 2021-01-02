package game_server.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.user.Credentials;
import game.user.User;
import game_server.controller.CardController;
import game_server.controller.UserController;
import game_server.db.DbConnection;
import game_server.enums.HttpMethod;
import game_server.enums.StatusCode;
import lombok.Builder;
import lombok.Data;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Builder
@Data
public class RequestHandler {

    private StatusCode status; // response
    private HttpRequest requestContext;
    private Map<String, String> objectsList; // key, message
    private String responseBody;
    private HttpResponse response;

    private String[] path;

    private DbConnection db;
    private UserController userController;
    private CardController cardController;

    public HttpResponse handleRequest() throws JsonProcessingException, SQLException {

        if(!HttpMethod.listOfMethods.contains(requestContext.getMethod())) {
            setResponseStatus("Method not supported", StatusCode.BADREQUEST);
        } else {
            if(this.requestContext.getVersion().equals("HTTP/1.1")) {
                handlePath();
            } else {
               setResponseStatus("Version not supported",StatusCode.VERSIONNOTSUPPORTED);
            }
        }

        return HttpResponse.builder()
                .version(this.requestContext.getVersion())
                .response(this.responseBody)
                .status(this.status)
                .requestHeaderPairs(this.requestContext.getHeaderPairs())
                .build();
    }

    public void handlePath() throws JsonProcessingException, SQLException {
        splitURL(this.requestContext.getPath());

        if (this.status != StatusCode.BADREQUEST
            && this.requestContext.getPath().matches("(/" + this.path[1] + "/)("+ this.path[2] +")(/?)" )) {
                selectAction(this.path[1],this.path[2], getClientToken());

        } else if (this.status != StatusCode.BADREQUEST
            && this.requestContext.getPath().matches("(/" + this.path[1] + ")(/?)") ) {
                selectAction(this.path[1], getClientToken());

        } else {
            setResponseStatus("Wrong URL Path", StatusCode.BADREQUEST);
        }
    }

    public void splitURL(String fullPath) {
        this.path = new String[3];
        String[] temp = fullPath.split("/");
        int i = 0;

        for(String one: temp) {
            this.path[i] = one;
            i++;
        }

        if(this.requestContext.getPath().contains("\\?"))
            handleUrlParameters(fullPath.split("\\?")[1]);

        validateURLActions(this.path[1], this.path[2]);
    }

    public void validateURLActions(String first, String second) {
        String[] allowedTables = {"users", "sessions", "transactions", "tradings", "score", "stats", "battles", "deck", "cards", "", "packages", null};
        if( !Arrays.asList( allowedTables ).contains( first )
                || !Arrays.asList( allowedTables ).contains( second ) ) {
            this.path = null;
            setResponseStatus("URL not allowed", StatusCode.BADREQUEST);
        }
    }

    public void selectAction(String first, String second, String token) throws JsonProcessingException {
        if(userController.setUser(token)) {
            switch (first) { //localhost:8008/first/second
                case "users":
                    manageUserInformation(token, second);
                    break;
                case "transactions":
                    buyNewPackage(second, requestContext.getBody(), token);
                    break;
                default:
                    setResponseStatus("URL not allowed", StatusCode.BADREQUEST);
                    break;
            }
        } else {
            setResponseStatus("Need to login to access any functionality", StatusCode.BADREQUEST);
        }
    }

    public void selectAction(String section, String token) throws JsonProcessingException, SQLException { // /users/

        if(!userController.setUser(token)) {
            anonymUserAction(section);
        } else { // logged in
            loggedUserAction(section);
        }
    }

    public void anonymUserAction(String action) throws JsonProcessingException, SQLException {
        switch (action) {
            case "users":
                handleUser(this.requestContext.getBody(), this.requestContext.getMethod());
                break;
            case "sessions":
                singIn(this.requestContext.getBody());
                break;
            default:
                setResponseStatus("Unauthorized. Log in to access all functionalities",
                        StatusCode.UNAUTHORIZED);
                break;
        }
    }

    public void loggedUserAction(String action) throws JsonProcessingException {
        switch (action) {
            case "score":
                getScoreBoard();
                break;
            case "stats":
                this.responseBody = userController.getUser().userStats("");
                break;
            case "battles":
                break;
            case "packages":
                insertNewPackage();
                break;
            case "cards":
                showUserCards(getClientToken());
                break;
            case "deck":
                manipulateDeck(getClientToken(), requestContext.getBody());
                break;
            default:
                setResponseStatus("Wrong URL", StatusCode.BADREQUEST);
                break;
        }
    }

    public void showUserCards(String token) { // ! not admins
        if(userController.setUser(token) && !userController.getUser().isAdmin()) {
            if(userController.initializeStack()) {
                this.responseBody = String.valueOf(cardController
                        .getAllCardsStats(userController.getUser().getStack().getStack()));
            } else {
                this.responseBody = "Stack is empty";
            }
        } else {
            setResponseStatus("Only players own cards (not admins)", StatusCode.BADREQUEST);
        }
    }

    public void  manipulateDeck(String token, String requestBody) {
        if(userController.setUser(token) && !userController.getUser().isAdmin()) {
            if(userController.initializeStack()) {
                switch (requestContext.getMethod()) {
                    case PUT:
                        userController.addCardsToDeck(requestBody);
                        break;
                    case GET:
                        userController.showDeck();
                        break;
                    default:
                        setResponseStatus("Deck - This method is not allowed", StatusCode.BADREQUEST);
                        break;
                }
            }
            else {
                this.responseBody = "Stack is empty";
            }
        } else {
            setResponseStatus("Only players own cards (not admins)", StatusCode.BADREQUEST);
        }
    }

    public void insertNewPackage() throws JsonProcessingException {
        if(userController.getUser().isAdmin()) {
            if( ! cardController.insertJSONCards( this.requestContext.getBody(), userController.getUser() ) ) {
                setResponseStatus( "This card already exists in DB", StatusCode.BADREQUEST);
            } else {
                setResponseStatus( "Cards added to DB", StatusCode.BADREQUEST);
            }
        } else {
            setResponseStatus("Only admin can add new packages", StatusCode.UNAUTHORIZED);
        }
    }

    public void buyNewPackage(String urlAction, String requestBody, String token) {

        if(requestContext.getMethod() == HttpMethod.POST && userController.setUser(token) ){
            if(urlAction.equals("packages") && requestBody.isEmpty()) {
                this.responseBody = userController.buyNewPackage(null);
            } else {
                setResponseStatus("Wrong URL. Usage: URL/transactions/packages", StatusCode.BADREQUEST);
            }
        } else {
            setResponseStatus("POST method is need to buy packages", StatusCode.BADREQUEST);
        }
    }

    public void getScoreBoard() {
        AtomicInteger i = new AtomicInteger();
        List<User> allUsers = new ArrayList<>(db.getAllUsers());
        allUsers.sort(Collections.reverseOrder());

        this.responseBody = "-- Scoreboard --";
        allUsers.forEach((temp)-> {
            if(!temp.isAdmin()) i.incrementAndGet();
            this.responseBody = this.responseBody + temp.userStats(i.toString());
        });
    }

    public void manageUserInformation(String username, String token) {
        if( userController.setUser(token) && token.equals( userController.getUser().getToken() ) ) { //token = user to be accessed
            if( requestContext.getMethod() == HttpMethod.GET ) { //get user info
                this.responseBody = userController.getUser().printUserDetails();
            } else if ( requestContext.getMethod() == HttpMethod.PUT ) { //modify user
//                editUser();
            } else {
                setResponseStatus("Method not allowed", StatusCode.BADREQUEST);
            }
        } else {

            this.responseBody = "You don't have access to other users' account";
        }
    }

    public String getClientToken() {
        String tokenLine = requestContext.getHeaderPairs().get("Authorization");
        if(tokenLine != null) {
            String[] auths = tokenLine.split(" ");
            if(!auths[1].isEmpty()) {
                return auths[1];
            }
        }
        return null;
    }

    public static Credentials getCredentials(String requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); // also private attributes
        return objectMapper.readValue(requestBody, Credentials.class);
    }

    public void handleUser(String requestBody, HttpMethod method) throws JsonProcessingException, SQLException {
        if (method == HttpMethod.POST) {
            signUp(requestBody, getClientToken());
        } else {
            setResponseStatus("Method is not allowed", StatusCode.BADREQUEST);
        }
    }

    private void signUp(String requestBody, String token) throws JsonProcessingException, SQLException { //POST
        Credentials credentials = getCredentials(requestBody);

        if(token == null) { // ! not logged
            if(db.getUser(credentials.getUsername()) == null) { // ! not exist in DB
               this.responseBody = userController.addUserDB(credentials);
            } else {
                this.responseBody = "This user already exist in DB";
            }
        } else { // ! already logged
            if(userController.setUser(token)) {
                this.responseBody = "Can't include token";
            };
        }
    }

    public void singIn(String requestBody) throws JsonProcessingException {
        Credentials credentials = getCredentials(requestBody);
        this.responseBody = userController.login(credentials);
        if(this.userController.getUser() == null) {
            this.status = StatusCode.UNAUTHORIZED;
        }
    }

//    private void editUser() {
//        if(isContentJson()) {
//            //            User user = convertFromJson(requestContext.getBody());
//            if(user != null) {
//                this.responseBody = "Data update was successful (UserController)";
//            } else {
//                this.responseBody = "Data update failed (UserController)";
//            }
//            userController.setUser(user);
//        }
//    }

    public void setResponseStatus(String responseMsg, StatusCode code) {
        this.status = code;
        this.responseBody = responseMsg;
    }

    public boolean isContentJson() { // for editUser
        if(!requestContext.getHeaderPairs().get("Content-Type").equals("application/json")) {
            setResponseStatus("Only Json request are allowed", StatusCode.BADREQUEST);
            return false;
        } else {
            return true;
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


//    private void convertToJson() {
//        if(this.requestContext.getHeaderPairs().get("Content-Type").equals("application/json")) {
//            convertToJson();
//            Gson g = new Gson();
//            Player p = g.fromJson(this.requestContext.getBody(), Player.class);
//            String str = g.toJson(p);
//        }
//    }


}





















package game_server.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.cards.Card;
import game.user.Credentials;
import game.user.User;
import game_server.controller.CardController;
import game_server.controller.TradeController;
import game_server.controller.UserController;
import game_server.db.DbConnection;
import game_server.enums.HttpMethod;
import game_server.enums.StatusCode;
import game_server.interfaces.RequestHandling;
import lombok.Builder;
import lombok.Data;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Builder
@Data
public class RequestHandler implements RequestHandling {

    private StatusCode status; // * response
    private HttpRequest requestContext;
    private String responseBody;
    private String[] path;
    private boolean startBattle;

    private DbConnection db;
    private UserController userController;
    private CardController cardController;
    private TradeController tradeController;

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
                .player(this.userController.getUser())
                .startBattle(startBattle)
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

        validateURLActions(this.path[1]);
    }

    public void validateURLActions(String first) {
        String[] allowedTables = {"users", "sessions", "transactions", "tradings", "score", "stats", "battles", "deck", "cards", "", "packages", null};
        if( !Arrays.asList( allowedTables ).contains( first ) ) {
            this.path = null;
            setResponseStatus("URL not allowed", StatusCode.BADREQUEST);
        }
    }

    public void selectAction(String first, String second, String token) throws JsonProcessingException {
        //! localhost:8008/first/second

        if(this.userController.setUser(token)) {
            switch (first) {
                case "users"        -> manipulateUserAccount(this.userController.getUser().getUsername(), second);
                case "transactions" -> buyNewPackage(second, requestContext.getBody(), token);
                case "tradings"     -> tradeCards(second, this.requestContext.getBody()); //TODO
                default             -> setResponseStatus("URL not allowed", StatusCode.BADREQUEST);
            }
        } else {
            setResponseStatus("Need to login to access any functionality", StatusCode.BADREQUEST);
        }
    }

    public void tradeCards(String wantedCardId, String requestBody) {
        String offeredCardId = requestBody.replaceAll("[\"]", "");
        String errorMsg = tradeController.tradeCards(wantedCardId, offeredCardId, userController.getUser().getUsername());
        if(errorMsg == null) {
            this.responseBody = "Trade was successful";
        } else {
            setResponseStatus(errorMsg, StatusCode.BADREQUEST);
        }
    }

    public void selectAction(String first, String token) throws JsonProcessingException, SQLException {

        if(!this.userController.setUser(token)) { // ! user is not logged in
            anonymousUserAction(first);
        } else {
            loggedUserAction(first);
        }

    }

    public void anonymousUserAction(String first) throws JsonProcessingException, SQLException {

        switch (first) {
            case "users"    -> handleUser(this.requestContext.getBody(), this.requestContext.getMethod());
            case "sessions" -> singIn(this.requestContext.getBody());
            default         -> setResponseStatus("Unauthorized. Log in to access all functionalities",
                                    StatusCode.UNAUTHORIZED);
        }
    }

    public void loggedUserAction(String first) throws JsonProcessingException, SQLException {

        switch (first) {
            case "score"    -> getScoreBoard();
            case "stats"    -> this.responseBody = this.userController.getUser().userStats("");
            case "battles"  -> startBattle(getClientToken());
            case "packages" -> insertNewPackage();
            case "cards"    -> showUserCards(getClientToken());
            case "deck"     -> manipulateDeck(getClientToken(), requestContext.getBody());
            case "tradings" -> handleTradings(getClientToken());
            default         -> setResponseStatus("Wrong URL", StatusCode.BADREQUEST);
        }
    }

    public void handleTradings(String token) throws JsonProcessingException, SQLException {
        if(this.userController.setUser(token) && !this.userController.getUser().isAdmin()) {
            switch(this.requestContext.getMethod()) {
                case GET -> showTrades();
                case POST -> addTrade(this.requestContext.getBody(), this.userController.getUser().getUsername());
                default -> setResponseStatus("Tranding - Wrong method", StatusCode.BADREQUEST);
            }
        } else {
            setResponseStatus("Only players own cards (not admins)", StatusCode.BADREQUEST);
        }
    }

    public void addTrade(String requestBody, String username) throws JsonProcessingException, SQLException {
        String errorMsg = tradeController.addNewTrade(requestBody, username);
        if(errorMsg == null) {
            this.responseBody = "New trade added";
        } else {
            setResponseStatus(errorMsg, StatusCode.BADREQUEST);
        }
    }

    public void showTrades() {
        String allStr = "--------------------\n" +
                        "-- Cards to Trade --\n" +
                        "--------------------\n\n";
        String allTradesInfo = String.valueOf(tradeController.getTradesSummary());
        if(allTradesInfo == null) {
            setResponseStatus("The are no trades available", StatusCode.NOCONTENT);
        } else {
            System.out.println(allStr + allTradesInfo);
            this.responseBody = allStr + allTradesInfo;
        }
    }

    public void startBattle(String token) {
        if(this.userController.setUser(token) && !this.userController.getUser().isAdmin() && this.requestContext.getMethod() == HttpMethod.POST) {
            if (this.userController.initializeStack()) {
                if(this.userController.getUser().getStack().getStackList().size() >= 4) {
                    this.startBattle = true;
                } else {
                    setResponseStatus("You don't have enough cards to play (min: 4)", StatusCode.BADREQUEST);
                }
            }  else {
                setResponseStatus("You can't start a battle. Your Stack is Empty", StatusCode.BADREQUEST);
            }
        } else {
            setResponseStatus("Only players own cards (not admins)", StatusCode.BADREQUEST);
        }
    }

    public void showUserCards(String token) { // ! none-admin users
        if(this.userController.setUser(token) && !this.userController.getUser().isAdmin()) {
            if(this.userController.initializeStack()) {
                this.responseBody = String.valueOf(cardController
                    .getCardListStats(this.userController.getUser().getStack().getStackList(),"Stack"));
            } else {
                setResponseStatus("Stack is empty", StatusCode.NOCONTENT);
            }
        } else {
            setResponseStatus("Only players own cards (not admins)", StatusCode.BADREQUEST);
        }
    }

    public void manipulateDeck(String token, String requestBody) {

        if(this.userController.setUser(token) && !this.userController.getUser().isAdmin()) {
            if(this.userController.initializeStack()) {
                switch (this.requestContext.getMethod()) {
                    case PUT -> initializeDeck(requestBody);
                    case GET -> showDeckCards(false); //TODO: show in json
                    default  -> setResponseStatus("Deck - This method is not allowed", StatusCode.BADREQUEST);
                }
            } else {
                setResponseStatus("Stack is empty", StatusCode.NOCONTENT);
            }
        } else {
            setResponseStatus("Only players own cards (not admins)", StatusCode.BADREQUEST);
        }
    }

    public void showDeckCards(boolean formatJson) {
        if(!this.userController.getUser().isAdmin()) {
            if(this.userController.getUser().getDeck().getDeckList() != null) {
                List<Card> deck = this.userController.getUser().getDeck().getDeckList();
                if(!deck.isEmpty()) {
                    if(!formatJson) {
                        this.responseBody = String.valueOf(cardController
                                .getCardListStats(deck, "Deck"));
                    }
                } else {
                    setResponseStatus("Deck is empty", StatusCode.NOCONTENT);
                }
            }
        } else {
            setResponseStatus("Only players own cards (not admins)", StatusCode.BADREQUEST);
        }
    }

    public void initializeDeck(String requestBody) {
        List<String> ids = parseJsonArray(requestBody);
        if(ids.size() == 4) {
            String errorMsg = this.userController.addCardsToDeck(ids);
            if(errorMsg == null) {
                this.responseBody = "New deck prepared";
            } else {
                setResponseStatus(  errorMsg, StatusCode.BADREQUEST);
            }
        } else {
            setResponseStatus("4 cards needs to be added to stack", StatusCode.BADREQUEST);
        }
    }

    public void insertNewPackage() throws JsonProcessingException {
        if(this.userController.getUser().isAdmin()) {
            if( ! this.cardController.insertJSONCards( this.requestContext.getBody(), this.userController.getUser() ) ) {
                setResponseStatus( "This card already exists in DB", StatusCode.BADREQUEST);
            } else {
                this.responseBody = "Cards added to DB";
            }
        } else {
            setResponseStatus("Only admin can add new packages", StatusCode.UNAUTHORIZED);
        }
    }

    public void buyNewPackage(String urlAction, String requestBody, String token) {

        if(this.requestContext.getMethod() == HttpMethod.POST && this.userController.setUser(token) ){
            if(urlAction.equals("packages") && requestBody.isEmpty()) {
                this.responseBody = this.userController.buyPackage(null);
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

        this.responseBody = "-- Scoreboard --\n";
        allUsers.forEach((temp)-> {
            if(!temp.isAdmin()) i.incrementAndGet();
            this.responseBody = this.responseBody + temp.userStats(i.toString());
        });
    }

    public void manipulateUserAccount(String token, String userNameURL) throws JsonProcessingException {
        if( userNameURL.equals(token) ) { // * token = user

            if( requestContext.getMethod() == HttpMethod.GET ) { // ! show user info
                this.responseBody = this.userController.getUser().printUserDetails();

            } else if ( requestContext.getMethod() == HttpMethod.PUT ) { // ! modify user info
                this.responseBody = this.userController.editUserData(this.requestContext.getBody());
            } else {
                setResponseStatus("Method not allowed", StatusCode.BADREQUEST);
            }

        } else {
            setResponseStatus("You don't have access to other users' account", StatusCode.UNAUTHORIZED);
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

    public void signUp(String requestBody, String token) throws JsonProcessingException, SQLException { //POST
        Credentials credentials = getCredentials(requestBody);

        if(token == null) { // ! not logged
            if(db.getUser(credentials.getUsername()) == null) { // ! not exist in DB
               this.responseBody = userController.signUp(credentials);
            } else {
                this.responseBody = "This user already exist in DB";
            }
        } else { // ! already logged
            if(this.userController.setUser(token)) {
                this.responseBody = "Can't include token";
            };
        }
    }

    public void singIn(String requestBody) throws JsonProcessingException {
        Credentials credentials = getCredentials(requestBody);
        this.responseBody = this.userController.signIn(credentials);
        if(this.userController.getUser() == null) {
            this.status = StatusCode.UNAUTHORIZED;
        }
    }

    public void setResponseStatus(String responseMsg, StatusCode code) {
        this.status = code;
        this.responseBody = responseMsg;
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

    public List<String> parseJsonArray(String json) {
        String replaceStr = json.replaceAll("[\\[\\] \\n\\r\"]", "");
        List<String> arr = Arrays.asList(replaceStr.split(","));
        return arr;
    }

    public boolean isContentJson() { // for editUser
        if(!requestContext.getHeaderPairs().get("Content-Type").equals("application/json")) {
            setResponseStatus("Only Json request are allowed", StatusCode.BADREQUEST);
            return false;
        } else {
            return true;
        }
    }

}





















package game_server;

import game_server.controller.CardController;
import game_server.controller.GameController;
import game_server.controller.TradeController;
import game_server.controller.UserController;
import game_server.db.DbConnection;
import game_server.enums.StatusCode;
import game_server.model.HttpRequest;
import game_server.model.HttpResponse;
import game_server.model.RequestHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@AllArgsConstructor
public class RequestThread implements Runnable {

    private final Socket s;
    private final GameController gameController;

    @Override
    @SneakyThrows
    public void run() {
        ArrayList<String> header = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream(),StandardCharsets.UTF_8));
        readHeader(header, reader);

        try {
            if(header.size() < 2) return; //postman solution - empty request
            HttpRequest requestContext = new HttpRequest(header);
            OutputStream outputStream = s.getOutputStream();
            readBody(requestContext, reader);

            try {
                RequestHandler requestHandler = RequestHandler.builder()
                        .requestContext(requestContext)
                        .responseStatus(StatusCode.OK)
                        .db(new DbConnection())
                        .userController(new UserController(
                                new DbConnection(), null))
                        .cardController( new CardController( new DbConnection() ) )
                        .tradeController( new TradeController( new DbConnection() ) )
                        .formatJson(true)
                        .startBattle(false)
                        .build();

                HttpResponse response = requestHandler.handleRequest();

                if(response.isStartBattle() && response.getPlayer() != null) { //! Battle
                    prepareBattle(gameController, response);
                }

                outputStream.write(response.getResponse().getBytes());

            } catch (RuntimeException e) {
                e.printStackTrace();
                HttpResponse res = HttpResponse.builder()
                        .version(requestContext.getVersion())
                        .response("Internal Server Error")
                        .status(StatusCode.INTERNALERROR)
                        .requestHeaderPairs(null)
                        .build();

                outputStream.write(res.getResponse().getBytes()); // e.getMessage();
            }
            outputStream.flush();
            outputStream.close();

        } catch (IOException e){
            System.out.println("Error reading header request or body");
        }
        reader.close();
    }

    private void readHeader(ArrayList<String> header, BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
            header.add(line);
            System.out.println(line);
        } while (line != null && !line.isEmpty());
    }

    public void readBody(HttpRequest requestContext, BufferedReader reader) throws IOException {
        if ( requestContext.getBodyLength() > 0 ) {
            int read;
            StringBuffer sb = new StringBuffer();
            while ((read = reader.read()) != -1) {
                sb.append((char) read);
                if (sb.length() == requestContext.getBodyLength()) {
                    break;
                }
            }
            requestContext.setBody(sb.toString());
        } else {
            requestContext.setBody("");
        }
    }

    public void prepareBattle(GameController gameController, HttpResponse response) throws InterruptedException {
        if(gameController.getPlayers().size() < 2) {
            gameController.addPlayer(response.getPlayer());
        }

        if(gameController.getPlayers().size() == 2)
            gameController.startGame();

        while(!gameController.getIsFinished().get()) {
            Thread.sleep(50);
        }

        response.setResponse(gameController.getBattleLog().getResultSummary());
    }
}

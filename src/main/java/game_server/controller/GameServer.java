package game_server.controller;

import game_server.db.DbConnection;
import game_server.enums.StatusCode;
import game_server.model.HttpRequest;
import game_server.model.HttpResponse;
import game_server.model.RequestHandler;
import lombok.Builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Builder
public class GameServer implements Runnable {

    private static ServerSocket listener = null;

    public static void main(String[] args) {
        System.out.println("start server");

        try {
            listener = new ServerSocket(10001, 5); //8080
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //threads
        Runtime.getRuntime().addShutdownHook(new Thread(new GameServer()));

        try {
            while (true) {
                Socket s = listener.accept();
                ArrayList<String> header = new ArrayList<>();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8) );
                String line;

                //Read Header
                do {
                    line = reader.readLine();
                    header.add(line);
                    System.out.println(line);
                } while (line != null && !line.isEmpty());

                try {
                    if(header.size() < 2) continue;
                    HttpRequest requestContext = new HttpRequest(header);

                    // Read Body
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

                    OutputStream outputStream = s.getOutputStream();

                    try {
                        RequestHandler requestHandler = RequestHandler.builder()
                                .requestContext(requestContext)
                                .status(StatusCode.OK)
                                .db(new DbConnection())
                                .userController(new UserController(
                                        new DbConnection(), null))
                                .cardController( new CardController( new DbConnection() ) )
                                .tradeController( new TradeController( new DbConnection() ) )
                                .build();

                        HttpResponse response = requestHandler.handleRequest();

                        // Write Response to Client
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
                    outputStream.close(); //close

                } catch (IOException e){
                    System.out.println("Error reading header request or body");
                }
                reader.close(); //close
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listener = null;
        System.out.println("close server");
    }
}
package server;

import lombok.Builder;
import server.enums.StatusCode;
import server.model.HttpRequest;
import server.model.HttpResponse;
import server.model.RequestHandler;

import javax.ws.rs.BadRequestException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

@Builder
public class HttpServer implements Runnable {

    private static ServerSocket listener = null;
    private static Map<Integer, String> messages = null; //temp no databank

    public static void main(String[] args) {
        System.out.println("start server");

        try {
            listener = new ServerSocket(8000, 5);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //threads
        Runtime.getRuntime().addShutdownHook(new Thread(new HttpServer()));

        try {
            while (true) {
                Socket s = listener.accept();

                ArrayList<String> header = new ArrayList<>();

                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String message;
                do {
                    message = reader.readLine();
                    header.add(message);
                    System.out.println("srv: received: " + message);
                } while (!"quit".equals(message) && !message.isEmpty());
                //(line != null && !line.isEmpty())

                HttpRequest requestContext = new HttpRequest(header);

                // Read Body
                if (requestContext.getBodyLength() > 0) {
                    int read;
                    StringBuffer sb = new StringBuffer();
                    while ((read = reader.read()) != -1) {
                        sb.append((char) read);
                        if (sb.length() == requestContext.getBodyLength())
                            break;
                    }
                    requestContext.setBody(sb.toString());
                } else {
                    requestContext.setBody("");
                }

                try {
                    RequestHandler requestHandler = RequestHandler.builder()
                            .requestContext(requestContext)
                            .status(StatusCode.OK)
                            .messages(messages) //private static
                            .newMessage(null)
                            .build();
                    HttpResponse response = requestHandler.handleRequest();
                    if (requestHandler.getNewMessage() != null)
                        messages.put(messages.size(), requestHandler.getNewMessage());

                    // Write Response to Client
                    OutputStream outputStream = s.getOutputStream();
                    outputStream.write(response.getResponse().getBytes());
                    outputStream.flush();
                    outputStream.close();
                } catch (BadRequestException e) { // Auch eine NotFoundException hinzufügen
                    OutputStream outputStream = s.getOutputStream();
                    outputStream.write("HTTP/1.1 400 Bad Request\r\n\r\n".getBytes());
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    OutputStream outputStream = s.getOutputStream();
                    outputStream.write("Response with 500 Code & exception as body".getBytes()); // e.getMessage();
                    outputStream.flush();
                    outputStream.close();
                }
                //close connection
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



//    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//                System.out.println("srv: sending welcome message");
//                writer.write("Welcome to myserver!");
//                writer.newLine();
//                writer.write("Please enter your commands...");
//                writer.flush();

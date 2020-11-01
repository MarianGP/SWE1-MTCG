package server;

import lombok.Builder;
import server.enums.HttpMethod;
import server.enums.StatusCode;
import server.model.HttpRequest;
import server.model.HttpResponse;
import server.model.RequestHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Builder
public class HttpServer implements Runnable {

    private static ServerSocket listener = null;
    private static Map<Integer, String> messages = new HashMap<Integer, String>(); //temp no databank

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

                //Read Header
                do {
                    message = reader.readLine();
                    header.add(message);
                    System.out.println("received: " + message);
                } while (!"quit".equals(message) && !message.isEmpty());
                //(line != null && !line.isEmpty())

                try {
                    HttpRequest requestContext = new HttpRequest(header);

                    // Read Body
                    if (
                            requestContext.getBodyLength() != null
                            && Integer.parseInt(requestContext.getBodyLength(), 10) > 0
                            && (requestContext.getMethod() == HttpMethod.POST || requestContext.getMethod() == HttpMethod.PUT)
                    )
                    {
                        int read;
                        StringBuffer sb = new StringBuffer();
                        while ((read = reader.read()) != -1) {
                            sb.append((char) read);
                            if (sb.length() == Integer.parseInt(requestContext.getBodyLength(),10)) {
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
                                .messages(messages)
                                .objectName(null)
                                .build();

                        HttpResponse response = requestHandler.handleRequest();

                        // Safe new message in messages
                        try {
                            if (!requestContext.getBody().isEmpty() && requestContext.getMethod() == HttpMethod.POST && requestHandler.getMessages().size() > 0)
                                messages = requestHandler.getMessages();
                        } catch (Exception e) {
                            System.out.println("Error saving new msg");
                        }

                        // Write Response to Client
                        outputStream.write(response.getResponse().getBytes());

                    } catch (Exception e) {
                        outputStream.write("Response with 500 Code & exception as body".getBytes()); // e.getMessage();
                    }
                    outputStream.flush();
                    outputStream.close();

                } catch (IOException e){
                    System.out.println("Error reading header request or body");
                }
                reader.close();
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
//    System.out.println("srv: sending welcome message");
//    writer.newLine();
//    writer.flush();

package server;

import lombok.Builder;
import server.enums.HttpMethod;
import server.enums.StatusCode;
import server.model.HttpRequest;
import server.model.HttpResponse;
import server.model.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Builder
public class HttpServer implements Runnable {

    private static ServerSocket listener = null;
    private static Map<String, String> messages = Collections.synchronizedMap(new HashMap<>()); //temp no DB, synchronized

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
                String line;

                //Read Header
                do {
                    line = reader.readLine();
                    header.add(line);
                    System.out.println(line);
                } while (line != null && !line.isEmpty());

                try {
                    HttpRequest requestContext = new HttpRequest(header);

                    // Read Body
                    if (
                            requestContext.getBodyLength() > 0 &&
                            (
                                requestContext.getMethod() == HttpMethod.POST
                                || requestContext.getMethod() == HttpMethod.PUT
                            )
                    )
                    {
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
                                .objectsList(messages)
                                .objectName(null)
                                .pathPair(null)
                                .build();

                        HttpResponse response = requestHandler.handleRequest();

                        // Safe new message in messages
                        try {
                            if (!requestContext.getBody().isEmpty() && requestContext.getMethod() == HttpMethod.POST
                                    && requestHandler.getObjectsList().size() > 0)
                                messages = requestHandler.getObjectsList();
                        } catch (Exception e) {
                            System.out.println("Error saving new msg");
                        }

                        // Write Response to Client
                        outputStream.write(response.getResponse().getBytes());

                    } catch (Exception e) {
                        outputStream.write("Response with 500 Code & exception as body".getBytes()); // e.getMessage();
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



//    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//    System.out.println("srv: sending welcome message");
//    writer.newLine();
//    writer.flush();

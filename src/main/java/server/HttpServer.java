package server;

import server.model.HttpServerHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HttpServer implements Runnable {

    private static ServerSocket listener = null;

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
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//                System.out.println("srv: sending welcome message");
//                writer.write("Welcome to myserver!");
//                writer.newLine();
//                writer.write("Please enter your commands...");
//                writer.newLine();
//                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String message;
                do {
                    message = reader.readLine();
                    header.add(message);
                    System.out.println("srv: received: " + message);
                } while (!"quit".equals(message) && !message.isEmpty());
                //(line != null && !line.isEmpty())
                String body = reader.readLine();

                if(body.isEmpty()) {
                    body = null;
                }

                RequestHandler handler = new HttpServerHandler(header, body);
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

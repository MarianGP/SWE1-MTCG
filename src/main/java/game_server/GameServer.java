package game_server;

import game_server.controller.GameController;
import lombok.Builder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

@Builder
public class GameServer implements Runnable {

    private static ServerSocket listener = null;
    private static GameController gameController = new GameController(new ArrayList<>(), false, null);


    public static void main(String[] args) {
        System.out.println("start server");

        try {
            listener = new ServerSocket(10001, 5); //8080
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new GameServer()));

        try {
            while (true) {
                Socket s = listener.accept();
                new Thread(new RequestThread(s, gameController)).start();
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
package game_server.interfaces;

import game.user.User;

public interface Playable {
    void startGame() throws InterruptedException;
    void addPlayer(User player);
}

package game.interfaces;

import game.user.User;

public interface Defeatable {
    User checkWinner(User currentPlayer, User nextPlayer);
    User getLoser(User currentPlayer, User nextPlayer);
}

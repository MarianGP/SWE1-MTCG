package game.battle;

import game.cards.Card;
import game.cards.MonsterCard;
import game.decks.CardDeck;
import game.decks.CardStack;
import game.enums.CardName;
import game.enums.Element;
import game.enums.MonsterType;
import game.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


class BattleTest {
    @Mock

    User player1 = User.builder().username("Player1").password("").token("")
            .bio(":/").image(":/").coins(20).elo(100).stack(new CardStack())
            .deck(new CardDeck()).isAdmin(false).build();

    User player2 = User.builder().username("Player2").password("").token("")
            .bio(":/").image(":/").coins(20).elo(100).stack(new CardStack())
            .deck(new CardDeck()).isAdmin(false).build();

    User player3 = User.builder().username("Player3").password("").token("")
            .bio(":/").image(":/").coins(20).elo(100).stack(new CardStack())
            .deck(new CardDeck()).isAdmin(false).build();

    User player4 = User.builder().username("Player4").password("").token("")
            .bio(":/").image(":/").coins(20).elo(100).stack(new CardStack())
            .deck(new CardDeck()).isAdmin(false).build();


    Card wizzard = new MonsterCard(MonsterType.WIZZARD, Element.FIRE, CardName.ONE, 100.0f);
    Card elf = new MonsterCard(MonsterType.ELF, Element.FIRE, CardName.ONE, 120.0f);

    Battle newBattle = new Battle(player1, player2, 100);
    Battle anotherBattle = new Battle(player3, player4, 100);
    Battle nextBattle = new Battle(player3, player2, 100);

    @BeforeEach
    void setUp() {
        player1.buyPackage();
        player2.buyPackage();
        player3.buyPackage();
        player4.buyPackage();

        player1.buyPackage();
        player2.buyPackage();
        player3.buyPackage();
        player4.buyPackage();
        //10 cards in stack

        player1.prepareDeck();
        player2.prepareDeck();
        player3.prepareDeck();
        player4.prepareDeck();
        //5 cards in deck
    }

    @Test
    @DisplayName("Check if their is a winner, loser, none yet")
    void returnWinnerPlayer2() {
        Assertions.assertEquals(null,anotherBattle.checkWinner(player3, player4));
        player4.getDeck().clearDeck();
        Assertions.assertEquals(0, player4.getDeck().getDeckList().size());
        Assertions.assertEquals(4, player3.getDeck().getDeckList().size());
        Assertions.assertEquals(player3,anotherBattle.checkWinner(player3, player4));
        Assertions.assertEquals(player4,anotherBattle.getLoser(player3, player4));

    }

    @Test
    @DisplayName("Swap Players: CurrentPlayer becomes NextPlayer")
    void tryToSwapAttackers() {
        User current = newBattle.getCurrentPlayer();
        User next = newBattle.getNextPlayer();
        newBattle.swapAttacker(newBattle.getCurrentPlayer(),newBattle.getNextPlayer());
        Assertions.assertEquals(current, newBattle.getNextPlayer());
        Assertions.assertEquals(next, newBattle.getCurrentPlayer());
    }

    @Test
    @DisplayName("Compare cards and move to round-winner: Player Next plays a stronger Card")
    void moveDefeatedCard() {
        Assertions.assertEquals(4, nextBattle.getCurrentPlayer().getDeck().getDeckList().size());
        Assertions.assertEquals(4, nextBattle.getNextPlayer().getDeck().getDeckList().size());

        nextBattle.compareCards(wizzard,elf);
        Assertions.assertEquals(6, nextBattle.getNextPlayer().getDeck().getDeckList().size());

        nextBattle.compareCards(elf,wizzard);
        Assertions.assertEquals(6, nextBattle.getCurrentPlayer().getDeck().getDeckList().size());
    }

    @Test
    @DisplayName ("A few rounds Round")
    void playOneRound() {
        nextBattle.startBattle();
        Assertions.assertEquals(0, newBattle.getRounds());
    }

}
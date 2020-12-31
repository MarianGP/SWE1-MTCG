package game.battle;

import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.SpellCard;
import game.enums.Element;
import game.enums.MonsterType;
import game.enums.Name;
import game.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


class BattleTest {
    @Mock
    User player1 = User.builder().username("Player1").build();
    User player2 = User.builder().username("Player2").build();
    User player3 = User.builder().username("Player3").build();
    User player4 = User.builder().username("Player4").build();

    Card wizzard = new MonsterCard(MonsterType.WIZZARD, Element.FIRE, Name.ONE, 100.0f);
    Card ork = new MonsterCard(MonsterType.ORK, Element.FIRE, Name.ONE, 100.0f);
    Card dragon = new MonsterCard(MonsterType.DRAGON, Element.FIRE, Name.ONE, 100.0f);
    Card elf = new MonsterCard(MonsterType.ELF, Element.FIRE, Name.ONE, 120.0f);
    Card water = new SpellCard(Element.WATER, Name.FIVE, 50.0f);
    Card fire = new SpellCard(Element.FIRE, Name.FIVE, 200.0f);
    Card normal = new SpellCard(Element.NORMAL, Name.FIVE, 200.0f);

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
        Assertions.assertEquals(0, player4.getDeck().getDeck().size());
        Assertions.assertEquals(5, player3.getDeck().getDeck().size());
        Assertions.assertEquals(player3,anotherBattle.checkWinner(player3, player4));
        Assertions.assertEquals(player4,anotherBattle.getLoser(player3, player4));

    }

    @Test
    @DisplayName("")
    void checkIfWinner() {
//        when(newBattle.checkWinner(player1, player2).thenReturn(false));
//        verify(newBattle.checkWinner(player1, player2);
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
        Assertions.assertEquals(5, nextBattle.getCurrentPlayer().getDeck().getDeck().size());
        Assertions.assertEquals(5, nextBattle.getNextPlayer().getDeck().getDeck().size());

        nextBattle.compareCards(wizzard,elf);
        Assertions.assertEquals(7, nextBattle.getNextPlayer().getDeck().getDeck().size());

        nextBattle.compareCards(elf,wizzard);
        Assertions.assertEquals(7, nextBattle.getCurrentPlayer().getDeck().getDeck().size());
    }

    @Test
    @DisplayName ("One Round ")
    void playOneRound() {
        Battle newBattle = new Battle(player2, player3, 2);
        newBattle.startBattle();
        Assertions.assertEquals(2, newBattle.getRounds());
    }

}
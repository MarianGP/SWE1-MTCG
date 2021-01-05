package game.cards;

<<<<<<< HEAD
import game.decks.CardDeck;
import game.decks.CardStack;
=======
import game.decks.CardStack;
import game.enums.Element;
import game.enums.MonsterType;
>>>>>>> integration-game-server
import game.interfaces.Attackable;
import game.interfaces.CardInterface;
import lombok.Getter;

<<<<<<< HEAD
public abstract class Card extends CardStack implements CardInterface, Attackable {

=======
@Getter
>>>>>>> integration-game-server

public abstract class Card extends CardStack implements CardInterface, Attackable {
    private String cid;
    private String name;
    private MonsterType type;
    private Element cardElement;
    private float damage;
    private boolean locked;
    private String owner;
}

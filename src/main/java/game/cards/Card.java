package game.cards;


import game.enums.Element;
import game.enums.MonsterType;
import game.interfaces.Attackable;
import game.interfaces.CardInterface;
import lombok.Data;


@Data
public abstract class Card implements CardInterface, Attackable {
    private String cid;
    private String name;
    private MonsterType type;
    private Element cardElement;
    private float damage;
    private boolean locked;
    private String owner;
}

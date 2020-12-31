package game.decks;

import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.SpellCard;
import game.enums.Element;
import game.enums.MonsterType;
import game.enums.Name;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter

public class Package {

    private List<Card> cardsInPackage;
    private int price;

    public Package(){
        this.price = 5;
        Random random = new Random();
        Card temp;
        this.cardsInPackage = new ArrayList<Card>();

        for (int i = 0; i < 5; i++) {

            if(random.nextInt(10)%2 == 0) {
                Element tempElement = Element.randomElement();
                temp = new MonsterCard(MonsterType.randomMonsterType(),tempElement,Name.randomName(), tempElement.getMaxDamage());
            } else {
                temp = new SpellCard(Element.randomElement(),Name.randomName());
            }
            this.cardsInPackage.add(temp);
        }

    }


}

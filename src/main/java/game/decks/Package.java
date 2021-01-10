package game.decks;

import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.SpellCard;
import game.enums.Element;
import game.enums.MonsterType;
import game.enums.CardName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter

public class Package {

    private List<Card> cardsInPackage;
    private final int PRICE = 5;

    public Package(){
        Random random = new Random();
        Card temp;
        this.cardsInPackage = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            if(random.nextInt(10)%2 == 0) {
                Element tempElement = Element.randomElement();
                temp = new MonsterCard(MonsterType.randomMonsterType(),tempElement, CardName.randomName(), tempElement.getMaxDamage());
            } else {
                temp = new SpellCard(Element.randomElement(), CardName.randomName());
            }
            this.cardsInPackage.add(temp);
        }
    }

    public Package(List<Card> packageDB){
        this.cardsInPackage = new ArrayList<>();

        packageDB.forEach((temp) -> {
            if(temp.getType() != null) {
                Element tempElement = Element.randomElement();
                temp = new MonsterCard(MonsterType.randomMonsterType(),tempElement, CardName.randomName(), tempElement.getMaxDamage());
            } else {
                temp = new SpellCard(Element.randomElement(), CardName.randomName());
            }
            this.cardsInPackage.add(temp);
        });

    }


}

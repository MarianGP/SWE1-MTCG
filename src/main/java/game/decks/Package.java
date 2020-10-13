package game.decks;

import game.cards.MonsterCard;
import game.cards.SpellCard;
import game.enums.Element;
import game.enums.MonsterType;
import game.enums.Name;
import game.interfaces.CardInterface;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter

public class Package {

    private ArrayList<CardInterface> cardsInPackage;
    private int price;

    public Package(){
        this.price = 5;
        Random random = new Random();
        CardInterface temp;
        this.cardsInPackage = new ArrayList<CardInterface>();

        for (int i = 0; i < 5; i++) {
            if(random.nextInt(10)%2 == 0) {
                temp = new MonsterCard(MonsterType.randomMonsterType(), Element.randomElement(),Name.randomName());
                System.out.println(((MonsterCard)temp).getName());
            } else {
                temp = new SpellCard(Element.randomElement(),Name.randomName());
                System.out.println( i + ":" + ((SpellCard)temp).getName());
            }
            cardsInPackage.add(temp);
        }
    }

    public List<CardInterface> getCardsInPackage() {
        return this.cardsInPackage;
    }



}

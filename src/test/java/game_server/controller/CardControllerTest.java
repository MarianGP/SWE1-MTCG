package game_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.cards.Card;
import game.cards.MonsterCard;
import game.enums.CardName;
import game.enums.Element;
import game.enums.MonsterType;
import game_server.db.DbConnection;
import game_server.serializer.CardData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

class CardControllerTest {

    @Mock
    List<Card> cardList = new ArrayList<>();
    Card wizzard = new MonsterCard("id1", MonsterType.WIZZARD, Element.FIRE, CardName.ONE.getName(), 100.0f, false, "yo");
    Card ork = new MonsterCard("id2",MonsterType.ORK, Element.FIRE, CardName.ONE.getName(), 100.0f, false, "yo");
    Card dragon = new MonsterCard("id3",MonsterType.DRAGON, Element.FIRE, CardName.ONE.getName(), 100.0f, false, "yo");
    Card elf = new MonsterCard("id4",MonsterType.ELF, Element.FIRE, CardName.ONE.getName(), 120.0f, false, "yo");

    String json = "[\n" +
            "    {\n" +
            "        \"id\": \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\n" +
            "        \"monsterType\": \"\",\n" +
            "        \"element\": \"Normal\",\n" +
            "        \"name\": \"North Wind\",\n" +
            "        \"damage\": 90.0\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\n" +
            "        \"monsterType\": \"Goblin\",\n" +
            "        \"element\": \"Fire\",\n" +
            "        \"name\": \"Supreme\",\n" +
            "        \"damage\": 100.0\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\n" +
            "        \"monsterType\": \"Dragon\",\n" +
            "        \"element\": \"Fire\",\n" +
            "        \"name\": \"Extrem\",\n" +
            "        \"damage\": 100.0\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"e85e3976-7c86-4d06-9a80-641c2019a79f\",\n" +
            "        \"monsterType\": \"\",\n" +
            "        \"element\": \"Fire\",\n" +
            "        \"name\": \"Supreme\",\n" +
            "        \"damage\": 100.0\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"fdd758f-649c-40f9-ba3a-8657f4b3439f\",\n" +
            "        \"monsterType\": \"\",\n" +
            "        \"element\": \"Water\",\n" +
            "        \"name\": \"Blue\",\n" +
            "        \"damage\": 105.0\n" +
            "    }\n" +
            "]";
    CardController cardController = new CardController(new DbConnection());

    @Test
    void testGetCardsListJson() throws JsonProcessingException {
        cardList.add(wizzard);
        cardList.add(ork);
        cardList.add(dragon);
        cardList.add(elf);
        //System.out.println(cardController.getCardsListJson(cardList));

        CardData[] listCustomCard = cardController.deserializeCardListToObject(cardController.getCardsListJson(cardList));
        Card card = cardController.buildCard(listCustomCard[0], "yo", false);
        Assertions.assertEquals("id1", card.getCid());
    }


    void testGetCardObjectFromJson() throws JsonProcessingException {
        CardData[] listCustomCard = cardController.deserializeCardListToObject(json);
        Card card = cardController.buildCard(listCustomCard[0], "marian", false);

        Assertions.assertEquals("North Wind", card.getName());
        Assertions.assertEquals(Element.NORMAL, card.getCardElement());
        Assertions.assertEquals(null, card.getType());

        card = cardController.buildCard(listCustomCard[listCustomCard.length-1], "marian", false);

        Assertions.assertEquals("Blue", card.getName());
        Assertions.assertEquals(Element.WATER, card.getCardElement());
        Assertions.assertEquals(null, card.getType());
    }
}
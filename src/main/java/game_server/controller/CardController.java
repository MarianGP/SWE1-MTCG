package game_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.SpellCard;
import game.enums.Element;
import game.enums.MonsterType;
import game.user.User;
import game_server.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import game_server.serializer.CustomCardSerializer;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CardController {
    private DbConnection db;
    private Card card;
    private List<Card> cardList;


    public CustomCardSerializer[] serializeCard(String jsonCardsArray) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        return objectMapper.readValue(jsonCardsArray, CustomCardSerializer[].class);
    }

    public boolean insertJSONCards(String jsonCardsArray, User user) throws JsonProcessingException {
        CustomCardSerializer[] listCustomCard = serializeCard(jsonCardsArray);
        int packageID = db.getMaxPackageId() + 1;
        for(CustomCardSerializer temp: listCustomCard) {
            Card card = buildCustomCard(temp);
            if (!db.insertCard(card, false, user, temp.getId(), packageID)) {
                return false;
            }
        }
        return true;
    }

    private  Card buildCustomCard(CustomCardSerializer customCard) {
        Card card;
        if ( customCard.getMonsterType() == null || customCard.getMonsterType().isEmpty()) {
            card = new SpellCard(
                    customCard.getId(),
                    Element.find(customCard.getElement()),
                    customCard.getName(),
                    customCard.getDamage());
        } else {
            card = new MonsterCard(
                    customCard.getId(),
                    MonsterType.find(customCard.getMonsterType()),
                    Element.find(customCard.getElement()),
                    customCard.getName(),
                    customCard.getDamage());
        }
        return card;
    }

    public StringBuilder getAllCardsStats(List<Card> stackList) {
        StringBuilder all = new StringBuilder();

        System.out.println("Your Card's Stack");
        for (Card temp: stackList) {
            all.append(temp.getCardStats());
        }

        return all;
    }

}

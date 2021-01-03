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
import game_server.serializer.CustomCardSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CardController {
    private DbConnection db;

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
            Card card = buildCustomCard(temp, user.getUsername(), false);
            if (!db.insertCard(card, false, user, temp.getId(), packageID)) {
                return false;
            }
        }
        return true;
    }

    private Card buildCustomCard(CustomCardSerializer customCard, String username, boolean inDeck) {
        Card card;
        if ( customCard.getMonsterType() == null || customCard.getMonsterType().isEmpty()) {
            card = new SpellCard(
                    customCard.getId(),
                    Element.find(customCard.getElement()),
                    customCard.getName(),
                    customCard.getDamage(),
                    inDeck,
                    username);
        } else {
            card = new MonsterCard(
                    customCard.getId(),
                    MonsterType.find(customCard.getMonsterType()),
                    Element.find(customCard.getElement()),
                    customCard.getName(),
                    customCard.getDamage(),
                    inDeck,
                    username);
        }
        return card;
    }

    public StringBuilder getCardListStats(List<Card> cardList, String deckName) {
        StringBuilder all = new StringBuilder();

        System.out.println("Your "+ deckName + " Cards: " );
        for (Card temp: cardList) {
            all.append(temp.getCardStats());
        }
        System.out.println(all);
        return all;
    }

    public boolean deleteCardsList(List <Card> cardList)  {
        for(Card temp: cardList) {
            db.deleteCard(temp.getCid());
        }
        //TODO: maybe use it
        return false;
    }

}

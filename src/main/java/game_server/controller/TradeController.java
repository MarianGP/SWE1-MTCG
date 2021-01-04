package game_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.cards.Card;
import game.cards.SpellCard;
import game_server.db.DbConnection;
import game_server.serializer.TradeData;
import game.trade.Trade;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Data
public class TradeController {
    DbConnection db;

    public StringBuilder getTradesSummary() { //TODO: list not empty
        List <String> allTradesId = db.getAllTradesId();
        StringBuilder all = new StringBuilder();
        Card card;
        int i = 1;
        if(allTradesId != null) {
            for(String id: allTradesId) {
                card = db.getCardById(id);
                all.append(i++ + ") ");
                all.append(card.getCardStats());
                all.append(getTradeRequirements(card.getCid()));
            }
        } else {
            return null;
        }
        System.out.println(all);
        return all;
    }

    public String getTradeRequirements(String cid) {
        Trade trade =  db.getTradeByCardId(cid);
        return trade.tradeSummary();
    }

    public String tradeCards(String wantedCardId, String offeredCardId, String username) {
        Trade wantedCardTrade =  db.getTradeByCardId(wantedCardId);
        Card offeredCard = db.getCardById(offeredCardId);

        if(wantedCardTrade == null)
            return "The card you want is not on the trading cart";

        if(offeredCard == null)
            return "A card with this Id doesn't exist";

        if(offeredCard.isLocked())
            return "You can't trade cards from your deck";

        if(wantedCardTrade.getOwner().equals(username))
            return "You can't trade cards with yourself";

        if(!offeredCard.getOwner().equals(username))
            return "You can't offer cards you don't own";

        if( wantedCardTrade.isSpell() != offeredCard instanceof SpellCard)
            return "Trade not accepted. Type offered doesn't match with required";

        if (wantedCardTrade.getMinDamage() > offeredCard.getDamage())
            return "Trade not accepted. Damage offered < Damage min";

        db.setCardOwner(wantedCardId, username);
        db.deleteTrade(wantedCardId);

        db.setCardOwner(offeredCardId, wantedCardTrade.getOwner());
        db.deleteTrade(offeredCard.getCid()); //if already in trade delete it

        return null;
    }

    public String addNewTrade(String json, String username) throws JsonProcessingException, SQLException {
        TradeData tradeSerialized = getParsedTrade(json);
        boolean isSpell = (!tradeSerialized.getType().equals("monster"));
        Trade trade = Trade.builder()
                .cardId(tradeSerialized.getCardToTrade())
                .minDamage(tradeSerialized.getMinimumDamage())
                .owner(username)
                .isSpell(isSpell)
                .build();

        if(!db.getCardById(tradeSerialized.getCardToTrade()).getOwner().equals(username))
            return "You don't own the card you want to trade";

        if(db.getTradeByCardId(trade.getCardId()) != null)
            return "This card is already in the trading market";

        if(db.insertTrade(trade)) {
            return null;
        }  else {
            return "Couldn't add card to trading market";
        }
    }

    public TradeData getParsedTrade(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, TradeData.class);
    }

}

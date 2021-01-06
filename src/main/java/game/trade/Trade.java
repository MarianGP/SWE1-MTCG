package game.trade;

import game.cards.Card;
import game.cards.SpellCard;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Trade {
    private String cardId;
    private float minDamage;
    private boolean isSpell;
    private String owner;

    public String tradeSummary() {
        String type = (this.isSpell) ? "Spell" : "Monster";
        return  "\t\tTrading Summary: -> " +
                "Min Damage: "  + this.minDamage +
                " - Type: " + type +
                " - Card owner: " + this.owner + "\n\n";
    }

    public String validateTrade(Card offeredCard, String username) {

        if (this == null)
            return "The card you want is not on the trading cart";

        if (offeredCard == null)
            return "A card with this Id doesn't exist";

        if (offeredCard.isLocked())
            return "You can't trade cards from your deck";

        if (this.getOwner().equals(username))
            return "You can't trade cards with yourself";

        if (!offeredCard.getOwner().equals(username))
            return "You can't offer cards you don't own";

        if (this.isSpell() != offeredCard instanceof SpellCard)
            return "Trade not accepted. Type offered doesn't match with required";

        if (this.getMinDamage() > offeredCard.getDamage())
            return "Trade not accepted. Damage offered < Damage min";

        return null;
    }
}

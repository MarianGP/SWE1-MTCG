package game_server.trade;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
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

}

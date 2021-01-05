package game_server.serializer;

import lombok.Getter;

@Getter
public class TradeData {
    private String cardToTrade;
    private String type;
    private float minimumDamage;

}

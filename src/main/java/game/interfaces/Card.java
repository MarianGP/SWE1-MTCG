package game.interfaces;

public interface Card extends Attackable {
    public void printCardStats();
    public boolean attack(Card attacker);
}

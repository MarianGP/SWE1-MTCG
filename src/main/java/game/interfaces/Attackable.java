package game.interfaces;

public interface Attackable {
    public boolean receiveAttack(Card attacker);
    public void receiveDamage();
}

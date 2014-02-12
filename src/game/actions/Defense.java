package game.actions;

import game.CardType;
import game.Player;

public interface Defense {
	
	public void defendAgainstPlayer(Player player);
	public CardType cardTypeRequired();

}

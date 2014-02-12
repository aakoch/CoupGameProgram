package game.actions;

import game.CardType;
import game.Player;

import java.util.List;

public interface Action {

	public void performAction(Player player);
	public List<Player> targetedPlayers(); //FIXME Change to players who can block?
	public CardType cardTypeRequired();
}

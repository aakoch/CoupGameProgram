package game.actions;

import game.CardType;
import game.Player;

import java.util.List;

//FIXME Change this into an enumeration since there are fixed actions?
public interface Action {

	public void performAction(Player player);
	public List<Player> targetedPlayers(); //FIXME Change to players who can block?
	public CardType cardTypeRequired();
	public List<Defense> defensesThatCanBlock();
	public boolean canPerformAction(Player player); //TODO test this??
	public String actionDescription();
}

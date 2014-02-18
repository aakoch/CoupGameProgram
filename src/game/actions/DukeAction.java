package game.actions;

import game.CardType;
import game.Player;

import java.util.ArrayList;
import java.util.List;

public class DukeAction implements Action {

	@Override
	public void performAction(Player player) {
		player.takeActionDuke();
	}

	@Override
	public List<Player> targetedPlayers() {
		return null;
	}

	@Override
	public CardType cardTypeRequired() {
		return CardType.duke;
	}

	@Override
	public boolean canPerformAction(Player player) {
		return player.getCoins() < 10;
	}

	@Override
	public String actionDescription() {
		return "Duke: gain three coins, unblockable";
	}

	@Override
	public List<Defense> defensesThatCanBlock() {
		return new ArrayList<Defense>();
	}
	
	
}

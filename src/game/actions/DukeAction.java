package game.actions;

import game.CardType;
import game.Player;

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
		return true;
	}

	@Override
	public String actionDescription() {
		return "Duke: gain three coins, unblockable";
	}
	
	
}

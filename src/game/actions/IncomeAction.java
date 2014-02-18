package game.actions;

import game.CardType;
import game.Player;

import java.util.List;

public class IncomeAction implements Action {

	@Override
	public void performAction(Player player) {
		player.takeActionIncome();
	}

	@Override
	public List<Player> targetedPlayers() {
		return null;
	}

	@Override
	public CardType cardTypeRequired() {
		return null;
	}

	@Override
	public boolean canPerformAction(Player player) {
		return player.getCoins() < 10;
	}

	@Override
	public String actionDescription() {
		return "Income: gain one coin, unblockable";
	}

}

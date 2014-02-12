package game.actions;

import game.CardType;
import game.Player;

import java.util.List;

public class CoupAction implements Action {

	private final Player playerToBeCouped;

	public CoupAction(Player playerToBeCouped) {
		this.playerToBeCouped = playerToBeCouped;
	}

	@Override
	public void performAction(Player player) {
		player.takeActionCoup();
		playerToBeCouped.revealACard();
	}

	@Override
	public List<Player> targetedPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CardType cardTypeRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override //FIXME Need to have other actions not performable if have 10 or more coins
	public boolean canPerformAction(Player player) {
		return player.getCoins() >= 7;
	}

	@Override
	public String actionDescription() {
		return "Coup: pay 7 coins and choose player to lose influence, unblockable";
	}
	
}

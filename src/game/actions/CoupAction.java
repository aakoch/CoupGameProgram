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
	
}

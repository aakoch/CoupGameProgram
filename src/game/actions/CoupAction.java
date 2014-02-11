package game.actions;

import game.Player;

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
	public Player targetedPlayer() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

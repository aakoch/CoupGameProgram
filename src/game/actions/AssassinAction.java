package game.actions;

import game.Player;

public class AssassinAction implements Action {

	private final Player playerToAssassinate;

	public AssassinAction(Player playerToAssassinate) {
		this.playerToAssassinate = playerToAssassinate;
	}

	@Override
	public void performAction(Player playerDoingAssassinating) {
		// TODO Auto-generated method stub
		playerDoingAssassinating.takeActionAssassin();
		playerToAssassinate.revealACard();
	}

	@Override
	public Player targetedPlayer() {
		return playerToAssassinate;
	}
	
}

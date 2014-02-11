package game.actions;

import game.Player;

public class IncomeAction implements Action {

	@Override
	public void performAction(Player player) {
		player.takeActionIncome();
	}

	@Override
	public Player targetedPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

}

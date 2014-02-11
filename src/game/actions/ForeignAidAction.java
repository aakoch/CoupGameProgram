package game.actions;

import game.Player;

public class ForeignAidAction implements Action {

	@Override
	public void performAction(Player player) {
		player.takeActionForeignAid();
	}

	@Override
	public Player targetedPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

}

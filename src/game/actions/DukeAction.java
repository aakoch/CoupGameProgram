package game.actions;

import game.Player;

public class DukeAction implements Action {

	@Override
	public void performAction(Player player) {
		player.takeActionDuke();
	}

	@Override
	public Player targetedPlayer() {
		// TODO Should return all players??  Anyone has a chance to block this...
		return null;
	}
	
	
}

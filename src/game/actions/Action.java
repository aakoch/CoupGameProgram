package game.actions;

import game.Player;

public interface Action {

	public void performAction(Player player);
	public Player targetedPlayer();
}

package game.actions;

import game.Player;

public class CaptainAction implements Action {

	private final Player playerFromWhomToSteal;

	public CaptainAction(Player playerFromWhoToSteal) {
		this.playerFromWhomToSteal = playerFromWhoToSteal;
	}

	@Override
	public void performAction(Player player) {
		player.takeActionCaptain(playerFromWhomToSteal);
	}

	@Override
	public Player targetedPlayer() {
		return playerFromWhomToSteal;
	}
	
}

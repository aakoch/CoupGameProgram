package game.actions;

import game.CardType;
import game.Player;

import java.util.Arrays;
import java.util.List;

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
	public List<Player> targetedPlayers() {
		return Arrays.asList(playerFromWhomToSteal);
	}

	@Override
	public CardType cardTypeRequired() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

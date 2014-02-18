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
		return CardType.captain;
	}

	@Override
	public boolean canPerformAction(Player player) {
		return player.getCoins() < 10  && !playerFromWhomToSteal.eliminated();
	}

	@Override
	public String actionDescription() {
		return "Captain: steal two coins from " + playerFromWhomToSteal + ", blockable by captain and ambassador";
	}

	@Override
	public List<Defense> defensesThatCanBlock() {
		return Arrays.asList(new CaptainDefense(), new AmbassadorDefense());
	}
	
}

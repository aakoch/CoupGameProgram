package game.actions;

import game.CardType;
import game.Player;

import java.util.ArrayList;
import java.util.List;

public class ForeignAidAction implements Action {
	
	private final List<Player> otherPlayers;

	public ForeignAidAction(Player playerDoingAction, List<Player> allPlayers){
		this.otherPlayers = new ArrayList<Player>(allPlayers);
		this.otherPlayers.remove(playerDoingAction);
	}

	@Override
	public void performAction(Player player) {
		player.takeActionForeignAid();
	}

	@Override
	public List<Player> targetedPlayers() {
		return otherPlayers;
	}

	@Override
	public CardType cardTypeRequired() {
		return null;
	}

	@Override
	public boolean canPerformAction(Player player) {
		return player.getCoins() < 10;
	}

	@Override
	public String actionDescription() {
		return "Foreign Aid: gain two coins, blockable by duke";
	}

}

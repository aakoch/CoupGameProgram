package game.actions;

import game.CardType;
import game.Player;

public class AmbassadorDefense implements Defense {

	@Override
	public void defendAgainstPlayer(Player player) {
		// Nothing happens
	}

	@Override
	public CardType cardTypeRequired() {
		return CardType.ambassador;
	}

}

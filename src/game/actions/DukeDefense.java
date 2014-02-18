package game.actions;

import game.CardType;
import game.Player;

public class DukeDefense implements Defense {

	@Override
	public void defendAgainstPlayer(Player player) {

	}

	@Override
	public CardType cardTypeRequired() {
		return CardType.duke;
	}

}

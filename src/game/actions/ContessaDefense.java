package game.actions;

import game.CardType;
import game.Player;

public class ContessaDefense implements Defense {

	@Override
	public void defendAgainstPlayer(Player player) {
		//Other player still has to pay for assassination
		player.takeActionAssassin();
	}

	@Override
	public CardType cardTypeRequired() {
		return CardType.contessa;
	}
	

}

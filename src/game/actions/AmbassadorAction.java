package game.actions;

import java.util.List;

import game.Card;
import game.CardPair;
import game.CardType;
import game.Game;
import game.Player;

public class AmbassadorAction implements Action {

	private final Game game;
	private final CardChooser cardChooser;

	public AmbassadorAction(Game game,
			CardChooser cardChooser) {
				this.game = game;
				this.cardChooser = cardChooser;
	}

	@Override
	public void performAction(Player player) {
		// TODO Auto-generated method stub
		List<Card> playerCanChooseFromTheseCardsAsAmbassador = this.game.playerCanChooseFromTheseCardsAsAmbassador(player);
		CardPair chooseCards = this.cardChooser.chooseCards(playerCanChooseFromTheseCardsAsAmbassador, player);
		game.playerChoosesTheseCardsAsAmbassador(player, chooseCards.getFirstCard(), chooseCards.getSecondCard());
	}

	@Override
	public List<Player> targetedPlayers() {
		return null;
	}

	@Override
	public CardType cardTypeRequired() {
		return null;
	}

}

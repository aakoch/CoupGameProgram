package game.actions;

import game.Card;
import game.CardPair;
import game.CardType;
import game.Game;
import game.Player;

import java.util.ArrayList;
import java.util.List;

public class AmbassadorAction implements Action {

	private final Game game;
	private final CardChooser cardChooser;

	public AmbassadorAction(Game game,
			CardChooser cardChooser) {
				this.game = game;
				this.cardChooser = cardChooser;
	}

	//FIXME Clean this up!
	@Override
	public void performAction(Player player) {
		List<Card> playerCanChooseFromTheseCardsAsAmbassador = this.game.playerCanChooseFromTheseCardsAsAmbassador(player);
		CardPair chosenCards = null;
		if(playerCanChooseFromTheseCardsAsAmbassador.size() == 4){
			chosenCards = this.cardChooser.chooseCards(playerCanChooseFromTheseCardsAsAmbassador, player);
		}else{
			if(playerCanChooseFromTheseCardsAsAmbassador.contains(player.getFirstCard())){
				chosenCards = this.cardChooser.chooseCards(playerCanChooseFromTheseCardsAsAmbassador, player, player.getSecondCard());
			}else{
				chosenCards = this.cardChooser.chooseCards(playerCanChooseFromTheseCardsAsAmbassador, player, player.getFirstCard());
			}
		}
		game.playerChoosesTheseCardsAsAmbassador(player, chosenCards);
	}

	@Override
	public List<Player> targetedPlayers() {
		return null;
	}

	@Override
	public CardType cardTypeRequired() {
		return CardType.ambassador;
	}

	@Override
	public boolean canPerformAction(Player player) {
		return player.getCoins() < 10;
	}

	@Override
	public String actionDescription() {
		return "Ambassador: draw two cards, choose two cards from these and your current cards, unblockable";
	}

	@Override
	public List<Defense> defensesThatCanBlock() {
		return new ArrayList<Defense>();
	}
	

}

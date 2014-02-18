package game.ui.javafx;

import game.CardType;
import game.Player;
import game.actions.Defense;

import java.util.List;

public class PlayerWithChoices extends Player {
	
	private IndividualPlayer playerUi;

	public PlayerWithChoices(){
		super();
	}

	public PlayerWithChoices(String name){
		super(name);
	}
	
	public void setUi(IndividualPlayer playerUi){
		this.playerUi = playerUi;
	}
	
	@Override
	public void revealACard(){
		this.revealACard(true);
	}

	public void checkIfWantToBlock(final ActionButton actionToBlock, List<Defense> possibleDefenses) {
		playerUi.checkIfWantToBlock(actionToBlock, possibleDefenses);
	}

	public void hidePopup() {
		playerUi.hidePopup();
	}

	public void replaceCard(CardType cardTypeRequired) {
		playerUi.replaceCard(cardTypeRequired);
	}

	public void revealACard(boolean advanceToNextPlayer) {
		if(getFirstCard().isRevealed()){
			getSecondCard().reveal();
			if(advanceToNextPlayer){
				playerUi.advanceToNextPlayer();
			}
		}else if(getSecondCard().isRevealed()){
			getFirstCard().reveal();
			if(advanceToNextPlayer){
				playerUi.advanceToNextPlayer();
			}
		}else{
			this.playerUi.forceToReveal(advanceToNextPlayer);
		}
	}
}

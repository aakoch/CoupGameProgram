package game.ui.javafx;

import game.CardType;
import game.Player;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Popup;

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
		if(getFirstCard().isRevealed()){
			getSecondCard().reveal();
			playerUi.advanceToNextPlayer();
		}else if(getSecondCard().isRevealed()){
			getFirstCard().reveal();
			playerUi.advanceToNextPlayer();
		}else{
			this.playerUi.forceToReveal();
		}
	}

	public void checkIfWantToBlock(final ActionButton actionToBlock, CardType cardRequiredToBlock) {
		final Popup popup = new Popup();
		popup.setX(100);
		popup.setY(100);
		popup.show(playerUi);
		Button blockButton = new Button("Click to block " + actionToBlock.getActionName() + " with " + cardRequiredToBlock);
		blockButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event arg0) {
				popup.hide();
				actionToBlock.continueAction(true);
			}
		});
		blockButton.setLayoutY(20);
		popup.getContent().add(blockButton);
		
		Button doNotBlockButton = new Button("Click to NOT block " + actionToBlock.getActionName());
		doNotBlockButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event arg0) {
				popup.hide();
				actionToBlock.continueAction(false);
			}
		});
		doNotBlockButton.setLayoutY(50);
		popup.getContent().add(doNotBlockButton);
	}
}

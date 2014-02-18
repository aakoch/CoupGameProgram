package game.ui.javafx;

import game.CardType;
import game.Player;
import game.actions.Action;
import game.actions.AssassinAction;
import game.actions.ContessaDefense;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ActionButton extends Button {

	private final Action action;
	private final Player player;
	private final IndividualPlayer playerUI;
	private final boolean advanceToNextPlayerOnClick;

	public ActionButton(final Action action, final Player player, final IndividualPlayer playerUI){
		this(action,player,playerUI,true);
	}
	
	public String getActionName(){
		return action.getClass().getCanonicalName();
	}
	
	public ActionButton(final Action action, final Player player, final IndividualPlayer playerUI, final boolean advanceToNextPlayerOnClick){
		super(action.actionDescription());
		this.action = action;
		this.player = player;
		this.playerUI = playerUI;
		this.advanceToNextPlayerOnClick = advanceToNextPlayerOnClick;
		setOnMouseClicked(new EventHandler<Event>(){

			@Override
			public void handle(Event arg0) {
				playerUI.disableAllActions();
				//TODO give other players chance to call bluff
				if(action instanceof AssassinAction){ //TODO check to see if there are players who can block??
					//FIXME don't special case it like this! - make multiple options available!
					PlayerWithChoices respondingPlayer = (PlayerWithChoices) action.targetedPlayers().get(0);
					respondingPlayer.checkIfWantToBlock(ActionButton.this, CardType.contessa);
				}else{
					continueAction(false);
				}
			}
		});
	}
	
	public void continueAction(boolean blocked){
		if(blocked){
			new ContessaDefense().defendAgainstPlayer(player);
			playerUI.advanceToNextPlayer(); //Blocked so we should always go on...
		}else{
			action.performAction(player);
			if(advanceToNextPlayerOnClick){
				playerUI.advanceToNextPlayer();
			}
		}
		playerUI.updateMoneyLabelText();
	}
	
	public void enableBasedOnAction(){
		this.setDisable(!action.canPerformAction(player));
	}
}

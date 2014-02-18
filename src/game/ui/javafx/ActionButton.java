package game.ui.javafx;

import game.CardType;
import game.Player;
import game.actions.Action;
import game.actions.AssassinAction;
import game.actions.ContessaDefense;
import game.actions.Defense;

import java.util.ArrayList;
import java.util.List;

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
		return action.getClass().getName();
	}
	
	public CardType getRequiredCard(){
		return action.cardTypeRequired();
	}
	
	public String getPlayerName(){
		return player.toString();
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
				
				if(action.cardTypeRequired() != null){
					numNeedingToNotCallBluff = playerUI.getNumberOfOtherPlayers();
					playerUI.giveOtherPlayersChanceToCallBluff(ActionButton.this);
				}else{
					numNeedingToNotCallBluff = 0;
					continueAfterBluffCall(false,null);
				}
				
			}
		});
	}
	
	int numNeedingToNotBlock = 0;
	List<PlayerWithChoices> playersWhoCanBlock;
	
	public void continueAction(Defense defense, Player blockingPlayer){
		if(defense != null){
			playerUI.closeAllOtherPopups();
			playerUI.checkIfWantToCallBluff(this,(PlayerWithChoices)blockingPlayer,defense); //FIXME other players can call bluff as well?
		}else{
			if(--numNeedingToNotBlock <= 0){
				completeAction();
			}
		}
	}
	

	public void continueAfterDefenseBluffCall(boolean calledBluffOnDefense, PlayerWithChoices blockingPlayer, Defense defenseAction) {
		if(!calledBluffOnDefense){
			defenseAction.defendAgainstPlayer(player);
			playerUI.advanceToNextPlayer(); //Blocked so we should always go on...
		}else{
			//TODO let blocking player choose to not show they have the card??
			if(blockingPlayer.has(defenseAction.cardTypeRequired())){
				player.revealACard(); //Wrong to call bluff on blocking - so now lose a card
				defenseAction.defendAgainstPlayer(player);
				blockingPlayer.replaceCard(action.cardTypeRequired());
			}else{
				//Need special case for if failed bluffer also target of assassin... - this person is eliminated now!
				if(defenseAction instanceof ContessaDefense){
					blockingPlayer.getFirstCard().reveal();
					blockingPlayer.getSecondCard().reveal();
					playerUI.advanceToNextPlayer();
				}
				else{
					blockingPlayer.revealACard(false);
					completeAction(); //Block failed!  Player still gets to do action - FIXME should be before advancing to next player
				}
			}
		}
	}


	private void completeAction() {
		action.performAction(player);
		if(advanceToNextPlayerOnClick){
			playerUI.advanceToNextPlayer();
		}
		playerUI.updateMoneyLabelText();
	}
	
	public void enableBasedOnAction(){
		this.setDisable(!action.canPerformAction(player));
	}
	
	int numNeedingToNotCallBluff = 0;

	public void continueAfterBluffCall(boolean bluffCalled, PlayerWithChoices bluffCaller) {
		if(bluffCalled){
			playerUI.closeAllOtherPopups();
			//TODO let player choose to not show they have the card
			if(player.has(action.cardTypeRequired())){
				playerUI.replaceCard(action.cardTypeRequired());
				//Need special case for if bluff caller is also target of assassin... - this person is eliminated now!
				if(action instanceof AssassinAction && action.targetedPlayers().get(0).equals(bluffCaller)){
					bluffCaller.getFirstCard().reveal();
					bluffCaller.getSecondCard().reveal();
					playerUI.advanceToNextPlayer();
				}else{
					bluffCaller.revealACard(false);
					completeAction();
				}
			}else{
				player.revealACard();
			}
		}else{
			if(--numNeedingToNotCallBluff <= 0){
				if(action.defensesThatCanBlock().isEmpty()){ //TODO check to see if there are players who can block??
					numNeedingToNotBlock = 0;
					continueAction(null,null);
				}else{
					numNeedingToNotBlock = action.targetedPlayers().size();
					playersWhoCanBlock = new ArrayList<PlayerWithChoices>();
					for(Player respondingPlayer : action.targetedPlayers()){
						PlayerWithChoices respondingPlayerWithChoice = (PlayerWithChoices) respondingPlayer;
						playersWhoCanBlock.add(respondingPlayerWithChoice);
						respondingPlayerWithChoice.checkIfWantToBlock(ActionButton.this, action.defensesThatCanBlock());
					}
				}
			}
		}
		
	}
}

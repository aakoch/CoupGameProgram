package game.remote;

import game.Game;
import game.Player;
import game.actions.Action;
import game.actions.ActionList;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO Implement version to use in client/server model
public class GameControllerServerSide {
	
    
    private final List<Player> players;
    private final List<PrintWriter> outWriters;
    private int curPlayer = -1;
	
	List<Map<String,Action>> playerNameToActionMap = new ArrayList<Map<String,Action>>();

	public GameControllerServerSide(Game g, List<PrintWriter> outWriters) {
		this.outWriters = outWriters;
		//		int xPos = 0;
//		int yPos = 0;
//		for(Player player : g.getPlayers()){
//			IndividualPlayer playerUi = new IndividualPlayer(g, (PlayerWithChoices)player, xPos, yPos, this);
//			allPlayerUis.add(playerUi);
//			playerUi.show();
//		}
//		commonUI = new CommonKnowledgeUI(g);
		players = g.getPlayers();
		String allPlayerReps = "";
		for(int i = 0; i < g.getPlayers().size(); i++){
			Player player = g.getPlayers().get(i);
			allPlayerReps += (player+":"+player.getFirstCard()+":"+player.getSecondCard()) + ":";
		}
		for(int i = 0; i < g.getPlayers().size(); i++){
			PrintWriter writer = outWriters.get(i);
			writer.println(allPlayerReps + i);
		}
		
		for(int i = 0; i < g.getPlayers().size(); i++){
			Player player = g.getPlayer(i);
			ActionList playerActions = new ActionList(g,player,null); //TODO card chooser implement
			Map<String,Action> actionStringToAction = new HashMap<String,Action>();
			String allActionStrings = "";
			for(Action action : playerActions.getAllActions()){
				String actionString = action.actionDescription();
				actionStringToAction.put(actionString, action);
				allActionStrings += actionString + "++";
			}
			allActionStrings = allActionStrings.substring(0,allActionStrings.length()-3);
			playerNameToActionMap.add(actionStringToAction);
			outWriters.get(i).println(allActionStrings);
		}
	}
	
	public void performAction(int playerNum, String actionStringKey){
		System.out.println("Player wishes to use: " + actionStringKey);
	}
	
	public int advanceToNextPlayer() {
//		commonUI.refresh();
//		List<Integer> uisIdsToRemove = new ArrayList<Integer>();
//		for(int i = 0; i < allPlayerUis.size(); i++){
//			IndividualPlayer playerUi = allPlayerUis.get(i);
//			playerUi.updateMoneyLabelText();
//			playerUi.updateCardLabels();
//			playerUi.disableAllActions();
//			if(playerUi.playerIsEliminated()){
//				uisIdsToRemove.add(i); //Should only ever be one... TODO confirm this!
//			}
//		}
		
		curPlayer = getNexPlayerIndex(curPlayer, new ArrayList<Integer>());//uisIdsToRemove);
		
		//TODO account for players removed
		for(int i = 0; i < outWriters.size(); i++){
			if(i == curPlayer){
				outWriters.get(i).println(Commands.ActionsEnable.toString());
			}else{
				outWriters.get(i).println(Commands.ActionsDisable.toString());
			}
		}
		
//		IndividualPlayer nextPlayerUi = allPlayerUis.get(curPlayer);
//		if(allPlayerUis.size() == 1){
//			nextPlayerUi.updateToDisplayerVictory();
//		}else{
//			nextPlayerUi.giveThisPlayerTheirTurn();
//			nextPlayerUi.toFront();
//		}
//		return nextPlayerUi;
		return curPlayer;
		
	}

	/**
	 * 
	 * @param curPlayer index of current player
	 * @param allPlayerUis all non-eliminated players --> will be updated to remove "playersToRemove"
	 * @param playersToRemove indices of players to remove
	 * @return index of player who should go next
	 */
	public int getNexPlayerIndex(int curPlayer, List<Integer> playersToRemove) {
		curPlayer = (curPlayer + 1) % players.size();
		while(playersToRemove.contains(curPlayer)){ //FIXME should NEVER ask to eliminate all of them!
			curPlayer = (curPlayer + 1) % players.size();
		}
		Player nextPlayer = players.get(curPlayer);
		for(int i : playersToRemove){
//			TODO SEND MESSAGE TO CLIENT TO SHOw DEFEAT
//			players.get(i).updateToDisplayerDefeat();
			players.remove(i);
		}
		return players.indexOf(nextPlayer);
	}



//	public void giveOtherPlayersChanceToCallBluff(PlayerWithChoices playerBeingCalled, ActionButton actionAttempting) {
//		for(IndividualPlayer playerUi : allPlayerUis){
//			if(!playerUi.forPlayer(playerBeingCalled)){
//				playerUi.giveChanceToCallBluff(actionAttempting);
//			}
//		}
//		
//	}
//
//	public void closeAllOtherPopups(PlayerWithChoices player) {
//		for(IndividualPlayer playerUi : allPlayerUis){
//			if(!playerUi.forPlayer(player)){
//				playerUi.hidePopup();
//			}
//		}
//	}
//
//	public int getNumberOfRemainingPlayers() {
//		return allPlayerUis.size();
//	}

}

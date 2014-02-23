package game.remote;

import game.Game;
import game.Player;
import game.actions.Action;
import game.actions.ActionList;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GameControllerServerSide {
	
    private final List<Player> players;
    private final List<PrintWriter> outWriters;
    private int curPlayer = -1;
	
	List<Map<String,Action>> playerActionMaps = new ArrayList<Map<String,Action>>();
	private List<BufferedReader> playerInputs;

	public GameControllerServerSide(Game g, List<PrintWriter> outWriters, List<BufferedReader> playerInputs) {
		this.outWriters = outWriters;
		players = g.getPlayers();
		String allPlayerReps = "";
		for(int i = 0; i < g.getPlayers().size(); i++){
			RemotePlayer player = (RemotePlayer) g.getPlayers().get(i);
			player.setGameController(this);
			allPlayerReps += (player+":"+player.getFirstCard()+":"+player.getSecondCard()) + ":";
		}
		for(int i = 0; i < g.getPlayers().size(); i++){
			PrintWriter writer = outWriters.get(i);
			writer.println(allPlayerReps + i);
		}
		
		for(int i = 0; i < g.getPlayers().size(); i++){
			Player player = g.getPlayer(i);
			RemoteCardChooser remoteCardChooser = new RemoteCardChooser(players,outWriters,playerInputs);
			ActionList playerActions = new ActionList(g,player,remoteCardChooser);
			Map<String,Action> actionStringToAction = new HashMap<String,Action>();
			String allActionStrings = "";
			for(Action action : playerActions.getAllActions()){
				String actionString = action.actionDescription();
				actionStringToAction.put(actionString, action);
				allActionStrings += actionString + "++";
			}
			allActionStrings = allActionStrings.substring(0,allActionStrings.length()-2);
			playerActionMaps.add(actionStringToAction);
			outWriters.get(i).println(allActionStrings);
		}
	}
	
	public void performAction(int playerNum, String actionStringKey){
		Action action = playerActionMaps.get(playerNum).get(actionStringKey);
		action.performAction(players.get(playerNum));
		String moneyString = "";
		for(Player player : players){
			moneyString += player.getCoins() + ":";
		}
		for(PrintWriter outWriter : outWriters){
			outWriter.println(Commands.UpdateCoins + "+++" + moneyString);
		}
		updatePlayerCards();
	}
	
	public int advanceToNextPlayer() {
		
		List<Integer> playersToRemove = new ArrayList<Integer>();
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).eliminated()){
				playersToRemove.add(i);
			}
		}
		
		curPlayer = getNexPlayerIndex(curPlayer, playersToRemove);//uisIdsToRemove);
		
		
		if(players.size() == 1){
			outWriters.get(0).println(Commands.VICTORY);
			return -1;
		}
		else{
			for(int i = 0; i < outWriters.size(); i++){
				if(i == curPlayer){
					Player player = players.get(i);
					Map<String,Action> textToAction = playerActionMaps.get(i);
					String enabledActions = "";
					for(Entry<String,Action> actionEntry : textToAction.entrySet()){
						if(actionEntry.getValue().canPerformAction(player)){
							enabledActions += (actionEntry.getKey() + "++");
						}
					}
					outWriters.get(i).println(Commands.ActionsEnable.toString()+"+++"+enabledActions);
				}else{
					outWriters.get(i).println(Commands.ActionsDisable.toString());
				}
			}
			return curPlayer;
		}
		
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
			players.remove(i);
			outWriters.get(i).println(Commands.DEFEAT);
			outWriters.remove(i); //It's done now!
		}
		return players.indexOf(nextPlayer);
	}

	public void updatePlayerCards() {
		String cardsString = Commands.UpdateCards.toString() + "+++";
		for(Player player : players){
			cardsString += (player.getFirstCard().getType() + ":" + player.getFirstCard().isRevealed() +
					"::" + player.getSecondCard().getType() + ":" + player.getSecondCard().isRevealed() + "++");
		}
		for(int i = 0; i < players.size(); i++){
			outWriters.get(i).println(cardsString + i);
		}
		
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

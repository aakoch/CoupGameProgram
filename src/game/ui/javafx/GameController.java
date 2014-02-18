package game.ui.javafx;

import game.Game;
import game.Player;

import java.util.ArrayList;
import java.util.List;

public class GameController {
	
    
    private final List<IndividualPlayer> allPlayerUis = new ArrayList<IndividualPlayer>();
    private int curPlayer = -1;

	public GameController(Game g) {
		int xPos = 0;
		int yPos = 0;
		for(Player player : g.getPlayers()){
			IndividualPlayer playerUi = new IndividualPlayer(g, (PlayerWithChoices)player, xPos += 100, yPos += 30, this);
			allPlayerUis.add(playerUi);
			playerUi.show();
		}
	}
	

    
	public void advanceToNextPlayer() {
		List<Integer> uisIdsToRemove = new ArrayList<Integer>();
		for(int i = 0; i < allPlayerUis.size(); i++){
			IndividualPlayer playerUi = allPlayerUis.get(i);
			playerUi.updateMoneyLabelText();
			playerUi.updateCardLabels();
			playerUi.disableAllActions();
			if(playerUi.playerIsEliminated()){
				uisIdsToRemove.add(i); //Should only ever be one... TODO confirm this!
			}
		}
		
		curPlayer = getNextPlayerUi(curPlayer, allPlayerUis, uisIdsToRemove);
		IndividualPlayer nextPlayerUi = allPlayerUis.get(curPlayer);
		if(allPlayerUis.size() == 1){
			nextPlayerUi.updateToDisplayerVictory();
		}else{
			nextPlayerUi.giveThisPlayerTheirTurn();
			nextPlayerUi.toFront();
		}
		
	}

	/**
	 * 
	 * @param curPlayer index of current player
	 * @param allPlayerUis all non-eliminated players --> will be updated to remove "playersToRemove"
	 * @param playersToRemove indices of players to remove
	 * @return index of player who should go next
	 */
	public static int getNextPlayerUi(int curPlayer, List<IndividualPlayer> allPlayerUis, List<Integer> playersToRemove) {
		curPlayer = (curPlayer + 1) % allPlayerUis.size();
		while(playersToRemove.contains(curPlayer)){ //FIXME should NEVER ask to eliminate all of them!
			curPlayer = (curPlayer + 1) % allPlayerUis.size();
		}
		IndividualPlayer nextPlayer = allPlayerUis.get(curPlayer);
		for(int i : playersToRemove){
			allPlayerUis.get(i).updateToDisplayerDefeat();
			allPlayerUis.remove(i);
		}
		return allPlayerUis.indexOf(nextPlayer);
	}

}

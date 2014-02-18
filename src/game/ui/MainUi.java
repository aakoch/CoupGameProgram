package game.ui;

import game.Game;
import game.Player;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

//FIXME make this not static and have static MainUiRunner
public class MainUi {
	
	private static int curPlayer = -1;
	private static List<IndividualPlayer> allPlayerUis = new ArrayList<IndividualPlayer>();

	public static void main(String[] args){
		final Frame frame = new JFrame("COUP");
		frame.setSize(200, 100);
		JPanel panel = new JPanel();
		
		JLabel playersLabel = new JLabel("Number of Players:");
		panel.add(playersLabel);
		
		JSpinner numberPlayersChooser = new JSpinner();
		final SpinnerModel spinnerModel = new SpinnerNumberModel(3,3,6,1);
		numberPlayersChooser.setModel(spinnerModel );
		panel.add(numberPlayersChooser);
		
		JButton createGameButton = new JButton("Start Game");
		createGameButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event) {
				int numPlayers = Integer.valueOf(spinnerModel.getValue().toString());
				Game game = new Game(numPlayers);
				game.deal();
				int xLoc = 0;
				int yLoc = 0;
				for(Player player : game.getPlayers()){
					IndividualPlayer playerFrame = new IndividualPlayer(game,player,xLoc,yLoc);
					playerFrame.updateCardLabels();
					yLoc += 50;
					xLoc += 50;
					playerFrame.setVisible(true);
					allPlayerUis.add(playerFrame);
				}
				frame.setVisible(false);
				advanceToNextPlayer(); //ie first player
		    }
		});
		panel.add(createGameButton );
		
		frame.add(panel);
		
		frame.setVisible(true);
		
		
	}

	//FIXME IMPORTANT Need to skip over eliminated players!
	public static void advanceToNextPlayer() {
		List<Integer> uisIdsToRemove = new ArrayList<Integer>();
		for(int i = 0; i < allPlayerUis.size(); i++){
			IndividualPlayer playerUi = allPlayerUis.get(0);
			playerUi.updateMoneyLabelText();
			playerUi.updateCardLabels();
			playerUi.disableAllActions();
			if(playerUi.playerIsEliminated()){
				uisIdsToRemove.add(i); //TODO Should only ever be one... confirm this!
			}
		}
		
		curPlayer = getNextPlayerUi(curPlayer, allPlayerUis, uisIdsToRemove);
		IndividualPlayer nextPlayerUi = allPlayerUis.get(curPlayer);
		if(allPlayerUis.size() == 1){
			nextPlayerUi.updateToDisplayerVictory();
		}else{
			nextPlayerUi.giveThisPlayerTheirTurn();
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
			allPlayerUis.get(i).updateToDisplayerDefeat(); //FIXME need to get this to work...
			allPlayerUis.remove(i);
		}
		return allPlayerUis.indexOf(nextPlayer);
	}
	
}

package game.ui;

import game.Game;
import game.Player;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class MainUi {

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
					playerFrame.setCards(player.getFirstCard(),player.getSecondCard());
					yLoc += 50;
					xLoc += 20;
					playerFrame.setVisible(true);
				}
				frame.setVisible(false);
		    }
		});
		panel.add(createGameButton );
		
		frame.add(panel);
		
		frame.setVisible(true);
		
	}
	
}

package game.ui;

import game.Card;
import game.Game;
import game.Player;
import game.actions.Action;
import game.actions.ActionList;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IndividualPlayer extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final JPanel panel = new JPanel();
	private JLabel cardOneLabel = new JLabel();
	private JLabel cardTwoLabel = new JLabel();

	public IndividualPlayer(Game game, Player player, int xLoc, int yLoc){
		super(player.toString());
		this.setLocation(xLoc, yLoc);
		this.setSize(500, 300);
		add(panel);
		panel.setLayout(new GridLayout(8,1));
		JPanel cardsPanel = new JPanel();
		cardsPanel.add(cardOneLabel);
		cardsPanel.add(cardTwoLabel);
		panel.add(cardsPanel);
		ActionList actionsForPlayer = new ActionList(game,player);
		for(Action action: actionsForPlayer.getAllActions()){
			JButton button = new JButton(action.actionDescription());
			button.setEnabled(action.canPerformAction(player));
			panel.add(button);
		}
	}

	public void setCards(Card firstCard, Card secondCard) {
		cardOneLabel.setText(firstCard.getType().toString());
		cardTwoLabel.setText(secondCard.getType().toString());
	}
	
}

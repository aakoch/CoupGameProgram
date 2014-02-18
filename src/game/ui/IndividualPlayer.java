package game.ui;

import game.Card;
import game.Game;
import game.Player;
import game.actions.Action;
import game.actions.ActionList;
import game.actions.AmbassadorAction;
import game.actions.CardChooser;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//TODO IMPORTANT:  update cards, show when player is out of game
public class IndividualPlayer extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final JPanel panel = new JPanel();
	private JLabel cardOneLabel = new JLabel();
	private JLabel cardTwoLabel = new JLabel();
	private JLabel moneyLabel = new JLabel();
	private List<ActionButton> actionButtons = new ArrayList<ActionButton>();
	private final Player player;
	private JPanel cardsPanel;

	public IndividualPlayer(final Game game, final Player player, int xLoc, int yLoc){
		super(player.toString());
		this.player = player;
		this.setLocation(xLoc, yLoc);
		add(panel);
		cardsPanel = new JPanel();
		cardsPanel.add(cardOneLabel);
		cardsPanel.add(cardTwoLabel);
		panel.add(cardsPanel);
		updateMoneyLabelText();
		panel.add(moneyLabel);
		CardChooser cardChooserUi = new CardChooserUI(this);
		ActionList actionsForPlayer = new ActionList(game,player,cardChooserUi);
		for(final Action action: actionsForPlayer.getAllActions()){
			ActionButton button = new ActionButton(action,player,this,!(action instanceof AmbassadorAction));
			button.setEnabled(false);
			panel.add(button);
			actionButtons.add(button);
		}
		panel.setLayout(new GridLayout(actionsForPlayer.getAllActions().size() + 3,1));
		this.setSize(500, 40 * actionsForPlayer.getAllActions().size() + 60);
	}
	
	public void updateMoneyLabelText() {
		moneyLabel.setText("Coins: " + player.getCoins());
	}

	public void updateCardLabels() {
		cardsPanel.setVisible(false);
		cardOneLabel.setText(cardText(player.getFirstCard()));
		cardTwoLabel.setText(cardText(player.getSecondCard()));
		cardsPanel.setVisible(true);
	}

	private String cardText(Card card) {
		return card.getType().toString() + " is " + (card.isRevealed() ? "" : "NOT ") + "revealed";
	}

	public void advanceToNextPlayer() {
		for(JButton actionButton : actionButtons){
			actionButton.setEnabled(false);
		}
		MainUi.advanceToNextPlayer(); //FIXME should NOT be static...
	}
	
	public void giveThisPlayerTheirTurn(){
		for(ActionButton actionButton : actionButtons){
			actionButton.enableBasedOnAction();
		}
	}

	public void disableAllActions() {
		for(JButton actionButton : actionButtons){
			actionButton.setEnabled(false);
		}
	}

	public boolean playerIsEliminated() {
		return player.eliminated();
	}

	public void updateToDisplayerDefeat() {
		panel.add(new JLabel("Sorry, you lose!"));
	}
	
	public void updateToDisplayerVictory() {
		panel.add(new JLabel("Hooray!  You win!"));
	}
	
	
}

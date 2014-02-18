package game.ui;

import game.Player;
import game.actions.Action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class ActionButton extends JButton {

	private static final long serialVersionUID = 1L;
	private final Action action;
	private final Player player;

	public ActionButton(final Action action, final Player player, final IndividualPlayer playerUI){
		this(action,player,playerUI,true);
	}
	
	public ActionButton(final Action action, final Player player, final IndividualPlayer playerUI, final boolean advanceToNextPlayerOnClick){
		super(action.actionDescription());
		this.action = action;
		this.player = player;
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event) {
				playerUI.disableAllActions();
				action.performAction(player);
				playerUI.updateMoneyLabelText();
				if(advanceToNextPlayerOnClick){
					playerUI.advanceToNextPlayer();
				}
			}
		});
	}
	
	public void enableBasedOnAction(){
		setEnabled(action.canPerformAction(player));
	}
}

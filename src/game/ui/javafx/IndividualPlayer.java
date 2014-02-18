package game.ui.javafx;

import game.Card;
import game.CardType;
import game.Game;
import game.Player;
import game.actions.Action;
import game.actions.ActionList;
import game.actions.AmbassadorAction;
import game.actions.AssassinAction;
import game.actions.CoupAction;
import game.actions.Defense;

import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class IndividualPlayer extends Stage{

	private final GameController gameController;
	private final PlayerWithChoices player;
	private static final int COMP_VERT_SPACE = 30;
	
	private List<ActionButton> allActionButtons = new ArrayList<ActionButton>();
	private Text moneyLabel;
	private Text card1Label;
	private Text card2Label;
	private Pane pane;
	private Scene scene;
	private Button card1RevealButton;
	private Button card2RevealButton;
	private Color color;
	private Game game;
	private boolean advanceAfterChoosing = true;

	public IndividualPlayer(final Game game, final PlayerWithChoices player, int xLoc, int yLoc, final GameController gameController){
		super();
		this.game = game;
		
		//TODO figure out how to make not closable
		
		this.player = player;
		player.setUi(this);
		this.gameController = gameController;
		this.setX(xLoc);
		this.setY(yLoc);
		
		int sceneR = randomColorVal();
		int sceneG = randomColorVal();
		int sceneB = randomColorVal();
		color = Color.rgb(sceneR, sceneG, sceneB);
		
		int inverseR = 255 - sceneR;
		int inverseG = 255 - sceneG;
		int inverseB = 255 - sceneB;
		
		Color labelColor = Color.rgb(inverseR, inverseG, inverseB);
		
		AnchorPane root = new AnchorPane();
		pane = new Pane();
		
		card1Label = new Text(getCardDisplay(player.getFirstCard()));
		pane.getChildren().add(card1Label);
		card1Label.setFill(labelColor);
		card1Label.setLayoutY(2 * COMP_VERT_SPACE/3);
		
		card1RevealButton = new Button("Reveal Card 1");
		card1RevealButton.setVisible(false);
		card1RevealButton.setLayoutY(0);
		card1RevealButton.setLayoutX(200);
		card1RevealButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event arg0) {
				player.getFirstCard().reveal();
				updateFollowingCardReveal(gameController);
			}

		});
		pane.getChildren().add(card1RevealButton);
		
		card2Label = new Text(getCardDisplay(player.getSecondCard()));
		pane.getChildren().add(card2Label);
		card2Label.setLayoutY(4 * COMP_VERT_SPACE/3);
		card2Label.setFill(labelColor);
		
		card2RevealButton = new Button("Reveal Card 2");
		card2RevealButton.setLayoutY(COMP_VERT_SPACE);
		card2RevealButton.setLayoutX(200);
		card2RevealButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event arg0) {
				player.getSecondCard().reveal();
				updateFollowingCardReveal(gameController);
			}
		});
		card2RevealButton.setVisible(false);
		pane.getChildren().add(card2RevealButton);
		
		moneyLabel = new Text(getCoinDisplay(player));
		pane.getChildren().add(moneyLabel );
		moneyLabel.setLayoutY(COMP_VERT_SPACE * 2);
		moneyLabel.setFill(labelColor);
		
		ActionList actionsForPlayer = new ActionList(game,player,new CardChooserUI(this));
		int buttonNumber = 1;
		for(final Action action: actionsForPlayer.getAllActions()){
			ActionButton button = new ActionButton(action,player,this,!(action instanceof AmbassadorAction
					|| action instanceof CoupAction || action instanceof AssassinAction));
			button.setLayoutX(0);
			button.setLayoutY((buttonNumber + 2) * COMP_VERT_SPACE);
			buttonNumber++;
			button.setDisable(true);
			pane.getChildren().add(button);
			allActionButtons.add(button);
		}
		root.getChildren().add(pane);
		
        scene = new Scene(root, 500, 500);
        setTitle(player.toString());
        setResizable(true);
		scene.setFill(color);
        setScene(scene);
	}

	private void updateFollowingCardReveal(
			final GameController gameController) {
		card1RevealButton.setVisible(false);
		card2RevealButton.setVisible(false);
		updateCardLabels();
		if(advanceAfterChoosing){
			gameController.advanceToNextPlayer();
		}
	}
	
	private String getCardDisplay(Card card) {
		return "Card " + card.getType() + " is " + (card.isRevealed() ? "" : "NOT ") + "reveald";
	}

	private String getCoinDisplay(final Player player) {
		return "coins: " + player.getCoins();
	}
	
	private int randomColorVal(){
		return 100 + (int) (Math.random() * 155);
	}
	
	public void giveThisPlayerTheirTurn(){
		for(ActionButton actionButton : allActionButtons){
			actionButton.enableBasedOnAction();
		}
	}

	public void disableAllActions() {
		for(ActionButton actionButton : allActionButtons){
			actionButton.setDisable(true);
		}
	}

	public void advanceToNextPlayer() {
		disableAllActions();
		gameController.advanceToNextPlayer();
	}

	public void updateMoneyLabelText() {
		moneyLabel.setText(this.getCoinDisplay(player));
	}

	public void updateCardLabels() {
		card1Label.setText(getCardDisplay(player.getFirstCard()));
		card2Label.setText(getCardDisplay(player.getSecondCard()));
	}

	public boolean playerIsEliminated() {
		return player.eliminated();
	}

	public void updateToDisplayerVictory() {
		scene.setFill(Color.GREEN);
		pane.getChildren().clear();
		Text winnerText = new Text("YOU WIN!");
		winnerText.setFont(Font.font("Verdana", 20));
		winnerText.setLayoutX(0);
		winnerText.setLayoutY(40);
		winnerText.setFill(Color.WHITE);
		pane.getChildren().add(winnerText );
	}

	public void updateToDisplayerDefeat() {
		scene.setFill(Color.RED);
		pane.getChildren().clear();
		Text loserText = new Text("YOU LOSE! :(");
		loserText.setFont(Font.font("Verdana", 20));
		loserText.setLayoutX(0);
		loserText.setLayoutY(40);
		loserText.setFill(Color.WHITE);
		pane.getChildren().add(loserText );
	}
	
	public void forceToReveal(boolean advanceAfterChoosing) {
		this.toFront();
		this.advanceAfterChoosing = advanceAfterChoosing;
		card1RevealButton.setVisible(true);
		card2RevealButton.setVisible(true);
	}

	public void forceToReveal() {
		this.forceToReveal(true);
	}

	public Paint getColor() {
		return color;
	}
	
	public String getPlayerName(){
		return player.toString();
	}

	public void giveOtherPlayersChanceToCallBluff(ActionButton actionAttempting) {
		gameController.giveOtherPlayersChanceToCallBluff(player, actionAttempting);
	}

	public boolean forPlayer(PlayerWithChoices playerBeingCalled) {
		return player.equals(playerBeingCalled);
	}
	
	private Popup popup;

	public void giveChanceToCallBluff(final ActionButton actionAttempting) {
		popup = new Popup();
		popup.setX(getX());
		popup.setY(getY());
		popup.show(this);
		
		Pane popupPane = new Pane();
		popup.getContent().add(popupPane);
		
		Button callBluffButton = new Button("Click to accuse " + actionAttempting.getPlayerName() 
				+ " of bluffing about having " + actionAttempting.getRequiredCard());
		callBluffButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event arg0) {
				popup.hide();
				actionAttempting.continueAfterBluffCall(true,player);
			}
		});
		callBluffButton.setLayoutY(20);
		popupPane.getChildren().add(callBluffButton);
		
		Button doNotCallButton = new Button("Click to NOT accuse " + actionAttempting.getPlayerName() 
				+ " of bluffing about having " + actionAttempting.getRequiredCard());
		doNotCallButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event arg0) {
				popup.hide();
				actionAttempting.continueAfterBluffCall(false,player);
			}
		});
		doNotCallButton.setLayoutY(50);
		popupPane.getChildren().add(doNotCallButton);
	}
	
	public void checkIfWantToBlock(final ActionButton actionToBlock, List<Defense> possibleDefenses) {
		popup = new Popup();
		popup.setX(getX());
		popup.setY(getY());
		popup.show(this);
		
		Pane popupPane = new Pane();
		popup.getContent().add(popupPane);
		int yLoc = 20;
		for(final Defense defense : possibleDefenses){
			Button blockButton = new Button("Click to block " + actionToBlock.getActionName() + " with " + defense.cardTypeRequired());
			blockButton.setOnMouseClicked(new EventHandler<Event>(){
				@Override
				public void handle(Event arg0) {
					popup.hide();
					actionToBlock.continueAction(defense,player);
				}
			});
			blockButton.setLayoutY(yLoc);
			yLoc += 30;
			popupPane.getChildren().add(blockButton);
		}
		
		Button doNotBlockButton = new Button("Click to NOT block " + actionToBlock.getActionName());
		doNotBlockButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event arg0) {
				popup.hide();
				actionToBlock.continueAction(null,null);
			}
		});
		doNotBlockButton.setLayoutY(yLoc);
		popupPane.getChildren().add(doNotBlockButton);
	}

	public void hidePopup() {
		if(popup != null){
			popup.hide();
		}
	}

	public void closeAllOtherPopups() {
		gameController.closeAllOtherPopups(player);
	}

	public void replaceCard(CardType cardTypeRequired) {
		game.reshuffleCardAndDrawNewCard(player, cardTypeRequired);
		updateCardLabels();
	}

	public int getNumberOfOtherPlayers() {
		return gameController.getNumberOfRemainingPlayers() - 1;
	}

	public void checkIfWantToCallBluff(final ActionButton actionAttempting, 
			final PlayerWithChoices blockingPlayer, final Defense defenseUsing) {
		// TODO Auto-generated method stub
		popup = new Popup();
		popup.setX(getX());
		popup.setY(getY());
		popup.show(this);
		
		Pane popupPane = new Pane();
		popup.getContent().add(popupPane);
		
		Button callBluffButton = new Button("Click to accuse " + blockingPlayer 
				+ " of bluffing about having " + defenseUsing.cardTypeRequired());
		callBluffButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event arg0) {
				popup.hide();
				actionAttempting.continueAfterDefenseBluffCall(true,blockingPlayer,defenseUsing);
			}
		});
		callBluffButton.setLayoutY(20);
		popupPane.getChildren().add(callBluffButton);
		
		Button doNotCallButton = new Button("Click to NOT accuse " + blockingPlayer 
				+ " of bluffing about having " + defenseUsing.cardTypeRequired());
		doNotCallButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event arg0) {
				popup.hide();
				actionAttempting.continueAfterDefenseBluffCall(false,blockingPlayer,defenseUsing);
			}
		});
		doNotCallButton.setLayoutY(50);
		popupPane.getChildren().add(doNotCallButton);
	}
	
}

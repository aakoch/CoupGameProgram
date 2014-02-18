package game.ui.javafx;

import game.Card;
import game.CardPair;
import game.Player;
import game.actions.CardChooser;

import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CardChooserUI extends Stage implements CardChooser {
	
	private final IndividualPlayer playerUI;
	private Pane pane;
	private Scene scene;
	private List<CheckBox> allCheckBoxes;

	public CardChooserUI(IndividualPlayer playerUI){
		//TODO figure out how to make not closeable
		
		this.playerUI = playerUI;
		
		this.setX(300);
		this.setY(100);
		
		AnchorPane root = new AnchorPane();
		pane = new Pane();
		root.getChildren().add(pane);
		
        scene = new Scene(root, 500, 500);
        setTitle(playerUI.getPlayerName() + " Must Choose Cards To Keep");
        setResizable(true);
		scene.setFill(playerUI.getColor());
        setScene(scene);
	}

	@Override
	public CardPair chooseCards(final List<Card> cards, Player playerToChoose) {
		final CardPair cardPair = new CardPair(cards.get(0), cards.get(1));
		
		pane.getChildren().clear();
		allCheckBoxes = new ArrayList<CheckBox>();
		
		final Button acceptButton = new Button("Accept");
		pane.getChildren().add(acceptButton);
		acceptButton.setLayoutX(80);
		acceptButton.setLayoutY(60);
		acceptButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event arg0) {
				List<Card> selectedCards = new ArrayList<Card>();
				for(int i = 0; i < allCheckBoxes.size(); i++){
					if(allCheckBoxes.get(i).isSelected()){
						selectedCards.add(cards.get(i));
					}
				}
				cardPair.setFirstCard(selectedCards.get(0));
				cardPair.setSecondCard(selectedCards.get(1));
				playerUI.advanceToNextPlayer(); //Now next player can go!
				CardChooserUI.this.hide();
			}
			
		});
		
		for(Card card : cards){
			final CheckBox checkBox = new CheckBox(card.getType().toString());
			pane.getChildren().add(checkBox);
			allCheckBoxes.add(checkBox);
			checkBox.setOnMouseClicked(new EventHandler<Event>(){
				@Override
				public void handle(Event arg0) {
					int selectedCount = 0;
					for(CheckBox localBox : allCheckBoxes){
						if(localBox.isSelected()){
							selectedCount++;
						}
					}
					acceptButton.setDisable(selectedCount != 2);
					
				}
		});
		}
		
		allCheckBoxes.get(0).setLayoutX(0);
		allCheckBoxes.get(0).setLayoutY(0);
		allCheckBoxes.get(0).setSelected(true);
		
		allCheckBoxes.get(1).setLayoutX(100);
		allCheckBoxes.get(1).setLayoutY(0);
		allCheckBoxes.get(1).setSelected(true);
		
		allCheckBoxes.get(2).setLayoutX(0);
		allCheckBoxes.get(2).setLayoutY(30);
		allCheckBoxes.get(2).setSelected(false);
		
		allCheckBoxes.get(3).setLayoutX(100);
		allCheckBoxes.get(3).setLayoutY(30);
		allCheckBoxes.get(3).setSelected(false);

		this.show();
				
		return cardPair; //returned right away but will update
	}

	@Override
	public CardPair chooseCards(final List<Card> cards, Player playerToChoose,
			Card cardThatMustBeIncluded) {
		List<Card> allCards = new ArrayList<Card>();
		allCards.add(cardThatMustBeIncluded);
		allCards.addAll(cards);
		CardPair result = chooseCards(allCards, playerToChoose);
		allCheckBoxes.get(0).setDisable(true); //Cannot uncheck the card that must be included
		return result;
	}

}
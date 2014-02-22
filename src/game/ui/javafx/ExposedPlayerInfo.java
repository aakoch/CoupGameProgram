package game.ui.javafx;

import game.Card;
import game.Player;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ExposedPlayerInfo extends Pane {

	private Player player;
	private List<Text> cardTexts = new ArrayList<Text>();
	private Text coinText;

	public ExposedPlayerInfo(Player player){
		super();
		this.player = player;
		
		Text playerNameText = new Text(player.toString());
		playerNameText.setFont(Font.font("Verdana", 14));
		playerNameText.setLayoutX(0);
		playerNameText.setLayoutY(40);
		playerNameText.setFill(Color.BLACK);
		this.getChildren().add(playerNameText);
		
		int nextYLoc = 60;
		for(Card card : player.getCards()){
			Text cardText = new Text(cardTextString(card));
			cardText.setFont(Font.font("Verdana", 12));
			cardText.setLayoutX(0);
			cardText.setLayoutY(nextYLoc);
			nextYLoc += 15;
			cardText.setFill(Color.BLACK);
			this.getChildren().add(cardText);
			cardTexts.add(cardText);
		}
		
		Text coinsText = new Text(coinTextString(player));
		coinsText.setFont(Font.font("Verdana", 12));
		coinsText.setLayoutX(0);
		coinsText.setLayoutY(nextYLoc);
		coinsText.setFill(Color.BLACK);
		this.getChildren().add(coinsText);
		this.coinText = coinsText;
	}

	private String coinTextString(Player player) {
		return "Has " + player.getCoins() + " coins";
	}
	
	private String cardTextString(Card card){
		if(card.isRevealed()){
			return "Showing card: " + card.getType();
		}else{
			return "Card not revealed";
		}
	}

	public void refresh() {
		this.cardTexts.get(0).setText(cardTextString(player.getFirstCard()));
		this.cardTexts.get(1).setText(cardTextString(player.getSecondCard()));
		this.coinText.setText(coinTextString(player));

	}
}

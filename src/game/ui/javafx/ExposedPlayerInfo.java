package game.ui.javafx;

import game.Card;
import game.Player;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ExposedPlayerInfo extends Pane {

	private Player player;

	public ExposedPlayerInfo(Player player){
		super();
		this.player = player;
		
		refresh();
		
	}

	public void refresh() {
		this.getChildren().clear();
		
		Text playerNameText = new Text(player.toString());
		playerNameText.setFont(Font.font("Verdana", 14));
		playerNameText.setLayoutX(0);
		playerNameText.setLayoutY(40);
		playerNameText.setFill(Color.BLACK);
		this.getChildren().add(playerNameText);
		
		int nextYLoc = 60;
		for(Card card : player.getCards()){
			Text cardText = null;
			if(card.isRevealed()){
				cardText = new Text("Showing card: " + card.getType());
			}else{
				cardText = new Text("Card not revealed");
			}
			cardText.setFont(Font.font("Verdana", 12));
			cardText.setLayoutX(0);
			cardText.setLayoutY(nextYLoc);
			nextYLoc += 15;
			cardText.setFill(Color.BLACK);
			this.getChildren().add(cardText);
		}
		
		Text coinsText = new Text("Has " + player.getCoins() + " coins");
		coinsText.setFont(Font.font("Verdana", 12));
		coinsText.setLayoutX(0);
		coinsText.setLayoutY(nextYLoc);
		coinsText.setFill(Color.BLACK);
		this.getChildren().add(coinsText);
	}
}

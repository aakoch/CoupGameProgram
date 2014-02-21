package game;

import java.io.Serializable;

public class Card implements Serializable {

	private boolean revealed = false;
	private final CardType type;

	public Card(CardType cardType) {
		if(cardType == null){
			throw new IllegalArgumentException("Card must have a type - card type cannot be null");
		}
		this.type = cardType;
	}

	public CardType getType() {
		return type;
	}

	public boolean isRevealed() {
		return revealed;
	}

	public void reveal() {
		revealed = true;
	}

	public void hide() {
		revealed = false;
	}
	
	@Override
	public String toString(){
		return type.toString();
	}

}

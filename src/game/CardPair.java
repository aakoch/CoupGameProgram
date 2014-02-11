package game;

public class CardPair {

	private final Card card;
	private final Card card2;

	public CardPair(Card card, Card card2) {
		this.card = card;
		this.card2 = card2;
	}

	public Card getFirstCard() {
		return card;
	}

	public Card getSecondCard() {
		return card2;
	}

}

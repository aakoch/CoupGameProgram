package game;

public class CardPair {

	private Card card;
	private Card card2;

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
	
	public void setFirstCard(Card card){
		this.card = card;
	}
	
	public void setSecondCard(Card card2){
		this.card2 = card2;
	}
	
	public boolean has(CardType cardType) {
		return (card.getType() == cardType && !card.isRevealed()) || 
				(card2.getType() == cardType && !card2.isRevealed());
	}
	
//	public Card matchingCard(CardType cardType){
//		if((card.getType() == cardType && !card.isRevealed())){
//			return card;
//		}
//	}
	

}

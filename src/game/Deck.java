package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {
	
	private Stack<Card> cards;
	
	public Deck(){
		cards = new Stack<Card>();
		for(int i = 0; i < 3; i++){
			cards.push(new Card(CardType.contessa));
			cards.push(new Card(CardType.duke));
			cards.push(new Card(CardType.captain));
			cards.push(new Card(CardType.ambassador));
			cards.push(new Card(CardType.assassin));
		}
		this.shuffleCards();
	}
	
	public int cardCount() {
		return cards.size();
	}

	public Card deal() {
		return cards.pop();
	}

	public boolean contains(Card dealtCard) {
		return cards.contains(dealtCard);
	}

	public void takeCardBack(Card dealtCard) {
		cards.push(dealtCard);
		this.shuffleCards();
	}

	private void shuffleCards() {
		List<Card> cardsToShuffle = getCopyOfCards();
		Collections.shuffle(cardsToShuffle);
		cards = new Stack<Card>();
		for(Card card : cardsToShuffle){
			cards.push(card);
		}
	}
	
	public List<Card> getCopyOfCards(){
		return new ArrayList<Card>(cards);
	}

}

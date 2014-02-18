package game;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class DeckTest {

	@Test
	public void deckInitiallyHas15Cards() {
		Deck deck = new Deck();
		assertThat(deck.cardCount(),equalTo(15));
	}
	
	@Test
	public void dealingReducesCardCount() {
		Deck deck = new Deck();
		deck.deal();
		assertThat(deck.cardCount(),equalTo(14));
		deck.deal();
		deck.deal();
		assertThat(deck.cardCount(),equalTo(12));
	}
	
	@Test
	public void dealingReturnsACard() throws Exception {
		Deck deck = new Deck();
		Card card = deck.deal();
		assertNotNull(card);
	}
	
	@Test
	public void dealingAllFifteenCardsRevealsThreeOfEachType() throws Exception {
		Map<CardType,Integer> typeToCount = new HashMap<CardType,Integer>();
		typeToCount.put(CardType.contessa, 0);
		typeToCount.put(CardType.duke, 0);
		typeToCount.put(CardType.captain, 0);
		typeToCount.put(CardType.assassin, 0);
		typeToCount.put(CardType.ambassador, 0);
		
		Deck deck = new Deck();
		for(int i = 0; i < 15; i++){
			Card card = deck.deal();
			typeToCount.put(card.getType(), typeToCount.get(card.getType()) + 1);
		}
		
		assertThat(typeToCount.get(CardType.contessa), equalTo(3));
		assertThat(typeToCount.get(CardType.duke), equalTo(3));
		assertThat(typeToCount.get(CardType.captain), equalTo(3));
		assertThat(typeToCount.get(CardType.assassin), equalTo(3));
		assertThat(typeToCount.get(CardType.ambassador), equalTo(3));
	}
	
	@Test
	public void deckKnowsWhenItLosesACard() throws Exception {
		Deck deck = new Deck();
		Card dealtCard = deck.deal();
		assertFalse(deck.contains(dealtCard));
	}
	
	@Test
	public void cardsCanBeReturnedToDeck() throws Exception {
		Deck deck = new Deck();
		Card dealtCard = deck.deal();
		assertThat(deck.cardCount(),equalTo(14));
		deck.takeCardBack(dealtCard);
		assertThat(deck.cardCount(),equalTo(15));
		assertTrue(deck.contains(dealtCard));
	}
	
	@Test
	public void deckIsRandomizedOnCreation() throws Exception {
		Deck deck = new Deck();
		Card dealtCard = deck.deal();
		
		Deck anotherDeck = new Deck();
		Card anotherDealt = anotherDeck.deal();
		
		if(dealtCard.getType().equals(anotherDealt.getType())){
			deckIsRandomizedOnCreation(); //Resursively call until they are different
		}
		else{
			assertNotSame(dealtCard.getType(),anotherDealt.getType());
		}
	}

}

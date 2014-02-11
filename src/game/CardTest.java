package game;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
public class CardTest {

	@Test
	public void cardHasASpecifiedType() {
		Card card = new Card(CardType.contessa);
		assertThat(card.getType(),equalTo(CardType.contessa));
	}
	
	@Test
	public void cardStartsHidden() throws Exception {
		Card card = new Card(CardType.assassin);
		assertThat(card.isRevealed(),equalTo(false));
	}
	
	@Test
	public void cardCanBeRevealed() throws Exception {
		Card card = new Card(CardType.assassin);
		card.reveal();
		assertThat(card.isRevealed(),equalTo(true));
	}
	
	@Test
	public void cardCanBeHidden() throws Exception {
		Card card = new Card(CardType.assassin);
		card.reveal();
		card.hide();
		assertThat(card.isRevealed(),equalTo(false));
	}
	
	@Test
	public void cardsHaveFiveDefinedTypes() throws Exception {
		new Card(CardType.assassin);
		new Card(CardType.ambassador);
		new Card(CardType.captain);
		new Card(CardType.contessa);
		new Card(CardType.duke);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void cardsCannotHaveNullCardType(){
		new Card(null);
	}

}

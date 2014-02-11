package game;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

	private Player player;
	
	@Before
	public void setUp(){
		player = new Player();
	}
	
	@Test
	public void playerCanReceiveCard() {
		Card card = new Card(CardType.contessa);
		player.receive(card);
		assertThat(player.getFirstCard(), equalTo(card));
	}
	
	@Test
	public void playerCanReceiveAnotherCard() throws Exception {
		Card card = new Card(CardType.contessa);
		player.receive(card);
		Card anotherCard = new Card(CardType.duke);
		player.receive(anotherCard);
		assertThat(player.getSecondCard(), equalTo(anotherCard));
	}
	
	@Test
	public void playerHoldsBothCards() throws Exception {
		Card card = new Card(CardType.contessa);
		player.receive(card);
		Card anotherCard = new Card(CardType.duke);
		player.receive(anotherCard);
		assertThat(player.getFirstCard(), equalTo(card));
		assertThat(player.getSecondCard(), equalTo(anotherCard));
	}
	
	
	@Test
	public void playerHoldsBothCardsTogether() throws Exception {
		Card card = new Card(CardType.contessa);
		player.receive(card);
		Card anotherCard = new Card(CardType.duke);
		player.receive(anotherCard);
		assertThat(player.getCards(), equalTo(Arrays.asList(card,anotherCard)));
	}
	
	@Test
	public void playerStartsWithTwoCoins() throws Exception {
		assertThat(player.getCoins(), equalTo(2));
	}
	
	@Test
	public void playerCanUseIncomeActionToGainOneCoin() throws Exception {
		player.takeActionIncome();
		assertThat(player.getCoins(), equalTo(3));
	}
	
	@Test
	public void playerCanUseForeignAidActionToGainTwoCoins() {
		player.takeActionForeignAid();
		assertThat(player.getCoins(), equalTo(4));
	}

	@Test
	public void playerCanUseDukeActionToGainThreeCoins() throws Exception {
		player.takeActionDuke();
		assertThat(player.getCoins(), equalTo(5));
	}
	
	@Test
	public void playerCanUseCaptainActionToTakeTwoCoinsFromAnotherPlayer(){
		Player anotherPlayer = new Player();
		player.takeActionCaptain(anotherPlayer);
		assertThat(player.getCoins(), equalTo(4));
		assertThat(anotherPlayer.getCoins(), equalTo(0));
	}

	@Test
	public void playerCanUseCaptainActionToTakeOneCoinFromAnotherPlayerWhoOnlyHasOneCoin(){
		Player anotherPlayer = new Player();
		anotherPlayer.setMoney(1);
		player.takeActionCaptain(anotherPlayer);
		assertThat(player.getCoins(), equalTo(3));
		assertThat(anotherPlayer.getCoins(), equalTo(0));
	}
	
	@Test
	public void playerMustPayThreeCoinsForAssassination() throws Exception {
		player.setMoney(3);
		assertThat(player.getCoins(), equalTo(3));
		player.takeActionAssassin();
		assertThat(player.getCoins(), equalTo(0));
	}
	
	@Test
	public void playerWithFewerThanThreeCoinsCannotPerformAssassination() throws Exception {
		player.setMoney(2);
		assertThat(player.getCoins(), equalTo(2));
		assertThat(player.takeActionAssassin(), equalTo(false));
		assertThat(player.getCoins(), equalTo(2));
	}
	
	@Test
	public void playerMustPaySevenCoinsForCoup() throws Exception {
		player.setMoney(7);
		assertThat(player.getCoins(), equalTo(7));
		player.takeActionCoup();
		assertThat(player.getCoins(), equalTo(0));
	}
	
	@Test
	public void playerWithFewerThanSevenCoinsCannotPerformCoup() throws Exception {
		player.setMoney(6);
		assertThat(player.getCoins(), equalTo(6));
		assertThat(player.takeActionCoup(), equalTo(false));
		assertThat(player.getCoins(), equalTo(6));
	}
	
	@Test
	public void playerKnowsIfTheyHaveACard() throws Exception {
		player.receive(new Card(CardType.contessa));
		player.receive(new Card(CardType.duke));
		
		assertTrue(player.has(CardType.contessa));
		assertTrue(player.has(CardType.duke));
	}
	
	@Test
	public void playerKnowsIfTheyDoNotHaveACard() throws Exception {
		player.receive(new Card(CardType.duke));
		player.receive(new Card(CardType.assassin));
		
		assertFalse(player.has(CardType.contessa));
		assertFalse(player.has(CardType.ambassador));
		assertFalse(player.has(CardType.captain));
	}
}

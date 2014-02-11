package game;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GameTest {

	@Test
	public void gameLeavesDeckWithTwoFewerCardsPerPlayerAfterDealing() {
		Game game = new Game(3);
		assertThat(game.getDeck().cardCount(),equalTo(15));
		game.deal();
		assertThat(game.getDeck().cardCount(),equalTo(9));
	}
	
	@Test
	public void gameLeavesDeckWithTwoFewerCardsPerPlayerAfterDealing_DifferentNumberOfPlayers() {
		Game game = new Game(5);
		assertThat(game.getDeck().cardCount(),equalTo(15));
		game.deal();
		assertThat(game.getDeck().cardCount(),equalTo(5));
	}
	
	@Test
	public void gameHasPlayers() throws Exception {
		Game game = new Game(3);
		assertThat(game.getPlayers().size(), equalTo(3));
		assertThat(game.getPlayers().get(0), is(Player.class));
	}
	
	@Test
	public void eachPlayerIsDealtTheirOwnCards() throws Exception {
		Game game = new Game(3);
		game.deal();
		assertThat(game.getPlayers().get(0).getCards(), not(game.getPlayers().get(1).getCards()));
		assertThat(game.getPlayers().get(1).getCards(), not(game.getPlayers().get(2).getCards()));
	}
	
	@Test
	public void playerCanUseAssassinActionToPayThreeCoinsAndRemoveFirstInfluenceFromAnotherPlayer() throws Exception {
		Game game = new Game(2);
		game.deal();
		Player player = game.getPlayers().get(0);
		player.setMoney(3);
		assertThat(player.getCoins(), equalTo(3));
		Player anotherPlayer = game.getPlayers().get(1);
		game.playerAssassinatesOtherPlayer(player, anotherPlayer);
		player.takeActionAssassin();
		assertThat(player.getCoins(), equalTo(0));
		assertThat(anotherPlayer.getCards().get(0).isRevealed(), not(anotherPlayer.getCards().get(1).isRevealed()));
	}
	
	@Test
	public void playerCanUseAssassinActionToPayThreeCoinsAndRemoveFinalInfluenceFromAnotherPlayer() throws Exception {
		Game game = new Game(2);
		game.deal();
		Player player = game.getPlayers().get(0);
		player.setMoney(3);
		Player anotherPlayer = game.getPlayers().get(1);
		anotherPlayer.getSecondCard().reveal();
		game.playerAssassinatesOtherPlayer(player, anotherPlayer);
		assertThat(anotherPlayer.getSecondCard().isRevealed(), equalTo(true));
		assertThat(anotherPlayer.getFirstCard().isRevealed(), equalTo(true));
	}
	
	@Test
	public void assassinationRevealsWhateverCardIsNotYetRevealed() throws Exception {
		Game game = new Game(2);
		game.deal();
		Player player = game.getPlayers().get(0);
		player.setMoney(3);
		Player anotherPlayer = game.getPlayers().get(1);
		anotherPlayer.getFirstCard().reveal();
		game.playerAssassinatesOtherPlayer(player, anotherPlayer);
		assertThat(anotherPlayer.getSecondCard().isRevealed(), equalTo(true));
		assertThat(anotherPlayer.getFirstCard().isRevealed(), equalTo(true));
	}
	
	
	@Test
	public void assassinationCannotBePerformedByPlayerWithFewerThanThreeCoins() throws Exception {
		Game game = new Game(2);
		game.deal();
		Player player = game.getPlayers().get(0);
		player.setMoney(2);
		assertThat(player.getCoins(), equalTo(2));
		Player anotherPlayer = game.getPlayers().get(1);
		game.playerAssassinatesOtherPlayer(player, anotherPlayer);
		assertThat(player.getCoins(), equalTo(2));
		assertThat(anotherPlayer.getSecondCard().isRevealed(), equalTo(false));
		assertThat(anotherPlayer.getFirstCard().isRevealed(), equalTo(false));
	}
	
	@Test
	public void coupRevealsOtherPlayersCardButCostsSevenCoins() throws Exception {
		Game game = new Game(2);
		game.deal();
		Player player = game.getPlayers().get(0);
		player.setMoney(7);
		assertThat(player.getCoins(), equalTo(7));
		Player anotherPlayer = game.getPlayers().get(1);
		game.playerCoupsOtherPlayer(player, anotherPlayer);
		assertThat(player.getCoins(), equalTo(0));
		assertThat(anotherPlayer.getCards().get(0).isRevealed(), not(anotherPlayer.getCards().get(1).isRevealed()));
		
		player.setMoney(7);
		assertThat(player.getCoins(), equalTo(7));
		game.playerCoupsOtherPlayer(player, anotherPlayer);
		assertThat(player.getCoins(), equalTo(0));
		assertThat(anotherPlayer.getSecondCard().isRevealed(), equalTo(true));
		assertThat(anotherPlayer.getFirstCard().isRevealed(), equalTo(true));
	}
	
	@Test
	public void coupCannotBePerformedByPlayerWithFewerThanSevenCoins() throws Exception {
		Game game = new Game(2);
		game.deal();
		Player player = game.getPlayers().get(0);
		player.setMoney(6);
		assertThat(player.getCoins(), equalTo(6));
		Player anotherPlayer = game.getPlayers().get(1);
		game.playerCoupsOtherPlayer(player, anotherPlayer);
		assertThat(player.getCoins(), equalTo(6));
		assertThat(anotherPlayer.getSecondCard().isRevealed(), equalTo(false));
		assertThat(anotherPlayer.getFirstCard().isRevealed(), equalTo(false));
	}
	
	@Test
	public void ambassadorActionLetsPlayerChooseFromTheirCardsPlusTwoFromDeck() throws Exception {
		Game game = new Game(3);
		game.deal();
		Player player = game.getPlayers().get(0);
		final List<Card> currentCards = player.getCards();
		
		List<Card> cardsInDeckBeforeAmbassador = game.getDeck().getCopyOfCards();
		
		final List<Card> allCardChoices = game.playerCanChooseFromTheseCardsAsAmbassador(player);
		assertThat(allCardChoices.size(), equalTo(4));
		assertTrue(allCardChoices.containsAll(currentCards));
		List<Card> newCardChoices = new ArrayList<Card>(allCardChoices){{
			removeAll(currentCards);
		}};
		for(Card card : newCardChoices){
			assertTrue(cardsInDeckBeforeAmbassador.contains(card));
		}
		
		game.playerChoosesTheseCardsAsAmbassador(player,allCardChoices.get(0), allCardChoices.get(2));
		
		assertThat(player.getFirstCard(), equalTo(allCardChoices.get(0)));
		assertThat(player.getSecondCard(), equalTo(allCardChoices.get(2)));
		
		assertTrue(game.getDeck().contains(allCardChoices.get(1)));
		assertTrue(game.getDeck().contains(allCardChoices.get(3)));
		assertFalse(game.getDeck().contains(allCardChoices.get(0)));
		assertFalse(game.getDeck().contains(allCardChoices.get(2)));
	}
	
	@Test
	public void whenPlayerACallsPlayerBThenGameDeterminesIfPlayerAIsRight_PlayerIsWrong() throws Exception {
		Game game = new Game(2);
		game.deal();
		CardType cardPlayerHas = game.getPlayers().get(1).getSecondCard().getType();
		assertFalse(game.isPlayerRightAboutOtherPlayerNotHavingCard(0,1,cardPlayerHas));
		cardPlayerHas = game.getPlayers().get(1).getFirstCard().getType();
		assertFalse(game.isPlayerRightAboutOtherPlayerNotHavingCard(0,1,cardPlayerHas));
	}
	
	@Test
	public void whenPlayerACallsPlayerBThenGameDeterminesIfPlayerAIsRight_PlayerIsRight() throws Exception {
		Game game = new Game(2);
		game.deal();
		List<CardType> allTypes = new ArrayList<CardType>(Arrays.asList(CardType.values()));
		allTypes.remove(game.getPlayers().get(1).getFirstCard().getType());
		allTypes.remove(game.getPlayers().get(1).getSecondCard().getType());
		assertTrue(game.isPlayerRightAboutOtherPlayerNotHavingCard(0,1,allTypes.get(0)));
	}
	
	@Test //TODO Need to let Player A choose which card to lose
	public void whenPlayerACallsPlayerBAndIsWrong_ThenPlayerALosesACard() throws Exception {
		Game game = new Game(2);
		game.deal();
		CardType cardPlayerHas = game.getPlayers().get(1).getSecondCard().getType();
		game.playerAccussesOtherPlayerOfBluffing(0,1,cardPlayerHas);
		
		assertSame(game.getPlayers().get(1).getCards().get(0).isRevealed(),game.getPlayers().get(1).getCards().get(1).isRevealed());
		assertNotSame(game.getPlayers().get(0).getCards().get(0).isRevealed(),game.getPlayers().get(0).getCards().get(1).isRevealed());
	}
	
	@Test
	public void whenPlayerACallsPlayerBAndIsWrong_ThenPlayerBReplacesCalledCard() throws Exception {
		Game game = new Game(2);
		game.deal();
		
		if(game.getPlayers().get(1).getFirstCard().getType().equals(game.getPlayers().get(1).getSecondCard())){
			whenPlayerACallsPlayerBAndIsWrong_ThenPlayerBReplacesCalledCard(); //try again until cards aren't the same
		}
		else{
			Card cardPlayerHas = game.getPlayers().get(1).getSecondCard();
			CardType cardTypePlayerHas = cardPlayerHas.getType();
			game.playerAccussesOtherPlayerOfBluffing(0,1,cardTypePlayerHas);
			
			Card cardPlayerHasAfter = game.getPlayers().get(1).getSecondCard();
			if(cardPlayerHasAfter.equals(cardPlayerHas)){
				whenPlayerACallsPlayerBAndIsWrong_ThenPlayerBReplacesCalledCard(); //recursively call until cards are different - should terminate
			}else{
				assertNotSame(cardPlayerHasAfter, cardPlayerHas);
			}
		}
		
	}
	
	@Test
	public void whenPlayerACallsPlayerBAndIsWrong_PlayerBFirstCardInsteadOfSecond() throws Exception {
		Game game = new Game(2);
		game.deal();
		Card cardPlayerHas = game.getPlayers().get(1).getFirstCard();
		Card otherCardPlayerHas = game.getPlayers().get(1).getSecondCard();
		CardType cardTypePlayerHas = cardPlayerHas.getType();
		
		game.playerAccussesOtherPlayerOfBluffing(0,1,cardTypePlayerHas);
		
		assertSame(game.getPlayers().get(1).getCards().get(0).isRevealed(),game.getPlayers().get(1).getCards().get(1).isRevealed());
		assertNotSame(game.getPlayers().get(0).getCards().get(0).isRevealed(),game.getPlayers().get(0).getCards().get(1).isRevealed());
		
		//Player still has the card that was not attacked:
		assertEquals(otherCardPlayerHas, game.getPlayers().get(1).getSecondCard());
		
		Card cardPlayerHasAfter = game.getPlayers().get(1).getFirstCard();
		if(cardPlayerHasAfter.equals(cardPlayerHas)){
			whenPlayerACallsPlayerBAndIsWrong_PlayerBFirstCardInsteadOfSecond(); //recursively call until cards are different - should terminate
		}else{
			assertNotSame(cardPlayerHasAfter, cardPlayerHas);
		}
		
	}

	@Test
	public void whenPlayerACallsPlayerBAndIsRight_ThenPlayerADoesNotLoseAnyCard() throws Exception {
		Game game = new Game(2);
		game.deal();
		List<CardType> cardTypesPlayerDoesNotHave = new ArrayList<CardType>(Arrays.asList(CardType.values()));
		cardTypesPlayerDoesNotHave.remove(game.getPlayers().get(1).getFirstCard().getType());
		cardTypesPlayerDoesNotHave.remove(game.getPlayers().get(1).getSecondCard().getType());
		game.playerAccussesOtherPlayerOfBluffing(0,1,cardTypesPlayerDoesNotHave.get(0));
		
		assertFalse(game.getPlayers().get(0).getCards().get(0).isRevealed());
		assertFalse(game.getPlayers().get(0).getCards().get(1).isRevealed());
	}
	
	@Test //TODO Need to let player B pick which card to lose
	public void whenPlayerACallsPlayerBAndIsRight_ThenPlayerBLosesACard() throws Exception {
		Game game = new Game(2);
		game.deal();
		List<CardType> cardTypesPlayerDoesNotHave = new ArrayList<CardType>(Arrays.asList(CardType.values()));
		cardTypesPlayerDoesNotHave.remove(game.getPlayers().get(1).getFirstCard().getType());
		cardTypesPlayerDoesNotHave.remove(game.getPlayers().get(1).getSecondCard().getType());
		game.playerAccussesOtherPlayerOfBluffing(0,1,cardTypesPlayerDoesNotHave.get(0));
		
		assertNotSame(game.getPlayers().get(1).getCards().get(0).isRevealed(),game.getPlayers().get(1).getCards().get(1).isRevealed());
		
	}
	
	
}

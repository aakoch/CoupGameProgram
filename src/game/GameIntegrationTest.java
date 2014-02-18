package game;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import game.actions.AmbassadorAction;
import game.actions.AmbassadorDefense;
import game.actions.AssassinAction;
import game.actions.BluffCallerOption;
import game.actions.CaptainAction;
import game.actions.CaptainDefense;
import game.actions.CardChooser;
import game.actions.ContessaDefense;
import game.actions.CoupAction;
import game.actions.DefenseChooser;
import game.actions.DukeAction;
import game.actions.DukeDefense;
import game.actions.ForeignAidAction;
import game.actions.IncomeAction;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class GameIntegrationTest {
	
	private Game game;
	private ActionChooser actionChooser;
	//FIXME make denfense chooser or its options based on response of action chooser
	private DefenseChooser defenseChooser;
	private BluffCallerOption bluffCallerOption;
	
	@Before
	public void setUp(){
		game = new Game(3);
		game.deal();
		actionChooser = Mockito.mock(ActionChooser.class);
		game.setActionChooser(actionChooser);
		defenseChooser = Mockito.mock(DefenseChooser.class);
		game.setDefenseChooser(defenseChooser);
		bluffCallerOption = Mockito.mock(BluffCallerOption.class);
		game.setBluffCallerOption(bluffCallerOption);
	}

	@Test
	public void firstPlayerChoosesAction_Duke() throws Exception {
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new DukeAction());
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(5));
		assertThat(game.getPlayer(1).getCoins(),equalTo(2));
		assertThat(game.getPlayer(2).getCoins(),equalTo(2));
	}
	
	@Test
	public void firstTwoPlayersChoosesAction_Duke() throws Exception {
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new DukeAction());
		Mockito.when(actionChooser.chooseAction(game.getPlayer(1))).thenReturn(new DukeAction());
		
		game.oneTurn();
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(5));
		assertThat(game.getPlayer(1).getCoins(),equalTo(5));
		assertThat(game.getPlayer(2).getCoins(),equalTo(2));
	}
	
	@Test
	public void firstPlayerChoosesAction_ForeignAid() throws Exception {
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new ForeignAidAction(game.getPlayer(0), game.getPlayers()));
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(4));
		assertThat(game.getPlayer(1).getCoins(),equalTo(2));
		assertThat(game.getPlayer(2).getCoins(),equalTo(2));
	}
	
	@Test
	public void firstPlayerChoosesAction_CaptainStealingFromThirdPlayer() throws Exception {
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new CaptainAction(game.getPlayer(2)));
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(4));
		assertThat(game.getPlayer(1).getCoins(),equalTo(2));
		assertThat(game.getPlayer(2).getCoins(),equalTo(0));
	}
	
	@Test
	public void firstPlayerChoosesAction_Ambassador() throws Exception {
		final List<Card> cardsPlayerHadBefore = new ArrayList<Card>(game.getPlayer(0).getCards());
		
		CardChooser cardChooser = Mockito.mock(CardChooser.class);
		Mockito.when(cardChooser.chooseCards(Mockito.anyList(), Mockito.eq(game.getPlayer(0)))).thenAnswer(
				new Answer<CardPair>(){

					@Override
					public CardPair answer(InvocationOnMock invocation)
							throws Throwable {
						List<Card> cardChoices = (List<Card>) invocation.getArguments()[0];
						cardChoices.removeAll(cardsPlayerHadBefore);
						//Trade in for cards player did NOT have
						return new CardPair(cardChoices.get(0),cardChoices.get(1));
					}
					
				});
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new AmbassadorAction(game, cardChooser));
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(2));
		assertThat(game.getPlayer(1).getCoins(),equalTo(2));
		assertThat(game.getPlayer(2).getCoins(),equalTo(2));
		
		List<Card> cardsPlayerHasNow = new ArrayList<Card>(game.getPlayer(0).getCards());
		cardsPlayerHasNow.removeAll(cardsPlayerHadBefore);
		assertThat(cardsPlayerHasNow.size(),equalTo(2)); //Cards player has now should NOT contain any of the cards he previously had
	}
	
	@Test
	public void allPlayersChooseAction_Income() throws Exception {
		Mockito.when(actionChooser.chooseAction(Mockito.any(Player.class))).thenReturn(new IncomeAction());
		
		game.oneTurn();
		game.oneTurn();
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(3));
		assertThat(game.getPlayer(1).getCoins(),equalTo(3));
		assertThat(game.getPlayer(2).getCoins(),equalTo(3));
	}
	
	@Test
	public void allPlayersChooseIncome_ThenFirstAssassinatesThird() throws Exception {
		assertThat(game.getPlayer(2).getFirstCard().isRevealed(),equalTo(false));
		assertSame(game.getPlayer(2).getFirstCard().isRevealed(),game.getPlayer(2).getSecondCard().isRevealed());
		
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(
				new IncomeAction()).thenReturn(new AssassinAction(game.getPlayer(2)));
		Mockito.when(actionChooser.chooseAction(game.getPlayer(1))).thenReturn(new IncomeAction());
		Mockito.when(actionChooser.chooseAction(game.getPlayer(2))).thenReturn(new IncomeAction());
				
		game.oneTurn();
		game.oneTurn();
		game.oneTurn();
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(0));
		assertThat(game.getPlayer(1).getCoins(),equalTo(3));
		assertThat(game.getPlayer(2).getCoins(),equalTo(3));
		
		assertNotSame(game.getPlayer(2).getFirstCard().isRevealed(),game.getPlayer(2).getSecondCard().isRevealed());
	}
	
	@Test
	public void allPlayersChooseDukeTwice_ThenFirstCoupsThird() throws Exception {
		assertThat(game.getPlayer(2).getFirstCard().isRevealed(),equalTo(false));
		assertSame(game.getPlayer(2).getFirstCard().isRevealed(),game.getPlayer(2).getSecondCard().isRevealed());
		
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(
				new DukeAction()).thenReturn(new DukeAction()).thenReturn(new CoupAction(game.getPlayer(2)));
		Mockito.when(actionChooser.chooseAction(game.getPlayer(1))).thenReturn(new DukeAction());
		Mockito.when(actionChooser.chooseAction(game.getPlayer(2))).thenReturn(new DukeAction());
				
		game.oneTurn();
		game.oneTurn();
		game.oneTurn();
		
		game.oneTurn();
		game.oneTurn();
		game.oneTurn();
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(1));
		assertThat(game.getPlayer(1).getCoins(),equalTo(8));
		assertThat(game.getPlayer(2).getCoins(),equalTo(8));
		
		assertNotSame(game.getPlayer(2).getFirstCard().isRevealed(),game.getPlayer(2).getSecondCard().isRevealed());
	}
	
	@Test
	public void allPlayersChooseIncome_ThenFirstTriesAssassinateThird_ThirdBlocksWithContessa() throws Exception {
		assertThat(game.getPlayer(2).getFirstCard().isRevealed(),equalTo(false));
		assertSame(game.getPlayer(2).getFirstCard().isRevealed(),game.getPlayer(2).getSecondCard().isRevealed());
		
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(
				new IncomeAction()).thenReturn(new AssassinAction(game.getPlayer(2)));
		Mockito.when(actionChooser.chooseAction(game.getPlayer(1))).thenReturn(new IncomeAction());
		Mockito.when(actionChooser.chooseAction(game.getPlayer(2))).thenReturn(new IncomeAction());
				
		Mockito.when(defenseChooser.chooseDefense(game.getPlayer(2))).thenReturn(new ContessaDefense());
		
		game.oneTurn();
		game.oneTurn();
		game.oneTurn();
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(0)); //Still loses money
		assertThat(game.getPlayer(1).getCoins(),equalTo(3));
		assertThat(game.getPlayer(2).getCoins(),equalTo(3));
		
		//Does not have to reveal card
		assertThat(game.getPlayer(2).getFirstCard().isRevealed(),equalTo(false));
		assertSame(game.getPlayer(2).getFirstCard().isRevealed(),game.getPlayer(2).getSecondCard().isRevealed());
	}
	
	@Test
	public void firstPlayerChoosesAction_CaptainStealingFromThirdPlayer_ThirdPlayerBlocksWithCaptain() throws Exception {
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new CaptainAction(game.getPlayer(2)));
		Mockito.when(defenseChooser.chooseDefense(game.getPlayer(2))).thenReturn(new CaptainDefense());
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(2));
		assertThat(game.getPlayer(1).getCoins(),equalTo(2));
		assertThat(game.getPlayer(2).getCoins(),equalTo(2));
	}

	@Test
	public void firstPlayerChoosesAction_CaptainStealingFromThirdPlayer_ThirdPlayerBlocksWithAmbassador() throws Exception {
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new CaptainAction(game.getPlayer(2)));
		Mockito.when(defenseChooser.chooseDefense(game.getPlayer(2))).thenReturn(new AmbassadorDefense());
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(2));
		assertThat(game.getPlayer(1).getCoins(),equalTo(2));
		assertThat(game.getPlayer(2).getCoins(),equalTo(2));
	}
	
	@Test
	public void firstPlayerChoosesActionDuke_SecondPlayerCorrectlyCallsHisBluff() throws Exception {
		if(game.getPlayer(0).getFirstCard().getType().equals(CardType.duke) || 
				game.getPlayer(0).getSecondCard().getType().equals(CardType.duke)){
			//Get into situation where player does NOT have the duke they claim to have
			setUp(); 
			firstPlayerChoosesActionDuke_SecondPlayerCorrectlyCallsHisBluff();
		}else{
			Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new DukeAction());
			Mockito.when(bluffCallerOption.callBluff(game.getPlayer(1), game.getPlayer(0), CardType.duke)).thenReturn(true);
			
			game.oneTurn();
			
			assertThat(game.getPlayer(0).getCoins(),equalTo(2)); //player 0 does not get to get their money
			assertThat(game.getPlayer(1).getCoins(),equalTo(2));
			assertThat(game.getPlayer(2).getCoins(),equalTo(2));
			
			//Player 0 loses a card
			assertNotSame(game.getPlayers().get(0).getCards().get(0).isRevealed(),game.getPlayers().get(0).getCards().get(1).isRevealed());
			
		}
	}
	
	@Test
	public void firstPlayerChoosesActionDuke_SecondPlayerIncorrectlyCallsHisBluff() throws Exception {
		if(game.getPlayer(0).getFirstCard().getType().equals(CardType.duke) || 
				game.getPlayer(0).getSecondCard().getType().equals(CardType.duke)){
			//Make sure we're in a situation where player DOES have the duke they claim to have
			Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new DukeAction());
			Mockito.when(bluffCallerOption.callBluff(game.getPlayer(1), game.getPlayer(0), CardType.duke)).thenReturn(true);
			
			game.oneTurn();
			
			assertThat(game.getPlayer(0).getCoins(),equalTo(5)); //player 0 still gets their money
			assertThat(game.getPlayer(1).getCoins(),equalTo(2));
			assertThat(game.getPlayer(2).getCoins(),equalTo(2));
			
			//Player 0 loses no card
			assertFalse(game.getPlayers().get(0).getCards().get(0).isRevealed());
			assertSame(game.getPlayers().get(0).getCards().get(0).isRevealed(),game.getPlayers().get(0).getCards().get(1).isRevealed());
			
			//Player 1 loses a card
			assertNotSame(game.getPlayers().get(1).getCards().get(0).isRevealed(),game.getPlayers().get(1).getCards().get(1).isRevealed());
		}else{
			//Try again if first player doen't have duke
			setUp(); 
			firstPlayerChoosesActionDuke_SecondPlayerIncorrectlyCallsHisBluff();
		}
	}
	
	@Test
	public void firstPlayerChoosesActionDuke_ThirdPlayerCorrectlyCallsHisBluff() throws Exception {
		if(game.getPlayer(0).getFirstCard().getType().equals(CardType.duke) || 
				game.getPlayer(0).getSecondCard().getType().equals(CardType.duke)){
			//Get into situation where player does NOT have the duke they claim to have
			setUp(); 
			firstPlayerChoosesActionDuke_SecondPlayerCorrectlyCallsHisBluff();
		}else{
			Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new DukeAction());
			Mockito.when(bluffCallerOption.callBluff(game.getPlayer(2), game.getPlayer(0), CardType.duke)).thenReturn(true);
			
			game.oneTurn();
			
			assertThat(game.getPlayer(0).getCoins(),equalTo(2)); //player 0 does not get to get their money
			assertThat(game.getPlayer(1).getCoins(),equalTo(2));
			assertThat(game.getPlayer(2).getCoins(),equalTo(2));
			
			//Player 0 loses a card
			assertNotSame(game.getPlayers().get(0).getCards().get(0).isRevealed(),game.getPlayers().get(0).getCards().get(1).isRevealed());
			
		}
	}
	
	@Test
	public void firstPlayerChoosesActionDuke_ThirdPlayerIncorrectlyCallsHisBluff() throws Exception {
		if(game.getPlayer(0).getFirstCard().getType().equals(CardType.duke) || 
				game.getPlayer(0).getSecondCard().getType().equals(CardType.duke)){
			//Make sure we're in a situation where player DOES have the duke they claim to have
			Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new DukeAction());
			Mockito.when(bluffCallerOption.callBluff(game.getPlayer(2), game.getPlayer(0), CardType.duke)).thenReturn(true);
			
			game.oneTurn();
			
			assertThat(game.getPlayer(0).getCoins(),equalTo(5)); //player 0 still gets their money
			assertThat(game.getPlayer(1).getCoins(),equalTo(2));
			assertThat(game.getPlayer(2).getCoins(),equalTo(2));
			
			//Player 0 loses no card
			assertFalse(game.getPlayers().get(0).getCards().get(0).isRevealed());
			assertSame(game.getPlayers().get(0).getCards().get(0).isRevealed(),game.getPlayers().get(0).getCards().get(1).isRevealed());
			
			//Player 2 loses a card
			assertNotSame(game.getPlayers().get(2).getCards().get(0).isRevealed(),game.getPlayers().get(2).getCards().get(1).isRevealed());
		}else{
			//Try again if first player doen't have duke
			setUp(); 
			firstPlayerChoosesActionDuke_SecondPlayerIncorrectlyCallsHisBluff();
		}
	}
	
	@Test
	public void firstPlayerChoosesActionDuke_OnlyOnePlayerPenalizedForIncorrectlyCallingHisBluff() throws Exception {
		if(game.getPlayer(0).getFirstCard().getType().equals(CardType.duke) || 
				game.getPlayer(0).getSecondCard().getType().equals(CardType.duke)){
			//Make sure we're in a situation where player DOES have the duke they claim to have
			Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new DukeAction());
			Mockito.when(bluffCallerOption.callBluff(game.getPlayer(1), game.getPlayer(0), CardType.duke)).thenReturn(true);
			Mockito.when(bluffCallerOption.callBluff(game.getPlayer(2), game.getPlayer(0), CardType.duke)).thenReturn(true);
			
			game.oneTurn();
			
			assertThat(game.getPlayer(0).getCoins(),equalTo(5)); //player 0 still gets their money
			assertThat(game.getPlayer(1).getCoins(),equalTo(2));
			assertThat(game.getPlayer(2).getCoins(),equalTo(2));
			
			//Player 0 loses no card
			assertFalse(game.getPlayers().get(0).getCards().get(0).isRevealed());
			assertSame(game.getPlayers().get(0).getCards().get(0).isRevealed(),game.getPlayers().get(0).getCards().get(1).isRevealed());
			
			//Player 1 loses a card -- for now have the option to call go in turn order TODO
			assertNotSame(game.getPlayers().get(1).getCards().get(0).isRevealed(),game.getPlayers().get(1).getCards().get(1).isRevealed());

			//Player 2 loses no card
			assertFalse(game.getPlayers().get(2).getCards().get(0).isRevealed());
			assertSame(game.getPlayers().get(2).getCards().get(0).isRevealed(),game.getPlayers().get(2).getCards().get(1).isRevealed());
			
		}else{
			//Try again if first player doen't have duke
			setUp(); 
			firstPlayerChoosesActionDuke_SecondPlayerIncorrectlyCallsHisBluff();
		}
	}
	
	@Test
	public void firstPlayerChoosesActionDuke_SecondPlayerCorrectlyCallsHisBluff_SecondPlayerGetsNextTurn() throws Exception {
		if(game.getPlayer(0).getFirstCard().getType().equals(CardType.duke) || 
				game.getPlayer(0).getSecondCard().getType().equals(CardType.duke)){
			//Get into situation where player does NOT have the duke they claim to have
			setUp(); 
			firstPlayerChoosesActionDuke_SecondPlayerCorrectlyCallsHisBluff();
		}else{
			Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new DukeAction());
			Mockito.when(bluffCallerOption.callBluff(game.getPlayer(1), game.getPlayer(0), CardType.duke)).thenReturn(true);
			Mockito.when(actionChooser.chooseAction(game.getPlayer(1))).thenReturn(new DukeAction());
			
			game.oneTurn();
			game.oneTurn();
			
			assertThat(game.getPlayer(0).getCoins(),equalTo(2)); //player 0 does not get to get their money
			assertThat(game.getPlayer(1).getCoins(),equalTo(5)); //player 1 gets money on their turn
			assertThat(game.getPlayer(2).getCoins(),equalTo(2));
			
			//Player 0 loses a card
			assertNotSame(game.getPlayers().get(0).getCards().get(0).isRevealed(),game.getPlayers().get(0).getCards().get(1).isRevealed());
			
		}
	}
	
	@Test
	public void firstPlayerChoosesAction_ForeignAid_SecondPlayerBlocksWithDuke() throws Exception {
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new ForeignAidAction(game.getPlayer(0), game.getPlayers()));
		Mockito.when(defenseChooser.chooseDefense(game.getPlayer(1))).thenReturn(new DukeDefense());
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(2)); //First player was blocked - does not get to use foreign aid
		assertThat(game.getPlayer(1).getCoins(),equalTo(2));
		assertThat(game.getPlayer(2).getCoins(),equalTo(2));
	}
	
	@Test
	public void firstPlayerChoosesAction_ForeignAid_ThirdPlayerBlocksWithDuke() throws Exception {
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new ForeignAidAction(game.getPlayer(0), game.getPlayers()));
		Mockito.when(defenseChooser.chooseDefense(game.getPlayer(2))).thenReturn(new DukeDefense());
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(2)); //First player was blocked - does not get to use foreign aid
		assertThat(game.getPlayer(1).getCoins(),equalTo(2));
		assertThat(game.getPlayer(2).getCoins(),equalTo(2));
	}
	
	@Test
	public void firstPlayerChoosesAction_ForeignAid_EitherSecondOrThirdBlocksWithDuke() throws Exception {
		for(int i = 0; i < 10; i++){
			setUp();
			Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new ForeignAidAction(game.getPlayer(0), game.getPlayers()));
			Mockito.when(defenseChooser.chooseDefense(game.getPlayer(1 + (int)(2*Math.random())))).thenReturn(new DukeDefense());
			
			game.oneTurn();
			
			assertThat(game.getPlayer(0).getCoins(),equalTo(2)); //First player was blocked - does not get to use foreign aid
			assertThat(game.getPlayer(1).getCoins(),equalTo(2));
			assertThat(game.getPlayer(2).getCoins(),equalTo(2));
		}
	}
	
	@Test
	public void firstPlayerChoosesActionForeignAid_SecondPlayerBlocksWithDuke_FirstPlayerSuccessfullyCallsBluff() throws Exception {
		if(game.getPlayer(1).getFirstCard().getType().equals(CardType.duke) || game.getPlayer(1).getSecondCard().getType().equals(CardType.duke)){
			//Try again until we get scenario where player 1 does not have duke
			setUp();
			firstPlayerChoosesActionForeignAid_SecondPlayerBlocksWithDuke_FirstPlayerSuccessfullyCallsBluff();
		}
		else{
			Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new ForeignAidAction(game.getPlayer(0), game.getPlayers()));
			Mockito.when(defenseChooser.chooseDefense(game.getPlayer(1))).thenReturn(new DukeDefense());
			Mockito.when(bluffCallerOption.callBluff(game.getPlayer(0), game.getPlayer(1), CardType.duke)).thenReturn(true);
			
			game.oneTurn();
			
			assertThat(game.getPlayer(0).getCoins(),equalTo(4)); //First player still gets foreign aid!
			assertThat(game.getPlayer(1).getCoins(),equalTo(2));
			assertThat(game.getPlayer(2).getCoins(),equalTo(2));
			
			//Player 1 loses a card
			assertNotSame(game.getPlayers().get(1).getCards().get(0).isRevealed(),game.getPlayers().get(1).getCards().get(1).isRevealed());
			
		}
	}
	
	@Test
	public void firstPlayerChoosesActionForeignAid_SecondPlayerBlocksWithDuke_FirstPlayerUnsuccessfullyCallsBluff() throws Exception {
		if(game.getPlayer(1).getFirstCard().getType().equals(CardType.duke)){
			Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new ForeignAidAction(game.getPlayer(0), game.getPlayers()));
			Mockito.when(defenseChooser.chooseDefense(game.getPlayer(1))).thenReturn(new DukeDefense());
			Mockito.when(bluffCallerOption.callBluff(game.getPlayer(0), game.getPlayer(1), CardType.duke)).thenReturn(true);
			
			Card originalCard = game.getPlayer(1).getFirstCard();
			
			game.oneTurn();
			
			assertThat(game.getPlayer(0).getCoins(),equalTo(2)); //First player does not get foreign aid!
			assertThat(game.getPlayer(1).getCoins(),equalTo(2));
			assertThat(game.getPlayer(2).getCoins(),equalTo(2));
			
			//Player 0 loses a card since they were wrong
			assertNotSame(game.getPlayers().get(0).getCards().get(0).isRevealed(),game.getPlayers().get(0).getCards().get(1).isRevealed());
			
			assertNotSame(game.getPlayer(1).getFirstCard(), originalCard); //Player 1 got different card
			//But player 1 still does not have any cards revealed:
			assertSame(game.getPlayers().get(1).getCards().get(0).isRevealed(),false);
			assertSame(game.getPlayers().get(1).getCards().get(0).isRevealed(),game.getPlayers().get(1).getCards().get(1).isRevealed());
			
		}
		else{
			//Try again until we get scenario where player 1 DOES have duke (as first card - for easier testing)
			setUp();
			firstPlayerChoosesActionForeignAid_SecondPlayerBlocksWithDuke_FirstPlayerUnsuccessfullyCallsBluff();
		}
	}	
	//TODO Need mapping from actions to possible defenses -- only offer those options
	
	@Test
	public void firstPlayerChoosesActionAmbassador_CannotReplaceRevealedCard() throws Exception {
		final List<Card> cardsPlayerHadBefore = new ArrayList<Card>(game.getPlayer(0).getCards());
		
		game.getPlayer(0).getFirstCard().reveal(); //Now the first card is revealed... it should still be in the player's hand
		
		CardChooser cardChooser = Mockito.mock(CardChooser.class);
		Mockito.when(cardChooser.chooseCards(Mockito.anyList(), Mockito.eq(game.getPlayer(0)), 
				Mockito.eq(game.getPlayer(0).getFirstCard()))).thenAnswer(
				new Answer<CardPair>(){

					@Override
					public CardPair answer(InvocationOnMock invocation)
							throws Throwable {
						List<Card> cardChoices = (List<Card>) invocation.getArguments()[0];
						cardChoices.removeAll(cardsPlayerHadBefore);
						//Trade in for cards player did NOT have
						return new CardPair((Card) invocation.getArguments()[2],cardChoices.get(0));
					}
					
				});
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new AmbassadorAction(game, cardChooser));
		
		game.oneTurn();
		
		assertThat(game.getPlayer(0).getCoins(),equalTo(2));
		assertThat(game.getPlayer(1).getCoins(),equalTo(2));
		assertThat(game.getPlayer(2).getCoins(),equalTo(2));
		
		List<Card> cardsPlayerHasNow = new ArrayList<Card>(game.getPlayer(0).getCards());
		cardsPlayerHasNow.removeAll(cardsPlayerHadBefore);
		assertThat(cardsPlayerHasNow.size(),equalTo(1)); //Cards player has now should contain one of the cards he previously had
		//One of the cards should be revealed (the one we had before, but I'm not testing that)
		assertNotSame(game.getPlayer(0).getFirstCard().isRevealed(),game.getPlayer(0).getSecondCard().isRevealed());
		
	}
	
}

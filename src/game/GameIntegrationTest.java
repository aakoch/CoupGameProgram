package game;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import game.actions.AmbassadorAction;
import game.actions.AssassinAction;
import game.actions.CaptainAction;
import game.actions.CardChooser;
import game.actions.ContessaDefense;
import game.actions.CoupAction;
import game.actions.DefenseChooser;
import game.actions.DukeAction;
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
	
	@Before
	public void setUp(){
		game = new Game(3);
		game.deal();
		actionChooser = Mockito.mock(ActionChooser.class);
		game.setActionChooser(actionChooser);
		defenseChooser = Mockito.mock(DefenseChooser.class);
		game.setDefenseChooser(defenseChooser);
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
		Mockito.when(actionChooser.chooseAction(game.getPlayer(0))).thenReturn(new ForeignAidAction());
		
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
	
	//TODO Need mapping from actions to possible defenses -- only offer those options
	
	//TODO Test blocking/defending foreign aid using duke
	
	//TODO Test calling bluffs - can call action OR defense
	
	
}

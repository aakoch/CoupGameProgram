package game.ui;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MainUiTest {
	
	IndividualPlayer player1, player2, player3;
	List<IndividualPlayer> allPlayerUis;
	
	@Before
	public void setUp(){
		player1 = Mockito.mock(IndividualPlayer.class);
		player2 = Mockito.mock(IndividualPlayer.class);
		player3 = Mockito.mock(IndividualPlayer.class);
		allPlayerUis = new ArrayList<IndividualPlayer>();
		allPlayerUis.add(player1);
		allPlayerUis.add(player2);
		allPlayerUis.add(player3);
	}

	@Test
	public void testGetNextPlayerNoneToRemove_On1() {
		List<Integer> playersToRemove = new ArrayList<Integer>();
		int nextId = MainUi.getNextPlayerUi(0, allPlayerUis, playersToRemove);
		assertEquals(player2, allPlayerUis.get(nextId));
	}
	
	@Test
	public void testGetNextPlayerNoneToRemove_On2() {
		List<Integer> playersToRemove = new ArrayList<Integer>();
		int nextId = MainUi.getNextPlayerUi(1, allPlayerUis, playersToRemove);
		assertEquals(player3, allPlayerUis.get(nextId));
	}
	
	@Test
	public void testGetNextPlayerNoneToRemove_On3() {
		List<Integer> playersToRemove = new ArrayList<Integer>();
		int nextId = MainUi.getNextPlayerUi(2, allPlayerUis, playersToRemove);
		assertEquals(player1, allPlayerUis.get(nextId));
	}
	
	@Test
	public void testGetNextPlayerRemovePlayerWhoJustWent_On1() {
		List<Integer> playersToRemove = new ArrayList<Integer>();
		playersToRemove.add(0);
		int nextId = MainUi.getNextPlayerUi(0, allPlayerUis, playersToRemove);
		assertEquals(player2, allPlayerUis.get(nextId));
	}
	
	@Test
	public void testGetNextPlayerRemovePlayerWhoJustWent_On2() {
		List<Integer> playersToRemove = new ArrayList<Integer>();
		playersToRemove.add(1);
		int nextId = MainUi.getNextPlayerUi(1, allPlayerUis, playersToRemove);
		assertEquals(player3, allPlayerUis.get(nextId));
	}
	
	@Test
	public void testGetNextPlayerRemovePlayerWhoJustWent_On3() {
		List<Integer> playersToRemove = new ArrayList<Integer>();
		playersToRemove.add(2);
		int nextId = MainUi.getNextPlayerUi(2, allPlayerUis, playersToRemove);
		assertEquals(player1, allPlayerUis.get(nextId));
	}

	@Test
	public void testGetNextPlayerRemovePlayerBeforeCurrent() {
		List<Integer> playersToRemove = new ArrayList<Integer>();
		playersToRemove.add(0);
		int nextId = MainUi.getNextPlayerUi(1, allPlayerUis, playersToRemove);
		assertEquals(player3, allPlayerUis.get(nextId));
	}
	
	@Test
	public void testGetNextPlayerRemovePlayerAfterCurrent() {
		List<Integer> playersToRemove = new ArrayList<Integer>();
		playersToRemove.add(2);
		int nextId = MainUi.getNextPlayerUi(1, allPlayerUis, playersToRemove);
		assertEquals(player1, allPlayerUis.get(nextId));
	}
}

package game;

import game.actions.Action;
import game.actions.Defense;
import game.actions.DefenseChooser;

import java.util.ArrayList;
import java.util.List;

public class Game {
	
	private Deck deck = new Deck();
	private final int numPlayers;
	private final List<Player> players;

	public Game(int numPlayers) {
		players = new ArrayList<Player>();
		for(int i = 0; i < numPlayers; i++){
			players.add(new Player());
		}
		this.numPlayers = numPlayers;
	}

	public Deck getDeck() {
		return deck;
	}

	public void deal() {
		for(int i = 0; i < numPlayers; i++){
			players.get(i).receive(deck.deal());
			players.get(i).receive(deck.deal());
		}
	}

	//FIXME Probably should not expose this list... use the "getPlayer" method instead
	public List<Player> getPlayers() {
		return players;
	}

	public void playerAssassinatesOtherPlayer(Player player,
			Player anotherPlayer) {
		if(player.takeActionAssassin()){
			player.takeActionAssassin(); //FIXME this should not be here...
			anotherPlayer.revealACard();
		}
		
	}

	public void playerCoupsOtherPlayer(Player player, Player anotherPlayer) {
		if(player.takeActionCoup()){
			anotherPlayer.revealACard();
		}
	}

	private List<Card> currentAmbassadorCardChoices;
	private ActionChooser actionChooser;

	public List<Card> playerCanChooseFromTheseCardsAsAmbassador(Player player) {
		List<Card> cardChoices = new ArrayList<Card>();
		cardChoices.addAll(player.getCards());
		for(int i = 0; i < 2; i++){
			cardChoices.add(deck.deal());
		}
		currentAmbassadorCardChoices = new ArrayList<Card>(cardChoices);
		return cardChoices;
	}

	public void playerChoosesTheseCardsAsAmbassador(Player player, Card card,
			Card card2) {
		player.replaceFirstCard(card);
		player.replaceSecondCard(card2);
		for(Card choice : currentAmbassadorCardChoices){
			if(!choice.equals(card) && !choice.equals(card2)){
				deck.takeCardBack(choice);
			}
		}
		
	}

	public boolean isPlayerRightAboutOtherPlayerNotHavingCard(int accusingPlayerNumber, int accussedPlayerNumber,
			CardType cardAccussedPlayerClaimedToHave) {
		return !players.get(accussedPlayerNumber).has(cardAccussedPlayerClaimedToHave);
	}

	public void playerAccussesOtherPlayerOfBluffing(int accusingPlayerNumber, int accussedPlayerNumber,
			CardType cardAccussedPlayerClaimedToHave) {
		
		if(!isPlayerRightAboutOtherPlayerNotHavingCard(accusingPlayerNumber, accussedPlayerNumber, cardAccussedPlayerClaimedToHave)){
			players.get(accusingPlayerNumber).revealACard();
			reshuffleCardAndDrawNewCard(accussedPlayerNumber,cardAccussedPlayerClaimedToHave);
		}
		else{
			players.get(accussedPlayerNumber).revealACard();
		}
	}

	private void reshuffleCardAndDrawNewCard(int accussedPlayerNumber,
			CardType cardAccussedPlayerClaimedToHave) {
		if(players.get(accussedPlayerNumber).getFirstCard().getType().equals(cardAccussedPlayerClaimedToHave)){
			Card cardAccusedOfNotHaving = players.get(accussedPlayerNumber).getFirstCard();
			deck.takeCardBack(cardAccusedOfNotHaving);
			players.get(accussedPlayerNumber).replaceFirstCard(deck.deal());
		}else{
			Card cardAccusedOfNotHaving = players.get(accussedPlayerNumber).getSecondCard();
			deck.takeCardBack(cardAccusedOfNotHaving);
			players.get(accussedPlayerNumber).replaceSecondCard(deck.deal());
		}
	}

	public Player getPlayer(int i) {
		return players.get(i);
	}

	public void setActionChooser(ActionChooser actionChooser) {
		this.actionChooser = actionChooser;
	}

	
	private int currentPlayerNum = 0;
	private DefenseChooser defenseChooser;
	public void oneTurn() {
		Player currentPlayer = getPlayer(currentPlayerNum);
		Action chosenAction = actionChooser.chooseAction(currentPlayer);
		if(chosenAction.targetedPlayer() != null){
			//FIXME need to give defense options based on chosen action...
			Defense chosenDefense = this.defenseChooser.chooseDefense(chosenAction.targetedPlayer());
			if(chosenDefense != null){
				chosenDefense.defendAgainstPlayer(currentPlayer);
			}else{
				chosenAction.performAction(currentPlayer);
			}
		}else{
			chosenAction.performAction(currentPlayer);
		}
		currentPlayerNum = (currentPlayerNum + 1) % players.size();
		
	}

	public void setDefenseChooser(DefenseChooser defenseChooser) {
		this.defenseChooser = defenseChooser;
		
	}

}

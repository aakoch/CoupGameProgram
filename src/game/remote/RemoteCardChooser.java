package game.remote;

import game.Card;
import game.CardPair;
import game.Player;
import game.actions.CardChooser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class RemoteCardChooser implements CardChooser {

	private List<Player> allPlayers;
	private List<PrintWriter> outWriters;
	private List<BufferedReader> playerInputs;

	public RemoteCardChooser(List<Player> players, List<PrintWriter> outWriters, List<BufferedReader> playerInputs) {
		this.allPlayers = players;
		this.outWriters = outWriters;
		this.playerInputs = playerInputs;
	}

	@Override
	public CardPair chooseCards(List<Card> cards, Player playerToChoose) {
		String chooseCardsString = Commands.ChooseCards.toString() + "+++";
		for(Card card : cards){
			chooseCardsString += (card.getType()+":");
		}
		int playerIndex = allPlayers.indexOf(playerToChoose);
		outWriters.get(playerIndex).println(chooseCardsString);
		try {
			String[] chosenCardIndices = playerInputs.get(playerIndex).readLine().split(":");
			int firstIndex = Integer.parseInt(chosenCardIndices[0]);
			int secondIndex = Integer.parseInt(chosenCardIndices[1]);
			System.out.println("Changed cards to indices: " + firstIndex + " and " + secondIndex);
			return new CardPair(cards.get(firstIndex),cards.get(secondIndex));
		} catch (IOException e) {
			throw new RuntimeException("Failed to get response from player",e);
		}
	}

	@Override
	public CardPair chooseCards(List<Card> cards, Player playerToChoose,
			Card cardThatMustBeIncluded) {
		String chooseCardsString = Commands.ChooseCards.toString() + "+++" + cardThatMustBeIncluded.getType() + ":";
		for(Card card : cards){
			chooseCardsString += (card.getType()+":");
		}
		chooseCardsString += "FIRST_REQUIRED";
		int playerIndex = allPlayers.indexOf(playerToChoose);
		outWriters.get(playerIndex).println(chooseCardsString);
		try {
			int chosenCardIndex = Integer.parseInt(playerInputs.get(playerIndex).readLine());
			//NOTE:  The card index for the player UI is one more than for the server side
			return new CardPair(cardThatMustBeIncluded,cards.get(chosenCardIndex-1));
		} catch (IOException e) {
			throw new RuntimeException("Failed to get response from player",e);
		}
	}

}

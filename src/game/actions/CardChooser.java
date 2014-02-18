package game.actions;

import game.Card;
import game.CardPair;
import game.Player;

import java.util.List;

public interface CardChooser {

	CardPair chooseCards(List<Card> cards, Player playerToChoose);

	CardPair chooseCards(List<Card> cards, Player playerToChoose, Card cardThatMustBeIncluded);

}

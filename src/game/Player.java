package game;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Player implements Serializable {
	
	private static int playerNumber = 1;
	
	private String name;
	
	private CardPair cardPair = new CardPair(null, null);

	private int money = 2;
	
	public Player(){
		this("Player " + (playerNumber++));
	}
	
	public Player(String name){
		this.name = name;
	}
	
	//FIXME change to return CardPair instead of List<Card>??
	public List<Card> getCards() {
		return Arrays.asList(this.cardPair.getFirstCard(),this.cardPair.getSecondCard());
	}
	
	public void setCards(CardPair pair){
		this.cardPair = pair;
	}

	public void receive(Card card) {
		if(this.cardPair.getFirstCard() == null){
			this.cardPair.setFirstCard(card);
		}else{
			this.cardPair.setSecondCard(card);
		}
	}

	public Card getFirstCard() {
		return cardPair.getFirstCard();
	}

	public Card getSecondCard() {
		return cardPair.getSecondCard();
	}

	public int getCoins() {
		return money;
	}

	public void takeActionIncome() {
		++money;
	}

	public void takeActionForeignAid() {
		money += 2;
	}

	public void takeActionDuke() {
		money += 3;
	}

	public void takeActionCaptain(Player anotherPlayer) {
		int moneyToTransfer = Math.min(anotherPlayer.getCoins(), 2);
		anotherPlayer.money -= moneyToTransfer;
		this.money += moneyToTransfer;
	}
	
	void setMoney(int newMoney){
		this.money = newMoney;
	}

	public boolean takeActionAssassin() {
		if(money < 3){
			return false;
		}
		money -= 3;
		return true;
		
	}

	public boolean takeActionCoup() {
		if(money < 7){
			return false;
		}
		money -= 7;
		return true;
	}

	public void replaceFirstCard(Card card) {
		this.cardPair.setFirstCard(card);
	}

	public void replaceSecondCard(Card card) {
		this.cardPair.setSecondCard(card);
	}

	public boolean has(CardType cardType) {
		return this.cardPair.has(cardType);
	}
	
	//FIXME give player an option...
	public void revealACard() {
		if(getSecondCard().isRevealed()){
			getFirstCard().reveal();
		}
		getSecondCard().reveal();
	}
	
	public boolean eliminated(){
		return getFirstCard().isRevealed() && getSecondCard().isRevealed();
	}
	
	@Override
	public String toString(){
		return name;
	}

	public void setCoins(int coins) {
		this.money = coins;
	}

}

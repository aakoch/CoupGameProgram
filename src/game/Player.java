package game;

import java.util.Arrays;
import java.util.List;

public class Player {

	private Card card;
	private Card secondCard;

	private int money = 2;
	
	public List<Card> getCards() {
		return Arrays.asList(card,secondCard);
	}

	public void receive(Card card) {
		if(this.card == null){
			this.card = card;
		}else{
			this.secondCard = card;
		}
	}

	public Card getFirstCard() {
		return card;
	}

	public Card getSecondCard() {
		return secondCard;
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
		this.card = card;
	}

	public void replaceSecondCard(Card card) {
		this.secondCard = card;
	}

	public boolean has(CardType cardType) {
		return card.getType() == cardType || secondCard.getType() == cardType;
	}
	
	//FIXME give player an option...
	public void revealACard() {
		if(getSecondCard().isRevealed()){
			getFirstCard().reveal();
		}
		getSecondCard().reveal();
	}

}

package game.actions;

import game.CardType;
import game.Player;

public interface BluffCallerOption {

	boolean callBluff(Player playerCallingBluff, Player playerBeingCalled, CardType cardClaimedToHave);

}

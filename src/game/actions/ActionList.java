package game.actions;

import game.Game;
import game.Player;

import java.util.ArrayList;
import java.util.List;

public class ActionList {
	
	private List<Action> allActions = new ArrayList<Action>();
	
	public ActionList(Game game, Player player){
		allActions.add(new IncomeAction());
		allActions.add(new ForeignAidAction(player, game.getPlayers()));
		allActions.add(new CoupAction(null));
		allActions.add(new AmbassadorAction(game, null));
		//FIXME how to have user decide who to target?s
		allActions.add(new AssassinAction(null));
		allActions.add(new CaptainAction(null));
		allActions.add(new DukeAction());
	}
	
	public List<Action> getAllActions(){
		return allActions;
	}

}

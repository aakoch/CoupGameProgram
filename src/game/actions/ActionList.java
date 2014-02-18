package game.actions;

import game.Game;
import game.Player;

import java.util.ArrayList;
import java.util.List;

public class ActionList {
	
	private List<Action> allActions = new ArrayList<Action>();
	
	public ActionList(Game game, Player player, CardChooser cardChooser){
		List<Player> otherPlayers = new ArrayList<Player>(game.getPlayers());
		otherPlayers.remove(player);
		
		allActions.add(new IncomeAction());
		allActions.add(new ForeignAidAction(player, game.getPlayers()));
		
		for(Player otherPlayer : otherPlayers){
			allActions.add(new CoupAction(otherPlayer));
		}
		allActions.add(new AmbassadorAction(game, cardChooser));
		for(Player otherPlayer : otherPlayers){
			allActions.add(new AssassinAction(otherPlayer));
		}
		for(Player otherPlayer : otherPlayers){
			allActions.add(new CaptainAction(otherPlayer));
		}
		allActions.add(new DukeAction());
	}
	
	public List<Action> getAllActions(){
		return allActions;
	}

}

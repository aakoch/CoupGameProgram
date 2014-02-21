package game.remote;

import game.Player;
import game.ui.javafx.CommonKnowledgeUI;

import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

public class CoupApplication extends Application {
	
	public static List<Player> allPlayers;
	public static Player playerForUi;

	public CoupApplication(){
	}

	@Override
	public void start(Stage arg0) throws Exception {
		//FIXME what to do with Stage arg??
		new PlayerUi(playerForUi);
		//FIXME will need for each separate player
		new CommonKnowledgeUI(allPlayers);
	}

}

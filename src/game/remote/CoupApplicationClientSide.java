package game.remote;

import game.Player;
import game.ui.javafx.CommonKnowledgeUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.stage.Stage;

public class CoupApplicationClientSide extends Application {
	
	public static List<Player> allPlayers;
	public static Player playerForUi;
	public static List<String> buttonLabels;
	public static PrintWriter out;
	public static BufferedReader in;
	private static PlayerUi playerUi;
	
	private static ExecutorService waitingTaskProcessor = Executors.newFixedThreadPool(1);

	public CoupApplicationClientSide(){
	}

	@Override
	public void start(Stage arg0) throws Exception {
		playerUi = new PlayerUi(playerForUi,buttonLabels,out,in);
		//FIXME will need to update on each tick
		new CommonKnowledgeUI(allPlayers);
		
		CoupApplicationClientSide.processNextServerMessage(); //Wait for next command
	}
	
	public static void processNextServerMessage()  {
		waitingTaskProcessor.execute(new Runnable(){
			@Override
			public void run() {
				String nextAction;
				try {
					nextAction = in.readLine();
					System.out.println(nextAction);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if(nextAction.equals(Commands.ActionsEnable.toString())){
					playerUi.enableAllActions();
				}else{
					playerUi.disableAllActions();
				}
				
			}
		});
	}

}

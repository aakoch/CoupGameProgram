package game.remote;

import game.Card;
import game.CardType;
import game.Player;
import game.ui.javafx.CommonKnowledgeUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	private static CommonKnowledgeUI commonUi;
	
	private static ExecutorService waitingTaskProcessor = Executors.newFixedThreadPool(1);

	public CoupApplicationClientSide(){
	}

	@Override
	public void start(Stage arg0) throws Exception {
		playerUi = new PlayerUi(playerForUi,buttonLabels,out,in);
		commonUi = new CommonKnowledgeUI(allPlayers);
		
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
				if(nextAction.equals(Commands.ActionsDisable.toString())){
					playerUi.disableAllActions();
				}else if(nextAction.equals(Commands.RevealOnlyUnrevealedCard.toString())){
					playerForUi.revealACard();
					playerUi.updateCardLabels();
					processNextServerMessage();
				}else if(nextAction.equals(Commands.RevealCardChoice.toString())){
					playerUi.forceToReveal();
				}else if(nextAction.equals(Commands.DEFEAT.toString())){
					playerUi.updateToDisplayerDefeat();
				}else if(nextAction.equals(Commands.VICTORY.toString())){
					playerUi.updateToDisplayerVictory();
				}else{
					String[] actionAndDetails = nextAction.split("\\+\\+\\+");
					String action = actionAndDetails[0];
					String details = actionAndDetails[1];
					System.out.println(details);
					if(action.equals(Commands.ActionsEnable.toString())){
						Set<String> buttonsToEnable = new HashSet<String>(Arrays.asList(details.split("\\+\\+")));
						playerUi.enableActions(buttonsToEnable);
					}
					else if(action.equals(Commands.UpdateCoins.toString())){
						String[] newCoinValues = details.split(":");
						for(int i = 0; i < allPlayers.size(); i++){
							allPlayers.get(i).setCoins(Integer.parseInt(newCoinValues[i]));
						}
						playerUi.updateMoneyLabelText();
						commonUi.refresh();
						processNextServerMessage();
					}
					else if(action.equals(Commands.UpdateCards.toString())){
						String[] cardDetailsPerPlayer = details.split("\\+\\+");
						for(int i = 0; i < allPlayers.size(); i++){
							String[] cardDetailsPerCard = cardDetailsPerPlayer[i].split("::");
							String[] firstCardDetails = cardDetailsPerCard[0].split(":");
							String[] secondCardDetails = cardDetailsPerCard[1].split(":");
							
							allPlayers.get(i).replaceFirstCard(buildNewCard(firstCardDetails));
							allPlayers.get(i).replaceSecondCard(buildNewCard(secondCardDetails));
						}
//						playerUi.updateCardLabels(); //This was already updated so shouldn't need to do again
						commonUi.refresh();
						processNextServerMessage();
					}
				}
				
			}

			private Card buildNewCard(String[] cardDetails) {
				Card newCard = new Card(CardType.valueOf(cardDetails[0]));
				if(cardDetails[1].equalsIgnoreCase(Boolean.TRUE.toString())){
					newCard.reveal();
				}
				return newCard;
			}
		});
	}

}

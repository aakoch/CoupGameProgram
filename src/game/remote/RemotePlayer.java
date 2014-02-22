package game.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import game.Player;

public class RemotePlayer extends Player {

	private static final long serialVersionUID = 1L;
	private PrintWriter writeToPlayer;
	private GameControllerServerSide gameController;
	private BufferedReader readFromPlayer;

	public RemotePlayer(String playerName, PrintWriter writeToPlayer, BufferedReader readFromPlayer) {
		super(playerName);
		this.writeToPlayer = writeToPlayer;
		this.readFromPlayer = readFromPlayer;
	}
	
	public void setGameController(GameControllerServerSide gcss){
		this.gameController = gcss;
	}
	
	@Override
	public void revealACard(){
		if(getFirstCard().isRevealed()){
			getSecondCard().reveal();
			writeToPlayer.println(Commands.RevealOnlyUnrevealedCard);
		}else if(getSecondCard().isRevealed()){
			getFirstCard().reveal();
			writeToPlayer.println(Commands.RevealOnlyUnrevealedCard);
		}else{
			writeToPlayer.println(Commands.RevealCardChoice);
			try {
				String doneResponse = readFromPlayer.readLine(); //Wait for 'DONE' response
				if(doneResponse.equals(Responses.FirstCard.toString())){
					getFirstCard().reveal();
				}else if(doneResponse.equals(Responses.SecondCard.toString())){
					getSecondCard().reveal();
				}else{
					throw new RuntimeException("Invalid response: " + doneResponse);
				}
			} catch (IOException e) {
				throw new RuntimeException("No response received from player.  Game terminating.",e);
			}
		}
		gameController.updatePlayerCards();
	}

}

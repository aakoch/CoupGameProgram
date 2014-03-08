package game.remote;

import game.Game;
import game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CoupServer {
	
	public static void main(String[] args){
		
		int numPlayers = Integer.parseInt(args[0]);
		if(numPlayers < 2){
			System.out.println("Not enough players");
			return;
		}
		else if(numPlayers > 8){
			System.out.println("Too many players");
			return;
		}
		
		
        List<Integer> portNumbers = new ArrayList<Integer>();
        int firstPort = 4445;
        for(int i = 0; i < numPlayers; i++){
        	portNumbers.add(firstPort + i);
        }
        
		int initialConnectionPort = 4444;

        try {
        	List<PrintWriter> playerWriters = new ArrayList<PrintWriter>();
        	List<BufferedReader> playerInputs = new ArrayList<BufferedReader>();
        	ServerSocket initialConnectionServerSocket = new ServerSocket(initialConnectionPort);
        	for(int portNumber : portNumbers){
				Socket initialConnectionClientSocket = initialConnectionServerSocket.accept();
				System.out.println("Telling player to connect on port " + portNumber);
				new PrintWriter(initialConnectionClientSocket.getOutputStream(), true).println("PORT:"+portNumber);
				//Above tells client which port to use now
        		ServerSocket serverSocket = new ServerSocket(portNumber);
        		Socket clientSocket = serverSocket.accept();
        		System.out.println("Established connection with player at port " + portNumber);
        		playerWriters.add(new PrintWriter(clientSocket.getOutputStream(), true));
        		playerInputs.add(new BufferedReader(
        				new InputStreamReader(clientSocket.getInputStream())));
        	}
        	System.out.println("Got all expected connections.  Ready to play.");
        	initialConnectionServerSocket.close();
        
        	List<String> playerNames = new ArrayList<String>();
    		for(int i = 0; i < portNumbers.size(); i++){
    			playerWriters.get(i).println("Player Name?");
    		}
    		for(int i = 0; i < portNumbers.size(); i++){
    			playerNames.add(playerInputs.get(i).readLine());
    		}
            	
    		playGame(playerWriters, playerInputs, playerNames);
    		

//            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port or listening for a connection");
            System.out.println(e.getMessage());
        }

	}

	private static void playGame(List<PrintWriter> playerWriters,
			List<BufferedReader> playerInputs, List<String> playerNames)
			throws IOException {
		List<PrintWriter> originalPlayerWriters = new ArrayList<PrintWriter>(playerWriters);
		List<BufferedReader> originalPlayerInputs = new ArrayList<BufferedReader>(playerInputs);
		List<String> originalPlayerNames = new ArrayList<String>(playerNames);
		
		List<Player> players = new ArrayList<Player>();
		for(int i = 0; i < playerNames.size(); i++){
			final String playerName = playerNames.get(i);
			final PrintWriter writeToPlayer = playerWriters.get(i);
			final BufferedReader readFromPlayer = playerInputs.get(i);
			players.add(new RemotePlayer(playerName,writeToPlayer,readFromPlayer));
		}
		Game g = new Game(players);
		g.deal();
		GameControllerServerSide gameController = new GameControllerServerSide(g, playerWriters,playerInputs);
		
		int nextPlayer = gameController.advanceToNextPlayer();
		while(nextPlayer != -1){
			String playerAction = playerInputs.get(nextPlayer).readLine();
			gameController.attemptToPerformAction(nextPlayer,playerAction);
			nextPlayer = gameController.advanceToNextPlayer();
		}
		
		for(int i = 0; i < originalPlayerWriters.size(); i++){
			originalPlayerWriters.get(i).println(Commands.GAME_OVER + "+++" + GameControllerServerSide.gameHistory);
		}
		System.out.println("History: " + GameControllerServerSide.gameHistory);
		
		//After game is over check if everyone wants to play again:
		for(int i = 0; i < originalPlayerInputs.size(); i++){
			String playAgain = originalPlayerInputs.get(i).readLine();
			if(!playAgain.equalsIgnoreCase(Responses.RESTART.toString())){
				throw new RuntimeException("Player responded invalidly about wanting to play again: " + playAgain);
			}
		}
		
		for(int i = 0; i < originalPlayerWriters.size(); i++){
			originalPlayerWriters.get(i).println(Responses.READY.toString());//get everyone ready
		}
		//If everyone agrees, then play again!
		playGame(originalPlayerWriters, originalPlayerInputs, originalPlayerNames);
	}


}

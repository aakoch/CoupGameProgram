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

        try {
        	List<PrintWriter> playerWriters = new ArrayList<PrintWriter>();
        	List<BufferedReader> playerInputs = new ArrayList<BufferedReader>();
        	for(int portNumber : portNumbers){
        		ServerSocket serverSocket = new ServerSocket(portNumber);
        		Socket clientSocket = serverSocket.accept();
        		playerWriters.add(new PrintWriter(clientSocket.getOutputStream(), true));
        		playerInputs.add(new BufferedReader(
        				new InputStreamReader(clientSocket.getInputStream())));
        	}
        
            // Initiate conversation with client
//            out.println("How Many Players?"); //FIXME for now assuming 4 players... change in future

            
//            if ((inputLine = in.readLine()) != null) {
//            	int numPlayers = Integer.parseInt(inputLine);
//            	
        	List<String> playerNames = new ArrayList<String>();
    		for(int i = 0; i < portNumbers.size(); i++){
    			playerWriters.get(i).println("Player Name?");
    		}
    		for(int i = 0; i < portNumbers.size(); i++){
    			playerNames.add(playerInputs.get(i).readLine());
    		}
            	
    		List<Player> players = new ArrayList<Player>();
    		for(int i = 0; i < playerNames.size(); i++){
    			final String playerName = playerNames.get(i);
    			final PrintWriter writeToPlayer = playerWriters.get(i);
    			final BufferedReader readFromPlayer = playerInputs.get(i);
    			players.add(new RemotePlayer(playerName,writeToPlayer,readFromPlayer));
    		}
    		Game g = new Game(players);
    		g.deal();
    		GameControllerServerSide gameController = new GameControllerServerSide(g, playerWriters);
    		
    		while(true){ //TODO until game is done
    			int nextPlayer = gameController.advanceToNextPlayer();
    			String playerAction = playerInputs.get(nextPlayer).readLine();
    			gameController.performAction(nextPlayer,playerAction);
    		}
    		

//            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port or listening for a connection");
            System.out.println(e.getMessage());
        }

	}


}

package game.remote;

import game.Card;
import game.CardType;
import game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;

public class CoupClient {

	public static void main(String[] args){
		String hostName = "hostIP";
		int portNumber = Integer.parseInt(args[0]);
        try {
        	Socket coupSocket = new Socket(hostName, portNumber);
        	PrintWriter out = new PrintWriter(coupSocket.getOutputStream(), true);
        	BufferedReader in = new BufferedReader(
        			new InputStreamReader(coupSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));

            System.out.println(in.readLine());
            
            String fromUser = stdIn.readLine();
            if (fromUser != null) {
                System.out.println("Your Response: " + fromUser);
                out.println(fromUser);
            }
            
            String[] playerData = in.readLine().split(":");
            int numberPlayers = (playerData.length-1)/3;
            List<Player> allPlayers = new ArrayList<Player>();
            for(int i = 0; i < numberPlayers; i++){
            	String playerName = playerData[3*i];
            	Card firstCard = new Card(CardType.valueOf(playerData[3*i+1]));
            	Card secondCard = new Card(CardType.valueOf(playerData[3*i+2]));
            	Player player = new Player(playerName);
            	player.receive(firstCard);
            	player.receive(secondCard);
            	allPlayers.add(player);
            }
            int thisPlayerIndex = Integer.parseInt(playerData[playerData.length - 1]);
            
            CoupApplication.playerForUi = allPlayers.get(thisPlayerIndex);
            CoupApplication.allPlayers = allPlayers;
            Application.launch(CoupApplication.class);
            
//            PlayerUi playerDisplay = new PlayerUi(player);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            throw new RuntimeException(e);
        }
	}
}

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
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;

public class CoupClient {

	public static void main(String[] args){
		if(args.length < 1){
			System.out.println("Improper usage.  Must enter arg: [host IP]");
			System.exit(1);
		}
		String hostName = args[0];
		Socket initialConnectSocket = getSocket(hostName, 4444); //initial connection port number
		int portNum = -1;
		try {
			System.out.println("Waiting for further input from server");
			BufferedReader initialInput = new BufferedReader(
					new InputStreamReader(initialConnectSocket.getInputStream()));
			String readLine = initialInput.readLine();
			System.out.println("Received input from server");
			portNum = Integer.parseInt(readLine.split(":")[1]);
			System.out.println("Server says to connect on port " + portNum);
		} catch (IOException e1) {
			throw new RuntimeException("Could not get connection port from server");
		}
		System.out.println("Attempting to connect on port " + portNum);
		Socket coupSocket = getSocket(hostName, portNum);
        try {
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
            
            startNewGame(out, in);
            Application.launch(CoupApplicationClientSide.class);
       } catch (IOException e) {
        	e.printStackTrace();
        	System.exit(1);
      }
            
            
	}

	private static Socket getSocket(String hostName, int portNumber) {
		Socket coupSocket = null;
        try {
        	coupSocket = new Socket(hostName, portNumber);
        } catch (UnknownHostException e) {
        	System.err.println("Don't know about host " + hostName +". Trying again in two seconds.");
        	waitTwoSeconds();
        	return getSocket(hostName,portNumber);
        } catch (IOException e) {
        	System.err.println("Couldn't get I/O for the connection to " +
        			hostName + ".  Trying again in two seconds.");
        	waitTwoSeconds();
        	return getSocket(hostName,portNumber);
        }
        System.out.println("Got connection to server!");
		return coupSocket;
	}

	private static void waitTwoSeconds() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			System.out.println("Interrupted");
			System.exit(1);
		}
	}

	public static void startNewGame(PrintWriter out, BufferedReader in)
			throws IOException {
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
		
		
		String[] buttonLabels = in.readLine().split("\\+\\+");
		
		CoupApplicationClientSide.playerForUi = allPlayers.get(thisPlayerIndex);
		CoupApplicationClientSide.allPlayers = allPlayers;
		CoupApplicationClientSide.buttonLabels = Arrays.asList(buttonLabels);
		CoupApplicationClientSide.out = out;
		CoupApplicationClientSide.in = in;
		
	}
}

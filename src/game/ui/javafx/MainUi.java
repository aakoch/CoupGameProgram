package game.ui.javafx;

import game.Game;
import game.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainUi extends Application {

    @Override public void start(final Stage stage) {
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("CoupMainUi.fxml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
        
        Scene scene = new Scene(root, 200, 200);
        stage.setTitle("Coup");
        stage.setResizable(false);
        scene.setFill(Color.DIMGRAY);
        stage.setScene(scene);
        //TODO maybe just create this all in code instead of using the fxml file??
        Pane pane = (Pane) ((AnchorPane)root).getChildren().get(0);
        Button startButton = (Button) pane.getChildren().get(2);
        final Slider slider = (Slider) pane.getChildren().get(1);
        startButton.setOnMouseClicked(new EventHandler<Event>(){

			@Override
			public void handle(Event arg0) {
				int numPlayers = (int) slider.getValue();
				List<Player> players = new ArrayList<Player>();
				for(int i = 0; i < numPlayers; i++){
					players.add(new PlayerWithChoices("Player " + (i+1)));
				}
				Game g = new Game(players);
				g.deal();
				GameController gameController = new GameController(g);
				stage.hide();
				gameController.advanceToNextPlayer(); //ie first player
			}

        	
        });
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

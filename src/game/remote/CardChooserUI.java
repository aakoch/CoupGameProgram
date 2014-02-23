package game.remote;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CardChooserUI extends Pane {

	private List<CheckBox> allCheckBoxes;
	private List<Text> allLabels;
	
	public CardChooserUI(Color color,final PrintWriter printToServer, final Pane paneToShowAfterChoosing){
		
        
		allCheckBoxes = new ArrayList<CheckBox>();
		allLabels = new ArrayList<Text>();
		
		Text text = new Text("CHOOSE CARDS TO KEEP (current cards on top row)");
		text.setLayoutX(15);
		text.setLayoutY(20);
		getChildren().add(text);
		
		final Button acceptButton = new Button("Accept");
		getChildren().add(acceptButton);
		acceptButton.setLayoutX(80);
		acceptButton.setLayoutY(60);
		acceptButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event arg0) {
				CardChooserUI.this.setVisible(false);
				paneToShowAfterChoosing.setVisible(true);
				if(allCheckBoxes.get(0).isDisable()){
					for(int i = 1; i < allCheckBoxes.size(); i++){
						if(allCheckBoxes.get(i).isSelected()){
							printToServer.println(Integer.toString(i));
							break;
						}
					}
				}else{
					String responseString = "";
					for(int i = 0; i < allCheckBoxes.size(); i++){
						if(allCheckBoxes.get(i).isSelected()){
							responseString += (i + ":");
						}
					}
					printToServer.println(responseString);
				}
				CoupApplicationClientSide.processNextServerMessage();
			}
			
		});
		
		for(int i = 0; i < 4; i++){
			final CheckBox checkBox = new CheckBox("");
			getChildren().add(checkBox);
			allCheckBoxes.add(checkBox);
			final Text checkBoxLabel = new Text("");
			getChildren().add(checkBoxLabel);
			allLabels.add(checkBoxLabel);
			checkBox.setOnMouseClicked(new EventHandler<Event>(){
				@Override
				public void handle(Event arg0) {
					int selectedCount = 0;
					for(CheckBox localBox : allCheckBoxes){
						if(localBox.isSelected()){
							selectedCount++;
						}
					}
					acceptButton.setDisable(selectedCount != 2);
					
				}
			});
		}
		
		allCheckBoxes.get(0).setLayoutX(0);
		allCheckBoxes.get(0).setLayoutY(20);
		allCheckBoxes.get(0).setSelected(true);
		
		allCheckBoxes.get(1).setLayoutX(100);
		allCheckBoxes.get(1).setLayoutY(20);
		allCheckBoxes.get(1).setSelected(true);
		
		allCheckBoxes.get(2).setLayoutX(0);
		allCheckBoxes.get(2).setLayoutY(40);
		allCheckBoxes.get(2).setSelected(false);
		
		allCheckBoxes.get(3).setLayoutX(100);
		allCheckBoxes.get(3).setLayoutY(40);
		allCheckBoxes.get(3).setSelected(false);
		
		for(int i = 0; i < 4; i++){
			allLabels.get(i).setLayoutX(allCheckBoxes.get(i).getLayoutX() + 20);
			allLabels.get(i).setLayoutY(allCheckBoxes.get(i).getLayoutY() + 10);
		}
	}

	public void updateWithChoices(String cardChoiceDetails) {
		if(cardChoiceDetails.contains("FIRST_REQUIRED")){
			allCheckBoxes.get(0).setDisable(true);
		}
		String[] choices = cardChoiceDetails.split(":");
		for(int i = 0; i < 4; i++){
			allLabels.get(i).setText(choices[i]);
		}
		allCheckBoxes.get(0).setSelected(true);
		allCheckBoxes.get(1).setSelected(true);
		allCheckBoxes.get(2).setSelected(false);
		allCheckBoxes.get(3).setSelected(false);
		
	}
}

package game.ui;

import game.Card;
import game.CardPair;
import game.Player;
import game.actions.CardChooser;

import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CardChooserUI implements CardChooser {
	
	private final IndividualPlayer playerUI;
	private JButton doneButton;
	private Frame frame;

	public CardChooserUI(IndividualPlayer playerUI){
		this.playerUI = playerUI;
	}

	@Override
	public CardPair chooseCards(final List<Card> cards, Player playerToChoose) {
		
		frame = new JFrame("Choose Two Cards To Keep");
		frame.setSize(300, 100);
		
		JPanel cardChooserPanel = new JPanel();
		JComboBox options = new JComboBox();
		for(Card card : cards){
			options.addItem(card);
		}
		
		cardChooserPanel.add(options);
		
		final JComboBox secondOptions = new JComboBox(); 
		for(Card card : cards){
			secondOptions.addItem(card);
		}
		secondOptions.removeItem(cards.get(0));
		
		cardChooserPanel.add(secondOptions);
		
		final CardPair cardPair = new CardPair((Card)options.getSelectedItem(),
				(Card)secondOptions.getSelectedItem());
		
		options.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				cardPair.setFirstCard((Card) itemEvent.getItem());
				for(Card card : cards){
					secondOptions.removeItem(card);
				}
				for(Card card : cards){
					if(!card.equals(cardPair.getFirstCard())){
						secondOptions.addItem(card);
					}
				}
				//TODO add back in previously selected item
			}
		});
		
		secondOptions.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				cardPair.setSecondCard((Card) itemEvent.getItem());
			}
		});
		
		createDoneButton();
		
		cardChooserPanel.add(doneButton);
		
		frame.add(cardChooserPanel);
		
		frame.setVisible(true);
				
		return cardPair; //returned right away but will update
	}

	@Override
	public CardPair chooseCards(List<Card> cards, Player playerToChoose,
			Card cardThatMustBeIncluded) {
		frame = new JFrame("Choose Two Cards To Keep");
		frame.setSize(300, 100);
		
		JPanel cardChooserPanel = new JPanel();
		JComboBox options = new JComboBox();
		options.addItem(cardThatMustBeIncluded);
		cardChooserPanel.add(options);
		
		JComboBox secondOptions = new JComboBox(); 
		for(Card card : cards){
			secondOptions.addItem(card);
		}
		
		cardChooserPanel.add(secondOptions);
		
		final CardPair cardPair = new CardPair((Card)options.getSelectedItem(),
				(Card)secondOptions.getSelectedItem());
		
		secondOptions.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				cardPair.setSecondCard((Card) itemEvent.getItem());
			}
		});
		
		createDoneButton();
		
		cardChooserPanel.add(doneButton);
		
		frame.add(cardChooserPanel);
		
		frame.setVisible(true);
				
		return cardPair; //returned right away but will update
	}

	private void createDoneButton() {
		doneButton = new JButton("Done");
		doneButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event) {
				playerUI.advanceToNextPlayer(); //Now next player can go!
				frame.setVisible(false);
			}
		});
	}
}
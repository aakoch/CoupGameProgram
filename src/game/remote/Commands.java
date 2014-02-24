package game.remote;

public enum Commands {
	ActionsEnable,
	ActionsDisable,
	UpdateCoins,
	UpdateCards,
	RevealCardChoice,
	RevealOnlyUnrevealedCard, 
	ChooseCards,
	CallBluff,
	Block,
	DEFEAT, VICTORY, GAME_OVER //TODO send 'GAME_OVER' with history when someone has won
}

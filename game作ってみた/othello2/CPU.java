import java.util.*;

public class CPU {
	
	int color;
	
	//mctGameStateが渡された場合GameStateに変換
	int[] decide(mctGameState state){
		GameState s = new GameState();
		s.set(state.data,state.turn,state.player);
		
		return decide(s);
	}
	

	
	int[] decide(GameState state){
		int[] pos = new int[2];
		return pos;
	}
}
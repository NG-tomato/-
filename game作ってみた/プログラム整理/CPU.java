import java.util.*;

public class CPU {
	
	int color;
	
	//mctGameState‚ª“n‚³‚ê‚½ê‡GameState‚É•ÏŠ·
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
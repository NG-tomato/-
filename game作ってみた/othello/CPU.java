import java.util.*;

public class CPU {
	
	int color;
	
	//mctGameState���n���ꂽ�ꍇGameState�ɕϊ�
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
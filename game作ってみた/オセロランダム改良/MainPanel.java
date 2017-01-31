//表示させるパネルを作成するクラス



//共通の処理のメソッド(日時機能、国際化、乱数ジェネレータ)を集めたクラス（ユーティリティクラス）
import java.util.*;

/*
パネルクラス
基本的動作を実装
*/
public class MainPanel{
	//縦横のマス
	
	//状態を表すクラスstateを作成
	GameState state = new GameState();
	
	//ランダムで打つAIのクラスRandomCPUを作成
	//black
	h_mcCPU b_cpu = new h_mcCPU(1);
	//RandomCPU b_cpu = new RandomCPU(1);
	
	//white
	//mctCPU w_cpu = new mctCPU(1);
	//hyoukaCPU w_cpu = new hyoukaCPU(-1);
	RandomCPU w_cpu = new RandomCPU(-1);
	
	//勝敗の結果の合計を入れる配列
	int winCount[] = new int[3];
	
	//メインパネルを作成するメソッド
	public MainPanel(int count){
		
		for(int i = 0; i < count; i++){
			/*if(i % 10 == 0){
				System.out.println("Now game count is "+ i);
			}*/
			//textDisplay();
			game();
			state.reset();
		}
		
		System.out.println();
		System.out.println("---Loop END---");
		System.out.println("Black win : " + winCount[0]);
		System.out.println("White win : " + winCount[1]);
		System.out.println("Draw      : " + winCount[2]);

	}
	
	
	//描写を行うメソッド
	public void textDisplay(){
		System.out.println();
		
		//左上から順にマスと駒を表示
		for(int y=1; y<=8; y++){
			for(int x=1; x<=8; x++){
				System.out.print("|");
				switch (state.data[x + y * 10]) {
				case 1:
					System.out.print("○");					//黒の駒を表示
					break;
				case -1:
					System.out.print("●");					//白の駒を表示
					break;
				default:
					System.out.print("  ");
				}
			}
			System.out.println("|");
		}
		System.out.println("TURN = "+state.turn);
		System.out.println("PLAYER = "+state.player);
		System.out.println("DISC = "+state.black+" : " +state.white);
		System.out.println("\n \n");
		
	}
	
	
	public void game(){
		boolean isLastPass = false;
		
		for(;;){
			//textDisplay();

			// 置くところがなければパス
			if( state.checkPass() == true ){
				if (isLastPass) {	//両方パスだと終了
					endGame();
					break;
				}
				
				state.player *= -1;
				isLastPass = true;
				//System.out.println("Pass! Next turn is : "+state.player);
				continue;
			}
			isLastPass = false;
			
			// 以下では必ず置けるものとする．
			if(state.player == b_cpu.color){ 			//Black

				//cpu内のdecideメソッドで置く場所を決定
				int action[] = b_cpu.decide(state);
				if (action[0] == -1 || action[1] == -1) { throw new RuntimeException("Illegal Move by Black: (-1, -1)"); }
				//置ける場所がある場合のみ駒を置く処理をする
				state.put(action[0], action[1]);
				//System.out.println("Black put point is : "+action[0]+" ,"+action[1]);
			}
			else { // White
				//cpu内のdecideメソッドで置く場所を決定
				int action[] = w_cpu.decide(state);
				
				if (action[0] == -1 || action[1] == -1) { throw new RuntimeException("Illegal Move by White: (-1, -1)"); }
				//置ける場所がある場合のみ駒を置く処理をする
				state.put(action[0], action[1]);
				//System.out.println("White put point is : "+action[0]+" ,"+action[1]);
			}
		}
	}
	
	int GameCount = 0;
	public void endGame(){
		//System.out.println("---Game END---");
		int End = state.Win();
		if(End == 1){
		//	Winner = "black";
			winCount[0] ++;
		}else if(End == -1){
		//	Winner = "white";
			winCount[1] ++;
		}else {
		//	Winner = "Drow";
			winCount[2] ++;
		}
		//textDisplay();
		GameCount ++;
		/*
		System.out.println("Game count = " + GameCount);
		System.out.println("Winner = " + End);
		*/
	}
}

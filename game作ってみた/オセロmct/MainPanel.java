//表示させるパネルを作成するクラス



//共通の処理のメソッド(日時機能、国際化、乱数ジェネレータ)を集めたクラス（ユーティリティクラス）
import java.util.*;

/*
パネル作成を行うクラスを継承
implements で受け取るイベントを実装する
implementsの場合、インターフェイスで定義されたメソッドをすべて実装する必要がある(ないとエラーになる)ため、共通の規格として扱える
MouseListener はマウスイベントを受け取るクラス
Observer はあるオブジェクトの変化をそれに依存するオブジェクトに知らせるクラス
*/
public class MainPanel{
	//縦横のマス
	
	//状態を表すクラスstateを作成
	GameState state = new GameState();
	
	//ランダムで打つAIのクラスRandomCPUを作成
	//black
	mcCPU b_cpu = new mcCPU(1);
	//RandomCPU b_cpu = new RandomCPU(1);
	
	//white
	mcCPU w_cpu = new mcCPU(-1);
	//RandomCPU w_cpu = new RandomCPU(-1);
	
	//勝敗の結果の合計を入れる配列
	int winCount[] = new int[3];
	
	//メインパネルを作成するメソッド
	public MainPanel(int count){
		//CPUを選択
		/*
		//ランダムで打つAIのクラスRandomCPUを作成
		//black
		if(B_CPU == 1){
		}
		//ランダムで打つAIのクラスRandomCPUを作成
		//white
		if(W_CPU == 1){
		}
		*/
		
		for(int i = 0; i < count; i++){
			//TextDisplay();
			Game();
			state.reset();
		}
		
		System.out.println();
		System.out.println("---Roop END---");
		System.out.println("Black win : " + winCount[0]);
		System.out.println("White win : " + winCount[1]);
		System.out.println("Drow      : " + winCount[2]);

	}
	
	
	//描写を行うメソッド
	public void TextDisplay(){
				
		
		System.out.println();
		
		//左上から順にマスと駒を表示
		for(int y=1; y<10 + 1; y++){
			for(int x=1; x<10 + 1; x++){
				System.out.print("|");
				if(state.data[x + y * 10] == 1){
					//黒の駒を表示
					System.out.print("○");
				}else if(state.data[x + y * 10] == -1){
					//白の駒を表示
					System.out.print("●");
				}else{
					System.out.print("  ");
				}
			}
			System.out.println("|");
		}
		
		
		System.out.println();
		
		System.out.println("TURN = "+state.turn);
		System.out.println("PLAYER = "+state.player);
		System.out.println("DISC = "+state.black+" : " +state.white);
		System.out.println("\n \n");
		
	}
	
	
	//コンポーネント上でマウスボタンが押されると呼び出されるクラス
	public void Game(){
		
		for(;;){
			//CPUのターン
			//Black
			if(state.player == b_cpu.color){
				//cpu内のdecideメソッドで置く場所を決定
				int b_action[] = b_cpu.decide(state);
				
				//座標が以外で置ける場所がある場合のみ駒を置く処理をする
				if(b_action[0] != -1){
					state.put(b_action[0], b_action[1]);
					//System.out.println("Black put point is : "+b_action[0]+" ,"+b_action[1]);				
				}
				/*盤面が埋まったら終了
				if(state.turn == (10 * 10) - 4){
					TextDisplay();
					EndGame();
				}*/
			}
			else if(state.player == w_cpu.color){
				//cpu内のdecideメソッドで置く場所を決定
				int w_action[] = w_cpu.decide(state);
				
				//置ける場所がある場合のみ駒を置く処理をする
				if(w_action[0] != -1){
					state.put(w_action[0], w_action[1]);
					//System.out.println("White put point is : "+w_action[0]+" ,"+w_action[1]);
				}
				
				/*盤面が埋まったら終了
				if(state.turn == (10 * 10) - 4){
					TextDisplay();
					EndGame();
				}*/
			}
			//TextDisplay();
			//パスチェック
			if( state.checkPass() == true ){
				state.player *= -1;
				//両方パスだと終了
				if(state.checkPass() == true){
					EndGame();
					break;
				}
				//System.out.println("Pass! Next turn is : "+state.player);
			}
		}
	}
	
	
	public void EndGame(){
		//System.out.println("---Game END---");
		int End = state.Win();
		String Winner;
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
		//System.out.println(Winner + " Win !");
	}
}

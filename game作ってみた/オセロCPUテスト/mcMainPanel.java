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
public class mcMainPanel{
	//縦横のマス
	int Squares = 8;
	int data[][];
	int count = 1;
	int turn;
	int player;
<<<<<<< HEAD
=======
	
	int s_data[][];
	int s_turn;
	int s_player;
	
>>>>>>> 00814f12bf8c02deea022ad00e0d192a92b06166
		
	//状態を表すクラスstateを作成
	mcGameState state = new mcGameState(Squares);
	
	//ランダムで打つAIのクラスRandomCPUを作成
	//white
	mcRandomCPU w_cpu = new mcRandomCPU(-1,Squares);
	//black
	mcRandomCPU b_cpu = new mcRandomCPU(1, Squares);
	
	//勝敗の結果の合計を入れる配列
	int winCount[] = new int[3];
	
	//メインパネルを作成するメソッド
	public mcMainPanel(int[][] d,int c, int t, int p){
<<<<<<< HEAD
		data = d;
		count = c;
		turn = t;
		player = p;
=======
		data = new int[Squares][Squares];
		data = Arrays.copyOf(d, d.length);
		count = c;
		turn = t;
		player = p;
		
		s_data = new int[Squares][Squares];
		s_data =Arrays.copyOf(d, d.length);
		s_turn = t;
		s_player = p;;

>>>>>>> 00814f12bf8c02deea022ad00e0d192a92b06166
	}
	
	public void mcGame(int x , int y){
		for(int i = 0; i < count; i++){
<<<<<<< HEAD
			state.set(data, turn, player);
=======
			//state.reset();
			state.set(s_data, s_turn, s_player);
			TextDisplay();
>>>>>>> 00814f12bf8c02deea022ad00e0d192a92b06166
			state.put(x, y);
			//TextDisplay();
			Game();
			//System.out.println("あああ");
		}
	}
	
<<<<<<< HEAD
	/*
=======
	
>>>>>>> 00814f12bf8c02deea022ad00e0d192a92b06166
	//描写を行うメソッド
	public void TextDisplay(){
				
		
		System.out.println();
		
		//左上から順にマスと駒を表示
		for(int y=1; y<Squares + 1; y++){
			for(int x=1; x<Squares + 1; x++){
				System.out.print("|");
				if(state.data[x][y] == 1){
					//黒の駒を表示
					System.out.print("○");
				}else if(state.data[x][y] == -1){
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
<<<<<<< HEAD
	*/
=======
	
>>>>>>> 00814f12bf8c02deea022ad00e0d192a92b06166
	
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
				if(state.turn == (Squares * Squares) - 4){
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
				
<<<<<<< HEAD
				/*盤面が埋まったら終了
				if(state.turn == (Squares * Squares) - 4){
					TextDisplay();
					EndGame();
				}*/
=======
>>>>>>> 00814f12bf8c02deea022ad00e0d192a92b06166
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
<<<<<<< HEAD
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
=======
		//Winner = "black";
			winCount[0] ++;
		}else if(End == -1){
		//Winner = "white";
			winCount[1] ++;
		}else {
		//Winner = "Drow";
			winCount[2] ++;
		}
		//System.out.println(Winner + " Win !");
		//TextDisplay();
>>>>>>> 00814f12bf8c02deea022ad00e0d192a92b06166
	}
	
	public int rePoint(){
		//勝ちが10点，引き分けが5点で評価値を計算
		int point = winCount[0]*10 + winCount[2]*5;
		return point;
	}
}

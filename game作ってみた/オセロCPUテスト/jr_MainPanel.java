
//共通の処理のメソッド(日時機能、国際化、乱数ジェネレータ)を集めたクラス（ユーティリティクラス）
import java.util.*;

public class jr_MainPanel{
	//縦横のマス
	int Squares = 8;
	
	int data[][];
	int count = 0;
	int turn;
	int player;
	int x;
	int y;
	
	int s_player;
	
	//状態を表すクラスstateを作成
	jr_GameState state; 
	
	//ランダムで打つAIのクラスRandomCPUを作成
	//white
	jr_RandomCPU w_cpu = new jr_RandomCPU(-1,Squares);
	//black
	jr_RandomCPU b_cpu = new jr_RandomCPU(1, Squares);
	
	//勝敗の結果の合計を入れる配列
	int winCount[] = new int[3];
	

	//メインパネルを作成するメソッド
	public jr_MainPanel(int[][] d, int t, int p, int[] a){
		data = d;
		turn = t;
		player = p;
		x = a[x];
		y = a[y];
		s_player = player;
		state = new jr_GameState(Squares, data, turn, player, x, y);
		
		for(int i = 0; i < count; i++){
			Game();
			state.reset();
		}
	}
	
	
	public void Game(){
		
		for(;;){
			//CPUのターン
			//Black
			if(state.player == b_cpu.color){
				//cpu内のdecideメソッドで置く場所を決定
				int b_action[] = b_cpu.decide(state);
				
				//置ける場所がある場合のみ駒を置く処理をする
				if(b_action[0] != -1){
					state.put(b_action[0], b_action[1]);
				}
			}
			else if(state.player == w_cpu.color){
				//cpu内のdecideメソッドで置く場所を決定
				int w_action[] = w_cpu.decide(state);
				
				//置ける場所がある場合のみ駒を置く処理をする
				if(w_action[0] != -1){
					state.put(w_action[0], w_action[1]);
				}
				
			}
			//パスチェック
			if( state.checkPass() == true ){
				state.player *= -1;
				//両方パスだと終了
				if(state.checkPass() == true){
					EndGame();
					break;
				}
			}
		}
	}
	
	
	public void EndGame(){
		int End = state.Win();
		if(End == s_player){
			winCount[0] ++;
		}else if(End == s_player*(-1)){
			winCount[2] ++;
		}else {
			winCount[1] ++;
		}
	}
	
	
	public int rePoint(){
		//勝ちが10点，負けが5点で評価値を計算
		int point = winCount[0]*10 + winCount[1]*5;
		return point;
	}
}

//Monte Carlo法のCPUのパッケージ
package mc.mcCPU;

//共通の処理のメソッド(日時機能、国際化、乱数ジェネレータ)を集めたクラス（ユーティリティクラス）
import java.util.*;

public class MainPanel{
	//縦横のマス
	int Squares = 8;
	
	int data[][];
	int count;
	int turn;
	int player;
	int x;
	int y;
	
	//状態を表すクラスstateを作成
	GameState state; 
	
	//ランダムで打つAIのクラスRandomCPUを作成
	//white
	RandomCPU w_cpu = new RandomCPU(-1,Squares);
	//black
	RandomCPU b_cpu = new RandomCPU(1, Squares);
	
	//勝敗の結果の合計を入れる配列
	int winCount[] = new int[3];
	

	//メインパネルを作成するメソッド
	public MainPanel(int[][] d, int t, int p, int[] a){
		data = d;
		turn = t;
		player = p;
		x = a[x];
		y = a[y];
		state = new GameState(Squares, data, turn, player, x, y);
		
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
				
				//座標が以外で置ける場所がある場合のみ駒を置く処理をする
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
		String Winner;
		if(End == 1){
			winCount[0] ++;
		}else if(End == -1){
			winCount[1] ++;
		}else {
			winCount[2] ++;
		}
	}
	
	
	public int rePoint(){
		//勝ちが10点，負けが5点で評価値を計算
		int point = winCount[0]*10 + winCount[1]*5;
		return point;
	}
}

//パネルを作成するクラス


//共通の処理のメソッド(日時機能、国際化、乱数ジェネレータ)を集めたクラス（ユーティリティクラス）
import java.util.*;


//mct用のメインパネルクラス
public class mctMainPanel{
	//パネルのサイズ
	int size = 10;
	
	//状態を表すクラスstateを作成
	mctGameState state = new mctGameState();
	
	//ランダムで打つAIのクラスRandomCPUを作成
	//black
	mct_RandomCPU b_cpu = new mct_RandomCPU(1);
	//white
	mct_RandomCPU w_cpu = new mct_RandomCPU(-1);
	
	//勝敗の結果の合計を入れる配列
	int winCount[] = new int[3];
	
	//stateの情報をリセットするときに使うデータを保存する変数
	//盤面のデータ
	int s_data[] = new int[size * size];
	//ターン
	int turn;
	int player;
	
	//プレイアウトの回数をカウントする変数
	int game_count;
	
	
	//メインパネルを作成するメソッド
	public mctMainPanel(int[] d, int t, int p){
		//リセットを行うときに使用するためのデータを保存する
		s_data = Arrays.copyOf(d ,d.length);
		turn = t;
		player = p;
		
		//現在の状態のゾブリストハッシュの値を作る
		state.zob.makeZob(s_data, player);
	}
	
	//mct用のプレイアウトを行うメソッド
	//1回プレイアウトを行うごとにそのプレイアウトの結果を返す
	public int mctGame(){
		Game();
		return state.Win();
	}
	
	
	//プレイアウトをゲーム終了（ゲーム終了）まで行うクラス
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
					break;
				}
			}
		}
	}
	
	//(x,y)に石を打ったときのゾブリストの値を返す変数
	//打てない時は0を返す
	public int reverseZob(int x,int y){
		int zobrist = 0;
		//打てるとき
		if(state.put(x, y)==true){
			zobrist = state.zob.zobrist;
			state.set(s_data, turn, player);
		}
		return zobrist;
	}
}

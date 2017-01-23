import java.util.*;

public class zobrist {
	//ある場所に駒が置かれた状況ごとの変数を入れる配列
	int[] black = new int[8 * 8];
	int[] white = new int[8 * 8];
	
	//現時点のゾブリスト値
	private int zobrist_now = 0;
	
	//手を決めるときに変更するゾブリストの値
	int zobrist = 0;
	
	//ある場所に石が置かれた状況ごとの変数を作成
	public zobrist(){
		Random rnd = new Random();
		for(int i = 0;i < 8 * 8;i++){
			black[i] = rnd.nextInt();
			white[i] = rnd.nextInt();
		}
	}
	
	//その時点でのゾブリストハッシュの値を作る関数
	//現状態の盤面で石が存在している場所ごとにその値の排他的論理和をとる
	public void makeZob(int[] state, int c){
		for(int i = 0;i < 8;i++){
			for(int j = 0;j < 8;j++){
				//石が存在している場合その石がどちらの石か判別して排他的論理和をとる(どちらでもない場合は何もしない)
				//壁の部分を検査しないようにstateの座標(x,y)には+1する
				//黒の場合
				if(state[(i+1) + (j+1) * 10] == 1){
					zobrist = zobrist ^ black[i + j * 8];
				}
				//白の場合
				else if(state[(i+1) + (j+1) * 10] == -1){
					zobrist = zobrist ^ white[i + j * 8];
				}
			}
		}
		//プレイヤが白の場合、ビット反転をする
		if(c == -1){
			zobrist = ~ zobrist;
		}
		//リセットなどを行うために、現時点のゾブリストハッシュの値として保存する
		zobrist_now = zobrist;
	}
	
	//置かれた際の1つの石の状態が変化したことを表す変数
	//石を置く動作と一緒に繰り返させる
	public void put(int x, int y, int c){
		//白が黒に変わったとき、その場所の白の値の排他的論理和を取ることで値を戻し、黒の値の排他的論理和をとる
		if(c == 1){
			zobrist = zobrist ^ white[x + y * 8];
			zobrist = zobrist ^ black[x + y * 8];
		}
		//黒が白に変わったとき、その場所の黒の値の排他的論理和をとることで値を戻し、白の値の排他的論理和をとる
		else if(c == -1){
			zobrist = zobrist ^ black[x + y * 8];
			zobrist = zobrist ^ white[x + y * 8];
		}
	}
	//駒を置く動作を行った後に手順の情報を入れるメソッド
	//石を置き終わった後に行う
	public void color(){
		//ゾブリストハッシュの値を反転させる
			zobrist = ~ zobrist;
	}
	
	
	//現時点でのゾブリストの値に戻す
	public void reset(){
		zobrist = zobrist_now;
	}
	
}
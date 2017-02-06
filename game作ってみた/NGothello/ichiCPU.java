import java.util.*;

public class ichiCPU extends CPU {
	
	//自分が置くターンを判別する関数
	int color;	//BLACK or WHITE
	int size = 10;
	
	private int[] valueMap = {  45, -11,   4,  -1,  -1,   4, -11,  45, 
								11, -16,  -1,  -3,  -3,  -1, -16, -11,
								 4,  -1,   2,  -1,  -1,   2,  -1,   4,
		                        -1,  -3,  -1,   0,   0,  -1,  -3,  -1,
		                        -1,  -3,  -1,   0,   0,  -1,  -3,  -1,
		                         4,  -1,   2,  -1,  -1,   2,  -1,   4,
		                       -11, -16,  -1,  -3,  -3,  -1, -16, -11,
		                        45, -11,   4,  -1,  -1,   4, -11,  45	}; 

	//ランダムクラスのインスタンス化
	Random rnd = new Random();
	
	public ichiCPU(int c){
		color = c;
	}
	
	int[] decide(GameState state){
		
		//置ける場所を記憶するリスト
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//盤面の空マスを置けるかチェック
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
				//すでに駒があるときはパス
				if(state.data[x + y * 10] != 0)
					continue;
				
				//置けるマスのとき、候補として記憶
				if(state.canReverse(x, y) == true){
					
					//[x,y]の2つの要素を持つ配列として記憶する
					int pos[] = {x,y,0};
					
					//[x,y]の配列を置ける場所を記憶するリストに追加する
					array.add(pos);
				}
				
			}
		}
		
		//置ける場所がない場合は座標が{-1,-1}として返す
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return pos;
		}
		//置ける場所が1つの場合はその手を返す
		else if(array.size() == 1){
			int pos[] = array.get(0);
			int data[] = {pos[0] , pos[1]};
			return data;
		}
		//探索の必要がある場合は評価を行って点数をつける
		for(int i = 0;i<array.size();i++){
			int[] pos = array.get(i);
			hyoukaPoint(pos, state);
			//System.out.println(pos[2]);
			//現在探索しているところまでで入れ替えを行う
			//点数が高いほど前、低いほど後ろに配置される
			//挿入ソート
			for(int j = 0;j < i;j++){
				int[] serch_pos = array.get(j);
				if(serch_pos[2] < pos[2]){
					int[] copyArray = Arrays.copyOf(pos ,3);
					array.remove(i);
					array.add(j,copyArray);
					break;
				}
			}
		}
		
		//System.out.println("Print of put point");
		for(int i=0;i<array.size();i++){
			int[] pos = array.get(i);
			/*
			System.out.println("x = " + pos[0] + ": y = " + pos[1]);
			System.out.println("point = "+ pos[2]);
			*/
		}
		
		//置く場所を返す
		return array.get(0);
	}
	
	
	//評価した点数を返すメソッド
	public void hyoukaPoint(int[] pos, GameState state){
		int player = state.player;
		//stateを新しく作り，その点数を返す
		GameState s = new GameState();
		s.set(state.data, state.turn, state.player);
		//手を打った状態へ遷移
		s.put(pos[0] ,pos[1]);
		//位置による評価
		int bp = banPoint(s);
		pos[2] = bp;
		//System.out.println(pos[2]);
	}
	
	//位置による評価を行うメソッド
	public int banPoint(GameState state){
		//手をうつ前のプレイヤーのための評価なので-1してプレイヤーを打つ前にもどす
		int player = state.player * -1;
		int sum = 0;
		
		int[] data = state.data;
		//位置ごとに計算を行い加算していく
		for(int i = 1;i <= 8; i++){
			for(int j = 1; j <= 8; j++){
				if(data[i + j * 10] != 0){
					sum += (data[i + j*10] * valueMap[(i-1) + (j-1) * 8]) + (int) Math.floor(Math.random() * 3);
				}
			}
		}
		//白がプレイヤーのとき数値を反転させる
		return sum * player;
	}


	
}
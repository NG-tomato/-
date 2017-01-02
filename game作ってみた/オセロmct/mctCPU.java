import java.util.*;

public class mctCPU {
	
	//自分が置くターンを判別する関数
	int color;	//BLACK or WHITE
	int size = 10;
	
	//1手読むごとの総プレイアウト数
	int count = 10;
	
	//プレイアウトの総計
	int total_count = 0;
	
	//下の探索に移るときのしきい値
	int threshold = 5;
	
	//mapに入れるデータの配列
	//{プレイアウト数,勝数}
	int[] data = new int[3];
	
	//データを入れる配列
	//
	Map<Integer, ArrayList<Integer>> map = new HashMap<>();
	
	
	public mctCPU(int c){
		color = c;
	}
	
	int[] decide(GameState state){
		
		
		mctMainPanel p = new mctMainPanel(count, state.data , state.turn, state.player);
		
		
		//置ける場所を記憶するリスト
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//盤面の空マスを置けるかチェック
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
				//すでに駒があるときはパス
				if(p.state.data[x + y * size] != 0)
					continue;
				
				//置けるマスのとき、候補として記憶
				int zobrist = p.reverseZob(x, y);
				if(zobrist != 0){
					
					//[x,y]の2つの要素を持つ配列として記憶する
					int pos[] = {x,y,zobrist};
					
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
		
		
				
		for(int i=0; i < array.size(); i++){
			int a[] = array.get(i);
			p.mcGame(a);
		}
		
		//ポイントが最大の手を求める
		int j = 0;
		double point = 0;
		for(int i = 0; i < array.size(); i++){
			int a[] = array.get(i);
			ArrayList<Integer> b = map.get(a[2]);
			//UCB1値の計算
			double P = UCB1(b.get(2));
			if(P > point){
				point = P;
				j = i;
			}
			total_count += count;
		}
		
		return array.get(j);
		
	}
	
	
	//手を選びながら探索していくメソッド
	public int select(mctMainPanel p){
		//置ける場所を記憶するリスト
		ArrayList<int[]> array = new ArrayList<int[]>(3);
		
		//プレイアウトの勝者を示す変数
		int winner = 0;
		
		//この選択のプレイヤをputする前に保存する変数
		int player = p.state.player;

		
		//盤面の空マスを置けるかチェック
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
				//すでに駒があるときはパス
				if(p.state.data[x + y * size] != 0)
					continue;
				
				//置けるマスのとき、候補として記憶
				//それに加えてゾブリストハッシュの値を記憶
				int zobrist = p.reverseZob(x, y);
				if(zobrist != 0){
					
					//[x,y]の2つの要素を持つ配列として記憶する
					int pos[] = {x,y,zobrist};
					
					//[x,y]の配列を置ける場所を記憶するリストに追加する
					array.add(pos);
				}
				
			}
		}
		
		//置ける場所がない場合は座標が{-1,-1}として返す
		//-----------------------このときの処理についてあとで考える必要あり
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return 0;
		}

		
		
		//UCB1値の高いものを選ぶ
		int select = 0;
		int[] select_point = new int[3];
		int[] search_point = new int[3];
		double select_UCB1 = 0;
		double search_UCB1 = 0;
		for(int i = 0; i < array.size(); i++){
			search_point = Arrays.copyOf(array.get(i), 3);
			//プレイアウト数が0の場合その手を選択する（このときUCB1値が無限になってしまうので先に除外する）
			//プレイアウト数0の手はまだmap上に作られていないので、作る
			if(map.containsKey(search_point[2])){
				select_point = search_point;
				ArrayList<Integer> put_data = new ArrayList<Integer>(3);
				map.put(search_point[2], put_data);
				break;
			}
			//UCB1値を計算
			search_UCB1 = UCB1(search_point[2]);
			//UCB1値が大きい方をselectの方にする
			if(select_UCB1 < search_UCB1){
				select_point = Arrays.copyOf(search_point, 3);
				select_UCB1 = search_UCB1;
			}
		}
		ArrayList<Integer> data = new ArrayList<Integer>(3);
		data = map.get(search_point[2]);
		
		//選んだ手のプレイアウト数が閾値以上の場合、select関数再帰的に呼び出す
		//それ以外の時はプレイアウトをする
		if(data.get(0) >= threshold){
			//閾値超え
			p.state.put(select_point[0], select_point[2]);
			winner = select(p);
		}else{
			//-------------------プレイアウトをする
			winner = 0;
		}
		
		//プレイアウト数を加算する
		data.set(0, data.get(0) + 1);
		
		//プレイヤーが勝っている場合ポイントを加算
		if(winner == player){
			data.set(1, data.get(1) + 1);
		}
		return winner;

	}
	
	
	public double UCB1(int zob){
		ArrayList<Integer> data = new ArrayList<Integer>(3);
		data = map.get(zob);
		double UCB1 = data.get(2) + Math.sqrt(2 * total_count / Math.log(data.get(0)) );
		return UCB1;
	}
}
import java.util.*;

public class mctCPU {
	
	//自分が置くターンを判別する関数
	int color;	//BLACK or WHITE
	int size = 10;
	
	//1手読むごとの総プレイアウト数
	int count = 100;
	
	//プレイアウトの総計
	int total_count = 0;
	
	//下の探索に移るときのしきい値
	int threshold = 10;
	
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
		
		//select関数を用いてプレイアウトしていく
		for(int i=0; i < count; i++){
			//int a[] = array.get(i);
			//p.mcGame(a);
			int x = select(p);
			//1回のプレイアウトごとにtotal_count変数を加算していく
			total_count ++;
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
		int winner = 2;
		
		//この選択のプレイヤをputする前に保存する変数
		int player = p.state.player;

		
		//盤面の空マスを置けるかチェック
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
				//すでに駒があるときはパス
				if(p.state.data[x + y * size] != 0)
					continue;
				
				//その手のゾブリストハッシュの値を取得
				int zobrist = p.reverseZob(x, y);
				if(map.containsKey(zobrist)){
					//すべて探索済みで評価しても意味がない場所の場合もパス
					ArrayList<Integer> data = map.get(zobrist);
					if(data.size()==3){
						continue;
					}
				}
				
				//置けるマスのとき、候補として記憶
				if(zobrist != 0){
					
					//[x,y]の2つの要素を持つ配列として記憶する
					int pos[] = {x,y,zobrist};
					
					//[x,y]の配列を置ける場所を記憶するリストに追加する
					array.add(pos);
				}
				
			}
		}
		
		//置ける場所がない場合
		//map内のリストに値を追加し、配列の大きさを変化させることで、探索が必要ない部分という判断ができるようにする
		if(array.size() <= 0){
			ArrayList<Integer> data = map.get(p.state.zob.zobrist);
			data.add(1);
			return 2;
		}
		
		int select = selectUCB(array);
		int[] select_point = array.get(select);
		
		ArrayList<Integer> data = new ArrayList<Integer>(3);
		data = map.get(select_point[2]);
		
		
		//select関数が呼び出されたかどうかを保存する変数
		boolean do_select = false;
		while(winner == 2){
			winner = 0;
			//選んだ手のプレイアウト数が閾値以上の場合、select関数を再帰的に呼び出す
			if(data.get(0) >= threshold){
				//選んだ手の状態に移動
				p.state.put(select_point[0], select_point[1]);
				winner = select(p);
				do_select = true;
			}
			//選んだ手が探索しても意味がない手の場合
			if(winner == 2){
				array.remove(select);
				if(array.size() == 0){
					return 2;
				}
				selectUCB(array);
			}
		}
		
		//select関数が呼び出されていないときプレイアウトをする
		if(do_select == false){
			//-------------------プレイアウトをする
			winner = p.mctGame();
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
		double UCB1 = data.get(1) + Math.sqrt(2 * total_count / Math.log(data.get(0)) );
		return UCB1;
	}
	
	public int selectUCB(ArrayList<int[]> array){
		//UCB1値の高いものを選ぶ
		int select = 0;
		int[] select_point = new int[3];
		int[] search_point = new int[3];
		double select_UCB1 = 0;
		double search_UCB1 = 0;
		int i;
		for(i = 0; i < array.size(); i++){
			search_point = Arrays.copyOf(array.get(i), 3);
			//プレイアウト数が0の場合その手を選択する（このときUCB1値が無限になってしまうので先に除外する）
			//プレイアウト数0の手はまだmap上に作られていないので、作る
			if(map.containsKey(search_point[2]) == false){
				select_point = search_point;
				ArrayList<Integer> put_data = new ArrayList<Integer>(2);
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
		return i;
	}
}
import java.util.*;

public class mctCPU {
	
	//自分が置くターンを判別する関数
	int color;
	
	//盤の大きさの変数（壁含む）
	int size = 10;
	
	//1手読むごとの総プレイアウト数
	int count = 100;
	
	//プレイアウトを行った回数を保存する変数
	int total_count = 0;
	
	//探索を深くするときのしきい値
	int threshold = 20;
	
	//mapに入れるデータの配列
	//{プレイアウト数,勝数}
	int[] data = new int[3];
	
	//データを入れるmap
	//プレイアウト数、ポイント（その局面のプレイヤの勝数）、UCB1値
	Map<Integer, double[]> map = new HashMap<>();
	
	//クラスを作成する際に、どっちのプレイヤか選択
	public mctCPU(int c){
		color = c;
	}
	
	//手を選ぶメソッド
	//これを実行することで手を選択
	int[] decide(GameState state){
		
		
		mctMainPanel p = new mctMainPanel(state.data , state.turn, state.player);
		
		//置ける場所を記憶するリスト
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//盤面の空マスを置けるかチェック
		for(int y=1; y<size-1; y++){
			for(int x=1; x<size-1; x++){
				
				//すでに駒があるときはパス
				if(p.state.data[x + y * size] != 0)
					continue;
				
				//置けるマスのとき、候補として記憶
				//mctMainPanelクラスのreverseZob(x,y)関数は，(x,y)に置けるとき置いたときのゾブリストハッシュの値を返す
				int zobrist = p.reverseZob(x, y);
				if(zobrist != 0){
					
					//[x,y]の2つの要素を持つ配列として記憶する
					int pos[] = {x,y,zobrist};
					
					//[x,y]の配列を置ける場所を記憶するリストに追加する
					array.add(pos);
				}
				
			}
		}
		
		
		//置ける場所がない場合
		//追記必要（パスの動作で探索を行うようにする）<-----------------------
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return pos;
		}
		
		//select関数を用いてプレイアウトしていく
		for(int i=0; i < count; i++){
			
			//1回のプレイアウトごとにtotal_count変数を加算していくことでここまでのプレイアウトの総計を求める
			total_count ++;

			//selsect関数はUCB1値が高いものを選んでプレイアウトし、結果をmap関数に適応する変数
			//配列プレイアウト数が一定以上の場合、プレイアウトは行わず、下に再帰的にメソッドを作ることで探索を深めていく
			int x = select(p);
			
		}
		
		
		//ポイントが最大の手を求める
		int i = selectUCB(array);
		
		//選んだ手のデータを表示
		/*
		int[] select_data = array.get(i);
		double[] playout = map.get(select_data[2]); 
		System.out.println("選んだ手のプレイアウト数 : " + playout[0]);
		System.out.println("選んだ手の勝数 : " + playout[1]);
		System.out.println("選んだ手のUCB1 : " + playout[2]);
		*/
		
		//手の(x,y)座標
		return Arrays.copyOf(array.get(i), 2);
		
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
				//reverseZob(x,y)関数は，(x,y)に置ける場合は置いたときのゾブリストハッシュの値、置けない場合は0を返す
				int zobrist = p.reverseZob(x, y, Arrays.copyOf(p.state.data, p.state.data.length), p.turn, p.player);
				
				
				/*
				//そのゾブリストの値のmapが存在している場合
				if(map.containsKey(zobrist)){
					//そのゾブリストの値のデータを取得する
					int[] data = map.get(zobrist);
				}
				*/
				
				//置けるマスのとき(zobrist = 0)、候補として記憶
				if(zobrist != 0){
					
					//[x,y,zobrist]の3つの要素を持つ配列として記憶する
					int pos[] = {x,y,zobrist};
					
					//[x,y,zobrist]の配列を置ける場所を記憶するリストに追加する
					array.add(pos);
				}
				
			}
		}
		
		
		
		//UCB1値が高いものを探すメソッド(selectUCB)を行う
		int select = selectUCB(array);
		
		//選んだ手の情報を取得する配列
		int[] select_point = new int[3];
		//選んだ手の局面の情報を取得する配列
		double[] data = new double[3];
		
		//selectが-1のとき選ぶ手が存在しないとき(パスのとき)
		if(select == -1){
			int passZob = p.passZob(Arrays.copyOf(p.state.data, p.state.data.length), p.turn, p.player);
			//passZobが空のとき保存場所をmapに追加するメソッド
			pass(passZob);
		}else
		//selectが-1でない(選ぶ手が存在する)とき
		{
			select_point = array.get(select);
		}

		data = map.get(select_point[2]);
		
		//選んだ手のプレイアウト数が閾値以上の場合、select関数を再帰的に呼び出す
		if(data[0] >= threshold){
			//選んだ手の状態に移動
			//パスのとき
			if(array.size() == 0){
				p.state.pass();
			}
			//パスでないとき
			else{
				p.state.put(select_point[0], select_point[1]);
			}
			winner = select(p);
		}
		//select関数が呼び出されていないときプレイアウトをする
		else{
			//-------------------プレイアウトをする
			winner = p.mctGame();
		}
		
		//プレイアウト数を加算する
		data[0] ++;
		
		//プレイヤーが勝っている場合ポイントを加算
		if(winner == player){
			data[1] ++;
		}
		
		//UCB1メソッドでdata内のUCB1値を更新
		UCB1(select_point[2]);
		
		
		return winner;

	}
	
	
	//UCB1値の計算をするメソッド
	public void UCB1(int zob){
		//データを取得する配列
		double[] data = map.get(zob);
		//UCB1を計算
		double UCB1 = data[1] + Math.sqrt(2 * total_count / Math.log(data[0]) );
		//UCB1を変更
		data[2] = UCB1;
	}
	
	
	//UCB値はmapに追加するように切り替える予定なので書き換えが必要
	public int selectUCB(ArrayList<int[]> array){
		
		//選んだ手の情報を保存する配列
		int select = -1;
				
		//mapから比較対象のデータを取得する配列
		double[] select_data = new double[3];
		
		//打てる手が保存されているarray配列全体で探索を行う
		for(int i = 0; i < array.size(); i++){
			//現在の比較対象の手を取得する配列
			int[] search_point = Arrays.copyOf(array.get(i), 3);
			
			//プレイアウト数が0の場合その手を選択する（このときUCB1値が無限になってしまうので先に除外する）
			//プレイアウト数0の手はまだmap上に作られていないので存在しているかどうかで判断する
			if(map.containsKey(search_point[2]) == false){
				//mapに入れるための空の配列をつくる
				double[] put_data = new double[3];
				//マップに空の配列を追加する
				map.put(search_point[2], put_data);
				//現在の手を選んだ手にする
				select = i;
				break;
			}
			//mapのデータを取得
			double[] search_data = map.get(search_point[2]);
			//UCB1値が大きい方をselectの方にする
			if(select_data[2] < search_data[2]){
				select_data = Arrays.copyOf(search_data, 3);
				//現在の手を選んだ手にする
				select = i;
			}
		}
		//選んだ手の配列上の番号を返す
		return select;
	}
	
	public void pass(int passZob){
		//プレイアウト数0の場合はまだmap上に作られていないので存在しているかどうかで判断する
		if(map.containsKey(passZob) == false){
			//mapに入れるための空の配列をつくる
			double[] put_data = new double[3];
			//マップに空の配列を追加する
			map.put(passZob, put_data);
		}
	}

}
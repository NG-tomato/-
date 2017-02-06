//モンテカルロ木探索でプレイアウトに評価値を使うかつカットを行う手法
import java.util.*;

public class hc_mctCPU extends CPU {
	
	//自分が置くターンを判別する関数
	int color;
	
	//盤の大きさの変数（壁含む）
	int size = 10;
	
	//1手読むごとの総プレイアウト数
	int count = 100;
	
	//1手読むごとの時間(msミリ秒なので，1秒=1000ms)
	long time = 300;
	
	//プレイアウトを行った回数を保存する変数
	int total_count = 0;
	
	//探索を深くするときのしきい値
	int threshold = 1;
	
	//mapに入れるデータの配列
	//{プレイアウト数,勝数，評価値}
	int[] data = new int[3];
	
	//データを入れるmap
	//プレイアウト数、ポイント（その局面のプレイヤの勝数）、UCB1値
	Map<Integer, int[]> map = new HashMap<>();
	
	//枝刈りするときに使用する評価関数を作成
	hyoukaCPU hyouka = new hyoukaCPU(1);
	
	//枝刈りするときの点数の差の最大値
	int cut = 100;
	
	//クラスを作成する際に、どっちのプレイヤか選択
	public hc_mctCPU(int c){
		color = c;
	}
		
	//手を選ぶメソッド
	//これを実行することで手を選択
	int[] decide(GameState state){
		mctMainPanel p = new mctMainPanel(state.data , state.turn, state.player);
		
		//置ける場所をリストに取得
		//putPointメソッドはmctMainPanelの値を与えることで置ける場所のデータを入れた変数を返す
		ArrayList<int[]> array = putPoint(p.state);
		
		//select関数を用いてプレイアウトしていく
		//閾値を選択
		//プレイアウトが閾値
		for(int i=0; i < count; i++){
		//時間が閾値
		//long start = System.currentTimeMillis();
		//for(long i = start; (i - start) <= time;i = System.currentTimeMillis()){
		//}コメントアウトしてる分の括弧閉じがないとずれるので
			//1回のプレイアウトごとにtotal_count変数を加算していくことでここまでのプレイアウトの総計を求める
			total_count ++;
						
			//selsect関数はUCB1値が高いものを選んでプレイアウトし、結果をmap関数に適応する変数
			//配列プレイアウト数が一定以上の場合、プレイアウトは行わず、下に再帰的にメソッドを作ることで探索を深めていく
			int x = select(p.state.clone());
			
		}

		//ポイントが最大の手を求める
		int i = selectUCB(array, state);
		/*
		//System.out.println("After Playouts");
		for (int[] pos : array) {
			int[] playoutResult = map.get(pos[2]);
			System.out.printf("Legal move: (%d, %d) @%d ==> %f (%d / %d)\n",
												pos[0], pos[1], pos[2],
												ucb1(playoutResult[0], playoutResult[1]),
												playoutResult[1], playoutResult[0]);
		}
		*/
		/*
		//選んだ手のデータを表示
		int[] select_data = array.get(i);
		int[] playout = map.get(select_data[2]); 
		System.out.println("選んだ手のデータを表示");
		System.out.println("手の配列上の位置 : " + i);
		System.out.println("ゾブリストハッシュの値 : " + select_data[2]);
		System.out.println("プレイアウト数 : " + playout[0]);
		System.out.println("勝数 : " + playout[1]);
		System.out.println("UCB1 : " + ucb1(playout[0], playout[1]));
		System.out.println("総プレイアウト数 : " + total_count);
		*/
		
		//手の(x,y)座標
		return Arrays.copyOf(array.get(i), 2);
		
	}
	
	
	//手を選びながら探索していくメソッド
	public int select(mctGameState state) {
		
		//System.out.println("select is called");

		//置ける場所を記憶するリスト
		ArrayList<int[]> array = putPoint(state);
		
		/*
		state.textDisplay();
		for (int[] pos : array) {
		System.out.printf("can put at (%d, %d)\n", pos[0], pos[1]);
		}
		*/
		
		if (array.size() == 0) {
			// 置けなければパスをする (しかない)
			if (state.isLastPass) {
				// 相手もパスをしていた場合は ここで結果を返す
				if (state.black > state.white) return 1;
				if (state.white > state.black) return -1;
				return 0;
			}
			// パスをして1手進めたところでプレイアウトしてもらう
			state.pass();
			return select(state);
		}
		int orgplayer = state.player;
		
		//UCB1値が高いものを探すメソッド(selectUCB)を行う
		int select = selectUCB(array, state);
		//選んだ手の情報を取得する配列
		int[] select_point = array.get(select);

		//選んだ手の局面の情報を取得する配列
		int[] data = map.get(select_point[2]);

		// 1手進めた後で．．．
		state.put(select_point[0], select_point[1]);
		//選んだ手のプレイアウト数が閾値以上の場合、select関数を再帰的に呼び出す
		int winner = (data[0] >= threshold) ? select(state) : playout(state);
		data[0] ++;
		if (winner == orgplayer){
			//System.out.println("winner=player");
			data[1]++;
			//if(orgplayer == 1 && winner == 1) System.out.println("test");
		//}else {
			//System.out.println("winner!=player");
		}
		//System.out.println("select is End");
		//System.out.println("player : " + orgplayer);
		return winner;
	}

	// 評価値を用いてプレイ
	int playout(mctGameState state) {
		while (true) {
			ArrayList<int[]> array = putPoint(state);

			// System.out.println("IN PLAYOUT:");
			// state.textDisplay();
			// System.out.println("array.size() = " + array.size());

			if (array.size() == 0) {
				// 置けなければパスをする (しかない)
				if (state.isLastPass) {
					// 相手もパスをしていた場合は ここで結果を返す
					if (state.black > state.white) return 1;
					if (state.white > state.black) return -1;
					return 0;
				}
				// パスをして1手進めたところでプレイアウトしてもらう
				state.pass();
			} else {
				hyoukaCPU CPU = new hyoukaCPU(1);
				int[] selected = CPU.decide(state);
				state.put(selected[0], selected[1]);
			}
		}
	}
	
	//UCB1値の計算をするメソッド
	//勝数が0の場所は無限になるため計算しない
	public double ucb1(int count, int win){
		//UCB1を計算
		if(count != 0){
			return (double)win / (double)count + Math.sqrt(2 * Math.log(total_count + 1) / count);
		} else {
			return 10.0;
		}
		//System.out.println("UCB1 : " + UCB1);
	}
	
	public int selectUCB(ArrayList<int[]> array, mctGameState state){
		GameState s = new GameState();
		s.set(state.data,state.turn,state.player);
		return selectUCB(array, s);
	}

	
	//UCB値はmapに追加するように切り替える予定なので書き換えが必要
	public int selectUCB(ArrayList<int[]> array, GameState state){
		//プレイアウト数0の手はまだmap上に作られていないかもしれない
		//for-each文で配列の要素を1回づつposに入れ，最後まで行ったら抜ける
		for (int[] pos : array) {
			//そのKeyのmapがつくられているかどうか
			if (!map.containsKey(pos[2])) {
				int[] data ={0, 0, hyouka.hyoukaPoint(pos, state)};
				map.put(pos[2], data);
			}
		}
		
		//打てる手の点数の最大値を求める
		int MaxPoint = Integer.MIN_VALUE;
		for(int i = 0; i < array.size(); i++){
			int[] p = map.get(array.get(i)[2]);
			if(MaxPoint < p[2]){
				MaxPoint = p[2];
			}
		}
		
		//選んだ手の情報を保存する配列		//mapから比較対象のデータを取得する配列
		int select = 0;
		int[] select_data = map.get(array.get(0)[2]);
		//打てる手が保存されているarray配列全体で探索を行う
		for(int i = 1; i < array.size(); i++){
			//現在の比較対象の手を取得する配列
			int[] search_point = array.get(i);
			int[] search_data = map.get(search_point[2]);
			//UCB1値が大きい方をselectの方にする
			//ポイントの最大値との差が変数cutよりも大きい時は比較を行わない
			if ((MaxPoint - search_data[2] < cut)&&(ucb1(select_data[0], select_data[1]) < ucb1(search_data[0], search_data[1]))) {
				select = i;
				select_data = search_data;
			}
		}
		
		//System.out.println("UCB : " + ucb1(select_data[0], select_data[1]));
		//選んだ手の配列上の番号を返す
		return select;
	}
	
	//置ける場所をArrayListで返す関数
	public ArrayList<int[]> putPoint(mctGameState state) {
		//置ける場所を記憶するリスト
		ArrayList<int[]> array = new ArrayList<int[]>();
		for(int y=1; y<size-1; y++){
			for(int x=1; x<size-1; x++){
				// 置けるかどうかの判定は先に行う
				if (!state.canPut(x, y)) continue;

				// state をcloneして実際に動かすえ
				mctGameState after = state.clone();
				after.put(x, y);

				// 追加
				int pos[] = {x, y, after.zobhash};
				array.add(pos);
			}
		}
		return array;
	}
		
	//閾値の最適値を求めるときの閾値を設定するためのメソッド
	public void setThreshold(int t){
		threshold = t;
		//閾値を設定するときにマップは初期化を行う
		map = new HashMap<>();
	}
		
	//枝刈りするポイントの差の最適値を求めるときの最大差を設定するためのメソッド
		public void setCut(int c){
		cut = c;
		//閾値を設定するときにマップは初期化を行う
		map = new HashMap<>();
	}

}

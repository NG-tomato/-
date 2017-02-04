import java.util.*;

public class fpu_mctCPU extends CPU {
	
	//自分が置くターンを判別する関数
	int color;
	
	//盤の大きさの変数（壁含む）
	int size = 10;
	
	//1手読むごとの総プレイアウト数
	int count = 100;
	
	//1手読むごとの時間(msミリ秒なので，1秒=1000ms)
	long time = 1000;
	
	//深さごとのプレイアウト数を保存する配列
	int[] total_count = new int[61];
	
	//探索を深くするときのしきい値
	int threshold = 20;
	
	//mapに入れるデータの配列
	//{プレイアウト数,勝数}
	int[] data = new int[3];
	
	//データを入れるmap
	//プレイアウト数、ポイント（その局面のプレイヤの勝数）、UCB1値
	Map<Integer, int[]> map = new HashMap<>();
	
	//fpuの値
	double fpu = 10.0;
	
	//UCB1アルゴリズムの定数C
	double C = 1;
	
	//一手選ぶごとのプレイアウトの平均時間を入れるリスト
	ArrayList<Long> avePlayout = new ArrayList<Long>();

	
	
	//クラスを作成する際に、どっちのプレイヤか選択
	public fpu_mctCPU(int c){
		color = c;
	}
		
	//手を選ぶメソッド
	//これを実行することで手を選択
	int[] decide(GameState state){
		mctMainPanel p = new mctMainPanel(state.data , state.turn, state.player);
		
		//置ける場所をリストに取得
		//putPointメソッドはmctMainPanelの値を与えることで置ける場所のデータを入れた変数を返す
		ArrayList<int[]> array = putPoint(p.state);
		
		//プレイアウトの時間計測開始
		long start = System.currentTimeMillis();
		
		//select関数を用いてプレイアウトしていく
		//閾値を選択
		//プレイアウトが閾値
		for(int i=0; i < count; i++){
		//時間が閾値
		//long start = System.currentTimeMillis();
		//for(long i = start; (i - start) <= time;i = System.currentTimeMillis()){
			//}コメントアウトしてる分の括弧閉じがないとずれるので
			
			//selsect関数はUCB1値が高いものを選んでプレイアウトし、結果をmap関数に適応する変数
			//配列プレイアウト数が一定以上の場合、プレイアウトは行わず、下に再帰的にメソッドを作ることで探索を深めていく
			int x = select(p.state.clone());
			
			
		}
		
		//プレイアウトの時間計測終了
		long end = System.currentTimeMillis();
		//プレイアウト時間（countごと）
		long PlayoutTime = end - start;
		//プレイアウト時間をリストに追加
		avePlayout.add(PlayoutTime);

		
		//ポイントが最大の手を求める
		int i = selectUCB(array,state.black + state.white - 4);

		//System.out.println("After Playouts");
		for (int[] pos : array) {
			int[] playoutResult = map.get(pos[2]);
			/*
			System.out.printf("Legal move: (%d, %d) @%d ==> %f (%d / %d)\n",ucb1(playoutResult[0],playoutResult[1]),playoutResult[1], playoutResult[0]);
			*/
		}
		
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
		int select = selectUCB(array, state.putNumber());
		//選んだ手の情報を取得する配列
		int[] select_point = array.get(select);

		//選んだ手の局面の情報を取得する配列
		int[] data = map.get(select_point[2]);
		
		
		// 1手進めた後で．．．
		state.put(select_point[0], select_point[1]);
		
		//現状が何手まで打った状態（＝木の深さ）か示す変数
		int t = state.putNumber();

		//選んだ手のプレイアウト数が閾値以上の場合、select関数を再帰的に呼び出す
		int winner = (data[0] >= threshold) ? select(state) : playout(state);
		total_count[t]++;
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

	// 完全にランダムプレイ
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
				int selected = new Random().nextInt(array.size());
				state.put(array.get(selected)[0], array.get(selected)[1]);
			}
		}
	}
	
	//UCB1値の計算をするメソッド
	//勝数が0の場所は無限になるため計算しない
	public double ucb1(int count, int win, int total){
		//UCB1を計算
		if(count != 0){
			return (double)win / (double)count + C * Math.sqrt(2 * Math.log(total_count[total] + 1) / count);
		} else {
			return fpu;
		}
		//System.out.println("UCB1 : " + UCB1);
	}
	
	
	//UCB値はmapに追加するように切り替える予定なので書き換えが必要
	public int selectUCB(ArrayList<int[]> array, int total){
		//プレイアウト数0の手はまだmap上に作られていないかもしれない
		for (int[] pos : array) {
			if (!map.containsKey(pos[2])) {
				map.put(pos[2], new int[2]);
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
			if (ucb1(select_data[0], select_data[1], total) < ucb1(search_data[0], search_data[1], total)) {
				select = i;
				select_data = search_data;
			}
		}
		
		//System.out.println("UCB : " + ucb1(select_data[0], select_data[1],total));
		//選んだ手の配列上の番号を返す
		return select;
	}
	
	//パスのときのゾブリストの値をmapに追加する関数
	public void pass(int passZob){
		//プレイアウト数0の場合はまだmap上に作られていないので存在しているかどうかで判断する
		if(map.containsKey(passZob) == false){
			//mapに入れるための空の配列をつくる
			//マップに空の配列を追加する
			map.put(passZob, new int[2]);
		}
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
		//閾値を設定するときにマップとプレイアウト数の初期化を行う
		map = new HashMap<>();
		int[] total_count = new int[61];
	}
	
	//fpuの最適値を求めるときの閾値を設定するためのメソッド
	public void setFPU(double f){
		fpu = f;
		//閾値を設定するときにマップとプレイアウト数の初期化を行う
		map = new HashMap<>();
		int[] total_count = new int[61];
	}
	
	//定数Cの最適値を求めるときの閾値を設定するためのメソッド
	public void setC(double teisu){
		 C = teisu;
		//閾値を設定するときにマップとプレイアウト数の初期化を行う
		map = new HashMap<>();
		int[] total_count = new int[61];
	}
	
	//プレイアウトの平均時間を返すメソッド
	public long avePlayout(){
		int x = avePlayout.size();
		long sum=0;
		for(int i=0;i<x;i++){
			long add = avePlayout.get(i);
			sum += add;
		}
		return (long)sum/x;
	}


}

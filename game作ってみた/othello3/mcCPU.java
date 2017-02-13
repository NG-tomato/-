import java.util.*;

public class mcCPU extends CPU {
	
	//自分が置くターンを判別する関数
	int color;
	
	
	//プレイアウト数
	int count = 50;
	
	//盤の大きさ(壁のところも含めて)
	int size = 10;
	
	//プレイヤを作るときにどっちの色のプレイヤかも指定する
	public mcCPU(int c){
		super(c);
	}
	
	//石を打つときにどこに打つかきめるメソッド
	//現在の状態(GameStateクラス)を引数にとる
	@Override
	int[] decide(GameState state){
		
		//置ける場所を記憶するリスト
		ArrayList<int[]> array = putPoint(state);
		
		
		//置ける場所がない場合は座標が{-1,-1}として返す
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return pos;
		}
		
		
		//それぞれの手の点数を保存する配列
		int[] point = new int[array.size()];
		//現在探索している手を示す変数
		int select = 0;
		//手をずらしながらプレイアウトを繰り返す
		for(int i=0; i < count; i++){
			GameState clone = state.clone();
			//探索中する手が配列の大きさ以上の場合探索する手を配列の0番目の手にもどす
			if(select >= array.size()){
				select = 0;
			}
			//探索する手を打ったあとプレイアウトする
			//mcGameはプレイアウトを行ったあと元の状態にもどす変数
			int a[] = array.get(select);
			clone.put(a[0],a[1]);
			playout(clone);
			if(clone.Win() == state.player){
				point[select]++;
			}
			select++;
		}
		
		//ポイントが最大の手を求める
		int j = 0;
		for(int i = 1; i < array.size(); i++){
			if(point[i] > point[j]){
				j = i;
			}
		}
		return array.get(j);
		
	}
	
	//置ける場所をArrayListで返す関数
	public ArrayList<int[]> putPoint(GameState state) {
		//置ける場所を記憶するリスト
		ArrayList<int[]> array = new ArrayList<int[]>();
		for(int y=1; y<size-1; y++){
			for(int x=1; x<size-1; x++){
				// 置けるかどうかの判定は先に行う
				if (!state.canReverse(x, y)) continue;

				// state をcloneして実際に動かす
				GameState after = state.clone();
				after.put(x, y);

				// 追加
				int pos[] = {x, y};
				array.add(pos);
			}
		}
		return array;
	}
	
	// 完全にランダムプレイ
	int playout(GameState state) {
		while (true) {
			ArrayList<int[]> array = putPoint(state);
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


	
}
import java.util.*;

public class h_mcCPU extends CPU {
	
	//自分が置くターンを判別する関数
	int color;
	
	
	//プレイアウト数
	int count = 1000;
	
	//盤の大きさ(壁のところも含めて)
	int size = 10;
	
	//プレイヤを作るときにどっちの色のプレイヤかも指定する
	public h_mcCPU(int c){
		color = c;
	}
	
	//石を打つときにどこに打つかきめるメソッド
	//現在の状態(GameStateクラス)を引数にとる
	int[] decide(GameState state){
		
		//置ける場所を記憶するリスト
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//盤面の空マスを置けるかチェック
		for(int y=1; y< size-1; y++){
			for(int x=1; x<size-1; x++){
				
				//すでに駒があるときはパス
				if(state.data[x + y * size] != 0)
					continue;
				
				//置けるマスのとき、候補として記憶
				if(state.canReverse(x, y) == true){
					
					//[x,y]の2つの要素を持つ配列として記憶する
					int pos[] = {x,y};
					
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
		
		h_MainPanel p = new h_mcMainPanel(count, state.data , state.turn, state.player);
		
		
		//それぞれの手の点数を保存する配列
		int[] point = new int[array.size()];
		//現在探索している手を示す変数
		int select = 0;
		//手をずらしながらプレイアウトを繰り返す
		for(int i=0; i < count; i++){
			//探索中する手が配列の大きさ以上の場合探索する手を配列の0番目の手にもどす
			if(select >= array.size()){
				select = 0;
			}
			//探索する手を打ったあとプレイアウトする
			//mcGameはプレイアウトを行ったあと元の状態にもどす変数
			int a[] = array.get(select);
			p.mcGame(a);
			point[select] = p.rePoint(state.player);
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
	
}
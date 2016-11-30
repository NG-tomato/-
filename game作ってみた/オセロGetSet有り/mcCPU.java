import java.util.*;

public class mcCPU {
	
	//自分が置くターンを判別する関数
	int color;	//BLACK or WHITE
	int size;
	
	
	//プレイアウト数
	int count = 10;
	
	public mcCPU(int c,int s){
		color = c;
		size = s;
	}
	
	int[] decide(GameState state){
		
		//置ける場所を記憶するリスト
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//盤面の空マスを置けるかチェック
		for(int y=1; y<size + 2; y++){
			for(int x=1; x<size + 2; x++){
				
				//すでに駒があるときはパス
				if(state.data[x][y] != 0)
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
		
		//ランダム選択
		
		//置ける場所がない場合は座標が{-1,-1}として返す
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return pos;
		}
		
		mcMainPanel p = new mcMainPanel(count, Arrays.copyOf(state.data, state.data.length), state.turn, state.player);
		
		//p.TextDisplay();
		//System.out.println(Arrays.deepToString(state.data));
		
		//それぞれの手の点数を保存する配列
		int[] point = new int[array.size()];
		
		for(int i=0; i < array.size(); i++){
			int a[] = array.get(i);
			point[i] = p.mcGame(a, state.player);
		}
		
		//ポイントが最大の手を求める
		int j = 0;
		for(int i = 1; i < array.size(); i++){
			if(point[i] > point[j]){
				j = i;
			}
		}
		System.out.println(point[j]);
		return array.get(j);
		
		/*
		//ランダムクラスのインスタンス化
		Random rnd = new Random();
		
		/*
		//ランダムクラス内のnextIntメソッドを利用し乱数を作成
		nextInt(x);
		0からxまでが乱数が取る可能性がある値
		置ける位置のいずれかを選択すればいいので、置ける場所を保存したリストのサイズ数内の範囲で乱数を作成することでランダムで置く場所を決めるようにする
		
		int index = rnd.nextInt(array.size());
		
		//乱数で選ばれた置ける場所を返す
		return array.get(index);
		*/
}
	
}
import java.util.*;

public class RandomCPU2 {
	
	//自分が置くターンを判別する関数
	int color;	//BLACK or WHITE
	int size;
	
	public RandomCPU2(int s){
		color = 1;
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
		
		//ランダムクラスのインスタンス化
		Random rnd = new Random();
		
		/*
		ランダムクラス内のnextIntメソッドを利用し乱数を作成
		nextInt(x);
		0からxまでが乱数が取る可能性がある値
		置ける位置のいずれかを選択すればいいので、置ける場所を保存したリストのサイズ数内の範囲で乱数を作成することでランダムで置く場所を決めるようにする
		*/
		int index = rnd.nextInt(array.size());
		
		//乱数で選ばれた置ける場所を返す
		return array.get(index);
	}
	
}

import java.util.*;

//Monte Carlo法の略 → mc
public class mcCPU {
	
	//自分が置くターンを判別する関数
	int color;	//BLACK or WHITE
	int size;
	
	//プレイアウト数
	int count = 3; 
	
	int data[][];
	
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
		
		
		//置ける場所がない場合は座標が{-1,-1}として返す
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return pos;
		}
		
		data = Arrays.copyOf(state.data, state.data.length);
		
		mcMainPanel mcpanel = new mcMainPanel(data, count, state.turn, state.player);
		
		//それぞれの手の点数を保存する配列
		int[] point = new int[array.size()];
			for(int i=0; i < array.size(); i++){
				int a[] = array.get(i);
				int x = a[0];
				int y = a[1];
				mcpanel.mcGame(x,y);
				point[i] = mcpanel.rePoint();
			}
		
		//ポイントが最大の手を求める
		int j = 0;
		for(int i = 1; i < array.size(); i++){
			if(point[i] > point[j]){
				j = i;
			}
		}
		//選ばれた置ける場所を返す
		System.out.println(array.get(j)[0] + ", " + array.get(j)[1]);
		return array.get(j);
	}
	
}
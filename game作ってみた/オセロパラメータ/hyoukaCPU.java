import java.util.*;

public class hyoukaCPU extends CPU {
	
	//自分が置くターンを判別する関数
	int color;	//BLACK or WHITE
	int size = 10;
	
	//ランダムクラスのインスタンス化
	Random rnd = new Random();
	
	private int[] valueMap = {  45, -11,   4,  -1,  -1,   4, -11,  45, 
								11, -16,  -1,  -3,  -3,  -1, -16, -11,
								 4,  -1,   2,  -1,  -1,   2,  -1,   4,
		                        -1,  -3,  -1,   0,   0,  -1,  -3,  -1,
		                        -1,  -3,  -1,   0,   0,  -1,  -3,  -1,
		                         4,  -1,   2,  -1,  -1,   2,  -1,   4,
		                       -11, -16,  -1,  -3,  -3,  -1, -16, -11,
		                        45, -11,   4,  -1,  -1,   4, -11,  45	}; 
	
	public hyoukaCPU(int c){
		color = c;
	}
	
	
	int[] decide(GameState state){
		
		//置ける場所を記憶するリスト
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//盤面の空マスを置けるかチェック
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
				//すでに駒があるときはパス
				if(state.data[x + y * 10] != 0)
					continue;
				
				//置けるマスのとき、候補として記憶
				if(state.canReverse(x, y) == true){
					
					//[x,y,0]の3つの要素を持つ配列として記憶する
					//3つ目の0のところにはあとで点数を返す
					int pos[] = {x, y, 0};
					
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
		//置ける場所が1つの場合はその手を返す
		else if(array.size() == 1){
			int pos[] = array.get(0);
			int data[] = {pos[0] , pos[1]};
			return data;
		}
		
		int select = 0;
		//探索の必要がある場合は評価を行って点数をつける
		for(int i = 0;i<array.size();i++){
			int[] pos = array.get(i);
			hyoukaPoint(pos, state);
			//最大の値がある地点をselectに保存する
			//新しく求めた値がそれまでの値より大きい場合にselectを変更する
			int[] serch_pos = array.get(select);
			if(serch_pos[2] < pos[2]){
				select = i;
			}
		}
		
		//System.out.println("select = "+ select );
		
		
		//乱数で選ばれた置ける場所を返す
		return array.get(select);
	}
	
	
	//評価した点数を返すメソッド
	public void hyoukaPoint(int[] pos, GameState state){
		int player = state.player;
		//stateを新しく作り，その点数を返す
		GameState s = new GameState();
		s.set(state.data, state.turn, state.player);
		//手を打った状態へ遷移
		s.put(pos[0] ,pos[1]);
		//位置による評価
		int bp = banPoint(s);
		int fs = fixStone(s);
		int cn = canNumber(s);
		pos[2] = bp * 2 + fs * 5 + cn * 1;
	}
	
	
	
	//位置による評価を行うメソッド
	public int banPoint(GameState state){
		//手をうつ前のプレイヤーのための評価なので-1してプレイヤーを打つ前にもどす
		int player = state.player * -1;
		int sum = 0;
		
		int[] data = state.data;
		//位置ごとに計算を行い加算していく
		for(int i = 1;i <= 8; i++){
			for(int j = 1; j <= 8; j++){
				if(data[i + j * 10] != 0){
					sum += (data[i + j*10] * valueMap[(i-1) + (j-1) * 8]) + (int) Math.floor(Math.random() * 3);
				}
			}
		}
		//白がプレイヤーのとき数値を反転させる
		return sum * player;
	}
	
	
	
	
	//確定石の数を返すメソッド
	//全部探すのは難しいので辺のみ
	public int fixStone(GameState state){
		//それぞれの色の確定石の数を保存する配列
		//{黒の確定石数，白の確定石数}
		int[] fs = new int[2];
		
		int[] data = state.data;
		//角の石の配列を作成
		int[] corner = { data[1+1*10], data[8+1*10], data[1+8*10], data[8+8*10] };
		
		//角がある場合それは確定石なので加算
		for(int i=0; i < corner.length; i++){
			if(data[i]==1){
				fs[0]++;
			}else if(data[i]==-1){
				fs[1]++;
			}
		}
		
		//横の確定石を返すメソッドfsHolLineと縦の確定石を返すメソッドfsVerLineを利用して辺の確定石を求める
		//上の横線の確定石
		int[] line = {corner[0], corner[1]};
		int[] add_fs = fsHolLine(line, 1, state);
		fs[0] += add_fs[0];fs[1]+=add_fs[1];
		//下の横線の確定石
		line[0] = corner[2];line[1] = corner[3];
		add_fs = fsHolLine(line, 8, state);
		fs[0] += add_fs[0]; fs[1]+=add_fs[1];
		//左の縦線の確定石
		line[0] = corner[0];line[1] = corner[2];
		add_fs = fsVerLine(line, 8, state);
		fs[0] += add_fs[0]; fs[1]+=add_fs[1];
		//右の縦線の確定石
		line[0] = corner[1];line[1] = corner[3];
		add_fs = fsVerLine(line, 8, state);
		fs[0] += add_fs[0]; fs[1]+=add_fs[1];
		
		
		//打った後なので逆のプレイヤの確定石数で返す
		if(state.player == 1){
			return (fs[1] - fs[0] + (int) Math.floor(Math.random() * 3)) * 11;
		}else{
			return (fs[0] - fs[1] + (int) Math.floor(Math.random() * 3)) * 11;
		}
	}
	
	//横並びの辺の確定石を求めるメソッド
	public int[] fsHolLine(int[] corner,int y, GameState state){
		//それぞれの色の確定石の数を保存する配列
		//{黒の確定石数，白の確定石数}
		int[] fs = new int[2];
		
		int[] data = state.data;

		//上の辺
		CheckTop:{
			//どっちの端もある場合
			if(corner[0] != 0 && corner[1] != 0){
				int b_fs = 0;
				int w_fs = 0;
				//全部埋まっている場合すべて確定石
				for(int i = 2;i<=7;i++){
					//空きがあるので確定石と判断せず次のforへ移る
					if(data[i + 10 * y] == 0){
						b_fs = 0;
						w_fs = 0;
						break;
					}else if(data[i + 10 * y] == 1){
						b_fs ++;
					}else{
						w_fs ++;
					}
					//最後まで探索し終わった(i==7)とき
					if(i == 7){
						//空きがないので確定石として加算して上の辺の探索を終わる
						fs[0] += b_fs; fs[1] += w_fs;
						break CheckTop;
					}
				}
			}
			/*
			どっちの端もあるかつ全部埋まってないまたは，右端だけある場合
			右端から順番に探索
			プレイヤと同じ石が連続している場合確定石と判断
			そうでない場合そこで探索を終了する
			*/
			if(corner[0]!=0){
				for(int i = 2;i <= 7;i++){
					//空きまたは別の石がある場合そこで処理を終了
					if(corner[0]!=data[i+10*y]){
						break;
					}
					if(corner[0] == 1){
						fs[0]++;
					}else{
						fs[1]++;
					}
				}
			}
			/*
			どっちの端もあるかつ全部埋まってないまたは，左端だけある場合
			左端から順番に探索
			上と逆の順で同様の処理
			*/
			if(corner[1]!=0){
				for(int i = 7;i <= 2;i++){
					//空きまたは別の石がある場合そこで処理を終了
					if(corner[1]!=data[i+10*y]){
						break;
					}
					if(corner[1] == 1){
						fs[0]++;
					}else{
						fs[1]++;
					}
				}
			}
			
		}
		return fs;
		
	}
	
	//縦並びの辺の確定石を求めるメソッド
	public int[] fsVerLine(int[] corner,int x, GameState state){
		//それぞれの色の確定石の数を保存する配列
		//{黒の確定石数，白の確定石数}
		int[] fs = new int[2];
		
		int[] data = state.data;

		//上の辺
		Check:{
			//どっちの端もある場合
			if(corner[0] != 0 && corner[1] != 0){
				int b_fs = 0;
				int w_fs = 0;
				//全部埋まっている場合すべて確定石
				for(int i = 2;i<=7;i++){
					//空きがあるので確定石と判断せず次のforへ移る
					if(data[x + 10 * i] == 0){
						b_fs = 0;
						w_fs = 0;
						break;
					}else if(data[x + 10 * i] == 1){
						b_fs ++;
					}else{
						w_fs ++;
					}
					//最後まで探索し終わった(i==7)とき
					if(i == 7){
						//空きがないので確定石として加算して上の辺の探索を終わる
						fs[0] += b_fs; fs[1] += w_fs;
						break Check;
					}
				}
			}
			/*
			どっちの端もあるかつ全部埋まってないまたは，上だけある場合
			上から順番に探索
			プレイヤと同じ石が連続している場合確定石と判断
			そうでない場合そこで探索を終了する
			*/
			if(corner[0]!=0){
				for(int i = 2;i <= 7;i++){
					//空きまたは別の石がある場合そこで処理を終了
					if(corner[0]!=data[x+10*i]){
						break;
					}
					if(corner[0] == 1){
						fs[0]++;
					}else{
						fs[1]++;
					}
				}
			}
			/*
			どっちの端もあるかつ全部埋まってないまたは，下だけある場合
			下から順番に探索
			上と逆の順で同様の処理
			*/
			if(corner[1]!=0){
				for(int i = 7;i <= 2;i++){
					//空きまたは別の石がある場合そこで処理を終了
					if(corner[1]!=data[x+10*i]){
						break;
					}
					if(corner[1] == 1){
						fs[0]++;
					}else{
						fs[1]++;
					}
				}
			}
			
		}
		return fs;
		
	}
	
	//横並びの辺の確定石を求めるメソッド
	public int canNumber(GameState state){
		int cn = 0;
		//盤面の空マスを置けるかチェック
		for(int y=1; y<=8; y++){
			for(int x=1; x<=8; x++){
				
				//すでに駒があるときはパス
				if(state.data[x + y * 10] != 0)
					continue;
				
				//置けるマスのとき、候補数を加算
				if(state.canReverse(x, y) == true){
					cn++;
				}
			}
		}
		return -(cn + (int) Math.floor(Math.random() * 2) * 10);
	}

}
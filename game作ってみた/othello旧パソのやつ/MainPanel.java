//表示させるパネルを作成するクラス



//共通の処理のメソッド(日時機能、国際化、乱数ジェネレータ)を集めたクラス（ユーティリティクラス）
import java.util.*;

/*
パネルクラス
基本的動作を実装
*/
public class MainPanel{
	//縦横のマス
	
	//状態を表すクラスstateを作成
	GameState state = new GameState();
	
	//ランダムで打つAIのクラスRandomCPUを作成
	//black
	//RandomCPU b_cpu = new RandomCPU(1);
	//mcCPU b_cpu = new mcCPU(1);
	//hyoukaCPU b_cpu = new hyoukaCPU(1);
	//mctCPU b_cpu = new mctCPU(1);
	//h_mctCPU b_cpu = new h_mctCPU(1);
	//c_mctCPU b_cpu = new c_mctCPU(1);
	//hc_mctCPU b_cpu = new hc_mctCPU(1);
	fpu_mctCPU b_cpu = new fpu_mctCPU(1); 
	//hfpu_mctCPU b_cpu = new hfpu_mctCPU(1); 

	
	//white
	RandomCPU w_cpu = new RandomCPU(-1);
	//mcCPU w_cpu = new mcCPU(-1);
	//mctCPU w_cpu = new mctCPU(-1);
	//hyoukaCPU w_cpu = new hyoukaCPU(-1);
	//h_mctCPU w_cpu = new h_mctCPU(-1);
	//c_mctCPU w_cpu = new c_mctCPU(-1);
	//hc_mctCPU w_cpu = new hc_mctCPU(-1);
	//fpu_mctCPU w_cpu = new fpu_mctCPU(-1); 
	//hfpu_mctCPU w_cpu = new hfpu_mctCPU(-1); 

	
	
	//勝敗の結果の合計を入れる配列
	int winCount[] = new int[3];
	
	//値を決定する際の値の増やす数
	int add = 3;

	//値を最適化するときに何回感覚で進捗状況を表示するか
	int lookGame = 100;
	
	//メインパネルを作成するメソッド
	public MainPanel(int count){
		
		
		//閾値を決めるメソッド
		//decideThreshold(count);
		
		
		
		//UCB1値のCの最適化
		//decideC(count);
		
		//FPUの最適値を求める
		//decideFPU(count);

		
		//枝刈りするときの点数の差の最大値
		//decideCut(count);
		
		
		//通常のgame
		for(int i = 0; i < count; i++){
			if(i % 50 == 0){
				System.out.println(i);
			}
			game();
			//textDisplay();
			state.reset();
			b_cpu.reset();
		}
		System.out.println();
		System.out.println("---Loop END---");
		System.out.println("Black win : " + winCount[0]);
		System.out.println("White win : " + winCount[1]);
		System.out.println("Draw      : " + winCount[2]);
		
		/*
		//平均時間を求める
		long shikou = 0;
		for(int i = 0; i < count; i++){
			if(i % 50 == 0){
				System.out.println(i);
			}
			game();
			//プレイアウトごとの思考平均時間
			shikou +=  b_cpu.avePlayout();
			state.reset();
			b_cpu.reset();
		}
		shikou = (long)shikou/count;
		System.out.println("Playout's average time(100 every time): " + shikou + " ms");
		*/
		
	}
	
		
	//描写を行うメソッド
	public void textDisplay(){
		System.out.println();
		
		//左上から順にマスと駒を表示
		for(int y=1; y<=8; y++){
			for(int x=1; x<=8; x++){
				System.out.print("|");
				switch (state.data[x + y * 10]) {
				case 1:
					System.out.print("○");					//黒の駒を表示
					break;
				case -1:
					System.out.print("●");					//白の駒を表示
					break;
				default:
					System.out.print("  ");
				}
			}
			System.out.println("|");
		}
		System.out.println("TURN = "+state.turn);
		System.out.println("PLAYER = "+state.player);
		System.out.println("DISC = "+state.black+" : " +state.white);
		System.out.println("\n \n");
		
	}
	
	
	public void game(){
		boolean isLastPass = false;
		
		for(;;){
			//textDisplay();

			// 置くところがなければパス
			if( state.checkPass() == true ){
				if (isLastPass) {	//両方パスだと終了
					endGame();
					break;
				}
				
				state.player *= -1;
				isLastPass = true;
				//System.out.println("Pass! Next turn is : "+state.player);
				continue;
			}
			isLastPass = false;
			
			// 以下では必ず置けるものとする．
			if(state.player == b_cpu.color){ 			//Black

				//cpu内のdecideメソッドで置く場所を決定
				int action[] = b_cpu.decide(state);
				if (action[0] == -1 || action[1] == -1) { throw new RuntimeException("Illegal Move by Black: (-1, -1)"); }
				//置ける場所がある場合のみ駒を置く処理をする
				state.put(action[0], action[1]);
				//System.out.println("Black put point is : "+action[0]+" ,"+action[1]);
			}
			else { // White
				//cpu内のdecideメソッドで置く場所を決定
				int action[] = w_cpu.decide(state);
				
				if (action[0] == -1 || action[1] == -1) { throw new RuntimeException("Illegal Move by White: (-1, -1)"); }
				//置ける場所がある場合のみ駒を置く処理をする
				state.put(action[0], action[1]);
				//System.out.println("White put point is : "+action[0]+" ,"+action[1]);
			}
		}
	}
	
	//int GameCount = 0;
	//ゲーム終了時に勝ち負けの数を追加するクラス
	public void endGame(){
		//System.out.println("---Game END---");
		int End = state.Win();
		if(End == 1){
		//	Winner = "black";
			winCount[0] ++;
		}else if(End == -1){
		//	Winner = "white";
			winCount[1] ++;
		}else {
		//	Winner = "Drow";
			winCount[2] ++;
		}
		//textDisplay();
		//GameCount ++;
		/*
		System.out.println("Game count = " + GameCount);
		System.out.println("Winner = " + End);
		*/
	}

	
	//木を展開する閾値Thresholdの最適化(vsランダムCPUの勝率により最適化)
	public void decideThreshold(int count){
		//探索した値の勝敗数を記憶するマップ
		//これを活用して，探索の回数を減らす
		Map<Integer, int[]> map = new HashMap<>();
		int j = 38;
		//Cの比較
		for(int add = 10;add >= 1;add = add / 2){
			//前の手順での勝数を保存する変数
			int backWin = -1;
			while(j>0){
				//比較中の閾値ごとの勝ち負けを保存
				int[] thresholdCount = new int[3];
				
				//勝敗数を初期化
				winCount = new int[3];
				
				//map上に勝敗数があるものはすでに勝敗数を計算済みなのでそれを取得する
				if(map.containsKey(j)){
					winCount = map.get(j);
					System.out.println();
					System.out.println(" Game skip ! : " + j);
				}
				//未計算の場合
				else{
					//閾値を変更
					b_cpu.setThreshold(j);
					System.out.println("Black's Threshold is " + j);
					for(int i = 0; i < count; i++){
					
						if(i%lookGame == 0){
							System.out.println("Now Game is " + i);
						}
						game();
						//textDisplay();
						//System.out.println("Now Game is "+ i);
						state.reset();
						b_cpu.reset();
					}
					map.put(j,winCount);
				}
				System.out.println();
				System.out.println("---Loop END---");
				System.out.println("Black win : " + winCount[0]);
				System.out.println("White win : " + winCount[1]);
				System.out.println("Draw      : " + winCount[2]);
				System.out.println();

				
				//勝数が逆転したら終了
				if(backWin > winCount[0]){
					System.out.println();
					j -= add;
					System.out.println();
					System.out.println("Last Threshold is "+ j);
					System.out.println();
					break;
				}else{
					backWin = winCount[0];
					//System.out.println("test" + add);
					j+=add;
					//System.out.println("test" + j);
				}
			}
			j -= add;
		}
	}

	//枝刈りを行う手法の値FPUを決定するメソッド(vsランダムCPUの勝率により最適化)
	public void decideFPU(int count){
		//探索した値の勝敗数を記憶するマップ
		//これを活用して，探索の回数を減らす
		Map<Double, int[]> map = new HashMap<>();
		
		double j = 5;
		//Cの比較
		for(double add = -2;add <=-0.001;add = (double)add / 2){
			
			//前の手順での勝数を保存する変数
			int backWin = -1;
			while(j>0){
				//比較中の閾値ごとの勝ち負けを保存
				int[] FPUCount = new int[3];
				
				//勝敗数を初期化
				winCount = new int[3];
				
				//map上に勝敗数があるものはすでに勝敗数を計算済みなのでそれを取得する
				if(map.containsKey(j)){
					winCount = map.get(j);
					System.out.println();
					System.out.println(" Game skip ! : " + j);

				}
				//未計算の場合
				else{
					//閾値を変更
					b_cpu.setFPU(j);
					System.out.println("Black's FPU is " + j);
					for(int i = 0; i < count; i++){
						
						if(i%lookGame == 0){
							System.out.println("Now Game is " + i);
						}
						game();
						//textDisplay();
						//System.out.println("Now Game is "+ i);
						state.reset();
						b_cpu.reset();
					}
					map.put(j,winCount);
				}
				System.out.println();
				System.out.println("---Loop END---");
				System.out.println("Black win : " + winCount[0]);
				System.out.println("White win : " + winCount[1]);
				System.out.println("Draw      : " + winCount[2]);
				System.out.println();
				
				//勝数が逆転したら終了
				if(backWin > winCount[0]){
					System.out.println();
					j -= add;
					System.out.println();
					System.out.println("Last FPU is "+ j);
					System.out.println();
					break;
				}else{
					backWin = winCount[0];
					//System.out.println("test" + add);
					j+=add;
					//System.out.println("test" + j);
				}
			}
			j -= add;
		}
	}

	

	
	
	//UCB1値のCの最適化
	public void decideC(int count){
		//探索した値の勝敗数を記憶するマップ
		//これを活用して，探索の回数を減らす
		Map<Double, int[]> map = new HashMap<>();

		
		double j = 3;
		//Cの比較
		for(double add = 1.0;add > 0.01;add = (double)add/2){
			
			//前の手順での勝数を保存する変数
			int backWin = -1;
			while(j>0){
				//比較中の閾値ごとの勝ち負けを保存
				int[] cutCount = new int[3];
				
				//勝敗数を初期化
				winCount = new int[3];
				
				//map上に勝敗数があるものはすでに勝敗数を計算済みなのでそれを取得する
				if(map.containsKey(j)){
					winCount = map.get(j);
					System.out.println();
					System.out.println(" Game skip ! : " + j);
				}
				//未計算の場合
				else{
					//閾値を変更
					b_cpu.setC(j);
					System.out.println("Black's C is " + j);
					for(int i = 0; i < count; i++){
						
						if(i%lookGame == 0){
							System.out.println("Now Game is " + i);
						}
						game();
						//textDisplay();
						//System.out.println("Now Game is "+ i);
						b_cpu.reset();
						state.reset();
					}					
					map.put(j,winCount);
				}
					
					
				System.out.println();
				System.out.println("---Loop END---");
				System.out.println("Black win : " + winCount[0]);
				System.out.println("White win : " + winCount[1]);
				System.out.println("Draw      : " + winCount[2]);
				System.out.println();
				
				//勝数が逆転したら終了
				if((backWin > winCount[0])||(add == 0)){
					System.out.println();
					j += add;
					System.out.println("Last C is "+ j);
					break;
				}else{
					backWin = winCount[0];
					//System.out.println("test" + add);
					j-=add;
					//System.out.println("test" + j);
				}
			}
			j += add;
			/*
			if(j>1){
				j=1;
			}*/
		}
	}
	

}

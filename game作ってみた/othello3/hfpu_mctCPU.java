import java.util.*;

public class hfpu_mctCPU extends fpu_mctCPU {
		
	//1手読むごとの総プレイアウト数
	int count = 100;
	
	//1手読むごとの時間(msミリ秒なので，1秒=1000ms)
	long time = 100;
	
	//時間とプレイアウト数どっちを使用するか(trueのときプレイアウト数，falseのとき時間)
	boolean switch_threshold = true;
	
	//探索を深くするときのしきい値
	int threshold = 30;
	
	//UCB1アルゴリズムの定数C
	double C = 0.18;
	
	//fpuの値
	double fpu = 1.69;//100.0;
	
	
	//プレイアウト時に使用する評価関数のCPUを指定
	hyoukaCPU CPU = new hyoukaCPU(1);
	
	//クラスを作成する際に、どっちのプレイヤか選択
	public hfpu_mctCPU(int c){
		super(c);
		super.count = count;
		super.time = time;
		super.switch_threshold = switch_threshold;
		super.threshold = threshold;
		super.fpu = fpu;
		super.C = C;

	}
		
	//評価値を用いてプレイアウトを行う
	@Override
	int playout(mctGameState state){
		while (true) {
			ArrayList<int[]> array = putPoint(state);
			if (array.size() == 0) {
				// 置けなければパスをする (しかない)
				if (state.isLastPass) {
					//プレイアウト回数の加算
					playoutCount++;
					// 相手もパスをしていた場合は ここで結果を返す
					if (state.black > state.white) return 1;
					if (state.white > state.black) return -1;		
					return 0;
				}
				// パスをして1手進めたところでプレイアウトしてもらう
				state.pass();
			} else {
				int[] selected = CPU.playoutDecide(state);
				state.put(selected[0], selected[1]);
			}
		}

	}
	
}

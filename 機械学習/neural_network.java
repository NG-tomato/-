import java.util.*;

public class neural_network{
	// 記号定数の定義
	static int INPUTNO = 2;		// 入力値のセル数
	static int HIDDENNO = 2;		// 中間層のセル数位
	static int ALPHA = 20;			// 学習係数
	static int MAXINPUTNO = 100;	// 学習データの最高個数
	static int BIGNUM = 100;		// 誤差の初期値
	static double LIMIT = 0.001;		// 誤差の上限値

	public static void main(String[] args){
		double[][] wh = new double[HIDDENNO][INPUTNO + 1];	// 中間層の重み
		double[] wo = new double[HIDDENNO + 1];				// 出力層の重み
		double[][] e = new double[MAXINPUTNO][INPUTNO + 1];	// 学習データセット
		double[] hi = new double[HIDDENNO + 1];				// 中間層の出力
		double o;											// 出力
		double err = BIGNUM;								// 誤差の評価
		int i,j;											// 繰り返しの制御
		int n_of_e;											// 学習データの個数
		
		//重みの初期化
		initwh(wh);
		initwo(wo);
		print(wh,wo);
		
		// 学習データの読み込み
		n_of_e =getdata(e);
		System.out.println("学習データの個数 : "+ n_of_e);
		
		
		// 学習
		while (err > LIMIT){
			err = 0.0;
			for(j = 0;j<n_of_e;++j){
				// 逆方向の計算
				o=forward(wh, wo, hi, e[j]);
				// 出力層の重みの調整
				olearn(wo, hi, e[j], o);
				// 誤差の積算
				err += (o-e[j][INPUTNO])*(o-e[j][INPUTNO]);
			}
			//誤差の出力
			System.out.println(err);
		}//学習終了
		
		// 結合荷重の出力
		print(wh, wo);
		
		// 学習データに対する出力
		for(i=0;i < n_of_e;++i){
			System.out.print(i+" ");
			for(j=0;j<INPUTNO+1;++j)
				System.out.print(e[i][j] + " ");
			o = forward(wh ,wo ,hi ,e[i]);
			System.out.println(o);
		}
		return;
	}
	
	
	/*
	学習データの読み込みをするメソッド
	getdata(学習データを入れる配列)
	*/
	public static int getdata(double e[][]){
		int n_of_e =0;	// データセットの個数
		int j = 0;		// 繰り返しの制御
		Scanner sc = new Scanner(System.in);
		// データの入力
		while(sc.hasNextInt()){
			++j;
			if(j > INPUTNO){
				j = 0;
				++n_of_e;
			}
		}
		return n_of_e;
	}
	
	
	
	public static void olearn(double wo[] ,double hi[] ,double e[] ,double o){
	}
	public static double forward(double wh[][] ,double wo[], double hi[], double e[]){
	}
	public static void print(double wh[][], double wo[]){}
	public static void initwh(double wh[][]){}
	public static void initwo(double wo[]){}
	public static double drnd(){}
	public static double s(double u){}
	
}
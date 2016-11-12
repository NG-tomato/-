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
	getdata(学習データの配列)
	*/
	public static int getdata(double e[][]){
		int n_of_e =0;	// データセットの個数
		int j = 0;		// 繰り返しの制御
		Scanner sc = new Scanner(System.in);
		// データの入力
		while(sc.hasNextInt()){
			e[n_of_e][j] = sc.nextInt();
			++j;
			if(j > INPUTNO){
				j = 0;
				++n_of_e;
			}
		}
		return n_of_e;
	}
	
	
	/*
	出力層の重みを学習するメソッド
	olearn(出力層の重みの配列, 中間層の出力の配列, 学習データの配列, 出力)
	*/
	public static void olearn(double wo[] ,double hi[] ,double e[] ,double o){
		int i;		// 繰り返しの制御
		double d;	// 重み計算に利用
		
		d = (e [INPUTNO] - o) * o * (1 - o);	// 誤差の計算
		for(i=0; i < HIDDENNO; ++i){
			wo[i] += ALPHA*hi[i] * d;			// 結合荷重の学習
		}
		wo[i] += ALPHA * (-1.0) * d;			// しきい値の学習
	}
	
	
	/*
	順方向の計算をするメソッド
	forward(中間層の重みの配列, 出力層の重みの配列, 中間層の出力の配列, 学習データの配列)
	*/
	public static double forward(double wh[][] ,double wo[], double hi[], double e[]){
		int j,i;		// 繰り返しの制御
		double u;		// 重み付き和の合計
		double o;		// 出力の計算
		
		// hiの計算
		for(i = 0; i < HIDDENNO; i++){
			u=0;						// 重み付き和を求める
			for(j = 0; j < INPUTNO; ++j){
				u += e[j] * wh[i][j];
			}
			u -= wh [i][j];		// しきい値の処理
			hi[i] = s(u);
		}
		
		//出力oの計算
		o = 0;
		for(i = 0;i < HIDDENNO; i++){
			o += hi[i] * wo[i];
		}
		o -= wo[i];		// しきい値の処理
		
		return s(o);
	}
	
	
	/*
	結果の出力
	print(中間層の重みの配列, 出力層の重みの配列)
	*/
	public static void print(double wh[][], double wo[]){
		int i,j;
		
		for(i = 0; i < HIDDENNO; ++i){
			for(j = 0; j < INPUTNO + 1; ++j){
				System.out.print(wh[i][j] + " ");
			}
		}
		System.out.println();
		for(i = 0; i < HIDDENNO + 1; ++i){
			System.out.print(wo[i] + " ");
		}
		System.out.println();
	}
	
	
	/*
	中間層の重みの初期化
	intwh(中間層の重みの配列)
	*/
	public static void initwh(double wh[][]){
		int i,j;
		//乱数による結合荷重の決定
		for(i = 0; i < HIDDENNO; ++i){
			for(j = 0; j < INPUTNO + 1; ++j){
				wh[i][j] = Math.random() * 2 - 1;
			}
		}
		// 定数を荷重として与える場合
		/*
		wh[0][0] = -2;
		wh[0][1] = 3;
		wh[0][2] = -1;
		wh[1][0] = -2;
		wh[1][1] = 1;
		wh[1][2] = 1;
		*/
		
	}
	
	
	/*
	出力層の重みの初期化
	intwh(出力層の重みの配列)
	*/
	public static void initwo(double wo[]){
		int i;
		for(i = 0; i < HIDDENNO + 1; ++i){
			wo[i] = Math.random() * 2 - 1;
			
		}
	}
	
	/*
	乱数を生成するクラス
	javaではMath.random()で生成できるため無視
	*/
	//public static double drnd(){}
	
	
	/*
	シグモイド関数
	s(重み付き和)
	*/
	public static double s(double u){
		return 1.0 / (1.0 + Math.exp(-u));
	}
	
}
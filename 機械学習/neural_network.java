import java.util.*;

public class neural_network{
	/* 記号定数の定義 */
	int INPUTNO = 2;		// 入力値のセル数
	int HIDDENNO = 2;		// 中間層のセル数位
	int ALPHA = 20;			// 学習係数
	int MAXINPUTNO = 100;	// 学習データの最高個数
	int BIGNUM = 100;		// 誤差の初期値
	double LIMIT = 0.001;		// 誤差の上限値
	
	public static void main(String[] args){
		double wh[HIDDENNO][INPUTNO + 1];	// 中間層の重み
		double wo[HIDDENNO + 1];			// 出力層の重み
		double e[MAXINPUTNO][INPUTNO + 1];	// 学習データセット
		double hi[HIDDENNO + 1];			// 中間層の出力
		double o;							// 出力
		double err = BIGNUM;
		
	}
}
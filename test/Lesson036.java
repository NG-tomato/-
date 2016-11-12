public class Lesson036{
    public static void main(String args[]){
        //randomメソッドは実数を生成するのでdouble型の変数を宣言
        double d;
        //randomメソッドで生成した実数を変数dに代入
		d = Math.random() * 2 - 1;
        //出力
        System.out.println(d);
    }
}
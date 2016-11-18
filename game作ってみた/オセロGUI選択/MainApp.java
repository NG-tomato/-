//App作成のMainクラス

//Java の GUI ツールキットである AWT を拡張したもの
import javax.swing.*;
import java.util.Scanner;

//Java で GUIアプリケーションを作成するためのクラスライブラリ(java.awt)から、Containerクラスを格納するためにContainerをimport
import java.awt.Container;

public class MainApp {

	public MainApp(int number){
		if(number == 1){
			MainPanel panel = new MainPanel();
		}else if(number == 2){
			MainApp2 panel = new MainApp2();
			panel.setVisible(true);
		}
	}

//Mainクラス
	public static void main(String[] args){
		//文字出力
		System.out.println("Please select number");
		System.out.println("Not need GUI : 1");
		System.out.println("Need GUI : 2");
		//値取得
		Scanner scan = new Scanner(System.in);
		int val = scan.nextInt();
		//MainAppクラスを呼び出し、画面を作成
		if(val == 1 || val == 2){
			MainApp app = new MainApp(val);
		}else{
			System.out.println("Unspecified character was entered");
		}
		
	}
	
}
//App作成のMainクラス

//Java の GUI ツールキットである AWT を拡張したもの
import javax.swing.*;
import java.util.Scanner;

//Java で GUIアプリケーションを作成するためのクラスライブラリ(java.awt)から、Containerクラスを格納するためにContainerをimport
import java.awt.Container;

public class MainApp {
	public MainApp() {
	}

	private void mainWithGUI() {
			MainApp2 panel = new MainApp2();
			panel.setVisible(true);
	}

	private void mainWithoutGUI() {
			//文字出力
			Scanner scan = new Scanner(System.in);
			/*
			String player = "Black";
			int B_CPU = 0;
			int W_CPU = 0;
			for(int i = 0; i < 2; i++){
				System.out.println("Please select use " + player + " CPU");
				System.out.println("RondomCPU     : 1");
				System.out.println("MonteCarloCPU : 2");
				int count = scan.nextInt();
				player = "White";
				if(i == 0){
					B_CPU = count;
				}else{
					W_CPU = count;
				}
			}
			*/
			// //文字出力
			System.out.println("Please input roop count");
			//繰り返し回数読み込み
			int count = scan.nextInt();
			//int count = 1;
			
			MainPanel panel = new MainPanel(count);
	}

//Mainクラス
	public static void main(String[] args){
		//文字出力
		System.out.println("Please select use GUI");
		System.out.println("Not need GUI : 1");
		System.out.println("Need GUI : 2");
		//値取得
		Scanner scan = new Scanner(System.in);
		int val = scan.nextInt();
		//int val = 1;
		
		if (val == 1) {
			new MainApp().mainWithoutGUI();
		} else if (val == 2) {
			new MainApp().mainWithGUI();
		}else{
			System.out.println("Unspecified character was entered");
		}
	}
}

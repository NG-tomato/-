//App作成のMainクラス

//Java の GUI ツールキットである AWT を拡張したもの
import javax.swing.*;

//Java で GUIアプリケーションを作成するためのクラスライブラリ(java.awt)から、Containerクラスを格納するためにContainerをimport
import java.awt.Container;

public class MainApp {

	public MainApp(){
		MainPanel panel = new MainPanel();
	}
	
//Mainクラス
	public static void main(String[] args){
		//MainAppクラスを呼び出し、画面を作成
		MainApp app = new MainApp();
		
	}
	
}
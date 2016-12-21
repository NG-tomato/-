//App作成のMainクラス

//Java の GUI ツールキットである AWT を拡張したもの
import javax.swing.*;

//Java で GUIアプリケーションを作成するためのクラスライブラリ(java.awt)から、Containerクラスを格納するためにContainerをimport
import java.awt.Container;

//JFrameを継承することでいろいろ省略
public class MainApp extends JFrame {

	public MainApp(){
		//フレームのタイトル（上に表示されるやつ）を決める
		setTitle("Othello");
		//×印で処理を終了
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		パネルを作成
		パネルの内容を決めるMainPanelクラスを呼び出して作成する
		
		#パネル ボタンやラベルなどの他のコンポーネントを貼り付けたり、レイアウトを設定することができる
		↓
		いくつかのコンポーネントをまとめてレイアウトを設定したい場合などに利用する
		
		#コンポーネント 他のプログラムから呼び出されたり連結されたりして使用されるプログラム部品
		*/
		MainPanel panel = new MainPanel();
		
		/*
		JFrameクラスのオブジェクトからContentPaneを取り出す
		
		#ContentPane ボタンなどの表示を行うコンポーネントを追加して表示させる場所
		*/
		Container contentPane = getContentPane();
		
		/*
		コンポーネントの追加
		上で作成したパネルを追加
		*/
		contentPane.add(panel);
		
		//内部の領域からサイズを決める
		pack();
	}
	
//Mainクラス
	public static void main(String[] args){
		//MainAppクラスを呼び出し、画面を作成
		MainApp app = new MainApp();
		//フレームを表示させる(デフォルトでは表示されないようになっている)
		app.setVisible(true);
		
	}
	
}
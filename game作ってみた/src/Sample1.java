import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;

//import test.Move_ball;

class Sample1 extends JPanel{
  public static void main(String args[]){
	  JFrame frame = new JFrame("てすと");
	  /*
	  Sample1 app = new Sample1();
	  frame.getContentPane().add(app);
  	*/
    /*
  	setBounds(int x, int y, int width, int height)
  	パラメータ:
  	x		このコンポーネントの新しい x 座標(nullだと中央)
  	y		このコンポーネントの新しい y 座標(nullだと中央)
  	width	このコンポーネントの新しい width
  	height	このコンポーネントの新しい height
  	*/
  	frame.setBounds(100, 50, 1000, 700);
  	//☓でプログラムを終了
  	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  	
  	//Windowに表示
  	//JFrameよりContentPaneを取得
  	Container contentPane = frame.getContentPane();
  	//ラベルのインスタンスを作成
  	JLabel label = new JLabel("Hellow Window");
  	//ラベルをContentPaneに配置
  	contentPane.add(label, BorderLayout.CENTER);
  	//位置を中央へ(横軸)
  	label.setHorizontalAlignment(SwingConstants.CENTER);
  	
  	
  	//ボタンのインスタンスを生成
  	JButton button = new JButton("test_Button");
  	//ボタンをContentPaneに配置
  	contentPane.add(button, BorderLayout.SOUTH);
  	
  	//frameの表示
  	frame.setVisible(true);

  }
  
//ウィンドウの初期設定
  public void paintComponent(Graphics g){
	Graphics2D g2 = (Graphics2D)g;
	g2.setBackground(Color.WHITE);
	g2.clearRect(0, 0, getWidth(), getHeight());
	}
  /*
  public void draw_ball(){
	Move_ball ball1 = new Move_ball(500,600);
	ball1.Moving();
  }
	*/
}
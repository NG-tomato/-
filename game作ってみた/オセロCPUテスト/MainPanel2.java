//表示させるパネルを作成するクラス

//Java の GUI ツールキットである AWT を拡張したもの
import javax.swing.*;

//Java で GUIアプリケーションを作成するためのクラスライブラリ
import java.awt.*;

//AWT コンポーネントによってトリガー（起動）されるさまざまな種類のイベントを処理するインタフェースとクラスを提供する
import java.awt.event.*;

//共通の処理のメソッド(日時機能、国際化、乱数ジェネレータ)を集めたクラス（ユーティリティクラス）
import java.util.*;

/*
パネル作成を行うクラスを継承
implements で受け取るイベントを実装する
implementsの場合、インターフェイスで定義されたメソッドをすべて実装する必要がある(ないとエラーになる)ため、共通の規格として扱える
MouseListener はマウスイベントを受け取るクラス
Observer はあるオブジェクトの変化をそれに依存するオブジェクトに知らせるクラス
*/
public class MainPanel2 extends JPanel implements MouseListener, Observer{
	
	//マス1つごとの大きさ
	static final int SIZE = 50;
	
	//置けるマス目(縦横同じ)の数（偶数のみ）
	//サイズ変更できるようにした
	static final int Squares = 8;
	
	//マス目全体の横の大きさ
	static final int W = SIZE * Squares;
	//マス目全体の縦の大きさ
	static final int H = SIZE * Squares;
	
	//状態を表すクラスstateを作成
	GameState state = new GameState(Squares);
	
	//ランダムで打つAIのクラスRandomCPUを作成
	//white
	mcCPU w_cpu = new mcCPU(-1,Squares);
	//black
	RandomCPU b_cpu = new RandomCPU(1, Squares);
	
	//メインパネルを作成するメソッド
	public MainPanel2(){
		
		/*
		パネルのサイズを設定する
		Dimension はサイズを表すクラスで、Dimension(横の大きさ,縦の大きさ)と表す
		*/
		setPreferredSize(new Dimension(W,H));
		
		/*
		マウスイベントの処理を追加する
		addMouseListener(MouseLisner l);
		lはマウスイベントを受け取るクラスで、nullの場合は例外はスローされず、処理も実行されない(つまり指定しないのと同じ)
		thisの場合は、自分自身のクラスで受け取る(addMouseListenerクラスが受け取って処理する)
		*/
		addMouseListener(this);
		
		/*
		監視されるオブジェクトを指定するクラス
		監視されるオブジェクトのデータを表している
		update メソッドを呼び出すことで、Observable の notifyObservers メソッド(監視しているメソッド)に変更を通知する
		
		addObserver(Observer o)
		オブジェクトのオブザーバセットにオブザーバ(監視対象)を追加する
		状態を表すクラスstateがObservableを継承して作られているため、これが監視対象になる
		*/
		state.addObserver(this);
	}
	
	/*
	描写を行うメソッド
	JComponentクラスで定義されているメソッドでコンポーネントの描画が必要になった時に内部的に呼び出される
	JPanelを継承したクラスなので、そのまま記述することで描写できる
	
	Graphics オブジェクトは、Java がサポートする基本的な描画操作に必要な状態情報をカプセル化したもの
	描画対象のコンポーネント、色、フォントなどを保持する
	*/
	public void paintComponent(Graphics g){
		
		//背景
		//色指定 setColor(Color c)  これ以降に描写するものの色を指定する
		g.setColor(Color.LIGHT_GRAY);
		
		//塗りつぶし fillRect(開始点ｘ座標, 開始点ｙ座標, 終了点x座標, 終了点y座標)
		g.fillRect(0, 0, W, H);
		
		//線
		//色指定
		g.setColor(Color.BLACK);
		
		/*
		升目を示す線を書く
		正方形で縦横同じ数なので、同じfor文の中で表示させる
		左上のほうから順番に記述していく
		*/
		for(int i = 0; i<Squares; i++){
			//drawLine(x1, y1, x2, y2);
			//点(x1, y1)と点(x2, y2)を線分で結ぶ
			g.drawLine(0, i*SIZE, W, i*SIZE);
			g.drawLine(i*SIZE, 0, i*SIZE, H);
		}
		
		//ボード上の3升目ぐらいに入ってる色の違う線（あってもなくてもゲーム性は変わらない）
		//g.setColor(Color.DARK_GRAY);
		//g.drawRect(SIZE*2, SIZE*2, SIZE*4, SIZE*4);
		
		//駒
		//ゲームの状態を示すstateクラスのdataメソッドを左上から順番に探索していくことで、駒が置いてある場所と黒か白かを検知する
		for(int y=1; y < Squares + 2; y++){
			for(int x=1; x < Squares + 2; x++){
				if(state.data[x][y] == 1){
					//黒の駒を表示
					g.setColor(Color.BLACK);
					/*
					円を塗りつぶす
					fillOval(x, y, w, h);
					点(x, y)を左上隅とする幅w、高さhの長方形に内接する楕円を描く
					*/
					g.fillOval((x-1)*SIZE, (y-1)*SIZE, SIZE, SIZE);
				}else if(state.data[x][y] == -1){
					//白の駒を表示
					g.setColor(Color.WHITE);
					g.fillOval((x-1)*SIZE, (y-1)*SIZE, SIZE, SIZE);
				}
			}
		}
		
		//データ表示
		g.setColor(Color.RED);
		/*
		点 (x, y) を左下隅とする長方形領域に文字列を描く
		drawString("文字列", x, y);
		点(x, y)を左下隅とする長方形領域に文字列を描く
		
		文字サイズやフォントを指定したい場合は以下のように記述
		Font font = new Font("Arrival", Font.BOLD, 30);
		Font(フォント名、フォントスタイル、フォントのサイズ)
		g.setFont(font);
		*/
		
		g.drawString("TURN = "+state.turn, 10, 30);
		g.drawString("PLAYER = "+state.player, 10, 50);
		g.drawString("DISC = "+state.black+" : " +state.white, 10, 70);
		
	}
	
	/*
	監視するObservarの更新を通知するクラス
	update(Observable o, Object arg);
	Update(監視可能なオブジェクト, 通知されるクラスに渡す引数)
	*/
	public void update(Observable o, Object arg){
		/*
		再描写イベントが発生した時に呼び出されるメソッド。updateメソッドを呼び出す。
		呼びだされたupdateメソッドは画面をクリアした後にpaintメソッドを呼び出し、再描写を行う。
		repaint();
		
		データの内容が変化しているので、再描写するだけで描写内容も反映される
		*/
		repaint();
	}
	
	//コンポーネント上でマウスボタンが押されると呼び出されるクラス
	public void mousePressed(MouseEvent e){
		
		
		//CPUのターン
		//Black
		if(state.player == b_cpu.color){
			//cpu内のdecideメソッドで置く場所を決定
			int b_action[] = b_cpu.decide(state);
			
			//置ける場所がある場合のみ駒を置く処理をする
			if(b_action[0] != -1)
				state.put(b_action[0], b_action[1]);
			/*
			//盤面が埋まったら終了
			if(state.turn == (Squares * Squares) - 4){
				JOptionPane.showMessageDialog(this, "End!");
			}
			*/
			/*
			//パスチェック
			else if( state.checkPass() == true ){
				//パスなので手番切り替え
				state.player *= -1;
				if(state.checkPass() == true){
					JOptionPane.showMessageDialog(this, "End!");
					return;
				}
				JOptionPane.showMessageDialog(this, "Pass! Next turn is "+state.turn);
			}
			*/
		}
		else if(state.player == w_cpu.color){
			//cpu内のdecideメソッドで置く場所を決定
			int w_action[] = w_cpu.decide(state);
			
			//置ける場所がある場合のみ駒を置く処理をする
			if(w_action[0] != -1)
				state.put(w_action[0], w_action[1]);
		}
		/*
		//盤面が埋まったら終了
		else if(state.turn == (Squares * Squares) - 4){
				JOptionPane.showMessageDialog(this, "End!");
		}
		*/
		/*
		パスチェック
		盤面が埋まったら両方のプレイヤが置けずパスするので，この処理のみでも問題ない
		*/
		if( state.checkPass() == true ){
			state.player *= -1;
			//両方パスだと終了
			if(state.checkPass() == true){
				int End = state.Win();
				String Winner;
				if(End == 1){
					Winner = "black";
				}else if(End == 1){
					Winner = "white";
				}else {
					Winner = "Drow";
				}

				JOptionPane.showMessageDialog(this, "End! " + Winner + " Win !");
				return;
			}
			JOptionPane.showMessageDialog(this, "Pass! Next turn is "+state.turn);
		}
		
	}
	
	public void mouseClicked(MouseEvent e){

	}
	public void mouseReleased(MouseEvent e){

	}
	public void mouseEntered(MouseEvent e){

	}
	public void mouseExited(MouseEvent e){

	}
}
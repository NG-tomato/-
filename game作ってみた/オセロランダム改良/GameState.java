import java.util.*;
import java.util.Arrays;

//Observableを継承することで、updateメソッドなどの監視対象となるクラスとなる
public class GameState extends Observable{
//public class GameState{

	/*
	状態として保持するデータ
	data 現在の駒が置かれている場所を保持する縦と横の多次元配列
		0が置かれていない場所 1が黒 -1が白 を表す
	turn 何ターン目か示す変数 全部置かれた場合のターン数は60なのでその場合はそこで終了する（Mainpanel内にある）
	player どちらのターンかを示す変数
		1の場合は黒 -1の場合は白 を表すため、-1を掛けることで変更する
	black 黒の個数
	white 白の個数
	*/
	int data[];
	int turn;
	int player;
	int black;
	int white;
	int size;
	
	//最初の状態を作るメソッド
	public GameState(){
		size = 10;
		int x = size/2;
		int y = size/2;
		//初期値（真ん中の４つが交互にある状態）を作成
		//dataは1列の配列内に保存
		//位置は[x+y*10]として入れる
		data = new int[size * size];
		data[x + y * 10] = 1;
		data[x + (y - 1) * 10] = -1;
		data[x - 1 + y * 10] = -1;
		data[x -1 + (y - 1) * 10] = 1;
		//壁を作成
		for(int i = 0;i < size - 1; i++){
			data[i * 10] = 2;
			data[size - 1 + (i + 1) * 10] = 2;
			data[i + 1] = 2;
			data[i + (size - 1) * 10] = 2;
		}
		turn = 0;
		player = 1;
		black = 2;
		white = 2;
	}
		
	/*
	駒を置く処理を作成
	(x,y)で置く位置を取得し、置けるかどうかをtrueかfalseで返す
	*/
	public boolean put(int x, int y){
		//すでに駒があるところには置けない
		if(data[x + y * 10] != 0){
			return false;
		}
		//リバースできないところには置けない
		//reverseメソッドを利用することで確かめる
		if(reverse(x,y,true)==false){
			return false;
		}
		
		//駒を置く
		data[x + y * 10] = player;
		player *= -1;
		turn++;
		countDisc();
		
		setChanged();
		notifyObservers();
		
		return true;
	}
	
	/*
	置けるかどうか確かめる変数
	x,y 確かめる位置の座標
	doReverse 実際に置き換えるかどうか(trueだと置き換える、falseだと置き換えない)
	*/
	public boolean reverse(int x,int y, boolean doReverse ){
		/*
		確かめる方向を指定する配列
		{x,y} 括弧ごとに2つの数をもち、その関係で方向が決まる(真ん中が(x,y)の座標だとして1ずつ増えていく数にその方向のものを掛けることでその方向の駒を走査できる)
		*/
		int dir[][] = {
				{-1,-1}, {0,-1}, {1,-1},
				{-1, 0},         {1, 0},
				{-1, 1}, {0, 1}, {1, 1}
		};
		
		/*
		置けるか確かめた結果を入れる変数
		最初に置けないと設定することで、複数の処理の中でtrueに変化しない場合は置けないという形にすることができる。
		*/
		boolean reversed = false;
		
		//for文で配列dirを切り替えることで確かめる方向を変更していく
		for(int i=0; i<8; i++){
			//確かめる方向にある隣のマスを指定
			int x0 = x+dir[i][0];
			int y0 = y+dir[i][1];
			
			/*
			//確かめるマスがマスの範囲外である場合その方向の走査を終了させて次の方向へ移る
			if(isOut(x0,y0) == true){
				continue;
			}
			*/
			
			//隣のマスの値を取得
			int nextState =data[x0 + y0 * 10];
			
			//隣のマスの駒が現在のturnのplayerの駒である場合、その方向の走査を終了して次の方向へ移る
			if(nextState == player){
				//走査した方向の隣の駒を表示する
				//System.out.println("Next state is player: " +x0 +","+ y0);
				continue;
			}
			//隣のマスに何も置かれていない場合または壁の場合、その方向の走査を終了して次の方向へ移る
			else if(nextState == 0 || nextState == 2){
				//System.out.println("Next state is null: " +x0 +","+ y0);
				continue;
			}
			//それ以外(隣のマスが現在のturnのplayerの駒でない駒の場合)、その方向の走査を続ける
			/*
			else{
				//System.out.println("Next state is enemy: " +x0 +","+ y0);
			}
			*/
			
			//隣の隣から端まで走査して、自分の色があればリバース
			
			//確かめる駒と駒を置く位置との距離を入れる変数
			int j = 2;
			
			while(true){
				//変数jと方向の関数を掛けることで、駒を置く位置に対しての座標が分かるため、それを駒を置く座標に対して加えることで、もとの状態における位置を指定する
				int x1 = x + (dir[i][0]*j);
				int y1 = y + (dir[i][1]*j);
				
				/*
				//確かめるマスがマスの範囲外である場合その方向の走査を終了させて次の方向へ移る
				if(isOut(x1,y1) == true){
					break;
				}
				*/
				
				//自分の駒がある場合
				if(data[x1 + y1 * 10]==player){
					//System.out.println("Player cell!: " +x1 +","+ y1);
					
					//doReverseがtrueの場合（置き換える処理の場合）置き換える
					if(doReverse){
						
						//自分の駒があった位置と駒を置く位置の間の駒を置き換える
						//1からj-1まで順に増やしていくことで、2つの距離間にあるマスを指定できる
						for(int k=1; k<j; k++){
							//マス指定
							int x2 = x + (dir[i][0]*k);
							int y2 = y + (dir[i][1]*k);
							//駒の変更
							data[x2 + y2 * 10] *= -1;
							//置き換わった駒を表示する
							//System.out.println("reverse: " +x2 +","+ y2);
						}
					}
					//置くことにより変わる駒があるので、置けるマスがあると判断し、reversed変数をtrueにする
					reversed = true;
					//この方向の走査は終わったので、whileを終了する
					break;
				}
				//空白があるまたは壁に到達したら終了
				if(data[x1 + y1 * 10] == 2 || data[x1 + y1 * 10] == 0){
					break;
				}
				
				j++;
				
			}
			
		}
		
		return reversed;
	}
	
	
	//その位置に置けるかどうかを判定するメソッド
	public boolean canReverse(int x, int y){
		return reverse(x, y, false);
	}
	
	//指定されたマスがマス内か確かめるメソッド
	public boolean isOut(int x, int y){
		if(x<0 || y<0 || x>=size || y>=size){
			return true;
		}
		return false;
	}
	
	//パスかどうかを判定するメソッド
	public boolean checkPass(){
		
		//コピーデータの全升目に対して、リバースできるかチェック
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
				//すでに駒があるところはチェックせずスキップする
				if(data[x + y * 10] != 0){
					continue;
				}
				
				//リバースできる（した）とき、元に戻してfalseを返す
				//canReverseメソッドを利用して確かめる
				if(canReverse(x,y) == true){
					//置ける場所がある場合、パスできないのでfalseを返す
					return false;
				}
				
			}
		}
		//置ける場所がないのでtrueを返す
		return true;
	}
	
	//それぞれの駒の枚数を数えるメソッド
	public void countDisc(){
		
		black = 0;
		white = 0;
		
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				if(data[x + y * 10] == 1){
					black++;
				}else if(data[x + y * 10] == -1){
					white++;
				}
			}
		}
	}
	public int Win(){
		if(black > white){
			return 1;
		}else if(black < white){
			return -1;
		}else{
			return 0;
		}
	}
	
	//初期状態に戻すメソッド
	public void reset(){
		size = 10;
		int x = size/2;
		int y = size/2;
		//初期値（真ん中の４つが交互にある状態）を作成
		//dataは1列の配列内に保存
		//位置は[x+y*10]として入れる
		data = new int[size * size];
		data[x + y * 10] = 1;
		data[x + (y - 1) * 10] = -1;
		data[x - 1 + y * 10] = -1;
		data[x -1 + (y-1) * 10] = 1;
		//壁を作成
		for(int i = 0;i < size - 1; i++){
			data[i * 10] = 2;
			data[size - 1 + (i + 1) * 10] = 2;
			data[i + 1] = 2;
			data[i + (size - 1) * 10] = 2;
		}
		turn = 0;
		player = 1;
		black = 2;
		white = 2;
	}
	
	public void set(int[] d, int t, int p){
		data = Arrays.copyOf(d ,d.length);
		
		turn = t;
		player = p;
		countDisc();
	}

	//描写を行うメソッド
	public void textDisplay(){
		//左上から順にマスと駒を表示
		for(int y=1; y<=8; y++){
			for(int x=1; x<=8; x++){
				System.out.print("|");
				switch (data[x + y * 10]) {
				case 1:
					System.out.print("○");					//黒の駒を表示
					break;
				case -1:
					System.out.print("●");					//白の駒を表示
					break;
				default:
					System.out.print("  ");
				}
			}
			System.out.println("|");
		}
		System.out.println("TURN = "+turn);
		System.out.println("PLAYER = "+player);
		System.out.println("DISC = "+black+" : " +white);
		System.out.println("");
	}

	
}

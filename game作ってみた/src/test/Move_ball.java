package test;


public class Move_ball {
	int hight;//ウィンドウの高さを保存する変数
	int side;//ウィンドウの幅を保存する変数
	int point = 400;//現在のボールの位置を保存する変数
	int speed;//現在のボールのスピードを保存する変数
	public Move_ball(int h, int s){
		hight = h;
		side = s;
		point = h/2;
	}
	
	public int Moving(){
		speed += 10;
		point -= speed;
		if(point <= 50)
			speed = - speed;
		return point;
	}
}

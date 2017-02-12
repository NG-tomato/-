import java.util.*;
import java.util.Arrays;

//mct�ŒT�����̏�Ԃ�ۑ�����N���X
public class mctGameState{
	final static int SIZE = 10;
	final static int BLACK = 1;
	final static int WHITE = -1;
	final static int WALL = 2;
	//x,y�̍��W��x+y*SIZE�̒l�ɕϊ�����֐�
	static int at(int x, int y) {
		return x + y * SIZE;
	}
	//�΂�u�����Ƃ��ɐ΂̎�ނ��m���߂���W��Ԃ��֐�
	//atDir�i�΂�������x���W�C�΂�������y���W�C���݂ǂ̊p�x�̒T�����s���Ă��邩�������ϐ��C���ʒu����̋���)
	static int atDir(int x, int y, int dirID, int len) {
		return at(x + dir[dirID][0] * len,
							y + dir[dirID][1] * len);
	}

	/*
		�m���߂�������w�肷��z��
		{x,y} ���ʂ��Ƃ�2�̐��������A���̊֌W�ŕ��������܂�(�^�񒆂�(x,y)�̍��W���Ƃ���1�������Ă������ɂ��̕����̂��̂��|���邱�Ƃł��̕����̐΂𑖍��ł���)
	*/
	final static int dir[][] = {
				{-1,-1}, {0,-1}, {1,-1},
				{-1, 0},         {1, 0},
				{-1, 1}, {0, 1}, {1, 1}
	};

	/*
	��ԂƂ��ĕێ�����f�[�^
	data ���݂̐΂��u����Ă���ꏊ��ێ�����c�Ɖ��̑������z��
		0���u����Ă��Ȃ��ꏊ 1���� -1���� ��\��
	turn ���^�[���ڂ������ϐ� �S���u���ꂽ�ꍇ�̃^�[������60�Ȃ̂ł��̏ꍇ�͂����ŏI������iMainpanel���ɂ���j
	player �ǂ���̃^�[�����������ϐ�
		1�̏ꍇ�͍� -1�̏ꍇ�͔� ��\�����߁A-1���|���邱�ƂŕύX����
	black ���̌�
	white ���̌�
	zobhash ���݂̔Ֆʂ̃]�u���X�g�n�b�V���̒l
	isLastPass �O�̎�ԂŃp�X���s�������ǂ����̒l�i���ꂪtrue�̂Ƃ��Ƀp�X����ƃQ�[���I���j
	*/
	int data[];
	int turn;
	int player;
	int black;
	int white;
	int zobhash;
	boolean isLastPass;
	
	//�ŏ��̏�Ԃ���郁�\�b�h
	public mctGameState(){
		reset();
	}
	
	//����̃R�s�[��mctGameState����郁�\�b�h
	public mctGameState clone() {
		mctGameState other = new mctGameState();
		other.data	  = Arrays.copyOf(data, data.length);
		other.turn		= turn;
		other.player	= player;
		other.black		= black;
		other.white		= white;
		other.zobhash	= zobhash;
		other.isLastPass = isLastPass;
		return other;
	}

	/*
	�΂�u���������쐬
	(x,y)�Œu���ʒu���擾���A�u���邩�ǂ�����true��false�ŕԂ�
	*/
	public boolean put(int x, int y){
		//���łɐ΂�����Ƃ���ɂ͒u���Ȃ�
		if(data[at(x, y)] != 0) return false;

		//���o�[�X�ł��Ȃ��Ƃ���ɂ͒u���Ȃ�
		if (!canPut(x, y)) return false;

		//�΂�u��
		reverse(x, y);
		data[x + y * 10] = player; if (player == BLACK) black++; else white++;
		zobhash = Zobrist.put(zobhash, at(x, y), player);

		// ���i�߂�
		player *= -1;
		zobhash = Zobrist.color(zobhash);
		turn++;
		isLastPass = false;
		return true;
	}
	
	//�p�X�̂Ƃ��̏���
	public void pass(){
		player *= -1;
		zobhash = Zobrist.color(zobhash);
		isLastPass = true;
	}
	
	//���̈ʒu�ɒu���邩�ǂ����𔻒肷�郁�\�b�h
	public boolean canPut(int x, int y){
		//for���Ŕz��dir��؂�ւ��邱�ƂŊm���߂������ύX���Ă���
		if (data[at(x, y)] != 0) return false;
		for(int i=0; i<8; i++){
			//�ׂ̃}�X�̐΂������u����Ă��Ȃ��ꍇ�܂��͕ǂ̏ꍇ�A���݂�turn��player�̐΂ł���ꍇ�A���̕����̑������I�����Ď��̕����ֈڂ�
			int next = data[atDir(x, y, i, 1)];
			if(next == player || next == 0 || next == 2){
				continue;
			}

			//�ׂׂ̗���[�܂ő������āA
			for (int len = 2; ; len++) {
				int over = data[atDir(x, y, i, len)];
				if (over == 2 || over == 0) { //�󔒂�����܂��͕ǂɓ��B������I��
					break;
				}
				if (over == -player) {
					continue; // �X�L�b�v
				}

				// �����̐F������΂悢
				return true;
			}
		}
		return false;
	}
	/*
	  �΂�u��
		x,y �u���ʒu
	*/
	public void reverse(int x,int y){
		int reverseCount = 0;
		
		//for���Ŕz��dir��؂�ւ��邱�ƂŊm���߂������ύX���Ă���
		for(int i=0; i<8; i++){
			//�m���߂�����ɂ���ׂ̃}�X���w��
			//�ׂ̃}�X�̐΂������u����Ă��Ȃ��ꍇ�܂��͕ǂ̏ꍇ�A���݂�turn��player�̐΂ł���ꍇ�A���̕����̑������I�����Ď��̕����ֈڂ�
			int next = data[atDir(x, y, i, 1)];
			if(next == player || next == 0 || next == 2){
				continue;
			}
			
			//�ׂׂ̗���[�܂ő������āA
			for (int len = 2; ; len++) {
				int over = data[atDir(x, y, i, len)];
				if (over == 2 || over == 0) { //�󔒂�����܂��͕ǂɓ��B������I��
					break;
				}
				if (over == -player) {
					continue; // �X�L�b�v
				}

				// �����̐F������� �Ԃ̐΂����ւ���
				for (int k = 1; k < len; k++) {
					int betweenPos = atDir(x, y, i, k);
					data[betweenPos] *= -1; // ���]
					reverseCount++;
					//zobrist��put���\�b�h���g���Đ΂�ύX����
					zobhash = Zobrist.reverse(zobhash, betweenPos, player);
				}

				break;
			}
		}

		// ���]�����������΂̐��𒲐�
		if (player == BLACK) {
			black += reverseCount; white -= reverseCount;
		} else {
			black -= reverseCount; white += reverseCount;
		}
	}
	
	
	//�w�肳�ꂽ�}�X���}�X�����m���߂郁�\�b�h
	public static boolean isOut(int x, int y){
		if(x<0 || y<0 || x>=SIZE || y>=SIZE){
			return true;
		}
		return false;
	}
	
	public void set(int[] d, int t, int p){
		data = Arrays.copyOf(d ,d.length);
		
		turn = t;
		player = p;
		countDisc();
		zobhash = Zobrist.makeZob(data, player);
	}

	//�p�X���ǂ����𔻒肷�郁�\�b�h
	public boolean checkPass(){
		//�R�s�[�f�[�^�̑S���ڂɑ΂��āA���o�[�X�ł��邩�`�F�b�N
		for(int x=1; x<SIZE-1; x++){
			for(int y=1; y<SIZE-1; y++){
				if(data[at(x, y)] != 0) continue; //���łɐ΂�����Ƃ���܂��͕ǂ̂Ƃ���̓`�F�b�N�����X�L�b�v���� 

				//���o�[�X�ł���Ƃ��Afalse��Ԃ�
				if(canPut(x,y)){
					return false;
				}
			}
		}

		//�u����ꏊ���Ȃ��̂�true��Ԃ�
		return true;
	}
	
	//���ꂼ��̐΂̖����𐔂��郁�\�b�h
	public void countDisc(){
		black = 0; white = 0;
		for(int y=1; y<SIZE-1; y++){
			for(int x=1; x<SIZE-1; x++){
				switch (data[at(x, y)]) {
				case BLACK:
					black++;
					break;
				case WHITE:
					white++;
					break;
				default:
					// do nothing
				}
			}
		}
	}
	
	public int Win(){
		countDisc();
		if(black > white){
			return 1;
		}else if(black < white){
			return -1;
		}else{
			return 0;
		}
	}
	
	//������Ԃɖ߂����\�b�h
	public void reset(){
		//�����l�i�^�񒆂̂S�����݂ɂ����ԁj���쐬
		//data��1��̔z����ɕۑ�
		//�ʒu��[x+y*10]�Ƃ��ē����
		data = new int[SIZE * SIZE]; black = 0; white = 0;
		//�ǂ��쐬
		for(int i = 0;i < SIZE; i++){
			data[at(0,      i)] = WALL;
			data[at(SIZE-1, i)] = WALL;
			data[at(i, 0     )] = WALL;
			data[at(i, SIZE-1)] = WALL;
		}
		data[at(SIZE/2  , SIZE/2  )] = BLACK; black++;
		data[at(SIZE/2  , SIZE/2-1)] = WHITE; white++;
		data[at(SIZE/2-1, SIZE/2  )] = WHITE; white++;
		data[at(SIZE/2-1, SIZE/2-1)] = BLACK; black++;

		turn = 0;
		player = BLACK;

		//����Ԃł̃]�u���X�g�n�b�V���̒l�����
		zobhash = Zobrist.makeZob(data, player);
	}
	
	//����܂őł��ꂽ��̖�����Ԃ��ϐ�
	public int putNumber(){
		return black + white - 4;
	}
	
	/*
	//�`�ʂ��s�����\�b�h
	public void textDisplay(){
		//���ォ�珇�Ƀ}�X�Ƌ��\��
		for(int y=1; y<=8; y++){
			for(int x=1; x<=8; x++){
				System.out.print("|");
				switch (data[x + y * 10]) {
				case 1:
					System.out.print("��");					//���̋��\��
					break;
				case -1:
					System.out.print("��");					//���̋��\��
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
	*/
}

import java.util.*;
import java.util.Arrays;

//Observable���p�����邱�ƂŁAupdate���\�b�h�Ȃǂ̊Ď��ΏۂƂȂ�N���X�ƂȂ�
public class GameState extends Observable{
	final static int SIZE = 10;
	final static int BLACK = 1;
	final static int WHITE = -1;
	final static int WALL = 2;
	//x,y�̍��W��x+y*SIZE�̒l�ɕϊ�����֐�
	static int at(int x, int y) {
		return x + y * SIZE;
	}
	
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
	data ���݂̋�u����Ă���ꏊ��ێ�����c�Ɖ��̑������z��
		0���u����Ă��Ȃ��ꏊ 1���� -1���� ��\��
	turn ���^�[���ڂ������ϐ� �S���u���ꂽ�ꍇ�̃^�[������60�Ȃ̂ł��̏ꍇ�͂����ŏI������iMainpanel���ɂ���j
	player �ǂ���̃^�[�����������ϐ�
		1�̏ꍇ�͍� -1�̏ꍇ�͔� ��\�����߁A-1���|���邱�ƂŕύX����
	black ���̌�
	white ���̌�
	*/
	int data[];
	int turn;
	int player;
	int black;
	int white;
	boolean isLastPass;
	
	//�ŏ��̏�Ԃ���郁�\�b�h
	public GameState(){
		reset();
	}
	
	//����̃R�s�[��GameState����郁�\�b�h
	public GameState clone() {
		GameState other = new GameState();
		other.data	  = Arrays.copyOf(data, data.length);
		other.turn		= turn;
		other.player	= player;
		other.black		= black;
		other.white		= white;
		other.isLastPass = isLastPass;
		return other;
	}

	/*
	���u���������쐬
	(x,y)�Œu���ʒu���擾���A�u���邩�ǂ�����true��false�ŕԂ�
	*/
	public boolean put(int x, int y){
		//���łɋ����Ƃ���ɂ͒u���Ȃ�
		if(data[at(x,y)] != 0)return false;
		
		//���o�[�X�ł��Ȃ��Ƃ���ɂ͒u���Ȃ�
		//reverse���\�b�h�𗘗p���邱�ƂŊm���߂�
		if(reverse(x,y,true)==false){
			return false;
		}
		
		//���u��
		data[at(x, y)] = player;
		player *= -1;
		turn++;
		countDisc();
		
		setChanged();
		notifyObservers();
		isLastPass = false;
		
		return true;
	}
	
	/*
	�u���邩�ǂ����m���߂�ϐ�
	x,y �m���߂�ʒu�̍��W
	doReverse ���ۂɒu�������邩�ǂ���(true���ƒu��������Afalse���ƒu�������Ȃ�)
	*/
	public boolean reverse(int x,int y, boolean doReverse ){
		//���łɋ����Ƃ���ɂ͒u���Ȃ�
		if(data[at(x,y)] != 0)return false;
		
		/*
		�u���邩�m���߂����ʂ�����ϐ�
		�ŏ��ɒu���Ȃ��Ɛݒ肷�邱�ƂŁA�����̏����̒���true�ɕω����Ȃ��ꍇ�͒u���Ȃ��Ƃ����`�ɂ��邱�Ƃ��ł���B
		*/
		boolean reversed = false;
		
		//for���Ŕz��dir��؂�ւ��邱�ƂŊm���߂������ύX���Ă���
		for(int i=0; i<8; i++){
						
			//�ׂ̃}�X�̒l���擾
			int nextState =data[atDir(x, y, i, 1)];
			
			//�ׂ̃}�X�̋���݂�turn��player�̋�ł���ꍇ�A���̕����̑������I�����Ď��̕����ֈڂ�
			if(nextState == player){
				continue;
			}
			//�ׂ̃}�X�ɉ����u����Ă��Ȃ��ꍇ�܂��͕ǂ̏ꍇ�A���̕����̑������I�����Ď��̕����ֈڂ�
			else if(nextState == 0 || nextState == 2){
				continue;
			}
			
			//�ׂׂ̗���[�܂ő������āA�����̐F������΃��o�[�X����
			//�m���߂��Ƌ��u���ʒu�Ƃ̋���������ϐ�
			int j = 2;
			while(true){
								
				//�����̋����ꍇ
				if(data[atDir(x, y, i, j)]==player){
					
					//doReverse��true�̏ꍇ�i�u�������鏈���̏ꍇ�j�u��������
					if(doReverse){
						
						//�����̋�������ʒu�Ƌ��u���ʒu�̊Ԃ̋��u��������
						//1����j-1�܂ŏ��ɑ��₵�Ă������ƂŁA2�̋����Ԃɂ���}�X���w��ł���
						for(int k=1; k<j; k++){
							//��̕ύX
							data[atDir(x, y, i, k)] *= -1;
						}
					}
					//�u�����Ƃɂ��ς������̂ŁA�u����}�X������Ɣ��f���Areversed�ϐ���true�ɂ���
					reversed = true;
					//���̕����̑����͏I������̂ŁAwhile���I������
					break;
				}
				//�󔒂�����܂��͕ǂɓ��B������I��
				if(data[atDir(x, y, i, j)] == 2 || data[atDir(x, y, i, j)] == 0){
					break;
				}
				
				j++;
				
			}
			
		}
		
		return reversed;
	}
	
	
	//���̈ʒu�ɒu���邩�ǂ����𔻒肷�郁�\�b�h
	public boolean canReverse(int x, int y){
		return reverse(x, y, false);
	}
	
	//�p�X���ǂ����𔻒肷�郁�\�b�h
	public boolean checkPass(){
		
		//�R�s�[�f�[�^�̑S���ڂɑ΂��āA���o�[�X�ł��邩�`�F�b�N
		for(int y=1; y<SIZE; y++){
			for(int x=1; x<SIZE; x++){
				
				//���łɋ����Ƃ���̓`�F�b�N�����X�L�b�v����
				if(data[at(x, y)] != 0){
					continue;
				}
				
				//���o�[�X�ł���i�����j�Ƃ��A���ɖ߂���false��Ԃ�
				//canReverse���\�b�h�𗘗p���Ċm���߂�
				if(canReverse(x,y) == true){
					//�u����ꏊ������ꍇ�A�p�X�ł��Ȃ��̂�false��Ԃ�
					return false;
				}
				
			}
		}
		//�u����ꏊ���Ȃ��̂�true��Ԃ�
		return true;
	}
	
	//���ꂼ��̋�̖����𐔂��郁�\�b�h
	public void countDisc(){
		black = 0;white = 0;
		for(int y=1; y<SIZE-1; y++){
			for(int x=1; x<SIZE-1; x++){
				if(data[at(x, y)] == 1){
					black++;
				}else if(data[at(x, y)] == -1){
					white++;
				}
			}
		}
	}
	
	//���҂�Ԃ����\�b�h
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
		int x = SIZE/2;
		int y = SIZE/2;
		//�����l�i�^�񒆂̂S�����݂ɂ����ԁj���쐬
		//data��1��̔z����ɕۑ�
		//�ʒu��[at(x, y)]�Ƃ��ē����
		data = new int[SIZE * SIZE];
		data[at(x, y)] = 1;
		data[at(x, y - 1)] = -1;
		data[at(x - 1, y)] = -1;
		data[at(x - 1, y-1)] = 1;
		//�ǂ��쐬
		for(int i = 0;i < SIZE - 1; i++){
			data[at(0, i)] = WALL;
			data[at(SIZE - 1, i + 1)] = WALL;
			data[at(i + 1, 0)] = WALL;
			data[at(i, SIZE - 1)] = WALL;
		}
		turn = 0;
		player = 1;
		black = 2;
		white = 2;
		isLastPass = false;
	}
	
	public void pass(){
		player *= -1;
		isLastPass = true;
	}
	
	public void set(int[] d, int t, int p){
		data = Arrays.copyOf(d ,d.length);
		
		turn = t;
		player = p;
		countDisc();
	}

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

	
}

import java.util.*;
import java.util.Arrays;

//Observable���p�����邱�ƂŁAupdate���\�b�h�Ȃǂ̊Ď��ΏۂƂȂ�N���X�ƂȂ�
public class GameState extends Observable{
//public class GameState{

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
	int size;
	
	//�ŏ��̏�Ԃ���郁�\�b�h
	public GameState(){
		size = 10;
		int x = size/2;
		int y = size/2;
		//�����l�i�^�񒆂̂S�����݂ɂ����ԁj���쐬
		//data��1��̔z����ɕۑ�
		//�ʒu��[x+y*10]�Ƃ��ē����
		data = new int[size * size];
		data[x + y * 10] = 1;
		data[x + (y - 1) * 10] = -1;
		data[x - 1 + y * 10] = -1;
		data[x -1 + (y - 1) * 10] = 1;
		//�ǂ��쐬
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
	���u���������쐬
	(x,y)�Œu���ʒu���擾���A�u���邩�ǂ�����true��false�ŕԂ�
	*/
	public boolean put(int x, int y){
		//���łɋ����Ƃ���ɂ͒u���Ȃ�
		if(data[x + y * 10] != 0){
			return false;
		}
		//���o�[�X�ł��Ȃ��Ƃ���ɂ͒u���Ȃ�
		//reverse���\�b�h�𗘗p���邱�ƂŊm���߂�
		if(reverse(x,y,true)==false){
			return false;
		}
		
		//���u��
		data[x + y * 10] = player;
		player *= -1;
		turn++;
		countDisc();
		
		setChanged();
		notifyObservers();
		
		return true;
	}
	
	/*
	�u���邩�ǂ����m���߂�ϐ�
	x,y �m���߂�ʒu�̍��W
	doReverse ���ۂɒu�������邩�ǂ���(true���ƒu��������Afalse���ƒu�������Ȃ�)
	*/
	public boolean reverse(int x,int y, boolean doReverse ){
		/*
		�m���߂�������w�肷��z��
		{x,y} ���ʂ��Ƃ�2�̐��������A���̊֌W�ŕ��������܂�(�^�񒆂�(x,y)�̍��W���Ƃ���1�������Ă������ɂ��̕����̂��̂��|���邱�Ƃł��̕����̋�𑖍��ł���)
		*/
		int dir[][] = {
				{-1,-1}, {0,-1}, {1,-1},
				{-1, 0},         {1, 0},
				{-1, 1}, {0, 1}, {1, 1}
		};
		
		/*
		�u���邩�m���߂����ʂ�����ϐ�
		�ŏ��ɒu���Ȃ��Ɛݒ肷�邱�ƂŁA�����̏����̒���true�ɕω����Ȃ��ꍇ�͒u���Ȃ��Ƃ����`�ɂ��邱�Ƃ��ł���B
		*/
		boolean reversed = false;
		
		//for���Ŕz��dir��؂�ւ��邱�ƂŊm���߂������ύX���Ă���
		for(int i=0; i<8; i++){
			//�m���߂�����ɂ���ׂ̃}�X���w��
			int x0 = x+dir[i][0];
			int y0 = y+dir[i][1];
			
			/*
			//�m���߂�}�X���}�X�͈̔͊O�ł���ꍇ���̕����̑������I�������Ď��̕����ֈڂ�
			if(isOut(x0,y0) == true){
				continue;
			}
			*/
			
			//�ׂ̃}�X�̒l���擾
			int nextState =data[x0 + y0 * 10];
			
			//�ׂ̃}�X�̋���݂�turn��player�̋�ł���ꍇ�A���̕����̑������I�����Ď��̕����ֈڂ�
			if(nextState == player){
				//�������������ׂ̗̋��\������
				//System.out.println("Next state is player: " +x0 +","+ y0);
				continue;
			}
			//�ׂ̃}�X�ɉ����u����Ă��Ȃ��ꍇ�܂��͕ǂ̏ꍇ�A���̕����̑������I�����Ď��̕����ֈڂ�
			else if(nextState == 0 || nextState == 2){
				//System.out.println("Next state is null: " +x0 +","+ y0);
				continue;
			}
			//����ȊO(�ׂ̃}�X�����݂�turn��player�̋�łȂ���̏ꍇ)�A���̕����̑����𑱂���
			/*
			else{
				//System.out.println("Next state is enemy: " +x0 +","+ y0);
			}
			*/
			
			//�ׂׂ̗���[�܂ő������āA�����̐F������΃��o�[�X
			
			//�m���߂��Ƌ��u���ʒu�Ƃ̋���������ϐ�
			int j = 2;
			
			while(true){
				//�ϐ�j�ƕ����̊֐����|���邱�ƂŁA���u���ʒu�ɑ΂��Ă̍��W�������邽�߁A��������u�����W�ɑ΂��ĉ����邱�ƂŁA���Ƃ̏�Ԃɂ�����ʒu���w�肷��
				int x1 = x + (dir[i][0]*j);
				int y1 = y + (dir[i][1]*j);
				
				/*
				//�m���߂�}�X���}�X�͈̔͊O�ł���ꍇ���̕����̑������I�������Ď��̕����ֈڂ�
				if(isOut(x1,y1) == true){
					break;
				}
				*/
				
				//�����̋����ꍇ
				if(data[x1 + y1 * 10]==player){
					//System.out.println("Player cell!: " +x1 +","+ y1);
					
					//doReverse��true�̏ꍇ�i�u�������鏈���̏ꍇ�j�u��������
					if(doReverse){
						
						//�����̋�������ʒu�Ƌ��u���ʒu�̊Ԃ̋��u��������
						//1����j-1�܂ŏ��ɑ��₵�Ă������ƂŁA2�̋����Ԃɂ���}�X���w��ł���
						for(int k=1; k<j; k++){
							//�}�X�w��
							int x2 = x + (dir[i][0]*k);
							int y2 = y + (dir[i][1]*k);
							//��̕ύX
							data[x2 + y2 * 10] *= -1;
							//�u������������\������
							//System.out.println("reverse: " +x2 +","+ y2);
						}
					}
					//�u�����Ƃɂ��ς������̂ŁA�u����}�X������Ɣ��f���Areversed�ϐ���true�ɂ���
					reversed = true;
					//���̕����̑����͏I������̂ŁAwhile���I������
					break;
				}
				//�󔒂�����܂��͕ǂɓ��B������I��
				if(data[x1 + y1 * 10] == 2 || data[x1 + y1 * 10] == 0){
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
	
	//�w�肳�ꂽ�}�X���}�X�����m���߂郁�\�b�h
	public boolean isOut(int x, int y){
		if(x<0 || y<0 || x>=size || y>=size){
			return true;
		}
		return false;
	}
	
	//�p�X���ǂ����𔻒肷�郁�\�b�h
	public boolean checkPass(){
		
		//�R�s�[�f�[�^�̑S���ڂɑ΂��āA���o�[�X�ł��邩�`�F�b�N
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
				//���łɋ����Ƃ���̓`�F�b�N�����X�L�b�v����
				if(data[x + y * 10] != 0){
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
	
	//������Ԃɖ߂����\�b�h
	public void reset(){
		size = 10;
		int x = size/2;
		int y = size/2;
		//�����l�i�^�񒆂̂S�����݂ɂ����ԁj���쐬
		//data��1��̔z����ɕۑ�
		//�ʒu��[x+y*10]�Ƃ��ē����
		data = new int[size * size];
		data[x + y * 10] = 1;
		data[x + (y - 1) * 10] = -1;
		data[x - 1 + y * 10] = -1;
		data[x -1 + (y-1) * 10] = 1;
		//�ǂ��쐬
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

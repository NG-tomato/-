import java.util.*;
import java.util.Arrays;

//mct�ŒT�����̏�Ԃ�ۑ�����N���X
public class mctGameState extends GameState{
	
	/*
	��ԂƂ��ĕێ�����f�[�^
	���̃f�[�^�͌p������GameState�ɂ���
	zobhash ���݂̔Ֆʂ̃]�u���X�g�n�b�V���̒l
	isLastPass �O�̎�ԂŃp�X���s�������ǂ����̒l�i���ꂪtrue�̂Ƃ��Ƀp�X����ƃQ�[���I���j
	*/
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
	
	//�f�[�^���Z�b�g����
	//�]�u���X�g�n�b�V�����ǉ�����Ă���̂ŁC
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
		isLastPass = false;

		//����Ԃł̃]�u���X�g�n�b�V���̒l�����
		zobhash = Zobrist.makeZob(data, player);
	}
	
	//����܂őł��ꂽ��̖�����Ԃ��ϐ�
	public int putNumber(){
		return black + white - 4;
	}
	
}

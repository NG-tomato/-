import java.util.*;

public class zobrist {
	//����ꏊ�ɋ�u���ꂽ�󋵂��Ƃ̕ϐ�������z��
	int[] black = new int[8 * 8];
	int[] white = new int[8 * 8];
	
	//�����_�̃]�u���X�g�l
	private int zobrist_now = 0;
	
	//������߂�Ƃ��ɕύX����]�u���X�g�̒l
	int zobrist = 0;
	
	//����ꏊ�ɐ΂��u���ꂽ�󋵂��Ƃ̕ϐ����쐬
	public zobrist(){
		Random rnd = new Random();
		for(int i = 0;i < 8 * 8;i++){
			black[i] = rnd.nextInt();
			white[i] = rnd.nextInt();
		}
	}
	
	//���̎��_�ł̃]�u���X�g�n�b�V���̒l�����֐�
	//����Ԃ̔ՖʂŐ΂����݂��Ă���ꏊ���Ƃɂ��̒l�̔r���I�_���a���Ƃ�
	public void makeZob(int[] state, int c){
		for(int i = 0;i < 8;i++){
			for(int j = 0;j < 8;j++){
				//�΂����݂��Ă���ꍇ���̐΂��ǂ���̐΂����ʂ��Ĕr���I�_���a���Ƃ�(�ǂ���ł��Ȃ��ꍇ�͉������Ȃ�)
				//�ǂ̕������������Ȃ��悤��state�̍��W(x,y)�ɂ�+1����
				//���̏ꍇ
				if(state[(i+1) + (j+1) * 10] == 1){
					zobrist = zobrist ^ black[i + j * 8];
				}
				//���̏ꍇ
				else if(state[(i+1) + (j+1) * 10] == -1){
					zobrist = zobrist ^ white[i + j * 8];
				}
			}
		}
		//�v���C�������̏ꍇ�A�r�b�g���]������
		if(c == -1){
			zobrist = ~ zobrist;
		}
		//���Z�b�g�Ȃǂ��s�����߂ɁA�����_�̃]�u���X�g�n�b�V���̒l�Ƃ��ĕۑ�����
		zobrist_now = zobrist;
	}
	
	//�u���ꂽ�ۂ�1�̐΂̏�Ԃ��ω��������Ƃ�\���ϐ�
	//�΂�u������ƈꏏ�ɌJ��Ԃ�����
	public void put(int x, int y, int c){
		//�������ɕς�����Ƃ��A���̏ꏊ�̔��̒l�̔r���I�_���a����邱�ƂŒl��߂��A���̒l�̔r���I�_���a���Ƃ�
		if(c == 1){
			zobrist = zobrist ^ white[x + y * 8];
			zobrist = zobrist ^ black[x + y * 8];
		}
		//�������ɕς�����Ƃ��A���̏ꏊ�̍��̒l�̔r���I�_���a���Ƃ邱�ƂŒl��߂��A���̒l�̔r���I�_���a���Ƃ�
		else if(c == -1){
			zobrist = zobrist ^ black[x + y * 8];
			zobrist = zobrist ^ white[x + y * 8];
		}
	}
	//���u��������s������Ɏ菇�̏������郁�\�b�h
	//�΂�u���I�������ɍs��
	public void color(){
		//�]�u���X�g�n�b�V���̒l�𔽓]������
			zobrist = ~ zobrist;
	}
	
	
	//�����_�ł̃]�u���X�g�̒l�ɖ߂�
	public void reset(){
		zobrist = zobrist_now;
	}
	
}
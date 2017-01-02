import java.util.*;

public class zobrist {
	//����ꏊ�ɋ�u���ꂽ�󋵂��Ƃ̕ϐ�������z��
	int[] black = new int[8 * 8];
	int[] white = new int[8 * 8];
	
	//�����_�̃]�u���X�g�l
	private int zobrist_now = 0;
	
	//������߂�Ƃ��ɕύX����]�u���X�g�̒l
	int zobrist = 0;
	
	//����ꏊ�ɋ�u���ꂽ�󋵂��Ƃ̕ϐ����쐬
	public zobrist(){
		Random rnd = new Random();
		for(int i = 0;i < 8 * 8;i++){
			black[i] = rnd.nextInt();
			white[i] = rnd.nextInt();
		}
	}
	
	//���̎��_�ł̃]�u���X�g�n�b�V���̒l�����֐�
	public void makeZob(int[] state, int c){
		for(int i = 0;i < 8;i++){
			for(int j = 0;j < 8;j++){
				if(state[(i+1) + (j+1) * 10] == 1){
					zobrist = zobrist ^ black[i + j * 8];
				}else if(state[(i+1) + (j+1) * 10] == -1){
					zobrist = zobrist ^ white[i + j * 8];
				}
			}
		}
		if(c == -1){
			zobrist = ~ zobrist;
		}
		zobrist_now = zobrist;
	}
	
	//�u���ꂽ�ۂ�1�̐΂̏�Ԃ��ω��������Ƃ�\���ϐ�
	//�΂�u������ƈꏏ�ɌJ��Ԃ�����
	public void put(int x, int y, int c){
		if(c == 1){
			zobrist = zobrist ^ white[x + y * 8];
			zobrist = zobrist ^ black[x + y * 8];
		}else if(c == -1){
			zobrist = zobrist ^ black[x + y * 8];
			zobrist = zobrist ^ white[x + y * 8];
		}
	}
	//���u��������s������Ɏ菇�̏�������ϐ�
	//�΂�u���I�������ɍs��
	public void color(){
			zobrist = ~ zobrist;
	}
	
	
	//�����_�ł̃]�u���X�g�̒l�ɖ߂�
	public void reset(){
		zobrist = zobrist_now;
	}
	
}
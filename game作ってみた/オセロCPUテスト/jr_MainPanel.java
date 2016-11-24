
//���ʂ̏����̃��\�b�h(�����@�\�A���ۉ��A�����W�F�l���[�^)���W�߂��N���X�i���[�e�B���e�B�N���X�j
import java.util.*;

public class jr_MainPanel{
	//�c���̃}�X
	int Squares = 8;
	
	int data[][];
	int count = 0;
	int turn;
	int player;
	int x;
	int y;
	
	int s_player;
	
	//��Ԃ�\���N���Xstate���쐬
	jr_GameState state; 
	
	//�����_���őł�AI�̃N���XRandomCPU���쐬
	//white
	jr_RandomCPU w_cpu = new jr_RandomCPU(-1,Squares);
	//black
	jr_RandomCPU b_cpu = new jr_RandomCPU(1, Squares);
	
	//���s�̌��ʂ̍��v������z��
	int winCount[] = new int[3];
	

	//���C���p�l�����쐬���郁�\�b�h
	public jr_MainPanel(int[][] d, int t, int p, int[] a){
		data = d;
		turn = t;
		player = p;
		x = a[x];
		y = a[y];
		s_player = player;
		state = new jr_GameState(Squares, data, turn, player, x, y);
		
		for(int i = 0; i < count; i++){
			Game();
			state.reset();
		}
	}
	
	
	public void Game(){
		
		for(;;){
			//CPU�̃^�[��
			//Black
			if(state.player == b_cpu.color){
				//cpu����decide���\�b�h�Œu���ꏊ������
				int b_action[] = b_cpu.decide(state);
				
				//�u����ꏊ������ꍇ�̂݋��u������������
				if(b_action[0] != -1){
					state.put(b_action[0], b_action[1]);
				}
			}
			else if(state.player == w_cpu.color){
				//cpu����decide���\�b�h�Œu���ꏊ������
				int w_action[] = w_cpu.decide(state);
				
				//�u����ꏊ������ꍇ�̂݋��u������������
				if(w_action[0] != -1){
					state.put(w_action[0], w_action[1]);
				}
				
			}
			//�p�X�`�F�b�N
			if( state.checkPass() == true ){
				state.player *= -1;
				//�����p�X���ƏI��
				if(state.checkPass() == true){
					EndGame();
					break;
				}
			}
		}
	}
	
	
	public void EndGame(){
		int End = state.Win();
		if(End == s_player){
			winCount[0] ++;
		}else if(End == s_player*(-1)){
			winCount[2] ++;
		}else {
			winCount[1] ++;
		}
	}
	
	
	public int rePoint(){
		//������10�_�C������5�_�ŕ]���l���v�Z
		int point = winCount[0]*10 + winCount[1]*5;
		return point;
	}
}

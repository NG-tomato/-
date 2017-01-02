//�\��������p�l�����쐬����N���X



//���ʂ̏����̃��\�b�h(�����@�\�A���ۉ��A�����W�F�l���[�^)���W�߂��N���X�i���[�e�B���e�B�N���X�j
import java.util.*;

/*
�p�l���쐬���s���N���X���p��
implements �Ŏ󂯎��C�x���g����������
implements�̏ꍇ�A�C���^�[�t�F�C�X�Œ�`���ꂽ���\�b�h�����ׂĎ�������K�v������(�Ȃ��ƃG���[�ɂȂ�)���߁A���ʂ̋K�i�Ƃ��Ĉ�����
MouseListener �̓}�E�X�C�x���g���󂯎��N���X
Observer �͂���I�u�W�F�N�g�̕ω�������Ɉˑ�����I�u�W�F�N�g�ɒm�点��N���X
*/
public class mctMainPanel{
	int size = 10;
	
	//��Ԃ�\���N���Xstate���쐬
	mctGameState state = new mctGameState();
	
	//�����_���őł�AI�̃N���XRandomCPU���쐬
	//black
	mct_RandomCPU b_cpu = new mct_RandomCPU(1);
	//white
	mct_RandomCPU w_cpu = new mct_RandomCPU(-1);
	
	//���s�̌��ʂ̍��v������z��
	int winCount[] = new int[3];
	
	int s_data[] = new int[size * size];
	int count;
	int turn;
	int player;
	
	int bbb;
	//���C���p�l�����쐬���郁�\�b�h
	public mctMainPanel(int c, int[] d, int t, int p){
		
		count = c;
		s_data = Arrays.copyOf(d ,d.length);

		turn = t;
		player = p;
		
		state.zob.makeZob(s_data, player);
	}
	
	public void mcGame(int[] put){
		
		for(int i = 0; i < count; i++){
			state.set(s_data, turn, player);
			state.put(put[0], put[1]);
			Game();
		}
	}
	
	
	
	public int rePoint(int p){
		int point = 0;
		
		if(p == 1){
			point = winCount[0];
		}else{
			point = winCount[1];
		}
		
		winCount = new int[3];
		
		return point;
	}
		
	
	//�R���|�[�l���g��Ń}�E�X�{�^�����������ƌĂяo�����N���X
	public void Game(){
		
		for(;;){
			//CPU�̃^�[��
			//Black
			if(state.player == b_cpu.color){
				//cpu����decide���\�b�h�Œu���ꏊ������
				int b_action[] = b_cpu.decide(state);
				
				//���W���ȊO�Œu����ꏊ������ꍇ�̂݋��u������������
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
		bbb ++;
		int End = state.Win();
		if(End == 1){
			winCount[0] ++;
		}else if(End == -1){
			winCount[1] ++;
		}else {
			winCount[2] ++;
		}
	}
	
	public int reverseZob(int x,int y){
		int zobrist = 0;
		if(state.put(x, y)==true){
			zobrist = state.zob.zobrist;
			state.set(s_data, turn, player);
		}
		return zobrist;
	}
}

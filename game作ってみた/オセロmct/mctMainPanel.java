//�p�l�����쐬����N���X


//���ʂ̏����̃��\�b�h(�����@�\�A���ۉ��A�����W�F�l���[�^)���W�߂��N���X�i���[�e�B���e�B�N���X�j
import java.util.*;


//mct�p�̃��C���p�l���N���X
public class mctMainPanel{
	//�p�l���̃T�C�Y
	int size = 10;
	
	//��Ԃ�\���N���Xstate���쐬
	mctGameState state = new mctGameState();
	
	//�����_���őł�AI�̃N���XRandomCPU���쐬
	//black
	mct_RandomCPU b_cpu = new mct_RandomCPU(1);
	//white
	mct_RandomCPU w_cpu = new mct_RandomCPU(-1);
		
	//state�̏������Z�b�g����Ƃ��Ɏg���f�[�^��ۑ�����ϐ�
	//�Ֆʂ̃f�[�^
	int s_data[] = new int[size * size];
	//�^�[��
	int turn;
	int player;
	
	//�v���C�A�E�g���I������Ƃ��ɏ��҂�����ϐ�
	int winner;
	
	//�v���C�A�E�g�̉񐔂��J�E���g����ϐ�
	int game_count;
	
	
	//���C���p�l�����쐬���郁�\�b�h
	public mctMainPanel(int[] d, int t, int p){
		//���Z�b�g���s���Ƃ��Ɏg�p���邽�߂̃f�[�^��ۑ�����
		s_data = Arrays.copyOf(d ,d.length);
		turn = t;
		player = p;
		
		//���݂̏�Ԃ̃]�u���X�g�n�b�V���̒l�����
		state.zob.makeZob(s_data, player);
		
		//state�ɂ��̒l���Z�b�g����
		state.set(s_data, turn, player);
	}
	
	//mct�p�̃v���C�A�E�g���s�����\�b�h
	//1��v���C�A�E�g���s�����Ƃɂ��̃v���C�A�E�g�̌��ʂ�Ԃ�
	public int mctGame(){
		
		Game();
		state.set(s_data, turn, player);
		return winner;
	}
	
	//�v���C�A�E�g���Q�[���I���i�Q�[���I���j�܂ōs���N���X
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
		//System.out.println("aaa");
		winner = state.Win();
	}

	
	//(x,y)�ɐ΂�ł����Ƃ��̃]�u���X�g�̒l��Ԃ��ϐ�
	//�łĂȂ�����0��Ԃ�
	public int reverseZob(int x,int y){
		int zobrist = 0;
		//�łĂ�Ƃ�
		if(state.put(x, y)==true){
			zobrist = state.zob.zobrist;
			state.set(s_data, turn, player);
		}
		return zobrist;
	}
	
	//�T�����ɐ΂�ł����ꍇ�̃]�u���X�g�̒l��Ԃ��ϐ�
	//��L�̂��̂ƈ���āA�ϐ������݂̏�Ԃɖ߂��ƍ��邽�߁A�T�����̏�Ԃ̕ϐ����Ƃ�
	public int reverseZob(int x,int y, int[] ss_data,int ss_turn,int ss_player){
		int zobrist = 0;
		//�łĂ�Ƃ�
		if(state.put(x, y)==true){
			zobrist = state.zob.zobrist;
			state.set(ss_data, ss_turn, ss_player);
		}
		return zobrist;
	}

	
	//�p�X�����Ƃ��̃]�u���X�g�̒l��Ԃ��ϐ�
	public int passZob(int[] ss_data,int ss_turn,int ss_player){
		state.pass();
		int zobrist = state.zob.zobrist;
		state.set(ss_data, ss_turn, ss_player);
		return zobrist;
	}
}

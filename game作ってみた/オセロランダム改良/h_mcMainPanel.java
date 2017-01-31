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
public class h_mcMainPanel{
	int size = 10;
	
	//��Ԃ�\���N���Xstate���쐬
	GameState state = new GameState();
	
	//�����_���őł�AI�̃N���XRandomCPU���쐬
	//black
	CPU b_cpu = new hyoukaCPU(1);
	//white
	CPU w_cpu = new hyoukaCPU(-1);
	
	//���s�̌��ʂ̍��v������z��
	int winCount[] = new int[3];
	
	int s_data[] = new int[size * size];
	int count;
	int turn;
	int player;
	
	//�v���C�A�E�g�̑��v��ۑ�����ϐ�
	
	int bbb;
	//���C���p�l�����쐬���郁�\�b�h
	public h_mcMainPanel(int c, int[] d, int t, int p){
		
		count = c;
		s_data = Arrays.copyOf(d ,d.length);

		turn = t;
		player = p;
		
		
	}
	
	public void mcGame(int[] put){
		
			state.set(s_data, turn, player);
			state.put(put[0], put[1]);
			Game();
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
	
	//�`�ʂ��s�����\�b�h
	public void TextDisplay(){
				
		
		System.out.println();
		
		//���ォ�珇�Ƀ}�X�Ƌ��\��
		for(int y=1; y<size - 1; y++){
			for(int x=1; x<size - 1; x++){
				System.out.print("|");
				if(state.data[x + y * size] == 1){
					//���̋��\��
					System.out.print("��");
				}else if(state.data[x + y * size] == -1){
					//���̋��\��
					System.out.print("��");
				}else{
					System.out.print("  ");
				}
			}
			System.out.println("|");
		}
		
		
		System.out.println();
		
		System.out.println("TURN = "+state.turn);
		System.out.println("PLAYER = "+state.player);
		System.out.println("DISC = "+state.black+" : " +state.white);
		System.out.println("\n \n");
		
	}
	
	
	//�v���C�A�E�g���s���N���X
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
		//System.out.println("---Game END---");
		int End = state.Win();
		//String Winner;
		if(End == 1){
		//	Winner = "black";
			winCount[0] ++;
		}else if(End == -1){
		//	Winner = "white";
			winCount[1] ++;
		}else {
		//	Winner = "Drow";
			winCount[2] ++;
		}
		//System.out.println(Winner + " Win !");
	}
}

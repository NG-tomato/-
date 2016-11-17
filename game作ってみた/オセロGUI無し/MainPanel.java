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
public class MainPanel{
	//�c���̃}�X
	int Squares = 8;
	
	//��Ԃ�\���N���Xstate���쐬
	GameState state = new GameState(Squares);
	
	//�����_���őł�AI�̃N���XRandomCPU���쐬
	//white
	RandomCPU w_cpu = new RandomCPU(Squares);
	//black
	RandomCPU2 b_cpu = new RandomCPU2(Squares);
	
	//���C���p�l�����쐬���郁�\�b�h
	public MainPanel(){
		TextDisplay();
		Game();
	}
	
	
	//�`�ʂ��s�����\�b�h
	public void TextDisplay(){
				
		
		System.out.println();
		
		//���ォ�珇�Ƀ}�X�Ƌ��\��
		for(int y=0; y<Squares; y++){
			for(int x=0; x<Squares; x++){
				System.out.print("|");
				if(state.data[x][y] == 1){
					//���̋��\��
					System.out.print("��");
				}else if(state.data[x][y] == -1){
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
					System.out.println("Black put point is : "+b_action[0]+" ,"+b_action[1]);				
				}
				//�Ֆʂ����܂�����I��
				if(state.turn == (Squares * Squares) - 4){
					TextDisplay();
					EndGame();
				}
			}
			else if(state.player == w_cpu.color){
				//cpu����decide���\�b�h�Œu���ꏊ������
				int w_action[] = w_cpu.decide(state);
				
				//�u����ꏊ������ꍇ�̂݋��u������������
				if(w_action[0] != -1){
					state.put(w_action[0], w_action[1]);
					System.out.println("White put point is : "+w_action[0]+" ,"+w_action[1]);
				}
				
				//�Ֆʂ����܂�����I��
				if(state.turn == (Squares * Squares) - 4){
					TextDisplay();
					EndGame();
				}
			}
			TextDisplay();
			//�p�X�`�F�b�N
			if( state.checkPass() == true ){
				state.player *= -1;
				//�����p�X���ƏI��
				if(state.checkPass() == true){
					EndGame();
				}
				System.out.println("Pass! Next turn is : "+state.player);
			}
		}
	}
	
	
	public void EndGame(){
		System.out.println("---Game END---");
		if(state.black > state.white){
			System.out.println("Black WIN");
		}else if(state.black < state.white){
			System.out.println("White WIN");
		}else{
			System.out.println("DRAW");
		}
		
		System.exit(0);
	}
}

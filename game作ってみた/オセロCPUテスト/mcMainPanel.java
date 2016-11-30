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
public class mcMainPanel{
	//�c���̃}�X
	int Squares = 8;
	int data[][];
	int count = 1;
	int turn;
	int player;
	
	int s_data[][];
	int s_turn;
	int s_player;
	
		
	//��Ԃ�\���N���Xstate���쐬
	mcGameState state = new mcGameState(Squares);
	
	//�����_���őł�AI�̃N���XRandomCPU���쐬
	//white
	mcRandomCPU w_cpu = new mcRandomCPU(-1,Squares);
	//black
	mcRandomCPU b_cpu = new mcRandomCPU(1, Squares);
	
	//���s�̌��ʂ̍��v������z��
	int winCount[] = new int[3];
	
	//���C���p�l�����쐬���郁�\�b�h
	public mcMainPanel(int[][] d,int c, int t, int p){
		data = Arrays.copyOf(d, d.length);
		count = c;
		turn = t;
		player = p;
		
		s_data = Arrays.copyOf(d, d.length);
		s_turn = t;
		s_player = p;;

	}
	
	public void mcGame(int x , int y){
		for(int i = 0; i < count; i++){
			//state.reset();
			state.set(s_data, s_turn, s_player);
			TextDisplay();
			state.put(x, y);
			//TextDisplay();
			Game();
			//System.out.println("������");
		}
	}
	
	
	//�`�ʂ��s�����\�b�h
	public void TextDisplay(){
				
		
		System.out.println();
		
		//���ォ�珇�Ƀ}�X�Ƌ��\��
		for(int y=1; y<Squares + 1; y++){
			for(int x=1; x<Squares + 1; x++){
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
					//System.out.println("Black put point is : "+b_action[0]+" ,"+b_action[1]);				
				}
				/*�Ֆʂ����܂�����I��
				if(state.turn == (Squares * Squares) - 4){
					TextDisplay();
					EndGame();
				}*/
			}
			else if(state.player == w_cpu.color){
				//cpu����decide���\�b�h�Œu���ꏊ������
				int w_action[] = w_cpu.decide(state);
				
				//�u����ꏊ������ꍇ�̂݋��u������������
				if(w_action[0] != -1){
					state.put(w_action[0], w_action[1]);
					//System.out.println("White put point is : "+w_action[0]+" ,"+w_action[1]);
				}
				
			}
			//TextDisplay();
			//�p�X�`�F�b�N
			if( state.checkPass() == true ){
				state.player *= -1;
				//�����p�X���ƏI��
				if(state.checkPass() == true){
					EndGame();
					break;
				}
				//System.out.println("Pass! Next turn is : "+state.player);
			}
		}
	}
	
	
	public void EndGame(){
		//System.out.println("---Game END---");
		int End = state.Win();
		String Winner;
		if(End == 1){
		//Winner = "black";
			winCount[0] ++;
		}else if(End == -1){
		//Winner = "white";
			winCount[1] ++;
		}else {
		//Winner = "Drow";
			winCount[2] ++;
		}
		//System.out.println(Winner + " Win !");
		//TextDisplay();
	}
	
	public int rePoint(){
		//������10�_�C����������5�_�ŕ]���l���v�Z
		int point = winCount[0]*10 + winCount[2]*5;
		return point;
	}
}

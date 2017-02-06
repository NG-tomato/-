//�\��������p�l�����쐬����N���X



//���ʂ̏����̃��\�b�h(�����@�\�A���ۉ��A�����W�F�l���[�^)���W�߂��N���X�i���[�e�B���e�B�N���X�j
import java.util.*;

/*
�p�l���N���X
��{�I���������
*/
public class MainPanel{
	//�c���̃}�X
	
	//��Ԃ�\���N���Xstate���쐬
	GameState state = new GameState();
	
	//�����_���őł�AI�̃N���XRandomCPU���쐬
	//black
	//RandomCPU b_cpu = new RandomCPU(1);
	//mcCPU b_cpu = new mcCPU(1);
	//hyoukaCPU b_cpu = new hyoukaCPU(1);
	//mctCPU b_cpu = new mctCPU(1);
	//h_mctCPU b_cpu = new h_mctCPU(1);
	c_mctCPU b_cpu = new c_mctCPU(1);
	//hc_mctCPU b_cpu = new hc_mctCPU(1);
	
	//white
	//RandomCPU w_cpu = new RandomCPU(-1);
	//mcCPU w_cpu = new mcCPU(-1);
	mctCPU w_cpu = new mctCPU(-1);
	//hyoukaCPU w_cpu = new hyoukaCPU(-1);
	//h_mctCPU w_cpu = new h_mctCPU(-1);
	//c_mctCPU w_cpu = new c_mctCPU(-1);
	//hc_mctCPU w_cpu = new hc_mctCPU(-1);

	
	
	//���s�̌��ʂ̍��v������z��
	int winCount[] = new int[3];
	
	//�l�����肷��ۂ̒l�̑��₷��
	int add = -200;

	//���C���p�l�����쐬���郁�\�b�h
	public MainPanel(int count){
		//臒l�����߂郁�\�b�h
		//decideThreshold(count);
		
		//�}���肷��Ƃ��̓_���̍��̍ő�l
		decideCut(count);
		
		/*
		//�ʏ��game
		for(int i = 0; i < count; i++){
			System.out.println(i);
			game();
			//textDisplay();
			state.reset();
		}
		
		System.out.println();
		System.out.println("---Loop END---");
		System.out.println("Black win : " + winCount[0]);
		System.out.println("White win : " + winCount[1]);
		System.out.println("Draw      : " + winCount[2]);
		*/
	}
	
		
	//�`�ʂ��s�����\�b�h
	public void textDisplay(){
		System.out.println();
		
		//���ォ�珇�Ƀ}�X�Ƌ��\��
		for(int y=1; y<=8; y++){
			for(int x=1; x<=8; x++){
				System.out.print("|");
				switch (state.data[x + y * 10]) {
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
		System.out.println("TURN = "+state.turn);
		System.out.println("PLAYER = "+state.player);
		System.out.println("DISC = "+state.black+" : " +state.white);
		System.out.println("\n \n");
		
	}
	
	
	public void game(){
		boolean isLastPass = false;
		
		for(;;){
			//textDisplay();

			// �u���Ƃ��낪�Ȃ���΃p�X
			if( state.checkPass() == true ){
				if (isLastPass) {	//�����p�X���ƏI��
					endGame();
					break;
				}
				
				state.player *= -1;
				isLastPass = true;
				//System.out.println("Pass! Next turn is : "+state.player);
				continue;
			}
			isLastPass = false;
			
			// �ȉ��ł͕K���u������̂Ƃ���D
			if(state.player == b_cpu.color){ 			//Black

				//cpu����decide���\�b�h�Œu���ꏊ������
				int action[] = b_cpu.decide(state);
				if (action[0] == -1 || action[1] == -1) { throw new RuntimeException("Illegal Move by Black: (-1, -1)"); }
				//�u����ꏊ������ꍇ�̂݋��u������������
				state.put(action[0], action[1]);
				//System.out.println("Black put point is : "+action[0]+" ,"+action[1]);
			}
			else { // White
				//cpu����decide���\�b�h�Œu���ꏊ������
				int action[] = w_cpu.decide(state);
				
				if (action[0] == -1 || action[1] == -1) { throw new RuntimeException("Illegal Move by White: (-1, -1)"); }
				//�u����ꏊ������ꍇ�̂݋��u������������
				state.put(action[0], action[1]);
				//System.out.println("White put point is : "+action[0]+" ,"+action[1]);
			}
		}
	}
	
	//int GameCount = 0;
	//�Q�[���I�����ɏ��������̐���ǉ�����N���X
	public void endGame(){
		//System.out.println("---Game END---");
		int End = state.Win();
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
		//textDisplay();
		//GameCount ++;
		/*
		System.out.println("Game count = " + GameCount);
		System.out.println("Winner = " + End);
		*/
	}
	
	//�W�J����v���C�A�E�g����臒l
	public void decideThreshold(int count){
		//臒l�̔�r
		//��r����臒l�̍�
		int add = 1;
		for(int j = 1;j < w_cpu.count;j+=add){
			//��r����臒l���Ƃ̏���������ۑ�
			int[] thresholdCount = new int[3];
			
			//臒l��ύX
			b_cpu.setThreshold(j);
			System.out.println("Black's threshold is " + j);
			w_cpu.setThreshold(j + add);
			System.out.println("White's threshold is " + (j+add));
			for(int i = 0; i < count/2; i++){
				
				if(i%50 == 0){
					System.out.println("Now Game is " + i);
				}
				game();
				//textDisplay();
				//System.out.println("Now Game is "+ i);
				state.reset();
			}
			System.out.println();
			System.out.println("---Half loop END---");
			System.out.println("Black win : " + winCount[0]);
			System.out.println("White win : " + winCount[1]);
			System.out.println("Draw      : " + winCount[2]);
			thresholdCount[0] += winCount[0];thresholdCount[1] += winCount[1];thresholdCount[2] += winCount[2];
			winCount = new int[3];
			
			//臒l��ύX
			b_cpu.setThreshold(j + add);
			System.out.println("Black's threshold is " + (j+add));
			w_cpu.setThreshold(j);
			System.out.println("White's threshold is " + j);
			for(int i = 0; i < count/2; i++){
				if(i%50 == 0){
					System.out.println("Now Game is " + i);
				}
				game();
				//textDisplay();
				//System.out.println("Now Game is "+ i);
				state.reset();
			}
			System.out.println();
			System.out.println("---Half loop END---");
			System.out.println("Black win : " + winCount[0]);
			System.out.println("White win : " + winCount[1]);
			System.out.println("Draw      : " + winCount[2]);
			thresholdCount[0] += winCount[1];thresholdCount[1] += winCount[0];thresholdCount[2] += winCount[2];
			winCount = new int[3];
			
			System.out.println("--- Loop END---");
			System.out.println(j + "win : " + thresholdCount[0]);
			System.out.println((j+add) + "win : " + thresholdCount[1]);
			System.out.println("Draw      : " + thresholdCount[2]);
			
			
			//�������t�]������I��
			if(thresholdCount[0] > thresholdCount[1]){
				break;
			}
		}
	}
	
	//�}���肷��Ƃ��̓_���̍��̍ő�l
	public void decideCut(int count){
		//�O�̎菇�ł̏�����ۑ�����ϐ�
		int backWin = -1;
		//臒l�̔�r
		for(int j = 800;j>0;){
			//��r����臒l���Ƃ̏���������ۑ�
			int[] cutCount = new int[3];
			
			//���s����������
			winCount = new int[3];
			
			//臒l��ύX
			b_cpu.setCut(j);
			System.out.println("Black's CUT is " + j);
			for(int i = 0; i < count; i++){
				
				if(i%10 == 0){
					System.out.println("Now Game is " + i);
				}
				game();
				//textDisplay();
				//System.out.println("Now Game is "+ i);
				state.reset();
			}
			System.out.println();
			System.out.println("---Loop END---");
			System.out.println("Black win : " + winCount[0]);
			System.out.println("White win : " + winCount[1]);
			System.out.println("Draw      : " + winCount[2]);
						
			
			//�������t�]������I��
			if(backWin > winCount[0]){
				System.out.println();
				System.out.println("Last cut is "+ j);
				break;
			}else{
				backWin = winCount[0];
				j += add;
			}
		}

	}
}

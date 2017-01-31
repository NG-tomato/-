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
	h_mcCPU b_cpu = new h_mcCPU(1);
	//RandomCPU b_cpu = new RandomCPU(1);
	
	//white
	//mctCPU w_cpu = new mctCPU(1);
	//hyoukaCPU w_cpu = new hyoukaCPU(-1);
	RandomCPU w_cpu = new RandomCPU(-1);
	
	//���s�̌��ʂ̍��v������z��
	int winCount[] = new int[3];
	
	//���C���p�l�����쐬���郁�\�b�h
	public MainPanel(int count){
		
		for(int i = 0; i < count; i++){
			/*if(i % 10 == 0){
				System.out.println("Now game count is "+ i);
			}*/
			//textDisplay();
			game();
			state.reset();
		}
		
		System.out.println();
		System.out.println("---Loop END---");
		System.out.println("Black win : " + winCount[0]);
		System.out.println("White win : " + winCount[1]);
		System.out.println("Draw      : " + winCount[2]);

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
	
	int GameCount = 0;
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
		GameCount ++;
		/*
		System.out.println("Game count = " + GameCount);
		System.out.println("Winner = " + End);
		*/
	}
}

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
	//c_mctCPU b_cpu = new c_mctCPU(1);
	//hc_mctCPU b_cpu = new hc_mctCPU(1);
	fpu_mctCPU b_cpu = new fpu_mctCPU(1); 
	//hfpu_mctCPU b_cpu = new hfpu_mctCPU(1); 

	
	//white
	RandomCPU w_cpu = new RandomCPU(-1);
	//mcCPU w_cpu = new mcCPU(-1);
	//mctCPU w_cpu = new mctCPU(-1);
	//hyoukaCPU w_cpu = new hyoukaCPU(-1);
	//h_mctCPU w_cpu = new h_mctCPU(-1);
	//c_mctCPU w_cpu = new c_mctCPU(-1);
	//hc_mctCPU w_cpu = new hc_mctCPU(-1);
	//fpu_mctCPU w_cpu = new fpu_mctCPU(-1); 
	//hfpu_mctCPU w_cpu = new hfpu_mctCPU(-1); 

	
	
	//���s�̌��ʂ̍��v������z��
	int winCount[] = new int[3];
	
	//�l�����肷��ۂ̒l�̑��₷��
	int add = 3;

	//�l���œK������Ƃ��ɉ��񊴊o�Ői���󋵂�\�����邩
	int lookGame = 100;
	
	//���C���p�l�����쐬���郁�\�b�h
	public MainPanel(int count){
		
		
		//臒l�����߂郁�\�b�h
		//decideThreshold(count);
		
		
		
		//UCB1�l��C�̍œK��
		//decideC(count);
		
		//FPU�̍œK�l�����߂�
		//decideFPU(count);

		
		//�}���肷��Ƃ��̓_���̍��̍ő�l
		//decideCut(count);
		
		
		//�ʏ��game
		for(int i = 0; i < count; i++){
			if(i % 50 == 0){
				System.out.println(i);
			}
			game();
			//textDisplay();
			state.reset();
			b_cpu.reset();
		}
		System.out.println();
		System.out.println("---Loop END---");
		System.out.println("Black win : " + winCount[0]);
		System.out.println("White win : " + winCount[1]);
		System.out.println("Draw      : " + winCount[2]);
		
		/*
		//���ώ��Ԃ����߂�
		long shikou = 0;
		for(int i = 0; i < count; i++){
			if(i % 50 == 0){
				System.out.println(i);
			}
			game();
			//�v���C�A�E�g���Ƃ̎v�l���ώ���
			shikou +=  b_cpu.avePlayout();
			state.reset();
			b_cpu.reset();
		}
		shikou = (long)shikou/count;
		System.out.println("Playout's average time(100 every time): " + shikou + " ms");
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

	
	//�؂�W�J����臒lThreshold�̍œK��(vs�����_��CPU�̏����ɂ��œK��)
	public void decideThreshold(int count){
		//�T�������l�̏��s�����L������}�b�v
		//��������p���āC�T���̉񐔂����炷
		Map<Integer, int[]> map = new HashMap<>();
		int j = 38;
		//C�̔�r
		for(int add = 10;add >= 1;add = add / 2){
			//�O�̎菇�ł̏�����ۑ�����ϐ�
			int backWin = -1;
			while(j>0){
				//��r����臒l���Ƃ̏���������ۑ�
				int[] thresholdCount = new int[3];
				
				//���s����������
				winCount = new int[3];
				
				//map��ɏ��s����������̂͂��łɏ��s�����v�Z�ς݂Ȃ̂ł�����擾����
				if(map.containsKey(j)){
					winCount = map.get(j);
					System.out.println();
					System.out.println(" Game skip ! : " + j);
				}
				//���v�Z�̏ꍇ
				else{
					//臒l��ύX
					b_cpu.setThreshold(j);
					System.out.println("Black's Threshold is " + j);
					for(int i = 0; i < count; i++){
					
						if(i%lookGame == 0){
							System.out.println("Now Game is " + i);
						}
						game();
						//textDisplay();
						//System.out.println("Now Game is "+ i);
						state.reset();
						b_cpu.reset();
					}
					map.put(j,winCount);
				}
				System.out.println();
				System.out.println("---Loop END---");
				System.out.println("Black win : " + winCount[0]);
				System.out.println("White win : " + winCount[1]);
				System.out.println("Draw      : " + winCount[2]);
				System.out.println();

				
				//�������t�]������I��
				if(backWin > winCount[0]){
					System.out.println();
					j -= add;
					System.out.println();
					System.out.println("Last Threshold is "+ j);
					System.out.println();
					break;
				}else{
					backWin = winCount[0];
					//System.out.println("test" + add);
					j+=add;
					//System.out.println("test" + j);
				}
			}
			j -= add;
		}
	}

	//�}������s����@�̒lFPU�����肷�郁�\�b�h(vs�����_��CPU�̏����ɂ��œK��)
	public void decideFPU(int count){
		//�T�������l�̏��s�����L������}�b�v
		//��������p���āC�T���̉񐔂����炷
		Map<Double, int[]> map = new HashMap<>();
		
		double j = 5;
		//C�̔�r
		for(double add = -2;add <=-0.001;add = (double)add / 2){
			
			//�O�̎菇�ł̏�����ۑ�����ϐ�
			int backWin = -1;
			while(j>0){
				//��r����臒l���Ƃ̏���������ۑ�
				int[] FPUCount = new int[3];
				
				//���s����������
				winCount = new int[3];
				
				//map��ɏ��s����������̂͂��łɏ��s�����v�Z�ς݂Ȃ̂ł�����擾����
				if(map.containsKey(j)){
					winCount = map.get(j);
					System.out.println();
					System.out.println(" Game skip ! : " + j);

				}
				//���v�Z�̏ꍇ
				else{
					//臒l��ύX
					b_cpu.setFPU(j);
					System.out.println("Black's FPU is " + j);
					for(int i = 0; i < count; i++){
						
						if(i%lookGame == 0){
							System.out.println("Now Game is " + i);
						}
						game();
						//textDisplay();
						//System.out.println("Now Game is "+ i);
						state.reset();
						b_cpu.reset();
					}
					map.put(j,winCount);
				}
				System.out.println();
				System.out.println("---Loop END---");
				System.out.println("Black win : " + winCount[0]);
				System.out.println("White win : " + winCount[1]);
				System.out.println("Draw      : " + winCount[2]);
				System.out.println();
				
				//�������t�]������I��
				if(backWin > winCount[0]){
					System.out.println();
					j -= add;
					System.out.println();
					System.out.println("Last FPU is "+ j);
					System.out.println();
					break;
				}else{
					backWin = winCount[0];
					//System.out.println("test" + add);
					j+=add;
					//System.out.println("test" + j);
				}
			}
			j -= add;
		}
	}

	

	
	
	//UCB1�l��C�̍œK��
	public void decideC(int count){
		//�T�������l�̏��s�����L������}�b�v
		//��������p���āC�T���̉񐔂����炷
		Map<Double, int[]> map = new HashMap<>();

		
		double j = 3;
		//C�̔�r
		for(double add = 1.0;add > 0.01;add = (double)add/2){
			
			//�O�̎菇�ł̏�����ۑ�����ϐ�
			int backWin = -1;
			while(j>0){
				//��r����臒l���Ƃ̏���������ۑ�
				int[] cutCount = new int[3];
				
				//���s����������
				winCount = new int[3];
				
				//map��ɏ��s����������̂͂��łɏ��s�����v�Z�ς݂Ȃ̂ł�����擾����
				if(map.containsKey(j)){
					winCount = map.get(j);
					System.out.println();
					System.out.println(" Game skip ! : " + j);
				}
				//���v�Z�̏ꍇ
				else{
					//臒l��ύX
					b_cpu.setC(j);
					System.out.println("Black's C is " + j);
					for(int i = 0; i < count; i++){
						
						if(i%lookGame == 0){
							System.out.println("Now Game is " + i);
						}
						game();
						//textDisplay();
						//System.out.println("Now Game is "+ i);
						b_cpu.reset();
						state.reset();
					}					
					map.put(j,winCount);
				}
					
					
				System.out.println();
				System.out.println("---Loop END---");
				System.out.println("Black win : " + winCount[0]);
				System.out.println("White win : " + winCount[1]);
				System.out.println("Draw      : " + winCount[2]);
				System.out.println();
				
				//�������t�]������I��
				if((backWin > winCount[0])||(add == 0)){
					System.out.println();
					j += add;
					System.out.println("Last C is "+ j);
					break;
				}else{
					backWin = winCount[0];
					//System.out.println("test" + add);
					j-=add;
					//System.out.println("test" + j);
				}
			}
			j += add;
			/*
			if(j>1){
				j=1;
			}*/
		}
	}
	

}

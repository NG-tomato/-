//�I�Z���̊�{�I������s���N���X
//���ʂ̏����̃��\�b�h(�����@�\�A���ۉ��A�����W�F�l���[�^)���W�߂��N���X�i���[�e�B���e�B�N���X�j
import java.util.*;

import java.util.Scanner;


/*
�p�l���N���X
��{�I���������
*/
public class MainPanel{
	//�c���̃}�X
	
	//��Ԃ�\���N���Xstate���쐬
	GameState state = new GameState();
	
	//CPU�̃N���X���쐬�i�ォ��㏑���j
	//black
	CPU b_cpu = new CPU(1);

	//white
	CPU w_cpu = new CPU(-1);

	
	//���s�̌��ʂ̍��v������z��
	int winCount[] = new int[3];
	
	//���񊴊o�Ői���󋵂�\�����邩
	int lookGame = 100;
	
	//���C���p�l�����쐬���郁�\�b�h
	public MainPanel(int count){
		//CPU�����߂�
		//����CPU
		Scanner scan = new Scanner(System.in);
		System.out.println("Please select Black Player");
		System.out.println("RandomCPU : 1");
		System.out.println("hyoukaCPU : 2");
		System.out.println("Monte Carlo : 3");
		System.out.println("Monte Carlo Tree: 4");
		System.out.println("Monte Carlo Tree + Cost function : 5");
		
		int val = scan.nextInt();
		if(val == 1)b_cpu = new RandomCPU(1); 
		else if(val == 2)b_cpu = new hyoukaCPU(1);
		else if(val == 3)b_cpu = new mcCPU(1);
		else if(val == 4)b_cpu = new fpu_mctCPU(1);
		else if(val == 5)b_cpu = new hfpu_mctCPU(1);
		else{
			System.out.println("Unspecified character was entered");
			return;
		}
		
		//����CPU
		System.out.println("Please select White Player");
		System.out.println("RandomCPU : 1");
		System.out.println("hyoukaCPU : 2");
		System.out.println("Monte Carlo : 3");
		System.out.println("Monte Carlo Tree: 4");
		System.out.println("Monte Carlo Tree + Cost function : 5");
		
		val = scan.nextInt();
		if(val == 1)w_cpu = new RandomCPU(-1); 
		else if(val == 2)w_cpu = new hyoukaCPU(-1);
		else if(val == 3)w_cpu = new mcCPU(-1);
		else if(val == 4)w_cpu = new fpu_mctCPU(-1);
		else if(val == 5)w_cpu = new hfpu_mctCPU(-1);
		else{
			System.out.println("Unspecified character was entered");
			return;
		}
		
		//�������邩���߂�
		System.out.println("Please select to do");
		System.out.println("Normal Game : 1");
		System.out.println("Normal Game +�@Survey Playout �� Time : 2");
		System.out.println("Decide Threshold : 3");
		System.out.println("Decide UCB1's C : 4");
		System.out.println("Decide FPU : 5");
		
		val = scan.nextInt();
		if(val == 1)normalGame(count);//�ʏ��game
		else if(val == 2)timeGame(count);//���ԑ���ƃv���C�A�E�g�񐔂����game
		else if(val == 3)decideThreshold(count);//臒l�����߂郁�\�b�h
		else if(val == 4)decideC(count);//UCB1�l��C�̍œK��
		else if(val == 5)decideFPU(count);//FPU�̍œK�l�����߂�
		else{
			System.out.println("Unspecified character was entered");
			return;
		}
		
		
		
		
	}
	
	//�ʏ��game
	public void normalGame(int count){
		System.out.println();
		System.out.println("Loop Start !");
		for(int i = 0; i < count; i++){
			if(i%lookGame == 0){
				System.out.println("Now Game is " + i);
			}
			game();
			state.reset();
			b_cpu.reset();
			w_cpu.reset();
		}
		
		System.out.println();
		System.out.println("---Loop END---");
		System.out.println("Black win : " + winCount[0]);
		System.out.println("White win : " + winCount[1]);
		System.out.println("Draw      : " + winCount[2]);

	}
	
	//���ԑ���ƃv���C�A�E�g�񐔂����game
	public void timeGame(int count){
		System.out.println();
		System.out.println("Loop Start !");
		long time = 0;
		double playout_count = 0;
		for(int i = 0; i < count; i++){
			if(i%lookGame == 0){
				System.out.println("Now Game is " + i);
			}
			game();
			state.reset();
			time += b_cpu.avePlayout();
			playout_count += b_cpu.aveCount();;
			b_cpu.reset();
			w_cpu.reset();
		}
		
		System.out.println();
		System.out.println("---Loop END---");
		System.out.println("Black win : " + winCount[0]);
		System.out.println("White win : " + winCount[1]);
		System.out.println("Draw      : " + winCount[2]);

		time /= count;
				
		//�v���C�A�E�g���Ƃ̎v�l���ώ���
		System.out.println();
		System.out.println("Playout's average time(100 every time): " + time + " ms");
		//�v���C�A�E�g���Ƃ̃v���C�A�E�g��
		System.out.println();
		System.out.println("Playout's Count: " + ((double)playout_count/count));

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
			// �u���Ƃ��낪�Ȃ���΃p�X
			if( state.checkPass() == true ){
				if (isLastPass) {	//�����p�X���ƏI��
					endGame();
					break;
				}
				
				state.player *= -1;
				isLastPass = true;
				continue;
			}
			isLastPass = false;
			
			// �ȉ��ł͕K���u������̂Ƃ���D
			if(state.player == b_cpu.color){ //Black

				//cpu����decide���\�b�h�Œu���ꏊ������
				int action[] = b_cpu.decide(state);
				if (action[0] == -1 || action[1] == -1) { throw new RuntimeException("Illegal Move by Black: (-1, -1)"); }
				//�u����ꏊ������ꍇ�̂݋��u������������
				state.put(action[0], action[1]);
			}
			else { // White
				//cpu����decide���\�b�h�Œu���ꏊ������
				int action[] = w_cpu.decide(state);
				
				if (action[0] == -1 || action[1] == -1) { throw new RuntimeException("Illegal Move by White: (-1, -1)"); }
				//�u����ꏊ������ꍇ�̂݋��u������������
				state.put(action[0], action[1]);
			}
		}
	}
	
	//int GameCount = 0;
	//�Q�[���I�����ɏ��������̐���ǉ�����N���X
	public void endGame(){
		int End = state.Win();
		if(End == 1){
			winCount[0] ++;
		}else if(End == -1){
			winCount[1] ++;
		}else {
			winCount[2] ++;
		}
	}

	
	//�؂�W�J����臒lThreshold�̍œK��(vs�����_��CPU�̏����ɂ��œK��)
	public void decideThreshold(int count){
		//�T�������l�̏��s�����L������}�b�v
		//��������p���āC�T���̉񐔂����炷
		Map<Integer, int[]> map = new HashMap<>();
		int j = 50;
		//C�̔�r
		for(int add = 10;add >= 1;add = add / 2){
			//�O�̎菇�ł̏�����ۑ�����ϐ�
			int backWin = -1;
			while(j>=0){
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
						w_cpu.reset();
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
					j+=add;
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
		
		double j = 3;
		//C�̔�r
		for(double add = -1;add <=-0.001;add = (double)add / 2){
			
			//�O�̎菇�ł̏�����ۑ�����ϐ�
			int backWin = -1;
			while(j>=0){
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
						state.reset();
						b_cpu.reset();
						w_cpu.reset();

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
					j+=add;
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

		
		double j = 1.0;
		//C�̔�r
		for(double add = 0.3;add > 0.01;add = (double)add/2){
			
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
						b_cpu.reset();
						w_cpu.reset();
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
					j-=add;
				}
			}
			j += add;
		}
	}
	
}

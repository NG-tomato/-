//�����e�J�����ؒT���Ńv���C�A�E�g�ɕ]���l���g����@
import java.util.*;

public class h_mctCPU extends CPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;
	
	//�Ղ̑傫���̕ϐ��i�Ǌ܂ށj
	int size = 10;
	
	//1��ǂނ��Ƃ̑��v���C�A�E�g��
	int count = 1000;
	
	//1��ǂނ��Ƃ̎���(ms�~���b�Ȃ̂ŁC1�b=1000ms)
	long time = 1000;
	
	//�v���C�A�E�g���s�����񐔂�ۑ�����ϐ�
	int total_count = 0;
	
	//�T����[������Ƃ��̂������l
	int threshold = 2;
	
	//map�ɓ����f�[�^�̔z��
	//{�v���C�A�E�g��,����}
	int[] data = new int[3];
	
	//�f�[�^������map
	//�v���C�A�E�g���A�|�C���g�i���̋ǖʂ̃v���C���̏����j�AUCB1�l
	Map<Integer, int[]> map = new HashMap<>();
	
	
	//�N���X���쐬����ۂɁA�ǂ����̃v���C�����I��
	public h_mctCPU(int c){
		color = c;
	}
		
	//���I�ԃ��\�b�h
	//��������s���邱�ƂŎ��I��
	int[] decide(GameState state){
		mctMainPanel p = new mctMainPanel(state.data , state.turn, state.player);
		
		//�u����ꏊ�����X�g�Ɏ擾
		//putPoint���\�b�h��mctMainPanel�̒l��^���邱�ƂŒu����ꏊ�̃f�[�^����ꂽ�ϐ���Ԃ�
		ArrayList<int[]> array = putPoint(p.state);
		
		/*
		//�]�u���X�g�n�b�V���̒l�������ɂȂ��Ă��邩�̊m�F
		for(int x =0;x <array.size();x++){
			int[] data = array.get(x);
			System.out.println(data[2]);
		}
		*/

		//select�֐���p���ăv���C�A�E�g���Ă���
		//�v���C�A�E�g��臒l
		//for(int i=0; i < count; i++){
		//���Ԃ�臒l
		long start = System.currentTimeMillis();
		for(long i = start; (i - start) <= time;i = System.currentTimeMillis()){

			//1��̃v���C�A�E�g���Ƃ�total_count�ϐ������Z���Ă������Ƃł����܂ł̃v���C�A�E�g�̑��v�����߂�
			total_count ++;
						
			//selsect�֐���UCB1�l���������̂�I��Ńv���C�A�E�g���A���ʂ�map�֐��ɓK������ϐ�
			//�z��v���C�A�E�g�������ȏ�̏ꍇ�A�v���C�A�E�g�͍s�킸�A���ɍċA�I�Ƀ��\�b�h����邱�ƂŒT����[�߂Ă���
			int x = select(p.state.clone());
			
		}

		//�|�C���g���ő�̎�����߂�
		int i = selectUCB(array);

		//System.out.println("After Playouts");
		for (int[] pos : array) {
			int[] playoutResult = map.get(pos[2]);
			/*
			System.out.printf("Legal move: (%d, %d) @%d ==> %f (%d / %d)\n",
												pos[0], pos[1], pos[2],
												ucb1(playoutResult[0], playoutResult[1]),
												playoutResult[1], playoutResult[0]);
			*/
		}
		
		/*
		//�I�񂾎�̃f�[�^��\��
		int[] select_data = array.get(i);
		int[] playout = map.get(select_data[2]); 
		System.out.println("�I�񂾎�̃f�[�^��\��");
		System.out.println("��̔z���̈ʒu : " + i);
		System.out.println("�]�u���X�g�n�b�V���̒l : " + select_data[2]);
		System.out.println("�v���C�A�E�g�� : " + playout[0]);
		System.out.println("���� : " + playout[1]);
		System.out.println("UCB1 : " + ucb1(playout[0], playout[1]));
		System.out.println("���v���C�A�E�g�� : " + total_count);
		*/
		
		//���(x,y)���W
		return Arrays.copyOf(array.get(i), 2);
		
	}
	
	
	//���I�тȂ���T�����Ă������\�b�h
	public int select(mctGameState state) {
		
		//System.out.println("select is called");

		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = putPoint(state);
		
		/*
		state.textDisplay();
		for (int[] pos : array) {
		System.out.printf("can put at (%d, %d)\n", pos[0], pos[1]);
		}
		*/
		
		if (array.size() == 0) {
			// �u���Ȃ���΃p�X������ (�����Ȃ�)
			if (state.isLastPass) {
				// ������p�X�����Ă����ꍇ�� �����Ō��ʂ�Ԃ�
				if (state.black > state.white) return 1;
				if (state.white > state.black) return -1;
				return 0;
			}
			// �p�X������1��i�߂��Ƃ���Ńv���C�A�E�g���Ă��炤
			state.pass();
			return select(state);
		}
		int orgplayer = state.player;
		
		//UCB1�l���������̂�T�����\�b�h(selectUCB)���s��
		int select = selectUCB(array);
		//�I�񂾎�̏����擾����z��
		int[] select_point = array.get(select);

		//�I�񂾎�̋ǖʂ̏����擾����z��
		int[] data = map.get(select_point[2]);

		// 1��i�߂���ŁD�D�D
		state.put(select_point[0], select_point[1]);
		//�I�񂾎�̃v���C�A�E�g����臒l�ȏ�̏ꍇ�Aselect�֐����ċA�I�ɌĂяo��
		int winner = (data[0] >= threshold) ? select(state) : playout(state);
		data[0] ++;
		if (winner == orgplayer){
			//System.out.println("winner=player");
			data[1]++;
			//if(orgplayer == 1 && winner == 1) System.out.println("test");
		//}else {
			//System.out.println("winner!=player");
		}
		//System.out.println("select is End");
		//System.out.println("player : " + orgplayer);
		return winner;
	}

	// �]���l��p���ăv���C
	int playout(mctGameState state) {
		while (true) {
			ArrayList<int[]> array = putPoint(state);

			// System.out.println("IN PLAYOUT:");
			// state.textDisplay();
			// System.out.println("array.size() = " + array.size());

			if (array.size() == 0) {
				// �u���Ȃ���΃p�X������ (�����Ȃ�)
				if (state.isLastPass) {
					// ������p�X�����Ă����ꍇ�� �����Ō��ʂ�Ԃ�
					if (state.black > state.white) return 1;
					if (state.white > state.black) return -1;
					return 0;
				}
				// �p�X������1��i�߂��Ƃ���Ńv���C�A�E�g���Ă��炤
				state.pass();
			} else {
				hyoukaCPU CPU = new hyoukaCPU(1);
				int[] selected = CPU.decide(state);
				state.put(selected[0], selected[1]);
			}
		}
	}
	
	//UCB1�l�̌v�Z�����郁�\�b�h
	//������0�̏ꏊ�͖����ɂȂ邽�ߌv�Z���Ȃ�
	public double ucb1(int count, int win){
		//UCB1���v�Z
		if(count != 0){
			return (double)win / (double)count + Math.sqrt(2 * Math.log(total_count + 1) / count);
		} else {
			return 10.0;
		}
		//System.out.println("UCB1 : " + UCB1);
	}
	
	
	//UCB�l��map�ɒǉ�����悤�ɐ؂�ւ���\��Ȃ̂ŏ����������K�v
	public int selectUCB(ArrayList<int[]> array){
		//�v���C�A�E�g��0�̎�͂܂�map��ɍ���Ă��Ȃ���������Ȃ�
		for (int[] pos : array) {
			if (!map.containsKey(pos[2])) {
				map.put(pos[2], new int[2]);
			}
		}
		
		//�I�񂾎�̏���ۑ�����z��		//map�����r�Ώۂ̃f�[�^���擾����z��
		int select = 0;
		int[] select_data = map.get(array.get(0)[2]);
		
		//�łĂ�肪�ۑ�����Ă���array�z��S�̂ŒT�����s��
		for(int i = 1; i < array.size(); i++){
			//���݂̔�r�Ώۂ̎���擾����z��
			int[] search_point = array.get(i);
			int[] search_data = map.get(search_point[2]);

			//UCB1�l���傫������select�̕��ɂ���
			if (ucb1(select_data[0], select_data[1]) < ucb1(search_data[0], search_data[1])) {
				select = i;
				select_data = search_data;
			}
		}
		
		//System.out.println("UCB : " + ucb1(select_data[0], select_data[1]));
		//�I�񂾎�̔z���̔ԍ���Ԃ�
		return select;
	}
	
	//�p�X�̂Ƃ��̃]�u���X�g�̒l��map�ɒǉ�����֐�
	public void pass(int passZob){
		//�v���C�A�E�g��0�̏ꍇ�͂܂�map��ɍ���Ă��Ȃ��̂ő��݂��Ă��邩�ǂ����Ŕ��f����
		if(map.containsKey(passZob) == false){
			//map�ɓ���邽�߂̋�̔z�������
			//�}�b�v�ɋ�̔z���ǉ�����
			map.put(passZob, new int[2]);
		}
	}
	
	//�u����ꏊ��ArrayList�ŕԂ��֐�
	public ArrayList<int[]> putPoint(mctGameState state) {
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = new ArrayList<int[]>();
		for(int y=1; y<size-1; y++){
			for(int x=1; x<size-1; x++){
				// �u���邩�ǂ����̔���͐�ɍs��
				if (!state.canPut(x, y)) continue;

				// state ��clone���Ď��ۂɓ�������
				mctGameState after = state.clone();
				after.put(x, y);

				// �ǉ�
				int pos[] = {x, y, after.zobhash};
				array.add(pos);
			}
		}
		return array;
	}
	
	//臒l�̍œK�l�����߂�Ƃ���臒l��ݒ肷�邽�߂̃��\�b�h
	public void setThreshold(int t){
		threshold = t;
		//臒l��ݒ肷��Ƃ��Ƀ}�b�v�͏��������s��
		map = new HashMap<>();
	}

}

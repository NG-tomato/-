import java.util.*;

public class fpu_mctCPU extends CPU {
	
	//1��ǂނ��Ƃ̑��v���C�A�E�g��
	int count = 50;
	
	//1��ǂނ��Ƃ̎���(ms�~���b�Ȃ̂ŁC1�b=1000ms)
	long time = 300;
	
	//���Ԃƃv���C�A�E�g���ǂ������g�p���邩(true�̂Ƃ��v���C�A�E�g���Cfalse�̂Ƃ�����)
	boolean switch_threshold = true;
	
	//�T����[������Ƃ��̂������l
	int threshold = 39;//38;//38;

	//UCB1�A���S���Y���̒萔C
	double C = 0.39;//0.39;

	//fpu�̒l
	double fpu = 2.47;//4.82;//100;

	//�[�����Ƃ̃v���C�A�E�g����ۑ�����z��
	int[] total_count = new int[61];
	
	
	//map�ɓ����f�[�^�̔z��
	//{�v���C�A�E�g��,����}
	int[] data = new int[3];
	
	//�f�[�^������map
	//�v���C�A�E�g���A�|�C���g�i���̋ǖʂ̃v���C���̏����j�AUCB1�l
	Map<Integer, int[]> map = new HashMap<>();	
	
	//���I�Ԃ��Ƃ̃v���C�A�E�g�̕��ώ��Ԃ����郊�X�g
	ArrayList<Long> avePlayout = new ArrayList<Long>();

	//�v���C�A�E�g�̉񐔂𐔂���ϐ�
	ArrayList<Integer> aveCount = new ArrayList<Integer>();
			
	//�v���C�A�E�g�̉񐔂̕ϐ�
	int playoutCount = 0;

	//�N���X���쐬����ۂɁA�ǂ����̃v���C�����I��
	public fpu_mctCPU(int c){
		super(c);
	}
		
	//���I�ԃ��\�b�h
	//��������s���邱�ƂŎ��I��
	@Override
	int[] decide(GameState state){
		mctMainPanel p = new mctMainPanel(state.data , state.turn, state.player);
		
		//�u����ꏊ�����X�g�Ɏ擾
		//putPoint���\�b�h��mctMainPanel�̒l��^���邱�ƂŒu����ꏊ�̃f�[�^����ꂽ�ϐ���Ԃ�
		ArrayList<int[]> array = putPoint(p.state);
		
		//�v���C�A�E�g�̎��Ԍv���J�n
		long start = System.currentTimeMillis();
		
		
		//select�֐���p���ăv���C�A�E�g���Ă���
		//臒l��I��
		//�v���C�A�E�g��臒l
		if(switch_threshold){
			for(int i=0; i < count; i++){
				/*	selsect�֐���UCB1�l���������̂�I��Ńv���C�A�E�g���A���ʂ�map�֐��ɓK������ϐ�	�v���C�A�E�g�������ȏ�̏ꍇ�A�v���C�A�E�g�͍s�킸�A���ɍċA�I�Ƀ��\�b�h����邱�ƂŒT����[�߂Ă���
				*/
				int x = select(p.state.clone());
			}
		}else{
			//���Ԃ�臒l
			for(long i = start; (i - start) <= time;i = System.currentTimeMillis()){
				int x = select(p.state.clone());
			}

		}
		
		//�v���C�A�E�g�̎��Ԍv���I��
		long end = System.currentTimeMillis();
		//�v���C�A�E�g���ԁicount���Ɓj
		long PlayoutTime = end - start;
		//�v���C�A�E�g���Ԃ����X�g�ɒǉ�
		avePlayout.add(PlayoutTime);
		//�v���C�A�E�g�񐔂����X�g�ɒǉ�
		aveCount.add(playoutCount);
		//�v���C�A�E�g�̉񐔂̕ϐ���������
		playoutCount = 0;



		
		//�|�C���g���ő�̎�����߂�
		int i = selectUCB(array,state.black + state.white - 4);

		for (int[] pos : array) {
			int[] playoutResult = map.get(pos[2]);
		}
		
		//���(x,y)���W
		return Arrays.copyOf(array.get(i), 2);
		
	}
	
	
	//���I�тȂ���T�����Ă������\�b�h
	public int select(mctGameState state) {
		
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = putPoint(state);
				
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
		int select = selectUCB(array, state.putNumber());
		//�I�񂾎�̏����擾����z��
		int[] select_point = array.get(select);

		//�I�񂾎�̋ǖʂ̏����擾����z��
		int[] data = map.get(select_point[2]);
		
		
		// 1��i�߂���ŁD�D�D
		state.put(select_point[0], select_point[1]);
		
		//���󂪉���܂őł�����ԁi���؂̐[���j�������ϐ�
		int t = state.putNumber();

		//�I�񂾎�̃v���C�A�E�g����臒l�ȏ�̏ꍇ�Aselect�֐����ċA�I�ɌĂяo��
		int winner = (data[0] >= threshold) ? select(state) : playout(state);
		total_count[t]++;
		data[0] ++;
		if (winner == orgplayer) data[1]++;
		return winner;
	}

	// ���S�Ƀ����_���v���C
	int playout(mctGameState state) {
		while (true) {
			ArrayList<int[]> array = putPoint(state);

			if (array.size() == 0) {
				// �u���Ȃ���΃p�X������ (�����Ȃ�)
				if (state.isLastPass) {	
					//�v���C�A�E�g�񐔂̉��Z
					playoutCount++;
					// ������p�X�����Ă����ꍇ�� �����Ō��ʂ�Ԃ�
					if (state.black > state.white) return 1;
					if (state.white > state.black) return -1;
					return 0;
				}
				// �p�X������1��i�߂��Ƃ���Ńv���C�A�E�g���Ă��炤
				state.pass();
			} else {
				int selected = new Random().nextInt(array.size());
				state.put(array.get(selected)[0], array.get(selected)[1]);
			}
		}
	}
	
	//UCB1�l�̌v�Z�����郁�\�b�h
	//������0�̏ꏊ�͖����ɂȂ邽�ߌv�Z���Ȃ�
	public double ucb1(int count, int win, int total){
		//UCB1���v�Z
		if(count != 0){
			return (double)win / (double)count + C * Math.sqrt(2 * Math.log(total_count[total] + 1) / count);
		} else {
			return fpu;
		}
	}
	
	
	//UCB�l��map�ɒǉ�����悤�ɐ؂�ւ���\��Ȃ̂ŏ����������K�v
	public int selectUCB(ArrayList<int[]> array, int total){
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
			if (ucb1(select_data[0], select_data[1], total) < ucb1(search_data[0], search_data[1], total)) {
				select = i;
				select_data = search_data;
			}
		}
		
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
	
	@Override
	public void reset(){
		map = new HashMap<>();
		total_count = new int[61];
		avePlayout = new ArrayList<Long>();
		aveCount = new ArrayList<Integer>();
	}
	
	//臒l�̍œK�l�����߂�Ƃ���臒l��ݒ肷�邽�߂̃��\�b�h
	@Override
	public void setThreshold(int t){
		threshold = t;
		//臒l��ݒ肷��Ƃ��Ƀ}�b�v�ƃv���C�A�E�g���̏��������s��
		reset();
	}
	
	//fpu�̍œK�l�����߂�Ƃ���臒l��ݒ肷�邽�߂̃��\�b�h
	@Override
	public void setFPU(double f){
		fpu = f;
		//臒l��ݒ肷��Ƃ��Ƀ}�b�v�ƃv���C�A�E�g���̏��������s��
		reset();
	}
		
	//�萔C�̍œK�l�����߂�Ƃ���臒l��ݒ肷�邽�߂̃��\�b�h
	@Override
	public void setC(double teisu){
		 C = teisu;
		//臒l��ݒ肷��Ƃ��Ƀ}�b�v�ƃv���C�A�E�g���̏��������s��
		reset();
	}
	
	//�v���C�A�E�g�̕��ώ��Ԃ�Ԃ����\�b�h
	@Override
	public long avePlayout(){
		int x = avePlayout.size();
		long sum=0;
		for(int i=0;i<x;i++){
			long add = avePlayout.get(i);
			sum += add;
		}
		return (long)sum/x;
	}
	
	//�v���C�A�E�g�̕��ω񐔂�Ԃ����\�b�h
	@Override
	public double aveCount(){
		int x = aveCount.size();
		int sum=0;
		for(int i=0;i<x;i++){
			int add = aveCount.get(i);
			sum += add;
		}
		return (double)sum/x;
	}


}

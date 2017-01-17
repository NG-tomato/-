import java.util.*;

public class mctCPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;
	
	//�Ղ̑傫���̕ϐ��i�Ǌ܂ށj
	int size = 10;
	
	//1��ǂނ��Ƃ̑��v���C�A�E�g��
	int count = 100;
	
	//�v���C�A�E�g���s�����񐔂�ۑ�����ϐ�
	int total_count = 0;
	
	//�T����[������Ƃ��̂������l
	int threshold = 20;
	
	//map�ɓ����f�[�^�̔z��
	//{�v���C�A�E�g��,����}
	int[] data = new int[3];
	
	//�f�[�^������map
	//�v���C�A�E�g���A�|�C���g�i���̋ǖʂ̃v���C���̏����j�AUCB1�l
	Map<Integer, double[]> map = new HashMap<>();
	
	//�N���X���쐬����ۂɁA�ǂ����̃v���C�����I��
	public mctCPU(int c){
		color = c;
	}
	
	//���I�ԃ��\�b�h
	//��������s���邱�ƂŎ��I��
	int[] decide(GameState state){
		
		
		mctMainPanel p = new mctMainPanel(state.data , state.turn, state.player);
		
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//�Ֆʂ̋�}�X��u���邩�`�F�b�N
		for(int y=1; y<size-1; y++){
			for(int x=1; x<size-1; x++){
				
				//���łɋ����Ƃ��̓p�X
				if(p.state.data[x + y * size] != 0)
					continue;
				
				//�u����}�X�̂Ƃ��A���Ƃ��ċL��
				//mctMainPanel�N���X��reverseZob(x,y)�֐��́C(x,y)�ɒu����Ƃ��u�����Ƃ��̃]�u���X�g�n�b�V���̒l��Ԃ�
				int zobrist = p.reverseZob(x, y);
				if(zobrist != 0){
					
					//[x,y]��2�̗v�f�����z��Ƃ��ċL������
					int pos[] = {x,y,zobrist};
					
					//[x,y]�̔z���u����ꏊ���L�����郊�X�g�ɒǉ�����
					array.add(pos);
				}
				
			}
		}
		
		
		//�u����ꏊ���Ȃ��ꍇ
		//�ǋL�K�v�i�p�X�̓���ŒT�����s���悤�ɂ���j<-----------------------
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return pos;
		}
		
		//select�֐���p���ăv���C�A�E�g���Ă���
		for(int i=0; i < count; i++){
			
			//1��̃v���C�A�E�g���Ƃ�total_count�ϐ������Z���Ă������Ƃł����܂ł̃v���C�A�E�g�̑��v�����߂�
			total_count ++;

			//selsect�֐���UCB1�l���������̂�I��Ńv���C�A�E�g���A���ʂ�map�֐��ɓK������ϐ�
			//�z��v���C�A�E�g�������ȏ�̏ꍇ�A�v���C�A�E�g�͍s�킸�A���ɍċA�I�Ƀ��\�b�h����邱�ƂŒT����[�߂Ă���
			int x = select(p);
			
		}
		
		
		//�|�C���g���ő�̎�����߂�
		int i = selectUCB(array);
		
		//�I�񂾎�̃f�[�^��\��
		/*
		int[] select_data = array.get(i);
		double[] playout = map.get(select_data[2]); 
		System.out.println("�I�񂾎�̃v���C�A�E�g�� : " + playout[0]);
		System.out.println("�I�񂾎�̏��� : " + playout[1]);
		System.out.println("�I�񂾎��UCB1 : " + playout[2]);
		*/
		
		//���(x,y)���W
		return Arrays.copyOf(array.get(i), 2);
		
	}
	
	
	//���I�тȂ���T�����Ă������\�b�h
	public int select(mctMainPanel p){
		
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = new ArrayList<int[]>(3);
		
		//�v���C�A�E�g�̏��҂������ϐ�
		int winner = 2;
		
		//���̑I���̃v���C����put����O�ɕۑ�����ϐ�
		int player = p.state.player;


		//�Ֆʂ̋�}�X��u���邩�`�F�b�N
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
				//���łɋ����Ƃ��̓p�X
				if(p.state.data[x + y * size] != 0)
					continue;
				
				//���̎�̃]�u���X�g�n�b�V���̒l���擾
				//reverseZob(x,y)�֐��́C(x,y)�ɒu����ꍇ�͒u�����Ƃ��̃]�u���X�g�n�b�V���̒l�A�u���Ȃ��ꍇ��0��Ԃ�
				int zobrist = p.reverseZob(x, y, Arrays.copyOf(p.state.data, p.state.data.length), p.turn, p.player);
				
				
				/*
				//���̃]�u���X�g�̒l��map�����݂��Ă���ꍇ
				if(map.containsKey(zobrist)){
					//���̃]�u���X�g�̒l�̃f�[�^���擾����
					int[] data = map.get(zobrist);
				}
				*/
				
				//�u����}�X�̂Ƃ�(zobrist = 0)�A���Ƃ��ċL��
				if(zobrist != 0){
					
					//[x,y,zobrist]��3�̗v�f�����z��Ƃ��ċL������
					int pos[] = {x,y,zobrist};
					
					//[x,y,zobrist]�̔z���u����ꏊ���L�����郊�X�g�ɒǉ�����
					array.add(pos);
				}
				
			}
		}
		
		
		
		//UCB1�l���������̂�T�����\�b�h(selectUCB)���s��
		int select = selectUCB(array);
		
		//�I�񂾎�̏����擾����z��
		int[] select_point = new int[3];
		//�I�񂾎�̋ǖʂ̏����擾����z��
		double[] data = new double[3];
		
		//select��-1�̂Ƃ��I�Ԏ肪���݂��Ȃ��Ƃ�(�p�X�̂Ƃ�)
		if(select == -1){
			int passZob = p.passZob(Arrays.copyOf(p.state.data, p.state.data.length), p.turn, p.player);
			//passZob����̂Ƃ��ۑ��ꏊ��map�ɒǉ����郁�\�b�h
			pass(passZob);
		}else
		//select��-1�łȂ�(�I�Ԏ肪���݂���)�Ƃ�
		{
			select_point = array.get(select);
		}

		data = map.get(select_point[2]);
		
		//�I�񂾎�̃v���C�A�E�g����臒l�ȏ�̏ꍇ�Aselect�֐����ċA�I�ɌĂяo��
		if(data[0] >= threshold){
			//�I�񂾎�̏�ԂɈړ�
			//�p�X�̂Ƃ�
			if(array.size() == 0){
				p.state.pass();
			}
			//�p�X�łȂ��Ƃ�
			else{
				p.state.put(select_point[0], select_point[1]);
			}
			winner = select(p);
		}
		//select�֐����Ăяo����Ă��Ȃ��Ƃ��v���C�A�E�g������
		else{
			//-------------------�v���C�A�E�g������
			winner = p.mctGame();
		}
		
		//�v���C�A�E�g�������Z����
		data[0] ++;
		
		//�v���C���[�������Ă���ꍇ�|�C���g�����Z
		if(winner == player){
			data[1] ++;
		}
		
		//UCB1���\�b�h��data����UCB1�l���X�V
		UCB1(select_point[2]);
		
		
		return winner;

	}
	
	
	//UCB1�l�̌v�Z�����郁�\�b�h
	public void UCB1(int zob){
		//�f�[�^���擾����z��
		double[] data = map.get(zob);
		//UCB1���v�Z
		double UCB1 = data[1] + Math.sqrt(2 * total_count / Math.log(data[0]) );
		//UCB1��ύX
		data[2] = UCB1;
	}
	
	
	//UCB�l��map�ɒǉ�����悤�ɐ؂�ւ���\��Ȃ̂ŏ����������K�v
	public int selectUCB(ArrayList<int[]> array){
		
		//�I�񂾎�̏���ۑ�����z��
		int select = -1;
				
		//map�����r�Ώۂ̃f�[�^���擾����z��
		double[] select_data = new double[3];
		
		//�łĂ�肪�ۑ�����Ă���array�z��S�̂ŒT�����s��
		for(int i = 0; i < array.size(); i++){
			//���݂̔�r�Ώۂ̎���擾����z��
			int[] search_point = Arrays.copyOf(array.get(i), 3);
			
			//�v���C�A�E�g����0�̏ꍇ���̎��I������i���̂Ƃ�UCB1�l�������ɂȂ��Ă��܂��̂Ő�ɏ��O����j
			//�v���C�A�E�g��0�̎�͂܂�map��ɍ���Ă��Ȃ��̂ő��݂��Ă��邩�ǂ����Ŕ��f����
			if(map.containsKey(search_point[2]) == false){
				//map�ɓ���邽�߂̋�̔z�������
				double[] put_data = new double[3];
				//�}�b�v�ɋ�̔z���ǉ�����
				map.put(search_point[2], put_data);
				//���݂̎��I�񂾎�ɂ���
				select = i;
				break;
			}
			//map�̃f�[�^���擾
			double[] search_data = map.get(search_point[2]);
			//UCB1�l���傫������select�̕��ɂ���
			if(select_data[2] < search_data[2]){
				select_data = Arrays.copyOf(search_data, 3);
				//���݂̎��I�񂾎�ɂ���
				select = i;
			}
		}
		//�I�񂾎�̔z���̔ԍ���Ԃ�
		return select;
	}
	
	public void pass(int passZob){
		//�v���C�A�E�g��0�̏ꍇ�͂܂�map��ɍ���Ă��Ȃ��̂ő��݂��Ă��邩�ǂ����Ŕ��f����
		if(map.containsKey(passZob) == false){
			//map�ɓ���邽�߂̋�̔z�������
			double[] put_data = new double[3];
			//�}�b�v�ɋ�̔z���ǉ�����
			map.put(passZob, put_data);
		}
	}

}
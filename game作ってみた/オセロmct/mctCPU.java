import java.util.*;

public class mctCPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;	//BLACK or WHITE
	int size = 10;
	
	//1��ǂނ��Ƃ̑��v���C�A�E�g��
	int count = 100;
	
	//�v���C�A�E�g�̑��v
	int total_count = 0;
	
	//���̒T���Ɉڂ�Ƃ��̂������l
	int threshold = 10;
	
	//map�ɓ����f�[�^�̔z��
	//{�v���C�A�E�g��,����}
	int[] data = new int[3];
	
	//�f�[�^������z��
	//
	Map<Integer, ArrayList<Integer>> map = new HashMap<>();
	
	
	public mctCPU(int c){
		color = c;
	}
	
	int[] decide(GameState state){
		
		
		mctMainPanel p = new mctMainPanel(count, state.data , state.turn, state.player);
		
		
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//�Ֆʂ̋�}�X��u���邩�`�F�b�N
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
				//���łɋ����Ƃ��̓p�X
				if(p.state.data[x + y * size] != 0)
					continue;
				
				//�u����}�X�̂Ƃ��A���Ƃ��ċL��
				int zobrist = p.reverseZob(x, y);
				if(zobrist != 0){
					
					//[x,y]��2�̗v�f�����z��Ƃ��ċL������
					int pos[] = {x,y,zobrist};
					
					//[x,y]�̔z���u����ꏊ���L�����郊�X�g�ɒǉ�����
					array.add(pos);
				}
				
			}
		}
		
		
		//�u����ꏊ���Ȃ��ꍇ�͍��W��{-1,-1}�Ƃ��ĕԂ�
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return pos;
		}
		
		//select�֐���p���ăv���C�A�E�g���Ă���
		for(int i=0; i < count; i++){
			//int a[] = array.get(i);
			//p.mcGame(a);
			int x = select(p);
			//1��̃v���C�A�E�g���Ƃ�total_count�ϐ������Z���Ă���
			total_count ++;
		}
		
		//�|�C���g���ő�̎�����߂�
		int j = 0;
		double point = 0;
		for(int i = 0; i < array.size(); i++){
			int a[] = array.get(i);
			ArrayList<Integer> b = map.get(a[2]);
			//UCB1�l�̌v�Z
			double P = UCB1(b.get(2));
			if(P > point){
				point = P;
				j = i;
			}
			total_count += count;
		}
		
		return array.get(j);
		
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
				int zobrist = p.reverseZob(x, y);
				if(map.containsKey(zobrist)){
					//���ׂĒT���ς݂ŕ]�����Ă��Ӗ����Ȃ��ꏊ�̏ꍇ���p�X
					ArrayList<Integer> data = map.get(zobrist);
					if(data.size()==3){
						continue;
					}
				}
				
				//�u����}�X�̂Ƃ��A���Ƃ��ċL��
				if(zobrist != 0){
					
					//[x,y]��2�̗v�f�����z��Ƃ��ċL������
					int pos[] = {x,y,zobrist};
					
					//[x,y]�̔z���u����ꏊ���L�����郊�X�g�ɒǉ�����
					array.add(pos);
				}
				
			}
		}
		
		//�u����ꏊ���Ȃ��ꍇ
		//map���̃��X�g�ɒl��ǉ����A�z��̑傫����ω������邱�ƂŁA�T�����K�v�Ȃ������Ƃ������f���ł���悤�ɂ���
		if(array.size() <= 0){
			ArrayList<Integer> data = map.get(p.state.zob.zobrist);
			data.add(1);
			return 2;
		}
		
		int select = selectUCB(array);
		int[] select_point = array.get(select);
		
		ArrayList<Integer> data = new ArrayList<Integer>(3);
		data = map.get(select_point[2]);
		
		
		//select�֐����Ăяo���ꂽ���ǂ�����ۑ�����ϐ�
		boolean do_select = false;
		while(winner == 2){
			winner = 0;
			//�I�񂾎�̃v���C�A�E�g����臒l�ȏ�̏ꍇ�Aselect�֐����ċA�I�ɌĂяo��
			if(data.get(0) >= threshold){
				//�I�񂾎�̏�ԂɈړ�
				p.state.put(select_point[0], select_point[1]);
				winner = select(p);
				do_select = true;
			}
			//�I�񂾎肪�T�����Ă��Ӗ����Ȃ���̏ꍇ
			if(winner == 2){
				array.remove(select);
				if(array.size() == 0){
					return 2;
				}
				selectUCB(array);
			}
		}
		
		//select�֐����Ăяo����Ă��Ȃ��Ƃ��v���C�A�E�g������
		if(do_select == false){
			//-------------------�v���C�A�E�g������
			winner = p.mctGame();
		}
		
		//�v���C�A�E�g�������Z����
		data.set(0, data.get(0) + 1);
		
		//�v���C���[�������Ă���ꍇ�|�C���g�����Z
		if(winner == player){
			data.set(1, data.get(1) + 1);
		}
		return winner;

	}
	
	
	public double UCB1(int zob){
		ArrayList<Integer> data = new ArrayList<Integer>(3);
		data = map.get(zob);
		double UCB1 = data.get(1) + Math.sqrt(2 * total_count / Math.log(data.get(0)) );
		return UCB1;
	}
	
	public int selectUCB(ArrayList<int[]> array){
		//UCB1�l�̍������̂�I��
		int select = 0;
		int[] select_point = new int[3];
		int[] search_point = new int[3];
		double select_UCB1 = 0;
		double search_UCB1 = 0;
		int i;
		for(i = 0; i < array.size(); i++){
			search_point = Arrays.copyOf(array.get(i), 3);
			//�v���C�A�E�g����0�̏ꍇ���̎��I������i���̂Ƃ�UCB1�l�������ɂȂ��Ă��܂��̂Ő�ɏ��O����j
			//�v���C�A�E�g��0�̎�͂܂�map��ɍ���Ă��Ȃ��̂ŁA���
			if(map.containsKey(search_point[2]) == false){
				select_point = search_point;
				ArrayList<Integer> put_data = new ArrayList<Integer>(2);
				map.put(search_point[2], put_data);
				break;
			}
			//UCB1�l���v�Z
			search_UCB1 = UCB1(search_point[2]);
			//UCB1�l���傫������select�̕��ɂ���
			if(select_UCB1 < search_UCB1){
				select_point = Arrays.copyOf(search_point, 3);
				select_UCB1 = search_UCB1;
			}
		}
		return i;
	}
}
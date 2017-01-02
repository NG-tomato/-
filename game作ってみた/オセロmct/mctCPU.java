import java.util.*;

public class mctCPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;	//BLACK or WHITE
	int size = 10;
	
	//1��ǂނ��Ƃ̑��v���C�A�E�g��
	int count = 10;
	
	//�v���C�A�E�g�̑��v
	int total_count = 0;
	
	//���̒T���Ɉڂ�Ƃ��̂������l
	int threshold = 5;
	
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
		
		
				
		for(int i=0; i < array.size(); i++){
			int a[] = array.get(i);
			p.mcGame(a);
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
		int winner = 0;
		
		//���̑I���̃v���C����put����O�ɕۑ�����ϐ�
		int player = p.state.player;

		
		//�Ֆʂ̋�}�X��u���邩�`�F�b�N
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
				//���łɋ����Ƃ��̓p�X
				if(p.state.data[x + y * size] != 0)
					continue;
				
				//�u����}�X�̂Ƃ��A���Ƃ��ċL��
				//����ɉ����ă]�u���X�g�n�b�V���̒l���L��
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
		//-----------------------���̂Ƃ��̏����ɂ��Ă��Ƃōl����K�v����
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return 0;
		}

		
		
		//UCB1�l�̍������̂�I��
		int select = 0;
		int[] select_point = new int[3];
		int[] search_point = new int[3];
		double select_UCB1 = 0;
		double search_UCB1 = 0;
		for(int i = 0; i < array.size(); i++){
			search_point = Arrays.copyOf(array.get(i), 3);
			//�v���C�A�E�g����0�̏ꍇ���̎��I������i���̂Ƃ�UCB1�l�������ɂȂ��Ă��܂��̂Ő�ɏ��O����j
			//�v���C�A�E�g��0�̎�͂܂�map��ɍ���Ă��Ȃ��̂ŁA���
			if(map.containsKey(search_point[2])){
				select_point = search_point;
				ArrayList<Integer> put_data = new ArrayList<Integer>(3);
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
		ArrayList<Integer> data = new ArrayList<Integer>(3);
		data = map.get(search_point[2]);
		
		//�I�񂾎�̃v���C�A�E�g����臒l�ȏ�̏ꍇ�Aselect�֐��ċA�I�ɌĂяo��
		//����ȊO�̎��̓v���C�A�E�g������
		if(data.get(0) >= threshold){
			//臒l����
			p.state.put(select_point[0], select_point[2]);
			winner = select(p);
		}else{
			//-------------------�v���C�A�E�g������
			winner = 0;
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
		double UCB1 = data.get(2) + Math.sqrt(2 * total_count / Math.log(data.get(0)) );
		return UCB1;
	}
}
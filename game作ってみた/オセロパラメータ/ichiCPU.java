import java.util.*;

public class ichiCPU extends CPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;	//BLACK or WHITE
	int size = 10;
	
	private int[] valueMap = {  45, -11,   4,  -1,  -1,   4, -11,  45, 
								11, -16,  -1,  -3,  -3,  -1, -16, -11,
								 4,  -1,   2,  -1,  -1,   2,  -1,   4,
		                        -1,  -3,  -1,   0,   0,  -1,  -3,  -1,
		                        -1,  -3,  -1,   0,   0,  -1,  -3,  -1,
		                         4,  -1,   2,  -1,  -1,   2,  -1,   4,
		                       -11, -16,  -1,  -3,  -3,  -1, -16, -11,
		                        45, -11,   4,  -1,  -1,   4, -11,  45	}; 

	//�����_���N���X�̃C���X�^���X��
	Random rnd = new Random();
	
	public ichiCPU(int c){
		color = c;
	}
	
	int[] decide(GameState state){
		
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//�Ֆʂ̋�}�X��u���邩�`�F�b�N
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
				//���łɋ����Ƃ��̓p�X
				if(state.data[x + y * 10] != 0)
					continue;
				
				//�u����}�X�̂Ƃ��A���Ƃ��ċL��
				if(state.canReverse(x, y) == true){
					
					//[x,y]��2�̗v�f�����z��Ƃ��ċL������
					int pos[] = {x,y,0};
					
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
		//�u����ꏊ��1�̏ꍇ�͂��̎��Ԃ�
		else if(array.size() == 1){
			int pos[] = array.get(0);
			int data[] = {pos[0] , pos[1]};
			return data;
		}
		//�T���̕K�v������ꍇ�͕]�����s���ē_��������
		for(int i = 0;i<array.size();i++){
			int[] pos = array.get(i);
			hyoukaPoint(pos, state);
			//System.out.println(pos[2]);
			//���ݒT�����Ă���Ƃ���܂łœ���ւ����s��
			//�_���������قǑO�A�Ⴂ�قǌ��ɔz�u�����
			//�}���\�[�g
			for(int j = 0;j < i;j++){
				int[] serch_pos = array.get(j);
				if(serch_pos[2] < pos[2]){
					int[] copyArray = Arrays.copyOf(pos ,3);
					array.remove(i);
					array.add(j,copyArray);
					break;
				}
			}
		}
		
		//System.out.println("Print of put point");
		for(int i=0;i<array.size();i++){
			int[] pos = array.get(i);
			/*
			System.out.println("x = " + pos[0] + ": y = " + pos[1]);
			System.out.println("point = "+ pos[2]);
			*/
		}
		
		//�u���ꏊ��Ԃ�
		return array.get(0);
	}
	
	
	//�]�������_����Ԃ����\�b�h
	public void hyoukaPoint(int[] pos, GameState state){
		int player = state.player;
		//state��V�������C���̓_����Ԃ�
		GameState s = new GameState();
		s.set(state.data, state.turn, state.player);
		//���ł�����Ԃ֑J��
		s.put(pos[0] ,pos[1]);
		//�ʒu�ɂ��]��
		int bp = banPoint(s);
		pos[2] = bp;
		//System.out.println(pos[2]);
	}
	
	//�ʒu�ɂ��]�����s�����\�b�h
	public int banPoint(GameState state){
		//������O�̃v���C���[�̂��߂̕]���Ȃ̂�-1���ăv���C���[��łO�ɂ��ǂ�
		int player = state.player * -1;
		int sum = 0;
		
		int[] data = state.data;
		//�ʒu���ƂɌv�Z���s�����Z���Ă���
		for(int i = 1;i <= 8; i++){
			for(int j = 1; j <= 8; j++){
				if(data[i + j * 10] != 0){
					sum += (data[i + j*10] * valueMap[(i-1) + (j-1) * 8]) + (int) Math.floor(Math.random() * 3);
				}
			}
		}
		//�����v���C���[�̂Ƃ����l�𔽓]������
		return sum * player;
	}


	
}
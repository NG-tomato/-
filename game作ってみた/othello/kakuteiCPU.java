import java.util.*;

public class kakuteiCPU extends CPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;	//BLACK or WHITE
	int size = 10;
	
	//�����_���N���X�̃C���X�^���X��
	Random rnd = new Random();
	
	public kakuteiCPU(int c){
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
		
		System.out.println("Print of put point");
		for(int i=0;i<array.size();i++){
			int[] pos = array.get(i);
			System.out.println("x = " + pos[0] + ": y = " + pos[1]);
			System.out.println("point = "+ pos[2]);
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
		int bp = fixStone(s);
		pos[2] = bp;
		//System.out.println(pos[2]);
	}
	
	//�m��΂̐���Ԃ����\�b�h
	//�S���T���͓̂���̂ŕӂ̂�
	public int fixStone(GameState state){
		//���ꂼ��̐F�̊m��΂̐���ۑ�����z��
		//{���̊m��ΐ��C���̊m��ΐ�}
		int[] fs = new int[2];
		
		int[] data = state.data;
		//�p�̐΂̔z����쐬
		int[] corner = { data[1+1*10], data[8+1*10], data[1+8*10], data[8+8*10] };
		
		//�p������ꍇ����͊m��΂Ȃ̂ŉ��Z
		for(int i=0; i < corner.length; i++){
			if(data[i]==1){
				fs[0]++;
			}else if(data[i]==-1){
				fs[1]++;
			}
		}
		
		//��̉����̊m���
		int[] line = {corner[0], corner[1]};
		int[] add_fs = fsHolLine(line, 1, state);
		fs[0] += add_fs[0];fs[1]+=add_fs[1];
		//���̉����̊m���
		line[0] = corner[2];line[1] = corner[3];
		add_fs = fsHolLine(line, 8, state);
		fs[0] += add_fs[0]; fs[1]+=add_fs[1];
		//���̏c���̊m���
		line[0] = corner[0];line[1] = corner[2];
		add_fs = fsVerLine(line, 8, state);
		fs[0] += add_fs[0]; fs[1]+=add_fs[1];
		//�E�̏c���̊m���
		line[0] = corner[1];line[1] = corner[3];
		add_fs = fsVerLine(line, 8, state);
		fs[0] += add_fs[0]; fs[1]+=add_fs[1];


		
		//�ł�����Ȃ̂ŋt�̃v���C���̊m��ΐ��ŕԂ�
		if(state.player == 1){
			return (fs[1] - fs[0] + (int) Math.floor(Math.random() * 3)) * 11;
		}else{
			return (fs[0] - fs[1] + (int) Math.floor(Math.random() * 3)) * 11;
		}
	}
	
	//�����т̕ӂ̊m��΂����߂郁�\�b�h
	public int[] fsHolLine(int[] corner,int y, GameState state){
		//���ꂼ��̐F�̊m��΂̐���ۑ�����z��
		//{���̊m��ΐ��C���̊m��ΐ�}
		int[] fs = new int[2];
		
		int[] data = state.data;

		//��̕�
		CheckTop:{
			//�ǂ����̒[������ꍇ
			if(corner[0] != 0 && corner[1] != 0){
				int b_fs = 0;
				int w_fs = 0;
				//�S�����܂��Ă���ꍇ���ׂĊm���
				for(int i = 2;i<=7;i++){
					//�󂫂�����̂Ŋm��΂Ɣ��f��������for�ֈڂ�
					if(data[i + 10 * y] == 0){
						b_fs = 0;
						w_fs = 0;
						break;
					}else if(data[i + 10 * y] == 1){
						b_fs ++;
					}else{
						w_fs ++;
					}
					//�Ō�܂ŒT�����I�����(i==7)�Ƃ�
					if(i == 7){
						//�󂫂��Ȃ��̂Ŋm��΂Ƃ��ĉ��Z���ď�̕ӂ̒T�����I���
						fs[0] += b_fs; fs[1] += w_fs;
						break CheckTop;
					}
				}
			}
			/*
			�ǂ����̒[�����邩�S�����܂��ĂȂ��܂��́C�E�[��������ꍇ
			�E�[���珇�ԂɒT��
			�v���C���Ɠ����΂��A�����Ă���ꍇ�m��΂Ɣ��f
			�����łȂ��ꍇ�����ŒT�����I������
			*/
			if(corner[0]!=0){
				for(int i = 2;i <= 7;i++){
					//�󂫂܂��͕ʂ̐΂�����ꍇ�����ŏ������I��
					if(corner[0]!=data[i+10*y]){
						break;
					}
					if(corner[0] == 1){
						fs[0]++;
					}else{
						fs[1]++;
					}
				}
			}
			/*
			�ǂ����̒[�����邩�S�����܂��ĂȂ��܂��́C���[��������ꍇ
			���[���珇�ԂɒT��
			��Ƌt�̏��œ��l�̏���
			*/
			if(corner[1]!=0){
				for(int i = 7;i <= 2;i++){
					//�󂫂܂��͕ʂ̐΂�����ꍇ�����ŏ������I��
					if(corner[1]!=data[i+10*y]){
						break;
					}
					if(corner[1] == 1){
						fs[0]++;
					}else{
						fs[1]++;
					}
				}
			}
			
		}
		return fs;
		
	}
	
	//�c���т̕ӂ̊m��΂����߂郁�\�b�h
	public int[] fsVerLine(int[] corner,int x, GameState state){
		//���ꂼ��̐F�̊m��΂̐���ۑ�����z��
		//{���̊m��ΐ��C���̊m��ΐ�}
		int[] fs = new int[2];
		
		int[] data = state.data;

		//��̕�
		Check:{
			//�ǂ����̒[������ꍇ
			if(corner[0] != 0 && corner[1] != 0){
				int b_fs = 0;
				int w_fs = 0;
				//�S�����܂��Ă���ꍇ���ׂĊm���
				for(int i = 2;i<=7;i++){
					//�󂫂�����̂Ŋm��΂Ɣ��f��������for�ֈڂ�
					if(data[x + 10 * i] == 0){
						b_fs = 0;
						w_fs = 0;
						break;
					}else if(data[x + 10 * i] == 1){
						b_fs ++;
					}else{
						w_fs ++;
					}
					//�Ō�܂ŒT�����I�����(i==7)�Ƃ�
					if(i == 7){
						//�󂫂��Ȃ��̂Ŋm��΂Ƃ��ĉ��Z���ď�̕ӂ̒T�����I���
						fs[0] += b_fs; fs[1] += w_fs;
						break Check;
					}
				}
			}
			/*
			�ǂ����̒[�����邩�S�����܂��ĂȂ��܂��́C�E�[��������ꍇ
			�E�[���珇�ԂɒT��
			�v���C���Ɠ����΂��A�����Ă���ꍇ�m��΂Ɣ��f
			�����łȂ��ꍇ�����ŒT�����I������
			*/
			if(corner[0]!=0){
				for(int i = 2;i <= 7;i++){
					//�󂫂܂��͕ʂ̐΂�����ꍇ�����ŏ������I��
					if(corner[0]!=data[x+10*i]){
						break;
					}
					if(corner[0] == 1){
						fs[0]++;
					}else{
						fs[1]++;
					}
				}
			}
			/*
			�ǂ����̒[�����邩�S�����܂��ĂȂ��܂��́C���[��������ꍇ
			���[���珇�ԂɒT��
			��Ƌt�̏��œ��l�̏���
			*/
			if(corner[1]!=0){
				for(int i = 7;i <= 2;i++){
					//�󂫂܂��͕ʂ̐΂�����ꍇ�����ŏ������I��
					if(corner[1]!=data[x+10*i]){
						break;
					}
					if(corner[1] == 1){
						fs[0]++;
					}else{
						fs[1]++;
					}
				}
			}
			
		}
		return fs;
		
	}


	
}
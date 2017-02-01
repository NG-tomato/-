import java.util.*;

public class hyoukaCPU extends CPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;	//BLACK or WHITE
	int size = 10;
	
	//�����_���N���X�̃C���X�^���X��
	Random rnd = new Random();
	
	private int[] valueMap = {  45, -11,   4,  -1,  -1,   4, -11,  45, 
								11, -16,  -1,  -3,  -3,  -1, -16, -11,
								 4,  -1,   2,  -1,  -1,   2,  -1,   4,
		                        -1,  -3,  -1,   0,   0,  -1,  -3,  -1,
		                        -1,  -3,  -1,   0,   0,  -1,  -3,  -1,
		                         4,  -1,   2,  -1,  -1,   2,  -1,   4,
		                       -11, -16,  -1,  -3,  -3,  -1, -16, -11,
		                        45, -11,   4,  -1,  -1,   4, -11,  45	}; 
	
	public hyoukaCPU(int c){
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
					
					//[x,y,0]��3�̗v�f�����z��Ƃ��ċL������
					//3�ڂ�0�̂Ƃ���ɂ͂��Ƃœ_����Ԃ�
					int pos[] = {x, y, 0};
					
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
		
		int select = 0;
		//�T���̕K�v������ꍇ�͕]�����s���ē_��������
		for(int i = 0;i<array.size();i++){
			int[] pos = array.get(i);
			hyoukaPoint(pos, state);
			//�ő�̒l������n�_��select�ɕۑ�����
			//�V�������߂��l������܂ł̒l���傫���ꍇ��select��ύX����
			int[] serch_pos = array.get(select);
			if(serch_pos[2] < pos[2]){
				select = i;
			}
		}
		
		//System.out.println("select = "+ select );
		
		
		//�����őI�΂ꂽ�u����ꏊ��Ԃ�
		return array.get(select);
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
		int fs = fixStone(s);
		int cn = canNumber(s);
		pos[2] = bp * 2 + fs * 5 + cn * 1;
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
		
		//���̊m��΂�Ԃ����\�b�hfsHolLine�Əc�̊m��΂�Ԃ����\�b�hfsVerLine�𗘗p���ĕӂ̊m��΂����߂�
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
			�ǂ����̒[�����邩�S�����܂��ĂȂ��܂��́C�ゾ������ꍇ
			�ォ�珇�ԂɒT��
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
			�ǂ����̒[�����邩�S�����܂��ĂȂ��܂��́C����������ꍇ
			�����珇�ԂɒT��
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
	
	//�����т̕ӂ̊m��΂����߂郁�\�b�h
	public int canNumber(GameState state){
		int cn = 0;
		//�Ֆʂ̋�}�X��u���邩�`�F�b�N
		for(int y=1; y<=8; y++){
			for(int x=1; x<=8; x++){
				
				//���łɋ����Ƃ��̓p�X
				if(state.data[x + y * 10] != 0)
					continue;
				
				//�u����}�X�̂Ƃ��A��␔�����Z
				if(state.canReverse(x, y) == true){
					cn++;
				}
			}
		}
		return -(cn + (int) Math.floor(Math.random() * 2) * 10);
	}

}
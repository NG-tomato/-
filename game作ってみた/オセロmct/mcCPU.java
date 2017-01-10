import java.util.*;

public class mcCPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;
	
	
	//�v���C�A�E�g��
	int count = 10;
	
	//�Ղ̑傫��(�ǂ̂Ƃ�����܂߂�)
	int size = 10;
	
	//�v���C�������Ƃ��ɂǂ����̐F�̃v���C�������w�肷��
	public mcCPU(int c){
		color = c;
	}
	
	//�΂�łƂ��ɂǂ��ɑł����߂郁�\�b�h
	//���݂̏��(GameState�N���X)�������ɂƂ�
	int[] decide(GameState state){
		
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//�Ֆʂ̋�}�X��u���邩�`�F�b�N
		for(int y=1; y< size-1; y++){
			for(int x=1; x<size-1; x++){
				
				//���łɋ����Ƃ��̓p�X
				if(state.data[x + y * size] != 0)
					continue;
				
				//�u����}�X�̂Ƃ��A���Ƃ��ċL��
				if(state.canReverse(x, y) == true){
					
					//[x,y]��2�̗v�f�����z��Ƃ��ċL������
					int pos[] = {x,y};
					
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
		
		mcMainPanel p = new mcMainPanel(count, state.data , state.turn, state.player);
		
		//p.TextDisplay();
		//System.out.println(Arrays.deepToString(state.data));
		
		//���ꂼ��̎�̓_����ۑ�����z��
		int[] point = new int[array.size()];
		
		for(int i=0; i < array.size(); i++){
			int a[] = array.get(i);
			for(int j = 0; j < count;j++){
				p.mcGame(a);
			}
			point[i] = p.rePoint(state.player);
		}
		
		//�|�C���g���ő�̎�����߂�
		int j = 0;
		for(int i = 1; i < array.size(); i++){
			if(point[i] > point[j]){
				j = i;
			}
		}
		//System.out.println(point[j]);
		return array.get(j);
		
	}
	
}
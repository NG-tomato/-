import java.util.*;

public class mcCPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;	//BLACK or WHITE
	
	
	//�v���C�A�E�g��
	int count = 10;
	int size = 10;
	
	public mcCPU(int c){
		color = c;
	}
	
	int[] decide(GameState state){
		
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//�Ֆʂ̋�}�X��u���邩�`�F�b�N
		for(int y=1; y<size; y++){
			for(int x=1; x<size; x++){
				
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
			p.mcGame(a);
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
import java.util.*;

public class mct_RandomCPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;	//BLACK or WHITE
	int size = 10;
	
	int count = 10;
	
	public mct_RandomCPU(int c){
		color = c;
	}
	
	int[] decide(mctGameState state){
		
		mcMainPanel p = new mcMainPanel(count, state.data , state.turn, state.player);

		
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//�Ֆʂ̋�}�X��u���邩�`�F�b�N
		for(int y=1; y<size + 2; y++){
			for(int x=1; x<size + 2; x++){
				
				//���łɋ����Ƃ��̓p�X
				if(state.data[x + y * 10] != 0)
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
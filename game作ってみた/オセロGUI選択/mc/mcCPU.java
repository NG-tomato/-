//Monte Carlo�@��CPU�̃p�b�P�[�W
package mc.mc;

import java.util.*;

//Monte Carlo�@�̗� �� mc
public class mcCPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;	//BLACK or WHITE
	int size;
	
	public mcCPU(int c,int s){
		color = c;
		size = s;
	}
	
	int[] decide(GameState state){
		
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//�Ֆʂ̋�}�X��u���邩�`�F�b�N
		for(int y=1; y<size + 2; y++){
			for(int x=1; x<size + 2; x++){
				
				//���łɋ����Ƃ��̓p�X
				if(state.data[x][y] != 0)
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
		
		//���ꂼ��̎�̓_����ۑ�����z��
		int[] point = new int[array.size()];
			for(int i=0; i < array.size(); i++){
				MainPanel panel = new MainPanel(state.data, state.turn, state.player, array.get(i));
				point[i] = panel.rePoint();
			}
		int j = 0;
		for(int i = 1; i < array.size(); i++){
			if(point[i] > point[j]){
				j = i;
			}
		}
		//�I�΂ꂽ�u����ꏊ��Ԃ�
		return array.get(j);
	}
	
}
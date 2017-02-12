import java.util.*;

public class mcCPU extends CPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;
	
	
	//�v���C�A�E�g��
	int count = 10;
	
	//�Ղ̑傫��(�ǂ̂Ƃ�����܂߂�)
	int size = 10;
	
	//�v���C�������Ƃ��ɂǂ����̐F�̃v���C�������w�肷��
	public mcCPU(int c){
		super(c);
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
		
		
		//���ꂼ��̎�̓_����ۑ�����z��
		int[] point = new int[array.size()];
		//���ݒT�����Ă����������ϐ�
		int select = 0;
		//������炵�Ȃ���v���C�A�E�g���J��Ԃ�
		for(int i=0; i < count; i++){
			//�T��������肪�z��̑傫���ȏ�̏ꍇ�T��������z���0�Ԗڂ̎�ɂ��ǂ�
			if(select >= array.size()){
				select = 0;
			}
			//�T��������ł������ƃv���C�A�E�g����
			//mcGame�̓v���C�A�E�g���s�������ƌ��̏�Ԃɂ��ǂ��ϐ�
			int a[] = array.get(select);
			p.mcGame(a);
			point[select] = p.rePoint(state.player);
			select++;
		}
		
		//�|�C���g���ő�̎�����߂�
		int j = 0;
		for(int i = 1; i < array.size(); i++){
			if(point[i] > point[j]){
				j = i;
			}
		}
		return array.get(j);
		
	}
	
}
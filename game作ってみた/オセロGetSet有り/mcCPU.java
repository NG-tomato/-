import java.util.*;

public class mcCPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;	//BLACK or WHITE
	int size;
	
	
	//�v���C�A�E�g��
	int count = 10;
	
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
		
		//�����_���I��
		
		//�u����ꏊ���Ȃ��ꍇ�͍��W��{-1,-1}�Ƃ��ĕԂ�
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return pos;
		}
		
		mcMainPanel p = new mcMainPanel(count, Arrays.copyOf(state.data, state.data.length), state.turn, state.player);
		
		//p.TextDisplay();
		//System.out.println(Arrays.deepToString(state.data));
		
		//���ꂼ��̎�̓_����ۑ�����z��
		int[] point = new int[array.size()];
		
		for(int i=0; i < array.size(); i++){
			int a[] = array.get(i);
			point[i] = p.mcGame(a, state.player);
		}
		
		//�|�C���g���ő�̎�����߂�
		int j = 0;
		for(int i = 1; i < array.size(); i++){
			if(point[i] > point[j]){
				j = i;
			}
		}
		System.out.println(point[j]);
		return array.get(j);
		
		/*
		//�����_���N���X�̃C���X�^���X��
		Random rnd = new Random();
		
		/*
		//�����_���N���X����nextInt���\�b�h�𗘗p���������쐬
		nextInt(x);
		0����x�܂ł����������\��������l
		�u����ʒu�̂����ꂩ��I������΂����̂ŁA�u����ꏊ��ۑ��������X�g�̃T�C�Y�����͈̔͂ŗ������쐬���邱�ƂŃ����_���Œu���ꏊ�����߂�悤�ɂ���
		
		int index = rnd.nextInt(array.size());
		
		//�����őI�΂ꂽ�u����ꏊ��Ԃ�
		return array.get(index);
		*/
}
	
}
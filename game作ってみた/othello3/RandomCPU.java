import java.util.*;

public class RandomCPU extends CPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;	//BLACK or WHITE
	int size = 10;
	
	//�����_���N���X�̃C���X�^���X��
	Random rnd = new Random();
	
	public RandomCPU(int c){
		super(c);
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
		
		
		/*
		�����_���N���X����nextInt���\�b�h�𗘗p���������쐬
		nextInt(x);
		0����x�܂ł����������\��������l
		�u����ʒu�̂����ꂩ��I������΂����̂ŁA�u����ꏊ��ۑ��������X�g�̃T�C�Y�����͈̔͂ŗ������쐬���邱�ƂŃ����_���Œu���ꏊ�����߂�悤�ɂ���
		*/
		int index = rnd.nextInt(array.size());
		
		//�����őI�΂ꂽ�u����ꏊ��Ԃ�
		return array.get(index);
	}
	
}
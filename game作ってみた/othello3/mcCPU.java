import java.util.*;

public class mcCPU extends CPU {
	
	//�������u���^�[���𔻕ʂ���֐�
	int color;
	
	
	//�v���C�A�E�g��
	int count = 50;
	
	//�Ղ̑傫��(�ǂ̂Ƃ�����܂߂�)
	int size = 10;
	
	//�v���C�������Ƃ��ɂǂ����̐F�̃v���C�������w�肷��
	public mcCPU(int c){
		super(c);
	}
	
	//�΂�łƂ��ɂǂ��ɑł����߂郁�\�b�h
	//���݂̏��(GameState�N���X)�������ɂƂ�
	@Override
	int[] decide(GameState state){
		
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = putPoint(state);
		
		
		//�u����ꏊ���Ȃ��ꍇ�͍��W��{-1,-1}�Ƃ��ĕԂ�
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return pos;
		}
		
		
		//���ꂼ��̎�̓_����ۑ�����z��
		int[] point = new int[array.size()];
		//���ݒT�����Ă����������ϐ�
		int select = 0;
		//������炵�Ȃ���v���C�A�E�g���J��Ԃ�
		for(int i=0; i < count; i++){
			GameState clone = state.clone();
			//�T��������肪�z��̑傫���ȏ�̏ꍇ�T��������z���0�Ԗڂ̎�ɂ��ǂ�
			if(select >= array.size()){
				select = 0;
			}
			//�T��������ł������ƃv���C�A�E�g����
			//mcGame�̓v���C�A�E�g���s�������ƌ��̏�Ԃɂ��ǂ��ϐ�
			int a[] = array.get(select);
			clone.put(a[0],a[1]);
			playout(clone);
			if(clone.Win() == state.player){
				point[select]++;
			}
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
	
	//�u����ꏊ��ArrayList�ŕԂ��֐�
	public ArrayList<int[]> putPoint(GameState state) {
		//�u����ꏊ���L�����郊�X�g
		ArrayList<int[]> array = new ArrayList<int[]>();
		for(int y=1; y<size-1; y++){
			for(int x=1; x<size-1; x++){
				// �u���邩�ǂ����̔���͐�ɍs��
				if (!state.canReverse(x, y)) continue;

				// state ��clone���Ď��ۂɓ�����
				GameState after = state.clone();
				after.put(x, y);

				// �ǉ�
				int pos[] = {x, y};
				array.add(pos);
			}
		}
		return array;
	}
	
	// ���S�Ƀ����_���v���C
	int playout(GameState state) {
		while (true) {
			ArrayList<int[]> array = putPoint(state);
			if (array.size() == 0) {
				// �u���Ȃ���΃p�X������ (�����Ȃ�)
				if (state.isLastPass) {	
					// ������p�X�����Ă����ꍇ�� �����Ō��ʂ�Ԃ�
					if (state.black > state.white) return 1;
					if (state.white > state.black) return -1;
					return 0;
				}
				// �p�X������1��i�߂��Ƃ���Ńv���C�A�E�g���Ă��炤
				state.pass();
			} else {
				int selected = new Random().nextInt(array.size());
				state.put(array.get(selected)[0], array.get(selected)[1]);
			}
		}
	}


	
}
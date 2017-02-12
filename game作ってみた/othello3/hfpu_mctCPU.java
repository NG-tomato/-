import java.util.*;

public class hfpu_mctCPU extends fpu_mctCPU {
		
	//1��ǂނ��Ƃ̑��v���C�A�E�g��
	int count = 100;
	
	//1��ǂނ��Ƃ̎���(ms�~���b�Ȃ̂ŁC1�b=1000ms)
	long time = 100;
	
	//���Ԃƃv���C�A�E�g���ǂ������g�p���邩(true�̂Ƃ��v���C�A�E�g���Cfalse�̂Ƃ�����)
	boolean switch_threshold = true;
	
	//�T����[������Ƃ��̂������l
	int threshold = 30;
	
	//UCB1�A���S���Y���̒萔C
	double C = 0.18;
	
	//fpu�̒l
	double fpu = 1.69;//100.0;
	
	
	//�v���C�A�E�g���Ɏg�p����]���֐���CPU���w��
	hyoukaCPU CPU = new hyoukaCPU(1);
	
	//�N���X���쐬����ۂɁA�ǂ����̃v���C�����I��
	public hfpu_mctCPU(int c){
		super(c);
		super.count = count;
		super.time = time;
		super.switch_threshold = switch_threshold;
		super.threshold = threshold;
		super.fpu = fpu;
		super.C = C;

	}
		
	//�]���l��p���ăv���C�A�E�g���s��
	@Override
	int playout(mctGameState state){
		while (true) {
			ArrayList<int[]> array = putPoint(state);
			if (array.size() == 0) {
				// �u���Ȃ���΃p�X������ (�����Ȃ�)
				if (state.isLastPass) {
					//�v���C�A�E�g�񐔂̉��Z
					playoutCount++;
					// ������p�X�����Ă����ꍇ�� �����Ō��ʂ�Ԃ�
					if (state.black > state.white) return 1;
					if (state.white > state.black) return -1;		
					return 0;
				}
				// �p�X������1��i�߂��Ƃ���Ńv���C�A�E�g���Ă��炤
				state.pass();
			} else {
				int[] selected = CPU.playoutDecide(state);
				state.put(selected[0], selected[1]);
			}
		}

	}
	
}

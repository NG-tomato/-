import java.util.*;

public class neural_network{
	/* �L���萔�̒�` */
	int INPUTNO = 2;		// ���͒l�̃Z����
	int HIDDENNO = 2;		// ���ԑw�̃Z������
	int ALPHA = 20;			// �w�K�W��
	int MAXINPUTNO = 100;	// �w�K�f�[�^�̍ō���
	int BIGNUM = 100;		// �덷�̏����l
	double LIMIT = 0.001;		// �덷�̏���l
	
	public static void main(String[] args){
		double wh[HIDDENNO][INPUTNO + 1];	// ���ԑw�̏d��
		double wo[HIDDENNO + 1];			// �o�͑w�̏d��
		double e[MAXINPUTNO][INPUTNO + 1];	// �w�K�f�[�^�Z�b�g
		double hi[HIDDENNO + 1];			// ���ԑw�̏o��
		double o;							// �o��
		double err = BIGNUM;
		
	}
}
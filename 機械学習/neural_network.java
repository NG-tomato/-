import java.util.*;

public class neural_network{
	
	public static void main(String[] args){
			/* �L���萔�̒�` */
		int INPUTNO = 2;		// ���͒l�̃Z����
		int HIDDENNO = 2;		// ���ԑw�̃Z������
		int ALPHA = 20;			// �w�K�W��
		int MAXINPUTNO = 100;	// �w�K�f�[�^�̍ō���
		int BIGNUM = 100;		// �덷�̏����l
		double LIMIT = 0.001;		// �덷�̏���l
		
		
		double[][] wh = new double[HIDDENNO][INPUTNO + 1];	// ���ԑw�̏d��
		double[] wo = new double[HIDDENNO + 1];				// �o�͑w�̏d��
		double[][] e = new double[MAXINPUTNO][INPUTNO + 1];	// �w�K�f�[�^�Z�b�g
		double[] hi = new double[HIDDENNO + 1];				// ���ԑw�̏o��
		double o;											// �o��
		double err = BIGNUM;								// �덷�̕]��
		int i,j;											// �J��Ԃ��̐���
		int n_of_e;											// �w�K�f�[�^�̌�
		
		//�d�݂̏�����
		initwh(wh);
		initwo(wo);
		print(wh,wo);
		
		// �w�K�f�[�^�̓ǂݍ���
		n_of_e =getdata(e);
		System.out.println("�w�K�f�[�^�̌� : "+ n_of_e);
		
		
		// �w�K
		while (err > LIMIT){
			err = 0.0;
			for(j = 0;j<n_of_e;++j){
				// �t�����̌v�Z
				o=forward(wh, wo, hi, e[j]);
				// �o�͑w�̏d�݂̒���
				olearn(wo, hi, e[j], o);
				// �덷�̐ώZ
				err += (o-e[j][INPUTNO])*(o-e[j][INPUTNO]);
			}
			//�덷�̏o��
			System.out.println(err);
		}//�w�K�I��
		
		// �����׏d�̏o��
		print(wh, wo);
		
		// �w�K�f�[�^�ɑ΂���o��
		for(i=0;i < n_of_e;++i){
			System.out.print(i+" ");
			for(j=0;j<INPUTNO+1;++j)
				System.out.print(e[i][j] + " ");
			o = forward(wh ,wo ,hi ,e[i]);
			System.out.println(o);
		}
		return;
	}
	
	
	public static int getdata(double e[][]){
	}
	public static void olearn(double wo[] ,double hi[] ,double e[] ,double o){
	}
	public static double forward(double wh[][] ,double wo[], double hi[], double e[]){
	}
	public static void print(double wh[][], double wo[]){}
	public static void initwh(double wh[][]){}
	public static void initwo(double wo[]){}
	public static double drnd(){}
	public static double s(double u){}
	
}
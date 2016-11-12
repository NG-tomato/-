import java.util.*;

public class neural_network{
	// �L���萔�̒�`
	static int INPUTNO = 2;		// ���͒l�̃Z����
	static int HIDDENNO = 2;		// ���ԑw�̃Z������
	static int ALPHA = 20;			// �w�K�W��
	static int MAXINPUTNO = 100;	// �w�K�f�[�^�̍ō���
	static int BIGNUM = 100;		// �덷�̏����l
	static double LIMIT = 0.001;		// �덷�̏���l

	public static void main(String[] args){
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
	
	
	/*
	�w�K�f�[�^�̓ǂݍ��݂����郁�\�b�h
	getdata(�w�K�f�[�^�̔z��)
	*/
	public static int getdata(double e[][]){
		int n_of_e =0;	// �f�[�^�Z�b�g�̌�
		int j = 0;		// �J��Ԃ��̐���
		Scanner sc = new Scanner(System.in);
		// �f�[�^�̓���
		while(sc.hasNextInt()){
			e[n_of_e][j] = sc.nextInt();
			++j;
			if(j > INPUTNO){
				j = 0;
				++n_of_e;
			}
		}
		return n_of_e;
	}
	
	
	/*
	�o�͑w�̏d�݂��w�K���郁�\�b�h
	olearn(�o�͑w�̏d�݂̔z��, ���ԑw�̏o�͂̔z��, �w�K�f�[�^�̔z��, �o��)
	*/
	public static void olearn(double wo[] ,double hi[] ,double e[] ,double o){
		int i;		// �J��Ԃ��̐���
		double d;	// �d�݌v�Z�ɗ��p
		
		d = (e [INPUTNO] - o) * o * (1 - o);	// �덷�̌v�Z
		for(i=0; i < HIDDENNO; ++i){
			wo[i] += ALPHA*hi[i] * d;			// �����׏d�̊w�K
		}
		wo[i] += ALPHA * (-1.0) * d;			// �������l�̊w�K
	}
	
	
	/*
	�������̌v�Z�����郁�\�b�h
	forward(���ԑw�̏d�݂̔z��, �o�͑w�̏d�݂̔z��, ���ԑw�̏o�͂̔z��, �w�K�f�[�^�̔z��)
	*/
	public static double forward(double wh[][] ,double wo[], double hi[], double e[]){
		int j,i;		// �J��Ԃ��̐���
		double u;		// �d�ݕt���a�̍��v
		double o;		// �o�͂̌v�Z
		
		// hi�̌v�Z
		for(i = 0; i < HIDDENNO; i++){
			u=0;						// �d�ݕt���a�����߂�
			for(j = 0; j < INPUTNO; ++j){
				u += e[j] * wh[i][j];
			}
			u -= wh [i][j];		// �������l�̏���
			hi[i] = s(u);
		}
		
		//�o��o�̌v�Z
		o = 0;
		for(i = 0;i < HIDDENNO; i++){
			o += hi[i] * wo[i];
		}
		o -= wo[i];		// �������l�̏���
		
		return s(o);
	}
	
	
	/*
	���ʂ̏o��
	print(���ԑw�̏d�݂̔z��, �o�͑w�̏d�݂̔z��)
	*/
	public static void print(double wh[][], double wo[]){
		int i,j;
		
		for(i = 0; i < HIDDENNO; ++i){
			for(j = 0; j < INPUTNO + 1; ++j){
				System.out.print(wh[i][j] + " ");
			}
		}
		System.out.println();
		for(i = 0; i < HIDDENNO + 1; ++i){
			System.out.print(wo[i] + " ");
		}
		System.out.println();
	}
	
	
	/*
	���ԑw�̏d�݂̏�����
	intwh(���ԑw�̏d�݂̔z��)
	*/
	public static void initwh(double wh[][]){
		int i,j;
		//�����ɂ�錋���׏d�̌���
		for(i = 0; i < HIDDENNO; ++i){
			for(j = 0; j < INPUTNO + 1; ++j){
				wh[i][j] = Math.random() * 2 - 1;
			}
		}
		// �萔���׏d�Ƃ��ė^����ꍇ
		/*
		wh[0][0] = -2;
		wh[0][1] = 3;
		wh[0][2] = -1;
		wh[1][0] = -2;
		wh[1][1] = 1;
		wh[1][2] = 1;
		*/
		
	}
	
	
	/*
	�o�͑w�̏d�݂̏�����
	intwh(�o�͑w�̏d�݂̔z��)
	*/
	public static void initwo(double wo[]){
		int i;
		for(i = 0; i < HIDDENNO + 1; ++i){
			wo[i] = Math.random() * 2 - 1;
			
		}
	}
	
	/*
	�����𐶐�����N���X
	java�ł�Math.random()�Ő����ł��邽�ߖ���
	*/
	//public static double drnd(){}
	
	
	/*
	�V�O���C�h�֐�
	s(�d�ݕt���a)
	*/
	public static double s(double u){
		return 1.0 / (1.0 + Math.exp(-u));
	}
	
}
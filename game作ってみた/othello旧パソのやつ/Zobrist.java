import java.util.*;

public class Zobrist {
	private static Zobrist instance = new Zobrist();

	// user �����̊֐��Q
	public static int makeZob(int[] state, int player) {
		return instance.makeZobCore(state, player);
	}

	//���u��������s������Ɏ菇�̏������郁�\�b�h
	public static int color(int zobrist) {
		return zobrist ^ 0x1;
	}

	// 0 ==> c �ւƕω�������ꍇ
	public static int put(int zobrist, int pos, int c) {
		if (c == mctGameState.BLACK) {
			return instance.putBlack(zobrist, pos);
		} else if (c == mctGameState.WHITE) {
			return instance.putWhite(zobrist, pos);
		} else {
			throw new RuntimeException("error color c");
		}
	}

	// -c ==> c �ւƕω�������ꍇ
	public static int reverse(int zobrist, int pos, int c) {
		if (c == mctGameState.BLACK) {
			zobrist = instance.unputWhite(zobrist, pos);
			zobrist = instance.putBlack(zobrist, pos);
			return zobrist;
		} else if (c == mctGameState.WHITE) {
			zobrist = instance.unputBlack(zobrist, pos);
			zobrist = instance.putWhite(zobrist, pos);
			return zobrist;
		} else {
			throw new RuntimeException("error color c");
		}
	}
	
	////////////////////////////////////////////////////////////////////////
	// ����
	private int makeZobCore(int[] state, int player) {
		int zobrist = 0;
		for (int i = 0; i < state.length; i++) {
			switch (state[i]) {
			case mctGameState.BLACK:
				zobrist = putBlack(zobrist, i);
				break;
			case mctGameState.WHITE:
				zobrist = putWhite(zobrist, i);
				break;
			}
		}
		if (player == mctGameState.WHITE) {
			zobrist = zobrist ^ 0x1;
		}
		return zobrist;
	}

	private int unputBlack(int zobrist, int pos) {
		return zobrist ^ black[pos];
	}
	private int putBlack(int zobrist, int pos) {
		return zobrist ^ black[pos];
	}
	private int unputWhite(int zobrist, int pos) {
		return zobrist ^ white[pos];
	}
	private int putWhite(int zobrist, int pos) {
		return zobrist ^ white[pos];
	}
	
	//����ꏊ�ɋ�u���ꂽ�󋵂��Ƃ̒萔������z��
	int[] black = new int[10 * 10];
	int[] white = new int[10 * 10];

	//����ꏊ�ɐ΂��u���ꂽ�󋵂��Ƃ̕ϐ����쐬
	private Zobrist(){
		Random rnd = new Random();
		for (int y = 1; y <= 8; y++) {
			for (int x = 1; x <= 8; x++) {
				black[x + y * 10] = rnd.nextInt() & (~0x1);
				white[x + y * 10] = rnd.nextInt() & (~0x1);
			}
		}
	}
}

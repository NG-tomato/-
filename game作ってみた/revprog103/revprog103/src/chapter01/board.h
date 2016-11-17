/*
�w�b�_�t�@�C��(.h �ŋL�q)
�T�u���[�`����ϐ��₻�̑��̎��ʎq�̑O���錾���܂܂�Ă��邱�Ƃ������B
�����̃\�[�X�t�@�C���Ő錾����K�v�̂��鎯�ʎq��1�̃w�b�_�t�@�C���ɒu���A�K�v�ɉ����ČX�̃\�[�X�t�@�C�������̃w�b�_�t�@�C�����C���N���[�h����
*/
#ifndef BOARD_H
#define BOARD_H

/* �Ֆʂ̑傫�� */
#define BOARD_SIZE	8

/* �}�X�̏�� */
#define WALL  -1
#define EMPTY 0
#define BLACK 1
#define WHITE 2

/* �}�X�̈ʒu�܂��͎�̎�ނ������ϐ����` */
#define PASS -1
#define NOMOVE -2

#define A1 10
#define B1 11
#define C1 12
#define D1 13
#define E1 14
#define F1 15
#define G1 16
#define H1 17

#define A2 19
#define B2 20
#define C2 21
#define D2 22
#define E2 23
#define F2 24
#define G2 25
#define H2 26

#define A3 28
#define B3 29
#define C3 30
#define D3 31
#define E3 32
#define F3 33
#define G3 34
#define H3 35

#define A4 37
#define B4 38
#define C4 39
#define D4 40
#define E4 41
#define F4 42
#define G4 43
#define H4 44

#define A5 46
#define B5 47
#define C5 48
#define D5 49
#define E5 50
#define F5 51
#define G5 52
#define H5 53

#define A6 55
#define B6 56
#define C6 57
#define D6 58
#define E6 59
#define F6 60
#define G6 61
#define H6 62

#define A7 64
#define B7 65
#define C7 66
#define D7 67
#define E7 68
#define F7 69
#define G7 70
#define H7 71

#define A8 73
#define B8 74
#define C8 75
#define D8 76
#define E8 77
#define F8 78
#define G8 79
#define H8 80

/* 	struct �\���̃^�O�� �\���̕ϐ���;
	�\���̂̐錾
	�� �\����:���낢��Ȏ�ނ݂̌��Ɋ֘A����f�[�^���܂Ƃ߂āA �P�̂����܂�ɂ�������
	
	typedef ��`����Ă���^ ��`����V�����^��;
	���ɒ�`����Ă���^�ɕʂ̐V�������O��t���Ē�`���邱�Ƃ��ł���
	
	�\���̂�錾����ƁA�ʏ�́ustruct �\���̃^�O���v�ƌ����f�[�^�^�Œ�`����邽�߁A����𗘗p���ĕϐ����`����ۂ́ustruct �\���̃^�O�� �ϐ����v�ƋL�q����B
	typedef�𗘗p����ƁA�ustruct �\���̃^�O���v=�u��`����V�����^���v�Ƃ��邱�Ƃ��ł��邽�߁A�Ȍ㗘�p����ۂ�struct���ȗ����ċL�q�\�ƂȂ�B
*/
typedef struct _Board Board;

/*	#ifdef �}�N����
	�E�E�E
	#endif
	
	�}�N�����̂��̂��u#define �}�N�����v�Œ�`����Ă���ΊԂ̍s���L���ɁA��`����Ă��Ȃ���Ζ����ɂȂ�
*/
#ifdef __cplusplus
// 	��������������c����Ƃ��ď�������悤�Ɏw��
extern "C" {
#endif

/*	
	�ՖʃN���X�̐������s��
	���� �Ȃ�
	�߂�l ���������ՖʃN���X�B�����Ɏ��s������NULL��Ԃ��B
	*/
Board	*Board_New(void);
	
/*	
	�ՖʃN���X�̔j�����s���܂�
	����
	 self : Board�N���X�ւ̃|�C���^
	�߂�l �Ȃ�
	*/
void	Board_Delete(Board *self);

/*	
	�Ֆʂ̏��������s���܂�
	����
	self : Board�N���X�ւ̃|�C���^
	�߂�l �Ȃ�
*/
void	Board_Clear(Board *self);


/*	
	�w�肳�ꂽ�}�X�̏�Ԃ�Ԃ��܂�
	����
	 self : Board�N���X�ւ̃|�C���^
	 in_pos : ��Ԃ��擾����}�X�̈ʒu
	�߂�l �w�肳�ꂽ�}�X�̏��
*/
int		Board_Disk(const Board *self, int in_pos);

/*
	�w�肳�ꂽ��Ԃ̃}�X�̐���Ԃ��܂�
	����
	 self : Board�N���X�ւ̃|�C���^
	 in_color : �����擾����}�X�̏��
	�߂�l �w�肳�ꂽ��Ԃ̃}�X�̐�
*/
int		Board_CountDisks(const Board *self, int in_color);


/*
	�w�肳�ꂽ�F�A�ʒu�Œ�����s���܂�
	����
	 self : Board�N���X�ւ̃|�C���^
	 in_color : ���肷��΂̐F
	 in_pos : ���肷��}�X�̈ʒu
	�߂�l ����ł����ꍇ�ɂ͕Ԃ����΂̐��i���肵���}�X�̐΂͐��Ɋ܂܂Ȃ��j�B����ł��Ȃ��ꍇ�ɂ�0
*/
int		Board_Flip(Board *self, int in_color, int in_pos);

/*
	�P��߂��܂�
	����
	 self : Board�N���X�ւ̃|�C���^
	�߂�l ���O�̎�ɂ���ĕԂ����΂̐��i���肵���}�X�̐΂͐��Ɋ܂܂Ȃ��j�B�Ֆʂ�������Ԃ̏ꍇ�ɂ�0��Ԃ�
*/
int		Board_Unflip(Board *self);

/*
	�w�肵���F�A�}�X�ɒ��肵���ꍇ�ɉ��ΕԂ����𒲂ׂ܂�
	����
	 self : Board�N���X�ւ̃|�C���^
	 in_color : ���肷��΂̐F
	 in_pos : ���肷��}�X�̈ʒu
	�߂�l ����ł���ꍇ�ɂ͕Ԃ��΂̐��i���肷��}�X�̐΂͐��Ɋ܂܂Ȃ��j�B����ł��Ȃ��ꍇ�ɂ�0
*/
int		Board_CanFlip(const Board *self, int in_color, int in_pos);

/*
	�w�肵���F�A�}�X�ɒ���ł��邩�ǂ����𒲂ׂ܂�
	����
	 self : Board�N���X�ւ̃|�C���^
	 in_color : ���肷��΂̐F
	 in_pos : ���肷��}�X�̈ʒu
	�߂�l ����ł���Ȃ�1�A�ł��Ȃ��Ȃ�0
*/
int		Board_CountFlips(const Board *self, int in_color, int in_pos);


/*
	�Ֆʂ̃R�s�[���s���܂�
	����
	 self : Board�N���X�ւ̃|�C���^
	 out_board : �R�s�[���Board�N���X�ւ̃|�C���^
	�߂�l �Ȃ�
*/
void	Board_Copy(const Board *self, Board *out_board);

/*
	�Ֆʂ̔��]���s���܂��B
	�e�}�X�̍��΂𔒐΂ɁA���΂����΂ɕύX���܂�
	����
	 self : Board�N���X�ւ̃|�C���^
	�߂�l �Ȃ�
*/
void	Board_Reverse(Board *self);


/*
	�w�肵���F�Œ���\���ǂ����𒲂ׂ܂�
	����
	 self : Board�N���X�ւ̃|�C���^
	 in_color : ������s���F
	�߂�l �w�肳�ꂽ�F�łǂ����̃}�X�ɒ���\�Ȃ�1�A�\�łȂ����0
*/
int		Board_CanPlay(const Board *self, int in_color);


/*
	�}�X�̈ʒu��Ԃ�
	����
	 in_x : �}�X��X���W
	 in_y : �}�X��Y���W
	�߂�l �w�肳�ꂽ�}�X�̈ʒu
*/
int		Board_Pos(int in_x, int in_y);

/*
	�}�X��X���W��Ԃ�
	����
	 in_pos : �}�X�̈ʒu
	�߂�l �}�X��X���W
*/
int		Board_X(int in_pos);

/*
	�}�X��Y���W��Ԃ�
	����
	 in_pos : �}�X�̈ʒu
	�߂�l �}�X��Y���W
*/
int		Board_Y(int in_pos);

/*
	�w�肳�ꂽ�F�̋t�̐F��Ԃ�
	����
	 in_color : BLACK�܂���WHITE
	�߂�l in_color��BLACK�Ȃ�WHITE�AWHITE�Ȃ�BLACK
*/
int		Board_OpponentColor(int in_color);

// __cplusplus�Ƃ����}�N������`����Ă����}��\������
#ifdef __cplusplus
}
#endif

#endif /* BOARD_H */

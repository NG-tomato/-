#ifndef BOARD_H
#define BOARD_H

/* �Ֆʂ̑傫�� */
#define BOARD_SIZE	8

/* �}�X�̏�� */
#define WALL  -1
#define EMPTY 0
#define BLACK 1
#define WHITE 2

/* �}�X�̈ʒu�܂��͎�̎�� */
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

typedef struct _Board Board;

#ifdef __cplusplus
extern "C" {
#endif
Board	*Board_New(void);
void	Board_Delete(Board *self);

void	Board_Clear(Board *self);

int		Board_Disk(const Board *self, int in_pos);
int		Board_CountDisks(const Board *self, int in_color);

int		Board_Flip(Board *self, int in_color, int in_pos);
int		Board_Unflip(Board *self);
int		Board_CanFlip(const Board *self, int in_color, int in_pos);
int		Board_CountFlips(const Board *self, int in_color, int in_pos);

void	Board_Copy(const Board *self, Board *out_board);
void	Board_Reverse(Board *self);

int		Board_CanPlay(const Board *self, int in_color);

int		Board_Pos(int in_x, int in_y);
int		Board_X(int in_pos);
int		Board_Y(int in_pos);
int		Board_OpponentColor(int in_color);
#ifdef __cplusplus
}
#endif

#endif /* BOARD_H */

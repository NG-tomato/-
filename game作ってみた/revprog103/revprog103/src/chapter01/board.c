/*
#include   --.h
ヘッダファイル(.h で終わるファイル)読み込み
ヘッダファイルとは変数や関数

*/
//一般ユーティリティ関数とマクロが定義されている標準ライブラリ
#include <stdlib.h>
//
#include "board.h"

/*	#define 文字列 式
	で文字列がコンパイル前がすべて式に置き換わる */
#define NUM_DISK	((BOARD_SIZE+1)*(BOARD_SIZE+2)+1)
#define NUM_STACK	(((BOARD_SIZE-2)*3+3)*BOARD_SIZE*BOARD_SIZE)

#define DIR_UP_LEFT		(-BOARD_SIZE-2)
#define DIR_UP			(-BOARD_SIZE-1)
#define DIR_UP_RIGHT	(-BOARD_SIZE)
#define DIR_LEFT		(-1)
#define DIR_RIGHT		(1)
#define DIR_DOWN_LEFT	(BOARD_SIZE)
#define DIR_DOWN		(BOARD_SIZE+1)
#define DIR_DOWN_RIGHT	(BOARD_SIZE+2)

// 盤面クラスの構造体を定義（詳しいことは1.2参照）
struct _Board
{
	// マスの状態を表す変数
	int Disk[NUM_DISK];
	/*
	返した石を覚えておくための変数
	逆順で読み込むことで手を戻せる
	*/
	int Stack[NUM_STACK];
	// Stackのどの位置にいるかを覚えておくための変数
	int *Sp;
};

#define OPPONENT_COLOR(c)			(BLACK + WHITE - c)
#define BOARD_STACK_POP(self)		(*(--self->Sp))
#define BOARD_STACK_PUSH(self, n)	(*(self->Sp++) = n)

/*
	static 型 値
	その関数が終了しても消滅せず、値を残すことができる。
	定義されたファイル内のみで利用可能
*/
static int Board_Initialize(Board *self);
static int Board_Finalize(Board *self);
static int Board_FlipLine(Board *self, int in_color, int in_pos, int in_dir);
static int Board_CountFlipsLine(const Board *self, int in_color, int in_pos, int in_dir);


/*	
	盤面クラスの生成を行う
	引数 なし
	戻り値 生成した盤面クラス。生成に失敗したらNULLを返す。
	*/
Board *Board_New(void){
	//Boardクラス(自身のクラス)を生成する
	Board *self;
	/*
	mallloc(メモリサイズ);
	指定バイト分、メモリ領域を確保する
	
	sizeof(変数)
	変数を渡した場合，その変数名で確保されているメモリサイズを返す
	*/
	self = malloc(sizeof(Board));
	
	//生成できていれば生成した盤面クラス、失敗していればNULLを返す
	if (self) {
		Board_Clear(self);
	}
	return self;
}


/*	
	盤面クラスの破棄を行います
	引数
	 self : Boardクラスへのポインタ
	戻り値 なし
	*/
void Board_Delete(Board *self)
{
	//メモリ領域を解放する
	free(self);
}


/*	
	盤面の初期化を行います
	引数
	self : Boardクラスへのポインタ
	戻り値 なし
*/
void Board_Clear(Board *self)
{
	int i, j;

	for (i = 0; i < NUM_DISK; i++) {
		self->Disk[i] = WALL;
	}
	for (i = 0; i < BOARD_SIZE; i++) {
		for (j = 0; j < BOARD_SIZE; j++) {
			self->Disk[Board_Pos(i, j)] = EMPTY;
		}
	}
	self->Disk[E4] = BLACK;
	self->Disk[D5] = BLACK;
	self->Disk[D4] = WHITE;
	self->Disk[E5] = WHITE;

	self->Sp = self->Stack;
}

int Board_Disk(const Board *self, int in_pos)
{
	return self->Disk[in_pos];
}

int Board_CountDisks(const Board *self, int in_color)
{
	int result = 0;
	int pos;

	for (pos = 0; pos < NUM_DISK; pos++) {
		if (self->Disk[pos] == in_color) {
			result++;
		}
	}

	return result;
}

static int Board_FlipLine(Board *self, int in_color, int in_pos, int in_dir)
{
	int result = 0;
	int op = OPPONENT_COLOR(in_color);
	int pos;

	for (pos = in_pos + in_dir; self->Disk[pos] == op; pos += in_dir) {}
	if (self->Disk[pos] == in_color) {
		for (pos -= in_dir; self->Disk[pos] == op; pos -= in_dir) {
			result++;
			self->Disk[pos] = in_color;
			BOARD_STACK_PUSH(self, pos);
		}
	}

	return result;
}

int Board_Flip(Board *self, int in_color, int in_pos)
{
	int result = 0;

	if (self->Disk[in_pos] != EMPTY) {
		return 0;
	}
	result += Board_FlipLine(self, in_color, in_pos, DIR_UP_LEFT);
	result += Board_FlipLine(self, in_color, in_pos, DIR_UP);
	result += Board_FlipLine(self, in_color, in_pos, DIR_UP_RIGHT);
	result += Board_FlipLine(self, in_color, in_pos, DIR_LEFT);
	result += Board_FlipLine(self, in_color, in_pos, DIR_RIGHT);
	result += Board_FlipLine(self, in_color, in_pos, DIR_DOWN_LEFT);
	result += Board_FlipLine(self, in_color, in_pos, DIR_DOWN);
	result += Board_FlipLine(self, in_color, in_pos, DIR_DOWN_RIGHT);
	if (result > 0) {
		self->Disk[in_pos] = in_color;
		BOARD_STACK_PUSH(self, in_pos);
		BOARD_STACK_PUSH(self, OPPONENT_COLOR(in_color));
		BOARD_STACK_PUSH(self, result);
	}

	return result;
}

int Board_Unflip(Board *self)
{
	int result;
	int i, color;

	if (self->Sp <= self->Stack) {
		return 0;
	}
	result = BOARD_STACK_POP(self);
	color = BOARD_STACK_POP(self);
	self->Disk[BOARD_STACK_POP(self)] = EMPTY;
	for (i = 0; i < result; i++) {
		self->Disk[BOARD_STACK_POP(self)] = color;
	}

	return result;
}

static int Board_CountFlipsLine(const Board *self, int in_color, int in_pos, int in_dir)
{
	int result = 0;
	int op = OPPONENT_COLOR(in_color);
	int pos;

	for (pos = in_pos + in_dir; self->Disk[pos] == op; pos += in_dir) {
		result++;
	}
	if (self->Disk[pos] != in_color) {
		return 0;
	}

	return result;
}

int Board_CountFlips(const Board *self, int in_color, int in_pos)
{
	int result = 0;

	if (self->Disk[in_pos] != EMPTY) {
		return 0;
	}
	result += Board_CountFlipsLine(self, in_color, in_pos, DIR_UP_LEFT);
	result += Board_CountFlipsLine(self, in_color, in_pos, DIR_UP);
	result += Board_CountFlipsLine(self, in_color, in_pos, DIR_UP_RIGHT);
	result += Board_CountFlipsLine(self, in_color, in_pos, DIR_LEFT);
	result += Board_CountFlipsLine(self, in_color, in_pos, DIR_RIGHT);
	result += Board_CountFlipsLine(self, in_color, in_pos, DIR_DOWN_LEFT);
	result += Board_CountFlipsLine(self, in_color, in_pos, DIR_DOWN);
	result += Board_CountFlipsLine(self, in_color, in_pos, DIR_DOWN_RIGHT);

	return result;
}

int Board_CanFlip(const Board *self, int in_color, int in_pos)
{
	if (self->Disk[in_pos] != EMPTY) {
		return 0;
	}
	if (Board_CountFlipsLine(self, in_color, in_pos, DIR_UP_LEFT)) {
		return 1;
	}
	if (Board_CountFlipsLine(self, in_color, in_pos, DIR_UP)) {
		return 1;
	}
	if (Board_CountFlipsLine(self, in_color, in_pos, DIR_UP_RIGHT)) {
		return 1;
	}
	if (Board_CountFlipsLine(self, in_color, in_pos, DIR_LEFT)) {
		return 1;
	}
	if (Board_CountFlipsLine(self, in_color, in_pos, DIR_RIGHT)) {
		return 1;
	}
	if (Board_CountFlipsLine(self, in_color, in_pos, DIR_DOWN_LEFT)) {
		return 1;
	}
	if (Board_CountFlipsLine(self, in_color, in_pos, DIR_DOWN)) {
		return 1;
	}
	if (Board_CountFlipsLine(self, in_color, in_pos, DIR_DOWN_RIGHT)) {
		return 1;
	}

	return 0;
}

void Board_Copy(const Board *self, Board *out_board)
{
	*out_board = *self;
	out_board->Sp = self->Sp - self->Stack + out_board->Stack;
}

void Board_Reverse(Board *self)
{
	int pos;
	int *p;
	int n;

	for (pos = 0; pos < NUM_DISK; pos++) {
		if (self->Disk[pos] == BLACK) {
			self->Disk[pos] = WHITE;
		} else if (self->Disk[pos] == WHITE) {
			self->Disk[pos] = BLACK;
		}
	}
	for (p = self->Sp; p > self->Stack;) {
		p--;
		n = *p;
		p--;
		*p = OPPONENT_COLOR(*p);
		p -= n + 1;
	}
}

int Board_CanPlay(const Board *self, int in_color)
{
	int x, y;

	for (x = 0; x < BOARD_SIZE; x++) {
		for (y = 0; y < BOARD_SIZE; y++) {
			if (Board_CanFlip(self, in_color, Board_Pos(x, y))) {
				return 1;
			}
		}
	}
	return 0;
}

int Board_Pos(int in_x, int in_y)
{
	return (in_y + 1) * (BOARD_SIZE + 1) + in_x + 1;
}

int Board_X(int in_pos)
{
	return in_pos % (BOARD_SIZE + 1) - 1;
}

int Board_Y(int in_pos)
{
	return in_pos / (BOARD_SIZE + 1) - 1;
}

int Board_OpponentColor(int in_color)
{
	return OPPONENT_COLOR(in_color);
}

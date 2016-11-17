#include "com.h"
#include "board.h"
#include <string.h>
#include <stdlib.h>

struct _Com
{
	Board *Board;
	int MidDepth;
	int WLDDepth;
	int ExactDepth;
	int Node;
};

static int Com_Initialize(Com *self);
static int Com_MidSearch(Com *self, int in_depth, int in_alpha, int in_beta, int in_color, int in_opponent, int in_pass, int *out_move);
static int Com_EndSearch(Com *self, int in_depth, int in_alpha, int in_beta, int in_color, int in_opponent, int in_pass, int *out_move);

static int Com_Initialize(Com *self)
{
	memset(self, 0, sizeof(Com));
	self->Board = Board_New();
	if (!self->Board) {
		return 0;
	}
	self->MidDepth = 1;
	self->WLDDepth = 1;
	self->ExactDepth = 1;
	self->Node = 0;
	return 1;
}

Com *Com_New(void)
{
	Com *self;
	self = malloc(sizeof(Com));
	if (self) {
		if (!Com_Initialize(self)) {
			Com_Delete(self);
			self = NULL;
		}
	}
	return self;
}

void Com_Delete(Com *self)
{
	if (self->Board) {
		Board_Delete(self->Board);
	}
	free(self);
}

void Com_SetLevel(Com *self, int in_mid, int in_exact, int in_wld)
{
	self->MidDepth = in_mid;
	self->WLDDepth = in_wld;
	self->ExactDepth = in_exact;
}

int Com_NextMove(Com *self, const Board *in_board, int in_color, int *out_value)
{
	int result;
	int left;
	int value;
	int color;

	Board_Copy(in_board, self->Board);
	self->Node = 0;
	left = Board_CountDisks(self->Board, EMPTY);
	if (left <= self->ExactDepth) {
		value = Com_EndSearch(self, left, -BOARD_SIZE * BOARD_SIZE, BOARD_SIZE * BOARD_SIZE, in_color, Board_OpponentColor(in_color), 0, &result);
	} else if (left <= self->WLDDepth) {
		value = Com_EndSearch(self, left, -BOARD_SIZE * BOARD_SIZE, 1, in_color, Board_OpponentColor(in_color), 0, &result);
	} else {
		if ((in_color == WHITE && self->MidDepth % 2 == 0) ||
			(in_color == BLACK && self->MidDepth % 2 == 1)) {
			Board_Reverse(self->Board);
			color = Board_OpponentColor(in_color);
		} else {
			color = in_color;
		}
		value = Com_MidSearch(self, self->MidDepth, -BOARD_SIZE * BOARD_SIZE, BOARD_SIZE * BOARD_SIZE, color, Board_OpponentColor(color), 0, &result);
	}
	if (out_value) {
		*out_value = value;
	}

	return result;
}

static int Com_MidSearch(Com *self, int in_depth, int in_alpha, int in_beta, int in_color, int in_opponent, int in_pass, int *out_move)
{
	int x, y;
	int value, max = in_alpha;
	int can_move = 0;
	int move;

	if (in_depth == 0) {
		self->Node++;
		return Board_CountDisks(self->Board, in_color) - Board_CountDisks(self->Board, in_opponent);
	}
	*out_move = NOMOVE;
	for (x = 0; x < BOARD_SIZE; x++) {
		for (y = 0; y < BOARD_SIZE; y++) {
			if (Board_Flip(self->Board, in_color, Board_Pos(x, y))) {
				if (!can_move) {
					*out_move = Board_Pos(x, y);
					can_move = 1;
				}
				value = -Com_MidSearch(self, in_depth - 1, -in_beta, -max, in_opponent, in_color, 0, &move);
				Board_Unflip(self->Board);
				if (value > max) {
					max = value;
					*out_move = Board_Pos(x, y);
					if (max >= in_beta) {
						return in_beta;
					}
				}
			}
		}
	}
	if (!can_move) {
		if (in_pass) {
			*out_move = NOMOVE;
			self->Node++;
			max = Board_CountDisks(self->Board, in_color) - Board_CountDisks(self->Board, in_opponent);
		} else {
			*out_move = PASS;
			max = -Com_MidSearch(self, in_depth - 1, -in_beta, -max, in_opponent, in_color, 1, &move);
		}
	}
	return max;
}

static int Com_EndSearch(Com *self, int in_depth, int in_alpha, int in_beta, int in_color, int in_opponent, int in_pass, int *out_move)
{
	int x, y;
	int value, max = in_alpha;
	int can_move = 0;
	int move;

	if (in_depth == 0) {
		self->Node++;
		return Board_CountDisks(self->Board, in_color) - Board_CountDisks(self->Board, in_opponent);
	}
	*out_move = NOMOVE;
	for (x = 0; x < BOARD_SIZE; x++) {
		for (y = 0; y < BOARD_SIZE; y++) {
			if (Board_Flip(self->Board, in_color, Board_Pos(x, y))) {
				if (!can_move) {
					*out_move = Board_Pos(x, y);
					can_move = 1;
				}
				value = -Com_EndSearch(self, in_depth-1, -in_beta, -max, in_opponent, in_color, 0, &move);
				Board_Unflip(self->Board);
				if (value > max) {
					max = value;
					*out_move = Board_Pos(x, y);
					if (max >= in_beta) {
						return in_beta;
					}
				}
			}
		}
	}
	if (!can_move) {
		if (in_pass) {
			*out_move = NOMOVE;
			self->Node++;
			max = Board_CountDisks(self->Board, in_color) - Board_CountDisks(self->Board, in_opponent);
		} else {
			*out_move = PASS;
			max = -Com_EndSearch(self, in_depth, -in_beta, -max, in_opponent, in_color, 1, &move);
		}
	}
	return max;
}

int Com_CountNodes(const Com *self)
{
	return self->Node;
}

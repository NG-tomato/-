#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "board.h"

#define BUFFER_SIZE 64
#define FORMAT_SIZE 16

struct _MainParam
{
	Board *Board;
};
typedef struct _MainParam MainParam;

static char * get_stream(char *out_buffer, int in_size, FILE *stream);
static void print_board(const Board *in_board);
static void play(Board *board);
static int main_param_initialize_each(MainParam *self);
static int main_param_initialize(MainParam *self);
static void main_param_finalize(MainParam *self);

static char * get_stream(char *out_buffer, int in_size, FILE *stream)
{
	char *result;
	int i;

	result = fgets(out_buffer, in_size, stream);
	if (result != NULL) {
		for (i = 0; i < in_size; i++) {
			if (out_buffer[i] == '\r' || out_buffer[i] == '\n') {
				out_buffer[i] = '\0';
			}
		}
	}
	return result;
}

static void print_board(const Board *in_board)
{
	int x, y;
	printf("  A B C D E F G H\n");
	for (y = 0; y < BOARD_SIZE; y++) {
		printf("%d ", y+1);
		for (x = 0; x < BOARD_SIZE; x++) {
			switch (Board_Disk(in_board, Board_Pos(x, y))) {
			case BLACK:
				printf("O ");
				break;
			case WHITE:
				printf("X ");
				break;
			case EMPTY:
				printf(". ");
				break;
			default:
				printf("# ");
				break;
			}
		}
		printf("\n");
	}
	printf("O %2d - X %2d\n", Board_CountDisks(in_board, BLACK), Board_CountDisks(in_board, WHITE));
	printf("\n");
}

static void play(Board *board)
{
	char buffer[BUFFER_SIZE];
	int history[BOARD_SIZE * BOARD_SIZE * 2];
	int color = BLACK;
	int n, x, y;

	for (n = 0; n < BOARD_SIZE * BOARD_SIZE * 2; n++) {
		history[n] = NOMOVE;
	}
	Board_Clear(board);

	n = 0;
	while (1) {
		print_board(board);
		if (color == BLACK) {
			printf("黒番です、次の手を入力してください\n");
		} else {
			printf("白番です、次の手を入力してください\n");
		}
		get_stream(buffer, BUFFER_SIZE, stdin);
		if (!strcmp(buffer, "q") || !strcmp(buffer, "quit")) {
			printf("ゲームを中断します\n");
			break;
		} else if (!strcmp(buffer, "u") || !strcmp(buffer, "undo")) {
			if (n > 0) {
				n--;
				if (history[n] != PASS) {
					Board_Unflip(board);
				}
				color = Board_OpponentColor(color);
			}
		} else if (!strcmp(buffer, "p") || !strcmp(buffer, "pass")) {
			if (!Board_CanPlay(board, color) && Board_CanPlay(board, Board_OpponentColor(color))) {
				color = Board_OpponentColor(color);
				history[n] = PASS;
				n++;
			} else {
				printf("パスはできません\n");
			}
		} else if (strlen(buffer) != 2) {
			printf("無効なコマンドです\n");
		} else {
			x = tolower(buffer[0]) - 'a';
			y = buffer[1] - '1';
			if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
				printf("無効なコマンドです\n");
			} else if (Board_Flip(board, color, Board_Pos(x, y))) {
				color = Board_OpponentColor(color);
				history[n] = Board_Pos(x, y);
				n++;
			} else {
				printf("そこには置けません\n");
			}
		}
		printf("\n");
	}
}

static int main_param_initialize_each(MainParam *self)
{
	self->Board = Board_New();
	if (!self->Board) {
		return 0;
	}

	return 1;
}

static int main_param_initialize(MainParam *self)
{
	memset(self, 0, sizeof(MainParam));
	if (!main_param_initialize_each(self)) {
		main_param_finalize(self);
		return 0;
	}

	return 1;
}

static void main_param_finalize(MainParam *self)
{
	if (self->Board) {
		Board_Delete(self->Board);
	}
}

int main(int argc, char **argv)
{
	MainParam param;

	if (!main_param_initialize(&param)) {
		printf("初期化に失敗しました\n");
		return 0;
	}

	play(param.Board);

	main_param_finalize(&param);

	return 0;
}

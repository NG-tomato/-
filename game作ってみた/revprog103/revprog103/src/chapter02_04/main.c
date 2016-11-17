#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "board.h"
#include "com.h"

#define BUFFER_SIZE 64
#define FORMAT_SIZE 16

struct _MainParam
{
	Board *Board;
	Com *Com;
};
typedef struct _MainParam MainParam;

static char * get_stream(char *out_buffer, int in_size, FILE *stream);
static void print_board(const Board *in_board);
static void play(Board *board, Com *com);
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

static void play(Board *board, Com *com)
{
	char buffer[BUFFER_SIZE];
	int history[BOARD_SIZE * BOARD_SIZE * 2];
	int color = BLACK;
	int player_color;
	int move;
	int score;
	clock_t clock_start, clock_end;
	int n, x, y;

	for (n = 0; n < BOARD_SIZE * BOARD_SIZE * 2; n++) {
		history[n] = NOMOVE;
	}
	Board_Clear(board);

	n = 0;
	while (1) {
		printf("���Ȃ��̐F��I�����Ă������� (1:�� 2:��)\n");
		get_stream(buffer, BUFFER_SIZE, stdin);
		if (!strcmp(buffer, "1")) {
			player_color = BLACK;
			break;
		} else if (!strcmp(buffer, "2")) {
			player_color = WHITE;
			break;
		}
	}
	Com_SetLevel(com, 12, 12, 12);
	while (1) {
		print_board(board);
		if (!Board_CanPlay(board, BLACK) && !Board_CanPlay(board, WHITE)) {
			if (Board_CountDisks(board, player_color) == 0) {
				score = -BOARD_SIZE * BOARD_SIZE;
			} else if (Board_CountDisks(board, Board_OpponentColor(player_color)) == 0) {
				score = BOARD_SIZE * BOARD_SIZE;
			} else {
				score = Board_CountDisks(board, player_color) - Board_CountDisks(board, Board_OpponentColor(player_color));
			}
			if (score > 0) {
				printf("���Ȃ���%d�Ώ����ł�\n", score);
			} else if (score < 0) {
				printf("�R���s���[�^��%d�Ώ����ł�\n", -score);
			} else {
				printf("���������ł�\n");
			}
			break;
		}
		if (color == player_color) {
			printf("���Ȃ��̔Ԃł��A���̎����͂��Ă�������\n");
			get_stream(buffer, BUFFER_SIZE, stdin);
			if (!strcmp(buffer, "q") || !strcmp(buffer, "quit")) {
				printf("�Q�[���𒆒f���܂�\n");
				break;
			} else if (!strcmp(buffer, "u") || !strcmp(buffer, "undo")) {
				if (n > 1) {
					n--;
					if (history[n] != PASS) {
						Board_Unflip(board);
					}
					n--;
					if (history[n] != PASS) {
						Board_Unflip(board);
					}
				}
			} else if (!strcmp(buffer, "p") || !strcmp(buffer, "pass")) {
				if (!Board_CanPlay(board, color) && Board_CanPlay(board, Board_OpponentColor(color))) {
					color = Board_OpponentColor(color);
					history[n] = PASS;
					n++;
				} else {
					printf("�p�X�͂ł��܂���\n");
				}
			} else if (strlen(buffer) != 2) {
				printf("�����ȃR�}���h�ł�\n");
			} else {
				x = tolower(buffer[0]) - 'a';
				y = buffer[1] - '1';
				if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
					printf("�����ȃR�}���h�ł�\n");
				} else if (Board_Flip(board, color, Board_Pos(x, y))) {
					color = Board_OpponentColor(color);
					history[n] = Board_Pos(x, y);
					n++;
				} else {
					printf("�����ɂ͒u���܂���\n");
				}
			}
		} else {
			printf("�R���s���[�^�v�l��...\n");
			if (Board_CanPlay(board, color)) {
				clock_start = clock();
				move = Com_NextMove(com, board, color, &score);
				clock_end = clock();
				printf("%c%c�ɒu���܂�\n", "ABCDEFGH"[Board_X(move)], "12345678"[Board_Y(move)]);
				printf("�]���l: %d\n", score);
				printf("�v�l����: %.2f �b �m�[�h��: %d NPS: %.2f knps\n", (double)(clock_end - clock_start) / CLOCKS_PER_SEC, 
					Com_CountNodes(com), (double)Com_CountNodes(com) / (clock_end - clock_start + 1) * CLOCKS_PER_SEC / 1000);
				Board_Flip(board, color, move);
			} else {
				printf("�p�X���܂�\n");
				move = PASS;
			}
			color = Board_OpponentColor(color);
			history[n] = move;
			n++;
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
	self->Com = Com_New();
	if (!self->Com) {
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
	if (self->Com) {
		Com_Delete(self->Com);
	}
	if (self->Board) {
		Board_Delete(self->Board);
	}
}

int main(int argc, char **argv)
{
	MainParam param;

	if (!main_param_initialize(&param)) {
		printf("�������Ɏ��s���܂���\n");
		return 0;
	}

	play(param.Board, param.Com);

	main_param_finalize(&param);

	return 0;
}

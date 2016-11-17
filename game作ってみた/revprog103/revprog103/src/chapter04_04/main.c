#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "board.h"
#include "com.h"
#include "evaluator.h"

#define BUFFER_SIZE 64
#define EVALUATOR_FILE "eval.dat"

struct _MainParam
{
	Board *Board;
	Evaluator *Evaluator;
	Com *Com;
};
typedef struct _MainParam MainParam;

static int get_rand(int in_max);
static void move_random(Board *board, int in_color);
static char * get_stream(char *out_buffer, int in_size, FILE *stream);
static void print_board(const Board *in_board);
static void play(Board *board, Com *com);
static void learn(Board *board, Evaluator *evaluator, Com *com);
static int main_param_initialize_each(MainParam *self);
static int main_param_initialize(MainParam *self);
static void main_param_finalize(MainParam *self);

static int get_rand(int in_max)
{
	return (int)((double)in_max * rand() / (RAND_MAX + 1.0));
}

static void move_random(Board *board, int in_color)
{
	while (!Board_Flip(board, in_color, Board_Pos(get_rand(BOARD_SIZE), get_rand(BOARD_SIZE)))) {}
}

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
		printf("あなたの色を選択してください (1:黒 2:白)\n");
		get_stream(buffer, BUFFER_SIZE, stdin);
		if (!strcmp(buffer, "1")) {
			player_color = BLACK;
			break;
		} else if (!strcmp(buffer, "2")) {
			player_color = WHITE;
			break;
		}
	}
	while (1) {
		printf("コンピュータのレベルを選択してください (1-4)\n");
		get_stream(buffer, BUFFER_SIZE, stdin);
		if (!strcmp(buffer, "1")) {
			Com_SetLevel(com, 2, 8, 10);
			break;
		} else if (!strcmp(buffer, "2")) {
			Com_SetLevel(com, 4, 10, 12);
			break;
		} else if (!strcmp(buffer, "3")) {
			Com_SetLevel(com, 6, 12, 14);
			break;
		} else if (!strcmp(buffer, "4")) {
			Com_SetLevel(com, 8, 14, 16);
			break;
		}
	}
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
				printf("あなたの%d石勝ちです\n", score);
			} else if (score < 0) {
				printf("コンピュータの%d石勝ちです\n", -score);
			} else {
				printf("引き分けです\n");
			}
			break;
		}
		if (color == player_color) {
			printf("あなたの番です、次の手を入力してください\n");
			get_stream(buffer, BUFFER_SIZE, stdin);
			if (!strcmp(buffer, "q") || !strcmp(buffer, "quit")) {
				printf("ゲームを中断します\n");
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
		} else {
			printf("コンピュータ思考中...\n");
			if (Board_CanPlay(board, color)) {
				clock_start = clock();
				move = Com_NextMove(com, board, color, &score);
				clock_end = clock();
				printf("%c%cに置きます\n", "ABCDEFGH"[Board_X(move)], "12345678"[Board_Y(move)]);
				printf("評価値: %.2f\n", (double)score / DISK_VALUE);
				printf("思考時間: %.2f 秒 ノード数: %d NPS: %.2f knps\n", (double)(clock_end - clock_start) / CLOCKS_PER_SEC, 
					Com_CountNodes(com), (double)Com_CountNodes(com) / (clock_end - clock_start + 1) * CLOCKS_PER_SEC / 1000);
				Board_Flip(board, color, move);
			} else {
				printf("パスします\n");
				move = PASS;
			}
			color = Board_OpponentColor(color);
			history[n] = move;
			n++;
		}
		printf("\n");
	}
}

static void learn(Board *board, Evaluator *evaluator, Com *com)
{
	char buffer[BUFFER_SIZE];
	int history_color[BOARD_SIZE * BOARD_SIZE];
	int i, j, move, num, turn, value;
	int color;
	int result;

	printf("対戦回数を入力してください\n");
	get_stream(buffer, BUFFER_SIZE, stdin);
	num = atoi(buffer);

	Com_SetLevel(com, 4, 12, 12);
	for (i = 0; i < num; i++) {
		Board_Clear(board);
		color = BLACK;
		turn = 0;
		for (j = 0; j < 8; j++) {
			if (Board_CanPlay(board, color)) {
				move_random(board, color);
				history_color[turn] = color;
				turn++;
			}
			color = Board_OpponentColor(color);
		}
		while (1) {
			if (Board_CanPlay(board, color)) {
				if (Board_CountDisks(board, EMPTY) > 12 && get_rand(100) < 1) {
					move_random(board, color);
				} else {
					move = Com_NextMove(com, board, color, &value);
					Board_Flip(board, color, move);
				}
				history_color[turn] = color;
				turn++;
			} else if (!Board_CanPlay(board, Board_OpponentColor(color))) {
				break;
			}
			color = Board_OpponentColor(color);
		}
		result = (Board_CountDisks(board, BLACK) - Board_CountDisks(board, WHITE)) * DISK_VALUE;
		for (j = Board_CountDisks(board, EMPTY); j < 8; j++) {
			turn--;
			Board_Unflip(board);
		}
		for (j = Board_CountDisks(board, EMPTY); j < BOARD_SIZE * BOARD_SIZE - 12; j++) {
			turn--;
			Board_Unflip(board);
			if (history_color[turn] == BLACK) {
				Evaluator_Update(evaluator, board, result);
			} else {
				Board_Reverse(board);
				Evaluator_Update(evaluator, board, -result);
				Board_Reverse(board);
			}
		}
		if ((i + 1) % 100 == 0) {
			printf("学習中... %d / %d\r", i + 1 , num );
			Evaluator_Save(evaluator, EVALUATOR_FILE);
		}
	}
	Evaluator_Save(evaluator, EVALUATOR_FILE);
	printf("終了しました                    \n");
}

static int main_param_initialize_each(MainParam *self)
{
	self->Board = Board_New();
	if (!self->Board) {
		return 0;
	}
	self->Evaluator = Evaluator_New();
	if (!self->Evaluator) {
		return 0;
	}
	Evaluator_Load(self->Evaluator, EVALUATOR_FILE);
	self->Com = Com_New(self->Evaluator);
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
	if (self->Evaluator) {
		Evaluator_Delete(self->Evaluator);
	}
	if (self->Board) {
		Board_Delete(self->Board);
	}
}

int main(int argc, char **argv)
{
	MainParam param;
	char buffer[BUFFER_SIZE];

	srand((unsigned)time(NULL));
	if (!main_param_initialize(&param)) {
		printf("初期化に失敗しました\n");
		return 0;
	}

	while (1) {
		printf("モードを選択してください (1:対戦 2:学習 q:終了)\n");
		get_stream(buffer, BUFFER_SIZE, stdin);
		if (!strcmp(buffer, "1")) {
			play(param.Board, param.Com);
		} else if (!strcmp(buffer, "2")) {
			learn(param.Board, param.Evaluator, param.Com);
		} else if (!strcmp(buffer, "q")) {
			break;
		}
	}

	main_param_finalize(&param);

	return 0;
}

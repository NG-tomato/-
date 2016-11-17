#include "evaluator.h"
#include "board.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* 評価パラメータ更新の度合い */
#define UPDATE_RATIO 0.003

/* パターンの最大評価値 */
#define MAX_PATTERN_VALUE (DISK_VALUE * 20)

/* 3の冪を表現する定数 */
#define POW_3_0		1
#define POW_3_1		3
#define POW_3_2		9
#define POW_3_3		27
#define POW_3_4		81
#define POW_3_5		243
#define POW_3_6		729
#define POW_3_7		2187
#define POW_3_8		6561
#define POW_3_9		19683
#define POW_3_10	59049

/* パターンID */
enum {
	PATTERN_ID_LINE4,
	PATTERN_ID_LINE3,
	PATTERN_ID_LINE2,
	PATTERN_ID_DIAG8,
	PATTERN_ID_DIAG7,
	PATTERN_ID_DIAG6,
	PATTERN_ID_DIAG5,
	PATTERN_ID_DIAG4,
	PATTERN_ID_EDGE8,
	PATTERN_ID_CORNER8,
	PATTERN_ID_PARITY,
	PATTERN_ID_NUM
};

/* 各パターンの状態数 */
static const int PatternSize[] =
{
	POW_3_8,		/* A4-H4 */
	POW_3_8,		/* A3-H3 */
	POW_3_8,		/* A2-H2 */
	POW_3_8,		/* A1-H8 */
	POW_3_7,		/* A2-G8 */
	POW_3_6,		/* A3-F8 */
	POW_3_5,		/* A4-E8 */
	POW_3_4,		/* A5-D8 */
	POW_3_8,		/* A1-G1 + B2 */
	POW_3_8,		/* A1-C1 + A2-C2 + A3-B3 */
	2,			/* Parity */
	0			/* dummy */
};

struct _Evaluator
{
	int *Value[PATTERN_ID_NUM];
	int MirrorLine[POW_3_8];
	int MirrorCorner[POW_3_8];
};

static int Evaluator_Initialize(Evaluator *self);
static void Evaluator_Finalize(Evaluator *self);
static void Evaluator_UpdatePattern(Evaluator *self, int in_pattern, int in_id, int in_mirror, int in_diff);

static int Evaluator_Initialize(Evaluator *self)
{
	int i, j;
	int mirror_in, mirror_out, coeff;
	int mirror_corner_coeff[] = { POW_3_2, POW_3_5, POW_3_0, POW_3_3, POW_3_6, POW_3_1, POW_3_4, POW_3_7 };

	memset(self, 0, sizeof(Evaluator));
	for (i = 0; i < PATTERN_ID_NUM; i++) {
		self->Value[i] = calloc(PatternSize[i], sizeof(int));
		if (!self->Value[i]) {
			return 0;
		}
	}
	for (i = 0; i < POW_3_8; i++) {
		mirror_in = i;
		mirror_out = 0;
		coeff = POW_3_7;
		for (j = 0; j < 8; j++) {
			mirror_out += mirror_in % 3 * coeff;
			mirror_in /= 3;
			coeff /= 3;
		}
		if (mirror_out < i) {
			self->MirrorLine[i] = mirror_out;
		} else {
			self->MirrorLine[i] = i;
		}
	}
	for (i = 0; i < POW_3_8; i++) {
		mirror_in = i;
		mirror_out = 0;
		for (j = 0; j < 8; j++) {
			mirror_out += mirror_in % 3 * mirror_corner_coeff[j];
			mirror_in /= 3;
		}
		if (mirror_out < i) {
			self->MirrorCorner[i] = mirror_out;
		} else {
			self->MirrorCorner[i] = i;
		}
	}

	return 1;
}

static void Evaluator_Finalize(Evaluator *self)
{
	int i;
	for (i = 0; i < PATTERN_ID_NUM; i++) {
		if (self->Value[i]) {
			free(self->Value[i]);
		}
	}
}

Evaluator *Evaluator_New(void)
{
	Evaluator *self;

	self = malloc(sizeof(Evaluator));
	if (self) {
		if (!Evaluator_Initialize(self)) {
			Evaluator_Delete(self);
			self = NULL;
		}
	}
	return self;
}

void Evaluator_Delete(Evaluator *self)
{
	Evaluator_Finalize(self);
	free(self);
}

int Evaluator_Load(Evaluator *self, const char *in_file_name)
{
	FILE *fp;
	int i;

	fp = fopen(in_file_name, "rb");
	if (!fp) {
		return 0;
	}
	for (i = 0; i < PATTERN_ID_NUM; i++) {
		if (fread(self->Value[i], sizeof(int), PatternSize[i], fp) < (size_t)PatternSize[i]) {
			fclose(fp);
			return 0;
		}
	}
	fclose(fp);
	return 1;
}

int Evaluator_Save(const Evaluator *self, const char *in_file_name)
{
	FILE *fp;
	int i;

	fp = fopen(in_file_name, "wb");
	if (!fp) {
		return 0;
	}
	for (i = 0; i < PATTERN_ID_NUM; i++) {
		if (fwrite(self->Value[i], sizeof(int), PatternSize[i], fp) < (size_t)PatternSize[i]) {
			fclose(fp);
			return 0;
		}
	}
	fclose(fp);
	return 1;
}

#define BOARD_INDEX_4(b,s1,s2,s3,s4) \
	(((Board_Disk((b),(s1)) * 3 + Board_Disk((b),(s2))) * 3 + Board_Disk((b),(s3))) * 3 + \
	Board_Disk((b),(s4)))

#define BOARD_INDEX_5(b,s1,s2,s3,s4,s5) \
	((((Board_Disk((b),(s1)) * 3 + Board_Disk((b),(s2))) * 3 + Board_Disk((b),(s3))) * 3 + \
	Board_Disk((b),(s4))) * 3 + Board_Disk((b),(s5)))

#define BOARD_INDEX_6(b,s1,s2,s3,s4,s5,s6) \
	(((((Board_Disk((b),(s1)) * 3 + Board_Disk((b),(s2))) * 3 + Board_Disk((b),(s3))) * 3 + \
	Board_Disk((b),(s4))) * 3 + Board_Disk((b),(s5))) * 3 + Board_Disk((b),(s6)))

#define BOARD_INDEX_7(b,s1,s2,s3,s4,s5,s6,s7) \
	((((((Board_Disk((b),(s1)) * 3 + Board_Disk((b),(s2))) * 3 + Board_Disk((b),(s3))) * 3 + \
	Board_Disk((b),(s4))) * 3 + Board_Disk((b),(s5))) * 3 + Board_Disk((b),(s6))) * 3 + \
	Board_Disk((b),(s7)))

#define BOARD_INDEX_8(b,s1,s2,s3,s4,s5,s6,s7,s8) \
	(((((((Board_Disk((b),(s1)) * 3 + Board_Disk((b),(s2))) * 3 + Board_Disk((b),(s3))) * 3 + \
	Board_Disk((b),(s4))) * 3 + Board_Disk((b),(s5))) * 3 + Board_Disk((b),(s6))) * 3 + \
	Board_Disk((b),(s7))) * 3 + Board_Disk((b),(s8)))

#define BOARD_INDEX_9(b,s1,s2,s3,s4,s5,s6,s7,s8,s9) \
	((((((((Board_Disk((b),(s1)) * 3 + Board_Disk((b),(s2))) * 3 + Board_Disk((b),(s3))) * 3 + \
	Board_Disk((b),(s4))) * 3 + Board_Disk((b),(s5))) * 3 + Board_Disk((b),(s6))) * 3 + \
	Board_Disk((b),(s7))) * 3 + Board_Disk((b),(s8))) * 3 + Board_Disk((b),(s9)))

#define BOARD_INDEX_10(b,s1,s2,s3,s4,s5,s6,s7,s8,s10) \
	((((((((Board_Disk((b),(s1)) * 3 + Board_Disk((b),(s2))) * 3 + Board_Disk((b),(s3))) * 3 + \
	Board_Disk((b),(s4))) * 3 + Board_Disk((b),(s5))) * 3 + Board_Disk((b),(s6))) * 3 + \
	Board_Disk((b),(s7))) * 3 + Board_Disk((b),(s8))) * 3 + Board_Disk((b),(s9))) * 3 + \
	Board_Disk((b),(s10)))

int Evaluator_Value(const Evaluator *self, const Board *in_board)
{
	int result = 0;

	/* A4-H4 */
	result += self->Value[PATTERN_ID_LINE4][BOARD_INDEX_8(in_board, A4, B4, C4, D4, E4, F4, G4, H4)];
	/* A5-H5 */
	result += self->Value[PATTERN_ID_LINE4][BOARD_INDEX_8(in_board, A5, B5, C5, D5, E5, F5, G5, H5)];
	/* D1-D8 */
	result += self->Value[PATTERN_ID_LINE4][BOARD_INDEX_8(in_board, D1, D2, D3, D4, D5, D6, D7, D8)];
	/* E1-E8 */
	result += self->Value[PATTERN_ID_LINE4][BOARD_INDEX_8(in_board, E1, E2, E3, E4, E5, E6, E7, E8)];

	/* A3-H3 */
	result += self->Value[PATTERN_ID_LINE3][BOARD_INDEX_8(in_board, A3, B3, C3, D3, E3, F3, G3, H3)];
	/* A6-H6 */
	result += self->Value[PATTERN_ID_LINE3][BOARD_INDEX_8(in_board, A6, B6, C6, D6, E6, F6, G6, H6)];
	/* C1-C8 */
	result += self->Value[PATTERN_ID_LINE3][BOARD_INDEX_8(in_board, C1, C2, C3, C4, C5, C6, C7, C8)];
	/* F1-F8 */
	result += self->Value[PATTERN_ID_LINE3][BOARD_INDEX_8(in_board, F1, F2, F3, F4, F5, F6, F7, F8)];

	/* A2-H2 */
	result += self->Value[PATTERN_ID_LINE2][BOARD_INDEX_8(in_board, A2, B2, C2, D2, E2, F2, G2, H2)];
	/* A7-H7 */
	result += self->Value[PATTERN_ID_LINE2][BOARD_INDEX_8(in_board, A7, B7, C7, D7, E7, F7, G7, H7)];
	/* B1-B8 */
	result += self->Value[PATTERN_ID_LINE2][BOARD_INDEX_8(in_board, B1, B2, B3, B4, B5, B6, B7, B8)];
	/* G1-G8 */
	result += self->Value[PATTERN_ID_LINE2][BOARD_INDEX_8(in_board, G1, G2, G3, G4, G5, G6, G7, G8)];

	/* A1-H8 */
	result += self->Value[PATTERN_ID_DIAG8][BOARD_INDEX_8(in_board, A1, B2, C3, D4, E5, F6, G7, H8)];
	/* A8-H1 */
	result += self->Value[PATTERN_ID_DIAG8][BOARD_INDEX_8(in_board, A8, B7, C6, D5, E4, F3, G2, H1)];

	/* A2-G8*/
	result += self->Value[PATTERN_ID_DIAG7][BOARD_INDEX_7(in_board, A2, B3, C4, D5, E6, F7, G8)];
	/* B1-H7 */
	result += self->Value[PATTERN_ID_DIAG7][BOARD_INDEX_7(in_board, B1, C2, D3, E4, F5, G6, H7)];
	/* A7-G1 */
	result += self->Value[PATTERN_ID_DIAG7][BOARD_INDEX_7(in_board, A7, B6, C5, D4, E3, F2, G1)];
	/* B8-H2 */
	result += self->Value[PATTERN_ID_DIAG7][BOARD_INDEX_7(in_board, B8, C7, D6, E5, F4, G3, H2)];

	/* A3-F8*/
	result += self->Value[PATTERN_ID_DIAG6][BOARD_INDEX_6(in_board, A3, B4, C5, D6, E7, F8)];
	/* C1-H6 */
	result += self->Value[PATTERN_ID_DIAG6][BOARD_INDEX_6(in_board, C1, D2, E3, F4, G5, H6)];
	/* A6-F1 */
	result += self->Value[PATTERN_ID_DIAG6][BOARD_INDEX_6(in_board, A6, B5, C4, D3, E2, F1)];
	/* C8-H3 */
	result += self->Value[PATTERN_ID_DIAG6][BOARD_INDEX_6(in_board, C8, D7, E6, F5, G4, H3)];

	/* A4-E8*/
	result += self->Value[PATTERN_ID_DIAG5][BOARD_INDEX_5(in_board, A4, B5, C6, D7, E8)];
	/* D1-H5 */
	result += self->Value[PATTERN_ID_DIAG5][BOARD_INDEX_5(in_board, D1, E2, F3, G4, H5)];
	/* A5-E1 */
	result += self->Value[PATTERN_ID_DIAG5][BOARD_INDEX_5(in_board, A5, B4, C3, D2, E1)];
	/* D8-H4 */
	result += self->Value[PATTERN_ID_DIAG5][BOARD_INDEX_5(in_board, D8, E7, F6, G5, H4)];

	/* A5-D8*/
	result += self->Value[PATTERN_ID_DIAG4][BOARD_INDEX_4(in_board, A5, B6, C7, D8)];
	/* E1-H4 */
	result += self->Value[PATTERN_ID_DIAG4][BOARD_INDEX_4(in_board, E1, F2, G3, H4)];
	/* A4-D1 */
	result += self->Value[PATTERN_ID_DIAG4][BOARD_INDEX_4(in_board, A4, B3, C2, D1)];
	/* E8-H5 */
	result += self->Value[PATTERN_ID_DIAG4][BOARD_INDEX_4(in_board, E8, F7, G6, H5)];

	/* A1-G1 + B2 */
	result += self->Value[PATTERN_ID_EDGE8][BOARD_INDEX_8(in_board, A1, B1, C1, D1, E1, F1, G1, B2)];
	/* H1-B1 + G2 */
	result += self->Value[PATTERN_ID_EDGE8][BOARD_INDEX_8(in_board, H1, G1, F1, E1, D1, C1, B1, G2)];
	/* A8-G8 + B7 */
	result += self->Value[PATTERN_ID_EDGE8][BOARD_INDEX_8(in_board, A8, B8, C8, D8, E8, F8, G8, B7)];
	/* H8-B8 + G7 */
	result += self->Value[PATTERN_ID_EDGE8][BOARD_INDEX_8(in_board, H8, G8, F8, E8, D8, C8, B8, G7)];
	/* A1-A7 + B2 */
	result += self->Value[PATTERN_ID_EDGE8][BOARD_INDEX_8(in_board, A1, A2, A3, A4, A5, A6, A7, B2)];
	/* A8-A2 + B7 */
	result += self->Value[PATTERN_ID_EDGE8][BOARD_INDEX_8(in_board, A8, A7, A6, A5, A4, A3, A2, B7)];
	/* H1-H7 + G2 */
	result += self->Value[PATTERN_ID_EDGE8][BOARD_INDEX_8(in_board, H1, H2, H3, H4, H5, H6, H7, G2)];
	/* H8-H2 + G7 */
	result += self->Value[PATTERN_ID_EDGE8][BOARD_INDEX_8(in_board, H8, H7, H6, H5, H4, H3, H2, G7)];

	/* A1-C1 + A2-C2 + A3-B3 */
	result += self->Value[PATTERN_ID_CORNER8][BOARD_INDEX_8(in_board, A1, B1, C1, A2, B2, C2, A3, B3)];
	/* H1-F1 + H2-F2 + H3-G3 */
	result += self->Value[PATTERN_ID_CORNER8][BOARD_INDEX_8(in_board, H1, G1, F1, H2, G2, F2, H3, G3)];
	/* A8-C8 + A7-C7 + A6-B6 */
	result += self->Value[PATTERN_ID_CORNER8][BOARD_INDEX_8(in_board, A8, B8, C8, A7, B7, C7, A6, B6)];
	/* H8-F8 + H7-F7 + H6-G6 */
	result += self->Value[PATTERN_ID_CORNER8][BOARD_INDEX_8(in_board, H8, G8, F8, H7, G7, F7, H6, G6)];

	/* parity */
	result += self->Value[PATTERN_ID_PARITY][Board_CountDisks(in_board, EMPTY) & 1];

	return result;
}

static void Evaluator_UpdatePattern(Evaluator *self, int in_pattern, int in_id, int in_mirror, int in_diff)
{
	if (MAX_PATTERN_VALUE - in_diff < self->Value[in_pattern][in_id]) {
		self->Value[in_pattern][in_id] = MAX_PATTERN_VALUE;
	} else if (-MAX_PATTERN_VALUE - in_diff > self->Value[in_pattern][in_id]) {
		self->Value[in_pattern][in_id] = -MAX_PATTERN_VALUE;
	} else {
		self->Value[in_pattern][in_id] += in_diff;
	}
	if (in_mirror >= 0) {
		self->Value[in_pattern][in_mirror] = self->Value[in_pattern][in_id];
	}
}

void Evaluator_Update(Evaluator *self, const Board *in_board, int in_value)
{
	int index, diff;

	diff = (int)((in_value - Evaluator_Value(self, in_board)) * UPDATE_RATIO);
	index = BOARD_INDEX_8(in_board, A4, B4, C4, D4, E4, F4, G4, H4);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE4, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, A5, B5, C5, D5, E5, F5, G5, H5);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE4, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, D1, D2, D3, D4, D5, D6, D7, D8);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE4, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, E1, E2, E3, E4, E5, E6, E7, E8);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE4, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, A3, B3, C3, D3, E3, F3, G3, H3);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE3, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, A6, B6, C6, D6, E6, F6, G6, H6);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE3, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, C1, C2, C3, C4, C5, C6, C7, C8);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE3, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, F1, F2, F3, F4, F5, F6, F7, F8);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE3, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, A2, B2, C2, D2, E2, F2, G2, H2);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE2, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, A7, B7, C7, D7, E7, F7, G7, H7);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE2, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, B1, B2, B3, B4, B5, B6, B7, B8);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE2, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, G1, G2, G3, G4, G5, G6, G7, G8);
	Evaluator_UpdatePattern(self, PATTERN_ID_LINE2, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, A1, B2, C3, D4, E5, F6, G7, H8);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG8, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_8(in_board, A8, B7, C6, D5, E4, F3, G2, H1);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG8, self->MirrorLine[index], index, diff);
	index = BOARD_INDEX_7(in_board, A2, B3, C4, D5, E6, F7, G8);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG7, self->MirrorLine[index * POW_3_1], index, diff);
	index = BOARD_INDEX_7(in_board, B1, C2, D3, E4, F5, G6, H7);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG7, self->MirrorLine[index * POW_3_1], index, diff);
	index = BOARD_INDEX_7(in_board, A7, B6, C5, D4, E3, F2, G1);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG7, self->MirrorLine[index * POW_3_1], index, diff);
	index = BOARD_INDEX_7(in_board, B8, C7, D6, E5, F4, G3, H2);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG7, self->MirrorLine[index * POW_3_1], index, diff);
	index = BOARD_INDEX_6(in_board, A3, B4, C5, D6, E7, F8);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG6, self->MirrorLine[index * POW_3_2], index, diff);
	index = BOARD_INDEX_6(in_board, C1, D2, E3, F4, G5, H6);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG6, self->MirrorLine[index * POW_3_2], index, diff);
	index = BOARD_INDEX_6(in_board, A6, B5, C4, D3, E2, F1);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG6, self->MirrorLine[index * POW_3_2], index, diff);
	index = BOARD_INDEX_6(in_board, C8, D7, E6, F5, G4, H3);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG6, self->MirrorLine[index * POW_3_2], index, diff);
	index = BOARD_INDEX_5(in_board, A4, B5, C6, D7, E8);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG5, self->MirrorLine[index * POW_3_3], index, diff);
	index = BOARD_INDEX_5(in_board, D1, E2, F3, G4, H5);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG5, self->MirrorLine[index * POW_3_3], index, diff);
	index = BOARD_INDEX_5(in_board, A5, B4, C3, D2, E1);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG5, self->MirrorLine[index * POW_3_3], index, diff);
	index = BOARD_INDEX_5(in_board, D8, E7, F6, G5, H4);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG5, self->MirrorLine[index * POW_3_3], index, diff);
	index = BOARD_INDEX_4(in_board, A5, B6, C7, D8);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG4, self->MirrorLine[index * POW_3_4], index, diff);
	index = BOARD_INDEX_4(in_board, E1, F2, G3, H4);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG4, self->MirrorLine[index * POW_3_4], index, diff);
	index = BOARD_INDEX_4(in_board, A4, B3, C2, D1);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG4, self->MirrorLine[index * POW_3_4], index, diff);
	index = BOARD_INDEX_4(in_board, E8, F7, G6, H5);
	Evaluator_UpdatePattern(self, PATTERN_ID_DIAG4, self->MirrorLine[index * POW_3_4], index, diff);
	Evaluator_UpdatePattern(self, PATTERN_ID_EDGE8, BOARD_INDEX_8(in_board, A1, B1, C1, D1, E1, F1, G1, B2), -1, diff);
	Evaluator_UpdatePattern(self, PATTERN_ID_EDGE8, BOARD_INDEX_8(in_board, H1, G1, F1, E1, D1, C1, B1, G2), -1, diff);
	Evaluator_UpdatePattern(self, PATTERN_ID_EDGE8, BOARD_INDEX_8(in_board, A8, B8, C8, D8, E8, F8, G8, B7), -1, diff);
	Evaluator_UpdatePattern(self, PATTERN_ID_EDGE8, BOARD_INDEX_8(in_board, H8, G8, F8, E8, D8, C8, B8, G7), -1, diff);
	Evaluator_UpdatePattern(self, PATTERN_ID_EDGE8, BOARD_INDEX_8(in_board, A1, A2, A3, A4, A5, A6, A7, B2), -1, diff);
	Evaluator_UpdatePattern(self, PATTERN_ID_EDGE8, BOARD_INDEX_8(in_board, A8, A7, A6, A5, A4, A3, A2, B7), -1, diff);
	Evaluator_UpdatePattern(self, PATTERN_ID_EDGE8, BOARD_INDEX_8(in_board, H1, H2, H3, H4, H5, H6, H7, G2), -1, diff);
	Evaluator_UpdatePattern(self, PATTERN_ID_EDGE8, BOARD_INDEX_8(in_board, H8, H7, H6, H5, H4, H3, H2, G7), -1, diff);
	index = BOARD_INDEX_8(in_board, A1, B1, C1, A2, B2, C2, A3, B3);
	Evaluator_UpdatePattern(self, PATTERN_ID_CORNER8, self->MirrorCorner[index], index, diff);
	index = BOARD_INDEX_8(in_board, H1, G1, F1, H2, G2, F2, H3, G3);
	Evaluator_UpdatePattern(self, PATTERN_ID_CORNER8, self->MirrorCorner[index], index, diff);
	index = BOARD_INDEX_8(in_board, A8, B8, C8, A7, B7, C7, A6, B6);
	Evaluator_UpdatePattern(self, PATTERN_ID_CORNER8, self->MirrorCorner[index], index, diff);
	index = BOARD_INDEX_8(in_board, H8, G8, F8, H7, G7, F7, H6, G6);
	Evaluator_UpdatePattern(self, PATTERN_ID_CORNER8, self->MirrorCorner[index], index, diff);
	Evaluator_UpdatePattern(self, PATTERN_ID_PARITY, Board_CountDisks(in_board, EMPTY) & 1, -1, diff);
}

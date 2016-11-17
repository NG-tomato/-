/*
ヘッダファイル(.h で記述)
サブルーチンや変数やその他の識別子の前方宣言が含まれていることが多い。
複数のソースファイルで宣言する必要のある識別子を1つのヘッダファイルに置き、必要に応じて個々のソースファイルがそのヘッダファイルをインクルードする
*/
#ifndef BOARD_H
#define BOARD_H

/* 盤面の大きさ */
#define BOARD_SIZE	8

/* マスの状態 */
#define WALL  -1
#define EMPTY 0
#define BLACK 1
#define WHITE 2

/* マスの位置または手の種類を示す変数を定義 */
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

/* 	struct 構造体タグ名 構造体変数名;
	構造体の宣言
	※ 構造体:いろいろな種類の互いに関連するデータをまとめて、 １つのかたまりにしたもの
	
	typedef 定義されている型 定義する新しい型名;
	既に定義されている型に別の新しい名前を付けて定義することができる
	
	構造体を宣言すると、通常は「struct 構造体タグ名」と言うデータ型で定義されるため、これを利用して変数を定義する際は「struct 構造体タグ名 変数名」と記述する。
	typedefを利用すると、「struct 構造体タグ名」=「定義する新しい型名」とすることができるため、以後利用する際にstructを省略して記述可能となる。
*/
typedef struct _Board Board;

/*	#ifdef マクロ名
	・・・
	#endif
	
	マクロ名のものが「#define マクロ名」で定義されていれば間の行が有効に、定義されていなければ無効になる
*/
#ifdef __cplusplus
// 	くくった部分をc言語として処理するように指示
extern "C" {
#endif

/*	
	盤面クラスの生成を行う
	引数 なし
	戻り値 生成した盤面クラス。生成に失敗したらNULLを返す。
	*/
Board	*Board_New(void);
	
/*	
	盤面クラスの破棄を行います
	引数
	 self : Boardクラスへのポインタ
	戻り値 なし
	*/
void	Board_Delete(Board *self);

/*	
	盤面の初期化を行います
	引数
	self : Boardクラスへのポインタ
	戻り値 なし
*/
void	Board_Clear(Board *self);


/*	
	指定されたマスの状態を返します
	引数
	 self : Boardクラスへのポインタ
	 in_pos : 状態を取得するマスの位置
	戻り値 指定されたマスの状態
*/
int		Board_Disk(const Board *self, int in_pos);

/*
	指定された状態のマスの数を返します
	引数
	 self : Boardクラスへのポインタ
	 in_color : 数を取得するマスの状態
	戻り値 指定された状態のマスの数
*/
int		Board_CountDisks(const Board *self, int in_color);


/*
	指定された色、位置で着手を行います
	引数
	 self : Boardクラスへのポインタ
	 in_color : 着手する石の色
	 in_pos : 着手するマスの位置
	戻り値 着手できた場合には返した石の数（着手したマスの石は数に含まない）。着手できない場合には0
*/
int		Board_Flip(Board *self, int in_color, int in_pos);

/*
	１手戻します
	引数
	 self : Boardクラスへのポインタ
	戻り値 直前の手によって返した石の数（着手したマスの石は数に含まない）。盤面が初期状態の場合には0を返す
*/
int		Board_Unflip(Board *self);

/*
	指定した色、マスに着手した場合に何石返すかを調べます
	引数
	 self : Boardクラスへのポインタ
	 in_color : 着手する石の色
	 in_pos : 着手するマスの位置
	戻り値 着手できる場合には返す石の数（着手するマスの石は数に含まない）。着手できない場合には0
*/
int		Board_CanFlip(const Board *self, int in_color, int in_pos);

/*
	指定した色、マスに着手できるかどうかを調べます
	引数
	 self : Boardクラスへのポインタ
	 in_color : 着手する石の色
	 in_pos : 着手するマスの位置
	戻り値 着手できるなら1、できないなら0
*/
int		Board_CountFlips(const Board *self, int in_color, int in_pos);


/*
	盤面のコピーを行います
	引数
	 self : Boardクラスへのポインタ
	 out_board : コピー先のBoardクラスへのポインタ
	戻り値 なし
*/
void	Board_Copy(const Board *self, Board *out_board);

/*
	盤面の反転を行います。
	各マスの黒石を白石に、白石を黒石に変更します
	引数
	 self : Boardクラスへのポインタ
	戻り値 なし
*/
void	Board_Reverse(Board *self);


/*
	指定した色で着手可能かどうかを調べます
	引数
	 self : Boardクラスへのポインタ
	 in_color : 着手を行う色
	戻り値 指定された色でどこかのマスに着手可能なら1、可能でなければ0
*/
int		Board_CanPlay(const Board *self, int in_color);


/*
	マスの位置を返す
	引数
	 in_x : マスのX座標
	 in_y : マスのY座標
	戻り値 指定されたマスの位置
*/
int		Board_Pos(int in_x, int in_y);

/*
	マスのX座標を返す
	引数
	 in_pos : マスの位置
	戻り値 マスのX座標
*/
int		Board_X(int in_pos);

/*
	マスのY座標を返す
	引数
	 in_pos : マスの位置
	戻り値 マスのY座標
*/
int		Board_Y(int in_pos);

/*
	指定された色の逆の色を返す
	引数
	 in_color : BLACKまたはWHITE
	戻り値 in_colorがBLACKならWHITE、WHITEならBLACK
*/
int		Board_OpponentColor(int in_color);

// __cplusplusというマクロが定義されていれば}を表示する
#ifdef __cplusplus
}
#endif

#endif /* BOARD_H */

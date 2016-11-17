#ifndef HASH_H
#define HASH_H

/* ハッシュ値 */
struct _HashValue
{
	unsigned long Low;		/* 下32bit */
	unsigned long High;		/* 上32bit */
};
typedef struct _HashValue HashValue;

/* 局面情報 */
struct _HashInfo
{
	int Lower;				/* 評価値の下限 */
	int Upper;				/* 評価値の上限 */
	unsigned char Depth;	/* 探索手数 */
	unsigned char Move;		/* 最善手 */
};
typedef struct _HashInfo HashInfo;

typedef struct _Hash Hash;

#ifdef __cplusplus
extern "C" {
#endif
Hash	*Hash_New(int in_size);
void	Hash_Delete(Hash *self);
void	Hash_Clear(Hash *self);
int		Hash_Set(Hash *self, const HashValue *in_value, const HashInfo *in_info);
int		Hash_Get(Hash *self, const HashValue *in_value, HashInfo *out_info);
void	Hash_ClearInfo(Hash *self);
int		Hash_CountGet(Hash *self);
int		Hash_CountHit(Hash *self);
#ifdef __cplusplus
}
#endif

#endif /* HASH_H */

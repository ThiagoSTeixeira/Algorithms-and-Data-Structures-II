/*
 * MAC0323 Estruturas de Dados e Algoritmo II
 * 
 * Tabela de simbolos implementada atraves de uma BST rubro-negra
 *
 *     https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html
 * 
 * As chaves e valores desta implementaÃ§Ã£o sÃ£o mais ou menos
 * genÃ©ricos
 */

/* interface para o uso da funcao deste mÃ³dulo */
#include "redblackst.h"  
#include <stdlib.h>      /* free() */
#include <string.h>      /* memcpy() */
#include "util.h"        /* emalloc(), ecalloc() */

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debug */
#endif


// CONSTANTES 
 
#define RED   TRUE
#define BLACK FALSE 

/*----------------------------------------------------------*/
/* 
 * Estrutura de um nÃ³ da Ã¡rvore
 *
 */
typedef struct node Node;

struct node {
        void *key;           // key
        void *val;           // associated data
        Node *left, *right;  // links to left and right subtrees
        Bool color;          // color of parent link
        int size;            // subtree count
};

//"construtor"
Node *newNode (void *key, void *val, Bool color, int size) {
    Node *tmp = emalloc (sizeof (Node));
    tmp->key = key;
    tmp->val = val;
    tmp->color = color;
    tmp->size = size;
    return tmp;
}

/*----------------------------------------------------    void *key;
    void *val;
    Node *left, *right;
    bool color;
    int size;------*/
/* 
 * Estrutura BÃ¡sica da Tabela de SÃ­mbolos: 
 * 
 * implementaÃ§Ã£o com Ã¡rvore rubro-negra
 */
struct redBlackST {
    Node *root;
    int (*compareTo) (const void *key1, const void *key2);
};

/*------------------------------------------------------------*/
/* 
 *  ProtÃ³tipos de funÃ§Ãµes administrativas.
 * 
 *  Entre essa funÃ§Ãµes estÃ£o isRed(), rotateLeft(), rotateRight(),
 *  flipColors(), moveRedLeft(), moveRedRight() e balance().
 * 
 *  NÃ£o deixe de implmentar as funÃ§Ãµes chamadas pela funÃ§Ã£o 
 *  check(): isBST(), isSizeConsistent(), isRankConsistent(),
 *  is23(), isBalanced().
 *
 */

/*---------------------------------------------------------------*/
static Bool isBST(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool isSizeConsistent(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool isRankConsistent(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool is23(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool isBalanced(RedBlackST st);

/*-----------------------------------------------------------*/
/*
 *  initST(COMPAR)
 *
 *  RECEBE uma funÃ§Ã£o COMPAR() para comparar chaves.
 *  RETORNA (referÃªncia/ponteiro para) uma tabela de sÃ­mbolos vazia.
 *
 *  Ã‰ esperado que COMPAR() tenha o seguinte comportamento:
 *
 *      COMPAR(key1, key2) retorna um inteiro < 0 se key1 <  key2
 *      COMPAR(key1, key2) retorna 0              se key1 == key2
 *      COMPAR(key1, key2) retorna um inteiro > 0 se key1 >  key2
 * 
 *  TODAS OS OPERAÃ‡Ã•ES da ST criada utilizam a COMPAR() para comparar
 *  chaves.
 * 
 */
RedBlackST initST (int (*compar) (const void *key1, const void *key2)) {
    RedBlackST st = emalloc (sizeof (struct redBlackST));
    st->root = NULL;
    st->compareTo = compar;
    return st;
}

/***************************************************************************
*  Red-black tree helper functions.
***************************************************************************/

/*-----------------------------------------------------------*/
/* 
 *  SIZE(ST)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST.
 * 
 *  RETORNA o nÃºmero de itens (= pares chave-valor) na ST.
 *
 */
int size (RedBlackST st) {
    if (st->root == NULL)
        return 0;
    return st->root->size;
}

int sizeNode (Node *node) {
    if (node == NULL)
        return 0;
    return node->size;
}

// is node x red; false if x is null
static Bool isRed (Node *node) {
    if (node == NULL)
        return FALSE;
    return node->color == RED;
}

// make a left-leaning link lean to the right
static Node *rotateRight (Node *h) {
    // assert (h != null) && isRed(h->left);
    Node *x = h->left;
    h->left = x->right;
    x->right = h;
    x->color = x->right->color;
    x->right->color = RED;
    x->size = h->size;
    h->size = sizeNode (h->left) + sizeNode (h->right) + 1;
    return x;
}

// make a right-leaning link lean to the left
static Node *rotateLeft (Node *h) {
    // assert (h != null) && isRed(h->right);
    Node *x = h->right;
    h->right = x->left;
    x->left = h;
    x->color = x->left->color;
    x->left->color = RED;
    x->size = h->size;
    h->size = sizeNode (h->left) + sizeNode (h->right) + 1;
    return x;
}

// flip the colors of a node and its two children
static void flipColors (Node *h) {
    // h must have opposite color of its two children
    // assert (h != null) && (h->left != null) && (h->right != null);
    // assert (!isRed(h) &&  isRed(h->left) &&  isRed(h->right))
    //    || (isRed(h)  && !isRed(h->left) && !isRed(h->right));
    h->color = !h->color;
    h->left->color = !h->left->color;
    h->right->color = !h->right->color;
}

// Assuming that h is red and both h->left and h->left->left
// are black, make h->left or one of its children red->
static Node *moveRedLeft (Node *h) {
    // assert (h != null);
    // assert isRed(h) && !isRed(h->left) && !isRed(h->left->left);
    flipColors (h);
    if (isRed (h->right->left)) { 
        h->right = rotateRight (h->right);
        h = rotateLeft (h);
        flipColors (h);
    }
    return h;
}

    // Assuming that h is red and both h->right and h->right->left
    // are black, make h->right or one of its children red->
static Node *moveRedRight(Node *h) {
    // assert (h != null);
    // assert isRed(h) && !isRed(h->right) && !isRed(h->right->left);
    flipColors (h);
    if (isRed (h->left->left)) { 
        h = rotateRight (h);
        flipColors (h);
    }
    return h;
}

// restore red-black tree invariant
static Node *balance(Node *h) {
    // assert (h != null);
    if (isRed (h->right))                         h = rotateLeft (h);
    if (isRed (h->left) && isRed (h->left->left)) h = rotateRight (h);
    if (isRed (h->left) && isRed (h->right))      flipColors (h);

    h->size = sizeNode (h->left) + sizeNode (h->right) + 1;
    return h;
}


/*-----------------------------------------------------------*/
/*
 *  freeST(ST)
 *
 *  RECEBE uma RedBlackST  ST e devolve ao sistema toda a memoria 
 *  utilizada por ST.
 *
 */
void freeST (RedBlackST st) {
}


/*------------------------------------------------------------*/
/*
 * OPERAÃ‡Ã•ES USUAIS: put(), get(), contains(), delete(),
 * size() e isEmpty().
 */

/*-----------------------------------------------------------*/
/*
 *  put(ST, KEY, NKEY, VAL, NVAL)
 * 
 *  RECEBE a tabela de sÃ­mbolos ST e um par KEY-VAL e procura a KEY na ST.
 *
 *     - se VAL Ã© NULL, a entrada da chave KEY Ã© removida da ST  
 *  
 *     - se KEY nao e' encontrada: o par KEY-VAL Ã© inserido na ST
 *
 *     - se KEY e' encontra: o valor correspondente Ã© atualizado
 *
 *  NKEY Ã© o nÃºmero de bytes de KEY e NVAL Ã© o nÃºmero de bytes de NVAL.
 *
 *  Para criar uma copia/clone de KEY Ã© usado o seu nÃºmero de bytes NKEY.
 *  Para criar uma copia/clode de VAL Ã© usado o seu nÃºmero de bytes NVAL.
 *
 */

// insert the key-value pair in the subtree rooted at h
Node *auxPut (RedBlackST st, Node *h, void *key, void *val) { 
    if (h == NULL) 
        return newNode (key, val, RED);

    printf(" %s\n", h->key);
    
    int cmp = st->compareTo (h->key, key);
    if      (cmp < 0) h->left  = auxPut (st, h->left,  key, val); 
    else if (cmp > 0) h->right = auxPut (st, h->right, key, val); 
    else              printf("hmmm\n"); //h->val   = val;
/*
        // fix-up any right-leaning links
    if (isRed (h->right) && !isRed (h->left))      h = rotateLeft  (h);
    if (isRed (h->left)  &&  isRed (h->left->left)) h = rotateRight (h);
    if (isRed (h->left)  &&  isRed (h->right))     flipColors (h);
    */
    h->size = sizeNode (h->left) + sizeNode (h->right) + 1;
    return h;
}    

void put (RedBlackST st, const void *key, size_t sizeKey, const void *val, size_t sizeVal) {
    printf("\nPUT %s\n\n", key);
    if (key == NULL) {
        ERROR ("KEY EH NULL");
        return;
    }

    if (val == NULL) {
            //delete (st, key);
        return;
    }
    void *copyKey = emalloc (sizeKey);
    memcpy (copyKey, key, sizeKey);
    printf ("CopyKey: %s \n", copyKey);

    void *copyVal = emalloc (sizeVal);
    memcpy (copyVal, val, sizeVal);

    st->root = auxPut (st, st->root, copyKey, copyVal);
    st->root->color = BLACK;
        // assert check();
}

/*-----------------------------------------------------------*/
/*
 *  get(ST, KEY)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e uma chave KEY.
 *
 *     - se KEY estÃ¡ em ST, RETORNA uma cÃ³pia/clone do valor
 *       associado a KEY;
 *
 *     - se KEY nÃ£o estÃ¡ em ST, RETORNA NULL.
 * 
 */
void *get (RedBlackST st, const void *key) {
    Node *n = st->root;
    while (n != NULL) {
        int cmp = st->compareTo (n->key, key);
        if      (cmp < 0) n = n->left;
        else if (cmp > 0) n = n->right;
        else              return n->val;
    }
    return NULL;
}

/*-----------------------------------------------------------*/
/* 
 *  CONTAINS(ST, KEY)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e uma chave KEY.
 * 
 *  RETORNA TRUE se KEY estÃ¡ na ST e FALSE em caso contrÃ¡rio.
 *
 */
Bool contains (RedBlackST st, const void *key) {
    return get(st, key) != NULL;
}

/*-----------------------------------------------------------*/
/* 
 *  DELETE(ST, KEY)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST e uma chave KEY.
 * 
 *  Se KEY estÃ¡ em ST, remove a entrada correspondente a KEY.
 *  Se KEY nÃ£o estÃ¡ em ST, faz nada.
 *
 */


    // delete the key-value pair with the minimum key rooted at h
Node* deleteMinNode (Node *h) { 
    if (h->left == NULL)
        return NULL;

    if (!isRed (h->left) && !isRed (h->left->left))
        h = moveRedLeft (h);

    h->left = deleteMinNode (h->left);
    return balance (h);
}


void delete (RedBlackST st, const void *key) {
    if (isEmpty (st))
        ERROR (BST underflow);

        // if both children of root are black, set root to red
    if (!isRed (st->root->left) && !isRed (st->root->right))
        st->root->color = RED;

    st->root = deleteMinNode (st->root);
    if (!isEmpty (st))
        st->root->color = BLACK;
        // assert check();
}

/*-----------------------------------------------------------*/
/* 
 *  ISEMPTY(ST, KEY)
 *
 *  RECEBE uma tabela de sÃ­mbolos ST.
 * 
 *  RETORNA TRUE se ST estÃ¡ vazia e FALSE em caso contrÃ¡rio.
 *
 */
Bool isEmpty (RedBlackST st) {
    return st->root == NULL;
}

/*------------------------------------------------------------*/
/*
 * OPERAÃ‡Ã•ES PARA TABELAS DE SÃMBOLOS ORDENADAS: 
 * min(), max(), rank(), select(), deleteMin() e deleteMax().
 */

/*-----------------------------------------------------------*/
/*
 *  MIN(ST)
 * 
 *  RECEBE uma tabela de sÃ­mbolos ST e RETORNA uma cÃ³pia/clone
 *  da menor chave na tabela.
 *
 *  Se ST estÃ¡ vazia RETORNA NULL.
 *
 */
void *min (RedBlackST st) {

    return NULL;
}


/*-----------------------------------------------------------*/
/*
 *  MAX(ST)
 * 
 *  RECEBE uma tabela de sÃ­mbolos ST e RETORNA uma cÃ³pia/clone
 *  da maior chave na tabela.
 *
 *  Se ST estÃ¡ vazia RETORNA NULL.
 *
 */
void *max (RedBlackST st) {
    return NULL;
}


/*-----------------------------------------------------------*/
/*
 *  RANK(ST, KEY)
 * 
 *  RECEBE uma tabela de sÃ­mbolos ST e uma chave KEY.
 *  RETORNA o nÃºmero de chaves em ST menores que KEY.
 *
 *  Se ST estÃ¡ vazia RETORNA NULL.
 *
 */
int rank (RedBlackST st, const void *key) {
    return 0;
} 


/*-----------------------------------------------------------*/
/*
 *  SELECT(ST, K)
 * 
 *  RECEBE uma tabela de sÃ­mbolos ST e um inteiro K >= 0.
 *  RETORNA a (K+1)-Ã©sima menor chave da tabela ST.
 *
 *  Se ST nÃ£o tem K+1 elementos RETORNA NULL.
 *
 */
void *select (RedBlackST st, int k) {
    return NULL;
}


/*-----------------------------------------------------------*/
/*
 *  deleteMIN(ST)
 * 
 *  RECEBE uma tabela de sÃ­mbolos ST e remove a entrada correspondente
 *  Ã  menor chave.
 *
 *  Se ST estÃ¡ vazia, faz nada.
 *
 */
void deleteMin (RedBlackST st) {
    if (st->root == NULL)
        return;

}


/*-----------------------------------------------------------*/
/*
 *  deleteMAX(ST)
 * 
 *  RECEBE uma tabela de sÃ­mbolos ST e remove a entrada correspondente
 *  Ã  maior chave.
 *
 *  Se ST estÃ¡ vazia, faz nada.
 *
 */
void deleteMax (RedBlackST st) {
}


/*-----------------------------------------------------------*/
/* 
 *  KEYS(ST, INIT)
 * 
 *  RECEBE uma tabela de sÃ­mbolos ST e um Bool INIT.
 *
 *  Se INIT Ã© TRUE, KEYS() RETORNA uma cÃ³pia/clone da menor chave na ST.
 *  Se INIT Ã© FALSE, KEYS() RETORNA a chave sucessora da Ãºltima chave retornada.
 *  Se ST estÃ¡ vazia ou nÃ£o hÃ¡ sucessora da Ãºltima chave retornada, KEYS() retorna NULL.
 *
 *  Se entre duas chamadas de KEYS() a ST Ã© alterada, o comportamento Ã© 
 *  indefinido. 
 *  
 */
void *keys (RedBlackST st, Bool init) {
    return NULL;
}



/*------------------------------------------------------------*/
/* 
 * FunÃ§Ãµes administrativas
 */

/***************************************************************************
 *  Utility functions.
 ***************************************************************************/

/*
 * HEIGHT(ST)
 * 
 * RECEBE uma RedBlackST e RETORNA a sua altura. 
 * Uma BST com apenas um nÃ³ tem altura zero.
 * 
 */
int height (RedBlackST st) {
        return 0;
}


/***************************************************************************
 *  Check integrity of red-black tree data structure.
 ***************************************************************************/

/*
 * CHECK(ST)
 *
 * RECEBE uma RedBlackST ST e RETORNA TRUE se nÃ£o encontrar algum
 * problema de ordem ou estrutural. Em caso contrÃ¡rio RETORNA 
 * FALSE.
 * 
 */
Bool check (RedBlackST st) {
    if (!isBST(st))            ERROR("check(): not in symmetric order");
    if (!isSizeConsistent (st)) ERROR("check(): subtree counts not consistent");
    if (!isRankConsistent (st)) ERROR("check(): ranks not consistent");
    if (!is23 (st))             ERROR("check(): not a 2-3 tree");
    if (!isBalanced (st))       ERROR("check(): not balanced");
    return isBST (st) && isSizeConsistent (st) && isRankConsistent (st) && is23 (st) && isBalanced (st);
}


/* 
 * ISBST(ST)
 * 
 * RECEBE uma RedBlackST ST.
 * RETORNA TRUE se a Ã¡rvore Ã© uma BST.
 * 
 */
static Bool isBST (RedBlackST st) {
    return FALSE;
}


/* 
 *  ISSIZECONSISTENT(ST) 
 *
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se para cada nÃ³ h
 *  vale que size(h) = 1 + size(h->left) + size(h->right) e 
 *  FALSE em caso contrÃ¡rio.
 */
static Bool isSizeConsistent (RedBlackST st) {
    return FALSE;
}


/* 
 *  ISRANKCONSISTENT(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se seus rank() e
 *  select() sÃ£o consistentes.
 */  
/* check that ranks are consistent */
static Bool isRankConsistent (RedBlackST st) {
    return FALSE;
}

/* 
 *  IS23(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA FALSE se hÃ¡ algum link RED
 *  para a direta ou se ha dois links para esquerda seguidos RED 
 *  Em caso contrÃ¡rio RETORNA TRUE (= a ST representa uma Ã¡rvore 2-3). 
 */
static Bool is23 (RedBlackST st) {
    return FALSE;
}


/* 
 *  ISBALANCED(ST) 
 * 
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se st satisfaz
 *  balanceamento negro perfeiro.
 */ 
static Bool isBalanced (RedBlackST st) {
    return FALSE;
}
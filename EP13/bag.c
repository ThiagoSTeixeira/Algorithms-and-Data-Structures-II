/*
 * MAC0323 Algoritmos e Estruturas de Dados II
 * 
 * ADT Bag implementada com lista ligada de itens. 
 *  
 *    https://algs4.cs.princeton.edu/13stacks/
 *    https://www.ime.usp.br/~pf/estruturas-de-dados/aulas/bag.html
 * 
 * ATENÇÃO: por simplicidade Bag contém apenas inteiros (int) não 
 * negativos (>=0) que são 'nomes' de vértices (vertex) de um 
 * digrafo.
 */

/* interface para o uso da funcao deste módulo */
#include "bag.h"  

#include <stdlib.h>  /* free() */
#include <string.h>  /* memcpy() */
#include "util.h"    /* emalloc() */

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debuging */
#endif

/*----------------------------------------------------------*/
/* 
 * Estrutura Básica da Bag
 * 
 * Implementação com listas ligada dos itens.
 */
struct node{
    int data;
    struct node *next;
};

struct bag {
    int n;
    struct node *first;
    struct node *last;
};

/*------------------------------------------------------------*/
/* 
 * Protótipos de funções administrativas: tem modificador 'static'
 * 
 */

/*-----------------------------------------------------------*/
/*
 *  newBag()
 *
 *  RETORNA (referência/ponteiro para) uma bag vazia.
 * 
 */
Bag
newBag()
{
    Bag newBag = ecalloc(1, sizeof(struct bag));
    newBag -> n = 0;
    return newBag;
}

/*-----------------------------------------------------------*/
/*
 *  freeBag(BAG)
 *
 *  RECEBE uma Bag BAG e devolve ao sistema toda a memoria 
 *  utilizada.
 *
 */
void  
freeBag(Bag bag)
{
    struct node *pnt1 = bag->first;
    struct node *pnt2 = bag->first->next;
    while(pnt2->next != NULL){
        free(pnt1);
        pnt1 = pnt2;
        pnt2 = pnt2->next;
    }
    free(pnt1);
    free(pnt2);
    free(bag);
    }    

/*------------------------------------------------------------*/
/*
 * OPERAÇÕES USUAIS: add(), size(), isEmpty() e itens().
 */

/*-----------------------------------------------------------*/
/*
 *  add(BAG, ITEM, NITEM)
 * 
 *  RECEBE uma bag BAG e um ITEM e insere o ITEM na BAG.
 *  NITEM é o número de bytes de ITEM.
 *
 *  Para criar uma copia/clone de ITEM é usado o seu número de bytes NITEM.
 *
 */
void  
add(Bag bag, vertex item)
{
    struct node *novo = ecalloc(1, sizeof(struct node));
    novo->data = item;
    struct node *primeiroAntigo = bag->first;
    bag->first = novo;
    bag->first->next = primeiroAntigo;
    bag->n++;
}    

/*-----------------------------------------------------------*/
/* 
 *  SIZE(BAG)
 *
 *  RECEBE uma bag BAG
 * 
 *  RETORNA o número de itens em BAG.
 */
int
size(Bag bag)
{
    return bag -> n;
}

/*-----------------------------------------------------------*/
/* 
 *  ISEMPTY(BAG)
 *
 *  RECEBE uma bag BAG.
 * 
 *  RETORNA TRUE se BAG está vazia e FALSE em caso contrário.
 *
 */
Bool
isEmpty(Bag bag)
{
    return bag->n == 0;
}

/*-----------------------------------------------------------*/
/* 
 *  ITENS(BAG, INIT)
 * 
 *  RECEBE uma bag BAG e um Bool INIT.
 *
 *  Se INIT é TRUE,  ITENS() RETORNA uma cópia/clone do primeiro item na lista de itens na BAG.
 *  Se INIT é FALSE, ITENS() RETORNA uma cópia/clone do item sucessor do último item retornado.
 *  Se BAG está vazia ou não há sucessor do último item retornado, ITENS() RETORNA -1.
 *
 *  Se entre duas chamadas de ITENS() a BAG é alterada, o comportamento é  indefinido. 
 *  
 */
vertex 
itens(Bag bag, Bool init)
{
    if(isEmpty(bag)) return -1;
    if(init){
        bag->last = bag->first;
        return bag->first->data;
    }
    else{
        if(bag->last->next == NULL) return -1;
        struct node *prox = bag->last->next;
        bag->last = prox;
        return prox->data;
    }
}

/*------------------------------------------------------------*/
/* 
 * Implementaçao de funções administrativas
 */


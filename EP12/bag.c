/****************************************************************
    Nome: Thiago Santos Teixeira
    NUSP: 10736987

    Ao preencher esse cabeçalho com o meu nome e o meu número USP,
    declaro que todas as partes originais desse exercício programa (EP)
    foram desenvolvidas e implementadas por mim e que portanto não 
    constituem desonestidade acadêmica ou plágio.
    Declaro também que sou responsável por todas as cópias desse
    programa e que não distribui ou facilitei a sua distribuição.
    Estou ciente que os casos de plágio e desonestidade acadêmica
    serão tratados segundo os critérios divulgados na página da 
    disciplina.
    Entendo que EPs sem assinatura devem receber nota zero e, ainda
    assim, poderão ser punidos por desonestidade acadêmica.

    Abaixo descreva qualquer ajuda que você recebeu para fazer este
    EP.  Inclua qualquer ajuda recebida por pessoas (inclusive
    monitoras e colegas). Com exceção de material de MAC0323, caso
    você tenha utilizado alguma informação, trecho de código,...
    indique esse fato abaixo para que o seu programa não seja
    considerado plágio ou irregular.

    Exemplo:

        A monitora me explicou que eu devia utilizar a função xyz().

        O meu método xyz() foi baseada na descrição encontrada na 
        página https://www.ime.usp.br/~pf/algoritmos/aulas/enumeracao.html.

    Descrição de ajuda ou indicação de fonte:



    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/
/*
 * MAC0323 Algoritmos e Estruturas de Dados II
 * 
 * ADT Bag implementada com lista ligada de itens. 
 *  
 *    https://algs4.cs.princeton.edu/13stacks/
 *    https://www.ime.usp.br/~pf/estruturas-de-dados/aulas/bag.html
 * 
 * Os itens desta implementação são genéricos: "shallow copy" dos itens dados.
 *
 *----------------------------------------------------------------------------
 * Object copying
 * https://en.wikipedia.org/wiki/Object_copying
 * 
 * Understanding Deep and Shallow Copy 
 * https://we-are.bookmyshow.com/understanding-deep-and-shallow-copy-in-javascript-13438bad941c
 *
 * Shallow copy is a bit-wise copy of an object. A new object is created that has an 
 * exact copy of the values in the original object. If any of the fields of the object 
 * are references to other objects, just the reference addresses are copied i.e., 
 * only the memory address is copied.
 
 * A deep copy copies all fields, and makes copies of dynamically allocated memory 
 * pointed to by the fields. A deep copy occurs when an object is copied along with the 
 * objects to which it refers.
 *
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
    void *data;
    struct node *next;
    size_t size;        //tive que inserir esse atributo ja que o sizeof quando usado no struct devolvia o tamanho do ponteiro
};
struct bag{
    int n;
    struct node *first;
    struct node *last;      //atributo adicionado para a funcao itens, ultimo item enviado
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
    newBag->n = 0;
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
    struct node *pnt1 = bag->first;     //ponteiro 1
    struct node *pnt2 = bag->first->next;       //ponteiro 2
    while(pnt2->next != NULL){
        free(pnt1->data);
        free(pnt1);
        pnt1 = pnt2;
        pnt2 = pnt2->next;
    }
    free(pnt2->data);
    free(pnt2);
    free(pnt1->data);
    free(pnt1);
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
add(Bag bag, const void *item, size_t nItem)
{
    //aloca espaco para o novo node
    struct node *novo = ecalloc(1, sizeof(struct node));
    //aloca o item do novo node
    void *newItem = emalloc(nItem);
    memcpy(newItem, item, nItem);
    novo->data = newItem;
    novo->size = nItem;
    //adiciona na bag
    struct node *primeiroAntigo = bag-> first;
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
    return bag->n;
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
 *  Se BAG está vazia ou não há sucessor do último item retornada, ITENS() RETORNA NULL.
 *
 *  Se entre duas chamadas de ITENS() a BAG é alterada, o comportamento é  indefinido. 
 *  
 */
void * 
itens(Bag bag, Bool init)
{
    if(isEmpty(bag)) return NULL;
    if(init){
        void *dataClone = emalloc(bag->first->size);
        memcpy(dataClone, bag->first->data, bag->first->size);
        bag->last = bag->first;
        return dataClone;
    }
    else{
        if(bag->last->next == NULL) return NULL;
        struct node *prox = bag->last->next;
        void *dataClone = emalloc(prox->size);
        memcpy(dataClone, prox->data, prox->size);
        bag->last = prox;
        return dataClone;
    }
}

/*------------------------------------------------------------*/
/* 
 * Implementaçao de funções administrativas
 */


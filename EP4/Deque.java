

/****************************************************************
    Nome:
    NUSP:

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

    Esse codigo teve inspiracao no arquivo "Bag.java" oferecido pelo professor em suas notas de aula

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/
import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    public Node inicio;
    public int n;
    
    private class Node{
        private Item info;  
        private Node prox;

    }

    // construct an empty deque
    public Deque(){  
        inicio = new Node();
        inicio.prox=null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty(){
        return n==0;
    }

    // return the number of items on the deque
    public int size(){
        return n;
    }

    // add the item to the front
    public void addFirst(Item item){
        if(item == null) throw new IllegalArgumentException();
        Node inicioAntigo = inicio;
        //inicio = new Node();
        inicio.info = item;
        inicio.prox = inicioAntigo;
        n++;
    }

    // add the item to the back
    public void addLast(Item item){
        if(item == null) throw new IllegalArgumentException();
        n++;
        Node novo = inicio;         //apenas para inicializar a variavel
        novo.info = item;
        novo.prox =null;
        if(isEmpty()){
            addFirst(item);
        }
        else{
            Node pnt;
            pnt = inicio;
            while(pnt.prox != null){
                pnt = pnt.prox;
            }
            pnt.prox = novo;
        }
    }

    // remove and return the item from the front
    public Item removeFirst(){
        if(isEmpty()) throw new NoSuchElementException();
        Node res = inicio;
        inicio = inicio.prox;
        n--;
        return res.info;
    }

    // remove and return the item from the back
    public Item removeLast(){
        if(isEmpty()) throw new NoSuchElementException();
        Node res;
        Node pnt1 = inicio;
        Node pnt2;
        n--;
        if (inicio.prox != null){
            pnt2 = inicio.prox;
        }
        else{
            res = inicio;
            inicio = null;
            return res.info;
        }
        while(pnt2.prox!=null){
            pnt1 = pnt1.prox;
            pnt2 = pnt2.prox;
        }
        res = pnt1.prox;
        pnt1.prox = null;
        return res.info;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item>{
        private Node current;

        public boolean hasNext(){
            return current.prox != null;
        }
        public Item next(){
            if (current == null) throw new NoSuchElementException();
            Item item = current.info;
            current = current.prox;
            return item;
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args){
        Deque<String> deque = new Deque<String>();
        deque.addFirst("ABC");
        deque.addFirst("DEF");
        //deque.addLast("JKL");
        //deque.addLast("epDmerda");
        //deque.removeFirst();
        //deque.removeLast();
        //deque.addLast("bata");
        Iterator<String> it = deque.iterator();
        int i=0;
        while(it.hasNext()){
            StdOut.println(it.next());
            i++;
            if(i>10) break;
        }

    }

}

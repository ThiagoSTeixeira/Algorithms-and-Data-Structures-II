/****************************************************************
    Nome:Thiago Santo Teixeira
    NUSP:10736987

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
//--------------------------------------------------------//
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class Percolation {
    private int [][] grid;   
    private final int fechado = 0;
    private final int aberto = 1;
    private int N;
    private WeightedQuickUnionUF tab;
     
    // creates n-by-n grid, with all sites initially blocked
    
    public Percolation(int n){
        grid = new int [n][n];
        N=n;
        tab = new WeightedQuickUnionUF((N*N)+2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        if(row<0 || row > N || col<0 || col> N) throw new IllegalArgumentException();
        grid[row][col] = aberto;                        //abre o espaco no grid
        if(row == 0) tab.union(0, (row*N)+ col);        //se row = 0
        if(row == N-1) tab.union(N*N +1, row*N +col);     // se row = n-1
        if(row < N-1 && isOpen(row+1, col)) tab.union(row*N + col, (row+1)*N+col); //se row < n-1
        if(row > 0 && isOpen(row-1, col)) tab.union(row*N +col, (row-1)*N + col);    // se row > 0
        if(col < N-1 && isOpen(row, col+1)) tab.union(row*N + col, row*N + col + 1); // se col< N-1
        if(col > 0 && isOpen(row, col-1)) tab.union(row*N+col, (row*N +col-1));     //se col >0
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if(row<0 || row >= N || col < 0 || col >= N) throw new IllegalArgumentException();
        return grid[row][col] == aberto;

    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        if(row<0 || row >= N || col < 0 || col >= N) throw new IllegalArgumentException();
        return grid[row][col] == fechado;
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        int contagem=0;
        for(int i = 0; i<N; i++){
            for(int j = 0; j<N; j++){
                if(isOpen(i, j)) contagem ++;
            }
        }
        return contagem;
    }

    // does the system percolate?
    public boolean percolates(){
        return tab.connected(0, N*N + 1);       

    }
}
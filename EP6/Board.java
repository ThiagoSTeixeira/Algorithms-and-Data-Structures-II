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
        Beneficios do StringBuilder:
        https://stackoverflow.com/questions/1532461/stringbuilder-vs-string-concatenation-in-tostring-in-java

        Como comparar vetores compostos:
        https://stackoverflow.com/questions/44299100/how-do-i-compare-two-dimensional-or-nested-java-arrays

        Como usar package:
        https://www.programiz.com/java-programming/packages-import


    Descrição de ajuda ou indicação de fonte:

    Esse codigo teve inspiracao no arquivo "Bag.java" oferecido pelo professor em suas notas de aula

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/
//package ep6;    //https://www.programiz.com/java-programming/packages-import
import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;
//--------------------------------------//
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Stack;

public class Board {
    private final int[][] tiles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles){
        n = tiles.length;
        this.tiles = new int [n][n];
        for(int i=0; i<n;i++) this.tiles[i]= Arrays.copyOf(tiles[i], n);
    }                                   
    // string representation of this board
    public String toString(){
        StringBuilder s = new StringBuilder();      //Usei string builder e .append ja que oferece beneficios de memoria e tempo em relacao a Strings simples
        s.append(n).append("\n");
        for(int i=0; i<n;i++){
            for(int j=0; j<n; j++){
                s.append(String.format("%2d", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // tile at (row, col) or 0 if blank
    public int tileAt(int row, int col){
        if(row < 0 || row>n-1 || col< 0 || col>n-1) throw new IllegalArgumentException();
        return tiles[row][col];
    }

    // board size n
    public int size(){
        return n;
    }
    
    //Retorna o valor que deveria estar em tiles[i][j]
    private int ValorDesejado(int i, int j) {      
        if (i == n - 1 && j == n - 1) return 0; //espaco vazio
        return i * n + j + 1;       //sempre dara o valor desejado para tal bloco
    }

    // number of tiles out of place
    public int hamming(){
        int hamming=0;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(tiles[i][j] !=0 && tiles[i][j] != ValorDesejado(i, j)) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        int manhattan = 0;      //distancia manhattan
        int posi=0;            //posicao desejada no i
        int posj=0;             //posicao desejada no j
        int soma=0;
        for(int i =0;i<n;i++){
            for(int j= 0; j<n; j++){
                if(tiles[i][j] !=0 && tiles[i][j]!= ValorDesejado(i, j)){
                    posi = (tiles[i][j]-1)/n;       //encontra o i desejado para o valor atual. Como nao começa no zero, temos que subtrair 1
                    posj= tiles[i][j] - posi*n -1; //encontra o j desejado para o valor atual. Com o i em mãos, encontrar o j nao eh dificil.
                    soma = Math.abs(i-posi) + Math.abs(j-posj);
                    manhattan += soma;
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal(){
        return hamming() == 0;  //se nao ha numeros fora de lugar, o tabuleiro esta correto
    }

    // does this board equal y?
    public boolean equals(Object y){
        if(y==null || y.getClass() != this.getClass()) return false;
        if(y == this) return true;
        Board prov = (Board) y;                                  //casting
        return Arrays.deepEquals(this.tiles, prov.tiles);       //tive que usar deepEquals ja que sao vetores compostos
    }  

    private void troca(int i, int j, int i2, int j2){
        int temp = tileAt(i, j);
        tiles[i][j]= tiles[i2][j2];
        tiles[i2][j2]= temp;
    }
    // all neighboring boards
    public Iterable<Board> neighbors(){
        boolean achou = false;
        int i2=0;
        int j2=0;
        for(int i =0; i<n;i++){
            for(int j=0; j<n; j++){
                if(tiles[i][j] == 0){
                    achou = true;
                    i2= i;
                    j2=j;
                }
                if(achou) break;
            }
        if(achou) break;
        }
        Stack<Board> pilha = new Stack<>();
        Board board;
        if(i2 != 0){
            board = new Board(tiles);
            board.troca(i2, j2, i2-1, j2);
            pilha.push(board);
        }
        if(i2 != n-1){
            board = new Board(tiles);
            board.troca(i2, j2, i2+1, j2);
            pilha.push(board);
        }
        if(j2!=0){
            board = new Board(tiles);
            board.troca(i2, j2, i2, j2-1);
            pilha.push(board);
        }
        if(j2!= n-1){
            board = new Board(tiles);
            board.troca(i2, j2, i2, j2+1);
            pilha.push(board);
        }
        return pilha;
    }

    //coloquei a isSolvable em solver.java

    public Board irmao() {       //board devolvido apos trocar 2 elementos (usado em solver.java)
        Board board = new Board(tiles);
        for (int i = 0; i < 2; i++) {
            if (board.tiles[i][0] != 0 && board.tiles[i][1] != 0) {
                board.troca(i, 0, i, 1);
                return board;
            }
        }
        throw new IllegalArgumentException();
    }

    // unit testing (required)
    public static void main(String[] args){
        int n = Integer.parseInt(args[0]);
        int[][] entrada = new int[n][n];
        int numero = n*n -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                entrada[i][j] = numero--;
            }
        }
        Board b = new Board(entrada);
        StdOut.println(b);
    }

}
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

//package ep6;
import java.lang.IllegalArgumentException;
//----------------------------------------//
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


public class Solver {
    private class Node implements Comparable<Node> {        //implementa a subclasse Node.

        private final Board board;
        private final int movimentos;
        private final Node ant;
        private int Pmanhattan = -1;

        Node(Board board, int movimentos, Node ant) {
            this.board = board;
            this.movimentos = movimentos;
            this.ant = ant;
        }

        private int achaPmanhattan() {
            if (Pmanhattan == -1) Pmanhattan = board.manhattan() + movimentos;
            return Pmanhattan;
        }

        @Override
        public int compareTo(Node node) {
            return Integer.compare(this.achaPmanhattan(), node.achaPmanhattan());
        }
    }
    //---------------------------------------------------------------------------//
    private Node objetivo;
    private int movimentos;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        if(initial ==null) throw new IllegalArgumentException();        //corner case do enunciado
        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> qp = new MinPQ<>();
        pq.insert(new Node(initial, 0, null));
        qp.insert(new Node(initial.irmao(), 0, null));
        Node c,k;
        while (!pq.min().board.isGoal() && !qp.min().board.isGoal()) {
            c = pq.delMin();
            k= qp.delMin();
            for (Board vizinho : c.board.neighbors()) {
                if (c.ant == null || !vizinho.equals(c.ant.board)) pq.insert(new Node(vizinho, c.movimentos + 1, c));
            }
            for (Board vizinho : k.board.neighbors()) {
                if (k.ant == null || !vizinho.equals(k.ant.board)) qp.insert(new Node(vizinho, k.movimentos + 1, k));
            }
        }
        if (pq.min().board.isGoal()) {
            movimentos = pq.min().movimentos;
            objetivo = pq.min();
        } 
        else movimentos = -1;
    }

    public boolean isSolvable(){
        return movimentos != -1;
    }
    // min number of moves to solve initial board
    public int moves(){
        return movimentos;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution(){
        if (!isSolvable()) return null;
        Stack<Board> solucao = new Stack<>();
        Node node = objetivo;
        while (node != null) {
            solucao.push(node.board);
            node = node.ant;
        }
        return solucao;
        
    }

    // test client (see below) 
    public static void main(String[] args) {
        // cria board do arquivo
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board comeco = new Board(blocks);
        // solve the puzzle
        Solver solver = new Solver(comeco);

        // print solucao
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
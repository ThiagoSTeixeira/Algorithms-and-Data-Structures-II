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
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;
import java.util.Arrays;


public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform(){
        String input = BinaryStdIn.readString();
        CircularSuffixArray suffixes = new CircularSuffixArray(input);
        int first = 0;
        StringBuilder ultimaColuna = new StringBuilder();

        for(int i =0; i< suffixes.length(); i++){
            int index = suffixes.index(i)-1;
            if(index < 0) index = suffixes.length() -1;
            ultimaColuna.append(input.charAt(index));
            if(suffixes.index(i) == 0) first = i;

        }

        BinaryStdOut.write(first);
        BinaryStdOut.write(ultimaColuna.toString());
        BinaryStdIn.close();
        BinaryStdOut.close();
    }


    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform(){
        int first = BinaryStdIn.readInt();
        String input = BinaryStdIn.readString();
        char[] sorted = input.toCharArray();
        Arrays.sort(sorted);
        int[] next = new int[input.length()];
        int currentSymbol = sorted[0];
        Queue<Integer> currentIndexes = new Queue<>();
        currentIndexes.enqueue(0);

        for(int i =1; i < sorted.length; i++){
            if(sorted[i] != currentSymbol) {
                preencher(input, next, currentSymbol, currentIndexes, i);
                currentSymbol = sorted[i];
            }
            currentIndexes.enqueue(i);
        }


        preencher(input, next, currentSymbol, currentIndexes, sorted.length);
        StringBuilder source = new StringBuilder();
        while( source.length() != sorted.length){
            source.append(sorted[first]);
            first = next[first];
        }


        BinaryStdOut.write(source.toString());
        BinaryStdIn.close();
        BinaryStdOut.close();   
    }


    private static void preencher(String input, int[] next, int currentSymbol, Queue<Integer> currentIndexes, int currentIndex){
        int lastOccur = 0;
        while(!currentIndexes.isEmpty()){
            int index = input.indexOf(currentSymbol, lastOccur);
            lastOccur = index+1;
            next[currentIndex - currentIndexes.size()] = index;
            currentIndexes.dequeue();
        }
    } 
    
    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args){
        if (args[0].charAt(0) == '-') {
            transform();
        } else if (args[0].charAt(0) == '+') {
            inverseTransform();
        }
    }
}

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
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private int[] indexes;

    // circular suffix array of s
    public CircularSuffixArray(String s){
        if (s == null) throw new IllegalArgumentException();
        int length = s.length();
        indexes = new int[length];
        for(int i =0; i<length; i++) indexes[i] = i;
        sort(s);    //metodo sort implementado abaixo
    }
    private void sort(String s){
        int alfa = 256; //alfabeto
        int length = s.length();
        int[] aux = new int[length];

        char[] c = s.toCharArray();
        for(int k = length-1; k>=0; k--){
            int[] count = new int[alfa + 1];
            for (int i = 0; i < length; i++) count[c[i] + 1]++;
            for (int r = 0; r < alfa; r++) count[r + 1] += count[r];
            for (int i = 0; i < length; i++) aux[count[c[(k + indexes[i]) % length]]++] = indexes[i];
            for (int i = 0; i < length; i++) indexes[i] = aux[i];
        }
    }
    // length of s
    public int length(){
        return indexes.length;
    }

    // returns index of ith sorted suffix
    public int index(int i){
        if (i < 0 || i> length()) throw new IllegalArgumentException();
        return indexes[i];
    }

    // unit testing (required)
    public static void main(String[] args){

    }

}

/****************************************************************
    Nome:Thiago Santos Teixeira
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
import java.util.LinkedList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode(){
        char c;
        LinkedList<Character> symbols = generateSymbolTable();
        while(!BinaryStdIn.isEmpty()){
            c = BinaryStdIn.readChar(8);
            int index = symbols.indexOf(c);
            BinaryStdOut.write(index, 8);
            symbols.remove(index);
            symbols.addFirst(c);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }
    private static LinkedList<Character> generateSymbolTable(){
        LinkedList<Character> symbols = new LinkedList<>();
        for(int i = 0; i< 256; i++) symbols.add((char) i);
        return symbols;
    } 

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode(){
        int index;
        LinkedList<Character> symbols = generateSymbolTable();
        while(!BinaryStdIn.isEmpty()){
            index = BinaryStdIn.readInt(8);
            char c = symbols.get(index);
            BinaryStdOut.write(c, 8);
            symbols.remove(index);
            symbols.addFirst(c);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();

    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args){
        if (args[0].charAt(0) == '-') encode();
        else if (args[0].charAt(0) == '+') decode();
    }

}
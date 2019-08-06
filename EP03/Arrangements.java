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
    Os metodos e funcoes para a permutacao da string foram inspirados em https://www.geeksforgeeks.org/lexicographic-permutations-of-string/


    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/

// excessões pedidas
import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;
import java.util.NoSuchElementException;

// pode ser útil
import java.util.Arrays; // Arrays.sort(), Arrays.copyOf(), ...

import java.util.Iterator; // passo 0 para criarmos um iterador

import edu.princeton.cs.algs4.StdOut;

public class Arrangements implements Iterable<String> {

    public char[] strConvertida;
    public int count = 0;
    private static void inverte(char[] strConvertida, int inicio, int fim){         //Funcao que inverte a substring apos inicio
        bubble(strConvertida, inicio, fim);
        while(inicio < fim){
            swap(strConvertida, inicio, fim);
            inicio++;
            fim--;
        }
    }
    public static int teto(char[] strConvertida, int comeco, char primeiro, int fim){     //encontra o menor caracter apos o comeco
        int teto = comeco;
        for(int i= comeco+1; i <= fim; i++){
            if(strConvertida[i] > primeiro && strConvertida[i]< strConvertida[teto]) teto = i;
        }
        return(teto); 
    }
    public static void swap(char[] strConvertida,int rmst,int tto){             //troca dois caracteres de lugar
        char temp = strConvertida[rmst];
        strConvertida[rmst] = strConvertida[tto];
        strConvertida[tto] = temp;
    }
    private static void bubble(char[] strConvertida,int inicio,int fim){       
        for(int i = inicio; i < fim; i++){
            if(strConvertida[i]<strConvertida[i+1]) swap(strConvertida, i, i+1);
        }
    }
    public Arrangements(String s){                  //construtor
        if (s == null) throw IllegalArgumentException;
        strConvertida = s.toCharArray();
        //Arrays.sort(strConvertida);
        //s = String.valueOf(strConvertida);

    }
    public int rightmost(char[] strConvertida,int n){       //encontra o primeiro caracter da direita para a esquerda menor que o seguinte.
            int i;
            for(i= n - 2; i>=0; i--){
                if(strConvertida[i]<strConvertida[i+1]) return i;
            }
            return(-1);
    }

    public Iterator<String> iterator(){         
        return new StringIterator();
    }
    private class StringIterator implements Iterator<String>{
        int n = strConvertida.length;
        
        public boolean hasNext(){
            if (rightmost(strConvertida, n) != -1) return true;
            Arrays.sort(strConvertida);
            count = 0;
            return false;
        }
        public String next(){
            if(!hasNext()) throw new NoSuchElementException();
            if(count == 0){
                Arrays.sort(strConvertida);
                count++;
                return String.valueOf(strConvertida);
                
            }
            int rmst = rightmost(strConvertida, n);
            int tto = teto(strConvertida, rmst+1, strConvertida[rmst], strConvertida.length-1);
            swap(strConvertida, rmst, tto);
            inverte(strConvertida, rmst+1, strConvertida.length-1);
            String s = String.valueOf(strConvertida);
            count++;
            return(s);
            
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }

    // Unit test
    public static void main(String[] args) {
        String s = args[0];
        Arrangements arr = new Arrangements(s);
        
        StdOut.println("Teste 1: imprime no máximo os 10 primeiros arranjos");
        Iterator<String> it = arr.iterator();
        for (int i = 0; it.hasNext() && i < 10; i++) {
            StdOut.println(i + " : " + it.next());
        }
        
        StdOut.println("Teste 2: imprime todos os arranjos");
        int i = 0;
        for (String arranjo: arr) {
            StdOut.println(i + " : " + arranjo);
            i++;
        }
    }
}

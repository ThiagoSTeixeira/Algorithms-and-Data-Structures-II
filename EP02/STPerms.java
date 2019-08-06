/****************************************************************
    Nome: Thiago Santos Teixeira
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



    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/

/******************************************************************************
 *  Compilation:  javac-algs4 STPerms.java
 *  Execution:    java STPerms n s t opcao
 *  
 *  Enumera todas as (s,t)-permutações das n primeiras letras do alfabeto.
 *  As permutações devem ser exibidas em ordem lexicográfica.
 *  Sobre o papel da opcao, leia o enunciado do EP.
 *
 *  % java STPerms 4 2 2 0
 *  badc
 *  bdac
 *  cadb
 *  cdab
 *  % java STPerms 4 2 2 1
 *  4
 *  % java STPerms 4 2 2 2
 *  badc
 *  bdac
 *  cadb
 *  cdab
 *  4
 *   
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

public class STPerms {
    private static String letras = "abcdefghijklmnopqrstuvwxyz";
    private static int count = 0;
    private static int maiorSeqCrescente = 0;
    private static int maiorSeqDecrescente = 0;


    public static void maxsequenciaCrescente(String nelementos, int maiorSeqCrescente, int atual){
        int comparador;
        int maxLocal = 0;
        int atualLocal = atual;
        for(comparador = atual+1; comparador < nelementos.length(); comparador++){
            if(nelementos.charAt(atualLocal) < nelementos.charAt(comparador)){
                maxLocal++;
                atualLocal = comparador;
            }
        }
        if(maxLocal > maiorSeqCrescente) maiorSeqCrescente = maxLocal;
    }
    public static void maxsequenciaDecrescente(String nelementos, int maiorSeqDecrescente, int atual){
        int comparador;
        int maxLocal = 0;
        int atualLocal = atual;
        for(comparador = atual+1; comparador < nelementos.length(); comparador++){
            if(nelementos.charAt(atualLocal) > nelementos.charAt(comparador)){
                maxLocal++;
                atualLocal = comparador;
            }
        }
        if(maxLocal > maiorSeqDecrescente) maiorSeqDecrescente = maxLocal;
    }    

    public static void permuta(String prefix, String str, int s, int t, int opcao) {                   //Essa funcao foi adaptada do codigo de inspiracao Permutations.java
        int n = str.length();
        if (n == 0 ){
            for( int i = 0; i< prefix.length()-1; i++){
                maxsequenciaCrescente(prefix, maiorSeqCrescente, i);
                maxsequenciaDecrescente(prefix, maiorSeqDecrescente, i);
            }
            if (maiorSeqCrescente == s && maiorSeqDecrescente == t){
                if(opcao != 1) StdOut.println(prefix);
                count++;
            }
            maiorSeqCrescente = 0;
            maiorSeqDecrescente = 0;

        }
        else {
            for (int i = 0; i < n; i++)
            permuta(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n), s, t, opcao);
        }

    }
    public static void main(String[] args){
        int n = Integer.parseInt(args[0]);
        int s = Integer.parseInt(args[1]);
        int t = Integer.parseInt(args[2]);
        int opcao = Integer.parseInt(args[3]);
        String nelementos = letras.substring(0, n);
        permuta("", nelementos, s, t, opcao);
        if(opcao != 0) StdOut.println(count);
        
    }
}
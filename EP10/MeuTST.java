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

/*************************************************************************
 *  Compilation:  javac MeuTST.java
 *  Execution:    java MeuTST wiktionary.txt
 *
 *  Symbol table with string keys, implemented using a ternary search
 *  trie (TST).
 *
 *  Remarks
 *  --------
 *    - can't use a key that is the empty string ""
 *
 *************************************************************************/

// Todos os includes a seguir são devidos a tarefa de MAC0323
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import java.util.Comparator;

import edu.princeton.cs.algs4.BST;
/**
 *  The {@code TST} class represents an symbol table of key-value
 *  pairs, with string keys and generic values.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides character-based methods for finding the string
 *  in the symbol table that is the <em>longest prefix</em> of a given prefix,
 *  finding all strings in the symbol table that <em>start with</em> a given prefix,
 *  and finding all strings in the symbol table that <em>match</em> a given pattern.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a ternary search trie.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/52trie">Section 5.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
// public class TST<Value> { MAC0323
public class MeuTST<Value extends Comparable<Value>> {
    
    private int n;              // size
    private Node<Value> root;   // root of TST

    private static class Node<Value> {
        private char c;                        // character
        private Node<Value> left, mid, right;  // left, middle, and right subtries
        private Value val;                     // value associated with string
    }

    /**
     * Initializes an empty string symbol table.
     */
    public MeuTST() {
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    // return subtrie corresponding to given key
    private Node<Value> get(Node<Value> x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key"); 
        }
        if (val == null) {
            delete(key);
        }    
        if (!contains(key)) n++;
        root = put(root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<Value>();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     *     or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        }
        if (query.length() == 0) return null;
        int length = 0;
        Node<Value> x = root;
        int i = 0;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if      (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                i++;
                if (x.val != null) length = i;
                x = x.mid;
            }
        }
        return query.substring(0, length);
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        Node<Value> x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.enqueue(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }


    /**
     *  TAREFA: keysWithPrefixByValue():
     * 
     *  Cria e retorna uma coleção iterável de strings.
     *
     *  A coleção deve conter as strings que tem 'prefix' como 
     *  prefixo. Além disso os strings na coleção devem estar 
     *  em ordem decrescente de valor (val). 
     * 
     *  Sinta-se a vontade para: 
     *
     *     - criar métodos auxiliares; 
     *     - criar classes auxiliares; e
     *     - usar classes do algs4, e nesse caso não deixe 
     *       de colocar o import correspondente.
     *
     */
    // all keys starting with given prefix
    public Iterable<String> keysWithPrefixByValue(String prefix) {
        if(prefix == null) throw new IllegalArgumentException();
        BST<String, Value> auxBST = montaAux(prefix);
        Queue<String> queue = new Queue<String>();
        while(!auxBST.isEmpty()){
            queue.enqueue(auxBST.max());
            auxBST.deleteMax();        
        }
        return queue;
        
    }
    private BST<String, Value> montaAux(String prefix){
        BST<String, Value> bst = new BST<String, Value>();
        for(String s : keysWithPrefix(prefix)) bst.put(s, get(s));
        return bst;
    }

     
    
    // all keys in subtrie rooted at x with given prefix
    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.val != null) queue.enqueue(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }


    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     *     as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), 0, pattern, queue);
        return queue;
    }
 
    private void collect(Node<Value> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
        if (x == null) return;
        char c = pattern.charAt(i);
        if (c == '.' || c < x.c) collect(x.left, prefix, i, pattern, queue);
        if (c == '.' || c == x.c) {
            if (i == pattern.length() - 1 && x.val != null) queue.enqueue(prefix.toString() + x.c);
            if (i < pattern.length() - 1) {
                collect(x.mid, prefix.append(x.c), i+1, pattern, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, i, pattern, queue);
    }

    
    /**
     * TAREFA: delete()
     *  
     * Não estava implementado. Hmm.
     *
     * Removes the key from the set if the key is present.
     * @param key the key
     * @throws NullPointerException if {@code key} is {@code null}
     * 
     * Utilize o método delete da classe TrieST como fonte de 
     * inspiração.
     */



    public void delete(String key) {
        // TAREFA
        if (key == null)
            throw new NullPointerException("delete(): argument key is null");
        delete(root, key, 0);
    }
    private void delete(Node<Value> x, String key, int d){
        if (x==null) return;
        char c = key.charAt(d);
        if(c < x.c) delete(x.left, key, d);
        else if (c > x.c) delete(x.right, key, d);
        else if (c == x.c && x.val != null && d == key.length() - 1) {
            x.val = null;
            this.n--;
        }
        else delete(x.mid, key, d+1);
    }

    
    /**
     * Unit tests the {@code TST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        MeuTST<Long> terms = new MeuTST<Long>();
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();  // read the next weight
            in.readChar();                // scan past the tab
            String query = in.readLine(); // read the next query
            terms.put(query, weight);     // construct the term
        }
        
        StdOut.println(terms.size() + " itens (= pares chave-valor) na TST");
        StdOut.println("Teste interativo. Digite algo e tecle ENTER. Tecle crtl+d para encerrar,");
        // read in queries from standard input and print out the matching terms
        StdOut.print(">>> ");
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Iterable<String> results = terms.keysWithPrefix(prefix);
            StdOut.println("----------------------");
            for (String key : results)
                StdOut.println("   '" + key + "' : " + terms.get(key));
            StdOut.println("----------------------");
            Iterable<String> resultsValue = terms.keysWithPrefixByValue(prefix);
            for (String key : resultsValue)
                StdOut.println("   '" + key + "' : " + terms.get(key));
            StdOut.print(">>> ");
        }

        // teste delete()
        StdOut.println("\niniciando teste de delete()...");
        MeuTST<Integer> st = new MeuTST<Integer>();
        st.put("are",12);
        st.put("by",4);
        st.put("sea",14);
        st.put("sells",11);
        st.put("she",10);
        st.put("shells",15);
        st.put("shore",7);
        st.put("surely",13);
        st.put("sur",0);
        st.put("the",8);
        
        StdOut.println(st.size() + " itens: ");
        for (String key : st.keys())
            StdOut.println("   '" + key + "' : " + st.get(key));
        
        st.delete("sea");
        StdOut.println("\n"+ st.size() + " itens depois de remover 'sea': ");
        for (String key : st.keys())
            StdOut.println("  '" + key + "' : " + st.get(key));

        st.delete("sea");
        StdOut.println("\n" + st.size() + " itens depois de remover 'sea' novamente: ");
        for (String key : st.keys())
            StdOut.println("   '" + key + "' : " + st.get(key));

        st.delete("are");
        st.delete("the");
        st.delete("by");
        StdOut.println("\n" + st.size() + " itens depois de remover 'are', 'the', 'by': ");
        for (String key : st.keys()) {
            StdOut.println("   '" + key + "' : " + st.get(key));
            st.delete(key);
        }

        StdOut.println("\n" + st.size() + " itens depois de remover... tudo: ");
        for (String key : st.keys()) {
            StdOut.println("   '" + key + "' : " + st.get(key));
        }

        StdOut.println("fim dos testes.");
    }
}


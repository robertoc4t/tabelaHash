package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ChainingHashSt<Key, Value> {

    private static final int INIT_CAPACITY = 4;

    private int n;  // number of key-value pairs
    private int m;  // hash table size
    private DequeSearch<Key, Value>[] st;

    public ChainingHashSt() {
        this(INIT_CAPACITY);
    }

    public ChainingHashSt(int m) {
        this.m = m;
        st = (DequeSearch<Key, Value>[]) new DequeSearch[m];
        for (int i = 0; i < m; i++)
            st[i] = new DequeSearch<Key, Value>();
    }

    private void resize(int chains) {
        ChainingHashSt<Key, Value> temp = new ChainingHashSt<>(chains);
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys()) {
                temp.put(key, st[i].get(key));
            }
        }
        this.m = temp.m;
        this.n = temp.n;
        this.st = temp.st;
    }

    private int hash(Key key) {
        int h = Math.abs(key.hashCode());
        int itmp = (int) ((Math.sqrt(5) - 1) / 2 * h) % m;
        return itmp;
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("Argument to contains() is null");
        return get(key) != null;
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("Argument to get() is null");
        int i = hash(key);
        return st[i].get(key);
    }

    public void put(Key key, Value value) {
        if (key == null) throw new IllegalArgumentException("First argument to put() is null");
        if (value == null) {
            delete(key);
            return;
        }
        if (n >= 10 * m) resize(2 * m);
        int i = hash(key);
        if (!st[i].contains(key)) n++;
        st[i].put(key, value);
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("Argument to delete() is null");
        int i = hash(key);
        if (st[i].contains(key)) n--;
        st[i].delete(key);
        if (m > INIT_CAPACITY && n <= m / 2) resize(m / 2); // Ajuste aqui
    }

    public Iterable<Key> keys() {
        Deque<Key> queue = new Deque<>();
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys())
                queue.push_back(key);
        }
        return queue;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("\n\nUso: java ChainingHashST arquivo1 arquivo2\n\n");
            System.exit(0);
        }
        int n;
        String tmp;
        StringTokenizer st;
        ChainingHashSt<String, Cidade> tabelahash = new ChainingHashSt<>(16);

        Cidade city;

        try {
            FileReader in1 = new FileReader(args[0]);
            BufferedReader br = new BufferedReader(in1);
            n = Integer.parseInt(br.readLine());

            for (int j = 0; j < n; j++) {
                tmp = br.readLine();
                st = new StringTokenizer(tmp);
                city = new Cidade(st.nextToken(), Integer.parseInt(st.nextToken()));
                tabelahash.put(city.get_nome(), city);
            }
            br.close();
            in1.close();

            in1 = new FileReader(args[1]);
            br = new BufferedReader(in1);

            n = Integer.parseInt(br.readLine());
            for (int j = 0; j < n; j++) {
                tmp = br.readLine();
                city = tabelahash.get(tmp);
                if (city == null) System.out.println("\n[Failed] " + tmp + " Não foi encontrado.");
                else {
                    System.out.println("\n[Ok]\t" + city.get_nome() + " foi encontrada. temperatura lá é " + city.get_temp() + " F");
                }
            }
            br.close();
            in1.close();
            System.out.println("\n");
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}

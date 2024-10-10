package org.example;

import java.util.ListIterator;

public class DequeSearch<Key,Item> implements Iterable<Item> {
    private int n;
    private No Sentinela;

    public DequeSearch() {
        n = 0;
        Sentinela = new No();
        Sentinela.prox = Sentinela;
        Sentinela.ant = Sentinela;
    }

    private class No{
        private Item dado;
        private Key chave;
        private No prox;
        private No ant;
    }

    public void push_front(Key key, Item item){
        //criar novo no e armazenar dados
        No temp = new No();
        temp.dado = item;
        temp.chave = key;

        //definir anterior e próximo do novo no
        temp.ant = Sentinela;
        temp.prox = Sentinela.prox;

        //ajustar a sentinela e o seguinte
        Sentinela.prox = temp;
        temp.prox.ant = temp;
        n++;
    }

    public void push_back(Key key, Item item){
        //criar novo no e armazenar dados
        No temp = new No();
        temp.dado = item;
        temp.chave = key;

        //definir anterior e proximo do novo no
        temp.ant = Sentinela.ant;
        temp.prox = Sentinela;

        //ajustar a sentinela e o anterior
        Sentinela.ant = temp;
        temp.ant.prox = temp;
        n++;
    }

    public boolean contains(Key key){
        if(key == null) throw new IllegalArgumentException("Argumento para contains é nulo");
        return get(key) != null;
    }

    public Item get(Key key){
        if(key == null) throw new IllegalArgumentException("Argumento para get nulo");
        for(No x = Sentinela.prox;x!=Sentinela;x=x.prox){
            if(key.equals(x.chave)){
                return x.dado;
            }
        }
        return null;
    }

    public void delete(Key key){
        if(key == null) throw new IllegalArgumentException("Argumento para delete nulo");
        delete(Sentinela.prox,key);
    }

    private void remove(No temp){
        temp.ant.prox = temp.prox;
        //Atualizar o nó próximo para apontar para o anterior do que será removido
        temp.prox.ant = temp.ant;
        n--;
    }

    private void delete(No x, Key key){
        if(x == Sentinela) return;
        if(key.equals(x.chave)){
            remove(x);
            return;
        }
        delete(x.prox, key);
    }

    public void put(Key key, Item val){
        if(key == null) throw new IllegalArgumentException("Argumento 'key' para put é nulo");
        if(val == null){
            delete(key);
            return;
        }
        for (No x = Sentinela.prox;x!=Sentinela;x=x.prox){
            if(key.equals(x.chave)){
                x.dado = val;
                return;
            }
        }
        push_front(key, val);
    }

    public Item pop_front(){
        No temp = Sentinela.prox;
        Item meuDado = temp.dado;
        //Atualizar o nó anterior para o próximo do que será removido
        temp.ant.prox = temp.prox;
        //Atualizar o nó próximo para apontar para o anterior do que será removido
        temp.prox.ant = temp.ant;
        n--;
        return meuDado;
    }

    public Item pop_back(){
        No temp = Sentinela.ant;
        Item meuDado = temp.dado;
        //Atualizar o nó anterior para apontar para o próximo do que será removido
        temp.ant.prox = temp.prox;
        //Atualizar o nó próximo para apontar para o anterior do que será removido
        temp.prox.ant = temp.ant;
        n--;
        return meuDado;
    }

    public No first(){
        if(isEmpty())return null;
        return Sentinela.prox;
    }

    public boolean isEmpty(){
        return Sentinela == Sentinela.prox;
    }

    public int size(){
        return n;
    }

    public ListIterator<Item> iterator(){
        return new DequeIterator();
    }

    public class DequeIterator implements ListIterator<Item>{
        private No atual = Sentinela.prox;
        private int indice = 0;
        private No acessadoUltimo = null;

        public boolean hasNext(){
            return indice < n;
        }
        public boolean hasPrevious(){
            return indice > 0;
        }
        public int previousIndex(){
            return indice - 1;
        }
        public int nextIndex(){
            return indice;
        }

        public Item next(){
            if(!hasNext()) return null;

            Item meuDado = atual.dado;
            acessadoUltimo = atual;
            atual = atual.prox;
            indice++;
            return meuDado;
        }

        public Item previous(){
            if(!hasPrevious()) return null;
            atual = atual.ant;

            Item meuDado = atual.dado;
            acessadoUltimo = atual;
            indice--;
            return meuDado;
        }

        public Item get(){
            if(atual == null) throw new IllegalStateException();
            return atual.dado;
        }

        public void set(Item x){
            if(acessadoUltimo == null) throw new IllegalStateException();
            acessadoUltimo.dado = x;
        }

        public void remove(){
            if(acessadoUltimo == null) throw new IllegalStateException();
            acessadoUltimo.ant.prox = acessadoUltimo.prox;
            acessadoUltimo.prox.ant = acessadoUltimo.ant;
            --n;
            if(atual == acessadoUltimo){
                atual = Sentinela.prox;
            }else{
                indice--;
            }acessadoUltimo = null;
        }

        public void add(Item x){
            //Inserir após atual
            No temp = new No();
            temp.dado = x;

            temp.prox = atual.prox;
            temp.ant = atual;

            temp.prox.ant = temp;
            atual.prox = temp;
            n++;
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Item item : this){
            sb.append(item + " ");
        }
        return sb.toString();
    }

    public Iterable<Key> keys() {
        Deque<Key> queue = new Deque<Key>();
        for (No x = Sentinela.prox;x!=Sentinela;x=x.prox){
            queue.push_back(x.chave);
        }return queue;
    }

    public static void main(String[] args) {
        DequeSearch<String,Integer> st = new DequeSearch<String,Integer>();
        for(int i = 0; !StdIn.isEmpty(); i++){
            String key = StdIn.readString();
            StdOut.println(key + " " + i);
            st.put(key, i);
        }
        StdOut.println(st.keys().toString());
        StdOut.println("-------------------");
        StdOut.println(st.toString());
    }








}

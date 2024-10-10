package org.example;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Deque<Item> implements Iterable<Item> {
    private int n; //contador de elementos
    private No Sentinela; //Nó artificial para marcar início e fim

    public Deque() {
        n = 0;
        Sentinela = new No();
        Sentinela.prox = Sentinela;
        Sentinela.ant = Sentinela;
    }

    private class No {//Classe Nó
        private Item dado;
        private No prox;
        private No ant;
    }

    public void push_front(Item item) {
        //criar novo nó e armazenar dados
        No temp = new No();
        temp.dado = item;

        //definir anterior e próximo do novo nó
        temp.ant = Sentinela;
        temp.prox = Sentinela.prox;

        //ajustar a sentinela e o seguinte
        Sentinela.prox = temp;
        temp.prox.ant = temp;
        n++;
    }

    public void push_back(Item item) {
        //criar novo no e armazenar dados
        No temp = new No();
        temp.dado = item;

        //definir anterior e próximo do novo nó
        temp.ant = Sentinela.ant;
        temp.prox = Sentinela;

        //ajustar a sentinela e o anterior
        Sentinela.ant = temp;
        temp.ant.prox = temp;
        n++;
    }

    public Item pop_front() {
        No temp = Sentinela.prox;
        Item meuDado = temp.dado;

        //Atualizar o nó anterior para apontar para o próximo do que será removido
        temp.ant.prox = temp.prox;

        //Atualizar o nó próximo para apontar para o anterior do que será removido
        temp.prox.ant = temp.ant;
        n--;
        return meuDado;
    }

    public Item pop_back() {
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
        if(isEmpty()){
            return null;
        }
        return Sentinela.prox;
    }

    public boolean isEmpty(){
        if(Sentinela == Sentinela.prox){
            return true;
        }
        return false;
    }

    public int size(){
        return n;
    }

    public ListIterator<Item> iterator(){
        return new DequeIterator();
    }

    public class DequeIterator implements ListIterator<Item> {
        private No atual = Sentinela.prox;
        private int indice = 0;
        private No acessadoUltimo = null;

        public boolean hasNext() {
            return indice < n;
        }
        public boolean hasPrevious() {
            return indice > 0;
        }
        public int previousIndex() {
            return indice - 1;
        }
        public int nextIndex() {
            return indice;
        }

        public Item next() {
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
            n--;
            if(atual == acessadoUltimo){
                atual = acessadoUltimo.prox;
            }else{
                indice--;
            }
            acessadoUltimo = null;
        }

        public void add(Item item){
            //inserir após atual
            No temp = new No();
            temp.dado = item;

            temp.prox = atual.prox;
            temp.ant = atual;

            temp.prox.ant = temp;
            atual.prox = temp;
            n++;

        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Item item : this){
            sb.append(item + " ");
        }return sb.toString();
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        //adiciona elementos
        StdOut.println(n + "inteiros aleatórios entre 0 e 99");
        Deque<Integer> list = new Deque<Integer>();
        for (int i = 0; i < n; i++) {
            list.push_front(i);
        }
        StdOut.println(list);
        StdOut.println();
        while(!list.isEmpty()){
            StdOut.println(list.pop_front());
        }
        for (int i = 0; i < n; i++) {
            list.push_back(i);
        }
        StdOut.println(list);
        StdOut.println();

        ListIterator<Integer> it = list.iterator();
        while(it.hasNext()){
            int x = it.next();
            it.set(x + 1);
        }

        StdOut.println(list);
        StdOut.println();
        while(it.hasPrevious()){
            int x = it.previous();
            it.set(x + x + x);
        }
        StdOut.println(list);
        StdOut.println();
        while(it.hasNext()){
            int x = it.next();
            if(x%2 == 0) it.remove();
        }

        StdOut.println(list);
        StdOut.println();
        while(it.hasPrevious()){
            int x = it.previous();
            it.add(x + x);
        }
        StdOut.println(list);
        StdOut.println();
    }
}

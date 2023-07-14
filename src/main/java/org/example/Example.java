package org.example;

public class Example<T> {
    private T elem;

    public Example(T elem) {
        this.elem = elem;
    }

    public static void main(String[] args) {
        Example<Integer> example = new Example<>(10);
    }
}

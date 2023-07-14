package org.example.Exeption;

public class UnknownId extends RuntimeException{
    public UnknownId() {
        super("нет элемента с таким id");
    }

    public UnknownId(String message) {
        super(message);
    }
}



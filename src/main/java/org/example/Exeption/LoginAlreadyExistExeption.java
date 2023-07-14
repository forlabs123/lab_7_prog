package org.example.Exeption;

public class LoginAlreadyExistExeption extends RuntimeException{
    public LoginAlreadyExistExeption() {
        super("Такой логин уже существует");
    }

    public LoginAlreadyExistExeption(String message) {
        super(message);
    }
}

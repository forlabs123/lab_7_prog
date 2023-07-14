package org.example.Exeption;

public class AuthorizationExeption extends RuntimeException {
    public AuthorizationExeption(){
        super("Ошибка авторизации");
    }

    public AuthorizationExeption(String message) {
        super(message);
    }
}

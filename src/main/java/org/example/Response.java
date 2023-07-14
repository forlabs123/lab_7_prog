package org.example;

import java.io.Serializable;

public class Response implements Serializable {

    private String message;
    private Exception exception;
    private StatusRuquest statusRuquest;

    private String login;
    private int number;


    private int counter;
    public Response(String message) {
        this.message = message;
    }

    public Response() {
    }

    public String getMessage() {
        return message;
    }
    public String getLogin() {
        return login;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public StatusRuquest getStatus() {
        return statusRuquest;
    }

    public void setStatus(StatusRuquest statusRuquest) {
        this.statusRuquest = statusRuquest;
    }
}

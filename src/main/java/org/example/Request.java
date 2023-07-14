package org.example;

import java.io.Serializable;

public class Request implements Serializable {
    private String command;
    private String args;
    private String login;
    private String password;
    private Serializable obj;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Request(String command, String args) {
        this.command = command;
        this.args = args;
    }

    public String getCommand() {
        return command;
    }

    public String getArgs() {
        return args;
    }

    public Serializable getObj() {
        return obj;
    }

    public void setObj(Serializable obj) {
        this.obj = obj;
    }
}

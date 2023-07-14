package org.example.ServerMoudle.commands;

import org.example.DataBase.Repository;
import org.example.Exeption.LoginAlreadyExistExeption;
import org.example.model.User;

public class Registretion extends Command {


    @Override
    public void execute(String[] args) {
        Repository repository = new Repository();
        String login = args[0];
        String password = args[1];
        if (repository.checkLogin(login)){
            repository.addUser(new User(0, login, password));
            ManagerResult.write("пользователь зарегестрирован ");
        } else throw new LoginAlreadyExistExeption();

    }
}

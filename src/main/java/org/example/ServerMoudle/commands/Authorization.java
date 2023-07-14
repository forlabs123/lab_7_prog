package org.example.ServerMoudle.commands;

import org.example.DataBase.Repository;
import org.example.Exeption.AuthorizationExeption;
import org.example.Exeption.LoginAlreadyExistExeption;
import org.example.model.User;

public class Authorization extends Command{


    @Override
    public void execute(String[] args) {
        Repository repository = new Repository();
        String login = args[0];
        String password = args[1];
        if (repository.findUser(login, password) == null){
            throw new AuthorizationExeption();
        } else{
            ManagerResult.write("пользователь авторизирован ");
        }
    }
}

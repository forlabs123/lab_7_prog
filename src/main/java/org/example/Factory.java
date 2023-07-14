package org.example;


import org.example.ClientMoudle.ExecuteScript;
import org.example.Exeption.UnknownCommandException;
import org.example.Exeption.WrongAmountOfElementsException;
import org.example.ServerMoudle.File.Collection;
import org.example.ServerMoudle.commands.*;
import org.example.model.Worker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class Factory {
    private final HashMap<String, Command> commandMap;
    private final HashMap<String, Integer> commandsWithArgs;


        public Factory(Collection collection){
        commandMap = new HashMap<>();
        commandMap.put("info", new Info(collection));
        commandMap.put("add", new Add(collection));
        commandMap.put("show", new Show(collection));
        commandMap.put("update", new UpdateId(collection));
        commandMap.put("remove_by_id", new RemoveById(collection));
        commandMap.put("clear", new Clear(collection));
        //commandMap.put("save", new Save(collection));
        commandMap.put("execute_script", new ExecuteScript(collection, this));
        commandMap.put("exit", new Exit());
        commandMap.put("remove_first", new RemoveFirst(collection));
        commandMap.put("remove_head", new RemoveHead(collection));
        commandMap.put("remove_greater", new RemoveGreater(collection));
        commandMap.put("remove_all_by_salary", new RemoveAllBySalary(collection));
        commandMap.put("print_descending", new PrintDescending(collection));
        commandMap.put("print_field_ascending_organization", new PrintFieldAscendingOrganization(collection));
        commandMap.put("help", new Help());
        commandMap.put("registration", new Registretion());
        commandMap.put("authorization", new Authorization());
        commandsWithArgs = new HashMap<>();
        commandsWithArgs.put("update",1);
        commandsWithArgs.put("remove_by_id",1);
        commandsWithArgs.put("remove_greater",1);
        commandsWithArgs.put("execute_script",1);
        commandsWithArgs.put("remove_all_by_salary",1);
        commandsWithArgs.put("save_as",1);
        commandsWithArgs.put("info",0);
        commandsWithArgs.put("add",0);
        commandsWithArgs.put("show",0);
        commandsWithArgs.put("clear",0);
        commandsWithArgs.put("exit",0);
        commandsWithArgs.put("remove_first",0);
        commandsWithArgs.put("remove_head",0);
        commandsWithArgs.put("print_greater",0);
        commandsWithArgs.put("print_descending",0);
        commandsWithArgs.put("help",0);
        commandsWithArgs.put("registration",2);
        commandsWithArgs.put("authorization",2);
        }
        public void save(){
            commandMap.get("save").execute(null);
        }

    public CommandResult create(String command1, String args, Serializable obj){
        String[] args1;
        if (args.isEmpty()){
            args1 = new String[0];
        } else{
            args1 = args.split(" ");
        }

        if (commandsWithArgs.containsKey(command1)){
            if (args1.length != commandsWithArgs.get(command1)) throw new WrongAmountOfElementsException();
        } else{ throw new UnknownCommandException();}
        Command command = commandMap.get(command1);
        if (command instanceof Add){
            ((Add) command).setClientWorker((Worker) obj);
        }
        //String[] a1 = Arrays.copyOfRange(a, 1, a.length);
        return new CommandResult(command, args1);
    }

        public CommandResult create(String command1,String args){
            return create(command1, args, null);
        }
    public static boolean checkCommand(String s) {
        if (s.equals("info") || s.equals("add") || s.equals("show") || s.equals("update") || s.equals("remove_by_id") || s.equals("clear") || s.equals("save") || s.equals("execute_script") || s.equals("exit") || s.equals("remove_first") || s.equals("remove_head") || s.equals("remove_greater") || s.equals("remove_all_by_salary") || s.equals("print_descending") || s.equals("print_field_ascending_organization")) {
            return true;
        }
        return false;
    }
}




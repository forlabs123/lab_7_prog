package org.example.ClientMoudle;

import org.example.CommandResult;
import org.example.Factory;
import org.example.ServerMoudle.File.Collection;
import org.example.ServerMoudle.commands.Add;
import org.example.ServerMoudle.commands.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * The 'execute_script' command.
 * Execute the script from the file.
 */
public class ExecuteScript extends Command {
    private Collection collection;
    private Factory factory;
    //private static ArrayList<String> filenames;
    private static ArrayDeque<String> filenames;
    public ExecuteScript(Collection collection, Factory factory) {
        this.factory = factory;
        this.collection = collection;
        filenames = new ArrayDeque<>();
    }
    /**
     * Execute a script
     */
    @Override
    public void execute(String[] args) {
       String filename = args[0];
       if (filenames.contains(filename)){
           filenames.clear();
           throw new IllegalArgumentException("бесконечный вызов файлов");
       }
       if (!new File(filename).exists()){
           filenames.clear();
           throw new IllegalArgumentException("нет такого файла");
       }
       if (! new File(filename).isFile()){
           filenames.clear();
           throw new IllegalArgumentException("это директория, а не файл");
       }
       filenames.add(filename);
       ArrayList<String> arrayListLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line=reader.readLine())!=null){
                if (!line.trim().isEmpty()) {
                    arrayListLines.add(line);
                }
            }
        } catch (Exception e) {
            filenames.clear();
            throw new RuntimeException(e);
        }
        try {
            for (int i = 0; i<arrayListLines.size(); i++){
                String arg = "";
                String commandLine = arrayListLines.get(i);
                if (commandLine.contains(" ")){
                    int pr = commandLine.indexOf(" ");
                    arg = commandLine.substring(pr + 1);
                    commandLine = commandLine.substring(0, pr);
                }
                CommandResult command = factory.create(commandLine, arg);
                if (command.getCommand() instanceof Add && command.getArgs().length == 0){
                   //if ( i == arrayListLines.size() -1 || Factory.checkCommand(arrayListLines.get(i+1))) throw new RuntimeException("в add не хватает данных");
                    //if (Factory.checkCommand(arrayListLines.get(i+1))) throw new RuntimeException("в add не хватает данных");
                    if (i < arrayListLines.size()-1 && !Factory.checkCommand(arrayListLines.get(i+1))){
                        String addWorker = "";
                        for (int j = 0; j<12; j++){
                            i++;
                            addWorker += arrayListLines.get(i) + ";";
                        }
                        String[] strings = {addWorker};
                        command.setArgs(strings);
                    }
                }
                command.getCommand().execute(command.getArgs());
            }
        }
        catch (Exception e) {
            filenames.clear();
            throw new RuntimeException(e);
        }
        filenames.pop();
    }
}

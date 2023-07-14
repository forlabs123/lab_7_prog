package org.example.ServerMoudle.commands;

import org.example.Exeption.WrongAmountOfElementsException;
import org.example.ServerMoudle.File.Collection;
import org.example.model.Worker;

import java.util.Comparator;
import java.util.List;

/**
 *  The 'PrintDescending' class is a command that prints the list of workers in the reverse order.
 */
public class PrintDescending extends Command{
    private Collection collection;

    public PrintDescending(Collection collection) {
        this.collection = collection;
    }

    /**
     *Print the list of organizations in reverse order
     */
    @Override
    public void execute(String[] args) {
        if (!(args.length <1)) throw new WrongAmountOfElementsException();
//        ArrayList<Worker> workerArrayList = new ArrayList<>(collection.getWorkers());
//        workerArrayList.sort(Collections.reverseOrder());
        List<Worker> workerArrayList = collection.getWorkers().stream().sorted(Comparator.comparing(Worker::getName)).toList();
        for(Worker worker : workerArrayList){
            ManagerResult.write(worker.toString());
        }
    }
}

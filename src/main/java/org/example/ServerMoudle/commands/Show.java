package org.example.ServerMoudle.commands;

import org.example.Exeption.EmptyCollectionException;
import org.example.ServerMoudle.File.Collection;
import org.example.model.Worker;

import java.util.Comparator;

/**
 * The `ShowCommand` class is a command that prints all the elements of the collection in string
 * representation
 */
public class Show extends Command{
    private Collection collection;
    public Show(Collection collection) {
        this.collection=collection;
    }

    /**
     * * Prints all the organizations in the collection
     * @param args the arguments passed to the command.
     */
    @Override
    public void execute(String[] args) {
        if (collection.countElements() == 0) throw new EmptyCollectionException();
        //if (collection.countElements() <= 2000) {
            for (Worker worker : collection.getWorkers().stream().sorted(Comparator.comparing(Worker::getName)).limit(100).toList()) {
                ManagerResult.write(worker.toString());
                //ManagerResult.write("операция успешно выполнена");
        }
    }
}

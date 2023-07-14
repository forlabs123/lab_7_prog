package org.example.ServerMoudle.commands;

import org.example.Exeption.WrongAmountOfElementsException;
import org.example.ServerMoudle.File.Collection;

/**
 * Clear the collection
 */
public class Clear extends Command{
    private Collection collection;

    public Clear(Collection collection) {
        this.collection = collection;
    }

    public Clear() {
    }

    /**
     * Clear the collection
     */
    @Override
    public void execute(String[] args){
        if (args.length != 0) throw new WrongAmountOfElementsException();
        collection.clear();
        ManagerResult.write("операция успешно выполнена");
    }
}

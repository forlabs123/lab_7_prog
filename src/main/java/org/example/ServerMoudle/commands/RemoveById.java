package org.example.ServerMoudle.commands;

import org.example.Exeption.UnknownId;
import org.example.Exeption.WrongAmountOfElementsException;
import org.example.ServerMoudle.File.Collection;
import org.example.model.Worker;

/**
 * The 'RemoveById' class is command that remove an organization from the collection
 */
public class RemoveById extends Command{
    private Collection collection;

    public RemoveById(Collection collection) {
        this.collection = collection;
    }
    /**
     * Remove an organization from the collection
     */
    @Override
    public void execute(String[] args) {
            if (args.length == 1) {
                int id = Integer.parseInt(args[0]);
                Worker worker = collection.getById(id);
                if (worker != null) {
                    collection.removeById(id);
                    ManagerResult.write("операция успешно выполнена");
                } else{
                    throw new UnknownId();
                }
            } else {
                throw new WrongAmountOfElementsException();
            }

    }
}

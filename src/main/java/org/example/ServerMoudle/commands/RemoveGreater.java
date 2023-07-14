package org.example.ServerMoudle.commands;

import org.example.Exeption.EmptyCollectionException;
import org.example.ServerMoudle.File.Collection;

/**
 * The 'RemoveGreater' class is a command that remove all elements greater that the person enter
 */
public class RemoveGreater extends Command{
    private Collection collection;
    public RemoveGreater(Collection collection){
        this.collection = collection;
    }

    /**
     * The command that remove all elemunt greater than person enter
     * @param args if args is null throw new exeption
     */
    @Override
    public void execute(String[] args) {
        if (collection.countElements() == 0) throw new EmptyCollectionException();
        float salary = Float.parseFloat(args[0]);
        collection.removeGreater(salary);
        ManagerResult.write("операция успешно выполнена");
    }
}

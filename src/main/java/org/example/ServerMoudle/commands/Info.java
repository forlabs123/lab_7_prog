package org.example.ServerMoudle.commands;

import org.example.Exeption.WrongAmountOfElementsException;
import org.example.ServerMoudle.File.Collection;

/**
 * The 'Info' class provides information about the collection.
 */
public class Info extends Command{
    Collection collection;

    public Info(Collection collection) {
        this.collection = collection;
    }

    /**
     *Print information about collection: type, initialization date, number of items
     */
    @Override
    public void execute(String[] args) {
        if (args.length > 0){
            throw new WrongAmountOfElementsException();
        }
        ManagerResult.write("Информация о коллекции: ");
        ManagerResult.write("Тип коллекции: " + collection.getType());
        ManagerResult.write("Количество элементов: " + collection.getWorkers().size());
        ManagerResult.write(""+ collection.getDateInit());
        if (collection.getDateSave() == null){
            ManagerResult.write("Коллекция не сохранена");
        } else {
            ManagerResult.write("Коллекция сохранена: " + collection.getDateSave());
        }
    }
}

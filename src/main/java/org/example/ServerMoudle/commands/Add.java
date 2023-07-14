package org.example.ServerMoudle.commands;

import org.example.Exeption.IdException;
import org.example.Exeption.WrongTypeException;
import org.example.ServerMoudle.File.Collection;
import org.example.ServerMoudle.RequestThread;
import org.example.Util.HelperUtil;
import org.example.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The class is responsible for adding an organization to the collection
 */
public class Add extends Command{
    private Collection collection;
    private Worker clientWorker;
    public Add(Collection collection) {
        this.collection = collection;
    }

    public Add() {
    }

    public void setClientWorker(Worker clientWorker) {
        this.clientWorker = clientWorker;
    }

    /**
     * The function adds an organization to the collection
     */
    @Override
    public void execute(String[] args) {
        if (clientWorker != null) {
            clientWorker.setId(collection.maxId() + 1);
            collection.add(clientWorker);
        } else {
            String[] params = args[0].split(";");
            //long id = Long.parseLong(params[0]);
            long id = collection.maxId() + 1;
            if (id <= 0) throw new WrongTypeException("id должен быть положительным");
            if (collection.getById(id) != null) {
                throw new IdException();
            }
            //if (params[1].isEmpty() || params[1].equals("null")) throw new WrongTypeException();
            String name = params[0];
            float x = Float.parseFloat(params[1]);
            long y = Long.parseLong(params[2]);
            Coordinates coordinates = new Coordinates(x, y);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(params[3], formatter);
            Float salary = Float.parseFloat(params[4]);
            LocalDate localDate2 = LocalDate.parse(params[5], formatter);
            Position position = createPosition(params[6]);
            Status status = createStatus(params[7]);
            String nameOrg = params[8];
            if (params[8].isEmpty() || params[8].equals("null"))
                throw new WrongTypeException("организация не может быть null");
            long emploeesCount = Long.parseLong(params[9]);
            OrganizationType status1;
            if (HelperUtil.checkInteger(params[10])) {
                int number1 = Integer.parseInt(params[10]);
                status1 = OrganizationType.values()[number1];
            } else {
                status1 = OrganizationType.valueOf(params[10]);
            }
            String address = params[11];
            Address adr = new Address(address);
            HelperUtil.checkWorkers(id, name, coordinates, localDate, salary, localDate2, position, status, nameOrg, emploeesCount, status1, address);
            Organization organisation = new Organization(nameOrg, emploeesCount, status1, adr);
            Worker worker = new Worker(id, name, coordinates, localDate, salary, localDate2, position, status, organisation);
            RequestThread requestThread = (RequestThread) Thread.currentThread();
            worker.setCreator(requestThread.getLogin());
            collection.add(worker);
        }
        ManagerResult.write("операция успешно выполнена");
    }

    private Position createPosition(String s){
        Position position;
        if (s.equals("null")){
            System.err.println("значение не может быть null");
        }
        if (HelperUtil.checkInteger(s)){
            int number = Integer.parseInt(s);
            position = Position.values()[number];
        } else {
            position = Position.valueOf(s);
        }
        return position;
    }
    private Status createStatus(String s){
        Status status;
        if (HelperUtil.checkInteger(s)){
            int number1 = Integer.parseInt(s);
            status = Status.values()[number1];
        } else {
            status = Status.valueOf(s);
        }
        return status;
    }

}

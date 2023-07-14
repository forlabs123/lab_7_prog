package org.example.ClientMoudle;

import org.example.Util.HelperUtil;
import org.example.model.*;

import java.time.LocalDate;
import java.util.Scanner;

public class WorkerCreator {

    public Worker createWorker() {
            Scanner scanner = new Scanner(System.in);
            //long id = collection.maxId() + 1;
            System.out.println("Введите имя работника ");
            String name = HelperUtil.inputString();
            Coordinates coordinates = createCoordinates();
            //System.out.println("Введите время dd-MM-yyyy");
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            //String date = scanner.next();
            LocalDate localDate = LocalDate.now();
            System.out.println("Введите зарплату работника        (зарплата больше 0, хотя бы МРОТ в теории): ");
            Float salary = HelperUtil.inputFloatSalary();
            System.out.println("Введите время dd-MM-yyyy");
            LocalDate localDate2 = HelperUtil.inputData();
            System.out.println("Выберите должность 0-DIRECTOR 1-HEAD_OF_DIVISION 2-BAKER 3-COOK 4-CLEANER");
            String number = scanner.next();
            Position position = createPosition(number);
            System.out.println("Выберите статус 0-HIRED 1-RECOMMENDED_FOR_PROMOTION 2-REGULAR 3-PROBATION");
            String numberStatus = scanner.next();
            Status status = createStatus(numberStatus);
            Organization organisation = createOrganisation();
            Worker worker = new Worker(0L, name, coordinates, localDate, salary, localDate2, position, status, organisation);
            return worker;
    }
    private Organization createOrganisation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите полное имя ");
        String fullName = scanner.nextLine();
        System.out.println("Введите количество сотрудников ");
        long emploeesCount = HelperUtil.inputLong();
        System.out.println("Выберите статус 0-PUBLIC 1-GOVERNMENT 2-PRIVATE_LIMITED_COMPANY 3-OPEN_JOINT_STOCK_COMPANY");
        int numberStatus = HelperUtil.inputInt();
        OrganizationType status = OrganizationType.values()[numberStatus];
        Address address = createAddres();
        return new Organization(fullName, emploeesCount, status, address);
    }
    private Address createAddres(){
        System.out.println("Введите адрес ");
        Scanner scanner = new Scanner(System.in);
        String address = scanner.next();
        return new Address(address);
    }

    private Coordinates createCoordinates(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите x, y");
        float x = HelperUtil.inputFloat();
        long y = HelperUtil.inputLong();
        return new Coordinates(x,y);
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

package org.example.ServerMoudle.File;

import org.example.DataBase.Repository;
import org.example.Request;
import org.example.ServerMoudle.RequestThread;
import org.example.model.Organization;
import org.example.model.Worker;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * The CollectionManager class manages the collection of all the collections
 * in the application
 */
public class Collection {
    private ArrayDeque<Worker> workers;
    private LocalDateTime dateSave;
   // private FileManager fileManager;
    private Repository dataBase;

    private LocalDateTime dateInit;
    private ReentrantLock reentrantLock;

    public Collection(){
        dateInit = LocalDateTime.now();
        dataBase = new Repository();
        reentrantLock = new ReentrantLock();
        workers = new ArrayDeque<>(dataBase.getWorkers());
            }
    public LocalDateTime getDateSave() {
        return dateSave;
    }
    /**
     * Get the creation date of the object
     * @return The creation date of the collection.
 */
    public LocalDateTime getDateInit() {
        return dateInit;
    }
    /**
     * This function returns the collection of workers
     * @return The ArrayDeque of Workers objects.
     */
    public ArrayDeque<Worker> getWorkers(){
        return workers;
    }

    public void add(Worker worker){
        dateSave = LocalDateTime.now();
        reentrantLock.lock();
        dataBase.addWorker(worker);
        workers.add(worker);
        reentrantLock.unlock();
    }
    public String getType(){
        return workers.getClass().getName();
    }
    public void clear(){
        ArrayList<Long> arrayList = new ArrayList<>();
        reentrantLock.lock();
        for (Worker worker : workers){
            RequestThread requestThread = (RequestThread) Thread.currentThread();
            if (worker.getCreator().equals(requestThread.getLogin())){
                dataBase.deleteWorker(worker.getId());
                arrayList.add(worker.getId());
            }
        }
        workers.removeIf(worker -> arrayList.contains(worker.getId()));
        reentrantLock.unlock();
    }
    public Worker getById(long id){
        for (Worker worker : workers){
            if (worker.getId() == id){
                return worker;
            }
        }
        return null;
    }
    public Worker getBySalary(float salary){
        for (Worker worker : workers){
            if (worker.getSalary() == salary){
                return worker;
            }
        }
        return null;
    }
    public void removeById(long id) {
        Worker worker = getById(id);
        RequestThread requestThread = (RequestThread) Thread.currentThread();
        if (worker != null && worker.getCreator().equals(requestThread.getLogin())){
            reentrantLock.lock();
            dataBase.deleteWorker(id);
            workers = workers.stream().filter(t -> t.getId() != id).collect(Collectors.toCollection(ArrayDeque::new));
            reentrantLock.unlock();
        }
    }
    public Long maxId(){
        if (workers.isEmpty()){
            return 0L;
        }
        return workers.stream().max(Comparator.comparing(Worker::getId)).get().getId();
    }
    public int countElements(){
        return workers.size();
    }
    public void removeGreater(float salary){
        ArrayList<Long> arrayList = new ArrayList<>();
        reentrantLock.lock();
        for (Worker worker : workers){
            RequestThread requestThread = (RequestThread) Thread.currentThread();
            if (worker.getCreator().equals(requestThread.getLogin()) && worker.getSalary() > salary){
                dataBase.deleteWorker(worker.getId());
                arrayList.add(worker.getId());
            }
        }
        workers.removeIf(worker -> arrayList.contains(worker.getId()));
        reentrantLock.unlock();
    }
    public void removeFirst(){
        if (workers.size() > 0) {
            Worker worker = workers.getFirst();
            RequestThread requestThread = (RequestThread) Thread.currentThread();
            if (worker.getCreator().equals(requestThread.getLogin())) {
                reentrantLock.lock();
                dataBase.deleteWorker(worker.getId());
                workers.removeFirst();
                reentrantLock.unlock();
            } else throw new IllegalArgumentException("вы не создатель  первого worker");
        }
    }
    public void showFirst() {
        reentrantLock.lock();
        System.out.println(workers.peek());
        reentrantLock.unlock();
    }
    public ArrayList<Organization> getOrganisation(){
        ArrayList<Organization> organizations = new ArrayList<>();
        for (Worker worker : workers){
            if (!organizations.contains(worker.getOrganization())){
                organizations.add(worker.getOrganization());
            }
        }
        return organizations;
    }
    public void removeAllBySalary(float salary) {
        ArrayList<Long> arrayList = new ArrayList<>();
        reentrantLock.lock();
        for (Worker worker : workers) {
            RequestThread requestThread = (RequestThread) Thread.currentThread();
            if (worker.getCreator().equals(requestThread.getLogin()) && worker.getSalary() == salary) {
                dataBase.deleteWorker(worker.getId());
                arrayList.add(worker.getId());
            }
        }
        workers.removeIf(worker -> arrayList.contains(worker.getId()));
        reentrantLock.unlock();
    }
    public void update(long id, Worker worker){
        RequestThread requestThread = (RequestThread) Thread.currentThread();
        if (worker.getCreator().equals(requestThread.getLogin())) {
            reentrantLock.lock();
            dataBase.updateWorker(worker);
            workers = workers.stream().filter(t -> t.getId() != id).collect(Collectors.toCollection(ArrayDeque::new));
            workers.add(worker);
            reentrantLock.unlock();
        }
    }
}

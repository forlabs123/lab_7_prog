package org.example.ServerMoudle.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.Exeption.IdException;
import org.example.ServerMoudle.DataAdapter.DateAdapter;
import org.example.model.Worker;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.HashSet;

public class FileManager {
    private String fileName;

    public FileManager(String fileName) {
        this.fileName = fileName;
    }

    public ArrayDeque<Worker> readFile(){
        ArrayDeque<Worker> workers = new ArrayDeque<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                if (!bufferedReader.ready()){
            } else {
                while ((line = bufferedReader.readLine()) != null) {
                 stringBuilder.append(line);
                }
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new DateAdapter()).create();
                var collectionType = new TypeToken<ArrayDeque<Worker>>() {
                }.getType();
                workers = gson.fromJson(stringBuilder.toString(), collectionType);}
            }  catch (Exception e) {
            throw new RuntimeException(e);
            }
        if (!checkWorkeId(workers)){
            workers.clear();
            throw new IdException();
        }
        boolean check = true;
        for (Worker worker : workers){
            worker.checkWorker();
        }

        return workers;
    }

    private boolean checkWorkeId(ArrayDeque<Worker> workers){
        HashSet<Long> set = new HashSet<>();
        for (Worker worker : workers){
            if (set.contains(worker.getId())){
                return false;
            }
            set.add(worker.getId());
        }
        return true;
    }
    public void save(ArrayDeque<Worker> arrayDeque) {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new DateAdapter()).setPrettyPrinting().create();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter( fileName))) {
            gson.toJson(arrayDeque, bufferedWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

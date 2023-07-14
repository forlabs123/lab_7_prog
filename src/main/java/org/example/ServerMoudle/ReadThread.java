package org.example.ServerMoudle;

import org.example.CommandResult;
import org.example.Factory;

import java.util.Scanner;

public class ReadThread extends Thread {

    private Factory factory;

    public ReadThread(Factory factory) {
        this.factory = factory;
    }


    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.next();
            if (command.equals("exit"))
                break;

            if (command.equals("save")) {
               CommandResult commandResult = factory.create("save", "");
               commandResult.getCommand().execute(null);
            }
        }
    }
}

package org.example.ServerMoudle;

import org.example.*;
import org.example.ClientMoudle.ExecuteScript;
import org.example.ServerMoudle.commands.ManagerResult;
import org.example.Util.HelperUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private int port1;
    private int port2;
    private Factory factory;

    private Scanner scanner;
    private UdpServer serverSocket;

    public Server(int port1, int port2, Factory factory) {
        this.port1 = port1;
        this.port2 = port2;
        this.factory = factory;
        scanner = new Scanner(System.in);
    }

    public void start() {
        try {
            //serverSocket = new ServerSocket(port);
            serverSocket = new UdpServer(port1, port2);

            ReadThread thread = new ReadThread(factory);
            thread.start();

            connectClient();
            //               }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean exitFromConsole() {
        if (!scanner.hasNext()) {
            return false;
        }
        String command = scanner.nextLine().toLowerCase(Locale.ROOT);
        if ("save".equals(command)) {
            factory.save();
            return false;
        }
        if ("exit".equals(command)) {
            return true;
        }
        System.out.println("unknown command");
        return false;
    }

    public void connectClient() {
        while (true) {
            ManagerResult.clear();

            byte[] bytes = serverSocket.listenAndGetData();

            if (bytes == null) {
                continue;
            }
            RequestThread requestThread = new RequestThread(serverSocket, factory, bytes);
            requestThread.start();
        }
    }
}


package org.example.ClientMoudle;

import org.example.Request;
import org.example.Response;
import org.example.StatusRuquest;
import org.example.Util.HelperUtil;
import org.example.model.Worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Scanner;

public class Client {
    private final int maxCountAttempt = 10;
    private String host;
    private int port1;
    private int port2;
    private SocketChannel socketChannel;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private DatagramChannel clientChanel;
    private UdpClient client;
    private DatagramSocket client1;
    public Client(String host, int port1, int port2) {
        this.host = host;
        this.port1 = port1;
        this.port2 = port2;
    }

    public boolean connect() {
        int countAttempt = 0;
        while (countAttempt < maxCountAttempt) {
            try {
                client = new UdpClient(port1, port2);
                return true;
            } catch (IOException e) {
                System.err.println("не получилось подключится к серверу, новая попытка " + countAttempt);
                countAttempt++;
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                   // throw new RuntimeException(ex);
                    System.err.println(ex.getMessage());
                }
            }
        }
        return false;
    }

    public void send() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("вводите команды");
        boolean workingProsess = true;
        String login = null;
        String password = null;
        while (workingProsess) {
            if (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                String args = "";
                if (command.contains(" ")) {
                    int pr = command.indexOf(" ");
                    args = command.substring(pr + 1);
                    command = command.substring(0, pr);
                }
                Request request = new Request(command, args);
                request.setLogin(login);
                request.setPassword(password);
                if (command.equals("add")) {
                    WorkerCreator workerCreator = new WorkerCreator();
                    Worker worker = workerCreator.createWorker();
                    worker.setCreator(login);
                    request.setObj(worker);
                }
                byte[] bytes = HelperUtil.toByteArray(request);
                client.sendData(bytes);
                byte[] answerBytes = client.listenAndGetData();
                Response response = (Response) HelperUtil.toObj(answerBytes);
                if (command.equals("authorization") && response.getStatus() == StatusRuquest.OK){
                    String[] args1 = args.split(" ");
                    login = args1[0];
                    password = args1[1];
                }
                if (response.getStatus() == StatusRuquest.EXIT){break;}
                if (response.getException() != null) {
                    System.err.println(response.getException().getMessage());
                } else {
                    if (!command.equals("authorization") && !command.equals("registration") && login != null && !login.equals(response.getLogin())){
                        continue;
                    }
                    System.out.println(response.getMessage());
                }
            } else {
                workingProsess = false;
                System.err.println("не вводите ctrl + D    :-(   ");
            }
        }
    }

}

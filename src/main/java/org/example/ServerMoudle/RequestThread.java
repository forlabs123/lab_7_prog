package org.example.ServerMoudle;

import org.example.*;
import org.example.ServerMoudle.commands.ManagerResult;
import org.example.Util.HelperUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestThread extends Thread {
    private UdpServer serverSocket;
    private ExecutorService executorService;
    private byte[] bytes;
    private String userLogin;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private Factory factory;

    public RequestThread(UdpServer serverSocket, Factory factory, byte[] bytes) {
        this.serverSocket = serverSocket;
        this.factory = factory;
        this.bytes = bytes;
        executorService = Executors.newFixedThreadPool(1000);
    }

    public String getLogin() {
        return userLogin;
    }

    @Override
    public void run() {
        Request request = (Request) HelperUtil.toObj(bytes);
        LOGGER.log(Level.INFO, "поступил запрос ({0},{1})", new Object[]{request.getCommand(), request.getArgs()});
        userLogin = request.getLogin();
        Response response = new Response();
        response.setLogin(userLogin);
        if ((request.getLogin() == null || request.getLogin().isEmpty()) && !request.getCommand().equals("authorization") && !request.getCommand().equals("registration")) {
            response.setStatus(StatusRuquest.ERROR);
            response.setMessage("вы не авторизированы");
        } else {
            try {
                CommandResult command = factory.create(request.getCommand(), request.getArgs(), request.getObj());
                command.getCommand().execute(command.getArgs());
                if (command.getCommand().getFlag() == false) {
                    response.setStatus(StatusRuquest.EXIT);
                } else {
                    response.setStatus(StatusRuquest.OK);
                }
                LOGGER.log(Level.INFO, "ответ отправлен клиенту ");
                response.setMessage(ManagerResult.getResult());
            } catch (Exception e) {
                response.setException(e);
                response.setStatus(StatusRuquest.ERROR);
                LOGGER.log(Level.SEVERE, "произошла ошибка - {0}", e.getMessage());
            }
        }
        byte[] bytesArray = HelperUtil.toByteArray(response);
        executorService.submit(() -> serverSocket.sendData(bytesArray));
        LOGGER.log(Level.INFO, "ответ - {0}", response.getMessage());
    }
}

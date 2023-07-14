package org.example.ServerMoudle;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpServer {
    private final int BUFFER_SIZE = 65507;
//    private final DatagramSocket ds;
    private int port1;
    private int port2;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public UdpServer(int port1, int port2) throws SocketException {
        this.port1 = port1;
        this.port2 = port2;
    }

    public byte[] listenAndGetData() {
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            DatagramSocket ds= new DatagramSocket(port1);
            DatagramPacket dp;
            dp = new DatagramPacket(buffer, buffer.length);
            //ds.setSoTimeout(1000);
            ds.receive(dp);
            ds.close();
            return buffer;
        } catch (IOException e) {
        }
        return null;
    }

    public void sendData(byte[] data) {
        try {
            LOGGER.log(Level.INFO, "ответ - отправка данных клиенту" );
            DatagramSocket ds = new DatagramSocket();
            ds.setBroadcast(true);
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            DatagramPacket dpToSend = new DatagramPacket(data, data.length, inetAddress, port2);
            ds.send(dpToSend);
            ds.close();
            LOGGER.log(Level.INFO,"ответ отправлен");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE,ex.getMessage());
        }
    }

}

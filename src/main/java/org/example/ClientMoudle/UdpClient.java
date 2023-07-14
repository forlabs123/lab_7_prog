package org.example.ClientMoudle;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpClient {
    private final int BUFFER_SIZE = 65507;
//    private final DatagramSocket ds;
    private int  port1;
    private int port2;
    private byte[] buffer;
    int countAttempt = 0;

    public UdpClient(int port1, int port2) throws SocketException {
        this.port1 = port1;
        this.port2 = port2;
        buffer = new byte[BUFFER_SIZE];
    }

    public byte[] listenAndGetData() {
        //byte[] buffer = new byte[BUFFER_SIZE];
        while (countAttempt < 11) {
            try {
                DatagramSocket ds = new DatagramSocket(port2);
                DatagramPacket dp;
                dp = new DatagramPacket(buffer, buffer.length);
                ds.setSoTimeout(0);
                ds.receive(dp);
                ds.close();
                return buffer;
            } catch (IOException e) {
                System.err.println("не получилось подключится к серверу, новая попытка " + countAttempt);
                countAttempt++;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return null;
    }

    public void sendData(byte[] data) {
        countAttempt =0;
        while (true){
            try {
                DatagramSocket ds = new DatagramSocket(port2);
                ds.close();
                break;
            } catch (SocketException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {

                }
            }
        }
        while (countAttempt < 11) {
            try {
                DatagramSocket ds = new DatagramSocket();
                InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
                DatagramPacket dpToSend = new DatagramPacket(data, data.length, inetAddress, port1);
                ds.setSoTimeout(2500);
                ds.send(dpToSend);
                ds.close();
                break;
            } catch (IOException ex) {
                System.err.println("не получилось подключится к серверу, новая попытка " + countAttempt);
                countAttempt++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
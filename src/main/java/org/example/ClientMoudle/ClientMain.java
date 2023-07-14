package org.example.ClientMoudle;

public class ClientMain {
    public static void main (String[] args) throws InterruptedException {
        if (args.length < 3){
            System.err.println("недостаточно аргументов");
            System.exit(2);
        }
        Client client = new Client(args[0], Integer.parseInt(args[1]),Integer.parseInt(args[2]));
        if (client.connect() == true) {
            try {
                client.send();
            } catch (NullPointerException e){
                e.getMessage();
            }
        } else{
            System.err.println("не удалось связаться с сервером   :-( ");
        }
    }

}

package com.file.transfer.demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        ClassLoader classLoader = Client.class.getClassLoader();
        try (Scanner scn = new Scanner(System.in);) {
            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 4000
            Socket socket = new Socket("127.0.0.1", 4000);
            System.out.println("Connected to Server...");

            try (DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                 DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {

                while (true) {
                    try {
                        //get command from user
                        String command = scn.nextLine();
                        output.writeUTF(command);
                        System.out.println("Command received by Client " + command);

                        String[] commands = command.split(" ");

                        if (commands[0].equals("quit")) {
                            System.out.println("Closing this connection.");
                            socket.close();
                            break;
                        }

                        String fileName = commands[1];
                        switch (commands[0]) {
                            case "get":
                                FileTransferUtil.writeToFile(fileName, inputStream);
                                System.out.println("File " + fileName + " downloaded");
                                break;

                            case "put":
                                FileTransferUtil.readFile(fileName, output);
                                System.out.println("File " + fileName + " uploaded");
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

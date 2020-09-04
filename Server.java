package com.file.transfer.demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Server class
public class Server {
    public static void main(String[] args) throws IOException {

        // server is listening on port 4000
        ServerSocket socket = new ServerSocket(4000);
        System.out.println("Waiting...");

        // running infinite loop for getting client request
        while (true) {
            try {
                // socket object to receive incoming client requests
                Socket serviceSocket = socket.accept();

                System.out.println("A new client is connected : " + serviceSocket);
                System.out.println("Forking a new thread for this client");

                // When the server gets a client, forks and, let the child process take care of the client in a
                // separate function,
                // create a new thread object
                Thread serviceThread = new ServiceClient(serviceSocket);
                //Invoking Service Client process
                serviceThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

// ServiceClient class
class ServiceClient extends Thread {

    final Socket socket;

    // Constructor
    public ServiceClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        final ClassLoader classLoader = ServiceClient.class.getClassLoader();
        try ( // obtaining input and out streams
              DataInputStream inputStream = new DataInputStream(
                      socket.getInputStream()); DataOutputStream outputStream = new DataOutputStream(
                socket.getOutputStream())) {
            while (true) {
                try {
                    //Get command from client
                    String command = null;
                    try {
                        command = inputStream.readUTF();
                    } catch (EOFException ex) {
                        //Ignore exception
                    }

                    System.out.println("Command received by Server: " + command);

                    if (command != null) {
                        String[] commands = command.split(" ");

                        if (commands[0].equals("quit")) {
                            System.out.println("Closing this connection.");
                            socket.close();
                            break;
                        }

                        //Get File Name
                        String fileName = commands[1];
                        switch (commands[0]) {
                            case "get":

                                FileTransferUtil.readFile(fileName, outputStream);
                                break;

                            case "put":
                                FileTransferUtil.writeToFile(fileName, inputStream);
                                break;

                            default:
                                System.out.println("Incorrect command");
                                break;
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


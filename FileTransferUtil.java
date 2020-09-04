package com.file.transfer.demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTransferUtil {

    public static void readFile(String fileName, DataOutputStream dos) {
        File myFile = new File(fileName);
        if (myFile.canRead()) {
            try (FileInputStream fis = new FileInputStream(myFile); BufferedInputStream bis = new BufferedInputStream(
                    fis)) {
                byte[] bytes = new byte[(int) myFile.length()];
                bis.read(bytes, 0, bytes.length);
                System.out.println("Sending " + fileName + "(" + bytes.length + " bytes)");
                dos.write(bytes, 0, bytes.length);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File  " + fileName + " doesn't exist");
        }
    }

    public static void writeToFile(String fileName, DataInputStream inputStream) {
        File myFile = new File(fileName);
        if (!myFile.exists()) {
            try {
                myFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        byte[] bytes = new byte[1024];

        try (FileOutputStream fos = new FileOutputStream(myFile); BufferedOutputStream bos = new BufferedOutputStream(
                fos)) {
            int bytesRead = inputStream.read(bytes, 0, bytes.length);

            bos.write(bytes, 0, bytesRead);
            bos.flush();
            System.out.println("File " + fileName + " downloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

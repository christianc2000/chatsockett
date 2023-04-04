/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatsocket;

/**
 *
 * @author Christian
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Scanner scanner;

    public void connect(String serverAddress, int serverPort) throws IOException {
        // Establecer conexión con el servidor
        socket = new Socket(serverAddress, serverPort);
        System.out.println("Conectado al servidor en " + socket.getInetAddress() + ":" + socket.getPort());

        // Configurar el flujo de entrada y salida
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

        // Inicializar el scanner para leer la entrada del usuario
        scanner = new Scanner(System.in);

        // Iniciar un hilo para recibir mensajes del servidor
        Thread receivingThread = new Thread(new ReceivingHandler());
        receivingThread.start();

        // Enviar mensajes al servidor
        while (true) {
            String message = scanner.nextLine();
            writer.println(message);
        }
    }

    private class ReceivingHandler implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.err.println("Error al recibir mensaje del servidor: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 8080;

        Client client = new Client();

        try {
            client.connect(serverAddress, serverPort);
        } catch (IOException e) {
            System.err.println("Error al conectarse al servidor: " + e.getMessage());
        }
    }
}


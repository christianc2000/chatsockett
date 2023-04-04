/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatsocket;

/**
 *
 * @author Christian
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<ClientHandler> clients = new ArrayList<>();
    private ServerSocket serverSocket;
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Servidor escuchando en el puerto " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Nuevo cliente conectado desde " + clientSocket.getInetAddress());

            // Pedir un nombre de usuario al cliente
            ClientHandler clientHandler = new ClientHandler(clientSocket, this);
            clientHandler.send("Por favor, ingrese un nombre de usuario:");
            clientHandler.setClientName(clientHandler.receive());

            clients.add(clientHandler);
            Thread thread = new Thread(clientHandler);
            thread.start();
        }
    }

    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            // Envía el mensaje a todos los clientes excepto al emisor
            if (client != sender) {
                client.send(sender.getClientName() + ": " + message);
            }
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        Server server = new Server(port);

        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}


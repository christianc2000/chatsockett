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

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Server server;
    private String clientName;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error al inicializar los flujos de entrada/salida: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = receive()) != null) {
                server.broadcast(message, this);
            }
        } catch (IOException e) {
            System.err.println("Error al recibir mensaje del cliente: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Cliente desconectado: " + clientName);
            } catch (IOException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    public void send(String message) {
        writer.println(message);
    }

    public String receive() throws IOException {
        return reader.readLine();
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}


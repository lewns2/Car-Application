package com.nexenio.bleindoorpositioningdemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection implements Runnable{
    private String lineFromServer;
    private Socket clientSocket;
    private HomeActivity homeActivity;
    private String host;
    private int port;
    private BufferedReader fromServer;
    private PrintWriter toServer;
    private boolean disconnected = false;

    public ServerConnection(HomeActivity homeActivity, String host, int port) {
        this.homeActivity = homeActivity;
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try {
            homeActivity.log("Connecting to host: " + host);
            clientSocket = new Socket("192.168.1.48", 10223);
            homeActivity.log("Connected.");
            fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toServer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void disconnectToServer() {
        disconnected = true;
        homeActivity.disconnected();
    }

    void sendToServer(String msg) {
        toServer.println(msg);
    }

    @Override
    public void run() {
        try{
            while(!disconnected) {
                lineFromServer = fromServer.readLine();
                homeActivity.log(lineFromServer);
            }
            toServer.close();
            fromServer.close();
            clientSocket.close();
        } catch(IOException e) {
            e.printStackTrace();
            disconnected = true;
        }
    }
}

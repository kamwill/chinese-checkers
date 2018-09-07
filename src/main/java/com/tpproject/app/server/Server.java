package com.tpproject.app.server;

import com.tpproject.app.game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;


    private static ArrayList<clientThread> players = new ArrayList<clientThread>();
    private static ArrayList<Game> games = new ArrayList<Game>();

    public static void main(String args[]) {

        // The default port number.
        int portNumber = 2222;
        System.out.println("Usage: java MultiThreadChatServerSync <portNumber>\n"
                + "Now using port number=" + portNumber);


        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }


        while (true) {

            try {
                clientSocket = serverSocket.accept();


                clientThread player = new clientThread(clientSocket, players, games);
                player.start();
                players.add(player);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }
}


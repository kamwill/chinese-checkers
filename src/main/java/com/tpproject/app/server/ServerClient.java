package com.tpproject.app.server;

import com.tpproject.app.clientApp.observer.Observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ServerClient implements Runnable {

    // The client socket
    private static Socket clientSocket = null;
    // The output stream
    private static PrintStream os = null;
    // The input stream
    private static BufferedReader is = null;

    private static BufferedReader inputLine = null;
    private static boolean closed = false;

    static ServerClient serverClient;

    private static Observer observer;

    private static int numberOfPlayers;

    private static final AtomicReference<String> response = new AtomicReference<>();

    public static void setObserver(Observer observer) {
        ServerClient.observer = observer;
    }

    protected static Observer getObserver(){
        return observer;
    }

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        ServerClient.numberOfPlayers = numberOfPlayers;
    }

    public String getResponseLine(){
        return String.valueOf(response);
    }

    public static ServerClient getServerClient(){

        if(serverClient!=null){
            return serverClient;
        }
        else {
            serverClient = new ServerClient();

            return serverClient;
        }


    }




    public void start(){
        // The default port.
        int portNumber = 2222;
        // The default host.
        String host = "localhost";


        System.out.println("Usage: java MultiThreadChatClient <host> <portNumber>\n"
                + "Now using host=" + host + ", portNumber=" + portNumber);


        /*
         * Open a socket on a given host and port. Open input and output streams.
         */
        try {
            clientSocket = new Socket(host, portNumber);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to the host "
                    + host);
        }

        /*
         * If everything has been initialized then we want to write some data to the
         * socket we have opened a connection to on the port portNumber.
         */
        if (clientSocket != null && os != null && is != null) {
            //try {

                /* Create a thread to read from the server. */
                new Thread(new ServerClient()).start();
                /*while (!closed) {
                    os.println(inputLine.readLine().trim());
                }
                /*
                 * Close the output stream, close the input stream, close the socket.
                 */
               /* os.close();
                is.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            } */
        }

    }

    public void executeRQ(String type, List<Integer> args){
        String preparedRQ = this.prepareRQ(type, args);
        this.sendRQ(preparedRQ);
    }


    public void sendRQ(String request){
        System.out.println(request);
        os.print(request);
    }

    public String prepareRQ(String type, List<Integer> args){
        String request;
        request = type + " ";
        for(Integer i: args){
            request += i + " ";
        }
        request = request+"\n";
        return request;
    }




    public void run() {
        /*
         * Keep on reading from the socket till we receive "Bye" from the
         * server. Once we received that then we want to break.
         */

        try {
            String responseLine;
            synchronized (this) {

                while ((responseLine = is.readLine()) != null) {

                    System.out.println(responseLine);
                    response.set(responseLine);
                    notifyObserver(responseLine);

                    if (responseLine.contains("*** Bye"))
                        break;
                }
            }

            closed = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }


    }

    static void notifyObserver(String response){
        if(observer!=null){
            observer.update(response);
            System.out.println("Notified");
        }else{
            System.out.println("No observer");
        }
    }
}
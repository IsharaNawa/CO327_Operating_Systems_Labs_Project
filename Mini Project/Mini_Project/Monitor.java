//E/17/219
//Nawarathna K.G.I.S.

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class Monitor implements Serializable, Runnable {

    //private fields to store socket info and the monitor id
    private final InetAddress ip;
    private final String monitorID;
    private final int port;

    //getters for the Monitor
    public InetAddress getIp() {
        return ip;
    }

    public String getMonitorID() {
        return monitorID;
    }

    public int getPort() {
        return port;
    }

    //making a string contatining monitor information
    public String monitor_str() {
        return "Monitor ID: " + monitorID + " IP: " + ip + " PORT:" + port;
    }

    //constructor of the monitor
    public Monitor(InetAddress ip, String monitorID, int port) {
        this.ip = ip;
        this.monitorID = monitorID;
        this.port = port;
    }

    //waiting for the tcp connection
    public void waitForGatewayConnection() {

        //create server socket to create a tcp server 
        ServerSocket serverSocket = null;

        //create a client socket
        Socket clientSocket = null;

        //create a print write to send data
        PrintWriter out = null;

        //number of max inocoming connections are 10
        int maxIncomingConnections = 10;

        try {

            //create a tcp server socket using the port and the ip
            serverSocket = new ServerSocket(this.port, maxIncomingConnections, this.ip);

            //printing info to the console
            System.out.printf("starting up on %s port %s\n", this.ip, this.port);

            // Wait for a connection
            System.out.println("waiting for a connection");

            //when there is a tcp connection, accept it
            clientSocket = serverSocket.accept();

            //print the info
            System.out.println("connection from " + clientSocket);

            while (true) {
                // Receive the data in small chunks and retransmit it
                System.out.println("sending data to the gateway");
                String message = "Hello from Vital Monitor: " + this.monitorID;

                // Send data
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                //send the message to the gateway
                out.println(message);

                //sleep for 2 seconds
                TimeUnit.SECONDS.sleep(2);
            }

            //exception handling
        } catch (UnknownHostException ex) {

            //when the server is not found
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {

            //when input output error occurs
            System.out.println("I/O error: " + ex.getMessage());
        } catch (InterruptedException e) {

            //when interrupt error occurs
            e.printStackTrace();
        }
    }

    //override run method to execute the waitForGatewayConnection
    //this method is run, when we start the monitor thread
    @Override
    public void run() {
        this.waitForGatewayConnection();
    }
}

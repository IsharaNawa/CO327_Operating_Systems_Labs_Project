//E/17/219
//Nawarathna K.G.I.S.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class MonitorHandler implements Runnable {

    //monitor related to the thread
    private Monitor monitor;

    //to signal if the thread should be stopped.
    private boolean exit;

    //thread
    Thread t;

    //name of the thread, this is realded to the monitor id
    private String threadName;

    //pulic constructor
    public MonitorHandler(Monitor monitor) {

        //get the monitor passed
        this.monitor = monitor;

        //create the thread
        this.t = new Thread(this,monitor.getMonitorID());

        //set the thread name
        this.threadName = monitor.getMonitorID();

        //exit siganl is false
        this.exit = false;

        //print the message to tell the thread has started
        System.out.println("[+] THREAD STARTED: " + monitor.getMonitorID());

        //start the thread
        t.start();
        
    }

    //this method will run when calling the start method
    @Override
    public void run() {

        //whenever the exit signal off, run this loop
        while(!exit){
            try {

                //create a server socket
                Socket serverSocket = new Socket(monitor.getIp(), monitor.getPort());
                
                //create a buffer reader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                
                //always run this
                while (true) {

                    //read the message from the monitor
                    String message = bufferedReader.readLine();

                    //print the message from the monitor
                    System.out.println(message);

                }
            } catch (ConnectException e) {

                //when there is a connection error
                e.printStackTrace();

            } catch (SocketException e) {

                //when there is an error with the connection
                //print the disconnection to the console
                System.out.println("[-] DISCONNECTED : " + monitor.monitor_str());
                
                //remove the id from the id list
                Gateway.monitorIds.remove(monitor.getMonitorID());
                
                //stop the thread
                this.stop();
            } catch (IOException e) {

                //print the io exception
                e.printStackTrace();
            }
            
        }

        //when the thread is stopped, print that to the console
        System.out.println("[-] THREAD STOPPED: " + this.threadName);

        //check for the monitor id list
        //if it is empty, print the below message
        if(Gateway.monitorIds.size()==0){
            System.out.println("[$] SEARCHING : Searching for vital monitors...");
        }

    }

    //method to stop the thread
    public void stop(){
        this.exit = true;
    }
}

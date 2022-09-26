//E/17/219
//Nawarathna K.G.I.S.

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Gateway {

    static List<String> monitorIds = new ArrayList<String>();


    public static void main(String[] args) {

        //this stores the broadcast port
        int BROADCAST_PORT = 3000;
        
        //array to get the data
        byte[] buffer = new byte[1024];
        
        //datagram socket and packet for the connection
        DatagramSocket ds = null;
        DatagramPacket packet = null;

        //get the monitor object
        Monitor monitor = null;

        try {

            //create a new datagram socket
            ds = new DatagramSocket(BROADCAST_PORT);

            //create a packet to get the datagram packet
            packet = new DatagramPacket(buffer, buffer.length);

            //printing the initial message
            System.out.println("[$] GATEWAY STARTED : The Gateway is started!");
            System.out.println("[$] SEARCHING : Searching for vital monitors...");

                //always run this
                while (true) {

                    //when a datagram received, get that into the packet
                    ds.receive(packet);

                    try {
                        
                        //create a byte array input stream using the data of the packet
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());

                        //create a object input stream to get the monitor
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                        //create the monitor object
                        monitor = (Monitor) objectInputStream.readObject();

                    }

                    //catching exceptions 
                    catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }catch(Exception e){
                        System.out.print("[!] EXCEPTION : ");
                        System.out.println(e);
                    }

                    //check if the current monitor is already connected to the server
                    if (!monitorIds.contains(monitor.getMonitorID())) {

                        //if not, add the monitor id to the list
                        monitorIds.add(monitor.getMonitorID());

                        //print the below message to the console
                        System.out.println("[+] CONNECTED : "+monitor.monitor_str());

                        //create a handle montor to handle the monitor
                        MonitorHandler monitorHandler = new MonitorHandler(monitor);

                    }
                }
        //cathcing exceptions  
        } catch (BindException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e){
            System.out.print("[!] EXCEPTION : ");
            System.out.println(e);
        }
        //finally close the datasocket 
        finally{
            ds.close();
        }
    }
}

import java.io.*;
import java.net.*;
import java.util.*;

class ChatServer  extends Thread{

	//properties
	public static ServerSocket server;
	public static int serverSocketNum = 666;

	//connection is added as a new property of the class
	public Socket connection;

	//now object initialization needs Socket connection passed to it
	public ChatServer(Socket connection) {
		this.connection = connection;
	}

	//handleConnection() method is no longer static
	//it uses connection parameter of the class istead of passing the connection to it
	public void handleConnection() throws IOException {
		try {
			/* Accepting a connection to a socket is somewhat complex.
			 * Refer you networking notes for details
			 *
			 * Once a connection is accepted we have a socket
			 * The socket can be then used for sending and receiving
			 * information. Here we only need the input.
			 */
			BufferedReader in = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));

			/*
			 * We opened the scoket, got a input reader.
			 * Keep reading what client says and print on screen.
			 * When client says quit, break.
			 */
			try {
				while(true) {
					String str = in.readLine();
					if(str.equals("quit") || str.equals("Quit")) break;
					System.out.println("Message="+str);
				} // wait for next message
			} catch(NullPointerException e) {
				// if the client just kill the connection the str will
				// be null.
				System.out.printf("\nClient %s closed the connection",Thread.currentThread().getName().split("-")[1]);
			}
			connection.close();
		} catch (IOException e){
			System.out.println("Some issue" + e);
		}
	}

	//run method simply executes the handleConnection() method
	public void run(){
		try{
			this.handleConnection();
		}catch (Exception e){}
	}

	//main method
	public static void main(String [] args) throws IOException {
		server = new ServerSocket(serverSocketNum);

		while(true) {
			/* wait for a connection */
			Socket socket = server.accept();

			//after new connection is found, create a new client which is a thread object
			ChatServer client = new ChatServer(socket);

			//start the new thread
			client.start();
		}
	}
}




		
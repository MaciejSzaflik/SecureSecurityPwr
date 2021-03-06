package noob.pwr;

import java.io.IOException;
import java.net.ServerSocket;

public class MultiServer {
	
    public static void main(String[] args) throws IOException {

    if (args.length != 1) {
        System.err.println("Usage: java MultiServer <port number>");
        System.exit(1);
    }
        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;
        
        System.out.println("starting server at:" + portNumber);
        AdminPanel.getInstance();
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (listening) {
	            new TalkThread(serverSocket.accept()).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}

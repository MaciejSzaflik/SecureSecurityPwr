package noob.pwr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserThread extends Thread {
	
	PrintWriter outputStream;
    BufferedReader inputBuffer;
	
	public UserThread(String hostName, int port)
	{
		Socket kkSocket;
		try {
			kkSocket = new Socket(hostName, port);
			outputStream = new PrintWriter(kkSocket.getOutputStream(), true);
			
			InputStreamReader inputReader = new InputStreamReader(kkSocket.getInputStream());
			inputBuffer = new BufferedReader(inputReader);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void WriteMessage(String message)
	{
		outputStream.println(message);
	}
	
	public void run() {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        try {
			while ((fromServer = inputBuffer.readLine()) != null) {
			    System.out.println(fromServer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("end thread");
    }
}

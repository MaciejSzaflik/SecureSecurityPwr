package noob.pwr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserThread extends Thread {
	
	PrintWriter outputStream;
    BufferedReader inputBuffer;
    public DiffieHellmanProtocol keyProtocol;
	
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
		outputStream.println(keyProtocol.cipher.Encrypt(message));
	}
	
	public void ResponsePlain(String message)
	{
		outputStream.println(message);
	}
	
	public void run() {
        String fromServer;
        keyProtocol = new DiffieHellmanProtocol(false);
        
        try {
			while ((fromServer = inputBuffer.readLine()) != null) {
				String message = keyProtocol.ParseMessage(fromServer);
				if(message!=null && !message.isEmpty())
				{
					if(!message.equals("end"))
					{
						System.out.println(message);
						ResponsePlain(message);
					}
					else
						System.out.println("Please send your name :)");
				}
				else if(keyProtocol.HaveFinished())
				{
					System.out.println(keyProtocol.cipher.Decrypt(fromServer));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("end thread");
    }
}

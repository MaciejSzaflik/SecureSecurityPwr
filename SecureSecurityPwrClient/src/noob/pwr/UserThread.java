package noob.pwr;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class UserThread extends Thread {
	
	PrintWriter outputStream;
    BufferedReader inputBuffer;
    public DiffieHellmanProtocol keyProtocol;
    private boolean passwordVerified = false;
	
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
						ResponsePlain(message);
						ChatClient.loginWindow.lblStatus.setText("Trying to connect..");
						ChatClient.loginWindow.lblStatus.setForeground(Color.RED);
					}
					else
					{
						ChatClient.loginWindow.lblStatus.setText("Connected");
						ChatClient.loginWindow.lblStatus.setForeground(Color.GREEN);
					}
				}
				else if(message.isEmpty())
				{
					if(!passwordVerified)
					{
						String realMessage = keyProtocol.cipher.Decrypt(fromServer);
						if(realMessage.equals("passOk"))
						{
							passwordVerified = true;
							ChatClient.SetVisibleList();
						}
						else{
							ChatClient.loginWindow.lblStatus.setText("Bad pass");
							ChatClient.loginWindow.lblStatus.setForeground(Color.RED);
						}
							
					}
					else if(ChatClient.state == ChatClient.State.Chat)
						System.out.println(keyProtocol.cipher.Decrypt(fromServer));
					else if(ChatClient.state == ChatClient.State.List)
						ChatClient.listOfUsers.SetUsers(keyProtocol.cipher.Decrypt(fromServer));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ChatClient.loginWindow.lblStatus.setText("Error need connect again");
			ChatClient.loginWindow.lblStatus.setForeground(Color.RED);
			passwordVerified = false;
			ChatClient.SetVisibleLogin();
			e.printStackTrace();
		}
        System.out.println("end thread");
    }
}

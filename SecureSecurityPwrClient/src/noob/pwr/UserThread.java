package noob.pwr;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import javax.crypto.IllegalBlockSizeException;

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
	
	public void run() {
        String fromServer;
        keyProtocol = new DiffieHellmanProtocol(false);
        
        try {
			while ((fromServer = inputBuffer.readLine()) != null) {
				String message = keyProtocol.ParseMessage(fromServer);
				if(message!=null && !message.isEmpty())
				{
					CheckStatusDiffeHellMan(message);
				}
				else if(message.isEmpty())
				{
					HandleTalkProtocolResponse(fromServer);
				}
			}
		} catch (IOException e) {
			HandleException(e);
		}
        System.out.println("end thread");
    }
	
	public void WriteMessage(String prefix,String values,String message)
	{
		//System.out.println(prefix+ ";" + values + ";"+ message);
		outputStream.println(keyProtocol.cipher.Encrypt(prefix+ ";" + values + ";"+ message));
	}
	
	public void ResponsePlain(String message)
	{
		outputStream.println(message);
	}
	
	private void CheckStatusDiffeHellMan(String message)
	{
		if(!message.equals("end"))
		{
			ResponsePlain(message);
			ChatClient.instance.loginWindow.lblStatus.setText("Trying to connect..");
			ChatClient.instance.loginWindow.lblStatus.setForeground(Color.RED);
		}
		else
		{
			ChatClient.instance.loginWindow.lblStatus.setText("Connected");
			ChatClient.instance.loginWindow.lblStatus.setForeground(Color.GREEN);
		}
	}
	
	private void HandleException(Exception e)
	{
		ChatClient.instance.loginWindow.lblStatus.setText("Error need connect again");
		ChatClient.instance.loginWindow.lblStatus.setForeground(Color.RED);
		passwordVerified = false;
		ChatClient.instance.SetVisibleLogin();
	}
	
	private void HandleTalkProtocolResponse(String fromServer)
	{
		String realMessage = "";
		try{	
			System.out.println("Encrypted: " + fromServer);
			realMessage = keyProtocol.cipher.Decrypt(fromServer);
			System.out.println("Decrypted: " + realMessage);
		}
		catch(javax.crypto.IllegalBlockSizeException exception)
		{
			System.out.println("something wrong with message:");
			System.out.println(fromServer);
			return;
		}
		String[] input = realMessage.split(";");
    	String option = input[0];
    	String values = input[1];
    	String message = input[2];
		
		if(!passwordVerified && option.equals(ComConst.PASS_AND_NICK))
		{
			if(values.equals("passOk"))
			{
				passwordVerified = true;
				ChatClient.instance.SetVisibleList();
			}
			else{
				ChatClient.instance.loginWindow.lblStatus.setText("Bad pass");
				ChatClient.instance.loginWindow.lblStatus.setForeground(Color.RED);
			}
		}
		else if(ChatClient.instance.state != ChatClient.State.Login && option.equals(ComConst.BROADCAST))
			ChatClient.instance.WriteToChat("broadcast", values+ ": " + message);
		else if(ChatClient.instance.state != ChatClient.State.Login && option.equals(ComConst.WHISPER))
			ChatClient.instance.WriteToChat(values, values+ ": " + message);
		else if(ChatClient.instance.state == ChatClient.State.List && option.equals(ComConst.USERS))
			ChatClient.instance.listOfUsers.SetUsers(values);
		else if(ChatClient.instance.state == ChatClient.State.List && option.equals(ComConst.HISTORY))
		{
			new HistoryWindow(ParseHistory(values));
		}
		
	}
	private String[] ParseHistory(String codedInfo)
	{
		if(codedInfo.equals(ComConst.EMPTY))
			return null;
		
		String[] info = codedInfo.split(":");

		for(int i = 0;i<info.length;i++)
		{
			try {
				info[i] = keyProtocol.cipher.Decrypt(info[i]);
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return info;
	}
}

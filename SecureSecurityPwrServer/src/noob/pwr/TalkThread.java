package noob.pwr;

import java.net.*;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import java.io.*;

public class TalkThread extends Thread {
    private Socket socket = null;
    private PrintWriter outWriter;
    private BufferedReader inputReader;
    private TalkProtocol talkProtocol;
    private DiffieHellmanProtocol keyProtocol;

    public TalkThread(Socket socket) {
        super("MultiServerThread");
        this.socket = socket;
    }
    
    public void run() {
    		System.out.println("Starting new thread");
        try {
            outWriter = new PrintWriter(socket.getOutputStream(), true);
            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
       
            String inputLine;
            talkProtocol = new TalkProtocol();
            keyProtocol = new DiffieHellmanProtocol(true);
            outWriter.println(keyProtocol.ParseMessage(""));
            
            while ((inputLine = inputReader.readLine()) != null) {
            	String keyProtR = keyProtocol.ParseMessage(inputLine);
            	if(keyProtR!=null && !keyProtR.isEmpty())
            	{
            		System.out.println("send to client " + keyProtR + keyProtocol.currentStep);
            		outWriter.println(keyProtR);
            	}
            	else if(keyProtocol.HaveFinished())
            	{
            		inputLine = keyProtocol.cipher.Decrypt(inputLine);
            		HandleResponse(talkProtocol.processInput(inputLine));
            	}
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean HandleResponse(TalkResponse response)
    {
    	if(response.type == ResponseType.Talk)
    	{
    		ConnectionKeeper.getInstance().BroadcastMessage(talkProtocol.name, response.message);
        }
    	if(response.type == ResponseType.Whisper)
    	{
    		ConnectionKeeper.getInstance().InformUser(talkProtocol.name, response.reciver, response.message);
        }
    	else if(response.type == ResponseType.NameSet)
    	{
    		ConnectionKeeper.getInstance().AddUser(talkProtocol.name, this);
    		InformClient(ComConst.PASS_AND_NICK,"passOk",ComConst.EMPTY);
    	}
    	else if(response.type == ResponseType.PasswordInvalid)
    	{
    		InformClient(ComConst.PASS_AND_NICK,"passInvalid",ComConst.EMPTY);
    	}
    	else if(response.type == ResponseType.GetUsers)
    	{
    		InformClient(ComConst.USERS,response.message,ComConst.EMPTY);
    	}
    	return false;
    }
    
    public void InformClient(String prefix, String values, String message)
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append(prefix);
    	sb.append(";");
    	sb.append(values);
    	sb.append(";");
    	sb.append(message);
    	outWriter.println(keyProtocol.cipher.Encrypt(sb.toString()));
    }
}

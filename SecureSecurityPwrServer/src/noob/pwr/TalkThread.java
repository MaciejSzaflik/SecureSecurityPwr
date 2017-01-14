package noob.pwr;

import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.*;

public class TalkThread extends Thread {
    private Socket socket = null;
    private PrintWriter outWriter;
    private BufferedReader inputReader;
    private TalkProtocol talkProtocol;
    private DiffieHellmanProtocol keyProtocol;
    private MessageDigest sha;
	
    	
    public TalkThread(Socket socket) {
        super("MultiServerThread");
        this.socket = socket;
        
		try {
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
            		System.out.println("Encrypted: " + inputLine);
            		inputLine = keyProtocol.cipher.Decrypt(inputLine);
            		System.out.println("Decrypted: " + inputLine);
            		HandleResponse(talkProtocol.processInput(inputLine));
            	}
            }
            socket.close();
        } catch (IOException e) {
        	
        	ConnectionKeeper.getInstance().RemoveUser(talkProtocol.name);
            System.out.println("Ended conncetion");
        } catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
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
    		RecordInformation(talkProtocol.name, response.reciver, response.message);
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
    	else if(response.type == ResponseType.History)
    	{
    		String histVal = PrepareWholeHistory(talkProtocol.name,response.reciver);
    		System.out.println("asked for hist: " + histVal);
    		if(histVal != null)
    			InformClient(ComConst.HISTORY,histVal,ComConst.EMPTY);
    		else
    			InformClient(ComConst.HISTORY,ComConst.EMPTY,ComConst.EMPTY);
    	}
    	return false;
    }
    
    
    public String PrepareWholeHistory(String reader,String withWho)
    {
    	List<String> values = ReadWholeHistory(reader,withWho);
    	StringBuilder sb = new StringBuilder();
    	if(values == null || values.size() == 0)
    		return null;
    	for(int i = 0;i<values.size();i++)
    	{
    		sb.append(keyProtocol.cipher.Encrypt(values.get(i)));
    		sb.append(":");
    	}
		return sb.toString();
    }
    
    public List<String> ReadWholeHistory(String reader,String withWho)
    {
    	return SimpleDataBase.getInstance().GetWholeMap(getNamesSorted(reader, withWho));
    }
    
    public void RecordInformation(String sender, String reciver, String message)
    {
    	SimpleDataBase.getInstance().PutToMap(getNamesSorted(sender, reciver), Utilities.GetTimeStamp(),sender+": "+message);
    }
    
    private String getNamesSorted(String alice, String bob)
    {
    	byte[] digest = null;
    	if(alice.compareTo(bob) > 0)
    		digest = sha.digest((alice + bob).getBytes());
    	else
    		digest = sha.digest((bob + alice).getBytes());
    	return new String(digest);
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

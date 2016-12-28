package noob.pwr;

import java.net.*;
import java.io.*;

public class TalkThread extends Thread {
    private Socket socket = null;
    private PrintWriter outWriter;
    private BufferedReader inputReader;
    private TalkProtocol talkProtocol;

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
            outWriter.println("Hello, please send your name :)");
            
            while ((inputLine = inputReader.readLine()) != null) {
            	System.out.println("cilent said: "+ inputLine);
            	if(HandleResponse(talkProtocol.processInput(inputLine)))
            	{
            		break;
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
    	else if(response.type == ResponseType.NameSet)
    	{
    		outWriter.println("Name set for: " + talkProtocol.name);
    		ConnectionKeeper.getInstance().AddUser(talkProtocol.name, this);
    	}
    	return false;
    }
    
    public void InformClient(String user, String message)
    {
    	outWriter.println(user +": " + message);
    }
}

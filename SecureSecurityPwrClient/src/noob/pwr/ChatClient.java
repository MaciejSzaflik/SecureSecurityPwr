package noob.pwr;
import java.io.*;
import java.net.*;

public class ChatClient {
	
	private String[] args;
	public static ChatClient instance;
	public static ClientWindow windowInstance;
	private UserThread mainThread;
	
	public ChatClient(String[] args)
	{
		this.args = args;
	}
	
	
    public static void main(String[] args) throws IOException {
    	ChatClient.instance = new ChatClient(args);
    	ChatClient.windowInstance = new ClientWindow();
    }
    
    public void StartConnection(String[] args)
    {
    	if(mainThread!= null && mainThread.isAlive())
    	{
    		System.out.println("Alredy connected!");
    		return;
    	}
    	
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        mainThread = new UserThread(hostName, portNumber);
        mainThread.start();
    }
    
    public void WriteMessage(String message)
    {
    	if(mainThread!=null && mainThread.isAlive())
    		mainThread.WriteMessage(message);
    }

}
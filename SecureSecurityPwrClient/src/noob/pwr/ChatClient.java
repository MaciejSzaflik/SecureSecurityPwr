package noob.pwr;
import java.io.*;
import java.net.*;

public class ChatClient {
	
	private String[] args;
	
	public enum State
	{
		Login,
		List,
		Chat
	}
	
	public static ChatClient instance;
	public static LoginWindow loginWindow;
	public static ListOfUsers listOfUsers;
	public static ClientWindow windowInstance;
	public static State state;
	private UserThread mainThread;
	private UserAsker userAsker;
	
	public ChatClient(String[] args)
	{
		this.args = args;
	}
	
	
    public static void main(String[] args) throws IOException {
    	ChatClient.instance = new ChatClient(args);
    	ChatClient.loginWindow = new LoginWindow();
    	ChatClient.windowInstance = new ClientWindow();
    	ChatClient.listOfUsers = new ListOfUsers();
    	SetVisibleLogin();
    	ChatClient.instance.StartConnection();
    	
    }	
    
    public static void SetVisibleLogin()
    {
    	ChatClient.state = State.Login;
    	ChatClient.loginWindow.setVisible(true);
    	ChatClient.windowInstance.setVisible(false);
    	ChatClient.listOfUsers.setVisible(false);
    	if(ChatClient.instance.userAsker!=null)
    		ChatClient.instance.userAsker.askForUsers = false;
    }
    
    public static void SetVisibleList()
    {
    	ChatClient.state = State.List;
    	ChatClient.loginWindow.setVisible(false);
    	ChatClient.windowInstance.setVisible(false);
    	ChatClient.listOfUsers.setVisible(true);
    	
    	ChatClient.instance.userAsker = new UserAsker();
    	ChatClient.instance.userAsker.start();
    }
    
    public static void SetVisibleChat()
    {
    	ChatClient.state = State.Chat;
    	ChatClient.loginWindow.setVisible(false);
    	ChatClient.windowInstance.setVisible(true);
    	ChatClient.listOfUsers.setVisible(false);
    	if(ChatClient.instance.userAsker!=null)
    		ChatClient.instance.userAsker.askForUsers = false;
    }
    
    public void StartConnection()
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
package noob.pwr;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map.Entry;

public class ChatClient {
	
	private String[] args;
	
	public enum State
	{
		Login,
		List
	}
	
	public static ChatClient instance;
	public String myNick;
	public LoginWindow loginWindow;
	public ListOfUsers listOfUsers;
	public HashMap<String,ClientWindow> chatsWindows;
	public State state;
	private UserThread mainThread;
	private UserAsker userAsker;
	
	public ChatClient(String[] args)
	{
		instance = this;
		this.args = args;
	}
	
	
    public static void main(String[] args) throws IOException {
    	ChatClient.instance = new ChatClient(args);
    	ChatClient.instance.loginWindow = new LoginWindow();
    	ChatClient.instance.chatsWindows = new HashMap<String,ClientWindow>();
    	ChatClient.instance.listOfUsers = new ListOfUsers();
    	ChatClient.instance.SetVisibleLogin();
    	ChatClient.instance.StartConnection();
    	
    }	
    
    public void SetVisibleLogin()
    {
    	state = State.Login;
    	loginWindow.setVisible(true);
    	listOfUsers.setVisible(false);
    	if(userAsker!=null)
    		userAsker.askForUsers = false;
    	
    	for(Entry<String,ClientWindow> entry : chatsWindows.entrySet())
    	{
    		entry.getValue().dispose();
    		entry.setValue(null);
    	}
    }
    
    public void SetVisibleList()
    {
    	state = State.List;
    	loginWindow.setVisible(false);
    	listOfUsers.setVisible(true);
    	listOfUsers.nameLabel.setText("User: " + myNick);
    	userAsker = new UserAsker();
    	userAsker.start();
    }
    
    public void SetVisibleChat(String user)
    {
    	if(!chatsWindows.containsKey(user) || chatsWindows.get(user) ==null)
    		chatsWindows.put(user, new ClientWindow(user));
    		
    	chatsWindows.get(user).setVisible(true);
    }
    
    public void WriteToChat(String user,String message)
    {
    	SetVisibleChat(user);
    	chatsWindows.get(user).AppendTextToMain(message);
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
    
    public void WriteMessage(String prefix,String values,String message)
    {
    	if(mainThread!=null && mainThread.isAlive())
    		mainThread.WriteMessage(prefix,values,message);
    }

}
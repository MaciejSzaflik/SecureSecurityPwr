package noob.pwr;

import java.util.HashMap;

public class ConnectionKeeper {
	private static ConnectionKeeper instance;
	private HashMap<String,TalkThread> openConnections;
	public static ConnectionKeeper getInstance()
	{
		if(instance == null)
			instance = new ConnectionKeeper();
		return instance;
	}
	
	private ConnectionKeeper()
	{
		openConnections = new HashMap<String,TalkThread>();
	}
	
	public void AddUser(String user, TalkThread thread)
	{
		if(!openConnections.containsKey(user))
		{
			openConnections.put(user, thread);
		}
		AdminPanel.getInstance().SetCurrentConnections(openConnections);
	}
	
	public void InformUser(String seender, String user, String message)
	{
		if(openConnections.containsKey(user))
		{
			openConnections.get(user).InformClient(seender, message);
		}
	}
	public void BroadcastMessage(String seender,String message)
	{
		openConnections.entrySet().removeIf(entry->!entry.getValue().isAlive());
		openConnections.forEach((u,t) -> t.InformClient(seender,message));
		AdminPanel.getInstance().SetCurrentConnections(openConnections);
	}
}

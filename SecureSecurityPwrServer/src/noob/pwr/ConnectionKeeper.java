package noob.pwr;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
	
	public String GetUsersNames()
	{
		StringBuilder sb = new StringBuilder();
		for(Entry<String, TalkThread> entry : openConnections.entrySet())
		{
			sb.append(entry.getKey());
			sb.append(":");
		}
		return sb.toString();
	}
	
	public void InformUser(String seender, String reciver, String message)
	{
		if(openConnections.containsKey(reciver))
		{
			openConnections.get(reciver).InformClient(ComConst.WHISPER,seender,message);
		}
	}
	public void BroadcastMessage(String seender,String message)
	{
		openConnections.entrySet().removeIf(entry->!entry.getValue().isAlive());
		openConnections.forEach((u,t) -> t.InformClient(ComConst.BROADCAST,seender,message));
		AdminPanel.getInstance().SetCurrentConnections(openConnections);
	}
}

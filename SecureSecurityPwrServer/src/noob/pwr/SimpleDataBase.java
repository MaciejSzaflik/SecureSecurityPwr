package noob.pwr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class SimpleDataBase {
	
	private static SimpleDataBase instance;
	public static SimpleDataBase getInstance()
	{
		if(instance == null)
			instance = new SimpleDataBase();
		return instance;
	}
	
	private DB db;
	private SimpleDataBase()
	{
	}
	
	public void PutToMap(String mapName, String key, String value)
	{
		db = DBMaker.fileDB("messages.db").make();
		ConcurrentMap<String,String> map = (ConcurrentMap<String, String>) db.hashMap(mapName).createOrOpen();
		map.put(key, value);
		db.close();
	}
	
	public List<String> GetWholeMap(String mapName)
	{
		db = DBMaker.fileDB("messages.db").make();
		ConcurrentMap<String,String> map = (ConcurrentMap<String, String>) db.hashMap(mapName).createOrOpen();
		List<String> list = new ArrayList<String>();
		for(Map.Entry<String,String> entry : map.entrySet())
		{
			list.add(entry.getKey() + ";" + entry.getValue());
		}
		db.close();
		return list;
	}
	
	public boolean CheckInMap(String mapName, String key, String value)
	{
		db = DBMaker.fileDB("file1.db").make();
		ConcurrentMap map = db.hashMap(mapName).createOrOpen();
		if(map.containsKey(key))
		{
			boolean result = map.get(key).equals(value);
			db.close();
			return result;
		}
		else
			map.put(key, value);
		
		db.close();
		return true;
		
	}
	
}

package noob.pwr;

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
		db = DBMaker.fileDB("file1.db").make();
		ConcurrentMap map = db.hashMap(mapName).createOrOpen();
		map.put(key, value);
		db.close();
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

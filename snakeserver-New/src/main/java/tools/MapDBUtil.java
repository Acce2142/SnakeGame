package tools;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;


public class MapDBUtil {
	
	private static final DB db = DBMaker.fileDB("data.db")
			.closeOnJvmShutdown()
			.checksumHeaderBypass()
			.make();
	BTreeMap<String, String> btreeMap = db
			.treeMap("Player", Serializer.STRING, Serializer.STRING)
			.createOrOpen();
	public void storePlayer(String name, String password) {
		btreeMap.put(name, password);
	}

	public String getPlayerPassword(String name) {
		return btreeMap.get(name);
	}

public static void main(String arg[]){
	MapDBUtil newMap = new MapDBUtil();
}
}

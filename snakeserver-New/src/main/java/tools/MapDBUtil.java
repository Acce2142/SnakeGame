package tools;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import server.Player;

public class MapDBUtil {

	private static final DB db = DBMaker.fileDB("data.db")
			.closeOnJvmShutdown()
			.checksumHeaderBypass()
			.make();

	public void storePlayer(String name, String password) {
		BTreeMap<String, String> btreeMap = db
				.treeMap("Player", Serializer.STRING, Serializer.STRING)
				.createOrOpen();

		btreeMap.put(name, password);
	}

	public String getPlayerPassword(String name) {
		BTreeMap<String, String> btreeMap = db
				.treeMap("Player", Serializer.STRING, Serializer.STRING)
				.createOrOpen();
		return btreeMap.get(name);
	}

}

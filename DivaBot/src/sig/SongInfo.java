package sig;

import org.json.JSONObject;

public class SongInfo {
	public int id;
	public String name;
	public String romanized_name;
	public String english_name;
	public String artist;
	public String vocaloid;
	public String album_art;
	public JSONObject rating;
	public JSONObject notecount;
	
	SongInfo(JSONObject jsonData) {
		id = jsonData.getInt("id");
		name = jsonData.getString("name");
		romanized_name = jsonData.getString("romanized_name");
		english_name = jsonData.getString("english_name");
		artist = jsonData.getString("artist");
		vocaloid = jsonData.getString("vocaloid");
		album_art = jsonData.getString("album_art");
		rating = jsonData.getJSONObject("rating");
		notecount = jsonData.getJSONObject("notecount");
	}
	
	public static SongInfo getByTitle(String title) {
		for (SongInfo s : MyRobot.SONGNAMES) {
			if (s.name.equalsIgnoreCase(title)) {
				return s;
			}
		}
		return null;
	}
}

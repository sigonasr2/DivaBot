import java.awt.Color;

import sig.utils.FileUtils;

public class SongData {
	String title;
	Color[] songCode;
	final static float TOLERANCE = 0.95f;
	public SongData(String title,Color[] songCode) {
		this.title=title;
		this.songCode=songCode;
	}
	
	public static SongData getByTitle(String title) {
		for (SongData s : MyRobot.SONGS) {
			if (s.title.equalsIgnoreCase(title)) {
				return s;
			}
		}
		return null;
	}
	
	public static SongData compareData(Color[] data) {
		for (SongData s : MyRobot.SONGS) {
			int matched = 0;
			for (int i=0;i<s.songCode.length;i++) {
				if (data[i].equals(s.songCode[i])) {
					matched++;
				}
			}
			if (matched/(double)s.songCode.length>=TOLERANCE) {
				//System.out.println(matched+"/"+s.songCode.length+" pixels matched for song "+s.title);
				return s;
			} else {
				//System.out.println(matched+"/"+s.songCode.length+" pixels matched for song "+s.title);
			}
		}
		return null;
	}
	
	public static void saveSongToFile(String title, Color[] data) {
		StringBuilder sb = new StringBuilder(title);
		sb.append(":");
		boolean first = true;
		for (Color pixel : data) {
			if (first) {
				first=false;
			} else {
				sb.append(",");
			}
			sb.append(pixel.getRGB());
			//Color c = new Color(9,true);
		}
		FileUtils.logToFile(sb.toString(),"colorData");
	}
	public static void loadSongsFromFile() {
		String[] data = FileUtils.readFromFile("colorData");
		MyRobot.SONGS = new SongData[data.length];
		for (int i=0;i<data.length;i++) {
			SongData sd = ParseSong(data[i]);
			MyRobot.SONGS[i]=sd;
		}
	}
	
	public static SongData ParseSong(String s) {
		String[] split = s.split(":");
		String title = split[0];
		String[] data = split[1].split(",");
		Color[] colors = new Color[data.length];
		for (int i=0;i<data.length;i++) {
			colors[i]=new Color(Integer.parseInt(data[i]),true);
		}
		return new SongData(title,colors);
	}
}

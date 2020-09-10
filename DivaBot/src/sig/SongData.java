package sig;
import java.awt.Color;

import sig.utils.FileUtils;
import sig.utils.ImageUtils;

public class SongData {
	String title;
	Color[] songCode;
	int distance=0;
	static int MAXTHRESHOLD=400000;
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
		int closestDistance = Integer.MAX_VALUE;
		int closestMaxThresholdDistance = Integer.MAX_VALUE;
		SongData closestSong = null;
		for (SongData s : MyRobot.SONGS) {
			int distance = 0;
			if (s!=null&&s.songCode!=null) {
				for (int i=0;i<s.songCode.length;i++) {
					distance += ImageUtils.distanceToColor(s.songCode[i],data[i]);
					/*if (distance>MAXTHRESHOLD) {
						distance=MAXTHRESHOLD+1;
						break;
					}*/
				}
				if (/*distance<=MAXTHRESHOLD &&*/ distance<closestDistance) {
					//System.out.println(distance+" pixels matched for song "+s.title);
					closestSong=s;
					closestSong.distance=distance;
					closestDistance=distance;
				}
				/*if (distance>=MAXTHRESHOLD && distance<closestMaxThresholdDistance) {
					System.out.println(distance+" pixels diff: "+s.title);
					closestMaxThresholdDistance=distance;
				}*/
			} else {
				continue;
			}
		}
		return closestSong;
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
		System.out.println("Store "+title+"/"+colors);
		return new SongData(title,colors);
	}
}

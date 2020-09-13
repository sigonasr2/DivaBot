package sig;
import java.awt.Color;
import java.io.IOException;

import sig.utils.FileUtils;
import sig.utils.ImageUtils;

public class SongData {
	String title;
	long r;
	long g;
	long b;
	long distance=0;
	static int MAXTHRESHOLD=200000;
	final static float TOLERANCE = 0.95f;
	public SongData(String title,long r,long g,long b) {
		this.title=title;
		this.r=r;
		this.g=g;
		this.b=b;
	}
	
	public static SongData getByTitle(String title) {
		for (SongData s : MyRobot.SONGS) {
			if (s.title.equalsIgnoreCase(title)) {
				return s;
			}
		}
		return null;
	}
	
	public static SongData compareData(long r,long g,long b) {
		long closestDistance = Long.MAX_VALUE;
		SongData closestSong = null;
		for (SongData s : MyRobot.SONGS) {
			long distance = 0;
			if (s!=null) {
				distance = (long)(Math.pow(r-s.r,2)+Math.pow(g-s.g,2)+Math.pow(b-s.b, 2));
				if (/*distance<=MAXTHRESHOLD &&*/ distance<closestDistance) {
					//System.out.println(distance+" pixels matched for song "+s.title+"/");
					closestSong=s;
					closestSong.distance=distance;
					closestDistance=distance;
				}
			}
		}
		return closestSong;
	}
	
	public static void saveSongToFile(String title, long r, long g, long b) throws IOException {
		boolean found=false;
		StringBuilder sb = new StringBuilder(title);
		sb.append(":");
		
		sb.append(r).append(",")
		.append(g).append(",")
		.append(b);
		String[] fileData = FileUtils.readFromFile("colorData");
		for (int i=0;i<fileData.length;i++) {
			String[] split = fileData[i].split(":");
			if (split.length>0) {
				//System.out.println(split[0]+"/"+title);
				if (split[0].equalsIgnoreCase(title)) {
					//System.out.println("Updated color data with new data for "+title+"!");
					fileData[i]=sb.toString();
					found=true;
					break;
				}
			}
		}
		if (!found) {
			FileUtils.logToFile(sb.toString(),"colorData");
			System.out.println("Appended color data with new data for "+title+"!");
		} else {
			FileUtils.writetoFile(fileData, "colorData",false);
			System.out.println("Updated color data with new data for "+title+"!");
		}
	}
	public static void loadSongsFromFile() throws IOException {
		String[] data = FileUtils.readFromFile("colorData");
		MyRobot.SONGS = new SongData[data.length];
		for (int i=0;i<data.length;i++) {
			SongData sd = ParseSong(data[i]);
			MyRobot.SONGS[i]=sd;
		}
		MyRobot.firstTwentyPixels= new int[20];
	}
	
	public static SongData ParseSong(String s) {
		String[] split = s.split(":");
		String title = split[0];
		String[] data = split[1].split(",");
		if (Math.pow(Long.parseLong(data[0]),2)+Math.pow(Long.parseLong(data[1]),2)+Math.pow(Long.parseLong(data[2]),2)<MyRobot.smallestSongColor) {
			MyRobot.smallestSongColor=(long)Long.parseLong(data[0])+Long.parseLong(data[1])+Long.parseLong(data[2]);
		}
		return new SongData(title,Long.parseLong(data[0]),Long.parseLong(data[1]),Long.parseLong(data[2]));
	}
}

package sig;
import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

import sig.utils.FileUtils;
import sig.utils.ImageUtils;

public class SongData {
	String title;
	ColorDistance cd1,cd2,cd3,cd4;
	long distance=0;
	int votes=0;
	static int MAXTHRESHOLD=200000;
	final static float TOLERANCE = 0.95f;
	public SongData(String title,ColorDistance cd1,ColorDistance cd2,ColorDistance cd3,ColorDistance cd4) {
		this.title=title;
		this.cd1=cd1;
		this.cd2=cd2;
		this.cd3=cd3;
		this.cd4=cd4;
	}
	
	public static SongData getByTitle(String title) {
		for (SongData s : MyRobot.SONGS) {
			if (s.title.equalsIgnoreCase(title)) {
				return s;
			}
		}
		return null;
	}
	
	public static SongData compareData(ColorDistance cd1,ColorDistance cd2,ColorDistance cd3,ColorDistance cd4) {
		long closestDistance = Long.MAX_VALUE,closestDistance2 = Long.MAX_VALUE,closestDistance3 = Long.MAX_VALUE,closestDistance4 = Long.MAX_VALUE;
		SongData closestSong = null,closestSong2 = null,closestSong3 = null,closestSong4 = null;
		HashMap<String,SongData> songVotes = new HashMap<>();
		for (SongData s : MyRobot.SONGS) {
			long distance = 0;
			if (s!=null) {
				distance = CalculateDistance(cd1,s.cd1);
				if (/*distance<=MAXTHRESHOLD &&*/ distance<closestDistance) {
					//System.out.println(distance+" pixels matched for song "+s.title+"/");
					closestSong=s;
					closestSong.distance=distance;
					closestSong.votes=1;
					closestDistance=distance;
				}
				distance = CalculateDistance(cd2,s.cd2);
				if (/*distance<=MAXTHRESHOLD &&*/ distance<closestDistance2) {
					//System.out.println(distance+" pixels matched for song "+s.title+"/");
					closestSong2=s;
					closestSong2.distance=distance;
					closestSong2.votes=1;
					closestDistance2=distance;
				}
				distance = CalculateDistance(cd3,s.cd3);
				if (/*distance<=MAXTHRESHOLD &&*/ distance<closestDistance3) {
					//System.out.println(distance+" pixels matched for song "+s.title+"/");
					closestSong3=s;
					closestSong3.distance=distance;
					closestSong3.votes=1;
					closestDistance3=distance;
				}
				distance = CalculateDistance(cd4,s.cd4);
				if (/*distance<=MAXTHRESHOLD &&*/ distance<closestDistance4) {
					//System.out.println(distance+" pixels matched for song "+s.title+"/");
					closestSong4=s;
					closestSong4.distance=distance;
					closestSong4.votes=1;
					closestDistance4=distance;
				}
			}
		}
		songVotes.put(closestSong.title,songVotes.containsKey(closestSong.title)?songVotes.get(closestSong.title).addVote():closestSong);
		songVotes.put(closestSong2.title,songVotes.containsKey(closestSong2.title)?songVotes.get(closestSong2.title).addVote():closestSong2);
		songVotes.put(closestSong3.title,songVotes.containsKey(closestSong3.title)?songVotes.get(closestSong3.title).addVote():closestSong3);
		songVotes.put(closestSong4.title,songVotes.containsKey(closestSong4.title)?songVotes.get(closestSong4.title).addVote():closestSong4);
		SongData highestVotes=null;
		int highest = -1;
		for (String s : songVotes.keySet()) {
			if (songVotes.get(s).votes>highest) {
				highest=songVotes.get(s).votes;
				highestVotes = songVotes.get(s);
			}
		}
 		return highestVotes;
	}
	
	private SongData addVote() {
		votes++;
		return this;
	}

	private static long CalculateDistance(ColorDistance cd1, ColorDistance cd2) {
		return (long)(Math.pow(cd1.r-cd2.r,2)+Math.pow(cd1.g-cd2.g,2)+Math.pow(cd1.b-cd2.b, 2));
	}

	public static void saveSongToFile(String title, ColorDistance cd1,ColorDistance cd2,ColorDistance cd3,ColorDistance cd4) throws IOException {
		boolean found=false;
		StringBuilder sb = new StringBuilder(title.replaceAll(":", "-COLON-").replaceAll("\\*","").replaceAll("/",""));
		sb.append(":");
		
		sb.append(cd1.r).append(",")
		.append(cd1.g).append(",")
		.append(cd1.b).append(",")
		.append(cd2.r).append(",")
		.append(cd2.g).append(",")
		.append(cd2.b).append(",")
		.append(cd3.r).append(",")
		.append(cd3.g).append(",")
		.append(cd3.b).append(",")
		.append(cd4.r).append(",")
		.append(cd4.g).append(",")
		.append(cd4.b);
		String[] fileData=null;
		if (MyRobot.FUTURETONE) {
		 fileData=FileUtils.readFromFile("FTcolorData");
		} else {
		 fileData=FileUtils.readFromFile("colorData");	
		}
		for (int i=0;i<fileData.length;i++) {
			String[] split = fileData[i].split(":");
			if (split.length>0) {
				//System.out.println(split[0]+"/"+title);
				if (split[0].replaceAll("-COLON-", ":").equalsIgnoreCase(title.replaceAll("\\*","").replaceAll("/",""))) {
					//System.out.println("Updated color data with new data for "+title+"!");
					fileData[i]=sb.toString();
					found=true;
					break;
				}
			}
		}
		if (!found) {
			if (MyRobot.FUTURETONE) {
				FileUtils.logToFile(sb.toString(),"FTcolorData");
			} else {
				FileUtils.logToFile(sb.toString(),"colorData");
			}
			System.out.println("Appended color data with new data for "+title+"!");
		} else {
			if (MyRobot.FUTURETONE) {
				FileUtils.writetoFile(fileData, "FTcolorData",false);
			} else {
				FileUtils.writetoFile(fileData, "colorData",false);
			}
			System.out.println("Updated color data with new data for "+title+"!");
		}
	}
	public static void loadSongsFromFile() throws IOException {
		String[] data = null;
		if (MyRobot.FUTURETONE) {
			data=FileUtils.readFromFile("FTcolorData");
		} else {
			data=FileUtils.readFromFile("colorData");
		}
		MyRobot.SONGS = new SongData[data.length];
		for (int i=0;i<data.length;i++) {
			SongData sd = ParseSong(data[i]);
			MyRobot.SONGS[i]=sd;
		}
		MyRobot.firstTwentyPixels= new int[20];
	}
	
	public static SongData ParseSong(String s) {
		String[] split = s.split(":");
		String title = split[0].replaceAll("-COLON-", ":");
		String[] data = split[1].split(",");
		return new SongData(title,new ColorDistance(Long.parseLong(data[0]),Long.parseLong(data[1]),Long.parseLong(data[2])),
				new ColorDistance(Long.parseLong(data[3]),Long.parseLong(data[4]),Long.parseLong(data[5])),
				new ColorDistance(Long.parseLong(data[6]),Long.parseLong(data[7]),Long.parseLong(data[8])),
				new ColorDistance(Long.parseLong(data[9]),Long.parseLong(data[10]),Long.parseLong(data[11])));
	}
}

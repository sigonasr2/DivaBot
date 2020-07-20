package sig;

import java.awt.image.BufferedImage;
import java.io.File;

public class Result {
	String songName;
	String difficulty;
	int cool,fine,safe,sad,worst;
	float percent;
	boolean fail;
	File f;
	public Result(String song,String diff,int cool,int fine,int safe,int sad,int worst,float percent,File f) {
		this.songName=song;
		this.difficulty=diff;
		this.cool=cool;
		this.fine=fine;
		this.safe=safe;
		this.sad=sad;
		this.worst=worst;
		this.percent=percent;
		this.f=f;
	}
	public Result(String song,String diff,int cool,int fine,int safe,int sad,int worst,float percent,boolean fail,File f) {
		this(song,diff,cool,fine,safe,sad,worst,percent,f);
		this.fail=fail;
	}
	public String display() {
		return new StringBuilder(Integer.toString(cool)).append("/").append(fine)
				.append("/").append(safe).append("/").append(sad).append("/").append(worst).append("   ").append(percent).append("%").toString();
	}
}

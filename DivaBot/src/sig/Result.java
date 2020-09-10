package sig;

import java.awt.image.BufferedImage;
import java.io.File;

public class Result {
	public String songName;
	public String difficulty;
	public int cool,fine,safe,sad,worst;
	public float percent;
	public boolean fail;
	public String mod;
	public int combo,score;
	public Result(String song,String diff,int cool,int fine,int safe,int sad,int worst,float percent) {
		this.songName=song;
		this.difficulty=diff;
		this.cool=cool;
		this.fine=fine;
		this.safe=safe;
		this.sad=sad;
		this.worst=worst;
		this.percent=percent;
		this.mod="";
		this.combo=-1;
		this.score=-1;
	}
	public Result(String song,String diff,int cool,int fine,int safe,int sad,int worst,float percent,boolean fail) {
		this(song,diff,cool,fine,safe,sad,worst,percent);
		this.fail=fail;
	}
	public Result(String song,String diff,int cool,int fine,int safe,int sad,int worst,float percent,int combo, int score,boolean fail) {
		this(song,diff,cool,fine,safe,sad,worst,percent,fail);
		this.combo=combo;
		this.score=score;
	}
	public Result(String song,String diff,int cool,int fine,int safe,int sad,int worst,float percent,String mod,int combo, int score,boolean fail) {
		this(song,diff,cool,fine,safe,sad,worst,percent,fail);
		this.combo=combo;
		this.score=score;
		this.mod=mod;
	}
	public String display() {
		return new StringBuilder(Integer.toString(cool)).append("/").append(fine)
				.append("/").append(safe).append("/").append(sad).append("/").append(worst).append("   ").append(percent).append("%")
				.toString();
	}
	public String displayDebug() {
		return new StringBuilder(Integer.toString(cool)).append(",").append(fine)
				.append(",").append(safe).append(",").append(sad).append(",").append(worst).append(",").append(percent).append("f")
				.append(",\"").append(difficulty).append("\",\"").append(mod).append("\",").append(combo).append(",").append(score).append(",")
				.append(Boolean.toString(fail).toLowerCase())
				.toString();
	}
	public String toString() {
		return displayDebug();
	}
}

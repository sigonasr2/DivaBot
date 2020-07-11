
public class Result {
	String songName;
	String difficulty;
	int cool,fine,safe,sad,worst;
	float percent;
	public Result(String song,String diff,int cool,int fine,int safe,int sad,int worst,float percent) {
		this.songName=song;
		this.difficulty=diff;
		this.cool=cool;
		this.fine=fine;
		this.safe=safe;
		this.sad=sad;
		this.worst=worst;
		this.percent=percent;
	}
}

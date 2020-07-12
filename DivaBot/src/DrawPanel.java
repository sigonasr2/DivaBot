import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.AttributedString;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sig.utils.DrawUtils;
import sig.utils.FileUtils;

public class DrawPanel extends JPanel{
	String difficulty;
	String panelText;
	Font programFont = new Font("Open Sans Condensed", Font.PLAIN, 32);
	String songname = "";
	String romanizedname = "";
	String englishname = "";
	String artist = "";
	int fcCount = 0;
	int passes = 0;
	int plays = 0;
	Result bestPlay=null;
	int overallrating = 0;
	BufferedImage bar;
	BufferedImage hard;
	BufferedImage extreme;
	BufferedImage exextreme;
	BufferedImage overallbar;
    long ratingTime = System.currentTimeMillis()-10000;
    int lastRating = -1;
    Thread t = null;
	
	DrawPanel() {
		try {
			bar = ImageIO.read(new File("divabar.png"));
			overallbar = ImageIO.read(new File("overlaybar.png"));
			exextreme = ImageIO.read(new File("exex.png"));
			extreme = ImageIO.read(new File("ex.png"));
			hard = ImageIO.read(new File("hd.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pullData(String songname,String difficulty) {
		this.songname=(songname.equalsIgnoreCase("PIANOGIRL")?"PIANO*GIRL":songname);
		this.difficulty=difficulty;
		romanizedname="";
		englishname="";
		this.bestPlay=null;
		plays=0;
		passes=0;
		fcCount=0;
		artist="";
		this.repaint();
		if (t!=null && t.isAlive()) {
			t.stop();
		}
		t = new Thread() {
			public void run() {
				try {
					JSONObject obj = FileUtils.readJsonArrayFromUrl("http://45.33.13.215:4501/song/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")).getJSONObject(0);
					romanizedname = obj.getString("romanized_name");
					englishname = obj.getString("english_name");
					artist = obj.getString("artist");
					obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/bestplay/sigonasr2/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")+"/"+difficulty);
					if (obj.has("cool")) {
						bestPlay = new Result(MyRobot.p.songname,difficulty,obj.getInt("cool"),obj.getInt("fine"),obj.getInt("safe"),obj.getInt("sad"),obj.getInt("worst"),(float)obj.getDouble("percent"));
					} else {
						bestPlay = null;
					}
					obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/playcount/sigonasr2/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")+"/"+difficulty);
					plays = obj.getInt("playcount");
					obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/songpasscount/sigonasr2/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")+"/"+difficulty);
					passes = obj.getInt("passcount");
					obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/songfccount/sigonasr2/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")+"/"+difficulty);
					fcCount = obj.getInt("fccount");
					/*obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/rating/sigonasr2");
					lastRating = overallrating;
					overallrating = (int)obj.getDouble("rating");
					if (lastRating<overallrating) {ratingTime=System.currentTimeMillis();}*/
					MyRobot.p.repaint();
					} catch (JSONException | IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setColor(new Color(0,255,0));
		g.fillRect(0, 0, 1400, 100);
		g.setFont(programFont);
		g.drawImage(bar, 0, 0, this);
		if (ratingTime>System.currentTimeMillis()-10000) {
			DrawUtils.drawOutlineText(g, programFont, 32, 36, 3, new Color(220,220,255,(int)Math.min(((System.currentTimeMillis()-ratingTime))/5,255)), new Color(0,0,0,64), "Rating up! "+lastRating+" -> "+overallrating);
		} else {
			DrawUtils.drawOutlineText(g, programFont, 32, 36, 3, Color.WHITE, new Color(0,0,0,64), songname+" / "+((romanizedname.length()>0)?romanizedname:englishname)+" "+(artist.length()>0?"by "+artist:"")+"    "+((plays>0)?("Plays - "+(passes)+"/"+(plays)):"")+" "+((plays!=0)?"("+(int)((Math.floor((float)passes/plays)*100))+"% pass rate)":"No plays")+"      "+((bestPlay!=null)?"Best Play - "+bestPlay.display():"     Overall Rating: "+overallrating));
		}
		switch (difficulty) {
			case "H":{
				g.drawImage(hard,0,0,20,51,this);
			}break;
			case "EX":{
				g.drawImage(extreme,0,0,20,51,this);
			}break;
			case "EXEX":{
				g.drawImage(exextreme,0,0,20,51,this);
			}break;
		}
		//as.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        //g.drawString(songname, 24, 32);
		g.drawImage(overallbar, 1349, 0, this);
	}
}

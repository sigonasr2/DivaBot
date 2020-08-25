package sig;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.AttributedString;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sig.utils.DrawUtils;
import sig.utils.FileUtils;
import sig.utils.ImageUtils;
import sig.utils.TextUtils;

public class DrawCanvas extends JPanel implements KeyListener{
	String difficulty;
	String panelText;
	//Font programFont = new Font("Alata Regular", Font.PLAIN, 32);
	Font programFont;
	Font programFontSmall;
	String songname = "";
	String romanizedname = "";
	String englishname = "";
	String artist = "";
	int fcCount = 0;
	int passes = 0;
	int plays = 0;
	double difficultyRating = 0;
	Result bestPlay=null;
	int overallrating = 0;
	BufferedImage bar;
	BufferedImage hard;
	BufferedImage extreme;
	BufferedImage exextreme;
	BufferedImage overallbar;
	BufferedImage panel,paneloverlay,songpanel,songpaneloverlay;
    long ratingTime = System.currentTimeMillis()-10000;
    long bestPlayTime = System.currentTimeMillis()-10000;
    int lastRating = -1;
    double lastScore = 0d;
    Thread t = null;
    boolean scrolling = false;
    int scrollX = 0;
    int scrollSpd = 2;
    Timer scrollTimer = new Timer();
    int displayTimer = 0;
    BufferedImage doubleBuffer=null,firstBuffer=null;
    boolean targetBuffer=false;
	DrawCanvas() throws FontFormatException, IOException {
		Font originalProgramFont = Font.createFont(Font.TRUETYPE_FONT,new File("Alata-Regular.ttf"));
		programFont = originalProgramFont.deriveFont(36f);
		programFontSmall = originalProgramFont.deriveFont(24f);
		try {
			bar = ImageUtils.toCompatibleImage(ImageIO.read(new File("divabar.png")));
			overallbar = ImageUtils.toCompatibleImage(ImageIO.read(new File("overlaybar.png")));
			exextreme = ImageUtils.toCompatibleImage(ImageIO.read(new File("exex.png")));
			extreme = ImageUtils.toCompatibleImage(ImageIO.read(new File("ex.png")));
			hard = ImageUtils.toCompatibleImage(ImageIO.read(new File("hd.png")));
			panel = ImageUtils.toCompatibleImage(ImageIO.read(new File("panel.png")));
			songpanel = ImageUtils.toCompatibleImage(ImageIO.read(new File("songpanel.png")));
			paneloverlay = ImageUtils.toCompatibleImage(ImageIO.read(new File("paneloverlay.png")));
			songpaneloverlay = ImageUtils.toCompatibleImage(ImageIO.read(new File("songpanel_overlay.png")));
			

			Thread t = new Thread() {
				public void run() {
					while (true) {
							displayTimer++;
							MyRobot.p.repaint(0, 0, 1400, 1000);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pullData(String songname,String difficulty) {
		this.songname=(songname.equalsIgnoreCase("PIANOGIRL")?"PIANO*GIRL":(songname.equalsIgnoreCase("16 -out of the gravity-"))?"1/6 -out of the gravity-":songname);
		this.difficulty=difficulty;
		romanizedname="";
		englishname="";
		this.bestPlay=null;
		plays=0;
		passes=0;
		fcCount=0;
		artist="";
		this.repaint(0,0,1400,1000);
		if (t!=null && t.isAlive()) {
			t.stop();
		}
		t = new Thread() {
			public void run() {
				try {
					/*JSONObject obj = FileUtils.readJsonArrayFromUrl("http://45.33.13.215:4501/song/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")).getJSONObject(0);
					romanizedname = obj.getString("romanized_name");
					englishname = obj.getString("english_name");
					artist = obj.getString("artist");*/
					SongInfo currentSong = SongInfo.getByTitle(MyRobot.p.songname);
					if (currentSong.rating.has(difficulty)) {
						difficultyRating = currentSong.rating.getDouble(difficulty);
					}
					romanizedname = currentSong.romanized_name;
					englishname = currentSong.english_name;
					artist = currentSong.artist;
					JSONObject obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/bestplay/sigonasr2/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")+"/"+difficulty);
					if (obj.has("cool")) {
						bestPlay = new Result(MyRobot.p.songname,difficulty,obj.getInt("cool"),obj.getInt("fine"),obj.getInt("safe"),obj.getInt("sad"),obj.getInt("worst"),(float)obj.getDouble("percent"));
						lastScore = obj.getDouble("score");
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
					if (lastRating<overallrating) {ratingTime=System.currentTimeMillis();}
					*/
					String text = songname+" / "+((romanizedname.length()>0)?romanizedname:englishname)+" "+(artist.length()>0?"by "+artist:"")+"    "+((plays>0)?("Plays - "+(passes)+"/"+(plays)):"")+" "+((plays!=0)?"("+((int)(Math.floor(((float)passes)/plays*100)))+"% pass rate"+((fcCount>0)?"  -  "+fcCount+" FC"+(fcCount==1?"":"s")+"    "+((int)(Math.floor(((float)fcCount)/plays*100)))+"% FC rate":"")+")":"No plays")+"      "+((bestPlay!=null)?"Best Play - "+bestPlay.display():"")+"     Overall Rating: "+overallrating;
					Rectangle2D bounds = TextUtils.calculateStringBoundsFont(text, programFont);
					if (bounds.getWidth()>1345) {
						scrolling=true;
					} else {
						scrolling=false;
					}
					scrollX = 0;
					MyRobot.p.repaint(0,0,1400,1000);
					} catch (JSONException | IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		long startTime = System.currentTimeMillis();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2.setColor(new Color(0,255,0));
		g2.fillRect(0, 0, 1362, 1000);
		//if (!MyRobot.isOnSongSelect()) {
			//g2.setFont(programFont);
			//g2.drawImage(bar, 0, 0,null);
		
			g2.drawImage(songpanel, 0,0,null);
			g2.drawImage(panel, 0,935,null);
			g2.drawImage(panel, 484,935,null);
			g2.drawImage(panel, 968,935,null);

			String songDisplay = ((romanizedname.length()>0)?romanizedname:englishname) + " - " + artist;
			Rectangle2D bounds = TextUtils.calculateStringBoundsFont(songDisplay, programFont);
			if (bounds.getWidth()>675) {
				DrawUtils.drawOutlineText(g2, programFontSmall, 8, 42, 1, Color.WHITE, new Color(0,0,0,64), songDisplay);
			} else {
				DrawUtils.drawOutlineText(g2, programFont, 8, 42, 1, Color.WHITE, new Color(0,0,0,64), songDisplay);
			}

			if ((bestPlayTime>System.currentTimeMillis()-10000)) {
				DrawUtils.drawOutlineText(g2, programFont, 8, 935+42, 1, new Color(220,220,255,(int)Math.min(((System.currentTimeMillis()-bestPlayTime))/5,255)), new Color(0,0,0,64),"New Record!");
			} else {
				DrawUtils.drawOutlineText(g2, programFontSmall, 8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),((bestPlay!=null)?bestPlay.display():""));
			}
			if ((ratingTime>System.currentTimeMillis()-10000)) {
				DrawUtils.drawOutlineText(g2, programFontSmall, 484+8, 935+42, 1, new Color(220,220,255,(int)Math.min(((System.currentTimeMillis()-ratingTime))/5,255)), new Color(0,0,0,64),"Rating up! "+lastRating+" -> "+overallrating);
			} else {
				DrawUtils.drawOutlineText(g2, programFont, 484+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),Integer.toString(overallrating));
			}
			if (displayTimer%3==0) {
				DrawUtils.drawOutlineText(g2, programFont, 968+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),difficultyRating + " - " + fullNameDifficulty());
			} else 
			if (displayTimer%3==1) {
				if (plays>0) {
					DrawUtils.drawOutlineText(g2, programFontSmall, 968+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),""+(passes)+"/"+(plays)+" play"+((plays!=1)?"s":"")+" "+"("+((int)(Math.floor(((float)passes)/plays*100)))+"% pass rate)");
				} else {
					DrawUtils.drawOutlineText(g2, programFont, 968+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),"No plays");
				}
			} else {
				if (fcCount>0) {
					DrawUtils.drawOutlineText(g2, programFont, 968+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),fcCount +" FC"+(fcCount==1?"":"s")+"    "+((int)(Math.floor(((float)fcCount)/plays*100)))+"% FC rate");
				} else {
					DrawUtils.drawOutlineText(g2, programFont, 968+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),difficultyRating + " - " + fullNameDifficulty());
				}
			}
			
		/*
			if (ratingTime>System.currentTimeMillis()-10000) {
				DrawUtils.drawOutlineText(g, programFont, 32, 36, 1, new Color(220,220,255,(int)Math.min(((System.currentTimeMillis()-ratingTime))/5,255)), new Color(0,0,0,64), "Rating up! "+lastRating+" -> "+overallrating);
			} else {
				String text = songname+" / "+((romanizedname.length()>0)?romanizedname:englishname)+" "+(artist.length()>0?"by "+artist:"")+"    "+((plays>0)?("Plays - "+(passes)+"/"+(plays)):"")+" "+((plays!=0)?"("+((int)(Math.floor(((float)passes)/plays*100)))+"% pass rate"+((fcCount>0)?"  -  "+fcCount+" FC"+(fcCount==1?"":"s")+"    "+((int)(Math.floor(((float)fcCount)/plays*100)))+"% FC rate":"")+")":"No plays")+"      "+((bestPlay!=null)?"Best Play - "+bestPlay.display():"")+"     Overall Rating: "+overallrating;
				Rectangle2D bounds = TextUtils.calculateStringBoundsFont(text, programFont);
				if (scrollX<-bounds.getWidth()-100) {
					scrollX=0;
				}
				DrawUtils.drawOutlineText(g2, programFont, 32+scrollX, 36, 1, Color.WHITE, new Color(0,0,0,64), text);
				if (scrolling) {
					DrawUtils.drawOutlineText(g2, programFont, 32+scrollX+(int)bounds.getWidth()+100, 36, 1, Color.WHITE, new Color(0,0,0,64), text);
				}
			}*/
			
			 
			/*switch (difficulty) {
				case "H":{
					g2.drawImage(hard,0,0,20,51,null);
				}break;
				case "EX":{
					g2.drawImage(extreme,0,0,20,51,null);
				}break;
				case "EXEX":{
					g2.drawImage(exextreme,0,0,20,51,null);
				}break;
			}*/
		//}
		//as.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        //g2.drawString(songname, 24, 32);
		//g2.drawImage(overallbar, 1349, 0,null);
		g2.drawImage(songpaneloverlay, 0,0,null);
		g2.drawImage(paneloverlay, 0,935,null);
		g2.drawImage(paneloverlay, 484,935,null);
		g2.drawImage(paneloverlay, 968,935,null);
		//System.out.println(System.currentTimeMillis()-startTime+"ms");
	}

	private String fullNameDifficulty() {
		switch (difficulty) {
			case "E":{
				return "Easy";
			}
			case "N":{
				return "Normal";
			}
			case "H":{
				return "Hard";
			}
			case "EX":{
				return "Extreme";
			}
			case "EXEX":{
				return "Extra Extreme";
			}
		}
		return "";
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

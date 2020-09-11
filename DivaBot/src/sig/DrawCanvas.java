package sig;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.AttributedString;
import java.util.HashMap;
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

public class DrawCanvas extends JPanel implements KeyListener,ComponentListener,WindowListener,MouseListener{
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
	BufferedImage addConfigButton,backgroundColorButton;
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
    static Color background = new Color(170,170,170);
    public static HashMap<String,String> configData = new HashMap<String,String>();
	DrawCanvas() throws FontFormatException, IOException {
		loadConfig();
		addConfigButton = ImageIO.read(new File("addDisplay.png"));
		backgroundColorButton = ImageIO.read(new File("backgroundCol.png"));
		Thread t = new Thread() {
			public void run() {
				while (true) {
						displayTimer++;
						MyRobot.p.repaint(0, 0, MyRobot.p.getWidth(),MyRobot.p.getHeight());
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}
	
	public void pullData(final String songname,final String difficulty) {
		this.songname=(songname.equalsIgnoreCase("PIANOGIRL")?"PIANO*GIRL":(songname.equalsIgnoreCase("16 -out of the gravity-"))?"1/6 -out of the gravity-":songname);
		this.difficulty=difficulty;
		romanizedname="";
		englishname="";
		this.bestPlay=null;
		plays=0;
		passes=0;
		fcCount=0;
		artist="";
		this.repaint(0,0,this.getWidth(),this.getHeight());
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
					if (MyRobot.p.songname!=null) {
						SongInfo currentSong = SongInfo.getByTitle(MyRobot.p.songname);
						FileUtils.writetoFile(new String[] {MyRobot.p.songname}, "testencode.txt");
						if (currentSong!=null) {
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
							MyRobot.p.repaint(0,0,MyRobot.p.getWidth(),MyRobot.p.getHeight());
							}
						}
					} catch (JSONException | IOException e) {
						e.printStackTrace();
				}
			}
		};
		t.start();
	}

	public static void saveConfig() {
		String[] data = new String[configData.size()];
		int i = 0;
		for (String s : configData.keySet()) {
			data[i++]=s+"\t"+configData.get(s).replaceAll("\t", "");
		}
		FileUtils.writetoFile(data, "config.txt", false);
	}
	
	public static void loadConfig() throws IOException {
		String[] data = FileUtils.readFromFile("config.txt");
		for (int i=0;i<data.length;i++) {
			String[] split = data[i].split("\t");
			configData.put(split[0],split[1]);
		}
		
		applyConfig();
	}
	
	public static void applyConfig() {
		if (configData.containsKey("BACKGROUND")) {
			try {
				background = new Color(Integer.parseInt(configData.get("BACKGROUND")));
			} catch (NumberFormatException e) {
			}
		}
		if (MyRobot.p!=null) {
			MyRobot.p.repaint(0, 0, MyRobot.p.getWidth(),MyRobot.p.getHeight());
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		long startTime = System.currentTimeMillis();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2.setColor(background);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g2.drawImage(addConfigButton,getWidth()-addConfigButton.getWidth()+1,0,this);
		g2.drawImage(backgroundColorButton,getWidth()-backgroundColorButton.getWidth()+1,backgroundColorButton.getHeight()+1,this);
		

//			String songDisplay = ((romanizedname.length()>0)?romanizedname:englishname) + " - " + artist;
//			Rectangle2D bounds = TextUtils.calculateStringBoundsFont(songDisplay, programFont);
//			if (bounds.getWidth()>675) {
//				DrawUtils.drawOutlineText(g2, programFontSmall, 8, 42, 1, Color.WHITE, new Color(0,0,0,64), songDisplay);
//			} else {
//				DrawUtils.drawOutlineText(g2, programFont, 8, 42, 1, Color.WHITE, new Color(0,0,0,64), songDisplay);
//			}
//
//			if ((bestPlayTime>System.currentTimeMillis()-10000)) {
//				DrawUtils.drawOutlineText(g2, programFont, 8, 935+42, 1, new Color(220,220,255,(int)Math.min(((System.currentTimeMillis()-bestPlayTime))/5,255)), new Color(0,0,0,64),"New Record!");
//			} else {
//				DrawUtils.drawOutlineText(g2, programFontSmall, 8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),((bestPlay!=null)?bestPlay.display():""));
//			}
//			if ((ratingTime>System.currentTimeMillis()-10000)) {
//				DrawUtils.drawOutlineText(g2, programFontSmall, 484+8, 935+42, 1, new Color(220,220,255,(int)Math.min(((System.currentTimeMillis()-ratingTime))/5,255)), new Color(0,0,0,64),"Rating up! "+lastRating+" -> "+overallrating);
//			} else {
//				DrawUtils.drawOutlineText(g2, programFont, 484+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),Integer.toString(overallrating));
//			}
//			if (displayTimer%3==0) {
//				DrawUtils.drawOutlineText(g2, programFont, 968+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),difficultyRating + " - " + fullNameDifficulty());
//			} else 
//			if (displayTimer%3==1) {
//				if (plays>0) {
//					DrawUtils.drawOutlineText(g2, programFontSmall, 968+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),""+(passes)+"/"+(plays)+" play"+((plays!=1)?"s":"")+" "+"("+((int)(Math.floor(((float)passes)/plays*100)))+"% pass rate)");
//				} else {
//					DrawUtils.drawOutlineText(g2, programFont, 968+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),"No plays");
//				}
//			} else {
//				if (fcCount>0) {
//					DrawUtils.drawOutlineText(g2, programFont, 968+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),fcCount +" FC"+(fcCount==1?"":"s")+"    "+((int)(Math.floor(((float)fcCount)/plays*100)))+"% FC rate");
//				} else {
//					DrawUtils.drawOutlineText(g2, programFont, 968+8, 935+42, 1, Color.WHITE, new Color(0,0,0,64),difficultyRating + " - " + fullNameDifficulty());
//				}
//			}
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
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		configData.put("WIDTH",Integer.toString(this.getWidth()+MyRobot.FRAME.getInsets().left+MyRobot.FRAME.getInsets().right));
		configData.put("HEIGHT",Integer.toString(this.getHeight()+MyRobot.FRAME.getInsets().top+MyRobot.FRAME.getInsets().bottom));
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		saveConfig();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point cursor = e.getPoint();
		cursor.translate(-MyRobot.FRAME.getInsets().left,-MyRobot.FRAME.getInsets().top);
		System.out.println(cursor+"/"+addConfigButton.getHeight());
		if (cursor.x>=getWidth()-addConfigButton.getWidth()&&
				cursor.x<=getWidth()&&
				cursor.y>=0&&
				cursor.y<=addConfigButton.getHeight()) {
				MyRobot.FRAME.setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else
		if (cursor.x>=getWidth()-addConfigButton.getWidth()&&
		cursor.x<=getWidth()&&
		cursor.y>=addConfigButton.getHeight()+1&&
		cursor.y<=addConfigButton.getHeight()+1+addConfigButton.getHeight()) {
			Color c = MyRobot.CP.getBackgroundColor();
			if (c!=null) {
				configData.put("BACKGROUND",Integer.toString(c.getRGB()));
				applyConfig();
			}
		} else
		{
			MyRobot.FRAME.setCursor(Cursor.getDefaultCursor());	
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

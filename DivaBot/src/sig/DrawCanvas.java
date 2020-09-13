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
import java.awt.event.MouseMotionListener;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sig.utils.DrawUtils;
import sig.utils.FileUtils;
import sig.utils.ImageUtils;
import sig.utils.TextUtils;

public class DrawCanvas extends JPanel implements KeyListener,ComponentListener,WindowListener,MouseListener,MouseMotionListener{
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
    static List<Display> displays = new ArrayList<Display>();
    public static Display selectedDisplay = null;
    public static Display draggedDisplay = null;
    public static Point initialDragPoint = null;
	DrawCanvas() throws FontFormatException, IOException {
		//loadConfig();
		addConfigButton = ImageIO.read(new File("addDisplay.png"));
		backgroundColorButton = ImageIO.read(new File("backgroundCol.png"));
		Thread t = new Thread() {
			public void run() {
				while (true) {
						displayTimer++;
						MyRobot.p.repaint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}
	
	public static void refreshAllLabels() {
		for (Display d : displays) {
			d.forceUpdate=true;
			d.nextUpdateTime=System.currentTimeMillis()-1;
		}
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
						//FileUtils.writetoFile(new String[] {MyRobot.p.songname}, "testencode.txt");
						if (currentSong!=null) {
							if (currentSong.rating.has(difficulty)) {
								difficultyRating = currentSong.rating.getDouble(difficulty);
							}
							romanizedname = currentSong.romanized_name;
							englishname = currentSong.english_name;
							artist = currentSong.artist;
							MyRobot.p.refreshAllLabels();
							MyRobot.p.repaint();
							JSONObject obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/bestplay/"+MyRobot.USERNAME+"/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")+"/"+difficulty);
							if (obj.has("cool")) {
								bestPlay = new Result(MyRobot.p.songname,difficulty,obj.getInt("cool"),obj.getInt("fine"),obj.getInt("safe"),obj.getInt("sad"),obj.getInt("worst"),(float)obj.getDouble("percent"));
								lastScore = obj.getDouble("score");
							} else {
								bestPlay = null;
							}
							obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/playcount/"+MyRobot.USERNAME+"/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")+"/"+difficulty);
							plays = obj.getInt("playcount");
							obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/songpasscount/"+MyRobot.USERNAME+"/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")+"/"+difficulty);
							passes = obj.getInt("passcount");
							obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/songfccount/"+MyRobot.USERNAME+"/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")+"/"+difficulty);
							fcCount = obj.getInt("fccount");
							/*obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/rating/"+MyRobot.USERNAME);
							lastRating = overallrating;
							overallrating = (int)obj.getDouble("rating");
							if (lastRating<overallrating) {ratingTime=System.currentTimeMillis();}
							*/
							//String text = songname+" / "+((romanizedname.length()>0)?romanizedname:englishname)+" "+(artist.length()>0?"by "+artist:"")+"    "+((plays>0)?("Plays - "+(passes)+"/"+(plays)):"")+" "+((plays!=0)?"("+((int)(Math.floor(((float)passes)/plays*100)))+"% pass rate"+((fcCount>0)?"  -  "+fcCount+" FC"+(fcCount==1?"":"s")+"    "+((int)(Math.floor(((float)fcCount)/plays*100)))+"% FC rate":"")+")":"No plays")+"      "+((bestPlay!=null)?"Best Play - "+bestPlay.display():"")+"     Overall Rating: "+overallrating;
							/*Rectangle2D bounds = TextUtils.calculateStringBoundsFont(text, programFont);
							if (bounds.getWidth()>1345) {
								scrolling=true;
							} else {
								scrolling=false;
							}
							scrollX = 0;*/
							MyRobot.p.repaint();
							MyRobot.p.refreshAllLabels();
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
		loadDisplays();
	}

	private static void loadDisplays() {
		if (configData.containsKey("DISPLAYDATA")) {
			String[] displaySplit = configData.get("DISPLAYDATA").split("~");
			for (int i=0;i<displaySplit.length;i++) {
				MyRobot.p.displays.add(
						new Display(displaySplit[i])
						);
			}
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
		
		for (int i=0;i<displays.size();i++) {
			displays.get(i).draw(g);
		}
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
		if (selectedDisplay!=null && e.getKeyCode()==KeyEvent.VK_DELETE) {
			DeleteDisplay();
		}
	}

	private void DeleteDisplay() {
		if (selectedDisplay!=null) {
			int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you would like to delete this display box?","Warning",JOptionPane.YES_NO_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){
				selectedDisplay.deleted=true;
				selectedDisplay=(displays.remove(selectedDisplay))?null:selectedDisplay;
				repaint();
			}
		}
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
		saveDisplays();
		saveConfig();
	}

	private void saveDisplays() {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<displays.size();i++) {
			if (sb.length()>0) {
				sb.append("~");
			}
			sb.append(displays.get(i).getSaveString());
		}
		if (displays.size()>0) {
			configData.put("DISPLAYDATA",sb.toString());
		}
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
		Point cursor = GetCursorPosition(e);
		initialDragPoint=cursor;
		selectedDisplay=null;
		for (int i=0;i<displays.size();i++) {
			Display d = displays.get(i);
			if (cursor.x>=d.x&&
				cursor.x<=d.x+d.width&&
				cursor.y>=d.y&&
				cursor.y<=d.y+d.height) {
				selectedDisplay=d;
				break;
			}
		}
		
		if (selectedDisplay==null) {
			MyRobot.FRAME.setCursor(Cursor.getDefaultCursor());
			DisplayManager.f.setVisible(false);
		} else {
			MyRobot.FRAME.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			draggedDisplay=selectedDisplay;
		}
	}

	private Point GetCursorPosition(MouseEvent e) {
		Point cursor = e.getPoint();
		cursor.translate(-MyRobot.FRAME.getInsets().left,-MyRobot.FRAME.getInsets().top);
		return cursor;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point cursor = GetCursorPosition(e);
		draggedDisplay=null;
		MyRobot.FRAME.setCursor(Cursor.getDefaultCursor());
		switch (e.getButton()) {
		case MouseEvent.BUTTON3:{
			selectedDisplay=null;
			for (int i=0;i<displays.size();i++) {
				Display d = displays.get(i);
				if (cursor.x>=d.x&&
					cursor.x<=d.x+d.width&&
					cursor.y>=d.y&&
					cursor.y<=d.y+d.height) {
					selectedDisplay=d;
					break;
				}
			}
			DeleteDisplay();
		}break;
		case MouseEvent.BUTTON1:{
			//System.out.println(cursor+"/"+addConfigButton.getHeight());
			if (cursor.x>=getWidth()-addConfigButton.getWidth()&&
					cursor.x<=getWidth()&&
					cursor.y>=0&&
					cursor.y<=addConfigButton.getHeight()) {
					Display d = new Display();
					displays.add(d);
					selectedDisplay=d;
					DisplayManager.setupSettings(selectedDisplay);
					return;
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
				return;
			}
			
			Display previousDisplay = selectedDisplay;
			if (selectedDisplay.equals(previousDisplay)) {
				//System.out.println("Double click");
				DisplayManager.setupSettings(selectedDisplay);
			}
		}break;
	}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (draggedDisplay!=null) {
			Point cursor = GetCursorPosition(e);
			if (initialDragPoint!=null) {
				if ((Math.abs(cursor.x-initialDragPoint.x)>24||
					Math.abs(cursor.y-initialDragPoint.y)>24)) {
					draggedDisplay.x=(int)(Math.floor((cursor.x-draggedDisplay.width/2)/8)*8);
					draggedDisplay.y=(int)(Math.floor((cursor.y-draggedDisplay.height/2)/8)*8);
					MyRobot.p.repaint();
					initialDragPoint=null;
				}
			} else {
				draggedDisplay.x=(int)(Math.floor((cursor.x-draggedDisplay.width/2)/8)*8);
				draggedDisplay.y=(int)(Math.floor((cursor.y-draggedDisplay.height/2)/8)*8);
				MyRobot.p.repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

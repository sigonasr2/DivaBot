package sig;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import sig.utils.FileUtils;
import sig.utils.ImageUtils;
import sig.utils.SoundUtils;
import sig.utils.WebUtils;

public class MyRobot{
	static CustomRobot MYROBOT;
	Color SCREEN[][];
	static SongData SONGS[];
	/*static String SONGNAMES[] = new String[] {"Yellow","The secret garden","Tell Your World","愛言葉","Weekender Girl","歌に形はないけれど","えれくとりっく・えんじぇぅ","神曲","カンタレラ","巨大少女","クローバー♣クラブ","恋スルVOC@LOID","桜ノ雨","39","深海シティアンダーグラウンド","深海少女","積乱雲グラフィティ","千年の独奏歌","ダブルラリアット","ハジメテノオト","初めての恋が終わる時","packaged","Palette","FREELY TOMORROW","from Y to Y","みくみくにしてあげる♪","メルト","モノクロ∞ブルースカイ","ゆめゆめ","16 -out of the gravity-","ACUTE","インタビュア","LOL -lots of laugh-","Glory 3usi9","soundless voice","ジェミニ","白い雪のプリンセスは","スキキライ","タイムマシン","Dear","DECORATOR","トリコロール・エア・ライン","Nostalogic","Hand in Hand","Fire◎Flower","ブラック★ロックシューター","メテオ","ワールドイズマイン","アマツキツネ","erase or zero","エレクトロサチュレイタ","on the rocks","からくりピエロ","カラフル×メロディ","Catch the Wave","キャットフード","サマーアイドル","shake it!","Just Be Friends","スイートマジック","SPiCa -39's Giving Day Edition-","番凩","テレカクシ思春期","天樂","どういうことなの！？","東京テディベア","どりーみんチュチュ","トリノコシティ","ネトゲ廃人シュプレヒコール","No Logic","ハイハハイニ","はじめまして地球人さん","＊ハロー、プラネット。 (I.M.PLSE-EDIT)","Hello, Worker","忘却心中","magnet","右肩の蝶","結ンデ開イテ羅刹ト骸","メランコリック","リモコン","ルカルカ★ナイトフィーバー","炉心融解","WORLD'S END UMBRELLA","アカツキアライヴァル","アゲアゲアゲイン","1925","え？あぁ、そう。","エイリアンエイリアン","ODDS&ENDS","君の体温","こっち向いて Baby","壊セ壊セ","39みゅーじっく！","サンドリヨン","SING&SMILE","スノーマン","DYE","なりすましゲンガー","ヒバナ","ヒビカセ","ブラックゴールド","ミラクルペイント","指切り","ありふれたせかいせいふく","アンハッピーリフレイン","大江戸ジュリアナイト","ゴーストルール","こちら、幸福安心委員会です。","孤独の果て -extend edition-","ジターバグ","Sweet Devil","砂の惑星","テオ","初音ミクの消失 -DEAD END-","秘密警察","妄想スケッチ","リンちゃんなう！","ローリンガール","ロキ","ロミオとシンデレラ","エンヴィキャットウォーク","骸骨楽団とリリア","サイハテ","ジグソーパズル","千本桜","ピアノ×フォルテ×スキャンダル","Blackjack","ぽっぴっぽー","裏表ラバーズ","Sadistic.Music∞Factory","デンパラダイム","二次元ドリームフィーバー","ネガポジ＊コンティニューズ","初音ミクの激唱","ワールズエンド・ダンスホール","ココロ","システマティック・ラヴ","Knife","二息歩行","PIANOGIRL","夢喰い白黒バク","ブレス・ユア・ブレス","恋は戦争","あなたの歌姫","Starduster","StargazeR","リンリンシグナル","Rosary Pale","多重未来のカルテット～QUARTET THEME～","LIKE THE WIND","AFTER BURNER",
			"ストロボナイツ","VOiCE","恋色病棟","ねこみみスイッチ","パラジクロロベンゼン","カラフル×セクシィ","劣等上等","Star Story","パズル","キップル・インダストリー","夢の続き","MEGANE","Change me"};*/
	static SongInfo SONGNAMES[] = new SongInfo[] {};
	static String NEWSONGS[] = new String[] {"Catch the Wave"};
	int SCREEN_X;
	int SCREEN_Y;
	int WINDOW_X;
	int WINDOW_Y;
	int TYPE_DELAY = 0;
	int MOUSE_DELAY = 0;
	int SKILL = 0;
	int X, Y;
	int lastX, lastY;
	int savedX, savedY;
	int RED, GREEN, BLUE;
	Color PIX_CLOSE_ACTIVE_OFF_MOUSE;
	Color PIX_CLOSE_ACTIVE_ON_MOUSE;
	HashMap<Character, Integer> KEYMAP;
	ArrayList<Integer> randKeys;
	List<Result> results = new ArrayList<Result>();
	GraphicsEnvironment grEnv;
	GraphicsDevice grDevice;
	JPanel drawPanel;
	BufferedImage bufImg;
	Rectangle rect;
	static JTextField title;
	final int WIDTH = 204;
	final int HEIGHT = 25;
    public static DrawCanvas p;
    static int currentSong = 0;
    static SongData selectedSong = null;
    static String difficulty = "H"; //H=Hard EX=Extreme EXEX=Extra Extreme
    static boolean recordedResults=false;
    static boolean CALIBRATION = true;
    static Point STARTDRAG = null;
    static Point ENDDRAG = null;
    
    int lastcool,lastfine,lastsafe,lastsad,lastworst,lastcombo,lastscore;
    float lastpercent;
    boolean lastfail;
    long lastSongSelectTime = System.currentTimeMillis();
    
    static TypeFace2 typeface1,typeface2; 
    static Thread t = null;
    
    String prevSongTitle = "";
    String prevDifficulty = "";
    boolean eyeTrackingSceneOn=true;
    boolean recordingResults=false;
    long lastReportedEyeTrackingTime = System.currentTimeMillis();
    
    boolean overlayHidden=false;
    static boolean onSongSelect=false;
    static BufferedImage finishbutton = null; 
    static Dimension screenSize = new Dimension(0,0);
    static boolean dragging = false;
    static String CALIBRATIONSTATUS = "";
    static boolean calibrating=false;
    static Rectangle calibrationline = null;
    static boolean repaintCalled = false;
    public static Overlay OVERLAY;
    public static boolean CALIBRATION_MODE=false;
    
    public static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    
	
	public static void main(String[] args) throws JSONException, IOException, FontFormatException {
		if (args.length>0) {
			if (args[0].equalsIgnoreCase("calibrate")) {
				CALIBRATION_MODE=true;
			}
		}
		JSONObject obj = FileUtils.readJsonFromUrl("http://www.projectdivar.com/songs");
		SONGNAMES = new SongInfo[JSONObject.getNames(obj).length];
		for (String key : JSONObject.getNames(obj)) {
			SONGNAMES[Integer.parseInt(key)-1] = new SongInfo(obj.getJSONObject(key));
		}
		finishbutton = ImageIO.read(new File("finish.png"));
	    new MyRobot().go();
	}
	
	boolean EyeTrackingIsOn() {
		//1888,760
		if (System.currentTimeMillis()-5000>lastReportedEyeTrackingTime) {
			BufferedImage img = ImageUtils.toCompatibleImage(MYROBOT.createScreenCapture(new Rectangle(1865,760,1,1)));
			Color pixel = new Color(img.getRGB(0, 0));
			lastReportedEyeTrackingTime=System.currentTimeMillis();
			eyeTrackingSceneOn=pixel.getRed()<60 && pixel.getGreen()<60 && pixel.getBlue()<60;
		}
		return eyeTrackingSceneOn;
	}

	boolean textFailPixel(BufferedImage img) {
		Color failPixel = new Color(img.getRGB(0, 0));
		//System.out.println(failPixel);
		//r=128,g=5,b=232
		return failPixel.getRed()>=50 && failPixel.getRed()<=150 && failPixel.getGreen()>=50 && failPixel.getGreen()<=150 && failPixel.getBlue()>=50 && failPixel.getBlue()<=150;
	}
	
	void BotMain() {
		try {
			JSONObject obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/rating/sigonasr2");
			p.lastRating = p.overallrating;
			p.overallrating = (int)obj.getDouble("rating");
			if (p.lastRating<p.overallrating) {p.ratingTime=System.currentTimeMillis();}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						try {
							if (MyRobot.calibrating) {
								MyRobot.calibrating=false;
								Overlay.OVERLAY.setVisible(false);
								Thread.sleep(1000);
								Calibrator c = new Calibrator();
							}
							if (checkSongSelect()) {
								if (!overlayHidden) {
									overlayHidden=true;
									p.repaint(0, 0, 1400, 1000);
								}
								GetCurrentSong();
								GetCurrentDifficulty();
								recordedResults=false;
								if (selectedSong!=null && difficulty!=null) {
									if (!prevSongTitle.equalsIgnoreCase(selectedSong.title) || !prevDifficulty.equalsIgnoreCase(difficulty)) {
										System.out.println("On Song Select Screen: Current Song-"+selectedSong.title+" Diff:"+difficulty);
										p.pullData(selectedSong.title,difficulty);
										prevSongTitle=selectedSong.title;
										prevDifficulty=difficulty;
										MYROBOT.keyPress(KeyEvent.VK_CONTROL);
										MYROBOT.keyPress(KeyEvent.VK_SHIFT);
										MYROBOT.keyPress(KeyEvent.VK_F11);
										MYROBOT.keyRelease(KeyEvent.VK_F11);
										MYROBOT.keyRelease(KeyEvent.VK_SHIFT);
										MYROBOT.keyRelease(KeyEvent.VK_CONTROL);
									}
								}
								lastSongSelectTime = System.currentTimeMillis();
							} else {
								if (overlayHidden) {
									overlayHidden=false;
									p.repaint(0, 0, 1400, 1000);
								}
								if ((selectedSong!=null && difficulty!=null)) {
									
									if (OnResultsScreen() && !recordedResults && !recordingResults && results.size()==0) {
										lastSongSelectTime=System.currentTimeMillis();
										MYROBOT.setAutoDelay(0);
										MYROBOT.keyPress(KeyEvent.VK_CONTROL);
										MYROBOT.keyPress(KeyEvent.VK_SHIFT);
										MYROBOT.keyPress(KeyEvent.VK_F12);
										MYROBOT.keyRelease(KeyEvent.VK_F12);
										MYROBOT.keyRelease(KeyEvent.VK_SHIFT);
										MYROBOT.keyRelease(KeyEvent.VK_CONTROL);
										Thread.sleep(200);
										MYROBOT.refreshScoreScreen();
										ImageIO.write(MYROBOT.createScoreScreenCapture(),"png",new File("scoreimage.png"));
										File tmp = new File("tmp");
										if (tmp.exists()) {
											FileUtils.deleteFile(tmp);
										} else {
											tmp.mkdir();
										}
										try {
											Result data = typeface1.getAllData(MYROBOT.createScoreScreenCapture());
											//ImageIO.write(MYROBOT.createNormalScreenCapture(new Rectangle(418,204,1227,690)),"png",new File("test.png"));
											if (data.cool==-1 || data.fine==-1 || data.safe==-1 || data.sad==-1 || data.worst==-1 || data.percent<0f || data.percent>110f || data.combo==-1 || data.score==-1) {
												System.out.println("Waiting for results to populate...");
											} else 
											if ((data.combo!=lastcombo || data.fail!=lastfail || data.cool!=lastcool || lastfine!=data.fine || lastsafe!=data.safe || lastsad!=data.sad || lastworst!=data.worst)
													&& data.score!=lastscore /*|| lastpercent!=percent*/){
												//System.out.println("Results for "+selectedSong.title+" "+difficulty+": "+data.cool+"/"+data.fine+"/"+data.safe+"/"+data.sad+"/"+data.worst+" "+data.percent+"%");
												
												System.out.println("Results for "+selectedSong.title+" "+difficulty+": "+data.display());
												File songFolder = new File(selectedSong.title+"/"+difficulty);
												if (!songFolder.exists()) {
													songFolder.mkdirs();
												}
												File[] songFolderFiles = songFolder.listFiles();
												int playId = songFolderFiles.length;
												File playFolder = new File(selectedSong.title+"/"+difficulty+"/"+playId);
												playFolder.mkdir();
												recordedResults=true;
												lastcool=data.cool;
												lastfine=data.fine;
												lastsafe=data.safe;
												lastsad=data.sad;
												lastworst=data.worst;
												lastpercent=data.percent;
												lastcombo=data.combo;
												lastscore=data.score;
												lastfail=data.fail;
												new File("scoreimage.png").renameTo(new File(playFolder,selectedSong.title+"_"+difficulty+"play_"+data.cool+"_"+data.fine+"_"+data.safe+"_"+data.sad+"_"+data.worst+"_"+data.percent+""
														+ "_"+data.combo+"_"+data.score+".png"));
												results.add(new Result(selectedSong.title,difficulty,data.cool,data.fine,data.safe,data.sad,data.worst,data.percent,data.combo,data.score,data.fail));
												SoundUtils.playSound("collect_item.wav");
												//gotoxy(800,64);
												//click();
												MYROBOT.setAutoDelay(0);
												MYROBOT.keyPress(KeyEvent.VK_CONTROL);
												MYROBOT.keyPress(KeyEvent.VK_SHIFT);
												MYROBOT.keyPress(KeyEvent.VK_F11);
												MYROBOT.keyRelease(KeyEvent.VK_F11);
												MYROBOT.keyRelease(KeyEvent.VK_SHIFT);
												MYROBOT.keyRelease(KeyEvent.VK_CONTROL);
											}
										} catch (IOException|NumberFormatException|IndexOutOfBoundsException e) {
											
										}
									} else {
										if (results.size()>0) {
											recordingResults=true;
											for (Result r  : results) {
												r.songName=r.songName.equalsIgnoreCase("PIANOGIRL")?"PIANO*GIRL":(r.songName.equalsIgnoreCase("16 -out of the gravity-"))?"1/6 -out of the gravity-":r.songName;
												HttpClient httpclient = HttpClients.createDefault();
												HttpPost httppost = new HttpPost("http://45.33.13.215:4501/submit");

												// Request parameters and other properties.
												List<NameValuePair> params = new ArrayList<NameValuePair>();
												params.add(new BasicNameValuePair("song", r.songName));
												params.add(new BasicNameValuePair("username", "sigonasr2"));
												params.add(new BasicNameValuePair("authentication_token", "sig"));
												params.add(new BasicNameValuePair("difficulty", r.difficulty));
												params.add(new BasicNameValuePair("cool", Integer.toString(r.cool)));
												params.add(new BasicNameValuePair("fine", Integer.toString(r.fine)));
												params.add(new BasicNameValuePair("safe", Integer.toString(r.safe)));
												params.add(new BasicNameValuePair("sad", Integer.toString(r.sad)));
												params.add(new BasicNameValuePair("worst", Integer.toString(r.worst)));
												params.add(new BasicNameValuePair("percent", Float.toString(r.percent)));
												params.add(new BasicNameValuePair("fail", Boolean.toString(r.fail)));
												params.add(new BasicNameValuePair("mod", r.mod));
												params.add(new BasicNameValuePair("combo", Integer.toString(r.combo)));
												params.add(new BasicNameValuePair("gameScore", Integer.toString(r.score)));
												try {
													httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
												} catch (UnsupportedEncodingException e) {
													e.printStackTrace();
												}

												//Execute and get the response.
												HttpResponse response = null;
												try {
													response = httpclient.execute(httppost);
												} catch (IOException e) {
													e.printStackTrace();
												}
												HttpEntity entity = response.getEntity();

												if (entity != null) {
												    try (InputStream instream = entity.getContent()) {
												    	Scanner s = new Scanner(instream).useDelimiter("\\A");
												    	String result = s.hasNext() ? s.next() : "";
												    	System.out.println(result);
												    	instream.close();
												    } catch (UnsupportedOperationException | IOException e) {
														e.printStackTrace();
													}
												}
											}
											results.clear();

											try {
												JSONObject obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/rating/sigonasr2");
												JSONObject obj2 = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/bestplay/sigonasr2/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")+"/"+difficulty);
												p.lastRating = p.overallrating;
												if (obj2.has("score")) {
													double newScore = obj2.getDouble("score");
													if (newScore>p.lastScore) {
														p.bestPlayTime=System.currentTimeMillis();
													}
													p.lastScore = newScore;
												}
												p.overallrating = (int)obj.getDouble("rating");
												if (p.lastRating<p.overallrating) {p.ratingTime=System.currentTimeMillis();}
												p.pullData(selectedSong.title, difficulty);
											} catch (JSONException | IOException e) {
												e.printStackTrace();
											}
											recordingResults=false;
										}
										if (!OnResultsScreen() && recordedResults) {
											recordedResults=false;
										}
									}
								}
							}
							MYROBOT.refreshScreen();
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
				}
					
					private boolean OnResultsScreen() {
						Color c1 = new Color(MYROBOT.createScreenCapture(new Rectangle(449,434,2,2)).getRGB(0, 0));
						Color c2 = new Color(MYROBOT.createScreenCapture(new Rectangle(449,400,2,2)).getRGB(0, 0));
						Color c3 = new Color(MYROBOT.createScreenCapture(new Rectangle(901,460,2,2)).getRGB(0, 0));
						return c1.getRed()>=254 && c1.getGreen()>=254 && c1.getBlue()>=254 && c2.getRed()==16 && c2.getGreen()==222 && c2.getBlue()==202 &&
								c3.getRed()>=219 && c3.getRed()<=255 && c3.getGreen()>=213 && c3.getGreen()<=255 && c3.getBlue()>=160 && c3.getBlue()<=220;
					}

					private void GetCurrentDifficulty() {
						Color c = new Color(MYROBOT.createScreenCapture(new Rectangle(320,274,10,10)).getRGB(0, 0));
						//return c.getRed()==43 && c.getGreen()==88 && c.getBlue()==213;
						if (c.getRed()>100 && c.getRed()<200 && c.getBlue()>200 && c.getBlue()<255 && c.getGreen()<50) {
							difficulty="EXEX";
						} else 
						if (c.getRed()>150 && c.getRed()<255 && c.getBlue()<50 && c.getGreen()<50) {
							difficulty="EX";
						} else 
						if (c.getRed()>175 && c.getRed()<225 && c.getBlue()<50 && c.getGreen()<175 && c.getGreen()>135) {
							difficulty="H";
						} else 
						if (c.getRed()>0 && c.getRed()<50 && c.getBlue()<50 && c.getGreen()<255 && c.getGreen()>190) {
							difficulty="N";
						} else 
						if (c.getRed()>0 && c.getRed()<50 && c.getBlue()>170 && c.getBlue()<230 && c.getGreen()<190 && c.getGreen()>150) {
							difficulty="E";
						}
					}
					private void GetCurrentSong() throws IOException {
						BufferedImage img = ImageUtils.toCompatibleImage(MYROBOT.createScreenCapture(new Rectangle(812-10,380-10,WIDTH+20,HEIGHT+20)));
						boolean found=false;
						LOOP1:
						for (int x=0;x<10;x++) {
							for (int y=0;y<10;y++) {
								Color[] col = new Color[WIDTH*HEIGHT];
								for (int i=0;i<WIDTH;i++) {
									for (int j=0;j<HEIGHT;j++) {
										col[i*HEIGHT+j]=new Color(img.getRGB(i+10+x,j+10+y),true);
									}
								}
								SongData ss = SongData.compareData(col);
								if (ss!=null) {
									selectedSong = ss;
									found=true;
									break LOOP1;
								}
							}
							for (int y=-1;y>-10;y--) {
								Color[] col = new Color[WIDTH*HEIGHT];
								for (int i=0;i<WIDTH;i++) {
									for (int j=0;j<HEIGHT;j++) {
										col[i*HEIGHT+j]=new Color(img.getRGB(i+10+x,j+10+y),true);
									}
								}
								SongData ss = SongData.compareData(col);
								if (ss!=null) {
									selectedSong = ss;
									found=true;
									break LOOP1;
								}
							}
						}
						if (!found) {
							LOOP2:
							for (int x=0;x>-10;x--) {
								for (int y=0;y<10;y++) {
									Color[] col = new Color[WIDTH*HEIGHT];
									for (int i=0;i<WIDTH;i++) {
										for (int j=0;j<HEIGHT;j++) {
											col[i*HEIGHT+j]=new Color(img.getRGB(i+10+x,j+10+y),true);
										}
									}
									SongData ss = SongData.compareData(col);
									if (ss!=null) {
										selectedSong = ss;
										found=true;
										break LOOP2;
									}
								}
								for (int y=-1;y>-10;y--) {
									Color[] col = new Color[WIDTH*HEIGHT];
									for (int i=0;i<WIDTH;i++) {
										for (int j=0;j<HEIGHT;j++) {
											col[i*HEIGHT+j]=new Color(img.getRGB(i+10+x,j+10+y),true);
										}
									}
									SongData ss = SongData.compareData(col);
									if (ss!=null) {
										selectedSong = ss;
										found=true;
										break LOOP2;
									}
								}
							}
						}
					}
					
				},
                0,
                50);
	}
	
	void go() throws FontFormatException, IOException {
	    initialize();        
	    //gotoxy(100, 100);
	    SCREEN = new Color[SCREEN_X][SCREEN_Y];
	    long startTime = System.currentTimeMillis();
	    
	    SongData.loadSongsFromFile();
	    
		 System.setProperty("awt.useSystemAAFontSettings","on");
	    JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p = new DrawCanvas();
        p.difficulty="EXEX";
        p.songname = "Dear";
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        BufferedImage img1 = null;
        BufferedImage img2 = null;
        typeface1 = null;
        typeface2=null;
        try {
	        	typeface1 = new TypeFace2(
	    				ImageIO.read(new File("typeface.png")),
	    				ImageIO.read(new File("typeface2.png")),
	    				ImageIO.read(new File("typeface3.png"))
	    				);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        InputMap inputMap = p.getInputMap(condition);
        ActionMap actionMap = p.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "Press");
        
        actionMap.put("Press", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
    			//BufferedImage img = ImageUtils.toCompatibleImage(MYROBOT.createScreenCapture(new Rectangle(460,426,WIDTH,HEIGHT)));
            	//Buffered img ImageUtils.toCompatibleImage(
            	MYROBOT.refreshScreen();
            	BufferedImage img = null;
            	try {
					ImageIO.write(img=MYROBOT.createScreenCapture(new Rectangle(812,380,WIDTH,HEIGHT)),"png",new File("test.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    			Color[] col = new Color[WIDTH*HEIGHT];
    			for (int i=0;i<WIDTH;i++) {
    				for (int j=0;j<HEIGHT;j++) {
    					col[i*HEIGHT+j]=new Color(img.getRGB(i,j),true);
    				}
    			}
    			SongData.saveSongToFile(NEWSONGS[currentSong],col);
    		    SongData.loadSongsFromFile();
    		    currentSong+=1;
    		    if (currentSong>=NEWSONGS.length) {
        			System.out.println("DONE!");
    		    } else {
        		    for (SongInfo i : SONGNAMES) {
        		    	if (i.name.equalsIgnoreCase(NEWSONGS[currentSong])) {
                			System.out.println(NEWSONGS[currentSong]+" - "+((i.romanized_name.length()>0)?i.romanized_name:i.english_name));
        		    		break;
        		    	}
        		    }
    		    }
            }
         });
        if (CALIBRATION_MODE) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            f.setUndecorated(true);
		    OVERLAY = new Overlay();
		    OVERLAY.setBounds(f.getGraphicsConfiguration().getBounds());
		    OVERLAY.setOpaque(false);
		    f.addMouseListener(OVERLAY);
		    f.addMouseMotionListener(OVERLAY);
	        screenSize=new Dimension(f.getGraphicsConfiguration().getBounds().width,f.getGraphicsConfiguration().getBounds().height);
	        f.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		    //f.add(p);
	        //System.out.println(f.getGraphicsConfiguration().getBounds().width+"/"+f.getGraphicsConfiguration().getBounds().height);
	        f.setSize(f.getGraphicsConfiguration().getBounds().width,f.getGraphicsConfiguration().getBounds().height);
	        f.add(OVERLAY);
	        f.setBackground(new Color(0,0,0,0));
        } else {
        	f.setSize(1362, 1036);
        	f.add(p);
        }
        f.setVisible(true);
	    f.setTitle("DivaBot");
	    title = new JTextField();
	    title.setSize(200,100);
	    title.setText((currentSong>=SONGNAMES.length)?"DONE!":SONGNAMES[currentSong].name);
	    
	    
	    SongData s = SongData.getByTitle(SONGNAMES[currentSong].name);
	   
	    BotMain();
	}
	
	void RunTests() throws IOException {
		
		selectedSong=new SongData("LIKE THE WIND",new Color[] {});
		difficulty="H";
		
		RunTest("test1.jpg",393,127,28,10,48,72.28f,"EXEX","",85,577160,false);
		RunTest("test2.jpg",518,144,17,3,23,81.94f,"H","",98,580090,false);
		RunTest("test3.jpg",646,54,1,0,0,103.06f,"EX","",691,666410,false);
		RunTest("test4.jpg",518,64,0,0,0,102.57f,"H","",582,470770,false);
		RunTest("test5.jpg",276,58,3,0,0,89.64f,"E","",215,238240,false);
		RunTest("test6.jpg",448,129,17,7,42,79.22f,"EXEX","",90,760770,false);
		RunTest("test7.jpg",419,227,28,7,20,75.76f,"EX","",154,473340,false);
		RunTest("test8.jpg",567,26,0,0,0,104.31f,"EX","",593,569500,false);
		RunTest("test9.jpg",197,51,0,0,0,100.02f,"H","",248,194700,false);
		RunTest("test10.jpg",486,245,46,22,59,65.34f,"H","SD",89,495850,false);
		RunTest("test11.jpg",0,0,0,0,159,0.00f,"EX","SD",0,0,true);
		RunTest("test12.jpg",0,0,0,0,79,0.08f,"EX","HD",0,580,true);
		RunTest("test13.jpg",245,19,4,0,2,87.04f,"E","",103,179360,false);
		RunTest("test14.png",623,39,1,0,0,100.83f,"EXEX","HS",350,869180,false);
		RunTest("test15.png",540,57,1,0,3,98.05f,"EX","HS",225,602000,false);
		RunTest("test16.png",320,46,2,0,4,93.26f,"EXEX","HS",135,463170,false);
		RunTest("test17.png",431,30,3,0,3,100.51f,"EXEX","HS",386,581700,false);
		RunTest("test18.png",427,86,5,1,4,92.45f,"EX","HS",136,526740,false);
		RunTest("testimage.png",371,40,3,4,3,97.63f,"EX","HS",233,523750,false);
		RunTest("testimage2.png",942,71,1,0,3,97.02f,"EXEX","",714,951020,false);
		RunTest("testimage3.png",546,52,0,0,0,101.77f,"EX","",598,567430,false);
		RunTest("testimage4.png",279,81,16,2,3,75.40f,"N","",85,228100,false);
		RunTest("testimage5.png",276,184,6,1,11,82.16f,"EXEX","HS",78,549870,false);
		RunTest("testimage6.png",455,60,2,0,8,93.48f,"EXEX","HS",272,532470,false);
		RunTest("testimage7.png",452,128,8,2,16,88.28f,"EXEX","HS",146,773150,false);
		RunTest("testimage8.png",229,38,2,0,13,83.25f,"EXEX","HS",65,300120,false);
		RunTest("testimage9.png",413,70,1,0,21,82.66f,"EXEX","HS",94,443050,false);
		RunTest("16 -out of the gravity-.jpg",554,45,1,0,1,101.36f,"EX","HS",339,606780,false);
		RunTest("＊ハロー、プラネット。 (I.M.PLSE-EDIT).jpg",336,128,24,6,93,58.85f,"EX","",52,308760,true);
		
		RunTest("39.jpg",531,71,2,0,2,97.82f,"EXEX","HS",324,832390,false);
		RunTest("39みゅーじっく！.jpg",573,175,5,0,18,91.22f,"EX","HS",354,754140,false);
		RunTest("1925.jpg",510,115,14,7,22,77.79f,"EX","HS",85,564860,false);
		RunTest("ACUTE.jpg",478,64,1,1,5,95.76f,"EX","HS",197,505210,false);
		RunTest("AFTER BURNER.jpg",370,113,25,16,30,68.76f,"EX","HS",94,386390,true);
		RunTest("Blackjack.jpg",415,123,15,7,50,71.22f,"EX","HS",79,443260,false);
		RunTest("Catch the Wave.jpg",603,72,0,0,1,100.65f,"EX","HS",559,773570,false);
		RunTest("Dear.jpg",402,64,0,0,1,100.90f,"EXEX","HS",394,587740,false);
		RunTest("DECORATOR.jpg",436,100,1,0,6,93.52f,"EX","HS",217,560180,false);
		RunTest("DYE.jpg",530,106,7,2,13,84.77f,"EX","HS",143,486360,false);
		RunTest("erase or zero.jpg",442,70,0,0,2,100.12f,"EX","HS",265,731120,false);
		RunTest("FREELY TOMORROW.jpg",367,57,0,0,0,102.84f,"EX","HS",424,498640,false);
		RunTest("from Y to Y.jpg",350,49,6,1,8,86.35f,"EXEX","HS",139,427650,false);
		RunTest("Glory 3usi9.jpg",468,43,0,0,2,101.62f,"EX","HS",382,549780,false);
		RunTest("Hand in Hand.jpg",401,54,1,0,3,97.58f,"EX","HS",176,610040,false);
		RunTest("Hello, Worker.jpg",439,118,7,1,14,89.93f,"EXEX","HS",147,930290,false);
		RunTest("Just Be Friends.jpg",510,107,6,0,12,89.38f,"EXEX","HS",203,602080,false);
		RunTest("Knife.jpg",327,85,14,9,27,51.96f,"EX","HS",124,395170,true);
		RunTest("LIKE THE WIND.jpg",330,144,20,9,20,72.06f,"EX","HS",65,425970,false);
		RunTest("LOL -lots of laugh-.jpg",489,59,1,2,2,96.36f,"EX","HS",183,641920,false);
		RunTest("magnet.jpg",435,101,18,4,35,76.98f,"EXEX","HS",115,480540,false);
		RunTest("No Logic.jpg",491,101,11,5,15,86.32f,"EX","HS",186,476910,false);
		RunTest("Nostalogic.jpg",346,70,15,7,16,83.61f,"EX","HS",94,486030,false);
		RunTest("WORLD'S END UMBRELLA.jpg",437,136,6,1,3,90.59f,"H","",215,475120,false);
		RunTest("ぽっぴっぽー.jpg",350,46,7,6,3,80.39f,"N","",175,263630,false);
		RunTest("サマーアイドル.jpg",245,19,4,0,2,87.04f,"E","",103,179360,false);
	}
	
	void RunTest(String _img,int _cool,int _fine, int _safe, int _sad, int _worst, float _percent,String _difficulty,String _mod,int _combo,int _score,boolean _fail) throws IOException {
		RunTest(_img,_cool,_fine,_safe,_sad,_worst,_percent,_difficulty,_mod,_combo,_score,_fail,false);
	}
	
	void RunTest(String _img,int _cool,int _fine, int _safe, int _sad, int _worst, float _percent,String _difficulty,String _mod,int _combo,int _score,boolean _fail,boolean debug) throws IOException {
		System.out.println("Running test "+_img);
		long startTime = System.currentTimeMillis();
		String testdir="testsuite";
		Point offset = new Point(418,204);
		File tmp = new File("tmp");
		if (tmp.exists()) {
			FileUtils.deleteFile(tmp);
		} else {
			tmp.mkdir();
		}
		BufferedImage img=null;
		try {
			img = ImageIO.read(new File(testdir,_img));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Result data = typeface1.getAllData(img,debug);
		assert data.cool == _cool : "Expected cool count to be "+_cool+", got "+data.cool;
		assert data.fine == _fine : "Expected fine count to be "+_fine+", got "+data.fine;
		assert data.safe == _safe : "Expected safe count to be "+_safe+", got "+data.safe;
		assert data.sad == _sad : "Expected sad count to be "+_sad+", got "+data.sad;
		assert data.worst == _worst : "Expected worst count to be "+_worst+", got "+data.worst;
		assert data.percent == _percent : "Expected percent to be "+_percent+", got "+data.percent;
		assert data.fail == _fail : "Expected fail to be "+_fail+", got "+data.fail;
		assert data.mod == _mod : "Expected mod to be "+_mod+", got "+data.mod;
		assert data.difficulty == _difficulty : "Expected difficulty to be "+_difficulty+", got "+data.difficulty;
		assert data.combo == _combo : "Expected combo to be "+_combo+", got "+data.combo;
		assert data.score == _score : "Expected score to be "+_score+", got "+data.score;
		System.out.println(" Passed ("+(System.currentTimeMillis()-startTime)+"ms)!");
	}
	
	public static boolean checkSongSelect() throws IOException {
		Color c = new Color(MYROBOT.createScreenCapture(new Rectangle(845,638,1,1)).getRGB(0, 0));
		onSongSelect = c.getRed()==43 && c.getGreen()==88 && c.getBlue()==213;
		//System.out.println(onSongSelect+"/"+c);
		return onSongSelect;
	}
	
	public static boolean isOnSongSelect() {
		return onSongSelect;
	}
	
	void initialize() {
		System.setProperty("sun.java2d.opengl", "True");
	    grEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    grDevice = grEnv.getDefaultScreenDevice();
	    updateScreenInfo();
	    setKeyMap();
	    try {
	        MYROBOT = new CustomRobot();
	        MYROBOT.refreshScreen();
	    } catch (Exception e) {
	        JOptionPane.showOptionDialog(null, "Can't build the robot!", "Error", -1, 1, null, null, this);
	        System.exit(1);
	    }
	    X = SCREEN_X / 2;
	    Y = SCREEN_Y / 2;
	    //MYROBOT.mouseMove(X, Y);
	    PIX_CLOSE_ACTIVE_OFF_MOUSE = new Color(184, 67, 44);
	    PIX_CLOSE_ACTIVE_ON_MOUSE = new Color(210, 35, 2);
	}
	
	void updateScreenInfo() {
	    SCREEN_X = grDevice.getDisplayMode().getWidth();
	    SCREEN_Y = grDevice.getDisplayMode().getHeight();
	    WINDOW_X = grEnv.getMaximumWindowBounds().width;
	    WINDOW_Y = grEnv.getMaximumWindowBounds().height;
	}
	
	void setKeyMap() {
	    KEYMAP = new HashMap<Character, Integer>();
	    KEYMAP.put('q', 81);
	    KEYMAP.put('w', 87);
	    KEYMAP.put('e', 69);
	    KEYMAP.put('r', 82);
	    KEYMAP.put('t', 84);
	    KEYMAP.put('y', 89);
	    KEYMAP.put('u', 85);
	    KEYMAP.put('i', 73);
	    KEYMAP.put('o', 79);
	    KEYMAP.put('p', 80);
	    KEYMAP.put('a', 65);
	    KEYMAP.put('s', 83);
	    KEYMAP.put('d', 68);
	    KEYMAP.put('f', 70);
	    KEYMAP.put('g', 71);
	    KEYMAP.put('h', 72);
	    KEYMAP.put('j', 74);
	    KEYMAP.put('k', 75);
	    KEYMAP.put('l', 76);
	    KEYMAP.put('z', 90);
	    KEYMAP.put('x', 88);
	    KEYMAP.put('c', 67);
	    KEYMAP.put('v', 86);
	    KEYMAP.put('b', 66);
	    KEYMAP.put('n', 78);
	    KEYMAP.put('m', 77);
	    KEYMAP.put('1', 49);
	    KEYMAP.put('2', 50);
	    KEYMAP.put('3', 51);
	    KEYMAP.put('4', 52);
	    KEYMAP.put('5', 53);
	    KEYMAP.put('6', 54);
	    KEYMAP.put('7', 55);
	    KEYMAP.put('8', 56);
	    KEYMAP.put('9', 57);
	    KEYMAP.put('0', 48);
	    KEYMAP.put(' ', 32);
	
	    randKeys = new ArrayList<Integer>();
	    for (int j = 44; j < 94; j++) {
	        randKeys.add(j);
	    }
	    randKeys.remove(randKeys.indexOf(58));
	    randKeys.remove(randKeys.indexOf(60));
	    randKeys.remove(randKeys.indexOf(62));
	    randKeys.remove(randKeys.indexOf(63));
	    randKeys.remove(randKeys.indexOf(64));
	}
	
	void typeHuman(String letters) {
	    MYROBOT.setAutoDelay(TYPE_DELAY);
	    char[] letter = letters.toCharArray();
	    for (int i = 0; i < letter.length; i++) {
	        System.out.print(letter[i]);
	        try {
	            if ((int) (Math.random() * SKILL) == (int) (Math.random() * SKILL) / 2) {
	                int limit = (int) ((Math.random() * 3) + 1);
	                for (int j = 0; j < limit; j++) {
	                    int k = (int) (Math.random() * (randKeys.size() - 1));
	                    manuPress(randKeys.get(k));
	                }
	                sleep(200);
	                for (int j = 0; j < limit; j++) {
	                    backspace();
	                }
	            }
	            if ((Character.isLowerCase(letter[i])) || (Character.isDigit(letter[i]))) {
	                MYROBOT.keyPress(KEYMAP.get(letter[i]));
	                MYROBOT.keyRelease(KEYMAP.get(letter[i]));
	            } else {
	                letter[i] = Character.toLowerCase(letter[i]);
	                MYROBOT.keyPress(16);
	                MYROBOT.keyPress(KEYMAP.get(letter[i]));
	                MYROBOT.keyRelease(KEYMAP.get(letter[i]));
	                MYROBOT.keyRelease(16);
	            }
	        } catch (Exception e) {
	            switch (letter[i]) {
	                case '!': {
	                    manuPressSHIFTED(49);
	                    break;
	                }
	                case '@': {
	                    manuPressSHIFTED(50);
	                    break;
	                }
	                case '#': {
	                    manuPressSHIFTED(51);
	                    break;
	                }
	                case '$': {
	                    manuPressSHIFTED(52);
	                    break;
	                }
	                case '%': {
	                    manuPressSHIFTED(53);
	                    break;
	                }
	                case '^': {
	                    manuPressSHIFTED(54);
	                    break;
	                }
	                case '&': {
	                    manuPressSHIFTED(55);
	                    break;
	                }
	                case '*': {
	                    manuPressSHIFTED(56);
	                    break;
	                }
	                case '(': {
	                    manuPressSHIFTED(57);
	                    break;
	                }
	                case ')': {
	                    manuPressSHIFTED(48);
	                    break;
	                }
	                case '?': {
	                    manuPressSHIFTED(47);
	                    break;
	                }
	                case '/': {
	                    manuPress(47);
	                    break;
	                }
	                case ':': {
	                    manuPressSHIFTED(59);
	                    break;
	                }
	                case ';': {
	                    manuPress(59);
	                    break;
	                }
	                case '.': {
	                    manuPress(46);
	                    break;
	                }
	                case ',': {
	                    manuPress(44);
	                    break;
	                }
	                case '"': {
	                    manuPressSHIFTED(222);
	                    break;
	                }
	                case '\'': {
	                    manuPress(222);
	                    break;
	                }
	                case '[': {
	                    manuPress(91);
	                    break;
	                }
	                case ']': {
	                    manuPress(93);
	                    break;
	                }
	                case '{': {
	                    manuPressSHIFTED(91);
	                    break;
	                }
	                case '}': {
	                    manuPressSHIFTED(93);
	                    break;
	                }
	                case '\\': {
	                    manuPress(92);
	                    break;
	                }
	                case '|': {
	                    manuPressSHIFTED(92);
	                    break;
	                }
	                case '=': {
	                    manuPressSHIFTED(61);
	                    break;
	                }
	                case '+': {
	                    manuPressSHIFTED(61);
	                    break;
	                }
	                case '-': {
	                    manuPress(45);
	                    break;
	                }
	                case '_': {
	                    manuPressSHIFTED(45);
	                    break;
	                }
	                case '`': {
	                    manuPress(192);
	                    break;
	                }
	                case '~': {
	                    manuPressSHIFTED(192);
	                    break;
	                }
	                case '<': {
	                    manuPressSHIFTED(44);
	                    break;
	                }
	                case '>': {
	                    manuPressSHIFTED(46);
	                    break;
	                }
	
	            }
	        }
	
	    }
	    System.out.println("");
	}
	
	void type(String letters) {
	    MYROBOT.setAutoDelay(TYPE_DELAY);
	    char[] letter = letters.toCharArray();
	    for (int i = 0; i < letter.length; i++) {
	        //System.out.print(letter[i]);
	        try {
	            if ((Character.isLowerCase(letter[i])) || (Character.isDigit(letter[i]))) {
	                MYROBOT.keyPress(KEYMAP.get(letter[i]));
	                MYROBOT.keyRelease(KEYMAP.get(letter[i]));
	            } else {
	                letter[i] = Character.toLowerCase(letter[i]);
	                MYROBOT.keyPress(16);
	                MYROBOT.keyPress(KEYMAP.get(letter[i]));
	                MYROBOT.keyRelease(KEYMAP.get(letter[i]));
	                MYROBOT.keyRelease(16);
	            }
	        } catch (Exception e) {
	            switch (letter[i]) {
	                case '!': {
	                    manuPressSHIFTED(49);
	                    break;
	                }
	                case '@': {
	                    manuPressSHIFTED(50);
	                    break;
	                }
	                case '#': {
	                    manuPressSHIFTED(51);
	                    break;
	                }
	                case '$': {
	                    manuPressSHIFTED(52);
	                    break;
	                }
	                case '%': {
	                    manuPressSHIFTED(53);
	                    break;
	                }
	                case '^': {
	                    manuPressSHIFTED(54);
	                    break;
	                }
	                case '&': {
	                    manuPressSHIFTED(55);
	                    break;
	                }
	                case '*': {
	                    manuPressSHIFTED(56);
	                    break;
	                }
	                case '(': {
	                    manuPressSHIFTED(57);
	                    break;
	                }
	                case ')': {
	                    manuPressSHIFTED(48);
	                    break;
	                }
	                case '?': {
	                    manuPressSHIFTED(47);
	                    break;
	                }
	                case '/': {
	                    manuPress(47);
	                    break;
	                }
	                case ':': {
	                    manuPressSHIFTED(59);
	                    break;
	                }
	                case ';': {
	                    manuPress(59);
	                    break;
	                }
	                case '.': {
	                    manuPress(46);
	                    break;
	                }
	                case ',': {
	                    manuPress(44);
	                    break;
	                }
	                case '"': {
	                    manuPressSHIFTED(222);
	                    break;
	                }
	                case '\'': {
	                    manuPress(222);
	                    break;
	                }
	                case '[': {
	                    manuPress(91);
	                    break;
	                }
	                case ']': {
	                    manuPress(93);
	                    break;
	                }
	                case '{': {
	                    manuPressSHIFTED(91);
	                    break;
	                }
	                case '}': {
	                    manuPressSHIFTED(93);
	                    break;
	                }
	                case '\\': {
	                    manuPress(92);
	                    break;
	                }
	                case '|': {
	                    manuPressSHIFTED(92);
	                    break;
	                }
	                case '=': {
	                    manuPressSHIFTED(61);
	                    break;
	                }
	                case '+': {
	                    manuPressSHIFTED(61);
	                    break;
	                }
	                case '-': {
	                    manuPress(45);
	                    break;
	                }
	                case '_': {
	                    manuPressSHIFTED(45);
	                    break;
	                }
	                case '`': {
	                    manuPress(192);
	                    break;
	                }
	                case '~': {
	                    manuPressSHIFTED(192);
	                    break;
	                }
	                case '<': {
	                    manuPressSHIFTED(44);
	                    break;
	                }
	                case '>': {
	                    manuPressSHIFTED(46);
	                    break;
	                }
	
	            }
	        }
	
	    }
	    System.out.println("");
	}
	
	void typeln(String letters) {
	    type(letters);
	    enter();
	}
	
	void typelnHuman(String letters) {
	    typeHuman(letters);
	    enter();
	}
	
	void getRun() {
	    MYROBOT.keyPress(524);
	    MYROBOT.keyPress(82);
	    MYROBOT.keyRelease(82);
	    MYROBOT.keyRelease(524);
	    sleep(500);
	}
	
	void combinePress(int keyOne, int keyTwo) {
	    MYROBOT.keyPress(keyOne);
	    MYROBOT.keyPress(keyTwo);
	    MYROBOT.keyRelease(keyTwo);
	    MYROBOT.keyRelease(keyOne);
	}
	
	void combinePress(int keyOne, int keyTwo, int keyThree) {
	    MYROBOT.keyPress(keyOne);
	    MYROBOT.keyPress(keyTwo);
	    MYROBOT.keyPress(keyThree);
	    MYROBOT.keyRelease(keyThree);
	    MYROBOT.keyRelease(keyTwo);
	    MYROBOT.keyRelease(keyOne);
	}
	
	void altTab() {
	    MYROBOT.keyPress(18);
	    MYROBOT.keyPress(9);
	    MYROBOT.keyRelease(9);
	    MYROBOT.keyRelease(18);
	}
	
	void winD() {
	    MYROBOT.keyPress(524);
	    MYROBOT.keyPress(68);
	    MYROBOT.keyRelease(68);
	    MYROBOT.keyRelease(524);
	}
	
	void altF4() {
	    MYROBOT.keyPress(18);
	    MYROBOT.keyPress(115);
	    MYROBOT.keyRelease(115);
	    MYROBOT.keyRelease(18);
	}
	
	void enter() {
	    MYROBOT.keyPress(10);
	    MYROBOT.keyRelease(10);
	}
	
	void backspace() {
	    MYROBOT.keyPress(8);
	    MYROBOT.keyRelease(8);
	}
	
	void sleep(long duration) {
	    try {
	        Thread.sleep(duration);
	    } catch (Exception e) {
	    }
	}
	
	void click() {
	    MYROBOT.mousePress(16);
	    MYROBOT.mouseRelease(16);
	}
	
	void doubleClick() {
	    click();
	    click();
	}
	
	void clickNhold(boolean tof) {
	    if (tof) {
	        MYROBOT.mousePress(16);
	    } else {
	        MYROBOT.mouseRelease(16);
	    }
	}
	
	void rightClick() {
	    MYROBOT.mousePress(4);
	    MYROBOT.mouseRelease(4);
	}
	
	void rightclickHold(boolean tof) {
	    if (tof) {
	        MYROBOT.mousePress(4);
	    } else {
	        MYROBOT.mouseRelease(4);
	    }
	}
	
	void middleClick() {
	    MYROBOT.mousePress(8);
	    MYROBOT.mouseRelease(8);
	}
	
	void manuPress(int code) {
	    MYROBOT.keyRelease(16);
	    MYROBOT.keyPress(code);
	    MYROBOT.keyRelease(code);
	}
	
	void manuPressSHIFTED(int code) {
	    MYROBOT.keyPress(16);
	    MYROBOT.keyPress(code);
	    MYROBOT.keyRelease(code);
	    MYROBOT.keyRelease(16);
	}
	
	void gotoxy(int goX, int goY) {
	    lastX = X;
	    lastY = Y;
	    MYROBOT.setAutoDelay(MOUSE_DELAY);
	    int initX = X;
	    int initY = Y;
	    if (goX == X) {
	        if (goY > Y) {
	            for (int i = Y; i <= goY; i++) {
	                Y = i;
	                MYROBOT.mouseMove(X, Y);
	            }
	        } else {
	            for (int i = Y; i >= goY; i--) {
	                Y = i;
	                MYROBOT.mouseMove(X, Y);
	            }
	        }
	    } else if (goX > X) {
	        for (int i = 0; i <= (goX - initX); i++) {
	            X = initX + i;
	            Y = initY + ((i * (goY - initY)) / (goX - initX));
	            MYROBOT.mouseMove(X, Y);
	        }
	
	    } else {
	        for (int i = 0; i >= (goX - initX); i--) {
	            X = initX + i;
	            Y = initY + ((i * (goY - initY)) / (goX - initX));
	            MYROBOT.mouseMove(X, Y);
	        }
	    }
	}
	
	void getRGB(Color pixel) {
	    RED = pixel.getRed();
	    GREEN = pixel.getGreen();
	    BLUE = pixel.getBlue();
	    System.out.println(RED + "," + GREEN + "," + BLUE);
	}
	
	void savePoint() {
	    savedX = X;
	    savedY = Y;
	}
	
	void captureScreen() {
	    System.out.println("Capturing...");
	    SCREEN = new Color[SCREEN_X][SCREEN_Y];
	    for (int i = 0; i < SCREEN_X; i++) {
	        for (int j = 0; j < SCREEN_Y / 2; j++) {
	            SCREEN[i][j] = MYROBOT.getPixelColor(i, j);
	        }
	    }
	    System.out.println("Capturing done");
	}
	
	void gotoPixel(Color pixel) {
	    if (MYROBOT.getPixelColor(X, Y).getRGB() != pixel.getRGB()) {
	        for (int i = SCREEN_X - 4; i >= 0; i--) {
	            for (int j = 3; j < SCREEN_Y / 2; j++) {
	                if ((MYROBOT.getPixelColor(i, j).getRGB() == pixel.getRGB())) {
	                    gotoxy(i, j);
	                    i = -1;
	                    break;
	                }
	            }
	        }
	    }
	    if (MYROBOT.getPixelColor(X, Y).getRGB() == pixel.getRGB()) {
	        while (MYROBOT.getPixelColor(X, Y).getRGB() == pixel.getRGB()) {
	            gotoxy((X - 1), Y);
	        }
	    }
	}
	
	void emergeFromPixel(Color pixel) {
	    if (MYROBOT.getPixelColor(X, Y).getRGB() != pixel.getRGB()) {
	        for (int i = SCREEN_X - 4; i >= 0; i--) {
	            for (int j = 3; j < SCREEN_Y / 2; j++) {
	                if ((MYROBOT.getPixelColor(i, j).getRGB() == pixel.getRGB())) {
	                    MYROBOT.mouseMove(i, j);
	                    X = i;
	                    Y = j;
	                    i = -1;
	                    break;
	                }
	            }
	        }
	    }
	}
	
	void shift(char dir) {
	    int initX = X;
	    Color initPixel;
	    dir = Character.toUpperCase(dir);
	    switch (dir) {
	        case 'L': {
	            gotoxy((X - 8), Y);
	            initPixel = MYROBOT.getPixelColor(X, Y);
	            while (MYROBOT.getPixelColor(X, Y).getRGB() == initPixel.getRGB()) {
	                gotoxy((X - 1), Y);
	            }
	            initX = X;
	            for (int i = initX; i > initX - 20; i--) {
	                if (MYROBOT.getPixelColor(i, Y).getRGB() == initPixel.getRGB()) {
	                    gotoxy(i, Y);
	                    while (MYROBOT.getPixelColor(X, Y).getRGB() == initPixel.getRGB()) {
	                        gotoxy((X - 1), Y);
	                    }
	                    break;
	                }
	            }
	            break;
	        }
	        case 'R': {
	            initPixel = MYROBOT.getPixelColor(X, Y);
	            while (MYROBOT.getPixelColor(X, Y).getRGB() == initPixel.getRGB()) {
	                gotoxy((X + 1), Y);
	            }
	            initX = X;
	            for (int i = initX; i < initX + 20; i++) {
	                if (MYROBOT.getPixelColor(i, Y).getRGB() == initPixel.getRGB()) {
	                    gotoxy(i, Y);
	                    while (MYROBOT.getPixelColor(X, Y).getRGB() == initPixel.getRGB()) {
	                        gotoxy((X + 1), Y);
	                    }
	                    break;
	                }
	            }
	            gotoxy((X + 8), Y);
	            break;
	        }
	    }
	}
	
	void emergeFromButton(String butName) {
	    int tempDelay = TYPE_DELAY;
	    TYPE_DELAY = 0;
	    MYROBOT.setAutoDelay(TYPE_DELAY);
	    butName = butName.toUpperCase();
	    if (butName.equals("CLOSE")) {
	        emergeFromPixel(PIX_CLOSE_ACTIVE_OFF_MOUSE);
	    } else if (butName.equals("MAXIMIZE")) {
	        emergeFromPixel(PIX_CLOSE_ACTIVE_OFF_MOUSE);
	        shift('L');
	    } else if (butName.equals("MINIMIZE")) {
	        emergeFromPixel(PIX_CLOSE_ACTIVE_OFF_MOUSE);
	        shift('L');
	        shift('L');
	    } else if (butName.equals("START")) {
	        MYROBOT.mouseMove(20, SCREEN_Y - 10);
	    }
	    TYPE_DELAY = tempDelay;
	    MYROBOT.setAutoDelay(TYPE_DELAY);
	}
	
	void gotoButton(String butName) {
	    butName = butName.toUpperCase();
	    if (butName.equals("CLOSE")) {
	        if (MYROBOT.getPixelColor(X, Y).getRGB() != PIX_CLOSE_ACTIVE_ON_MOUSE.getRGB()) {
	            gotoPixel(PIX_CLOSE_ACTIVE_OFF_MOUSE);
	        }
	    } else if (butName.equals("MAXIMIZE")) {
	        gotoPixel(PIX_CLOSE_ACTIVE_OFF_MOUSE);
	        shift('L');
	    } else if (butName.equals("MINIMIZE")) {
	        gotoPixel(PIX_CLOSE_ACTIVE_OFF_MOUSE);
	        shift('L');
	        shift('L');
	    } else if (butName.equals("START")) {
	        gotoxy(20, SCREEN_Y - 10);
	    }
	}
	
	void catchTitleBar() {
	    gotoButton("CLOSE");
	    gotoxy(X - 150, Y);
	}
	
	void pressKey(int keyCode) {
		manuPress(keyCode);
	}
	
	void pressKey(String keyName) {
	    keyName = keyName.toUpperCase();
	    if (keyName.equals("ESC")) {
	        manuPress(27);
	    }
	}
	
	void closeAllWindows() {
	    while ((MYROBOT.getPixelColor(65, SCREEN_Y - 35).getRed() > 213) && (MYROBOT.getPixelColor(65, SCREEN_Y - 35).getGreen() > 220) && (MYROBOT.getPixelColor(65, SCREEN_Y - 35).getBlue() > 228)) {
	        altTab();
	        gotoButton("CLOSE");
	        sleep(100);
	        click();
	        sleep(50);
	    }
	}
	
	void quickCloseAllWindows() {
	    while ((MYROBOT.getPixelColor(65, SCREEN_Y - 35).getRed() > 213) && (MYROBOT.getPixelColor(65, SCREEN_Y - 35).getGreen() > 220) && (MYROBOT.getPixelColor(65, SCREEN_Y - 35).getBlue() > 228)) {
	        winD();
	        sleep(100);
	        altTab();
	        altF4();
	        type("n");
	    }
	}
	
	void getNotepad() {
	    int saveDelay = TYPE_DELAY;
	    TYPE_DELAY = 0;
	    MYROBOT.setAutoDelay(0);
	    MYROBOT.keyPress(524);
	    MYROBOT.keyPress(82);
	    MYROBOT.keyRelease(82);
	    MYROBOT.keyRelease(524);
	    sleep(200);
	    typeln("notepad");
	    sleep(500);
	    TYPE_DELAY = saveDelay;
	    MYROBOT.setAutoDelay(TYPE_DELAY);
	}
	
	void closeActiveWindow() {
	    gotoButton("CLOSE");
	    click();
	    type("n");
	}
	
	void openMyComputer() {
	    gotoxy(50, 50);
	    doubleClick();
	}
	
	void lockMouse() {
	    Thread lockMouse = new Thread(new Runnable() {
	
	        @Override
	        public void run() {
	            while (true) {
	                MYROBOT.mouseMove(X, Y);
	                sleep(100);
	            }
	        }
	    });
	    lockMouse.start();
	}
}
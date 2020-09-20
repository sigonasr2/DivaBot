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
	public static JFrame FRAME;
	/*static String SONGNAMES[] = new String[] {"Yellow","The secret garden","Tell Your World","愛言葉","Weekender Girl","歌に形はないけれど","えれくとりっく・えんじぇぅ","神曲","カンタレラ","巨大少女","クローバー♣クラブ","恋スルVOC@LOID","桜ノ雨","39","深海シティアンダーグラウンド","深海少女","積乱雲グラフィティ","千年の独奏歌","ダブルラリアット","ハジメテノオト","初めての恋が終わる時","packaged","Palette","FREELY TOMORROW","from Y to Y","みくみくにしてあげる♪","メルト","モノクロ∞ブルースカイ","ゆめゆめ","16 -out of the gravity-","ACUTE","インタビュア","LOL -lots of laugh-","Glory 3usi9","soundless voice","ジェミニ","白い雪のプリンセスは","スキキライ","タイムマシン","Dear","DECORATOR","トリコロール・エア・ライン","Nostalogic","Hand in Hand","Fire◎Flower","ブラック★ロックシューター","メテオ","ワールドイズマイン","アマツキツネ","erase or zero","エレクトロサチュレイタ","on the rocks","からくりピエロ","カラフル×メロディ","Catch the Wave","キャットフード","サマーアイドル","shake it!","Just Be Friends","スイートマジック","SPiCa -39's Giving Day Edition-","番凩","テレカクシ思春期","天樂","どういうことなの！？","東京テディベア","どりーみんチュチュ","トリノコシティ","ネトゲ廃人シュプレヒコール","No Logic","ハイハハイニ","はじめまして地球人さん","＊ハロー、プラネット。 (I.M.PLSE-EDIT)","Hello, Worker","忘却心中","magnet","右肩の蝶","結ンデ開イテ羅刹ト骸","メランコリック","リモコン","ルカルカ★ナイトフィーバー","炉心融解","WORLD'S END UMBRELLA","アカツキアライヴァル","アゲアゲアゲイン","1925","え？あぁ、そう。","エイリアンエイリアン","ODDS&ENDS","君の体温","こっち向いて Baby","壊セ壊セ","39みゅーじっく！","サンドリヨン","SING&SMILE","スノーマン","DYE","なりすましゲンガー","ヒバナ","ヒビカセ","ブラックゴールド","ミラクルペイント","指切り","ありふれたせかいせいふく","アンハッピーリフレイン","大江戸ジュリアナイト","ゴーストルール","こちら、幸福安心委員会です。","孤独の果て -extend edition-","ジターバグ","Sweet Devil","砂の惑星","テオ","初音ミクの消失 -DEAD END-","秘密警察","妄想スケッチ","リンちゃんなう！","ローリンガール","ロキ","ロミオとシンデレラ","エンヴィキャットウォーク","骸骨楽団とリリア","サイハテ","ジグソーパズル","千本桜","ピアノ×フォルテ×スキャンダル","Blackjack","ぽっぴっぽー","裏表ラバーズ","Sadistic.Music∞Factory","デンパラダイム","二次元ドリームフィーバー","ネガポジ＊コンティニューズ","初音ミクの激唱","ワールズエンド・ダンスホール","ココロ","システマティック・ラヴ","Knife","二息歩行","PIANOGIRL","夢喰い白黒バク","ブレス・ユア・ブレス","恋は戦争","あなたの歌姫","Starduster","StargazeR","リンリンシグナル","Rosary Pale","多重未来のカルテット～QUARTET THEME～","LIKE THE WIND","AFTER BURNER",
			"ストロボナイツ","VOiCE","恋色病棟","ねこみみスイッチ","パラジクロロベンゼン","カラフル×セクシィ","劣等上等","Star Story","パズル","キップル・インダストリー","夢の続き","MEGANE","Change me"};*/
	/*static String FUTURETONESONGNAMES[] = new String[] {"Yellow","The secret garden","Tell Your World","愛言葉","Weekender Girl","歌に形はないけれど","えれくとりっく・えんじぇぅ","神曲","カンタレラ","巨大少女","クローバー♣クラブ","恋スルVOC@LOID","桜ノ雨","39","深海シティアンダーグラウンド","深海少女","積乱雲グラフィティ","千年の独奏歌","ダブルラリアット","ハジメテノオト","初めての恋が終わる時","packaged","Palette","FREELY TOMORROW","from Y to Y","みくみくにしてあげる♪","メルト","モノクロ∞ブルースカイ","ゆめゆめ","16 -out of the gravity-","ACUTE","インタビュア","LOL -lots of laugh-","Glory 3usi9","soundless voice","ジェミニ","白い雪のプリンセスは","スキキライ","タイムマシン","Dear","DECORATOR","トリコロール・エア・ライン","Nostalogic","Hand in Hand","Fire◎Flower","ブラック★ロックシューター","メテオ","ワールドイズマイン","アマツキツネ","erase or zero","エレクトロサチュレイタ","on the rocks","からくりピエロ","カラフル×メロディ","Catch the Wave","キャットフード","サマーアイドル","shake it!","Just Be Friends","スイートマジック","SPiCa -39's Giving Day Edition-","番凩","テレカクシ思春期","天樂","どういうことなの！？","東京テディベア","どりーみんチュチュ","トリノコシティ","ネトゲ廃人シュプレヒコール","No Logic","ハイハハイニ","はじめまして地球人さん","＊ハロー、プラネット。 (I.M.PLSE-EDIT)","Hello, Worker","忘却心中","magnet","右肩の蝶","結ンデ開イテ羅刹ト骸","メランコリック","リモコン","ルカルカ★ナイトフィーバー","炉心融解","WORLD'S END UMBRELLA","アカツキアライヴァル","アゲアゲアゲイン","1925","え？あぁ、そう。","エイリアンエイリアン","ODDS&ENDS","君の体温","こっち向いて Baby","壊セ壊セ","39みゅーじっく！","サンドリヨン","SING&SMILE","スノーマン","DYE","なりすましゲンガー","ヒバナ","ヒビカセ","ブラックゴールド","ミラクルペイント","指切り","ありふれたせかいせいふく","アンハッピーリフレイン","大江戸ジュリアナイト","ゴーストルール","こちら、幸福安心委員会です。","孤独の果て -extend edition-","ジターバグ","Sweet Devil","砂の惑星","テオ","初音ミクの消失 -DEAD END-","秘密警察","妄想スケッチ","リンちゃんなう！","ローリンガール","ロキ","ロミオとシンデレラ","エンヴィキャットウォーク","骸骨楽団とリリア","サイハテ","ジグソーパズル","千本桜","ピアノ×フォルテ×スキャンダル","Blackjack","ぽっぴっぽー","裏表ラバーズ","Sadistic.Music∞Factory","デンパラダイム","二次元ドリームフィーバー","ネガポジ＊コンティニューズ","初音ミクの激唱","ワールズエンド・ダンスホール","ココロ","システマティック・ラヴ","Knife","二息歩行","PIANOGIRL","夢喰い白黒バク","ブレス・ユア・ブレス","恋は戦争","あなたの歌姫","Starduster","StargazeR","リンリンシグナル","Rosary Pale","多重未来のカルテット～QUARTET THEME～","LIKE THE WIND","AFTER BURNER",
	"ストロボナイツ","VOiCE","恋色病棟","ねこみみスイッチ","パラジクロロベンゼン","カラフル×セクシィ","劣等上等","Star Story","パズル","キップル・インダストリー","夢の続き","MEGANE","Change me"};*/
	static SongInfo SONGNAMES[] = new SongInfo[] {};
	static String NEWSONGS[] = new String[] {};
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
    public static long smallestSongColor = Long.MAX_VALUE;
    public static int[] firstTwentyPixels = new int[20];
    
    int lastcool=-1,lastfine=-1,lastsafe=-1,lastsad=-1,lastworst=-1,lastcombo=-1,lastscore=-1;
    float lastpercent;
    boolean lastfail;
    long lastSongSelectTime = System.currentTimeMillis();
    
    static TypeFace2 typeface1,typeface2,typeface3; 
    static Thread t = null;
    
    String prevSongTitle = "";
    String prevDifficulty = "";
    boolean eyeTrackingSceneOn=true;
    boolean recordingResults=false;
    long lastReportedEyeTrackingTime = System.currentTimeMillis();
    
    boolean overlayHidden=false;
    static boolean onSongSelect=false;
    static int stillOnSongSelect=0;
    static BufferedImage finishbutton = null; 
    static Dimension screenSize = new Dimension(0,0);
    static boolean dragging = false;
    static String CALIBRATIONSTATUS = "";
    static boolean calibrating=false;
    static Rectangle calibrationline = null;
    static boolean repaintCalled = false;
    public static Overlay OVERLAY;
    public static boolean CALIBRATION_MODE=false;
    public static boolean DEBUG_MODE=false;
    public static ColorPanel CP;
    public static DisplayManager DM;
    public static ReloadSong AO;
    public static boolean FUTURETONE = false;
    public static boolean LASTMODE_FUTURETONE = false;
    public static String USERNAME = "";
    public static String AUTHTOKEN = "";
    
    public static long lastmainlooptime = 0;
    public static Result lastData=null;
    public static int screen=0;
    
	
	public static void main(String[] args) throws JSONException, IOException, FontFormatException {
		if (args.length>0) {
			if (args[0].equalsIgnoreCase("calibrate")) {
				File f = new File("screenConfig.txt");
				if (f.exists()) {
					String[] data= FileUtils.readFromFile("screenConfig.txt");
					try {
						screen=Integer.parseInt(data[0]);
					} catch (Exception e) {
						System.err.println("Could not read from screenConfig.txt. It's invalid data, consider deleting the file and run the program again.");
					}
				}
				CALIBRATION_MODE=true;
			}
			if (args[0].equalsIgnoreCase("debug")) {
				DEBUG_MODE=true;
				AuthenticateUser();
			}
		} else {
			AuthenticateUser();
		}
		JSONObject obj = FileUtils.readJsonFromUrl("http://www.projectdivar.com/songs");
		SONGNAMES = new SongInfo[JSONObject.getNames(obj).length];
		for (String key : JSONObject.getNames(obj)) {
			SONGNAMES[Integer.parseInt(key)-1] = new SongInfo(obj.getJSONObject(key));
		}
		finishbutton = ImageIO.read(new File("finish.png"));
	    new MyRobot().go();
	}

	private static void AuthenticateUser() throws IOException {
		File authentication_token = new File("authToken.txt");
		if (!authentication_token.exists()) {
			String enteredValue = "";
			String username = "";
			boolean success = false;
			do {
				username = "";
				enteredValue = JOptionPane.showInputDialog("First time boot!\n\nPlease login on http://www.projectdivar.com and paste your App Authentication Token here.\nThe App Authentication token is used to record your scores and verify who you are! Do not share it with others!!!", "XXXXX-XXXXX-XXXXX");
				if (enteredValue==null) {
					System.exit(0);
				}
				if (!enteredValue.isEmpty()) {
					do {
						username = JOptionPane.showInputDialog("Please enter your Project DivaR username:","");
					} while (username.isEmpty());
					if (username==null) {
						System.exit(0);
					}
					success = SendAuthenticationRequest(enteredValue, username);
				}
			} while (!success);
		} else {
			String[] data = FileUtils.readFromFile("authToken.txt");
			String username = data[0];
			String authenticationToken = data[1];
			boolean success = SendAuthenticationRequest(data[1], data[0]);
			if (!success) {
				authentication_token.delete();
				AuthenticateUser();
			}
		}
	}

	private static boolean SendAuthenticationRequest(String authenticationToken, String username) {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://projectdivar.com/authenticateuser");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("authenticationToken", authenticationToken));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
		    	if (result.equalsIgnoreCase("authentication success!")) {
		    		FileUtils.writetoFile(new String[] {username,authenticationToken,"The App Authentication token is used to record your scores and verify who you are! Do not share it with others!!!"}, "authToken.txt", false);
		    		USERNAME=username;
		    		AUTHTOKEN=authenticationToken;
		    		return true;
		    	} else {
		    		JOptionPane.showMessageDialog(null,"Authentication Failed!\n\nPlease check your credentials and try again!");
		    	}
		    	instream.close();
		    } catch (UnsupportedOperationException | IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	boolean textFailPixel(BufferedImage img) {
		Color failPixel = new Color(img.getRGB(0, 0));
		//System.out.println(failPixel);
		//r=128,g=5,b=232
		return failPixel.getRed()>=50 && failPixel.getRed()<=150 && failPixel.getGreen()>=50 && failPixel.getGreen()<=150 && failPixel.getBlue()>=50 && failPixel.getBlue()<=150;
	}
	
	void BotMain() {
		lastmainlooptime=System.currentTimeMillis();
		try {
			JSONObject obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/rating/"+USERNAME);
			p.lastRating = p.overallrating;
			if (obj.has("rating")) {
				p.overallrating = (int)obj.getDouble("rating");
				if (p.lastRating<p.overallrating) {p.ratingTime=System.currentTimeMillis();}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				RunMainLoop();
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	private void RunMainLoop() {
		try {
	if (CALIBRATION_MODE) {
		if (MyRobot.calibrating) {
			MyRobot.calibrating=false;
			Overlay.OVERLAY.setVisible(false);
			Thread.sleep(1000);
			Calibrator2 c = new Calibrator2();
		}
	} else {
		//ImageIO.write(MYROBOT.createScreenCapture(),"png",new File("testscreen.png"));
		if (checkSongSelect()) {
			if (!overlayHidden) {
				overlayHidden=true;
				MyRobot.p.repaint();
			}
			GetCurrentSong();
			GetCurrentDifficulty();
			recordedResults=false;
			if (selectedSong!=null && difficulty!=null) {
				if (!prevSongTitle.equalsIgnoreCase(selectedSong.title) || !prevDifficulty.equalsIgnoreCase(difficulty)) {
					System.out.println("On Song Select Screen: Current Song-"+selectedSong.title+" Diff:"+difficulty);
					p.pullData(selectedSong.title,difficulty);
					MyRobot.p.refreshAllLabels();
					prevSongTitle=selectedSong.title;
					prevDifficulty=difficulty;
					MyRobot.p.repaint();
					if (NEWSONGS.length==0) {
						MYROBOT.keyPress(KeyEvent.VK_CONTROL);
						MYROBOT.keyPress(KeyEvent.VK_SHIFT);
						MYROBOT.keyPress(KeyEvent.VK_F11);
						MYROBOT.keyRelease(KeyEvent.VK_F11);
						MYROBOT.keyRelease(KeyEvent.VK_SHIFT);
						MYROBOT.keyRelease(KeyEvent.VK_CONTROL);
					}
				}
			}
			lastSongSelectTime = System.currentTimeMillis();
		} else {
			if (overlayHidden) {
				overlayHidden=false;
				MyRobot.p.repaint();
			}
			if ((selectedSong!=null && difficulty!=null)) {
				if (OnResultsScreen() && !recordedResults && !recordingResults && results.size()==0) {
					lastSongSelectTime=System.currentTimeMillis();
					MYROBOT.setAutoDelay(0);
					if (NEWSONGS.length==0) {
						MYROBOT.keyPress(KeyEvent.VK_CONTROL);
						MYROBOT.keyPress(KeyEvent.VK_SHIFT);
						MYROBOT.keyPress(KeyEvent.VK_F12);
						MYROBOT.keyRelease(KeyEvent.VK_F12);
						MYROBOT.keyRelease(KeyEvent.VK_SHIFT);
						MYROBOT.keyRelease(KeyEvent.VK_CONTROL);
					}
					MYROBOT.refreshScoreScreen();
					ImageIO.write(MYROBOT.createScoreScreenCapture(),"png",new File("scoreimage.png"));
					File tmp = new File("tmp");
					if (tmp.exists()) {
						FileUtils.deleteFile(tmp);
					} else {
						tmp.mkdir();
					}
					try {
						final Result data = typeface1.getAllData(MYROBOT.createScoreScreenCapture());
						//ImageIO.write(MYROBOT.,"png",new File("test.png"));
						if (data.cool==-1 || data.fine==-1 || data.safe==-1 || data.sad==-1 || data.worst==-1 || data.percent<0f || data.percent>110f || data.combo==-1 || data.score==-1) {
							if (lastData==null || (lastData.cool!=data.cool || data.fine!=lastData.fine || data.safe!=lastData.safe || data.sad!=lastData.sad || data.worst!=lastData.worst || data.percent!=lastData.percent || data.combo!=lastData.combo || data.score!=lastData.score)) {
								System.out.println("Waiting for results to populate...");
								System.out.println(data);
							}
							if (lastData==null) {
								lastData=new Result("","",-1,-1,-1,-1,-1,-1f);
							}
							lastData.cool=data.cool;
							lastData.fine=data.fine;
							lastData.safe=data.safe;
							lastData.sad=data.sad;
							lastData.worst=data.worst;
							lastData.percent=data.percent;
							lastData.combo=data.combo;
							lastData.score=data.score;
						} else 
						if ((data.combo!=lastcombo || data.fail!=lastfail || data.cool!=lastcool || lastfine!=data.fine || lastsafe!=data.safe || lastsad!=data.sad || lastworst!=data.worst)
								&& data.score!=lastscore /*|| lastpercent!=percent*/){
							//System.out.println("Results for "+selectedSong.title+" "+difficulty+": "+data.cool+"/"+data.fine+"/"+data.safe+"/"+data.sad+"/"+data.worst+" "+data.percent+"%");
							
							System.out.println("Results for "+selectedSong.title+" "+data.difficulty+": "+data.display());
							File songFolder = new File(selectedSong.title+"/"+data.difficulty);
							if (!songFolder.exists()) {
								songFolder.mkdirs();
							}
							File[] songFolderFiles = songFolder.listFiles();
							int playId = songFolderFiles.length;
							final File playFolder = new File(selectedSong.title+"/"+data.difficulty+"/"+playId);
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
							File resultImage=new File(playFolder,selectedSong.title+"_"+data.difficulty+"play_"+data.cool+"_"+data.fine+"_"+data.safe+"_"+data.sad+"_"+data.worst+"_"+data.percent+""
									+ "_"+data.combo+"_"+data.score+".png");
							new File("scoreimage.png").renameTo(resultImage);
							results.add(new Result(selectedSong.title,data.difficulty,data.cool,data.fine,data.safe,data.sad,data.worst,data.percent,data.mod,data.combo,data.score,data.fail,resultImage));
							SoundUtils.playSound("collect_item.wav");
							
							//gotoxy(800,64);
							//click();
							MYROBOT.setAutoDelay(0);
							if (NEWSONGS.length==0) {
								MYROBOT.keyPress(KeyEvent.VK_CONTROL);
								MYROBOT.keyPress(KeyEvent.VK_SHIFT);
								MYROBOT.keyPress(KeyEvent.VK_F11);
								MYROBOT.keyRelease(KeyEvent.VK_F11);
								MYROBOT.keyRelease(KeyEvent.VK_SHIFT);
								MYROBOT.keyRelease(KeyEvent.VK_CONTROL);
							}
						}
					} catch (IOException|NumberFormatException|IndexOutOfBoundsException e) {
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
				} else {
					if (results.size()>0) {
						recordingResults=true;
						for (final Result r  : results) {
							r.songName=r.songName.equalsIgnoreCase("恋ノート")?"恋ノート////":r.songName.equalsIgnoreCase("雨のちSweetDrops")?"雨のちSweet*Drops":r.songName.equalsIgnoreCase("Equation+")?"Equation+**":r.songName.equalsIgnoreCase("PIANOGIRL")?"PIANO*GIRL":(r.songName.equalsIgnoreCase("16 -out of the gravity-"))?"1/6 -out of the gravity-":r.songName;
							HttpClient httpclient = HttpClients.createDefault();
							HttpPost httppost = new HttpPost("http://45.33.13.215:4501/submit");

							// Request parameters and other properties.
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("song", r.songName));
							params.add(new BasicNameValuePair("username", USERNAME));
							params.add(new BasicNameValuePair("authentication_token", AUTHTOKEN));
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
							
							JSONObject report = null;

							if (entity != null) {
							    try (InputStream instream = entity.getContent()) {
							    	Scanner s = new Scanner(instream).useDelimiter("\\A");
							    	String result = s.hasNext() ? s.next() : "";
							    	report=new JSONObject(result);
							    	instream.close();
							    } catch (UnsupportedOperationException | IOException e) {
									e.printStackTrace();
								}
							}
							
							final JSONObject finalReport=report;

							System.out.println("Submitting screenshot for "+r.f);
							Thread t = new Thread() {
								public void run() {
									HashMap<String,String> s = new HashMap<>();
									s.put("username",USERNAME);
									s.put("authentication_token",AUTHTOKEN);
									s.put("playid",Integer.toString(finalReport.getInt("id")));
									WebUtils.POSTimage("http://projectdivar.com/upload", r.f, s);
								}
							};
							t.start();
						}
						results.clear();

						try {
							JSONObject obj = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/rating/"+USERNAME);
							JSONObject obj2 = FileUtils.readJsonFromUrl("http://45.33.13.215:4501/bestplay/"+USERNAME+"/"+URLEncoder.encode(MyRobot.p.songname, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")+"/"+difficulty);
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
	}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean OnResultsScreen() throws IOException {
		//r.x-418, r.y-204
		/*ImageIO.write(MYROBOT.createScreenCapture(new Rectangle(31,230,40,40)),"png",new File("color1.png"));
		ImageIO.write(MYROBOT.createScreenCapture(new Rectangle(31,196,40,40)),"png",new File("color2.png"));
		ImageIO.write(MYROBOT.createScreenCapture(new Rectangle(483,256,40,40)),"png",new File("color3.png"));*/
		if (!FUTURETONE) {
			Color c1 = new Color(MYROBOT.createScreenCapture(new Rectangle(31,230,40,40)).getRGB(0, 0));
			Color c2 = new Color(MYROBOT.createScreenCapture(new Rectangle(31,196,40,40)).getRGB(0, 0));
			Color c3 = new Color(MYROBOT.createScreenCapture(new Rectangle(483,256,40,40)).getRGB(0, 0));
			//System.out.println(c1+"/"+c2+"/"+c3);
			return c1.getRed()>=240 && c1.getGreen()>=240 && c1.getBlue()>=240 && c2.getRed()>=7 && c2.getRed()<=60 && c2.getGreen()>=180 && c2.getGreen()<=250 && c2.getBlue()>=150 && c2.getBlue()<=240 &&
					c3.getRed()>=140 && c3.getRed()<=255 && c3.getGreen()>=140 && c3.getGreen()<=255 && c3.getBlue()>=110 && c3.getBlue()<=240;
		} else {
			BufferedImage img2 = ImageUtils.toBufferedImage(MYROBOT.currentScreen.getScaledInstance(1280 , 720, Image.SCALE_SMOOTH));
			Color ft_pixel1 = new Color(img2.getRGB(260, 38));
			Color ft_pixel2 = new Color(img2.getRGB(86, 38));
			return (ft_pixel1.getRed()<60&&ft_pixel1.getRed()>=0&&
					ft_pixel1.getGreen()<90&&ft_pixel1.getGreen()>20&&
					ft_pixel1.getBlue()<90&&ft_pixel1.getBlue()>20
				&&ft_pixel2.getRed()<60&&ft_pixel2.getRed()>=0&&
				ft_pixel2.getGreen()<90&&ft_pixel2.getGreen()>20&&
				ft_pixel2.getBlue()<90&&ft_pixel2.getBlue()>20);
		}
	}

	private void GetCurrentDifficulty() {
		if (FUTURETONE) {
			Color c = new Color(MYROBOT.createScreenCapture(new Rectangle(334,267,10,10)).getRGB(0, 0));
			//return c.getRed()==43 && c.getGreen()==88 && c.getBlue()==213;
			if (c.getRed()>30 && c.getRed()<140 && c.getBlue()>80 && c.getBlue()<160 && c.getGreen()<40) {
				difficulty="EXEX";
			} else 
			if (c.getRed()>50 && c.getRed()<160 && c.getBlue()<40 && c.getGreen()<40) {
				difficulty="EX";
			} else 
			if (c.getRed()>90 && c.getRed()<160 && c.getBlue()<40 && c.getGreen()>30 && c.getGreen()<120) {
				difficulty="H";
			} else 
			if (c.getRed()<40 && c.getBlue()<70 && c.getGreen()<140 && c.getGreen()>60) {
				difficulty="N";
			} else 
			if (c.getRed()<40 && c.getBlue()>70 && c.getBlue()<140 && c.getGreen()<100 && c.getGreen()>20) {
				difficulty="E";
			}
		} else {
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
	}
	private void GetCurrentSong() throws IOException {
		
		BufferedImage img=null;
		long r=0,g=0,b=0;
		if (!FUTURETONE) {
			img = ImageUtils.toCompatibleImage(MYROBOT.createScreenCapture(new Rectangle(630,80,580,380)));
			for (int i=0;i<580;i++) {
				for (int j=0;j<380;j++) {
					r+=Math.pow(new Color(img.getRGB(i,j),true).getRed(),2);
					g+=Math.pow(new Color(img.getRGB(i,j),true).getGreen(),2);
					b+=Math.pow(new Color(img.getRGB(i,j),true).getBlue(),2);
				}
			}
		} else {
			img = ImageUtils.toCompatibleImage(MYROBOT.createScreenCapture(new Rectangle(610,214,600,246)));
			for (int i=0;i<600;i++) {
				for (int j=0;j<246;j++) {
					r+=Math.pow(new Color(img.getRGB(i,j),true).getRed(),2);
					g+=Math.pow(new Color(img.getRGB(i,j),true).getGreen(),2);
					b+=Math.pow(new Color(img.getRGB(i,j),true).getBlue(),2);
				}
			}
		}
		selectedSong = SongData.compareData(r,g,b);
	}
	
	void go() throws FontFormatException, IOException {
	    initialize();      
        p = new DrawCanvas();  
	    DrawCanvas.loadConfig();
	    //gotoxy(100, 100);
	    SCREEN = new Color[SCREEN_X][SCREEN_Y];
	    long startTime = System.currentTimeMillis();
	    
	    SongData.loadSongsFromFile();
	    
		 System.setProperty("awt.useSystemAAFontSettings","on");
	    FRAME = new JFrame();
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	    				ImageIO.read(new File("typeface3.png")),
	    				ImageIO.read(new File("typeface4.png")),
	    				ImageIO.read(new File("typeface5.png"))
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
            	if (NEWSONGS.length>0) {
	            	try {
						MYROBOT.refreshScreen();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
	            	BufferedImage img = null;
	    			long totalr=0;
	    			long totalg=0;
	    			long totalb=0;
	            	try {
	            		if (FUTURETONE) {
	            			ImageIO.write(img=MYROBOT.createScreenCapture(new Rectangle(610,214,600,246)),"png",new File("test.png"));
	    	    			for (int i=0;i<600;i++) {
	    	    				for (int j=0;j<246;j++) {
	    	    					totalr+=Math.pow(new Color(img.getRGB(i,j),true).getRed(),2);
	    	    					totalg+=Math.pow(new Color(img.getRGB(i,j),true).getGreen(),2);
	    	    					totalb+=Math.pow(new Color(img.getRGB(i,j),true).getBlue(),2);
	    	    				}
	    	    			}
	            		} else {
	            			ImageIO.write(img=MYROBOT.createScreenCapture(new Rectangle(630,80,580,380)),"png",new File("test.png"));
	    	    			for (int i=0;i<580;i++) {
	    	    				for (int j=0;j<380;j++) {
	    	    					totalr+=Math.pow(new Color(img.getRGB(i,j),true).getRed(),2);
	    	    					totalg+=Math.pow(new Color(img.getRGB(i,j),true).getGreen(),2);
	    	    					totalb+=Math.pow(new Color(img.getRGB(i,j),true).getBlue(),2);
	    	    				}
	    	    			}
	            		}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	    			try {
						SongData.saveSongToFile(NEWSONGS[currentSong],totalr,totalg,totalb);
					} catch (IOException e2) {
						e2.printStackTrace();
					}
	    		    try {
						SongData.loadSongsFromFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
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
            }
         });
        if (CALIBRATION_MODE) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            FRAME.setUndecorated(true);
		    OVERLAY = new Overlay();
		    OVERLAY.setBounds(FRAME.getGraphicsConfiguration().getBounds());
		    OVERLAY.setOpaque(false);
		    FRAME.setAlwaysOnTop(true);
		    FRAME.addMouseListener(OVERLAY);
		    FRAME.addMouseMotionListener(OVERLAY);
	        screenSize=new Dimension(FRAME.getGraphicsConfiguration().getBounds().width,FRAME.getGraphicsConfiguration().getBounds().height);
	        //FRAME.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		    //f.add(p);
	        //System.out.println(f.getGraphicsConfiguration().getBounds().width+"/"+f.getGraphicsConfiguration().getBounds().height);
	        FRAME.setSize(FRAME.getGraphicsConfiguration().getBounds().width,FRAME.getGraphicsConfiguration().getBounds().height);
	        FRAME.add(OVERLAY);
	        FRAME.setBackground(new Color(0,0,0,0));
        } else {
    		if (DEBUG_MODE) {
    			RunTests();
    		}
    		FRAME.addComponentListener(p);
		    FRAME.addWindowListener(p);
		    FRAME.addMouseListener(p);
		    FRAME.addMouseMotionListener(p);
		    FRAME.addKeyListener(p);
		    if (DrawCanvas.configData.containsKey("WIDTH")&&DrawCanvas.configData.containsKey("HEIGHT")) {
		    	try {
		    		FRAME.setSize(
		    				Integer.parseInt(DrawCanvas.configData.get("WIDTH")),
		    				Integer.parseInt(DrawCanvas.configData.get("HEIGHT")));
		    	} catch (NumberFormatException e) {
			    	FRAME.setSize(640, 480);
		    	}
		    } else {
		    	FRAME.setSize(640, 480);
		    }
			CP = new ColorPanel();
			DM = new DisplayManager();
			AO = new ReloadSong();
        	FRAME.add(p);
        }
        FRAME.setIconImage(ImageIO.read(new File("cross.png")));
        FRAME.setVisible(true);
	    FRAME.setTitle("DivaBot");
	    title = new JTextField();
	    title.setSize(200,100);
	    title.setText((currentSong>=SONGNAMES.length)?"DONE!":SONGNAMES[currentSong].name);
	    
	    
	    //SongData s = SongData.getByTitle(SONGNAMES[currentSong].name);
	   
	    BotMain();
	}
	
	static void RunTests() throws IOException {
		
		selectedSong=new SongData("LIKE THE WIND",0,0,0);
		difficulty="H";

		RunTest("test56.png",405,105,17,8,41,63.72f,"EX","",109,453145,false,Mode.FUTURETONE);
		RunTest("test55.png",421,50,1,0,3,98.37f,"EXEX","",406,689821,false,Mode.FUTURETONE);
		RunTest("test54.png",448,129,20,6,35,74.89f,"EXEX","",247,678260,true,Mode.FUTURETONE);
		RunTest("test53.png",456,163,31,7,47,75.89f,"EXEX","",105,736989,false,Mode.FUTURETONE);
		RunTest("test52.png",430,75,7,2,16,85.70f,"EX","",181,499246,false,Mode.FUTURETONE);
		RunTest("test51.png",406,45,1,0,2,102.30f,"EXEX","",444,592515,false,Mode.FUTURETONE);
		RunTest("test50.png",241,74,12,3,23,46.73f,"EXEX","",87,289190,true,Mode.FUTURETONE);
		RunTest("test49.png",516,37,0,0,8,97.08f,"EXEX","HS",258,839135,false,Mode.FUTURETONE);
		RunTest("test48.png",481,132,18,6,35,71.92f,"EX","HS",89,467720,false,Mode.FUTURETONE);
		RunTest("test47.png",298,34,0,0,0,103.39f,"EXEX","HS",332,463628,false,Mode.FUTURETONE);
		RunTest("test1.jpg",393,127,28,10,48,72.28f,"EXEX","",85,577160,false,Mode.MEGAMIX);
		RunTest("test2.jpg",518,144,17,3,23,81.94f,"H","",98,580090,false,Mode.MEGAMIX);
		RunTest("test3.jpg",646,54,1,0,0,103.06f,"EX","",691,666410,false,Mode.MEGAMIX);
		RunTest("test4.jpg",518,64,0,0,0,102.57f,"H","",582,470770,false,Mode.MEGAMIX);
		RunTest("test5.jpg",276,58,3,0,0,89.64f,"E","",215,238240,false,Mode.MEGAMIX);
		RunTest("test6.jpg",448,129,17,7,42,79.22f,"EXEX","",90,760770,false,Mode.MEGAMIX);
		RunTest("test7.jpg",419,227,28,7,20,75.76f,"EX","",154,473340,false,Mode.MEGAMIX);
		RunTest("test8.jpg",567,26,0,0,0,104.31f,"EX","",593,569500,false,Mode.MEGAMIX);
		RunTest("test9.jpg",197,51,0,0,0,100.02f,"H","",248,194700,false,Mode.MEGAMIX);
		RunTest("test10.jpg",486,245,46,22,59,65.34f,"H","SD",89,495850,false,Mode.MEGAMIX);
		RunTest("test11.jpg",0,0,0,0,159,0.00f,"EX","SD",0,0,true,Mode.MEGAMIX);
		RunTest("test12.jpg",0,0,0,0,79,0.08f,"EX","HD",0,580,true,Mode.MEGAMIX);
		RunTest("test13.jpg",245,19,4,0,2,87.04f,"E","",103,179360,false,Mode.MEGAMIX);
		RunTest("test14.png",623,39,1,0,0,100.83f,"EXEX","HS",350,869180,false,Mode.MEGAMIX);
		RunTest("test15.png",540,57,1,0,3,98.05f,"EX","HS",225,602000,false,Mode.MEGAMIX);
		RunTest("test16.png",320,46,2,0,4,93.26f,"EXEX","HS",135,463170,false,Mode.MEGAMIX);
		RunTest("test17.png",431,30,3,0,3,100.51f,"EXEX","HS",386,581700,false,Mode.MEGAMIX);
		RunTest("test18.png",427,86,5,1,4,92.45f,"EX","HS",136,526740,false,Mode.MEGAMIX);
		RunTest("test19.png",4,2,2,0,95,0.42f,"EXEX","HS",2,4130,true,Mode.MEGAMIX);
		RunTest("test20.png",3,1,1,2,58,0.75f,"EX","HS",2,7810,true,Mode.MEGAMIX);
		RunTest("test21.png",13,19,15,6,41,2.16f,"EX","HS",5,17860,true,Mode.MEGAMIX);
		RunTest("test22.png",49,37,21,15,26,4.68f,"EX","HS",10,42210,true,Mode.MEGAMIX);
		RunTest("test23.png",10,20,10,7,72,3.85f,"EXEX","HS",11,20050,true,Mode.MEGAMIX);
		RunTest("test24.png",35,29,19,22,11,7.85f,"N","HD",17,29600,true,Mode.MEGAMIX);
		RunTest("test25.png",175,75,13,10,32,50.84f,"E","SD",26,122670,false,Mode.MEGAMIX);
		RunTest("test26.png",29,24,6,7,35,3.12f,"EXEX","HS",26,28100,true,Mode.MEGAMIX);
		RunTest("test27.png",45,35,8,6,22,7.00f,"EX","HS",27,69780,true,Mode.MEGAMIX);
		RunTest("test28.png",463,139,10,2,50,72.96f,"EXEX","HS",99,479170,false,Mode.MEGAMIX);
		RunTest("test29.png",354,112,4,3,43,67.73f,"EXEX","HS",55,331060,true,Mode.MEGAMIX);
		RunTest("test30.png",390,90,8,9,22,74.95f,"N","HS",82,326560,false,Mode.MEGAMIX);
		RunTest("test31.png",329,69,8,1,34,72.15f,"EX","HS",40,358760,false,Mode.MEGAMIX);
		//RunTest("test32.png",0,1,1,0,57,0.57f,"EX","HS",1,1890,true,Mode.MEGAMIX);
		//RunTest("test33.png",181,84,10,2,5,71.04f,"E","",149,157020,false,Mode.MEGAMIX);
		//RunTest("test34.png",28,10,0,0,25,3.66f,"EX","",20,26790,true,Mode.MEGAMIX);
		RunTest("testimage.png",371,40,3,4,3,97.63f,"EX","HS",233,523750,false,Mode.MEGAMIX);
		RunTest("testimage2.png",942,71,1,0,3,97.02f,"EXEX","",714,951020,false,Mode.MEGAMIX);
		RunTest("testimage3.png",546,52,0,0,0,101.77f,"EX","",598,567430,false,Mode.MEGAMIX);
		RunTest("testimage4.png",279,81,16,2,3,75.40f,"N","",85,228100,false,Mode.MEGAMIX);
		RunTest("testimage5.png",276,184,6,1,11,82.16f,"EXEX","HS",78,549870,false,Mode.MEGAMIX);
		RunTest("testimage6.png",455,60,2,0,8,93.48f,"EXEX","HS",272,532470,false,Mode.MEGAMIX);
		RunTest("testimage7.png",452,128,8,2,16,88.28f,"EXEX","HS",146,773150,false,Mode.MEGAMIX);
		RunTest("testimage8.png",229,38,2,0,13,83.25f,"EXEX","HS",65,300120,false,Mode.MEGAMIX);
		RunTest("testimage9.png",413,70,1,0,21,82.66f,"EXEX","HS",94,443050,false,Mode.MEGAMIX);
		RunTest("16 -out of the gravity-.jpg",554,45,1,0,1,101.36f,"EX","HS",339,606780,false,Mode.MEGAMIX);
		RunTest("＊ハロー、プラネット。 (I.M.PLSE-EDIT).jpg",336,128,24,6,93,58.85f,"EX","",52,308760,true,Mode.MEGAMIX);
		
		RunTest("39.jpg",531,71,2,0,2,97.82f,"EXEX","HS",324,832390,false,Mode.MEGAMIX);
		RunTest("39みゅーじっく！.jpg",573,175,5,0,18,91.22f,"EX","HS",354,754140,false,Mode.MEGAMIX);
		RunTest("1925.jpg",510,115,14,7,22,77.79f,"EX","HS",85,564860,false,Mode.MEGAMIX);
		RunTest("ACUTE.jpg",478,64,1,1,5,95.76f,"EX","HS",197,505210,false,Mode.MEGAMIX);
		RunTest("AFTER BURNER.jpg",370,113,25,16,30,68.76f,"EX","HS",94,386390,true,Mode.MEGAMIX);
		RunTest("Blackjack.jpg",415,123,15,7,50,71.22f,"EX","HS",79,443260,false,Mode.MEGAMIX);
		RunTest("Catch the Wave.jpg",603,72,0,0,1,100.65f,"EX","HS",559,773570,false,Mode.MEGAMIX);
		RunTest("Dear.jpg",402,64,0,0,1,100.90f,"EXEX","HS",394,587740,false,Mode.MEGAMIX);
		RunTest("DECORATOR.jpg",436,100,1,0,6,93.52f,"EX","HS",217,560180,false,Mode.MEGAMIX);
		RunTest("DYE.jpg",530,106,7,2,13,84.77f,"EX","HS",143,486360,false,Mode.MEGAMIX);
		RunTest("erase or zero.jpg",442,70,0,0,2,100.12f,"EX","HS",265,731120,false,Mode.MEGAMIX);
		RunTest("FREELY TOMORROW.jpg",367,57,0,0,0,102.84f,"EX","HS",424,498640,false,Mode.MEGAMIX);
		RunTest("from Y to Y.jpg",350,49,6,1,8,86.35f,"EXEX","HS",139,427650,false,Mode.MEGAMIX);
		RunTest("Glory 3usi9.jpg",468,43,0,0,2,101.62f,"EX","HS",382,549780,false,Mode.MEGAMIX);
		RunTest("Hand in Hand.jpg",401,54,1,0,3,97.58f,"EX","HS",176,610040,false,Mode.MEGAMIX);
		RunTest("Hello, Worker.jpg",439,118,7,1,14,89.93f,"EXEX","HS",147,930290,false,Mode.MEGAMIX);
		RunTest("Just Be Friends.jpg",510,107,6,0,12,89.38f,"EXEX","HS",203,602080,false,Mode.MEGAMIX);
		RunTest("Knife.jpg",327,85,14,9,27,51.96f,"EX","HS",124,395170,true,Mode.MEGAMIX);
		RunTest("LIKE THE WIND.jpg",330,144,20,9,20,72.06f,"EX","HS",65,425970,false,Mode.MEGAMIX);
		RunTest("LOL -lots of laugh-.jpg",489,59,1,2,2,96.36f,"EX","HS",183,641920,false,Mode.MEGAMIX);
		RunTest("magnet.jpg",435,101,18,4,35,76.98f,"EXEX","HS",115,480540,false,Mode.MEGAMIX);
		RunTest("No Logic.jpg",491,101,11,5,15,86.32f,"EX","HS",186,476910,false,Mode.MEGAMIX);
		RunTest("Nostalogic.jpg",346,70,15,7,16,83.61f,"EX","HS",94,486030,false,Mode.MEGAMIX);
		RunTest("WORLD'S END UMBRELLA.jpg",437,136,6,1,3,90.59f,"H","",215,475120,false,Mode.MEGAMIX);
		RunTest("ぽっぴっぽー.jpg",350,46,7,6,3,80.39f,"N","",175,263630,false,Mode.MEGAMIX);
		RunTest("サマーアイドル.jpg",245,19,4,0,2,87.04f,"E","",103,179360,false,Mode.MEGAMIX);
		RunTest("test34.png",515,65,2,0,3,95.67f,"H","",294,512237,false,Mode.FUTURETONE);
		RunTest("test35.png",0,0,0,0,57,0.0f,"N","HD",0,0,true,Mode.FUTURETONE);
		RunTest("test36.png",0,0,0,0,57,0.0f,"N","SD",0,0,true,Mode.FUTURETONE);
		RunTest("test37.png",0,0,0,0,62,0.0f,"E","",0,0,true,Mode.FUTURETONE);
		RunTest("test38.jpg",345,49,6,2,2,94.53f,"H","",232,347990,false,Mode.MEGAMIX);
		RunTest("test39.jpg",556,127,5,0,4,92.63f,"H","",237,546040,false,Mode.MEGAMIX);
		RunTest("scoreimage36.png",312,36,0,1,0,101.60f,"H","",334,305679,false,Mode.FUTURETONE);
		RunTest("scoreimage155.png",527,91,2,2,5,91.63f,"H","",254,483780,false,Mode.FUTURETONE);
		RunTest("scoreimage255.png",581,211,31,9,54,65.89f,"EXEX","HS",149,501743,true,Mode.FUTURETONE);
		RunTest("scoreimage306.png",382,58,3,3,4,92.56f,"EXEX","HS",99,661705,false,Mode.FUTURETONE);
		RunTest("scoreimage314.png",342,88,14,5,24,80.85f,"EXEX","HS",62,625251,false,Mode.FUTURETONE);
		RunTest("scoreimage325.png",201,34,8,9,17,38.86f,"EXEX","HS",69,293062,true,Mode.FUTURETONE);
		RunTest("scoreimage340.png",449,35,0,0,2,100.94f,"EX","HS",202,648571,false,Mode.FUTURETONE);
		RunTest("scoreimage342.png",392,41,0,0,0,102.14f,"EXEX","HS",433,713988,false,Mode.FUTURETONE);
		RunTest("test40.png",221,117,7,1,8,78.00f,"H","HS",111,253033,false,Mode.FUTURETONE);
		RunTest("test41.png",329,108,3,0,17,80.10f,"EX","HS",113,419386,false,Mode.FUTURETONE);
		RunTest("test42.png",473,132,11,6,36,59.76f,"EXEX","",352,581856,true,Mode.FUTURETONE);
		RunTest("test43.png",7,4,3,2,60,3.14f,"EXEX","",4,15367,true,Mode.FUTURETONE);
		RunTest("test44.png",450,45,1,0,6,99.41f,"EXEX","HS",364,586674,false,Mode.FUTURETONE);
		RunTest("test45.png",394,37,0,0,0,103.84f,"EXEX","HS",431,546139,false,Mode.FUTURETONE);
		RunTest("test46.png",452,25,0,0,1,103.16f,"EXEX","HS",260,750134,false,Mode.FUTURETONE);
	}
	
	static void RunTest(String _img,int _cool,int _fine, int _safe, int _sad, int _worst, float _percent,String _difficulty,String _mod,int _combo,int _score,boolean _fail, Mode _mode) throws IOException {
		RunTest(_img,_cool,_fine,_safe,_sad,_worst,_percent,_difficulty,_mod,_combo,_score,_fail,_mode,false);
	}
	
	static void RunTest(String _img,int _cool,int _fine, int _safe, int _sad, int _worst, float _percent,String _difficulty,String _mod,int _combo,int _score,boolean _fail,Mode _mode,boolean debug) throws IOException {
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
		try {
			//assert data.isResult == true : "Expected a result screenshot.";
			assert data.mode == _mode : "Expected mode to be "+_mode+", got "+data.mode;
			assert data.cool == _cool : "Expected cool count to be "+_cool+", got "+data.cool;
			assert data.fine == _fine : "Expected fine count to be "+_fine+", got "+data.fine;
			assert data.safe == _safe : "Expected safe count to be "+_safe+", got "+data.safe;
			assert data.sad == _sad : "Expected sad count to be "+_sad+", got "+data.sad;
			assert data.worst == _worst : "Expected worst count to be "+_worst+", got "+data.worst;
			assert data.percent == _percent : "Expected percent to be "+_percent+", got "+data.percent;
			assert data.fail == _fail : "Expected fail to be "+_fail+", got "+data.fail;
			assert data.mod.equals(_mod) : "Expected mod to be "+_mod+", got "+data.mod;
			assert data.difficulty == _difficulty : "Expected difficulty to be "+_difficulty+", got "+data.difficulty;
			assert data.combo == _combo : "Expected combo to be "+_combo+", got "+data.combo;
			assert data.score == _score : "Expected score to be "+_score+", got "+data.score;
		} catch(AssertionError e) {
			System.err.println("\t"+e.getMessage()+" "+"("+(System.currentTimeMillis()-startTime)+"ms)!");
			if (!debug) {
				System.out.print("Automatically running in debug mode");
				FileUtils.deleteFile("debug");
				File f = new File("debug");
				f.mkdir();
				while (!f.exists()) {
					try {
						Thread.sleep(1000);
						f.mkdir();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				RunTest(_img,_cool,_fine,_safe,_sad,_worst,_percent,_difficulty,_mod,_combo,_score,_fail,_mode,true);
			}
			System.exit(1);
		}
		System.out.println("\tPassed ("+(System.currentTimeMillis()-startTime)+"ms)!");
	}
	
	public static boolean checkSongSelect() throws IOException {
		ColorRegion cr = new ColorRegion(MYROBOT.createScreenCapture(),new Rectangle(842,635,5,5));
		onSongSelect = cr.getAllRange(15,45,75,90,200,230);
		
		if (onSongSelect) {
			stillOnSongSelect++;
			FUTURETONE=false;			
			if (LASTMODE_FUTURETONE) {
				System.out.println("Switching to Megamix");
				SongData.loadSongsFromFile();
				LASTMODE_FUTURETONE=false;
			}
		} else
		{
			cr = new ColorRegion(MYROBOT.createScreenCapture(),new Rectangle(741,171,5,5));
			if (!onSongSelect&&cr.getAllRange(160,200,0,15,170,200)) {
				stillOnSongSelect++;
				FUTURETONE=true;
				onSongSelect=true;
				if (!LASTMODE_FUTURETONE) {
					System.out.println("Switching to Future Tone");
					SongData.loadSongsFromFile();
					LASTMODE_FUTURETONE=true;
				}
			} else {
				stillOnSongSelect=0;
			}
		}
		
		//777,179 FUTURE TONE
		return stillOnSongSelect>=5;
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
	    	e.printStackTrace();
	        JOptionPane.showOptionDialog(null, e.getMessage(), "Error", -1, 1, null, null, this);
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
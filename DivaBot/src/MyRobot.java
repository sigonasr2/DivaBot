import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import sig.utils.FileUtils;
import sig.utils.ImageUtils;
import sig.utils.SoundUtils;

public class MyRobot{
	Robot MYROBOT;
	Color SCREEN[][];
	static SongData SONGS[];
	static String SONGNAMES[] = new String[] {"Yellow","The secret garden","Tell Your World","愛言葉","Weekender Girl","歌に形はないけれど","えれくとりっく・えんじぇぅ","神曲","カンタレラ","巨大少女","クローバー♣クラブ","恋スルVOC@LOID","桜ノ雨","39","深海シティアンダーグラウンド","深海少女","積乱雲グラフィティ","千年の独奏歌","ダブルラリアット","ハジメテノオト","初めての恋が終わる時","packaged","Palette","FREELY TOMORROW","from Y to Y","みくみくにしてあげる♪","メルト","モノクロ∞ブルースカイ","ゆめゆめ","1/6 -out of the gravity-","ACUTE","インタビュア","LOL -lots of laugh-","Glory 3usi9","soundless voice","ジェミニ","白い雪のプリンセスは","スキキライ","タイムマシン","Dear","DECORATOR","トリコロール・エア・ライン","Nostalogic","Hand in Hand","Fire◎Flower","ブラック★ロックシューター","メテオ","ワールドイズマイン","アマツキツネ","erase or zero","エレクトロサチュレイタ","on the rocks","からくりピエロ","カラフル×メロディ","Catch the Wave","キャットフード","サマーアイドル","shake it!","Just Be Friends","スイートマジック","SPiCa -39's Giving Day Edition-","番凩","テレカクシ思春期","天樂","どういうことなの！？","東京テディベア","どりーみんチュチュ","トリノコシティ","ネトゲ廃人シュプレヒコール","No Logic","ハイハハイニ","はじめまして地球人さん","＊ハロー、プラネット。 (I.M.PLSE-EDIT)","Hello, Worker","忘却心中","magnet","右肩の蝶","結ンデ開イテ羅刹ト骸","メランコリック","リモコン","ルカルカ★ナイトフィーバー","炉心融解","WORLD'S END UMBRELLA","アカツキアライヴァル","アゲアゲアゲイン","1925","え？あぁ、そう。","エイリアンエイリアン","ODDS&ENDS","君の体温","こっち向いて Baby","壊セ壊セ","39みゅーじっく！","サンドリヨン","SING&SMILE","スノーマン","DYE","なりすましゲンガー","ヒバナ","ヒビカセ","ブラックゴールド","ミラクルペイント","指切り","ありふれたせかいせいふく","アンハッピーリフレイン","大江戸ジュリアナイト","ゴーストルール","こちら、幸福安心委員会です。","孤独の果て -extend edition-","ジターバグ","Sweet Devil","砂の惑星","テオ","初音ミクの消失","秘密警察","妄想スケッチ","リンちゃんなう！","ローリンガール","ロキ","ロミオとシンデレラ","エンヴィキャットウォーク","骸骨楽団とリリア","サイハテ","ジグソーパズル","千本桜","ピアノ×フォルテ×スキャンダル","Blackjack","ぽっぴっぽー","裏表ラバーズ","Sadistic.Music∞Factory","デンパラダイム","二次元ドリームフィーバー","ネガポジ＊コンティニューズ","初音ミクの激唱","ワールズエンド・ダンスホール","ココロ","システマティック・ラヴ","Knife","二息歩行","PIANOGIRL","夢喰い白黒バク"};
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
	final int WIDTH = 200;
	final int HEIGHT = 5;
    
    static int currentSong = 0;
    static SongData selectedSong = null;
    static String difficulty = "H"; //H=Hard EX=Extreme EXEX=Extra Extreme
    static boolean recordedResults=false;
    
    int lastcool,lastfine,lastsafe,lastsad,lastworst;
    float lastpercent;
    long lastSongSelectTime = System.currentTimeMillis();
    
    static TypeFace typeface1,typeface2; 
    
    boolean eyeTrackingSceneOn=true;
    boolean recordingResults=false;
    long lastReportedEyeTrackingTime = System.currentTimeMillis();
	
	public static void main(String[] args) {
	    new MyRobot().go();
	}
	
	boolean EyeTrackingIsOn() {
		//1888,760
		if (System.currentTimeMillis()-5000>lastReportedEyeTrackingTime) {
			BufferedImage img = MYROBOT.createScreenCapture(new Rectangle(1871,760,1,1));
			Color pixel = new Color(img.getRGB(0, 0));
			lastReportedEyeTrackingTime=System.currentTimeMillis();
			eyeTrackingSceneOn=pixel.getRed()<60 && pixel.getGreen()<60 && pixel.getBlue()<60;
		}
		return eyeTrackingSceneOn;
	}
	
	void BotMain() {
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						
						if (isOnSongSelect()) {
							GetCurrentSong();
							GetCurrentDifficulty();
							recordedResults=false;
							if (selectedSong!=null && difficulty!=null) {
								System.out.println("On Song Select Screen: Current Song-"+selectedSong.title+" Diff:"+difficulty);
							}
							lastSongSelectTime = System.currentTimeMillis();
						} else {
							/*selectedSong=new SongData("test",new Color[] {});
							difficulty="EXEX";*/
							if ((selectedSong!=null && difficulty!=null) /*|| true*/) {
								//Look for the results screen.
								//602,217 254,254,254
								//602,260 16,222,202
								//901,460 220-255,220-255,160-220
								
								if (OnResultsScreen() && !recordedResults && !recordingResults && results.size()==0) {
									lastSongSelectTime=System.currentTimeMillis();
									if (EyeTrackingIsOn()) {
										eyeTrackingSceneOn=false;
										gotoxy(800,64);
										click();
										gotoxy(1870,761);
										click();
									}
									//1885,761
								    //System.out.println(typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,451,115,26))));
								    //System.out.println(typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,484,115,26))));
								    //System.out.println(typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,518,115,26))));
								    //System.out.println(typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,553,115,26))));
								    //System.out.println(typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,583,115,26))));
								    //System.out.println(typeface2.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1428,361,128,30))));
									File tmp = new File("tmp");
									if (tmp.exists()) {
										FileUtils.deleteFile(tmp);
									} else {
										tmp.mkdir();
									}
									int cool = typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,451,115,26)),new File(tmp,"cool"));
									int fine = typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,484,115,26)),new File(tmp,"fine"));
									int safe = typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,518,115,26)),new File(tmp,"safe"));
									int sad = typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,553,115,26)),new File(tmp,"sad"));
									int worst = typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,583,115,26)),new File(tmp,"worst"));
									/*try {
										ImageIO.write(MYROBOT.createScreenCapture(new Rectangle(1235,583,115,26)),"png",new File("worst.png"));
									} catch (IOException e) {
										e.printStackTrace();
									}*/
									float percent = (float)typeface2.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1428,361,128,30)),new File(tmp,"percent"))/100f;
									if (cool==-1 || fine==-1 || safe==-1 || sad==-1 || worst==-1 || percent==-0.01f) {
										System.out.println("Waiting for results to populate...");
									} else 
									if (cool!=lastcool || lastfine!=fine || lastsafe!=safe || lastsad!=sad || lastworst!=worst /*|| lastpercent!=percent*/){
										System.out.println("Results for "+selectedSong.title+" "+difficulty+": "+cool+"/"+fine+"/"+safe+"/"+sad+"/"+worst+" "+percent+"%");
										File songFolder = new File(selectedSong.title+"/"+difficulty);
										if (!songFolder.exists()) {
											songFolder.mkdirs();
										}
										File[] songFolderFiles = songFolder.listFiles();
										int playId = songFolderFiles.length;
										File playFolder = new File(selectedSong.title+"/"+difficulty+"/"+playId);
										playFolder.mkdir();
										try {
											FileUtils.copyFileDir(new File(tmp,"cool"), new File(playFolder,"cool"));
											FileUtils.copyFileDir(new File(tmp,"fine"), new File(playFolder,"fine"));
											FileUtils.copyFileDir(new File(tmp,"safe"), new File(playFolder,"safe"));
											FileUtils.copyFileDir(new File(tmp,"sad"), new File(playFolder,"sad"));
											FileUtils.copyFileDir(new File(tmp,"worst"), new File(playFolder,"worst"));
											FileUtils.copyFileDir(new File(tmp,"percent"), new File(playFolder,"percent"));
											//FileUtils.deleteFile(tmp);
											ImageIO.write(MYROBOT.createScreenCapture(new Rectangle(418,204,1227,690)),"png",new File(playFolder,selectedSong.title+"_"+difficulty+"play_"+cool+"_"+fine+"_"+safe+"_"+sad+"_"+worst+"_"+percent+".png"));
										} catch (IOException e) {
											e.printStackTrace();
										}
										recordedResults=true;
										lastcool=cool;
										lastfine=fine;
										lastsafe=safe;
										lastsad=sad;
										lastworst=worst;
										lastpercent=percent;
										results.add(new Result(selectedSong.title,difficulty,cool,fine,safe,sad,worst,percent));
										SoundUtils.playSound("collect_item.wav");
										if (!EyeTrackingIsOn()) {
											eyeTrackingSceneOn=true;
											gotoxy(800,64);
											click();
											gotoxy(1870,761);
											click();
										}
									}
								} else {
									if (results.size()>0) {
										recordingResults=true;
										for (Result r  : results) {
											r.songName=r.songName.equalsIgnoreCase("PIANOGIRL")?"PIANO*GIRL":r.songName;
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
										/*MYROBOT.setAutoDelay(0);
										MYROBOT.keyPress(KeyEvent.VK_ALT);
										MYROBOT.keyPress(KeyEvent.VK_TAB);
										MYROBOT.keyRelease(KeyEvent.VK_ALT);
										MYROBOT.setAutoDelay(5000);
										MYROBOT.keyRelease(KeyEvent.VK_TAB);
										boolean first=true;
										for (Result r  : results) {
											if (!first) {
												MYROBOT.setAutoDelay(5000);
											} else {
												first=false;
												MYROBOT.setAutoDelay(100);
												TYPE_DELAY=50;
											}
											StringSelection selection = new StringSelection((r.songName.equalsIgnoreCase("PIANOGIRL"))?"PIANO*GIRL":r.songName);
										    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
										    clipboard.setContents(selection, selection);
											MYROBOT.keyPress(KeyEvent.VK_CONTROL);
											MYROBOT.keyPress(KeyEvent.VK_V);
											MYROBOT.keyRelease(KeyEvent.VK_V);
											MYROBOT.keyRelease(KeyEvent.VK_CONTROL);
											MYROBOT.setAutoDelay(100);
											MYROBOT.keyPress(KeyEvent.VK_TAB);
											type(Integer.toString(r.cool));
											MYROBOT.keyPress(KeyEvent.VK_TAB);
											type(Integer.toString(r.fine));
											MYROBOT.keyPress(KeyEvent.VK_TAB);
											type(Integer.toString(r.safe));
											MYROBOT.keyPress(KeyEvent.VK_TAB);
											type(Integer.toString(r.sad));
											MYROBOT.keyPress(KeyEvent.VK_TAB);
											type(Integer.toString(r.worst));
											MYROBOT.keyPress(KeyEvent.VK_TAB);
											type(r.difficulty);
											MYROBOT.keyPress(KeyEvent.VK_TAB);
											type(Float.toString(r.percent));
											MYROBOT.keyPress(KeyEvent.VK_TAB);
											MYROBOT.keyRelease(KeyEvent.VK_TAB);
											MYROBOT.setAutoDelay(0);
											MYROBOT.keyPress(KeyEvent.VK_CONTROL);
											MYROBOT.keyPress(KeyEvent.VK_ALT);
											MYROBOT.keyPress(KeyEvent.VK_SHIFT);
											MYROBOT.keyPress(KeyEvent.VK_1);
											MYROBOT.keyRelease(KeyEvent.VK_1);
											MYROBOT.keyRelease(KeyEvent.VK_CONTROL);
											MYROBOT.keyRelease(KeyEvent.VK_ALT);
											MYROBOT.keyRelease(KeyEvent.VK_SHIFT);
										}
										results.clear();
										sleep(1000);
										MYROBOT.setAutoDelay(0);
										MYROBOT.keyPress(KeyEvent.VK_ALT);
										MYROBOT.keyPress(KeyEvent.VK_TAB);
										MYROBOT.keyRelease(KeyEvent.VK_ALT);
										MYROBOT.setAutoDelay(250);
										MYROBOT.keyRelease(KeyEvent.VK_TAB);
										*/
										recordingResults=false;
									}
									if (!OnResultsScreen() && recordedResults) {
										recordedResults=false;
									}
								}
							}
						}
				    //572,453 
				    //Red: 100-200, Blue: 200-255 Purple (EXEX)
				    //Red: 150-255, Green: < 50 Blue: < 50 (EX)
				    //Red: 175-225, Green: 135-175 Blue: < 50 (Hard)
				}
					
					private boolean OnResultsScreen() {
						Color c1 = new Color(MYROBOT.createScreenCapture(new Rectangle(602,217,2,2)).getRGB(0, 0));
						Color c2 = new Color(MYROBOT.createScreenCapture(new Rectangle(602,260,2,2)).getRGB(0, 0));
						Color c3 = new Color(MYROBOT.createScreenCapture(new Rectangle(901,460,2,2)).getRGB(0, 0));
						return c1.getRed()>=254 && c1.getGreen()>=254 && c1.getBlue()>=254 && c2.getRed()==16 && c2.getGreen()==222 && c2.getBlue()==202 &&
								c3.getRed()>=220 && c3.getRed()<=255 && c3.getGreen()>=220 && c3.getGreen()<=255 && c3.getBlue()>=160 && c3.getBlue()<=220;
					}

					private void GetCurrentDifficulty() {
						Color c = new Color(MYROBOT.createScreenCapture(new Rectangle(653,459,10,10)).getRGB(5, 5));
						//return c.getRed()==43 && c.getGreen()==88 && c.getBlue()==213;
						if (c.getRed()>100 && c.getRed()<200 && c.getBlue()>200 && c.getBlue()<255 && c.getGreen()<50) {
							difficulty="EXEX";
						} else 
						if (c.getRed()>150 && c.getRed()<255 && c.getBlue()<50 && c.getGreen()<50) {
							difficulty="EX";
						} else 
						if (c.getRed()>175 && c.getRed()<225 && c.getBlue()<50 && c.getGreen()<175 && c.getGreen()>135) {
							difficulty="H";
						}
					}
					private void GetCurrentSong() {
						BufferedImage img = MYROBOT.createScreenCapture(new Rectangle(460,426,WIDTH,HEIGHT));
						Color[] col = new Color[WIDTH*HEIGHT];
						for (int i=0;i<WIDTH;i++) {
							for (int j=0;j<HEIGHT;j++) {
								col[i*HEIGHT+j]=new Color(img.getRGB(i,j),true);
							}
						}
						SongData ss = SongData.compareData(col);
						if (ss!=null) {
							selectedSong = ss;
						}
					}
					
				},
                0,
                50);
	}
	
	void go() {
	    initialize();        
	    //gotoxy(100, 100);
	    SCREEN = new Color[SCREEN_X][SCREEN_Y];
	    long startTime = System.currentTimeMillis();
	    
	    SongData.loadSongsFromFile();
	    
	    //img.get
	    //System.out.println("Captured in "+(System.currentTimeMillis()-startTime)+"ms");
	    /*for (int i=0;i<200;i++) {
	    	for (int j=0;j<5;j++) {
	    		System.out.println(img.getRGB(i, j));
	    	}
	    }
	    System.out.println("Took "+(System.currentTimeMillis()-startTime)+"ms");*/
	    //System.out.println(Arrays.deepToString(SCREEN));
	    //460,426
	    //Screen = new Color[]
	    JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = (JPanel)f.getContentPane();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = p.getInputMap(condition);
        ActionMap actionMap = p.getActionMap();
        BufferedImage img1 = null;
        BufferedImage img2 = null;
        typeface1 = null;
        typeface2=null;
        try {
			 img1 = ImageIO.read(new File("typeface1.png"));
			 img2 = ImageIO.read(new File("typeface2.png"));
			 typeface1 = new TypeFace(img1);
			 typeface2 = new TypeFace(img2);
			 typeface2.green_minthreshold=typeface2.blue_minthreshold=100;
			 typeface2.green_maxthreshold=typeface2.blue_maxthreshold=200;
			 typeface2.darkFillCheck=false;
		} catch (IOException e1) {
			e1.printStackTrace();
		}

        /*inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "Press"); //DEBUG KEYS.
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "Identifier");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "Toggle");*/
        actionMap.put("Press", new AbstractAction() {
           @Override
           public void actionPerformed(ActionEvent e) {
       			BufferedImage img = MYROBOT.createScreenCapture(new Rectangle(460,426,WIDTH,HEIGHT));
       			Color[] col = new Color[WIDTH*HEIGHT];
       			for (int i=0;i<WIDTH;i++) {
       				for (int j=0;j<HEIGHT;j++) {
       					col[i*HEIGHT+j]=new Color(img.getRGB(i,j),true);
       				}
       			}
       			SongData.saveSongToFile(title.getText(),col);
       		    SongData.loadSongsFromFile();
       			title.setText((++currentSong>=SONGNAMES.length)?"DONE!":SONGNAMES[currentSong]);
        	   //System.out.println(title.getText());
           }
        });
        actionMap.put("Identifier", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
       			BufferedImage img = MYROBOT.createScreenCapture(new Rectangle(460,426,WIDTH,HEIGHT));
       			Color[] col = new Color[WIDTH*HEIGHT];
       			for (int i=0;i<WIDTH;i++) {
       				for (int j=0;j<HEIGHT;j++) {
       					col[i*HEIGHT+j]=new Color(img.getRGB(i,j),true);
       				}
       			}
        		SongData.compareData(col);
         	   //System.out.println(title.getText());
            }
         });
        actionMap.put("Toggle", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
       			currentSong++;
       			SongData s = SongData.getByTitle(SONGNAMES[currentSong]);
       		    BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT,
       		            BufferedImage.TYPE_INT_RGB);
       		    for (int i=0;i<WIDTH;i++) {
       		    	for (int j=0;j<HEIGHT;j++) {
       		    		bufferedImage.setRGB(i, j, s.songCode[i*HEIGHT+j].getRGB());
       		    	}
       		    }
       		    p.getGraphics().drawImage(bufferedImage, 0, 0, f);
            }
         });
	    //f.setVisible(true);
	    f.setSize(400, 400);
	    title = new JTextField();
	    title.setSize(200,100);
	    title.setText((currentSong>=SONGNAMES.length)?"DONE!":SONGNAMES[currentSong]);
	    SongData s = SongData.getByTitle(SONGNAMES[currentSong]);
	    /*BufferedImage bufferedImage = new BufferedImage(TypeFace.WIDTH, TypeFace.HEIGHT,
	            BufferedImage.TYPE_INT_RGB);
	    for (int i=0;i<TypeFace.WIDTH;i++) {
	    	for (int j=0;j<TypeFace.HEIGHT;j++) {
	    		bufferedImage.setRGB(i, j, typeface1.numbers[i*TypeFace.HEIGHT+j][3].getRGB());
	    	}
	    }
	    p.getGraphics().drawImage(bufferedImage, 0, 0, f);*/
	    //1205,451 Cool Number range  160x26
	    //1205,484 Fine Number range  160x26
	    //1205,518 Safe Number range  160x26
	    //1205,553 Sad Number range  160x26
	    //1205,583 Worst Number range  160x26
	    //1428,361 Percentage 128x30
	    //572,453 
	    //Red: 100-200, Blue: 200-255 Purple (EXEX)
	    //Red: 150-255, Green: < 50 Blue: < 50 (EX)
	    //Red: 175-225, Green: 135-175 Blue: < 50 (Hard)
	    //1255-824: Red:43 Green:88 Blue:213
	    //p.getGraphics().drawImage(MYROBOT.createScreenCapture(new Rectangle(1235,583,115,26)), 0, i+=26, f);
	    //System.out.println(typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,451,115,26))));
	    //System.out.println(typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,484,115,26))));
	    //System.out.println(typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,518,115,26))));
	    //System.out.println(typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,553,115,26))));
	    //System.out.println(typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,583,115,26))));
	    //System.out.println(typeface2.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1428,361,128,30))));
	    //p.getGraphics().drawImage(MYROBOT.createScreenCapture(new Rectangle(1205,484,160,26)), 0, i+=26, f);
	    //p.getGraphics().drawImage(MYROBOT.createScreenCapture(new Rectangle(1205,518,160,26)), 0, i+=26, f);
	    //p.getGraphics().drawImage(MYROBOT.createScreenCapture(new Rectangle(1205,553,160,26)), 0, i+=26, f);
	    //p.getGraphics().drawImage(MYROBOT.createScreenCapture(new Rectangle(1205,583,160,26)), 0, i+=26, f);
	    //p.getGraphics().drawImage(MYROBOT.createScreenCapture(new Rectangle(1428,361,128,30)), 0, i+=26, f);
	    
	    RunTests();
	    
	    BotMain();
	}
	
	void RunTests() {
		
		//418,204
		/*int cool = typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,451,115,26)),new File(tmp,"cool"));
		int fine = typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,484,115,26)),new File(tmp,"fine"));
		int safe = typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,518,115,26)),new File(tmp,"safe"));
		int sad = typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,553,115,26)),new File(tmp,"sad"));
		int worst = typeface1.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1235,583,115,26)),new File(tmp,"worst"));

		float percent = (float)typeface2.extractNumbersFromImage(MYROBOT.createScreenCapture(new Rectangle(1428,361,128,30)),new File(tmp,"percent"))/100f;*/
		
		selectedSong=new SongData("packaged",new Color[] {});
		difficulty="EXEX";
		RunTest("shake it!_EXplay_568_88_8_4_7_96.03.png",580,80,0,4,7,95.03f);
		RunTest("え？あぁ、そう。_EXEXplay_499_121_11_9_43_77.11.png",439,121,11,5,43,77.11f);
		RunTest("サマーアイドル_EXplay_959_56_19_5_10_81.32.png",363,58,15,5,10,84.32f);
		RunTest("テレカクシ思春期_EXplay_44_108_7_4_18_81.8.png",447,109,7,4,16,84.80f);
		RunTest("どういうことなの！？_EXplay_449_85_3_0_3_95.01.png",448,85,2,0,3,95.01f);
		RunTest("天樂_EXplay_361_58_9_4_11_92.67.png",351,56,8,4,11,92.67f);
		RunTest("番凩_EXEXplay_41_110_1_10_21_77.76.png",431,110,17,10,31,77.79f);
		RunTest("結ンデ開イテ羅刹ト骸_EXEXplay_47_123_10_5_46_74.19.png",471,123,10,5,46,74.19f);
	}
	
	void RunTest(String _img,int _cool,int _fine, int _safe, int _sad, int _worst, float _percent) {
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
		int cool = typeface1.extractNumbersFromImage(ImageUtils.cropImage(img,new Rectangle(1235-offset.x,451-offset.y,115,26)),new File(tmp,"cool"));
		int fine = typeface1.extractNumbersFromImage(ImageUtils.cropImage(img,new Rectangle(1235-offset.x,484-offset.y,115,26)),new File(tmp,"fine"));
		int safe = typeface1.extractNumbersFromImage(ImageUtils.cropImage(img,new Rectangle(1235-offset.x,518-offset.y,115,26)),new File(tmp,"safe"));
		int sad = typeface1.extractNumbersFromImage(ImageUtils.cropImage(img,new Rectangle(1235-offset.x,553-offset.y,115,26)),new File(tmp,"sad"));
		int worst = typeface1.extractNumbersFromImage(ImageUtils.cropImage(img,new Rectangle(1235-offset.x,583-offset.y,115,26)),new File(tmp,"worst"));
		float percent = (float)typeface2.extractNumbersFromImage(ImageUtils.cropImage(img,new Rectangle(1428-offset.x,361-offset.y,128,30)),new File(tmp,"percent"))/100f;
		
		assert cool == _cool : "Expected cool count to be "+_cool+", got "+cool;
		assert fine == _fine : "Expected fine count to be "+_fine+", got "+fine;
		assert safe == _safe : "Expected safe count to be "+_safe+", got "+safe;
		assert sad == _sad : "Expected sad count to be "+_sad+", got "+sad;
		assert worst == _worst : "Expected worst count to be "+_worst+", got "+worst;
		assert percent == _percent : "Expected percent to be "+_percent+", got "+percent;
		System.out.println(" Passed ("+(System.currentTimeMillis()-startTime)+"ms)!");
	}
	
	boolean isOnSongSelect() {
		Color c = new Color(MYROBOT.createScreenCapture(new Rectangle(1255,824,20,20)).getRGB(10, 10));
		return c.getRed()==43 && c.getGreen()==88 && c.getBlue()==213;
	}
	
	void initialize() {
	    grEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    grDevice = grEnv.getDefaultScreenDevice();
	    updateScreenInfo();
	    setKeyMap();
	    try {
	        MYROBOT = new Robot();
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
package sig;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import sig.utils.ImageUtils;
import sig.utils.TextUtils;

public class Display {
	Color backgroundCol=Color.BLUE;
	Color textCol=Color.WHITE;
	Font font=new Font("Dialog.bold",Font.PLAIN,32);
	int fontSize=32;
	Font modifiedfont=font;
	int x;
	int y;
	int modDisplayX=0;
	int width=200;
	int height=48;
	int fontHeight=0;
	int delay=10000;
	String header="";
	long nextUpdateTime = System.currentTimeMillis();
	boolean forceUpdate=false;
	String[] labels;
	String currentText;
	int cycle=0;
	boolean deleted=false;
	/**
	 * Load a display from a save formatted string.
	 * */
	Display(String loadString) {
		this();
		String[] split = loadString.split("\\*");
		backgroundCol=new Color(Integer.parseInt(split[0]));
		textCol=new Color(Integer.parseInt(split[1]));
		fontSize=Integer.parseInt(split[2]);
		font=new Font(split[3],Font.PLAIN,fontSize);
		width=Integer.parseInt(split[4]);
		height=Integer.parseInt(split[5]);
		delay=Integer.parseInt(split[6]);
		labels=split[7].split("\\|");
		x=Integer.parseInt(split[8]);
		y=Integer.parseInt(split[9]);
		if (split.length>=11) {
			header=split[10];
		}
	}
	Display() {
		HashMap<String,String> config = MyRobot.p.configData;
		if (config.containsKey("LAST_BACKGROUND")) {
			try {
				backgroundCol=new Color(Integer.parseInt(config.get("LAST_BACKGROUND")));
			} catch (NumberFormatException e) {
				backgroundCol=Color.BLUE;
			}
		}
		if (config.containsKey("LAST_TEXT")) {
			try {
				textCol=new Color(Integer.parseInt(config.get("LAST_TEXT")));
			} catch (NumberFormatException e) {
				textCol=Color.WHITE;
			}
		}
		if (config.containsKey("LAST_WIDTH")) {
			try {
				width=Integer.parseInt(config.get("LAST_WIDTH"));
			} catch (NumberFormatException e) {
				width=200;
			}
		}
		if (config.containsKey("LAST_HEIGHT")) {
			try {
				height=Integer.parseInt(config.get("LAST_HEIGHT"));
			} catch (NumberFormatException e) {
				height=48;
			}
		}
		x=0;
		y=0;
		if (config.containsKey("LAST_DELAY")) {
			try {
				delay=Integer.parseInt(config.get("LAST_DELAY"));
			} catch (NumberFormatException e) {
				delay=10000;
			}
		}
		cycle=0;
		if (config.containsKey("LAST_FONTSIZE")) {
			try {
				fontSize=Integer.parseInt(config.get("LAST_FONTSIZE"));
			} catch (NumberFormatException e) {
				fontSize=32;
			}
		}
		if (config.containsKey("LAST_FONT")) {
			try {
				font = new Font(config.get("LAST_FONT"),Font.PLAIN,fontSize);
			} catch (NumberFormatException e) {
				font = new Font("Batang",Font.PLAIN,fontSize);
			}
		}
		header = "";
		labels = new String[]{"Add a label!"};
		currentText=interpretLabel(labels[cycle]);
		Thread t = new Thread() {
			public void run() {
				try {
					while (!deleted) {
						if (System.currentTimeMillis()>nextUpdateTime) {
							if (!forceUpdate) {
								AdvanceCycle();
							}
							if (labels.length>0) {
								if (labels.length>cycle) {
									currentText=interpretLabel(labels[cycle]);
								} else {
									currentText=interpretLabel(labels[0]);
								}
							}
							updateFont();
							MyRobot.p.repaint();
							nextUpdateTime=System.currentTimeMillis()+delay;
							forceUpdate=false;
						}
						Thread.sleep(50);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			private void AdvanceCycle() {
				if (labels.length!=0) {
					cycle=(cycle+1)%labels.length;
					currentText=interpretLabel(labels[cycle]);
				}
			}
		};
		t.start();
	}
	
	public void updateFont() {
		int currentSize=fontSize;
		modifiedfont = font;
		Rectangle2D bounds = TextUtils.calculateStringBoundsFont(currentText, font);
		fontHeight = (int)bounds.getHeight();
		if (labels[cycle].equalsIgnoreCase("Best Play")) {
			BufferedImage scaledMiku=ImageUtils.toBufferedImage(DrawCanvas.mikuFC.getScaledInstance(-1, height, BufferedImage.SCALE_SMOOTH));
			modDisplayX = width-scaledMiku.getWidth();
		} else {
			modDisplayX = width;
		}
		while (currentSize>1&&bounds.getWidth()>modDisplayX) {
			currentSize-=2;
			if (currentSize<=1) {break;}
			modifiedfont = new Font(font.getFontName(),Font.PLAIN,currentSize);
			bounds = TextUtils.calculateStringBoundsFont(currentText, modifiedfont);
			fontHeight = (int)bounds.getHeight();
		}
		forceUpdate=true;
	}
	
	public void draw(Graphics g) {
		g.setColor(backgroundCol);
		g.fillRect(x, y, width, height);
		g.setColor(textCol);
		g.setFont(modifiedfont);
		DrawCanvas data = MyRobot.p;
		if (labels[cycle].equalsIgnoreCase("Best Play")) {
			if (data.bestPlay!=null) {
				if (data.bestPlay.safe+data.bestPlay.sad+data.bestPlay.worst==0) {
					if (height>1) {
						BufferedImage scaledMiku=ImageUtils.toBufferedImage(DrawCanvas.mikuFC.getScaledInstance(-1, height, BufferedImage.SCALE_SMOOTH));
						g.drawImage(scaledMiku,x,y+height-scaledMiku.getHeight(),MyRobot.p);
					}
					g.drawString(currentText,x+width-modDisplayX,y+height/2+fontHeight/4);
				} else {
					g.drawString(currentText,x,y+height/2+fontHeight/4);
				}
			} else {
				g.drawString(currentText,x,y+height/2+fontHeight/4);
			}
		} else {
			g.drawString(currentText,x,y+height/2+fontHeight/4);
		}
	}
	
	public String getSaveString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(backgroundCol.getRGB()).append("*")
				.append(textCol.getRGB()).append("*")
				.append(fontSize).append("*")
				.append(font.getFontName()).append("*")
				.append(width).append("*")
				.append(height).append("*")
				.append(delay).append("*")
				.append(combineLabels()).append("*")
				.append(x).append("*")
				.append(y).append("*")
				.append(header).append("*")
				.toString();
	}
	
	private String combineLabels() {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<labels.length;i++) {
			if (sb.length()>0) {
				sb.append("|");
			}
			sb.append(labels[i].replaceAll("\\|","").replaceAll("\\*",""));
		}
		return sb.toString();
	}

	private String interpretLabel(String string){
		DrawCanvas data = MyRobot.p;
		try {
			switch (string) {
				case "Best Play":{
					if (data.bestPlayTime>System.currentTimeMillis()-5000) {
						return "New Record!";
					} else {
						if (data.bestPlay!=null) {
							if (data.bestPlay.safe+data.bestPlay.sad+data.bestPlay.worst==0) {
								return header+data.bestPlay.display();
							} else {
								return header+((data.bestPlay!=null)?data.bestPlay.display():"No plays");
							}
						} else {
							return header+"No plays";
						}
					}
				}
				case "Overall Rating":{
					if (data.ratingTime>System.currentTimeMillis()-10000) {
						return "Rating up! "+data.lastRating+" -> "+data.overallrating;
					} else {
						return header+Integer.toString(data.overallrating);
					}
				}
				case "Song Difficulty":{
					return header+data.difficultyRating + " - " + fullNameDifficulty(data.difficulty);
				}
				case "Song Title (Japanese)":{
					return header+data.songname;
				}
				case "Song Title (Romanized)":{
					return header+((data.romanizedname.length()>0)?data.romanizedname:data.englishname);
				}
				case "Song Title (Japanese+Romanized)":{
					if (data.songname.equalsIgnoreCase(((data.romanizedname.length()>0)?data.romanizedname:data.englishname))) {
						return header+data.songname;
					} else {
						return header+(data.songname + " - " + ((data.romanizedname.length()>0)?data.romanizedname:data.englishname));
					}
				}
				case "Song Title (English)":{
					return header+data.englishname;
				}
				case "Song Title (Japanese+Romanized+ENG)":{
					if (data.songname.equalsIgnoreCase(((data.romanizedname.length()>0)?data.romanizedname:data.englishname))) {
						return header+data.songname;
					} else {
						return header+(data.songname + " - " + ((data.romanizedname.length()>0)?(data.romanizedname.equalsIgnoreCase(data.englishname))?data.romanizedname:data.romanizedname+" ("+data.englishname+")":data.englishname));
					}
				}
				case "Song Artist":{
					return header+data.artist;
				}
				case "Play Count":{
					if (data.plays>0) {
						return header+Integer.toString(data.plays)+" play"+((data.plays!=1)?"s":"");
					} else {
						return "No Plays";
					}
				}
				case "Pass/Play Count":{
					if (data.plays>0) {
						return header+Integer.toString(data.passes) + "/" + Integer.toString(data.plays)+" play"+((data.plays!=1)?"s":"");
					}
					 else {
						return "No Plays";
					}
				}
				case "Pass/Play Count (+%)":{
					if (data.plays>0) {
						return header+(data.passes)+"/"+(data.plays)+" play"+((data.plays!=1)?"s":"")+" "+"("+((int)(Math.floor(((float)data.passes)/data.plays*100)))+"% pass rate)";
					} else {
						return "No Plays";
					}
				}
				case "FC Count":{
					if (data.plays>0) {
						return header+header+data.fcCount +" FC"+(data.fcCount==1?"":"s");
					} else {
						return "No Plays";
					}
				}
				case "FC Count (+%)":{
					if (data.plays>0) {
						return header+data.fcCount +" FC"+(data.fcCount==1?"":"s")+"    "+((int)(Math.floor(((float)data.fcCount)/data.plays*100)))+"% FC rate";
					} else {
						return "No Plays";
					}
				}
				default:{
					return header+string;
				}
			}
		} catch(NullPointerException e) {
			return header+string;
		}
	}
	private String fullNameDifficulty(String difficulty) {
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
				return "EX Extreme";
			}
		}
		return "";
	}
}

package sig;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashMap;

public class Display {
	Color backgroundCol=Color.BLUE;
	Color textCol=Color.WHITE;
	Font font=new Font("Batang",Font.PLAIN,32);
	int fontSize=32;
	int x;
	int y;
	int width=200;
	int height=48;
	int delay=10000;
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
		labels = new String[]{"Add a label!"};
		currentText=interpretLabel(labels[cycle]);
		Thread t = new Thread() {
			public void run() {
				try {
					while (!deleted) {
						AdvanceCycle();
						MyRobot.p.repaint();
						Thread.sleep(delay);
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
	
	public void draw(Graphics g) {
		g.setColor(backgroundCol);
		g.fill3DRect(x, y, width, height, true);
		g.setColor(textCol);
		g.setFont(font);
		g.drawString(currentText,x,y+fontSize);
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
					if (data.bestPlayTime>System.currentTimeMillis()-10000) {
						return "New Record!";
					} else {
						return ((data.bestPlay!=null)?data.bestPlay.display():"");
					}
				}
				case "Overall Rating":{
					if (data.ratingTime>System.currentTimeMillis()-10000) {
						return "Rating up! "+data.lastRating+" -> "+data.overallrating;
					} else {
						return Integer.toString(data.overallrating);
					}
				}
				case "Song Difficulty":{
					return data.difficultyRating + " - " + fullNameDifficulty(data.difficulty);
				}
				case "Song Title (Japanese)":{
					return data.songname + " by "+data.artist;
				}
				case "Song Title (Romanized)":{
					return ((data.romanizedname.length()>0)?data.romanizedname:data.englishname) + " by "+data.artist;
				}
				case "Song Title (Japanese+Romanized)":{
					return (data.songname + " - " + ((data.romanizedname.length()>0)?data.romanizedname:data.englishname))  + " by "+data.artist;
				}
				case "Song Title (English)":{
					return data.englishname  + " by "+data.artist;
				}
				case "Song Title (Japanese+Romanized+ENG)":{
					return (data.songname + " - " + ((data.romanizedname.length()>0)?(data.romanizedname.equalsIgnoreCase(data.englishname))?data.romanizedname:data.romanizedname+" ("+data.englishname+")":data.englishname))  + " by "+data.artist;
				}
				case "Play Count":{
					return Integer.toString(data.plays)+" play"+((data.plays!=1)?"s":"");
				}
				case "Pass/Play Count":{
					return Integer.toString(data.passes) + "/" + Integer.toString(data.plays)+" play"+((data.plays!=1)?"s":"");
				}
				case "Pass/Play Count (+%)":{
					return (data.passes)+"/"+(data.plays)+" play"+((data.plays!=1)?"s":"")+" "+"("+((int)(Math.floor(((float)data.passes)/data.plays*100)))+"% pass rate)";
				}
				case "FC Count":{
					return data.fcCount +" FC"+(data.fcCount==1?"":"s");
				}
				case "FC Count (+%)":{
					return data.fcCount +" FC"+(data.fcCount==1?"":"s")+"    "+((int)(Math.floor(((float)data.fcCount)/data.plays*100)))+"% FC rate";
				}
				default:{
					return string;
				}
			}
		} catch(NullPointerException e) {
			return string;
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
				return "Extra Extreme";
			}
		}
		return "";
	}
}

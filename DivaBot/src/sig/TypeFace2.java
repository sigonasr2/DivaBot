package sig;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import sig.utils.FileUtils;
import sig.utils.ImageUtils;

public class TypeFace2 {
	BufferedImage img;
	BufferedImage font;
	BufferedImage percentfont;
	BufferedImage scorefont;
	int xpointer = 99;
	int ypointer = 0;
	public static final int THRESHOLD = 30000;
	
	public TypeFace2(BufferedImage font,BufferedImage percentfont,
			BufferedImage scorefont) {
		this.font=font;
		this.percentfont = percentfont;
		this.scorefont = scorefont;
		
		File debugdir = new File("debug");
		debugdir.mkdirs();
	}
	
	public Result getAllData(BufferedImage img) throws IOException {
		return getAllData(img,false);
	}
	
	final static int XOFFSET = 8;
	
	final static Rectangle RECT_SEARCH_COOL=new Rectangle(866+XOFFSET,260,100+XOFFSET,22);
	final static Rectangle RECT_SEARCH_FINE=new Rectangle(866+XOFFSET,294,100+XOFFSET,22);
	final static Rectangle RECT_SEARCH_SAFE=new Rectangle(866+XOFFSET,329,100+XOFFSET,22);
	final static Rectangle RECT_SEARCH_SAD=new Rectangle(866+XOFFSET,364,100+XOFFSET,22);
	final static Rectangle RECT_SEARCH_WORST=new Rectangle(866+XOFFSET,400,100+XOFFSET,22);
	final static Rectangle RECT_SEARCH_PCT=new Rectangle(1182+XOFFSET,165,1132,168);
	final static Rectangle RECT_SEARCH_PCT2=new Rectangle(1123+XOFFSET,165,1051,168);
	final static Rectangle RECT_SEARCH_SCORE=new Rectangle(859+XOFFSET,578,250+XOFFSET,32);
	final static Rectangle RECT_SEARCH_COMBO=new Rectangle(1010+XOFFSET,435,100+XOFFSET,20);

	public Result getAllData(BufferedImage img, boolean debug) throws IOException,NumberFormatException,IndexOutOfBoundsException {
		BufferedImage img2 = ImageUtils.toBufferedImage(img.getScaledInstance(1280 , 720, Image.SCALE_SMOOTH));
		Result result = new Result("","",-1,-1,-1,-1,-1,-1f);
		int[] finalNumbers = new int[5];
		
		Rectangle[] ranges = new Rectangle[] {
				//These coords are in regard to the old screenshot sizes.
				RECT_SEARCH_COOL, //33 pixels per line.
				RECT_SEARCH_FINE,
				RECT_SEARCH_SAFE,
				RECT_SEARCH_SAD,
				RECT_SEARCH_WORST,
		};
		
		for (int i=0;i<ranges.length;i++) {
			Rectangle r = ranges[i];

			//System.out.println("Image "+i+":");
			File temp = new File("rectangle"+i+".png");
			ImageIO.write(img2.getSubimage(r.x,r.y,r.width,r.height),"png",temp);
			
			finalNumbers[i]=extractNumbersFromImage(img2.getSubimage(
					r.x,r.y,r.width,r.height),debug);
			
		}
		result.cool = finalNumbers[0];
		result.fine = finalNumbers[1];
		result.safe = finalNumbers[2];
		result.sad = finalNumbers[3];
		result.worst = finalNumbers[4];
		
		float percent = extractPercentFromImage(img2,debug);
		
		result.percent=percent;
		// result.percent = ??
		
		
		//489,197
		Color failPixel = new Color(img2.getRGB(489, 197));
		result.fail = failPixel.getRed()<100&&failPixel.getGreen()<100&&failPixel.getBlue()<100;
		
		Color difficultyPixel = new Color(img2.getRGB(622, 110));
		result.difficulty = getDifficulty(difficultyPixel);
		
		result.mod = getMod(img2);
		
		//1109,435
		result.combo = extractNumbersFromImage(img2.getSubimage(
				RECT_SEARCH_COMBO.x,RECT_SEARCH_COMBO.y,RECT_SEARCH_COMBO.width,RECT_SEARCH_COMBO.height),debug);
		
		result.score = extractScoreNumbersFromImage(img2.getSubimage(
				RECT_SEARCH_SCORE.x,RECT_SEARCH_SCORE.y,RECT_SEARCH_SCORE.width,RECT_SEARCH_SCORE.height),debug);
		
		return result;
	}
	
	private String getMod(BufferedImage img2) {
		//1082,101 HS  R>125
		//1113,122 HD  G>100, R>125
		//1145,104 SD  G>100, B>125
		Color modPixel = new Color(img2.getRGB(1082, 101));
		if (modPixel.getRed()>125) {return "HS";}
		modPixel = new Color(img2.getRGB(1113, 122));
		if (modPixel.getGreen()>100&&modPixel.getRed()>125) {return "HD";}
		modPixel = new Color(img2.getRGB(1145, 104));
		if (modPixel.getGreen()>100&&modPixel.getBlue()>125) {return "SD";}
		return "";
	}

	private String getDifficulty(Color difficultyPixel) {
		String[] diffs = new String[] {"E","N","H","EX","EXEX"};
		Color[] cols = new Color[] {new Color(95,243,255),new Color(20,234,0),new Color(251,191,0),new Color(253,14,81),new Color(157,0,227),};
		int lowestDistance = Integer.MAX_VALUE;
		int lowestIndex = -1;
		for (int i=0;i<cols.length;i++) {
			int distance = (int)ImageUtils.distanceToColor(difficultyPixel, cols[i]);
			if (distance<lowestDistance) {
				lowestDistance = distance;
				lowestIndex=i;
			}
		}
		return diffs[lowestIndex];
	}

	public float extractPercentFromImage(BufferedImage img) throws IOException {
		return extractPercentFromImage(img,false);
	}
	
	public float extractPercentFromImage(BufferedImage img,boolean debug) throws IOException {
		//1180,167
		//second part: 1123
		String decimal = "";
		String integer = "";
		xpointer=RECT_SEARCH_PCT.x;
		ypointer=RECT_SEARCH_PCT.y;
		BufferedImage test = null;
		
		trialloop:
		while (ypointer<RECT_SEARCH_PCT.height) {
			xpointer=RECT_SEARCH_PCT.x;
			while (xpointer>RECT_SEARCH_PCT.width) {
				int foundIndex = -1;
				for (int i=0;i<10;i++) {
					if (debug) {
						test = new BufferedImage(24,29,BufferedImage.TYPE_INT_ARGB);
					}
					boolean ruleBreak=false;
					
					colorloop:
					for (int x=0;x<24;x++) {
						for (int y=0;y<29;y++) {
							Color fontCol = new Color(percentfont.getRGB(x+i*24,y));
							Color pixelCol = new Color(img.getRGB(xpointer-24+x+1, y+ypointer));
							/*if (fontCol.equals(Color.RED) && pixelCol.getRed()<50
									 && pixelCol.getGreen()<150 && pixelCol.getBlue()>150) {
								//Breaks a rule.
								ruleBreak=true;
								if (!debug) {
									break colorloop;
								} else {
									test.setRGB(x, y, Color.RED.getRGB());
								}
							} else
							if (fontCol.equals(Color.GREEN) && (pixelCol.getRed()>50
									 || pixelCol.getGreen()>170 || pixelCol.getBlue()<150)) {
								//Breaks a rule.
								ruleBreak=true;
								if (!debug) {
									break colorloop;
								} else {
									test.setRGB(x, y, Color.GREEN.getRGB());
								}
							} else
							if (debug) {
								test.setRGB(x, y, pixelCol.getRGB());
							}*/
							
							if (fontCol.equals(Color.RED)) {
								if (lightColorCheck(pixelCol)) {
									if (debug) {
										test.setRGB(x, y, pixelCol.getRGB());
									}
								} else {
									ruleBreak=true;
									if (!debug) {
										break colorloop;
									} else {
										test.setRGB(x, y, Color.RED.getRGB());
									}
								}
							} else 
							if (fontCol.equals(Color.GREEN)) {
								if (darkColorCheck(pixelCol)) {
									if (debug) {
										test.setRGB(x, y, pixelCol.getRGB());
									}
								} else {
									ruleBreak=true;
									if (!debug) {
										break colorloop;
									} else {
										test.setRGB(x, y, Color.GREEN.getRGB());
									}
								}
							} else {
								if (debug) {
									test.setRGB(x, y, img.getRGB(xpointer-24+x+1, y+ypointer));
								}
							}
						}
					}
					if (!ruleBreak) {
						foundIndex=i;
						if (debug) {
							System.out.println("Passes as "+((foundIndex+1)%10));
						}
					} else 
					if (debug) {
						ImageIO.write(test,"png",new File("debug",System.nanoTime()+"_"+((i+1)%10)+".png"));
					}
				}
				
				if (foundIndex!=-1) {
					//System.out.println("  Closest Match: Index "+((shortestIndex+1)%10)+" ("+shortestDistance+")");
					if (decimal.equals("")) {
						decimal = Integer.toString((foundIndex+1)%10); 
					} else {
						decimal = Integer.toString((foundIndex+1)%10)+decimal;
					}
					if (debug) {
						System.out.println("Input as "+((foundIndex+1)%10));
						System.out.println("-------------");
					}
					xpointer-=24;
				} else {
					//Try shifting the xpointer slowly to the right and try again.
					xpointer--;
				}
			}
			if (decimal.length()>0) {
				break trialloop;
			}
			ypointer++;
		}

		xpointer=RECT_SEARCH_PCT2.x;
		ypointer=RECT_SEARCH_PCT2.y;
		trialloop:
		while (ypointer<RECT_SEARCH_PCT2.height) {
			xpointer=RECT_SEARCH_PCT2.x;
			while (xpointer>RECT_SEARCH_PCT2.width) {
				int foundIndex = -1;
				for (int i=0;i<10;i++) {
					if (debug) {
						test = new BufferedImage(24,29,BufferedImage.TYPE_INT_ARGB);
					}
					boolean ruleBreak=false;
					
					colorloop:
					for (int x=0;x<24;x++) {
						for (int y=0;y<29;y++) {
							Color fontCol = new Color(percentfont.getRGB(x+i*24,y));
							Color pixelCol = new Color(img.getRGB(xpointer-24+x+1, y+ypointer));
							/*if (fontCol.equals(Color.RED) && pixelCol.getRed()<50
									 && pixelCol.getGreen()<150 && pixelCol.getBlue()>150) {
								//Breaks a rule.
								ruleBreak=true;
								if (!debug) {
									break colorloop;
								} else {
									test.setRGB(x, y, Color.RED.getRGB());
								}
							} else
							if (fontCol.equals(Color.GREEN) && (pixelCol.getRed()>50
									 || pixelCol.getGreen()>170 || pixelCol.getBlue()<150)) {
								//Breaks a rule.
								ruleBreak=true;
								if (!debug) {
									break colorloop;
								} else {
									test.setRGB(x, y, Color.GREEN.getRGB());
								}
							} else
							if (debug) {
								test.setRGB(x, y, pixelCol.getRGB());
							}*/
							
							if (fontCol.equals(Color.RED)) {
								if (lightColorCheck(pixelCol)) {
									if (debug) {
										test.setRGB(x, y, pixelCol.getRGB());
									}
								} else {
									ruleBreak=true;
									if (!debug) {
										break colorloop;
									} else {
										test.setRGB(x, y, Color.RED.getRGB());
									}
								}
							} else 
							if (fontCol.equals(Color.GREEN)) {
								if (darkColorCheck(pixelCol)) {
									if (debug) {
										test.setRGB(x, y, pixelCol.getRGB());
									}
								} else {
									ruleBreak=true;
									if (!debug) {
										break colorloop;
									} else {
										test.setRGB(x, y, Color.GREEN.getRGB());
									}
								}
							} else {
								if (debug) {
									test.setRGB(x, y, img.getRGB(xpointer-24+x+1, y+ypointer));
								}
							}
						}
					}
					if (!ruleBreak) {
						foundIndex=i;
						if (debug) {
							System.out.println("Passes as "+((foundIndex+1)%10));
						}
					} else 
					if (debug) {
						ImageIO.write(test,"png",new File("debug",System.nanoTime()+"_"+((i+1)%10)+".png"));
					}
				}
				
				if (foundIndex!=-1) {
					//System.out.println("  Closest Match: Index "+((shortestIndex+1)%10)+" ("+shortestDistance+")");
					if (integer.equals("")) {
						integer = Integer.toString((foundIndex+1)%10); 
					} else {
						integer = Integer.toString((foundIndex+1)%10)+integer;
					}
					if (debug) {
						System.out.println("Input as "+((foundIndex+1)%10));
						System.out.println("-------------");
					}
					xpointer-=24;
				} else {
					//Try shifting the xpointer slowly to the right and try again.
					xpointer--;
				}
			}
			if (integer.length()>0) {
				break trialloop;
			}
			ypointer++;
		}
		
		return Float.parseFloat(integer+"."+decimal);
	}

	private boolean lightColorCheck(Color pixel) {
		return pixel.getRed()+pixel.getGreen()+pixel.getBlue()>=580;
		/*(pixelCol.getRed()>=200
			 && pixelCol.getGreen()>=200 && pixelCol.getBlue()>=200);*/
	}
	
	private boolean darkColorCheck(Color pixel) {
		return pixel.getRed()<110
				 && pixel.getGreen()<176 && pixel.getBlue()>100;
	}

	public int extractNumbersFromImage(BufferedImage img) throws IOException {
		return extractNumbersFromImage(img,false);
	}
	
	public int extractNumbersFromImage(BufferedImage img,boolean debug) throws IOException {
		this.img=img;
		File f = null;
		BufferedImage test = null;
		xpointer=RECT_SEARCH_COOL.width-1;
		ypointer=0;
		String total = "";
		trialloop:
		while (ypointer<4) {
			xpointer=RECT_SEARCH_COOL.width-1;
			while (xpointer>22) {
				int distance = 0;
				int foundIndex = -1;
				//Compare the 22x21 range.
				for (int i=0;i<10;i++) {
					if (debug) {
						test = new BufferedImage(22,20,BufferedImage.TYPE_INT_ARGB);
					}
					boolean ruleBreak=false;
					
					colorloop:
					for (int x=0;x<22;x++) {
						for (int y=0;y<20;y++) {
							Color fontCol = new Color(font.getRGB(x+i*22,y));
							Color pixelCol = new Color(img.getRGB(xpointer-22+x+1, y+ypointer));
							if (fontCol.equals(Color.RED) && pixelCol.getRed()<150
									 && pixelCol.getGreen()<150 && pixelCol.getBlue()<150) {
								//Breaks a rule.
								ruleBreak=true;
								if (!debug) {
									break colorloop;
								} else {
									test.setRGB(x, y, Color.RED.getRGB());
								}
							} else
							if (fontCol.equals(Color.GREEN) && (pixelCol.getRed()>166
									 || pixelCol.getGreen()>171 || pixelCol.getBlue()>185)) {
								//Breaks a rule.
								ruleBreak=true;
								if (!debug) {
									break colorloop;
								} else {
									test.setRGB(x, y, Color.GREEN.getRGB());
								}
							} else
							if (debug) {
								test.setRGB(x, y, pixelCol.getRGB());
							}
						}
					}
					if (!ruleBreak) {
						foundIndex=i;
						if (debug) {
							System.out.println("Passes as "+((foundIndex+1)%10));
						}
					} else 
					if (debug) {
						ImageIO.write(test,"png",new File("debug",System.nanoTime()+"_"+((i+1)%10)+".png"));
					}
				}
				if (foundIndex!=-1) {
					//System.out.println("  Closest Match: Index "+((shortestIndex+1)%10)+" ("+shortestDistance+")");
					if (total.equals("")) {
						total = Integer.toString((foundIndex+1)%10); 
					} else {
						total = Integer.toString((foundIndex+1)%10)+total;
					}
					if (debug) {
						System.out.println("Input as "+((foundIndex+1)%10));
						System.out.println("-------------");
					}
					xpointer-=22;
				} else {
					//Try shifting the xpointer slowly to the right and try again.
					xpointer--;
				}
			}
			if (total.length()>0) {
				break trialloop;
			}
			ypointer++;
		}
		
		if (total.equals("")) {
			return -1;
		} else {
			return Integer.parseInt(total);
		}
	}
	
	public int extractScoreNumbersFromImage(BufferedImage img,boolean debug) throws IOException {
		this.img=img;
		File f = null;
		BufferedImage test = null;
		xpointer=RECT_SEARCH_SCORE.width-1;
		ypointer=0;
		String total = "";
		trialloop:
		while (ypointer<4) {
			xpointer=RECT_SEARCH_SCORE.width-1;
			while (xpointer>31) {
				int distance = 0;
				int foundIndex = -1;
				//Compare the 22x21 range.
				for (int i=0;i<10;i++) {
					if (debug) {
						test = new BufferedImage(30,28,BufferedImage.TYPE_INT_ARGB);
					}
					boolean ruleBreak=false;
					
					colorloop:
					for (int x=0;x<30;x++) {
						for (int y=0;y<28;y++) {
							Color fontCol = new Color(scorefont.getRGB(x+i*30,y));
							Color pixelCol = new Color(img.getRGB(xpointer-30+x+1, y+ypointer));
							if (fontCol.equals(Color.RED) && pixelCol.getRed()<200
									 && pixelCol.getGreen()<200 && pixelCol.getBlue()<200
									/*pixelCol.getRed()+pixelCol.getGreen()+pixelCol.getBlue()<490*/) {
								ruleBreak=true;
								if (!debug) {
									break colorloop;
								} else {
									test.setRGB(x, y, Color.RED.getRGB());
								}
							} else
							if (fontCol.equals(Color.GREEN) && (pixelCol.getRed()>166
									 || pixelCol.getGreen()>171 || pixelCol.getBlue()>185)) {
								//Breaks a rule.
								ruleBreak=true;
								if (!debug) {
									break colorloop;
								} else {
									test.setRGB(x, y, Color.GREEN.getRGB());
								}
							} else
							if (debug) {
								test.setRGB(x, y, pixelCol.getRGB());
							}
						}
					}
					if (!ruleBreak) {
						foundIndex=i;
						if (debug) {
							System.out.println("Passes as "+((foundIndex+1)%10));
						}
					} else 
					if (debug) {
						ImageIO.write(test,"png",new File("debug",System.nanoTime()+"_"+((i+1)%10)+".png"));
					}
				}
				if (foundIndex!=-1) {
					//System.out.println("  Closest Match: Index "+((shortestIndex+1)%10)+" ("+shortestDistance+")");
					if (total.equals("")) {
						total = Integer.toString((foundIndex+1)%10); 
					} else {
						total = Integer.toString((foundIndex+1)%10)+total;
					}
					if (debug) {
						System.out.println("Input as "+((foundIndex+1)%10));
						System.out.println("-------------");
					}
					xpointer-=30;
				} else {
					//Try shifting the xpointer slowly to the right and try again.
					xpointer--;
				}
			}
			if (total.length()>0) {
				break trialloop;
			}
			ypointer++;
		}
		
		if (total.equals("")) {
			return -1;
		} else {
			return Integer.parseInt(total);
		}
	}
}

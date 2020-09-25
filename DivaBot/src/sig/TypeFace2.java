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
	BufferedImage futuretone_percentfont;
	BufferedImage futuretone_scorefont;
	int xpointer = 99;
	int ypointer = 0;
	public static final int THRESHOLD = 30000;
	
	public TypeFace2(BufferedImage font,BufferedImage percentfont,
			BufferedImage scorefont,
			BufferedImage ft_percentfont,
			BufferedImage ft_scorefont) {
		this.font=font;
		this.percentfont = percentfont;
		this.scorefont = scorefont;
		this.futuretone_percentfont = ft_percentfont;
		this.futuretone_scorefont = ft_scorefont;
		
		File debugdir = new File("debug");
		debugdir.mkdirs();
	}
	
	public Result getAllData(BufferedImage img) throws Exception {
		return getAllData(img,false);
	}
	
	final static int XOFFSET = 8;
	
	final static Rectangle MEGAMIX_RECT_SEARCH_COOL=new Rectangle(866+XOFFSET,260,100+XOFFSET+1,22+8);
	final static Rectangle MEGAMIX_RECT_SEARCH_FINE=new Rectangle(866+XOFFSET,294,100+XOFFSET+1,22+8);
	final static Rectangle MEGAMIX_RECT_SEARCH_SAFE=new Rectangle(866+XOFFSET,329,100+XOFFSET+1,22+8);
	final static Rectangle MEGAMIX_RECT_SEARCH_SAD=new Rectangle(866+XOFFSET,364,100+XOFFSET+1,22+8);
	final static Rectangle MEGAMIX_RECT_SEARCH_WORST=new Rectangle(866+XOFFSET,400,100+XOFFSET+1,22+8);
	final static Rectangle MEGAMIX_RECT_SEARCH_PCT=new Rectangle(1182+XOFFSET,163,1132,8);
	final static Rectangle MEGAMIX_RECT_SEARCH_PCT2=new Rectangle(1123+XOFFSET,163,1051,8);
	final static Rectangle MEGAMIX_RECT_SEARCH_SCORE=new Rectangle(859+XOFFSET,578-4,250+XOFFSET+1,32+14);
	final static Rectangle MEGAMIX_RECT_SEARCH_COMBO=new Rectangle(1010+XOFFSET,435-2,100+XOFFSET+1,22+8);
	final static Rectangle FUTURETONE_RECT_SEARCH_COOL=new Rectangle(850+XOFFSET,223,122+XOFFSET+1,22+8);
	final static Rectangle FUTURETONE_RECT_SEARCH_FINE=new Rectangle(850+XOFFSET,258,122+XOFFSET+1,22+8);
	final static Rectangle FUTURETONE_RECT_SEARCH_SAFE=new Rectangle(850+XOFFSET,294,122+XOFFSET+1,22+8);
	final static Rectangle FUTURETONE_RECT_SEARCH_SAD=new Rectangle(850+XOFFSET,330,122+XOFFSET+1,22+8);
	final static Rectangle FUTURETONE_RECT_SEARCH_WORST=new Rectangle(850+XOFFSET,367,122+XOFFSET+1,22+8);
	final static Rectangle FUTURETONE_RECT_SEARCH_PCT=new Rectangle(1174+XOFFSET,152,1125,8);
	final static Rectangle FUTURETONE_RECT_SEARCH_PCT2=new Rectangle(1114+XOFFSET,152,1045,8);
	final static Rectangle FUTURETONE_RECT_SEARCH_SCORE=new Rectangle(866+XOFFSET,543-4,250+XOFFSET+1,32+24);
	final static Rectangle FUTURETONE_RECT_SEARCH_COMBO=new Rectangle(1023+XOFFSET,402,100+XOFFSET+1,22+8);

    public final static int[] DEFAULT_YPOINTERS = new int[] {-1,-1,-1,-1,-1,-1,-1,-1}; 
	public static int[] ypointers = new int[] {-1,-1,-1,-1,-1,-1,-1,-1};
	public static int[] officialypointers = new int[] {-1,-1,-1,-1,-1,-1,-1,-1};
	
	public static void deepCopyDefaultYPointers() {
		ypointers = new int[DEFAULT_YPOINTERS.length];
		for (int i=0;i<DEFAULT_YPOINTERS.length;i++) {
			ypointers[i]=DEFAULT_YPOINTERS[i];
		}
	}
	public static void deepCopyDefaultOfficialYPointers() {
		officialypointers = new int[DEFAULT_YPOINTERS.length];
		for (int i=0;i<DEFAULT_YPOINTERS.length;i++) {
			officialypointers[i]=DEFAULT_YPOINTERS[i];
		}
	}
	public static void deepCopyOfficialYPointersFromPointers() {
		officialypointers = new int[ypointers.length];
		for (int i=0;i<ypointers.length;i++) {
			officialypointers[i]=ypointers[i];
		}
	}
	
	public boolean isClass(String className) {
	    try  {
	        Class.forName(className);
	        return true;
	    }  catch (ClassNotFoundException e) {
	        return false;
	    }
	}

	public Result getAllData(BufferedImage img, boolean debug) throws IOException {
		deepCopyDefaultYPointers();
		BufferedImage img2 = ImageUtils.toBufferedImage(img.getScaledInstance(1280 , 720, Image.SCALE_SMOOTH));
		Result result = new Result("","",-1,-1,-1,-1,-1,-1f);
		int[] finalNumbers = new int[5];
		
		int pointerAccumulator = 0;
		
		Rectangle[] ranges = null;
		
		result.mode = getMode(img2,debug);
		
		if (isClass("sig.MyRobot")) {
			if (MyRobot.FUTURETONE) {
				/*if (result.mode!=Mode.FUTURETONE) {
					System.out.println("The current mode is "+result.mode+", but the results screen thinks this is Megamix... We're going to manually fix it for now. Let sig know this is appearing.");
				}*/
				result.mode=Mode.FUTURETONE;
			} else {
				/*if (result.mode!=Mode.MEGAMIX) {
					System.out.println("The current mode is "+result.mode+", but the results screen thinks this is Future Tone... We're going to manually fix it for now. Let sig know this is appearing.");
				}*/
				result.mode=Mode.MEGAMIX;
			}
		}
		
		switch (result.mode) {
			case MEGAMIX:{
				ranges = new Rectangle[] {
						//These coords are in regard to the old screenshot sizes.
						MEGAMIX_RECT_SEARCH_COOL, //33 pixels per line.
						MEGAMIX_RECT_SEARCH_FINE,
						MEGAMIX_RECT_SEARCH_SAFE,
						MEGAMIX_RECT_SEARCH_SAD,
						MEGAMIX_RECT_SEARCH_WORST,
				};
			}break;
			case FUTURETONE:{
				ranges = new Rectangle[] {
						//These coords are in regard to the old screenshot sizes.
						FUTURETONE_RECT_SEARCH_COOL, //33 pixels per line.
						FUTURETONE_RECT_SEARCH_FINE,
						FUTURETONE_RECT_SEARCH_SAFE,
						FUTURETONE_RECT_SEARCH_SAD,
						FUTURETONE_RECT_SEARCH_WORST,
				};
			}break;
		}
		
		for (int i=0;i<ranges.length;i++) {
			Rectangle r = ranges[i];

			//System.out.println("Image "+i+":");
			if (debug) {
				File temp = new File("rectangle"+i+".png");
				ImageIO.write(img2.getSubimage(r.x,r.y,r.width,r.height),"png",temp);
				//System.out.println("Search "+i);
			}

			finalNumbers[i]=extractNumbersFromImage(img2.getSubimage(
					r.x,r.y,r.width,r.height),debug,pointerAccumulator);
			ypointers[pointerAccumulator++] = ypointer;
			//System.out.println(finalNumbers[i]);
			if (finalNumbers[i]==-1) {
				return result;
			}
		}
		result.cool = finalNumbers[0];
		result.fine = finalNumbers[1];
		result.safe = finalNumbers[2];
		result.sad = finalNumbers[3];
		result.worst = finalNumbers[4];
		float percent = -1f;
		switch (result.mode) {
			case MEGAMIX:{
				percent = extractPercentFromImage(img2,debug,pointerAccumulator);
			}break;
			case FUTURETONE:{
				percent = extractFutureTonePercentFromImage(img2,debug,pointerAccumulator);
			}break;
		}
		ypointers[pointerAccumulator++] = ypointer;
		if (percent<0) {
			return result;
		}
		result.percent=percent;
		// result.percent = ??
		
		
		//489,197
		result.fail = false;
		switch (result.mode) {
			case MEGAMIX:{
				ColorRegion failRegion = new ColorRegion(img2,new Rectangle(484,191,5,5));
				if (failRegion.getAllRange(0, 130, 0, 130, 0, 130)) {
					result.fail = true;
					break;
				}
			}break;
			case FUTURETONE:{
				ColorRegion failRegion = new ColorRegion(img2,new Rectangle(585,146,5,5));
				if (failRegion.getAllRange(180, 240, 100, 200, 190, 240)) {
					result.fail = true;
				}
			}break;
		}

		switch (result.mode) {
			case MEGAMIX:{
				ColorRegion difficultyRegion = new ColorRegion(img2,new Rectangle(620,100,5,5));
				result.difficulty = getDifficulty(difficultyRegion);
				if (debug) {
					System.out.println("Diff:"+result.difficulty+"/"+difficultyRegion);
					ImageIO.write(difficultyRegion.img.getSubimage(difficultyRegion.region.x,difficultyRegion.region.y,difficultyRegion.region.width,difficultyRegion.region.height),"png",new File("debug","difficulty.png"));
				}
			}break;
			case FUTURETONE:{
				ColorRegion difficultyRegion = new ColorRegion(img2,new Rectangle(583,68,5,5));
				result.difficulty = getFutureToneDifficulty(difficultyRegion);
				if (debug) {
					System.out.println("Diff:"+result.difficulty+"/"+difficultyRegion);
					ImageIO.write(difficultyRegion.img.getSubimage(difficultyRegion.region.x,difficultyRegion.region.y,difficultyRegion.region.width,difficultyRegion.region.height),"png",new File("debug","difficulty.png"));
				}
			}break;
		}
		
		//393,54
		switch (result.mode) {
			case MEGAMIX:{
				result.mod = getMod(img2);
			}break;
			case FUTURETONE:{
				result.mod = getFutureToneMod(img2);
			}break;
		}
		
		switch (result.mode) {
			case MEGAMIX:{
				//1109,435
				result.combo = extractNumbersFromImage(img2.getSubimage(
						MEGAMIX_RECT_SEARCH_COMBO.x,MEGAMIX_RECT_SEARCH_COMBO.y,MEGAMIX_RECT_SEARCH_COMBO.width,MEGAMIX_RECT_SEARCH_COMBO.height),debug,pointerAccumulator);
				if (result.combo<0) {
					return result;
				}
				ypointers[pointerAccumulator++] = ypointer;
				
				result.score = extractScoreNumbersFromImage(img2.getSubimage(
						MEGAMIX_RECT_SEARCH_SCORE.x,MEGAMIX_RECT_SEARCH_SCORE.y,MEGAMIX_RECT_SEARCH_SCORE.width,MEGAMIX_RECT_SEARCH_SCORE.height),debug,pointerAccumulator);
				if (result.score<0) {
					return result;
				}
				ypointers[pointerAccumulator++] = ypointer;
			}break;
			case FUTURETONE:{
				//1109,435
				result.combo = extractNumbersFromImage(img2.getSubimage(
						FUTURETONE_RECT_SEARCH_COMBO.x,FUTURETONE_RECT_SEARCH_COMBO.y,FUTURETONE_RECT_SEARCH_COMBO.width,FUTURETONE_RECT_SEARCH_COMBO.height),debug,pointerAccumulator);
				if (result.combo<0) {
					return result;
				}
				ypointers[pointerAccumulator++] = ypointer;
				
				result.score = extractFutureToneScoreNumbersFromImage(img2.getSubimage(
						FUTURETONE_RECT_SEARCH_SCORE.x,FUTURETONE_RECT_SEARCH_SCORE.y,FUTURETONE_RECT_SEARCH_SCORE.width,FUTURETONE_RECT_SEARCH_SCORE.height),debug,pointerAccumulator);
				if (result.score<0) {
					return result;
				}
				ypointers[pointerAccumulator++] = ypointer;
			}break;
		}
		
		return result;
	}
	
	private Mode getMode(BufferedImage img2,boolean debug) {
		ColorRegion ft_results = new ColorRegion(img2,new Rectangle(81,35,80,37));
		if (debug) {
			System.out.println("Mode Check:"+ft_results);
		}
		if (ft_results.getAllRange(30,150,60,180,60,180)) {
			return Mode.FUTURETONE;
		}
		return Mode.MEGAMIX;
	}

	private String getFutureToneMod(BufferedImage img2) {
		//393,54 HS  R>125
		//423,72 HD  G>100, R>125
		//454,54 SD  G>100, B>125
		Color modPixel = new Color(img2.getRGB(393, 54));
		if (modPixel.getRed()>125&&modPixel.getGreen()>125&&modPixel.getBlue()>125) {
			return "";
		}
		if (modPixel.getRed()>125) {return "HS";}
		modPixel = new Color(img2.getRGB(423, 72));
		if (modPixel.getGreen()>100&&modPixel.getRed()>125) {return "HD";}
		modPixel = new Color(img2.getRGB(454, 54));
		if (modPixel.getGreen()>100&&modPixel.getBlue()>125) {return "SD";}
		return "";
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

	private String getFutureToneDifficulty(ColorRegion difficultyRegion) {
		String[] diffs = new String[] {"E","N","H","EX","EXEX"};
		Color[] cols = new Color[] {new Color(15,63,160),new Color(31,175,13),new Color(157,123,17),new Color(152,13,16),new Color(98,0,165),};
		int lowestDistance = Integer.MAX_VALUE;
		int lowestIndex = -1;
		Color avg = new Color(difficultyRegion.getRed(),difficultyRegion.getGreen(),difficultyRegion.getBlue());
		for (int i=0;i<cols.length;i++) {
			int distance = (int)ImageUtils.distanceToColor(avg, cols[i]);
			if (distance<lowestDistance) {
				lowestDistance = distance;
				lowestIndex=i;
			}
		}
		return diffs[lowestIndex];
	}

	private String getDifficulty(ColorRegion difficultyRegion) {
		String[] diffs = new String[] {"E","N","H","EX","EXEX"};
		Color[] cols = new Color[] {new Color(95,243,255),new Color(20,234,0),new Color(251,191,0),new Color(253,14,81),new Color(157,0,227),};
		int lowestDistance = Integer.MAX_VALUE;
		int lowestIndex = -1;
		Color avg = new Color(difficultyRegion.getRed(),difficultyRegion.getGreen(),difficultyRegion.getBlue());
		for (int i=0;i<cols.length;i++) {
			int distance = (int)ImageUtils.distanceToColor(avg, cols[i]);
			if (distance<lowestDistance) {
				lowestDistance = distance;
				lowestIndex=i;
			}
		}
		return diffs[lowestIndex];
	}
	
	public float extractFutureTonePercentFromImage(BufferedImage img,boolean debug,int iteration) throws IOException {
		//1180,167
		//second part: 1123
		String decimal = "";
		String integer = "";
		xpointer=FUTURETONE_RECT_SEARCH_PCT.x;
		ypointer=FUTURETONE_RECT_SEARCH_PCT.y;
		BufferedImage test = null;
		
		boolean startPointer=officialypointers[iteration]>=0;
		
		if (startPointer) {
			ypointer=officialypointers[iteration];
			//System.out.println("Found a saved ypointer of "+ypointer);
		}
		
		trialloop:
		while (ypointer<FUTURETONE_RECT_SEARCH_PCT.height+FUTURETONE_RECT_SEARCH_PCT.y) {
			xpointer=FUTURETONE_RECT_SEARCH_PCT.x;
			while (xpointer>FUTURETONE_RECT_SEARCH_PCT.width) {
				int foundIndex = -1;
				for (int i=0;i<10;i++) {
					if (debug) {
						test = new BufferedImage(18,18,BufferedImage.TYPE_INT_ARGB);
					}
					boolean ruleBreak=false;
					
					colorloop:
					for (int x=0;x<18;x++) {
						for (int y=0;y<18;y++) {
							Color fontCol = new Color(futuretone_percentfont.getRGB(x+i*18,y));
							Color pixelCol = new Color(img.getRGB(xpointer-18+x+1, y+ypointer));
							
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
								if (futureToneDarkColorCheck(pixelCol)) {
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
									test.setRGB(x, y, img.getRGB(xpointer-18+x+1, y+ypointer));
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
					xpointer-=18;
				} else {
					//Try shifting the xpointer slowly to the right and try again.
					xpointer--;
				}
			}
			if (decimal.length()>0) {
				break trialloop;
			}
			if (startPointer) {
				startPointer=false;
				ypointer=FUTURETONE_RECT_SEARCH_PCT.y;
				//System.out.println("Could not find with saved ypointer. Switching back to old ypointer.");
			} else {
				ypointer++;
			}
		}
		
		startPointer=officialypointers[iteration]>=0;
		
		if (startPointer) {
			ypointer=officialypointers[iteration];
			//System.out.println("Found a saved ypointer of "+ypointer);
		}

		xpointer=FUTURETONE_RECT_SEARCH_PCT2.x;
		ypointer=FUTURETONE_RECT_SEARCH_PCT2.y;
		trialloop:
		while (ypointer<FUTURETONE_RECT_SEARCH_PCT2.height+FUTURETONE_RECT_SEARCH_PCT2.y) {
			xpointer=FUTURETONE_RECT_SEARCH_PCT2.x;
			while (xpointer>FUTURETONE_RECT_SEARCH_PCT2.width) {
				int foundIndex = -1;
				for (int i=0;i<10;i++) {
					if (debug) {
						test = new BufferedImage(18,18,BufferedImage.TYPE_INT_ARGB);
					}
					boolean ruleBreak=false;
					
					colorloop:
					for (int x=0;x<18;x++) {
						for (int y=0;y<18;y++) {
							Color fontCol = new Color(futuretone_percentfont.getRGB(x+i*18,y));
							Color pixelCol = new Color(img.getRGB(xpointer-18+x+1, y+ypointer));
							
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
								if (futureToneDarkColorCheck(pixelCol)) {
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
									test.setRGB(x, y, img.getRGB(xpointer-18+x+1, y+ypointer));
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
					xpointer-=18;
				} else {
					//Try shifting the xpointer slowly to the right and try again.
					xpointer--;
				}
			}
			if (integer.length()>0) {
				break trialloop;
			}
			if (startPointer) {
				startPointer=false;
				ypointer=FUTURETONE_RECT_SEARCH_PCT2.y;
				//System.out.println("Could not find with saved ypointer. Switching back to old ypointer.");
			} else {
				ypointer++;
			}
		}
		if (integer.length()>0&&decimal.length()>0) {
			return Float.parseFloat(integer+"."+decimal);
		} else {
			return -1.0f;
		}
	}
	
	public float extractPercentFromImage(BufferedImage img,boolean debug,int iteration) throws IOException {
		//1180,167
		//second part: 1123
		String decimal = "";
		String integer = "";
		xpointer=MEGAMIX_RECT_SEARCH_PCT.x;
		ypointer=MEGAMIX_RECT_SEARCH_PCT.y;
		BufferedImage test = null;
		
		boolean startPointer=officialypointers[iteration]>=0;
		
		if (startPointer) {
			ypointer=officialypointers[iteration];
			//System.out.println("Found a saved ypointer of "+ypointer);
		}
		
		trialloop:
		while (ypointer<MEGAMIX_RECT_SEARCH_PCT.height+MEGAMIX_RECT_SEARCH_PCT.y) {
			xpointer=MEGAMIX_RECT_SEARCH_PCT.x;
			while (xpointer>MEGAMIX_RECT_SEARCH_PCT.width) {
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
			if (startPointer) {
				startPointer=false;
				ypointer=MEGAMIX_RECT_SEARCH_PCT.y;
				//System.out.println("Could not find with saved ypointer. Switching back to old ypointer.");
			} else {
				ypointer++;
			}
		}
		
		startPointer=officialypointers[iteration]>=0;
		
		if (startPointer) {
			ypointer=officialypointers[iteration];
			//System.out.println("Found a saved ypointer of "+ypointer);
		}

		xpointer=MEGAMIX_RECT_SEARCH_PCT2.x;
		ypointer=MEGAMIX_RECT_SEARCH_PCT2.y;
		trialloop:
		while (ypointer<MEGAMIX_RECT_SEARCH_PCT2.height+MEGAMIX_RECT_SEARCH_PCT2.y) {
			xpointer=MEGAMIX_RECT_SEARCH_PCT2.x;
			while (xpointer>MEGAMIX_RECT_SEARCH_PCT2.width) {
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
			if (startPointer) {
				startPointer=false;
				ypointer=MEGAMIX_RECT_SEARCH_PCT2.y;
				//System.out.println("Could not find with saved ypointer. Switching back to old ypointer.");
			} else {
				ypointer++;
			}
		}
		if (integer.length()>0&&decimal.length()>0) {
			return Float.parseFloat(integer+"."+decimal);
		} else {
			return -1.0f;
		}
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
	private boolean futureToneDarkColorCheck(Color pixel) {
		return pixel.getRed()<176
				 && pixel.getGreen()<178 && pixel.getBlue()>100;
	}
	
	public int extractNumbersFromImage(BufferedImage img,boolean debug,int iteration) throws IOException {
		this.img=img;
		File f = null;
		BufferedImage test = null;
		xpointer=img.getWidth()-1;
		ypointer=0;
		String total = "";
		
		boolean startPointer=officialypointers[iteration]>=0;
		
		if (startPointer) {
			ypointer=officialypointers[iteration];
			//System.out.println("Found a saved ypointer of "+ypointer);
		}
		
		trialloop:
		while (ypointer<8) {
			xpointer=img.getWidth()-1;
			while (xpointer>22) {
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
			if (startPointer) {
				startPointer=false;
				ypointer=0;
				//System.out.println("Could not find with saved ypointer. Switching back to old ypointer.");
			} else {
				ypointer++;
			}
		}
		if (total.equals("")) {
			return -1;
		} else {
			return Integer.parseInt(total);
		}
	}
	
	public int extractFutureToneScoreNumbersFromImage(BufferedImage img,boolean debug,int iteration) throws IOException {
		this.img=img;
		File f = null;
		BufferedImage test = null;
		xpointer=FUTURETONE_RECT_SEARCH_SCORE.width-1;
		ypointer=0;
		String total = "";
		
		boolean startPointer=officialypointers[iteration]>=0;
		
		if (startPointer) {
			ypointer=officialypointers[iteration];
		}
		
		trialloop:
		while (ypointer<20) {
			xpointer=FUTURETONE_RECT_SEARCH_SCORE.width-1;
			while (xpointer>31) {
				int distance = 0;
				int foundIndex = -1;
				//Compare the 22x21 range.
				for (int i=0;i<10;i++) {
					if (debug) {
						test = new BufferedImage(28,27,BufferedImage.TYPE_INT_ARGB);
					}
					boolean ruleBreak=false;
					
					colorloop:
					for (int x=0;x<28;x++) {
						for (int y=0;y<27;y++) {
							Color fontCol = new Color(futuretone_scorefont.getRGB(x+i*28,y));
							Color pixelCol = new Color(img.getRGB(xpointer-28+x+1, y+ypointer));
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
					xpointer-=28;
				} else {
					//Try shifting the xpointer slowly to the right and try again.
					xpointer--;
				}
			}
			if (total.length()>0) {
				break trialloop;
			}
			if (startPointer) {
				startPointer=false;
				ypointer=0;
			} else {
				ypointer++;
			}
		}
		
		if (total.equals("")) {
			return -1;
		} else {
			return Integer.parseInt(total);
		}
	}
	
	public int extractScoreNumbersFromImage(BufferedImage img,boolean debug,int iteration) throws IOException {
		this.img=img;
		File f = null;
		BufferedImage test = null;
		xpointer=MEGAMIX_RECT_SEARCH_SCORE.width-1;
		ypointer=0;
		String total = "";
		
		boolean startPointer=officialypointers[iteration]>=0;
		
		if (startPointer) {
			ypointer=officialypointers[iteration];
		}
		
		trialloop:
		while (ypointer<12) {
			xpointer=MEGAMIX_RECT_SEARCH_SCORE.width-1;
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
			if (startPointer) {
				startPointer=false;
				ypointer=0;
			} else {
				ypointer++;
			}
		}
		
		if (total.equals("")) {
			return -1;
		} else {
			return Integer.parseInt(total);
		}
	}
}

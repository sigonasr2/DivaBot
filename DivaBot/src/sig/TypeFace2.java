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

import sig.utils.ImageUtils;

public class TypeFace2 {
	BufferedImage img;
	BufferedImage font;
	int xpointer = 99;
	public static final int THRESHOLD = 30000;
	
	public TypeFace2(BufferedImage font) {
		this.font=font;
	}
	
	public int[] getAllData(BufferedImage img) throws IOException {
		BufferedImage img2 = ImageUtils.toBufferedImage(img.getScaledInstance(1280, 720, Image.SCALE_AREA_AVERAGING));
		int[] finalNumbers = new int[5];
		
		Rectangle[] ranges = new Rectangle[] {
				//These coords are in regard to the old screenshot sizes.
				new Rectangle(866,262,100,20), //33 pixels per line.
				new Rectangle(866,296,100,20),
				new Rectangle(866,331,100,20),
				new Rectangle(866,366,100,20),
				new Rectangle(866,400,100,20),
		};
		
		for (int i=0;i<ranges.length;i++) {
			Rectangle r = ranges[i];

			System.out.println("Image "+i+":");
			File temp = new File("rectangle"+i+".png");
			ImageIO.write(img2.getSubimage(r.x,r.y,r.width,r.height),"png",temp);
			
			finalNumbers[i]=extractNumbersFromImage(img2.getSubimage(
					r.x,r.y,r.width,r.height),false);
			
		}
		return finalNumbers;
	}
	
	public int extractNumbersFromImage(BufferedImage img) throws IOException {
		return extractNumbersFromImage(img,false);
	}
	
	public int extractNumbersFromImage(BufferedImage img,boolean debug) throws IOException {
		this.img=img;
		File f = null;
		BufferedImage test = null;
		xpointer=99;
		String total = "";
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
						Color pixelCol = new Color(img.getRGB(xpointer-22+x+1, y));
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
						if (fontCol.equals(Color.GREEN) && (pixelCol.getRed()>150
								 || pixelCol.getGreen()>150 || pixelCol.getBlue()>150)) {
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
					ImageIO.write(test,"png",new File(System.nanoTime()+"_"+((i+1)%10)+".png"));
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
		
		if (total.equals("")) {
			return -1;
		} else {
			return Integer.parseInt(total);
		}
	}
}

package sig;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import sig.utils.FileUtils;

public class Calibrator2 {
	
	public static BufferedImage img;
	
	Calibrator2() throws IOException {
			boolean failed=false;
		
		if (MyRobot.STARTDRAG.x>MyRobot.ENDDRAG.x) {
			int xTemp=MyRobot.STARTDRAG.x;
			MyRobot.STARTDRAG.x=MyRobot.ENDDRAG.x;
			MyRobot.ENDDRAG.x=xTemp;
		}
		if (MyRobot.STARTDRAG.y>MyRobot.ENDDRAG.y) {
			int yTemp=MyRobot.STARTDRAG.y;
			MyRobot.STARTDRAG.y=MyRobot.ENDDRAG.y;
			MyRobot.ENDDRAG.y=yTemp;
		}

		int xoffset1=0;
		int yoffset1=0;
		int xoffset2=0;
		int yoffset2=0;
		
		img = MyRobot.MYROBOT.getSizedCapture(new Rectangle(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y));
		ImageIO.write(img,"png",new File("capture_1.png"));
		boolean found=false;
		for (int x=0;x<img.getWidth();x++) {
			for (int y=0;y<img.getHeight();y++) {
				//Find the first corner.
				Color col = new Color(img.getRGB(x, y));
				if (col.getRed()+col.getGreen()+col.getBlue()>150) {
					MyRobot.STARTDRAG.x+=x;
					MyRobot.STARTDRAG.y+=y;
					xoffset1=x;
					yoffset1=y;
					found=true;
					System.out.println("x:"+x+",y:"+y+",col:"+col);
					break;
				}
			}
			if (found) {break;}
		}
		
		img = MyRobot.MYROBOT.getSizedCapture(new Rectangle(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y));
		ImageIO.write(img,"png",new File("capture_2.png"));
		
		found=false;
		for (int x=img.getWidth()-1;x>=0;x--) {
			for (int y=img.getHeight()-1;y>=0;y--) {
				//Find the first corner.
				Color col = new Color(img.getRGB(x, y));
				if (col.getRed()+col.getGreen()+col.getBlue()>150) {
					MyRobot.ENDDRAG.x-=img.getWidth()-1-x;
					MyRobot.ENDDRAG.y-=img.getHeight()-1-y;
					xoffset2=img.getWidth()-1-x;
					yoffset2=img.getHeight()-1-y;
					found=true;
					System.out.println("x:"+x+",y:"+y+",col:"+col);
					break;
				}
			}
			if (found) {break;}
		}
		for (int y=yoffset1;y>=0;y--) {
			BufferedImage newimg = MyRobot.MYROBOT.getSizedCapture(new Rectangle(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y));
			Color col = new Color(newimg.getRGB(0, 0));
			if (col.getRed()+col.getGreen()+col.getBlue()<5) {
				break;
			}
			MyRobot.STARTDRAG.y-=1;
		}
		for (int y=yoffset2;y<img.getHeight();y++) {
			BufferedImage newimg = MyRobot.MYROBOT.getSizedCapture(new Rectangle(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y));
			Color col = new Color(newimg.getRGB(newimg.getWidth()-1, newimg.getHeight()-1));
			if (col.getRed()+col.getGreen()+col.getBlue()<5) {
				break;
			}
			MyRobot.ENDDRAG.y+=1;
		}
		
		img = MyRobot.MYROBOT.getSizedCapture(new Rectangle(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y));
		ImageIO.write(img,"png",new File("capture_3.png"));
		
		System.out.println(MyRobot.STARTDRAG+","+MyRobot.ENDDRAG);
		
		MyRobot.FRAME.setCursor(Cursor.getDefaultCursor());

		Overlay.started=false;
		MyRobot.FRAME.setAlwaysOnTop(false);
		Overlay.OVERLAY.setVisible(true);
		
		MyRobot.CALIBRATIONSTATUS="Calibration is complete! - X"+(MyRobot.STARTDRAG.x)+" Y"+(MyRobot.STARTDRAG.y)+" W"+(MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x)+" H"+(MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y)+" R"+((float)(MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x)/(MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y));
		if (((float)(MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x)/(MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y))<=16/9f-0.015||
				((float)(MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x)/(MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y))>=16/9f+0.015) {
			int dialogResult = JOptionPane.showConfirmDialog (null, "Could not detect the game properly!\n\nYour calibration cut a bit "+((((float)(MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x)/(MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y))<=16/9f-0.04)?"more":"less")+" than expected. Do you want to try selecting a more accurate region?","Warning",JOptionPane.YES_NO_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){
				MyRobot.STARTDRAG=null;
				MyRobot.ENDDRAG=null;
				Overlay.OVERLAY.setVisible(true);
				MyRobot.CALIBRATIONSTATUS="";
				return;
			}
		}
		if (Math.abs(MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y)<460) {
			JOptionPane.showMessageDialog(null, "It is NOT recommended to capture a region below 460 pixels in height! This can cause inaccurate plays to be reported! Consider expanding the size of your capture region.");
		}
		MyRobot.FRAME.setAlwaysOnTop(true);
		
		img = MyRobot.MYROBOT.getSizedCapture(new Rectangle(0,0,MyRobot.screenSize.width,MyRobot.screenSize.height));
		FileUtils.deleteFile("calibration_data.txt");
		FileUtils.logToFile(Integer.toString(MyRobot.STARTDRAG.x), "calibration_data.txt");
		FileUtils.logToFile(Integer.toString(MyRobot.STARTDRAG.y), "calibration_data.txt");
		FileUtils.logToFile(Integer.toString(MyRobot.ENDDRAG.x), "calibration_data.txt");
		FileUtils.logToFile(Integer.toString(MyRobot.ENDDRAG.y), "calibration_data.txt");
	}
}

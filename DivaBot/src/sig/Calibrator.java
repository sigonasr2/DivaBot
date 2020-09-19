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

public class Calibrator{
	
	public static BufferedImage img;
	
	Calibrator() throws IOException, InterruptedException {
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
		
		img = MyRobot.MYROBOT.getSizedCapture(new Rectangle(0,0,MyRobot.screenSize.width,MyRobot.screenSize.height));
		ImageIO.write(img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture_5.png"));
		failed=CalibrationStage1();
		if (failed) {return;}
		failed=CalibrationStage2();
		if (failed) {return;}
		failed=CalibrationStage3();
		if (failed) {return;}
		failed=CalibrationStage4();
		if (failed) {return;}
		failed=CalibrationStage5();
		if (failed) {return;}
		failed=CalibrationStage6();
		if (failed) {return;}
		failed=CalibrationStage7();
		if (failed) {return;}
		failed=CalibrationStage8();
		if (failed) {return;}
		MyRobot.CALIBRATIONSTATUS="Calibration is complete! - X"+(MyRobot.STARTDRAG.x)+" Y"+(MyRobot.STARTDRAG.y)+" W"+(MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x)+" H"+(MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y)+" R"+((float)(MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x)/(MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y));

		MyRobot.FRAME.setCursor(Cursor.getDefaultCursor());

		Overlay.started=false;
		MyRobot.FRAME.setAlwaysOnTop(false);
		Overlay.OVERLAY.setVisible(true);
		if (((float)(MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x)/(MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y))<=16/9f-0.04||
				((float)(MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x)/(MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y))>=16/9f+0.04) {
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
		ImageIO.write(img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture_5.png"));
		FileUtils.deleteFile("calibration_data.txt");
		FileUtils.logToFile(Integer.toString(MyRobot.STARTDRAG.x), "calibration_data.txt");
		FileUtils.logToFile(Integer.toString(MyRobot.STARTDRAG.y), "calibration_data.txt");
		FileUtils.logToFile(Integer.toString(MyRobot.ENDDRAG.x), "calibration_data.txt");
		FileUtils.logToFile(Integer.toString(MyRobot.ENDDRAG.y), "calibration_data.txt");
	}

	private boolean CalibrationStage1() throws IOException, InterruptedException {
		boolean calibrated=false;
		int MAXTRIES=10000;
		MyRobot.CALIBRATIONSTATUS="Calibration Stage 1...";
		ImageIO.write(img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture.png"));
		while (!calibrated&&MAXTRIES>0) {
			//Try moving left until the difference is too high or the colors are not right anymore.
			MyRobot.STARTDRAG.x-=1;
			BufferedImage miniImg = img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,1,1);
			Color col = new Color(miniImg.getRGB(0, 0));
			System.out.println("Checking "+col);
			if (!((col.getRed()>=5&&col.getRed()<=100&&
					col.getGreen()>=170&&col.getGreen()<=210&&
					col.getBlue()>=180&&col.getBlue()<=250)||
					(col.getRed()<=30&&col.getGreen()<=30&&col.getBlue()<=30&&
					col.getRed()>=2&&col.getGreen()>=2&&col.getBlue()>=2)
				)) {
				//This is the max X. Calibration on this side good.
				MyRobot.STARTDRAG.x++;
				System.out.println("End at "+MyRobot.STARTDRAG.x);
				return false;
			}
			MAXTRIES--;
		}
		MyRobot.CALIBRATIONSTATUS="Calibration failed! Try making the capture region larger and defining it more accurately.";

		return true;
	}

	private boolean CalibrationStage2() throws IOException {
		boolean calibrated=false;
		int MAXTRIES=10000;
		MyRobot.CALIBRATIONSTATUS="Calibration Stage 2...";
		ImageIO.write(img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture_2.png"));
		while (!calibrated&&MAXTRIES>0) {
			//Try moving left until the difference is too high or the colors are not right anymore.
			MyRobot.STARTDRAG.y-=1;
			BufferedImage miniImg = img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,1,1);
			Color col = new Color(miniImg.getRGB(0, 0));
			System.out.println("Checking "+col);
			if (!((col.getRed()>=5&&col.getRed()<=100&&
					col.getGreen()>=170&&col.getGreen()<=210&&
					col.getBlue()>=180&&col.getBlue()<=250)||
					(col.getRed()<=30&&col.getGreen()<=30&&col.getBlue()<=30&&
					col.getRed()>=2&&col.getGreen()>=2&&col.getBlue()>=2)
				)) {
				//This is the max Y. Calibration on this side good.
				MyRobot.STARTDRAG.y++;
				System.out.println("End at "+MyRobot.STARTDRAG.y);
				return false;
			}
			MAXTRIES--;
		}
		MyRobot.CALIBRATIONSTATUS="Calibration failed! Try making the capture region larger and defining it more accurately.";
		return true;
	}

	private boolean CalibrationStage3() throws IOException, InterruptedException {
		boolean calibrated=false;
		int MAXTRIES=10000;
		MyRobot.CALIBRATIONSTATUS="Calibration Stage 3...";
		ImageIO.write(img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture_3.png"));
		while (!calibrated&&MAXTRIES>0) {
			//Try moving left until the difference is too high or the colors are not right anymore.
			MyRobot.ENDDRAG.x+=1;
			BufferedImage miniImg = img.getSubimage(MyRobot.ENDDRAG.x,MyRobot.ENDDRAG.y,1,1);
			Color col = new Color(miniImg.getRGB(0, 0));
			System.out.println("Checking "+col);
			if (!((col.getRed()>=40&&col.getRed()<=90&&
					col.getGreen()>=10&&col.getGreen()<=55&&
					col.getBlue()>=40&&col.getBlue()<=90)||
					(col.getRed()<=30&&col.getGreen()<=30&&col.getBlue()<=30&&
					col.getRed()>=2&&col.getGreen()>=2&&col.getBlue()>=2)
					)) {
				//This is the max X. Calibration on this side good.
				MyRobot.ENDDRAG.x--;
				System.out.println("End at "+MyRobot.STARTDRAG.x);
				return false;
			}
			MAXTRIES--;
		}
		MyRobot.CALIBRATIONSTATUS="Calibration failed! Try making the capture region larger and defining it more accurately.";

		return true;
	}

	private boolean CalibrationStage4() throws IOException {
		boolean calibrated=false;
		int MAXTRIES=10000;
		MyRobot.CALIBRATIONSTATUS="Calibration Stage 4...";
		ImageIO.write(img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture_4.png"));
		while (!calibrated&&MAXTRIES>0) {
			//Try moving left until the difference is too high or the colors are not right anymore.
			MyRobot.ENDDRAG.y+=1;
			BufferedImage miniImg = img.getSubimage(MyRobot.ENDDRAG.x,MyRobot.ENDDRAG.y,1,1);
			Color col = new Color(miniImg.getRGB(0, 0));
			System.out.println("Checking "+col);
			if (!((col.getRed()>=40&&col.getRed()<=90&&
					col.getGreen()>=10&&col.getGreen()<=55&&
					col.getBlue()>=40&&col.getBlue()<=90)||
					(col.getRed()<=30&&col.getGreen()<=30&&col.getBlue()<=30&&
					col.getRed()>=2&&col.getGreen()>=2&&col.getBlue()>=2)
					)) {
				//This is the max Y. Calibration on this side good.
				MyRobot.ENDDRAG.y--;
				System.out.println("End at "+MyRobot.ENDDRAG.y);
				return false;
			}
			MAXTRIES--;
		}
		MyRobot.CALIBRATIONSTATUS="Calibration failed! Try making the capture region larger and defining it more accurately.";
		return true;
	}

	private boolean CalibrationStage5() throws IOException, InterruptedException {
		boolean calibrated=false;
		int MAXTRIES=100;
		MyRobot.CALIBRATIONSTATUS="Calibration Stage 5...";
		ImageIO.write(img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture.png"));
		while (!calibrated&&MAXTRIES>0) {
			//Try moving left until the difference is too high or the colors are not right anymore.
			MyRobot.STARTDRAG.x+=1;
			for (int i=0;i<100;i++) {
				BufferedImage miniImg = img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y+i,1,1);
				Color col = new Color(miniImg.getRGB(0, 0));
				System.out.println("Checking "+col);
				if (((col.getRed()>=5&&col.getRed()<=100&&
						col.getGreen()>=170&&col.getGreen()<=210&&
						col.getBlue()>=180&&col.getBlue()<=250)||
						(col.getRed()<=30&&col.getGreen()<=30&&col.getBlue()<=30&&
						col.getRed()>=2&&col.getGreen()>=2&&col.getBlue()>=2)
					)) {
					//This is the max X. Calibration on this side good.
					MyRobot.STARTDRAG.y=MyRobot.STARTDRAG.y+i;
					System.out.println("End at "+MyRobot.STARTDRAG.x);
					return false;
				}
			}
			MAXTRIES--;
		}
		MyRobot.STARTDRAG.x-=100;
		return false;
	}

	private boolean CalibrationStage6() throws IOException {
		boolean calibrated=false;
		int MAXTRIES=100;
		MyRobot.CALIBRATIONSTATUS="Calibration Stage 6...";
		ImageIO.write(img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture_2.png"));
		while (!calibrated&&MAXTRIES>0) {
			//Try moving left until the difference is too high or the colors are not right anymore.
			MyRobot.STARTDRAG.y+=1;
			for (int i=0;i<100;i++) {
				BufferedImage miniImg = img.getSubimage(MyRobot.STARTDRAG.x+i,MyRobot.STARTDRAG.y,1,1);
				Color col = new Color(miniImg.getRGB(0, 0));
				System.out.println("Checking "+col);
				if (((col.getRed()>=5&&col.getRed()<=100&&
						col.getGreen()>=170&&col.getGreen()<=210&&
						col.getBlue()>=180&&col.getBlue()<=250)||
						(col.getRed()<=30&&col.getGreen()<=30&&col.getBlue()<=30&&
						col.getRed()>=2&&col.getGreen()>=2&&col.getBlue()>=2)
					)) {
					MyRobot.STARTDRAG.x=MyRobot.STARTDRAG.x+i;
					//This is the max Y. Calibration on this side good.
					System.out.println("End at "+MyRobot.STARTDRAG.y);
					return false;
				}
			}
			MAXTRIES--;
		}
		MyRobot.STARTDRAG.y-=100;
		return false;
	}

	private boolean CalibrationStage7() throws IOException, InterruptedException {
		boolean calibrated=false;
		int MAXTRIES=100;
		MyRobot.CALIBRATIONSTATUS="Calibration Stage 7...";
		ImageIO.write(img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture_3.png"));
		while (!calibrated&&MAXTRIES>0) {
			//Try moving left until the difference is too high or the colors are not right anymore.
			MyRobot.ENDDRAG.x-=1;
			for (int i=0;i<100;i++) {
				BufferedImage miniImg = img.getSubimage(MyRobot.ENDDRAG.x,MyRobot.ENDDRAG.y-i,1,1);
				Color col = new Color(miniImg.getRGB(0, 0));
				System.out.println("Checking "+col);
				if (((col.getRed()>=40&&col.getRed()<=90&&
						col.getGreen()>=10&&col.getGreen()<=55&&
						col.getBlue()>=40&&col.getBlue()<=90)||
						(col.getRed()<=30&&col.getGreen()<=30&&col.getBlue()<=30&&
						col.getRed()>=2&&col.getGreen()>=2&&col.getBlue()>=2)
						)) {
					//This is the max X. Calibration on this side good.
					MyRobot.ENDDRAG.y=MyRobot.ENDDRAG.y-i;
					MyRobot.ENDDRAG.x++;
					System.out.println("End at "+MyRobot.STARTDRAG.x);
					return false;
				}
			}
			MAXTRIES--;
		}
		MyRobot.ENDDRAG.x+=100;
		return false;
	}

	private boolean CalibrationStage8() throws IOException {
		boolean calibrated=false;
		int MAXTRIES=100;
		MyRobot.CALIBRATIONSTATUS="Calibration Stage 8...";
		ImageIO.write(img.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture_4.png"));
		while (!calibrated&&MAXTRIES>0) {
			//Try moving left until the difference is too high or the colors are not right anymore.
			MyRobot.ENDDRAG.y-=1;
			for (int i=0;i<100;i++) {
				BufferedImage miniImg = img.getSubimage(MyRobot.ENDDRAG.x-i,MyRobot.ENDDRAG.y,1,1);
				Color col = new Color(miniImg.getRGB(0, 0));
				System.out.println("Checking "+col);
				if (((col.getRed()>=40&&col.getRed()<=90&&
						col.getGreen()>=10&&col.getGreen()<=55&&
						col.getBlue()>=40&&col.getBlue()<=90)||
						(col.getRed()<=30&&col.getGreen()<=30&&col.getBlue()<=30&&
						col.getRed()>=2&&col.getGreen()>=2&&col.getBlue()>=2)
						)) {
					MyRobot.ENDDRAG.x=MyRobot.ENDDRAG.x-i;
					//This is the max Y. Calibration on this side good.
					System.out.println("End at "+MyRobot.ENDDRAG.y);
					return false;
				}
			}
			MAXTRIES--;
		}
		MyRobot.ENDDRAG.y+=100;
		return false;
	}
}

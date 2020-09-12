package sig.utils;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import sig.MyRobot;

public class MouseUtils {
	public static Point GetCursorPosition(JFrame f, MouseEvent e) {
		Point cursor = e.getPoint();
		cursor.translate(-f.getInsets().left,-f.getInsets().top);
		return cursor;
	}
}

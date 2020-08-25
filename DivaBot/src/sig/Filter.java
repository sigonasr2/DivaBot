package sig;

import java.awt.Color;
import java.awt.Point;

public class Filter {
	Point red_threshold;
	Point green_threshold;
	Point blue_threshold;
	
	public Filter(Point red_threshold, Point green_threshold, Point blue_threshold) {
		this.red_threshold = red_threshold;
		this.green_threshold = green_threshold;
		this.blue_threshold = blue_threshold;
	}

	public boolean isWithinThreshold(Color c) {
		return c.getRed()>=red_threshold.x && c.getRed()<=red_threshold.y &&
				c.getGreen()>=green_threshold.x && c.getGreen()<=green_threshold.y &&
				c.getBlue()>=blue_threshold.x && c.getBlue()<=blue_threshold.y;
	}
}

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Tile {
	static final int tileWidth = 100; //maybe 80
	static final int tileHeight = 100;
	static int arcWidth = 15;
	static int arcHeight = 15;

	public int value;
	public boolean mergeable = true;

	private static Color BGcolor;
	private static Color textColor;
	private Font font;
	
	public Tile(int value) {
		this.value = value;
	}
		

	public static Color setBGColor(int value) {
		if(value == 2) {
			BGcolor = Color.WHITE;
		} else if (value == 4) {
			BGcolor = Color.WHITE;
		} else if (value == 8) {
			BGcolor = new Color(255, 204, 0); //yellow
		} else if (value == 16) {
			BGcolor = new Color(255, 153, 0); //orange
		} else if (value == 32) {
			BGcolor = new Color(204, 51, 0); //red
		} else if (value == 64) {
			BGcolor = new Color(255, 153, 153); //pinkish red
		} else if (value == 128) {
			BGcolor = new Color(204, 0, 102); //magenta
		} else if (value == 256) {
			BGcolor = new Color(204, 102, 153); //purple-ish pink
		} else if (value == 512) {
			BGcolor = new Color(153, 0, 153); //pink
		} else if (value == 1024) {
			BGcolor = new Color(255, 153, 255); //light purple
		} else if (value == 2048) {
			BGcolor = new Color(159, 255, 128); //bright blue
		} else if (value > 2048 ) {
			BGcolor = Color.BLACK;
		}
		
		return BGcolor;
	}
	
	public static Color setTextColor(int value) {
		if(value < 8) {
			textColor = Color.DARK_GRAY;
		} else if (value <= 2048) {
			textColor = Color.WHITE;
		} else if (value > 2048) {
			textColor = Color.PINK;
		}
		return textColor;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void Merged() {
		mergeable = false;
	}
	
	public void clearMerged() {
		mergeable = true;
	}
	
	public boolean mergeable() {
		return mergeable;
	}


}

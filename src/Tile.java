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

	private static Color BGcolor;
	private static Color textColor;
	private Font font;
	
	public Tile() {
	}
	
	public Tile(int value) {
		this.value = value;
	}
		

	public static Color setBGColor(int value) {
		switch(value) {
		case 2: BGcolor = Color.WHITE;
		case 4: BGcolor = Color.WHITE;
		case 8: BGcolor = new Color(255, 133, 51);
		case 16: BGcolor = new Color(230, 92, 0);
		case 32: BGcolor = new Color(255, 51, 0);
		case 64: BGcolor = new Color(230, 184, 0);
		case 128: BGcolor = new Color(255, 224, 102);
		case 256: BGcolor = new Color(255, 224, 102);
		case 512: BGcolor = new Color(255, 153, 153);
		case 1024: BGcolor = new Color(255, 102, 153);
		case 2048: BGcolor = new Color(159, 255, 128);
		default: BGcolor = Color.WHITE;
		}
		
		return BGcolor;
	}
	
	public static Color setTextColor(int value) {
		if(value < 8) {
			textColor = Color.DARK_GRAY;
		} else if (value < 2048) {
			textColor = Color.WHITE;
		} else if (value == 2048) {
			textColor = Color.GREEN;
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
	
	public boolean isEmplty() {
		return value == 0;
	}

}

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GameBoard {

	public static final int row = 4;
	public static final int col = 4;

	private final int startTile = 2;
	private Tile[][] board;
	private boolean lose;
	private boolean win;
	private BufferedImage game;
	private BufferedImage finalGame;
	private int x;
	private int y;

	private static int spacing = 10;
	public int WIDTH = 5*10 + 4*Tile.tileWidth;
	public int HEIGHT = 5*10 + 4*Tile.tileHeight;


	public GameBoard(int x, int y) {
		this.x = x;
		this.y = y;
		board = new Tile[row][col];
		game = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		finalGame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		paintGame();
	}
	public void paintGame() {
		Graphics2D g = (Graphics2D) game.getGraphics();
		g.setColor(new Color(204, 191, 179));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(new Color(92, 77, 61));
		for(int i = 0; i < 4; i++) {
			for (int j = 0; j <4; j++) {
				int x = spacing + spacing*col + Tile.tileWidth*col;
				int y = spacing + spacing*col + Tile.tileHeight*col;
				g.fillRoundRect(x, y, Tile.tileWidth, Tile.tileHeight, Tile.arcWidth, Tile.arcHeight);
			}
		}	
	}

	public void render (Graphics2D g) {
		Graphics2D p = (Graphics2D) finalGame.getGraphics();
		p.drawImage(game, 0, 0, null); 

		//draw tiles
		g.drawImage(finalGame, x, y, null);
		p.dispose();

	}

	public void update() {

	}

}
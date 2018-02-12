import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Game extends JPanel implements KeyListener, ActionListener{
	public static final int Width = 470;
	public static final int Height = 600;

	public final int target = 2048; //target variable to reach 
	static int biggestTile; //highest tile reached
	static int score; //current score
	static int highscore;

	public Color gridColor = new Color(192, 165, 140); //colour of lines of grid
	public Color backgroundColor = new Color(210, 192, 173); //colour of background of grid

	private Random rand = new Random(); //to generate 2 random tiles in the begining
	
	private Tile[][] tiles; //to create tiles
	public boolean MovesAvailable = true; 
	public boolean isRunning = false; //to check state of game - running or stopped. 
	public boolean lostGame = false; //to check status of game - win or loose. 
	public boolean wonGame = false;
	
	JButton start;
	private int emptySpaces = 0;
	
			
	public Game() {
		setLayout(null);
		setPreferredSize(new Dimension(470, 600));
		setFocusable(true); //allows keyboard input 
		KeyListener key = null;
		addKeyListener(key);
		requestFocusInWindow();
	
		
		start = new JButton("START");
		start.setBounds(342, 100, 80, 30);
		start.setBackground(backgroundColor);
		start.setFocusPainted(false);
		start.addActionListener(this);
		this.add(start);
	
		
		//render();
	}
	
	public void start() {
			
		if(!isRunning) {
			isRunning = true;
			start.setText("REFRESH");
		}
			tiles = new Tile[4][4];
			score = 0;
			biggestTile = 0;
			addRandomTile();
			addRandomTile();
			repaint();
		
	}
	
	@Override
	public void paintComponent(Graphics a) {
		super.paintComponents(a);
		Graphics2D g = (Graphics2D) a;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		drawGrid(g);
		
	}
	
	public void drawGrid(Graphics2D g) {
		//stats
			//title
			g.setFont(new Font("FunSized", Font.BOLD, 75));
			g.setColor(new Color(49, 38, 27));
			g.drawString("2048", 10, 70);
			
			//scores
			g.setColor(backgroundColor);
			g.fillRect(274, 13, 65, 60); //score
			g.setColor(gridColor);
			g.fillRect(274+65, 13, 120, 60); //high score
			g.fillRoundRect(277+65, 100, 80, 30, 15, 15); //start
			g.setFont(new Font("ArialBlack", Font.BOLD, 18));
			g.setColor(Color.white);
			g.drawString("Score", 280, 30);
			g.drawString("High Score", 350, 30);
			g.drawString(""+score, 300, 57);
			g.drawString(""+highscore, 400, 57);
			g.drawString("START", 277+75, 123);
			g.setColor(new Color(49, 38, 27));
			g.setFont(new Font("ArialBlack", Font.BOLD, 14));
			g.drawString("Join the numbers and get to the 2048 tile!", 10, 120);
			
			
			
		//grid
				g.setColor(gridColor);
				g.fillRoundRect(10, 160, 450, 450, 15, 15);
				
				if(isRunning) {
					for(int i = 0; i <4; i++) {
						for (int j = 0; j < 4; j++) {
							
							if(tiles[i][j] == null) {
								g.setColor(backgroundColor);
								int x = 20 + 10*i + Tile.tileWidth*i;
								int y = 170 + 10*j + Tile.tileHeight*j;
								g.fillRoundRect(x, y, 100, 100, 15, 15);
							} else {
								drawTile(g, i, j);
							}
							
						}
					}
				}else if(!isRunning && lostGame) {
					g.setColor(Color.BLACK);
					g.setFont(new Font("FunSized", Font.BOLD, 20));
					g.drawString("Game Over", 155, 365);
				}else if(!isRunning && wonGame) {
					g.setColor(Color.BLACK);
					g.setFont(new Font("FunSized", Font.BOLD, 20));
					g.drawString("You Won!", 155, 365);
				}else {
					g.setColor(Color.BLACK);
					g.setFont(new Font("FunSized", Font.BOLD, 20));
					g.drawString("Click START", 155, 365);
				}
				
				
	}
	
	public void drawTile(Graphics2D g, int row, int col) {
		int value = tiles[row][col].getValue();
		g.setColor(Tile.setBGColor(value));
		int x = 20 +10*row + 100*row;
		int y = 170 +10*col + 100*col;
		g.fillRoundRect(x, y, 100, 100, 15, 15);
		
		g.setFont(new Font("FunSized", Font.PLAIN, 35));
		FontMetrics fontM = g.getFontMetrics();
		g.setColor(Tile.setTextColor(value));
		String s = String.valueOf(value);
		g.drawString(s, x + 50 - fontM.stringWidth(s)/2 , y + 55);
		
	}
	
	public void addRandomTile() {
		
		ArrayList<Integer> r = new ArrayList<Integer>();
		ArrayList<Integer> c = new ArrayList<Integer>();

		List<Tile> availableSpace = new ArrayList<Tile>();
		for(int i = 0; i<4; i++) {
			for(int j = 0; j <4; j++) {
				if(tiles[i][j] == null) {
					availableSpace.add(tiles[i][j]);
					r.add(i);
					c.add(j);
				} else {
					continue;
				}
			}
		}
		
		if(availableSpace.isEmpty()) {
			MovesAvailable = false;
			isRunning = false;
			repaint();
		} else {
			int index = rand.nextInt(availableSpace.size());
			tiles[r.get(index)][c.get(index)] = new Tile(2);
		}
	
	}
	
	public int emptySpaces() {
		emptySpaces = 0;
		for(int i = 0; i<4; i++) {
			for(int j = 0; j <4; j++) {
				if(tiles[i][j] == null) {
					emptySpaces ++;
				} else {
					continue;
				}
			}
		}
		return emptySpaces;
	}
	
	public boolean isAvailable() {
		if(emptySpaces == 0) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e);
		if(!isAvailable()) {
			lostGame = true;
		}
		
		if(!wonGame && !lostGame && isRunning) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_RIGHT: 
				moveRight();
				break;
			case KeyEvent.VK_LEFT: 
				moveLeft();
				break;
			case KeyEvent.VK_UP: 
				moveUp();
				break;
			case KeyEvent.VK_DOWN: 
				moveDown();
				break;
			}	
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	public void moveRight() {
		for(int i = 0; i < 4; i++) {
			for (int j = 2; j <=0; j--) {
				for(int k = j; k < 3; k++) {
					int next = k+1;
					int val = tiles[i][k].getValue();
					Tile cur = tiles[i][k];
					if(tiles[i][k] == null) {
						continue;
				
					} else if(tiles[i][next] == null) {
						tiles[i][next] = cur;
						tiles[i][k] = null;
						break;
						
					} else if (tiles[i][next].getValue() == val) {
						tiles[i][next].setValue(val*2);
						tiles[i][k] = null;
						val = 2*val;
						score += val;
						break;
					}
				}
			}
		}
		addRandomTile();
	}
	
	
	public void moveLeft() {
	
	}
	
	public void moveUp() {
		
	}
	
	public void moveDown() {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == start) {
			start();
		}
		
	}
	
	
}

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

import javax.swing.JButton;
import javax.swing.JPanel;

public class Game extends JPanel{
	public static final int Width = 470;
	public static final int Height = 600;

	public final int target = 2048; //target variable to reach 
	static int biggestTile = 2; //highest tile reached
	static int score; //current score
	static int highscore = 0;

	public Color gridColor = new Color(192, 165, 140); //colour of lines of grid
	public Color backgroundColor = new Color(210, 192, 173); //colour of background of grid

	private Random rand = new Random(); //to generate 2 random tiles in the begining
	
	private Tile[][] tiles; //to create tiles
	public boolean MovesAvailable = true; 
	public boolean isRunning = false; //to check state of game - running or stopped. 
	public boolean lostGame = false; //to check status of game - win or loose. 
	public boolean wonGame = false;
	public boolean pauseGame = false;
	
	public boolean movesR = true;
	public boolean movesL = true;
	public boolean movesU = true;
	public boolean movesD = true;
	
	public int moves = 0;
	
	JButton start;
	
	
	public Game() {
		setLayout(null);
		setPreferredSize(new Dimension(470, 600));
		setFocusable(true); //allows keyboard input 
		
		addKeyListener (new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {	
				if(e.getKeyCode() == KeyEvent.VK_SPACE){
					if(pauseGame) {
						System.out.println("\n>>>> Game Resumed <<<<");
						isRunning = true;
						pauseGame = false;
						repaint();
					} else {
						start();
						System.out.println(">>>> Game Started <<<<");
					}	
				}
				
				if(pauseGame) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						System.out.println("\n>>> Game Exited <<<<");
						System.exit(0);
					}
				}

				
				if(isRunning) {
						String s = "";
						switch(e.getKeyCode()) {
						case KeyEvent.VK_RIGHT: 
							moveRight();
							s += "Right Key Pressed";
							break;
						case KeyEvent.VK_LEFT: 
							moveLeft();
							s += "Left Key Pressed";
							break;
						case KeyEvent.VK_UP: 
							moveUp();
							s += "Up Key Pressed";
							break;
						case KeyEvent.VK_DOWN: 
							moveDown();
							s += "Down Key Pressed";
							break;
						case KeyEvent.VK_SPACE:
							break;
						case KeyEvent.VK_ESCAPE:
							isRunning = false;
							pauseGame = true;
							break;
						default: s += "Invalid Key Pressed >>> " + e.getKeyCode();
						}
						
						if(pauseGame) {
							System.out.println("\nGame paused: User wishes to exit >> Getting confirmation");
						} else {
							System.out.println(s + "\nMoves: " + moves + " Highest Tile on Board: " + biggestTile + "\n");
							for(int i = 0; i <4; i ++) {
								for(int j = 0; j <4; j++) {
									tiles[i][j].clearMerged();
								}
							}
						}
						
						if(!movesR && !movesL && !movesU && !movesD) {
							isRunning = false;
							lostGame = true;
						}
						
					} 
					
					repaint();

				}		
		});

	}
	
	public void start() {
		//set game board values
			tiles = new Tile[4][4];
			for(int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++){
					tiles[i][j] = new Tile(0);
					}
						}
			//set score values
			if(score> highscore) {
				highscore = score;
			}
			score = 0;
			biggestTile = 0;
			moves = 0;
			//set game state 
			isRunning = true;
			movesR = true; movesL = true; movesU = true; movesD = true;
			//add random tiles
			addRandomTile();
			addRandomTile();
			//render game
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
			g.setFont(new Font("ArialBlack", Font.BOLD, 18));
			g.setColor(Color.white);
			g.drawString("Score", 280, 30);
			g.drawString("High Score", 350, 30);
			g.drawString(""+score, 280, 57);
			g.drawString(""+highscore, 400, 57);
			
			g.setColor(new Color(49, 38, 27)); //instructions
			g.setFont(new Font("ArialBlack", Font.BOLD, 14));
			g.drawString("Join the numbers and get to the 2048 tile!", 10, 130);
			
			
			
		//grid
				g.setColor(gridColor);
				g.fillRoundRect(10, 160, 450, 450, 15, 15);
				
				if(isRunning) {
					for(int i = 0; i <4; i++) {
						for (int j = 0; j < 4; j++) {
							
							if(tiles[i][j].getValue() == 0) {
								g.setColor(backgroundColor);
								int x = 20 + 10*j + Tile.tileWidth*j;
								int y = 170 + 10*i + Tile.tileHeight*i;
								g.fillRoundRect(x, y, 100, 100, 15, 15);
							} else {
								drawTile(g, i, j);
							}
							
						}
					}
				}else if(!isRunning && lostGame) {
					g.setColor(Color.BLACK);
					g.setFont(new Font("FunSized", Font.BOLD, 20));
					g.drawString("No More Moves!", 125, 345);
					g.drawString("Biggest Tile: " + biggestTile, 125, 405);
					g.drawString("Valid Moves Made: " + moves, 125, 445);
					
				}else if(!isRunning && wonGame) {
					g.setColor(Color.BLACK);
					g.setFont(new Font("FunSized", Font.BOLD, 20));
					g.drawString("You Won!", 155, 365);
				} else if(!isRunning && pauseGame){
					g.setColor(backgroundColor);
					g.fillRoundRect(10, 160, 450, 450, 15, 15);
					g.setColor(Color.white);
					g.drawString("Are you sure you want to quit?", 130, 360);
					g.drawString("SPACE to Resume", 110, 377);
					g.drawString("ENTER to Quit", 270, 377);
				}else {
					g.setColor(Color.BLACK);
					g.setFont(new Font("FunSized", Font.BOLD, 20));
					g.drawString("Press SPACE to START", 95, 385);
				}
				
				
	}
	
	public void drawTile(Graphics2D g, int row, int col) {
		int value = tiles[row][col].getValue();
		
		g.setColor(Tile.setBGColor(value));
		int x = 20 +10*col + 100*col;
		int y = 170 +10*row + 100*row;
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
					if(tiles[i][j].getValue() == 0) {
						availableSpace.add(tiles[i][j]);
						r.add(i);
						c.add(j);
					} else {
						continue;
					}
				}
			} 
				int index = rand.nextInt(availableSpace.size());
				int probablity = rand.nextInt(5);
				if(probablity < 4) {
					tiles[r.get(index)][c.get(index)] = new Tile(2); // probability of 0.8 (4 of 5)
				} else {
					tiles[r.get(index)][c.get(index)] = new Tile(4); //probability of 0.2 (1 of 5)
				}
				
	}
	
	public int emptySpaces() {
		int emptySpaces = 0;
		for(int i = 0; i<4; i++) {
			for(int j = 0; j <4; j++) {
				if(tiles[i][j].getValue() == 0) {
					emptySpaces ++;
				} else {
					continue;
				}
			}
		}
		return emptySpaces;
	}

	
	public void FindbiggestTile() {
		for(int i = 0; i<4; i++) {
			for(int j = 0; j<4; j++) {
				int val = tiles[i][j].getValue();
				if(val == 0) {
					continue;
				}
				
				if(val > biggestTile) {
					biggestTile = val;
				}
			}
		}
	}
	
	
	public void moveRight() {
		int valid = 0;
		for(int i = 0; i < 4; i++) {
			for (int j = 2; j >=0; j--) {
				for(int k = j; k < 3; k++) {
					int val = tiles[i][k].getValue();
					if(tiles[i][k].getValue()==0) {
						continue;
					} else {
						if((tiles[i][k+1].getValue() == 0)) {
							tiles[i][k+1].setValue(val);
							//tiles[i][k+1].setValue(tiles[i][k].getValue());
							tiles[i][k].setValue(0);
							valid++;
						
						} else if (tiles[i][k+1].getValue() == val) {
							if(tiles[i][k+1].mergeable()&& tiles[i][k].mergeable()) {
								tiles[i][k+1].setValue(val*2);
								score += val*2;
								tiles[i][k+1].Merged();
								tiles[i][k].setValue(0);
								valid++;
							}
			
						}
					}
					
				}
			}
		}
		if(valid > 0) {
			addRandomTile();
			System.out.println("Valid Move");
			moves++;
		} else {
			System.out.println("Invalid Move");
			movesR = false;
		}
		FindbiggestTile();
	}
	
	public void moveLeft() {
		int valid = 0;
		for(int i = 0; i < 4; i++) {
			for (int j=1; j < 4; j++) {
				for(int k = j; k > 0; k--) {
					int val = tiles[i][k].getValue();
					if(tiles[i][k].getValue()==0) {
						continue;
					} else {
						if((tiles[i][k-1].getValue() == 0)) {
							tiles[i][k-1].setValue(val);
							tiles[i][k].setValue(0);
							valid++;
						} else if (tiles[i][k-1].getValue() == val) {
							if(tiles[i][k-1].mergeable()&& tiles[i][k].mergeable()) {
								tiles[i][k-1].setValue(val*2);
								score += val*2;
								tiles[i][k-1].Merged();
								tiles[i][k].setValue(0);
								valid++;
							}
							
							
						}
					}
					
				}
			}
		}
		if(valid > 0) {
			addRandomTile();
			System.out.println("Valid Move");
			moves++;
		}else {
			System.out.println("Invalid Move");
			movesL = false;
		}
		FindbiggestTile();
	}
	
	public void moveUp() {
		int valid = 0;
		for(int j = 0; j < 4; j++) {
			for (int i=1; i < 4; i++) {
				for(int k = i; k > 0; k--) {
					int val = tiles[k][j].getValue();
					if(tiles[k][j].getValue()==0) {
						continue;
					} else {
						if((tiles[k-1][j].getValue() == 0)) {
							tiles[k-1][j].setValue(val);
							tiles[k][j].setValue(0);
							valid ++;
						} else if (tiles[k-1][j].getValue() == val) {
							if(tiles[k-1][j].mergeable() && tiles[k][j].mergeable) {
								tiles[k-1][j].setValue(val*2);
								score += val*2;
								tiles[k-1][j].Merged();
								tiles[k][j].setValue(0);
								valid ++;
							}	
						}
					}
				}
			}
		}
		if(valid > 0) {
			addRandomTile();
			System.out.println("Valid Move");
			moves++;
		}else {
			System.out.println("Invalid Move");
			movesU = false;
		}
		
		FindbiggestTile();
	}
	
	public void moveDown() {
		int valid = 0;
			for(int j = 0; j < 4; j++) {
				for (int i=2; i >= 0; i--) {
					for(int k = i; k <3; k++) {
						int val = tiles[k][j].getValue();
						if(tiles[k][j].getValue()==0) {
							continue;
						} else {
							if((tiles[k+1][j].getValue() == 0)) {
								tiles[k+1][j].setValue(val);
								tiles[k][j].setValue(0);
								valid ++;
							} else if (tiles[k+1][j].getValue() == val) {
								if(tiles[k+1][j].mergeable() && tiles[k][j].mergeable()) {
									tiles[k+1][j].setValue(val*2);
									score += val*2;
									tiles[k+1][j].Merged();
									tiles[k][j].setValue(0);
									valid++;
								}
									
							}
						}
						
					}
				}
			}
			if(valid > 0) {
				addRandomTile();
				System.out.println("Valid Move");
				moves++;
			}else {
				System.out.println("Invalid Move");
				movesD = false;
			}
		FindbiggestTile();
	}
	
}

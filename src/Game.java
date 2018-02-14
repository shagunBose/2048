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
	public static final int Width = 470; //screen size of the panel
	public static final int Height = 600;
	
	//state of game
	public boolean MovesAvailable = true; 
	public boolean isRunning = false; //to check state of game - running or stopped. 
	public boolean lostGame = false; //to check status of game - win or loose. 
	public boolean wonGame = false;
	public boolean refreshGame = false;
	public boolean quitGame = false;
	
	//score
	public final int target = 2048; //target variable to reach 
	static int biggestTile = 2; //highest tile reached
	static int score; //current score
	static int highscore = 0;

	//graphics
	public Color gridColor = new Color(192, 165, 140); //colour of lines of grid
	public Color backgroundColor = new Color(210, 192, 173); //colour of background of grid
	
	//Tiles
	private Tile[][] tiles; //to create tiles
	public boolean movesR = true;
	public boolean movesL = true;
	public boolean movesU = true;
	public boolean movesD = true;
	public int moves = 0; //counts number of valid moves 
	private Random rand = new Random(); //to generate 2 random tiles in the beginning
	
	Sound sound = new Sound();
	
	//constructor! 
	public Game() {
		setLayout(null);
		setPreferredSize(new Dimension(470, 600));
		setFocusable(true); //allows keyboard input 
		addKeyListener (new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {	
				//Checks if any more moves can be made for each side. 
				//if no more moves available, game state changes from isRunning to lostGame. 
				
				if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if(isRunning) {
						moveRight();
						System.out.println("Right Key Pressed");
						printToConsole();
					}
					
				} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					if(isRunning) {
						moveLeft();
						System.out.println("Right Key Pressed");
						printToConsole();
					}
					
					
				} else if(e.getKeyCode() == KeyEvent.VK_UP) {
					if(isRunning) {
						moveUp();
						System.out.println("Up Key Pressed");
						printToConsole();
					}
					
				} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					if(isRunning) {
						moveDown();
						System.out.println("Down Key Pressed");
						printToConsole();
					}
					
				} else if(e.getKeyCode() == KeyEvent.VK_R) { //r for restart
					isRunning = false;
					refreshGame = true;
					System.out.println("\nGame paused: User wishes to restart >> Getting confirmation");
				
				}else if(e.getKeyCode() == KeyEvent.VK_Q) { //q for quit game
					isRunning = false;
					quitGame = true;
					System.out.println("\nGame paused: User wishes to exit >> Getting confirmation");
				
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER){
					if(lostGame || refreshGame) {
						isRunning = true;
						start();
						lostGame = false;
						refreshGame = false;
					}
					if(quitGame || wonGame) {
						System.out.println("\n>>>> Game Resumed <<<<");
						isRunning = true;
						quitGame = false;
						wonGame = false;
					}
					
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if(quitGame || wonGame || lostGame) {
						System.out.println("\n>>> Game Exited <<<<");
						System.exit(0);
					}
					if(refreshGame) {
						System.out.println("\n>>>> Game Resumed <<<<");
						isRunning = true;
						refreshGame = false;
					}
					
				} else {
					System.out.println(">>>> Invalid Key Pressed: " + e.getKeyChar() + " <<<<\n");
				}
				
				if(!movesR && !movesL && !movesU && !movesD) {
					isRunning = false;
					lostGame = true;
					sound.lostGame();
				}
				
				repaint(); //after every key event - the game board is updated.
				}		
		});
		
		start();
	}
	
	public void printToConsole() {
		//the system logs the number of valid moves and highest tile on the board
		System.out.println("Moves: " + moves + " Highest Tile on Board: " + biggestTile + "\n");
		for(int i = 0; i <4; i ++) {
			for(int j = 0; j <4; j++) {
				tiles[i][j].clearMerged();
			}
		}
	}
	
	public void start() {
		System.out.println(">>>> New Game Started <<<<");
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
			wonGame = false;
			lostGame = false;
			movesR = true; movesL = true; movesU = true; movesD = true; //at the beginning, moves in all directions is possible
			
			//add 2 random tiles
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
		//this method draws the grid for the game. 
		
		//statistics - title, score and instructions. 
			//title
			g.setFont(new Font("FunSized", Font.BOLD, 75));
			g.setColor(new Color(49, 38, 27));
			g.drawString("2048", 10, 70);
			
			//scores
			g.setColor(backgroundColor);
			g.fillRect(274, 13, 92, 60); //score
			g.setColor(gridColor);
			g.fillRect(274+92, 13, 92, 60); //high score
			g.setFont(new Font("ArialBlack", Font.BOLD, 18));
			g.setColor(Color.white);
			g.drawString("Score", 280, 30);
			g.drawString("Best", 372, 30);
			g.drawString(""+score, 280, 57);
			g.drawString(""+highscore, 372, 57);
			
			//instruction
			g.setColor(new Color(49, 38, 27)); //instructions
			g.setFont(new Font("ArialBlack", Font.BOLD, 14));
			g.drawString("Join the numbers and get to the 2048 tile!", 10, 120);
			g.drawString("R: Restart  Q: Quit  Arrow Keys: Move Tiles", 10, 140);
			
			
			
		//grid
			g.setColor(gridColor);
			g.fillRoundRect(10, 160, 450, 450, 15, 15); //draws the background of the grid - will appear as the lines of the grid.
			
			//the board will look different at different times in the game-state. 
			//when the state of the game = isRunning; the board will have the tiles on it. 
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
			
			//if the user lost the game - the board will display an informational message
			}else if(!isRunning && lostGame) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("FunSized", Font.BOLD, 20));

				g.drawString("No More Moves!", 125, 315);
				g.drawString("Biggest Tile: " + biggestTile, 125, 345);
				g.drawString("Valid Moves Made: " + moves, 125, 375);
				g.drawString("Press ENTER to Restart", 125, 435);
				g.drawString("Or ESC to Quit", 125, 465);

			//if the user won the game (got 2048 tile) - the board displays an information message. 
			}else if(!isRunning && wonGame) {
				System.out.println("\n>>>> 2048 <<<<<");
				g.setColor(Color.BLACK);
				g.setFont(new Font("FunSized", Font.BOLD, 20));
				g.drawString("You Won!", 155, 365);
				g.drawString("ENTER to Resume", 110, 377);
				g.drawString("ESC to Quit", 270, 377);
			
			// if the user wanted to exit the game - the board asks for confirmation
			} else if(!isRunning && refreshGame){
				g.setColor(backgroundColor);
				g.fillRoundRect(10, 160, 450, 450, 15, 15);
				g.setColor(Color.white);
				g.drawString("Are you sure you want to restart?", 130, 360);
				g.drawString("ESC to Resume", 110, 377);
				g.drawString("ENTER to Restart", 270, 377);
			
			//in the start - the board displays instructions. 
			}else if(!isRunning && quitGame){
				g.setColor(backgroundColor);
				g.fillRoundRect(10, 160, 450, 450, 15, 15);
				g.setColor(Color.white);
				g.drawString("Are you sure you want to quit?", 130, 360);
				g.drawString("ENTER to Resume", 110, 377);
				g.drawString("ESC to Quit", 270, 377);

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
	
	//adds a random tile to an empty space on the board 
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
				
				
				sound.playSound(); //play pop sound 
	}
	
	//calculates and returns how many empty spaces are there in the tile array when called
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

//this finds the biggest tile on the board
	public void FindbiggestTile() {
		for(int i = 0; i<4; i++) {
			for(int j = 0; j<4; j++) {
				int val = tiles[i][j].getValue();
				if(val == 0) {
					continue;
				}
				
				if(val > biggestTile) {
					biggestTile = val;
					if(biggestTile == 2048) {
						wonGame = true;
						isRunning = false;
					}
				}
				
			}
		}
	}
	
	//the following methods are used to use the tiles around the board. 
	//in each method the logic followed is the same - for each row/column, for each tile, each possible move is checked and executed is it is possible. 
	//for that reason I have only added comments to the move right function instead of all of them.
	public void moveRight() {
		int valid = 0;
		for(int i = 0; i < 4; i++) {
			for (int j = 2; j >=0; j--) {
				for(int k = j; k < 3; k++) {
					int val = tiles[i][k].getValue();
					if(tiles[i][k].getValue()==0) { //if the tile value is 0 that is the same is a tile that doesn't exist. 
						continue;
					} else {
						if((tiles[i][k+1].getValue() == 0)) { //if the next tile is 0, shift this tile to the next 
							tiles[i][k+1].setValue(val);
							tiles[i][k].setValue(0);
							valid++; //increment valid moves
						
						} else if (tiles[i][k+1].getValue() == val) { //if the next tile has the same value as this one, merge the two slide.
							if(tiles[i][k+1].mergeable()&& tiles[i][k].mergeable()) {
								tiles[i][k+1].setValue(val*2);
								score += val*2;
								tiles[i][k+1].Merged(); //once a tile is merged in one key stroke, it is not merged again.
								tiles[i][k].setValue(0); 
								valid++; //increment valid moves
								sound.playSound();
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
		} else { //if there are no more valid moves in this direction, an informational message is printed and movesR is set to false. 
			System.out.println("Invalid Move");
			movesR = false;
		}
		FindbiggestTile(); //calls find biggest tile function to update state of board on what the biggest tile is now. 
	}
	
	//please see commented note above moveRight();
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
								sound.playSound();
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
	
	//please see commented note above moveRight();
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
								sound.playSound();
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
	
	//please see commented note above moveRight();
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
									sound.playSound();
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

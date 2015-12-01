import java.util.*;

public class BattleshipModel {
	// Model state
	private BoardSquare[][] board;
	private String player1Name;
	private String player2Name;
	private int player1ShipCount;
	private int player2ShipCount;
	private boolean isGameOver;
	
	// Private class constant values
	private static final int BOARD_WIDTH = 10;
	private static final int BOARD_HEIGHT = 10;
	private static final String LETTERS = "ABCDEFGHIJ";
	
	//Constructor
	public BattleshipModel(String player1, String player2) {
		this.player1Name = player1;
		this.player2Name = player2;
		player1ShipCount = 0;
		player2ShipCount = 0;
		isGameOver = false;
		
		//Create new board, and populate it with BoardSquares
		board = new BoardSquare[BOARD_WIDTH][BOARD_HEIGHT];
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			for (int col = 0; col < BOARD_WIDTH; col++) {
				board[row][col] = new BoardSquare();
			}
		}
	}
	
	//ship should be 'A', 'B', 'C', or 'D'
	//loc should be validated
	//Orientation should be determined by the view/controller from user input
	public boolean placeShip(boolean isPlayer1, char ship, String loc, Orientation o) {
		Ship shipToPlace = getShip(ship);
		int startRow = getRow(loc);
		int startCol = getCol(loc);
		int dx = o.dx;
		int dy = o.dy;
		
		// attempt to place the ship in each BoardSquare
		for (int i = 0; i < shipToPlace.getLength(); i++) {
			int row = startRow + i * dy;
			int col = startCol + i * dx;
			Ship locToChange = (isPlayer1) ? board[row][col].P1Ship : board[row][col].P2Ship;
			
			if (row < 0 || row > 9 || col < 0 || col > 9 || locToChange != null) {
				shipToPlace = null;
				return false;
			} else {
				if (isPlayer1) {
					board[row][col].P1Ship = shipToPlace;
				} else {
					board[row][col].P2Ship = shipToPlace;
				}
			}
		}
		// incrementShipCount()
		if (isPlayer1) {
			player1ShipCount++;
		} else {
			player2ShipCount++;
		}
		return true;
	}
	
	//returns "Hit", "Miss", "Hit and sunk <ship_name>", or "Unsuccessful"
	public String makeShot(boolean isPlayer1, String loc) {
		int row = getRow(loc);
		int col = getCol(loc);
		BoardSquare current = board[row][col];
		
		boolean isShot = (isPlayer1) ? current.P1Offensive : current.P2Offensive;
		if (isShot) {
			return "Unsuccessful";
		} else {
			// loc has not been shot by player, set it as shot
			if (isPlayer1) {
				current.P1Offensive = true;
			} else {
				current.P2Offensive = true;
			}
			
			Ship target = (isPlayer1) ? board[row][col].P2Ship : board[row][col].P1Ship;
			if (target == null) {
				return "Miss";
			} else {
				target.damage++;
				if (target.damage == target.getLength()) {
					// ship is destroyed, decrement ship count
					if (isPlayer1) {
						player2ShipCount--;
					} else {
						player2ShipCount--;
					}
					isGameOver = player1ShipCount <= 0 || player2ShipCount <= 0;
					
					String playerName = (isPlayer1) ? player1Name : player2Name;
					return "Hit and sunk " + playerName + "'s " + target.getReference() + "!";
				}
			}
		return "Hit";
		}
	}
	
	//returns whether or not the game is over
	public boolean isGameOver() {
		return isGameOver;
	}
	
	// returns Player 1's name
	public String getPlayer1Name() {
		return player1Name;
	}
	
	// returns Player 2's name
	public String getPlayer2Name() {
		return player2Name;
	}
	
	//Get zero based row index for the board
	private int getRow(String pos) {
		return LETTERS.indexOf(pos.charAt(0));
	}
	
	//Get zero based col index for the board
	private int getCol(String pos) {
		return Integer.parseInt(pos.substring(1)) - 1;
	}
	
	//helper to return Ship object from character passed from the view/controller
	private Ship getShip(char shipReference) {
		if (shipReference == Carrier.REFERENCE) {
			return new Carrier();
		} else if (shipReference == Battleship.REFERENCE) {
			return new Battleship();
		} else if (shipReference == Cruiser.REFERENCE) {
			return new Cruiser();
		} else if (shipReference == Destroyer.REFERENCE) {
			return new Destroyer();
		} else {
			return null;
		}
	}
	
	//Return the array of offensive grid values for player based on passed param
	//Each char in the array will be " ", "H", or "M"
	public char[] getOffensiveGrid(boolean isPlayer1) {
		char[] offenseGridVals = new char[BOARD_WIDTH * BOARD_HEIGHT];
		
		for (int row = 0; row < BOARD_WIDTH; row++) {
			for (int col = 0; col < BOARD_HEIGHT; col++) {
				//value begins as not shot
				char gridVal = ' ';
				
				//get and return proper offensive grid values
				BoardSquare current = board[row][col];
				boolean isShot = (isPlayer1) ? current.P1Offensive : current.P2Offensive;
				boolean hasShip = false;
				if (isPlayer1) {
					if (current.P2Ship != null) { 
						hasShip = true;
					}
				} else {
					if (current.P1Ship != null) {
						hasShip = true;
					}
				}
				
				if (isShot) {
					gridVal = hasShip ? 'H' : 'M';
				}
				
				offenseGridVals[row * BOARD_WIDTH + col] = gridVal;
			}
		}
		return offenseGridVals;
	}
	
	//Return the array of offensive grid values for player based on passed param
	public char[] getDefensiveGrid(boolean isPlayer1) {
		char[] defenseGridVals = new char[BOARD_WIDTH * BOARD_HEIGHT];
		
		for (int row = 0; row < BOARD_WIDTH; row++) {
			for (int col = 0; col < BOARD_HEIGHT; col++) {
				//value begins as empty
				char gridVal = ' ';
				
				//get and return proper defensive grid values
				BoardSquare current = board[row][col];
				Ship ship = (isPlayer1) ? current.P1Ship : current.P2Ship;
				
				if (ship != null) {
					gridVal = ship.getReference();
				}
				
				defenseGridVals[row * BOARD_WIDTH + col] = gridVal;
			}
		}
		return defenseGridVals;
	}
}

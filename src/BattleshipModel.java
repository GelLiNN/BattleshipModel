import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * BattleshipModel class for handling battleship game state
 * 
 * @author Tim Davis
 * @author Kellan Nealy
 */
 
public class BattleshipModel {
	// Model state
	private BoardSquare[][] board;
	private String player1Name;
	private String player2Name;
	private int player1ShipCount;
	private int player2ShipCount;
	private boolean isGameOver;
	
	// Dynamic file path for configuration file, works for different operating systems!
	private static final String CONF_PATH = System.getProperty("user.dir") + File.separator
			+ "src" + File.separator + "battleship.bshp";
	
	// State values to be read from configuration
	private int BOARD_WIDTH;
	private int BOARD_HEIGHT;
	private String LETTERS = "";	
	public boolean alternateTurnOnHit;
	public boolean diagShipPlacementEnabled;
	private TreeMap<String, Integer> possibleShips;
	
	/**
	 * Constructor for BattleshipModel
	 * @param player1 The name of Player 1
	 * @param player2 The name of Player 2
	 */
	public BattleshipModel(String player1, String player2) {
		readStoreConfigs();
		String[] letterStrings = getPossibleRowChars();
		for (String letter : letterStrings) {
			LETTERS += letter;
		}
		
		// Create new board, and populate it with BoardSquares
		board = new BoardSquare[BOARD_WIDTH][BOARD_HEIGHT];
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			for (int col = 0; col < BOARD_WIDTH; col++) {
				board[row][col] = new BoardSquare();
			}
		}
		// Set initial game state
		this.player1Name = player1;
		this.player2Name = player2;
		player1ShipCount = 0;
		player2ShipCount = 0;
		isGameOver = false;
	}
	
	/**
	 * Attempts to place a ship in the board
	 * @param isPlayer1 True is Player 1's turn, false is Player 2's turn
	 * @param ship The reference for the ship to place ('A', 'B', 'C', or 'D')
	 * @param loc The bound-validated starting location to place the ship
	 * @param o The orientation for the ship to be placed in, starting from loc
	 * @return True if ship placement was successful, false otherwise
	 */
	public boolean placeShip(boolean isPlayer1, String ship, String loc, Orientation o) {
		Ship shipToPlace = getShip(ship);
		int startRow = getRow(loc);
		int startCol = getCol(loc);
		int dx = o.dx;
		int dy = o.dy;
		
		// attempt to place the ship in each BoardSquare
		for (int i = 0; i < shipToPlace.getLength(); i++) {
			int row = startRow + i * dy;
			int col = startCol + i * dx;
			Ship locToChange = null;
			
			// check bounds first to avoid index exceptions
			boolean isNotWithinBounds = (row < 0 || (row > BOARD_HEIGHT - 1)
					|| col < 0 || (col > BOARD_WIDTH - 1));
			
			if (!isNotWithinBounds) {
				locToChange = (isPlayer1) ? board[row][col].P1Ship : board[row][col].P2Ship;
			}
			
			// if we go out of the bounds or there is a ship in the way
			if (isNotWithinBounds || locToChange != null) {
				deleteReferences(isPlayer1, shipToPlace.getShipName());
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
		
		incrementShipCount(isPlayer1);
		return true;
	}
	
	/**
	 * Private helper to get a new ship object from a ship reference
	 * @param shipReference Character ship reference
	 * @return New ship object
	 */
	private Ship getShip(String shipName) {
		return new Ship(shipName, possibleShips.get(shipName), shipName.toUpperCase().charAt(0));
	}
	
	private void deleteReferences(boolean isPlayer1, String shipName) {
		for (int row = 0; row < BOARD_HEIGHT; row ++) {
			for (int col = 0; col < BOARD_WIDTH; col++) {
				
				if (isPlayer1 && board[row][col].P1Ship != null && 
						board[row][col].P1Ship.getShipName().equalsIgnoreCase(shipName)) {
					board[row][col].P1Ship = null;
					
				} else if (!isPlayer1 && board[row][col].P2Ship != null && 
						board[row][col].P2Ship.getShipName().equalsIgnoreCase(shipName)) {

					board[row][col].P2Ship = null;
				}
			}
		}
	}
	
	/**
	 * Private helper for incrementing players' ship counts
	 * @param isPlayer1 True is Player 1's turn, false is Player 2's turn
	 */
	private void incrementShipCount(boolean isPlayer1) {
		if (isPlayer1) {
			player1ShipCount++;
		} else {
			player2ShipCount++;
		}
	}
	
	/**
	 * Attempts to make a shot in the board
	 * @param isPlayer1 True is Player 1's turn, False is Player 2's turn
	 * @param loc The bound-validated location to shoot
	 * @return "Hit", "Miss", "Hit and sunk <player_name>'s <ship_name>", or "Unsuccessful"
	 */
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
					// ship is destroyed, decrement ship count and remove ship!
					if (isPlayer1) {
						player2ShipCount--;
					} else {
						player1ShipCount--;
					}
					isGameOver = player1ShipCount <= 0 || player2ShipCount <= 0;
					deleteReferences(isPlayer1, target.getShipName());
					String targetPlayerName = (isPlayer1) ? player1Name : player2Name;
					return "Hit and sunk " + targetPlayerName + "'s " + target.getShipName() + "!";
				}
			}
		return "Hit";
		}
	}
	
	/**
	 * Private getter for the zero-based row index for the board
	 * @param pos The board location to get the row of (i.e. A1)
	 * @return Zero-based row index for the board
	 */
	private int getRow(String pos) {
		return LETTERS.indexOf(pos.charAt(0));
	}
	
	/**
	 * Private getter for the zero-based col index for the board
	 * @param pos The board location to get the col of (i.e. A1)
	 * @return Zero-based col index for the board
	 */
	private int getCol(String pos) {
		return Integer.parseInt(pos.substring(1)) - 1;
	}
	
	/**
	 * Returns Player 1's name
	 * @return Player 1's name
	 */
	public String getPlayer1Name() {
		return player1Name;
	}
	
	/**
	 * Returns Player 2's name
	 * @return Player 2's name
	 */
	public String getPlayer2Name() {
		return player2Name;
	}
	
	/**
	 * Return the array of offensive grid values for player based on passed parameter
	 * @param isPlayer1 True for Player 1, False for Player 2
	 * @return Array of offensive grid values for passed player
	 */
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
	
	/**
	 * Return the array of defensive grid values for player based on passed parameter
	 * @param isPlayer1 True for Player 1, False for Player 2
	 * @return Array of defensive grid values for passed player
	 */
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

	/**
	 * Returns whether or not the game is over
	 * @return True if game is over, false if not
	 */
	public boolean isGameOver() {
		return isGameOver;
	}
	
	private void readStoreConfigs() {
		possibleShips = new TreeMap<String, Integer>();
		try {
			Scanner fileReader = new Scanner(new File(CONF_PATH));
			while (fileReader.hasNextLine()) {
				
				String currentLine  = fileReader.nextLine();
				if (currentLine.equalsIgnoreCase("[state]")) {
					
					// iterate until we get to empty line before the next stanza
					while (currentLine.length() > 0 && fileReader.hasNextLine()) {
						currentLine = fileReader.nextLine();
						String[] settingVals = currentLine.split(":");
						String settingName = settingVals[0].trim();
						
						if (settingName.equalsIgnoreCase("boardWidth")) {
							this.BOARD_WIDTH = Integer.parseInt(settingVals[1].trim());
						} else if (settingName.equalsIgnoreCase("boardHeight")) {
							this.BOARD_HEIGHT = Integer.parseInt(settingVals[1].trim());
						} else if (settingName.equalsIgnoreCase("alternateTurnOnHit")) {
							this.alternateTurnOnHit = Boolean.parseBoolean(settingVals[1].trim());
						} else if (settingName.equalsIgnoreCase("diagShipPlacement")) {
							this.diagShipPlacementEnabled = Boolean.parseBoolean(settingVals[1].trim());
						}
					}
				} else if (currentLine.equalsIgnoreCase("[ships]")) {
					
					// iterate until we get to empty line before the next stanza
					while (currentLine.length() > 0 && fileReader.hasNextLine()) {
						currentLine = fileReader.nextLine();
						String[] settingVals = currentLine.split(":");
						String shipName = settingVals[0].trim();
						int shipLength = Integer.parseInt(settingVals[1].trim());
						
						// if it's a repeated ship name entry, we will append the ship number
						// duplicate ships shall always start at 2
						int shipNum = 2;
						if (possibleShips.containsKey(shipName)) {
							possibleShips.put(shipName + "_" + shipNum, shipLength);
							shipNum++;
						} else {
							possibleShips.put(shipName, shipLength);
							shipNum = 2;
						}
					}
				}
			}
			fileReader.close();
		} catch (Exception e) {
			System.out.println("Error!  Could not read from configuration!!\n" + e.getMessage() + "\n");
			e.printStackTrace(System.out);
			System.exit(0);
		}
	}
	
	public String[] getPossibleRowChars() {
		String[] possibleRowChars = new String[BOARD_HEIGHT];
		char start = 'A';
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			possibleRowChars[i] = "" + (char) (start + i);
		}
		return possibleRowChars;
	}
	
	public int[] getPossibleColNums() {
		int[] possibleColNums = new int[BOARD_WIDTH];
		for (int i = 1; i <= BOARD_HEIGHT; i++) {
			possibleColNums[i - 1] = i;
		}
		return possibleColNums;
	}
	
	public String[] getPossibleOrientations() {
		return (diagShipPlacementEnabled ? new String[]{"DD", "DU", "H", "V"} : new String[]{"H", "V"});
	}
	
	public String[] getShipNames() {
		String[] possibleShipNames = new String[possibleShips.keySet().size()];
		int i = 0;
		for (String ship : possibleShips.keySet()) {
			possibleShipNames[i] = ship;
			i++;
		}
		return possibleShipNames;
	}
	
	public int getShipLength(String shipName) {
		return possibleShips.get(shipName);
	}
}
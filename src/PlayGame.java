import java.util.*;
/**
 * class PlayGame 
 * 
 * @Croaching Tiger
 * @version 1
 */
public class PlayGame
{
    /*
     * Method whoGoesFirst - method that decides which player goes first
     */

    public boolean whoGoesFirst (){
        int result;
        Random r = new Random();
        result = r.nextInt(10) + 1; // random number from 1-10
        if (result % 2 == 0)
            return true;
        else return false;
    } 

    /*
     * Method validateSquare - validate square is A1 through J10
     * pre-condition: in must have length > 0
     * @param in - string to be validated 
     * @return true if between A1 and J10
     */
    public boolean validateSquare (String in, BattleshipModel model) {
        String[] validRowChar = model.getPossibleRowChars(); // valid row
        int[] validColNum = model.getPossibleColNums(); // valid column

        String rowChar = in.substring(0,1); // get row from input string
        String colNum = in.substring(1); // get column from input string

        return Arrays.binarySearch(validRowChar, rowChar) >= 0 &&
        		Arrays.binarySearch(validColNum, Integer.parseInt(colNum)) >= 0;
    }

    /*
     * Method validateOrientation - validate orientation H, V, DD, and DU.
     * @param orientationInput - string containing user orientation
     * @return true if user selected valid orientation of DD, DU, H or V
     */
    public boolean validateOrientation (String orientationInput, BattleshipModel model){
        String[] validOrientation = model.getPossibleOrientations(); // valid orientations

        int i = 0;
        boolean foundOrientation = false;
        orientationInput.trim(); // should not be white spaces but including since this is a public method
        while ( (!foundOrientation) && (i < validOrientation.length) ) {
            if (validOrientation[i].compareTo(orientationInput) == 0 ) { // confirm row is valid if equals is zero
                foundOrientation  = true;
            }
            i++;
        }
        return foundOrientation;
    }

    /*
     * Method ResetScreen - fills screen with backspaces to clear screen for user
     */
    public void ResetScreen(){
        for (int i = 0; i < 10000; i++) {
            System.out.println("\b");
        }
    }

    /**
     * Method GetShipChar
     * @param item - ship that is being placed
     * @return - char representation of the ship, always upper-case
     */
    public char getShipChar (String item) { 
    	return item.toUpperCase().charAt(0);
    }

    public Orientation GetOrientation(String orientation) {
        Orientation o = null;
        switch (orientation) {
            case "DD": o = Orientation.DD;
            return o;

            case "DU": o = Orientation.DU;
            return o;

            case "V":  o = Orientation.V;
            return o;

            case "H":  o = Orientation.H;
            return o;
        }
        return o;
    }

    /*
     * Method place - helper method for setupGame - this method places all five ships for a player
     * Player can't exit during the place  method
     * @param playerID - player's name
     * @param playerNum = true for player 1, false for player 2
     */

    public void place (String playerID, boolean playerNum, BattleshipModel game) {
        // loop
        boolean wasSuccessful; // initialized below to false
        System.out.println(playerID + ": place your ships by typing <start square> and <orientation: DD DU V or H> and then pressing ENTER.");
        //scanner object
        Scanner console = new Scanner(System.in);
        String userInput;
        String[] tokens = null;  

        boolean validIn = false;
        boolean validMove = false;

        String[] ships = game.getShipNames();
        // get all ships to be placed for each player, and iterate until complete!
        for (String currentShip : ships) {
            validMove = false;
            while (!validMove) {   
                System.out.print(playerID + "'s " + currentShip +
                		" of length " + game.getShipLength(currentShip) + ": ");
                userInput = console.nextLine();
                tokens = userInput.split("\\s+");
                if (tokens.length == 2) 
                {
                    if (validateSquare(tokens[0], game) && validateOrientation(tokens[1], game))  {
                    	
                        validMove = game.placeShip(playerNum, currentShip, tokens[0], GetOrientation(tokens[1]));
                        if (!validMove) {
                            System.out.println("Invalid placement. Try again. We said this");
                        }
                    }
                    else 
                    {
                        System.out.println("Invalid placement. Try again. She said this");
                    }
                }
                else 
                {
                	String[] orientations = game.getPossibleOrientations();
                    System.out.println("Invalid input. Please type <start square> and <orientation - " +
                    					Arrays.toString(orientations).substring(1, orientations.length) +
                    					" > and then press ENTER.");   
                }

            }
            System.out.println(); // if move is valid and ship placed, then user gets to see his/her board
            System.out.println ("Here's what your board looks like:");
            System.out.println(playerID + "'s defensive board.");
            printBoard(game.getDefensiveGrid(playerNum), game);
        }
    }

    /*
     * Method setupGame - sets up game for one player
     * @param playerID - player's name
     * @param player boolean - true for player 1, false for player1
     * @game - BattleshipModel object
     * @userLogin - userLogin object
     */
    public boolean setupGame (String playerID, boolean playerNum, BattleshipModel game) {
        boolean setup = false;
        boolean exit = false;
        Scanner console = new Scanner(System.in);
        String userInput;

        this.printMenuSetup(playerID);
        System.out.println("Enter command. ");
        userInput = console.nextLine();

        String[] tokens = userInput.split("\\s+");
        while (!setup) {
            switch (tokens[0].toUpperCase()) {
                case "M":
                	this.printMenuSetup(playerID);
                break;

                case "E":
	                System.out.println("OK. We'll exit setup.");
	                exit = true;
	                setup = true;
                break;

                case "S":
	                //Setup
	                place(playerID, playerNum, game);
	                setup = true;
                break;

                case "I":
                	printSetupInstructions();
                break;

                case "O":
	                System.out.println(playerID + "'s offensive board.");
	                printBoard(game.getOffensiveGrid(playerNum), game);
                break;

                case "D":
	                System.out.println(playerID + "'s defensive board.");
	                printBoard(game.getDefensiveGrid(playerNum), game);
                break;

                case "R":
	                //call reset board
	                ResetScreen();
                break;

                default: 
                	System.out.println("Invalid input. Please try again.");
                break;

            }
            if (!exit && !setup) {
                System.out.println("Enter command. ");
                userInput = console.nextLine();
                tokens = userInput.split("\\s+");
            }
        }

        if (exit){ // user doesn't want to continue
            return false;
        }
        else 
            return true;
    }

    /*
     * Method playGame - 
     */
    //public boolean playGame(BattleshipModelInterface battleShip) {
    public boolean play (String firstPlayerName, boolean firstPlayerBoolean, String secPlayerName, boolean secPlayerBoolean, BattleshipModel game ){

        boolean userWantsToExit = false;

        Scanner console = new Scanner(System.in);
        String currentPlayer = firstPlayerName; // keep track of who has current turn
        String nextPlayer = secPlayerName;      // keep track of which player has next turn
        boolean currentTurn = true; // first person to go is TRUE
        String userInput = "";  //compiler was complaining that this string wasn't initialized

        while (!userWantsToExit && !game.isGameOver()) {   // keep going until model tells us the game is over or the user wants to exit

            boolean currentTurnOver = false; // control variable for current turn
            while (!currentTurnOver) {   // current turn is over only when player shoots a valid shot and misses or shoots a valid shot and wins the game
            	
                printMenuPlay(firstPlayerName,secPlayerName);
                System.out.println(currentPlayer + ": please enter a command.. "); // prompt current player to give a command
                userInput = console.nextLine(); // get players command
                String[] tokens = userInput.split("\\s+");  // split the input into tokens
            	
                switch (tokens[0].toUpperCase()) {
                    case "E": // user wants to exit
	                    System.out.println("OK. We'll exit the game."); 
	                    userWantsToExit = true; 
	                    currentTurnOver = true;
                    break;

                    case "T":
	                    boolean stillShooting = true;
	                    while (stillShooting) {
	                    	//print out the board first
		                    System.out.println(currentPlayer + "'s offensive board.");
		                    printBoard(game.getOffensiveGrid(currentTurn), game);
	                        System.out.println("\n" + currentPlayer + " :take a shot by specifying a square A1 through "
		                    + (char) ('A' + game.getPossibleRowChars().length - 1) + game.getPossibleColNums().length + " you have not yet fired on.");
	                        userInput = console.nextLine();
	                        
	                        String[] shotTokens = userInput.split("\\s+");  //Hit", "Miss", "Hit and sunk <ship_name>", or "Unsuccessful"
	                        if (validateSquare(shotTokens[0], game)) {
	                        	
	                            String shotResponse = game.makeShot(currentTurn,shotTokens[0]);
	                            if (shotResponse.charAt(0) == 'H') {  // case will always be capital
	                             
	                                System.out.println("Congratulations: " + shotResponse);
	                            
	                                // turn shouldn't continue if battleship is supposed to alternate on hit
	                                stillShooting = !game.alternateTurnOnHit;
	                                currentTurnOver = game.alternateTurnOnHit;
	                                if (game.isGameOver()) {
	                                	System.out.println(currentPlayer + " is the winner!\n");
	                                	return false;
	                                }
	                            } else if (shotResponse.charAt(0) == 'U') {
	                                // it's still the player's turn if shot was neither hit nor miss
	
	                                System.out.println(shotResponse);
	                                System.out.println("You fired in the same spot. Please try again.");
	                                stillShooting = true;
	                                currentTurnOver = false;
	                                
	                            } else if (shotResponse.charAt(0) == 'M') { 
	                                System.out.println(shotResponse);
	                                stillShooting = false;
	                                currentTurnOver = true;
	                            }
	                        } else {
	                            System.out.println("Invalid target.");
	                        }
	                    }
                    break;

                    case "I": // display instructions
                    	printMenuPlay(currentPlayer,nextPlayer);
                    break;

                    case "O": // display offensive board
	                    System.out.println(currentPlayer + "'s offensive board.");
	                    printBoard(game.getOffensiveGrid(currentTurn), game);
                    break;

                    case "D": // display defensive board
	                    System.out.println(currentPlayer + "'s defensive board.");
	                    printBoard(game.getDefensiveGrid(currentTurn), game);
                    break;

                    case "R": // reset screen to blank to remove board views
                    	ResetScreen();
                    break;

                    default: 
                    	System.out.println("Invalid input. Please try again.");
                    break;
                }          
            }
            if (!userWantsToExit && currentTurnOver) { // other player's turn as this player missed or was unsuccessful when he/she took a shot
                currentTurnOver = false; // this needs to be false for the game to continue
                if (currentPlayer == firstPlayerName) {  //switch player names
                    currentPlayer = secPlayerName;
                }
                else if (currentPlayer == secPlayerName) {
                    currentPlayer = firstPlayerName;
                }

                if (currentTurn == true) {
                	// model uses true for player 1 and false for player2
                    currentTurn = false;
                }
                else if (currentTurn == false) {
                    currentTurn = true;
                }
                System.out.println(currentPlayer + ": it's your turn now.");
            }
        }
        console.close();
        return false; // we only get here if user wants to exit or game is over so false means stop the game
    }

    public void runGame(String player1, String player2, BattleshipModel game, PlayGame thisGame) {
        String playerWithFirstTurn;       // was goesFirst
        String playerWithSecondTurn;      // was goesSecond

        if (thisGame.whoGoesFirst()) {
            playerWithFirstTurn = player1;
            playerWithSecondTurn = player2;
            System.out.println(playerWithFirstTurn + ", you're going first!");
            System.out.println(playerWithSecondTurn + ", you're going second!");
            System.out.println();

        }
        else {
            playerWithFirstTurn = player2;
            playerWithSecondTurn = player1;
            System.out.println(playerWithFirstTurn + ", you're going first!");
            System.out.println(playerWithSecondTurn + ", you're going second!");
            System.out.println();
        }

        boolean keepGoing = true;
        while (keepGoing) {
            keepGoing = thisGame.setupGame(playerWithFirstTurn, true, game); // setup first board

            if (keepGoing) {
                keepGoing = thisGame.setupGame(playerWithSecondTurn, false, game);  // setup second board
                if (keepGoing) {
                    keepGoing = thisGame.play(playerWithFirstTurn, true, playerWithSecondTurn, false, game); // play the game
                }
            }
            else {
                System.out.println("Player wants to quit.");
            }
        }
        System.out.println("Thanks for playing.");
    }

    //pre-condition, array has to have no null pointers
    public static void printBoard(char[] gridVals, BattleshipModel model) {
        int index = 0;
        int maxRow = model.getPossibleRowChars().length;
        int maxCol = model.getPossibleColNums().length;
        
        String colHeaders = "  ";
        String horizBorder = "   +";
        for (int col = 1; col <= maxCol; col++)
        {
        	colHeaders += "   " + col;
        	horizBorder += "---+";
        }
        System.out.println(colHeaders);
        System.out.println(horizBorder);
        
        for (int row = 0; row < maxRow; row++) {    
            char rowLabel = (char) ('A' + row);
            System.out.print(" " + rowLabel + " |");

            for (int col = 0; col < maxCol; col++) {

                System.out.print(" " + gridVals[index] + " |");
                index++;
            }

            System.out.println(); // end the current line
            System.out.println(horizBorder);

        }
        System.out.println();
        System.out.println();
    }

    /*
     * Method print menu prints menu choices for user.
     * Validate input - report unrecognized commands
     * if "in" "contains"
     */
    public void printMenuSetup(String playerID) {
        System.out.println("Hi " + playerID + " you're in SETUP MODE.");
        System.out.println("Here's your battleship setup menu. Enter letter at the prompt below.");
        System.out.println("M - View menu options.");
        System.out.println("I - View setup instructions.");
        System.out.println("S - Setup my defensive board.");
        System.out.println("O - View my offensive board.");
        System.out.println("D - View my defensive board.");
        System.out.println("R - Reset screen to remove view of board(s).");
        System.out.println("E - Exit game.");
        System.out.println();
    }

    /*
     * Method print menu prints menu choices for user.
     * Validate input - report unrecognized commands
     * if "in" "contains"
     */
    public void printMenuPlay(String playerID, String otherPlayer) {
        System.out.println("Hi " + playerID + " you're in PLAY MODE.");
        System.out.println("Battleship Play Menu. Enter letter at the prompt below.");
        System.out.println("I - View play Instructions.");
        System.out.println("T - Take a shot at " + otherPlayer + "'s ships.");
        System.out.println("O - View my offensive board.");
        System.out.println("D - View my defensive board.");
        System.out.println("R - Reset screen to remove view of board(s).");
        System.out.println("E - Exit game.");
    }

    /*
     * Method print menu prints menu choices for user.
     */
    public void printSetupInstructions() {
        System.out.println("Setup Instructions.");
        System.out.println("During setup mode, you will need to place your aircraft ");
        System.out.println("carrier, battleship, cruiser, and two destroyers");
        System.out.println("on a 10x10 grid. Specify the starting position for each ship by");
        System.out.println("entering a starting square of A1 through J10 and specify the");
        System.out.println("orientation by entering H for horizontal, V for vertical,");
        System.out.println("DD for diagnol down and DU for diaganol up. See sample input below.");
        System.out.println("Player1 aircraft carrier <starting square> <orientation: DD DU V or H>");
        System.out.println("Player1 aircraft carrier B2 H");
        System.out.println();
        System.out.println("The game will let you know if your placement is invalid or if");
        System.out.println("you already placed a ship in the specified location. The game shows");
        System.out.println("you your board after each ship placement.");
        System.out.println();
    }

    /*
     * Method print menu prints menu choices for user.
     */
    public void printPlayInstructions() {
        System.out.println("Play Instructions.");
        System.out.println("Game randomly chooses either Player1 or Player2 to start.");
        System.out.println("When it's a player's turn, the player can take several actions.");
        System.out.println("O and ENTER to view offensive board, D and ENTER to view defensive board.");
        System.out.println("To take a shot, a player types 'T' and then presses ENTER.");
        System.out.println("Then the player enters a square A1 through J10 where a missile will be shot.");
        System.out.println("The game responds three ways: Miss, Hit and continue, Hit and ship sunk.");
        System.out.println("If a player hits the other player's ship, he/she gets another turn.");
        System.out.println("When Player1's turn is over, game prompts for a move from Player2");
        System.out.println("Play continues until one player has sunk all the other player's battleships.");
    }
}
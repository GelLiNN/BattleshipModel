/**
 * The programatic interface for the Battleship Model class.
 * This interface supports communication with both the view
 * and controller classes in the Battleship application.
 * 
 * @author Sam Cooledge
 * @author Molly Allen
 * @author Kellan Nealy
 * @author Timothy Davis
 */

//The order for the following methods is subject to  peer review and change

public interface BattleshipModelInterface {
  
  /**
   * Makes a shot during Play Mode.
   * @param isPlayer1 whether or not it's player 1's turn
   * @param validated location i.e. "D7"
   * @return The status of the shot. See the shotStatus constants
   */
 public String makeShot(boolean isPlayer1, String loc);
 
 /**
  * using the same Location class Dan defined
  * @param isPlayer1 whether or not it's player 1's turn
  * @param ship 'A', 'B', 'C', or 'D'
  * @param validated location i.e. "D7"
  * @param Orientation decided by view/controller from user input
  */
 public boolean placeShip(boolean isPlayer1, char ship, String loc, Orientation o);
 
  /**
   * views are going to need to see offensive grid
   * @param  isPlayer1 whether or not it's player 1's turn 
   * @return char array of grid values
   */
 public char[] getOffensiveGrid(boolean isPlayer1);
 
  /**
   * views are going to need to see defensive grid
   * @param isPlayer1 whether or not it's player 1's turn
   * @return char array of grid values
   */
 public char[] getDefensiveGrid(boolean isPlayer1);
 
 /**
  * @return true if all of the ships of one player have been sunk
  */
 public boolean isGameOver();
}
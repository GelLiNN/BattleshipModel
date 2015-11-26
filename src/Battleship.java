
public class Battleship extends Ship{
	private static final int LENGTH = 4;
	public static final char REFERENCE = 'B';
	
	public Battleship() {
		super(LENGTH, REFERENCE);
	}
	
	public int getLength() {
		return LENGTH;
	}
}

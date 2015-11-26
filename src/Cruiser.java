
public class Cruiser extends Ship{
	private static final int LENGTH = 3;
	public static final char REFERENCE = 'C';
	
	public Cruiser() {
		super(LENGTH, REFERENCE);
	}
	
	public int getLength() {
		return LENGTH;
	}
}

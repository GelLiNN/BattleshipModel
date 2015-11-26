
public class Carrier extends Ship{
	private static final int LENGTH = 5;
	public static final char REFERENCE = 'A';
	
	public Carrier() {
		super(LENGTH, REFERENCE);
	}
	
	public int getLength() {
		return LENGTH;
	}
}

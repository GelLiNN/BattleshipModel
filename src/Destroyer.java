
public class Destroyer extends Ship{
	private static final int LENGTH = 2;
	public static final char REFERENCE = 'D';
	
	public Destroyer() {
		super(LENGTH, REFERENCE);
	}
	
	public int getLength() {
		return LENGTH;
	}
}

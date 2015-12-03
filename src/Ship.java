
public abstract class Ship {
	private int length;
	private char reference;
	public int damage;
	
	public Ship(int length, char reference) {
		this.length = length;
		this.reference = reference;
		damage = 0;
	}
	
	public int getLength() {
		return length;
	}
	
	public char getReference() {
		return reference;
	}
}


public class Ship {
	private String shipName;
	private int length;
	private char reference;
	public int damage;
	
	public Ship(String shipName, int length, char reference) {
		this.shipName = shipName;
		this.length = length;
		this.reference = reference;
		damage = 0;
	}
	
	public String getShipName() {
		return shipName;
	}
	
	public int getLength() {
		return length;
	}
	
	public char getReference() {
		return reference;
	}
}

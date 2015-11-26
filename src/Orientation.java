
public enum Orientation {
	DD (0, 0),
	DU (0, 0),
	H  (0, 0),
	V  (0, 0);
	
	public final int dx;
	public final int dy;
	
	Orientation(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
}

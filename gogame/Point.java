package gogame;

public final class Point {
	public final int x;
	public final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		
		if(null != o && getClass() == o.getClass()) {
			Point otherPoint = (Point)o;
			return this.x == otherPoint.x && this.y == otherPoint.y;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 31 * x + y; // recommendation from Joshua Bloch
	}
	
	@Override
	public String toString() {
		return "x: %d y: %d".formatted(x, y);
	}
}

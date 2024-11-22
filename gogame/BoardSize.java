package gogame;

import java.util.Arrays;

public enum BoardSize {
	NINE(9), THIRTEEN(13), NINETEEN(19);
	
	private final int size;
	BoardSize(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
	
	public static BoardSize fromString(String s) {
		return Arrays.stream(values())
				.filter(boardSize -> boardSize.toString().equals(s))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException());
	}
	
	public static String[] getStringValues() {
		return Arrays.stream(values())
				.map(Object::toString)
				.toArray(String[]::new);
	}
	
	@Override
	public String toString() {
		return "%dx%d".formatted(size, size);
	}
}

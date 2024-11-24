package gogame;

public enum BoardSpace {
	EMPTY(null), BLACK(Stone.BLACK), WHITE(Stone.WHITE);
	
	public final Stone stone;
	private BoardSpace(Stone stone) {
		this.stone = stone;
	}
	
	public static BoardSpace fromStone(Stone stone) {
		return Stone.BLACK == stone ? BoardSpace.BLACK : (Stone.WHITE == stone ? BoardSpace.WHITE : BoardSpace.EMPTY);
	}
}
package gogame;

public enum Stone {
	BLACK, WHITE;
	
	public Stone opposite() {
		return Stone.BLACK == this ? Stone.WHITE : Stone.BLACK;
	}
}

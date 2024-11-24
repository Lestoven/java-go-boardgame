package gogame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.util.Objects.hash;

public final class GoState implements Predicate<Point>, Serializable {
	private static final long serialVersionUID = 1;
	
	public final BoardSpace[][] board;
	private int blackCaptured;
	private int whiteCaptured;
	public Stone turn;
	private final Set<GoState> prevStates;
	
	public GoState(int size) {
		board = new BoardSpace[size][size];
		IntStream.range(0, size)
			.forEach(y -> IntStream.range(0, size)
					.forEach(x -> board[y][x] = BoardSpace.EMPTY)); // initialize all BoardSpace on the board to empty
		
		blackCaptured = 0;
		whiteCaptured = 0;
		turn = Stone.BLACK;
		prevStates = new HashSet<>();
	}
	
	private GoState(GoState o) {
		board = Arrays.stream(o.board)
				.map(row -> row.clone())
				.toArray(BoardSpace[][]::new);
		blackCaptured = o.blackCaptured;
		whiteCaptured = o.whiteCaptured;
		turn = o.turn;
		prevStates = o.prevStates; // Already stored "GoState"s inside prevStates won't change during the program, so much better performance can be achieved if we don't copy unnecessarily
	}
	
	public Point[] getNeighbors(Point p) {
		int x = p.x;
		int y = p.y;
		Point[] neighbours = { new Point(x, y - 1), new Point(x + 1, y), new Point(x, y + 1), new Point(x - 1, y) };
		
		return Arrays.stream(neighbours).filter(this).toArray(Point[]::new);
	}

	@Override
	public boolean test(Point p) {
		return p.x >= 0 && p.x < board.length &&
				p.y >= 0 && p.y < board.length;
	}

	public Point[] getLiberties(Stone s, Point p, Set<Point> scanned) {
		Queue<Point> toScan = new LinkedList<>(); // the queue
		Set<Point> liberties = new HashSet<>(); // empty points next to the given group
		Set<Point> discovered = new HashSet<>(); // points that the BFS already discovered (We need to mark the discovered points, to avoid putting them into the queue multiple times)
		
		Consumer<Point> expandBFS = currentPoint -> {
			Arrays.stream(getNeighbors(currentPoint))
				.filter(neighborPoint -> !discovered.contains(neighborPoint))
				.forEach(neighborPoint -> { toScan.add(neighborPoint); discovered.add(neighborPoint); });
		}; // expand the BFS, to search for additional liberties, and group members (scanned)
		
		toScan.add(p); // add the starting point to the queue
		
		while(!toScan.isEmpty()) {
			Point currentPoint = toScan.poll(); // remove the head of the queue
			discovered.add(currentPoint); // mark the currentPoint (processed) as discovered
			BoardSpace boardSpace = getBoardSpace(currentPoint); // currentPoint's representation on the table
			
			if(BoardSpace.EMPTY == boardSpace && currentPoint != p) { // if boardSpace is empty, and currentPoint is not the starting point, we found a "liberty" point
				liberties.add(currentPoint); // an "empty space", next to the group is found
			} else if(s == boardSpace.stone) { // if boardSpace contains a connected point to the group (same color as the given "s")
				scanned.add(currentPoint); // we found a same color point, so add to the group
				expandBFS.accept(currentPoint);
			} else if(currentPoint == p) { // the neighbors of the starting point (p) must be added to the queue, regardless of what's on it
				expandBFS.accept(currentPoint); 
			}
		}
		
		return liberties.toArray(Point[]::new);
	}
	
	public void checkCaptured(Point p) {
		BoardSpace boardSpace = getBoardSpace(p);
		if(BoardSpace.EMPTY == boardSpace) { // empty space cannot be captured
			return;
		}
		
		Set<Point> sameColorStoneNeighbors = new HashSet<>();
		
		if(0 == getLiberties(boardSpace.stone, p, sameColorStoneNeighbors).length) { // if the stone's liberty (on "p") is 0, then the stone is captured
			sameColorStoneNeighbors.stream()
				.forEach(currentPoint -> board[currentPoint.y][currentPoint.x] = BoardSpace.EMPTY); // remove stones from the board <-> set boardSpaces to empty
			
			if(boardSpace.stone == Stone.BLACK) {
				whiteCaptured += sameColorStoneNeighbors.size();
			} else {
				blackCaptured += sameColorStoneNeighbors.size();
			}
		}
	}
	
	public GoState placeStone(Point p) {
		board[p.y][p.x] = BoardSpace.fromStone(turn);
		Arrays.stream(getNeighbors(p)).forEach(this::checkCaptured);
		turn = turn.opposite(); // switch current player's color
		return this;
	}
	
	public boolean isLegalMove(Point p) {
		if(test(p) && BoardSpace.EMPTY == getBoardSpace(p) &&
				(0 < getLiberties(turn, p, new HashSet<>()).length ||
					Arrays.stream(getNeighbors(p)) 
						.anyMatch(np -> turn.opposite() == getBoardSpace(np).stone && 
							Arrays.equals(getLiberties(turn.opposite(), np, new HashSet<>()), new Point[] {p})) // Is there any neighbor stone: that is opposite color and it's exactly one liberty is "p"
				)
		) { // legal move: inside the board, space is empty, the new stone's liberty will be > 0 or the stone's liberty is 0, but will remove an opposite stone
			GoState newGoState = new GoState(this).placeStone(p);
			return prevStates.stream()
					.noneMatch(prevState -> prevState.equals(newGoState)); // less efficient: return prevStates.stream().noneMatch(prevState -> prevState.equals(new GoState(this).placeStone(p)));
		}
		return false; // illegal move
	}
	
	public boolean makeMove(Point p) {
		if(null == p) {
			prevStates.add(new GoState(this));
			turn = turn.opposite(); // switch current player's color
		} else if(!isLegalMove(p)) {
			return false;
		} else { // legal move
			prevStates.add(new GoState(this));
			placeStone(p); // place the stone on the board
		}
		
		return prevStates.stream().anyMatch(this::equals);
	}
	
	/**
	 * Helper method to return a BoardSpace by a Point, to reduce redundancy.
	 * @param p A Point of the table.
	 * @return The table's BoardSpace on the specified Point.
	 */
	private BoardSpace getBoardSpace(Point p) {
		return board[p.y][p.x];
	}
	
	public static GoState loadGame(File filename) {
		try {
			try(ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(filename))) {
				return (GoState) inStream.readObject();
			}
		}
		catch(Exception e) {
			throw new IllegalArgumentException("Failed to load game file from: %s".formatted(filename));
		}
	}
	
	public void saveGame(File filename) {
		try {
			try(ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(filename))) {
				outStream.writeObject(this);
			}
		}
		catch(Exception e) {
			throw new IllegalArgumentException("Failed to save game file to: %s".formatted(filename));
		}
	}
	
	
	public int getBlackCaptured() { return blackCaptured; }
	public int getWhiteCaptured() { return whiteCaptured; }
	public Set<GoState> getPrevStates() { return prevStates; }
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		
		if(null != o && getClass() == o.getClass()) {
			GoState otherGoState = (GoState)o;
			return Arrays.deepEquals(this.board, otherGoState.board) && this.turn == otherGoState.turn;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return hash(Arrays.deepHashCode(board), turn);
	}
	
	@Override
	public String toString() {
		return "Black Captured: %d%nWhite Captured: %d".formatted(blackCaptured, whiteCaptured);
	}
}

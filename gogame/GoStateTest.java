package gogame;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Tests of the GoState")
public class GoStateTest {
	@DisplayName("Testing, that \"getNeighbors\" method returns the neighbors of a given point on the board.")
	@ParameterizedTest(name = "Board size: {0}; Point: {1}")
	@ArgumentsSource(NeighborPointsArgumentProvider.class)
	public void testGetNeighbors(int boardSize, Point inputPoint, List<Point> expectedNeighbors) {
		GoState state = new GoState(boardSize);
		Point[] neighbors = state.getNeighbors(inputPoint);
		assertEquals(expectedNeighbors, Arrays.asList(neighbors));
	}
	
	
	@DisplayName("Testing, that \"isLegalMove\" method returns false, for placing stone outside of the board's boundary.")
	@ParameterizedTest(name = "Board size: {0}; x: {1} y: {2}")
	@CsvSource({
		"9, 0, 9",
		"9, -3, 4",
		"13, 4, 15",
		"19, 19, 24"
	})
	public void testIsLegalMoveOutsideOfBoard(int boardSize, int x, int y) {
		GoState state = new GoState(boardSize);
		assertFalse(state.isLegalMove(new Point(x, y)));
	}
	
	@DisplayName("Testing, that \"isLegalMove\" method returns false, for placing stone on taken space.")
	@ParameterizedTest(name = "Board size: {0}; x: {1} y: {2}")
	@CsvSource({
		"9, 5, 5",
		"9, 8, 8",
		"13, 8, 7",
		"13, 0, 8",
		"19, 15, 3",
		"19, 18, 18"
	})
	public void testIsLegalMoveToCaputred(int boardSize, int x, int y) {
		GoState state = new GoState(boardSize);
		Point p = new Point(x, y);
		state.placeStone(p); // placing the stone
		assertAll(
				() -> assertFalse(state.isLegalMove(p)), // Opponent player unable to place a stone on the taken position
				() -> assertFalse(state.isLegalMove(p))  // Same player unable to place a stone again on the taken position
		);
	}
	
	static Stream<Arguments> provideSuicidalGoStates() {
	    return Stream.of(
	        Arguments.of("""
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,B,_,_,_,_,_
	        	_,_,B,_,B,_,_,_,_
	        	_,_,_,B,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	""", new Point(3, 2), Stone.WHITE),
	        Arguments.of("""
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,W,_,_,_,_,_,_,_,_,_
	        	_,_,W,B,W,_,_,_,_,_,_,_,_
	        	_,_,W,B,_,W,_,_,_,_,_,_,_
	        	_,_,W,W,W,W,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	""", new Point(4, 4), Stone.BLACK),
	        Arguments.of("""
	        	B,B,B,B,W,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	B,B,B,_,W,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	B,B,B,B,W,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	W,W,B,B,B,W,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,W,B,B,B,W,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,W,W,W,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_
	        	""", new Point(3, 1), Stone.BLACK)
	    );
	}
	@DisplayName("Testing, that \"isLegalMove\" doesn't allow suicidal move. (The group's \"liberty\" cannot become 0).")
	@ParameterizedTest
	@MethodSource("provideSuicidalGoStates")
	public void testIsLegalMoveSuicidal(String gameState, Point suicidalMove, Stone currentPlayerColor) {
		GoState state = GoStateParser.parseGoState(gameState);
		state.turn = currentPlayerColor;
		assertFalse(state.isLegalMove(suicidalMove));
	}
	
	
	/*
	  _,B,W,_,_,_,_,_,_
	  B,_,B,W,_,_,_,_,_
	  _,B,W,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	*/
	@DisplayName("Testing, that \"isLegalMove\" method returns false, if move would result in a repetition of the game state.")
	@Test
	public void testIsLegalMoveStateRepeated() {
		GoState state = new GoState(9);
		state.makeMove(new Point(1, 0));
		state.makeMove(new Point(2, 0));
		state.makeMove(new Point(0, 1));
		state.makeMove(new Point(3, 1));
		state.makeMove(new Point(1, 2));
		state.makeMove(new Point(2, 2));  
		state.makeMove(new Point(2, 1)); // The comment before this method displays the state here.
		state.makeMove(new Point(1, 1)); // White stone placed to (1, 1), removing black stone at (2, 1) 
		assertFalse(state.isLegalMove(new Point(2, 1))); // Would cause the same state as the comment shows, so it's illegal
	}
	
	
	static Stream<Arguments> provideCapturedGoStates() {
	    return Stream.of(
	        Arguments.of("""
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,B,_,_,_,_,_
	        	_,_,B,W,B,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	""", new Point(3, 3), Stone.BLACK, List.of(new Point(3, 2))),
	        Arguments.of("""
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,W,_,_,_,_,_,_,_,_,_
	        	_,_,W,B,W,_,_,_,_,_,_,_,_
	        	_,_,W,B,_,W,_,_,_,_,_,_,_
	        	_,_,W,W,W,W,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	""", new Point(4, 4), Stone.WHITE, List.of(new Point(3, 3), new Point(3, 4))),
	        Arguments.of("""
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,B,W,B
	        	_,_,_,_,_,_,B,W,W
	        	_,_,_,_,_,_,B,W,W
	        	""", new Point(7, 5), Stone.BLACK, List.of(new Point(7, 6), new Point(7, 7), new Point(7, 8), new Point(8, 7), new Point(8, 8)))
	    );
	}
	@DisplayName("Testing, that \"checkCaptured\" method handles capturing correctly.")
	@ParameterizedTest
	@MethodSource("provideCapturedGoStates")
	public void testCheckCaptured(String gameState, Point capturingMove, Stone currentPlayerColor, List<Point> capturedPositions) {
		GoState state = GoStateParser.parseGoState(gameState);
		state.turn = currentPlayerColor;
		state.placeStone(capturingMove); // encircle(capture) oppenent's stones
		
		int expectedCapturedStonesCount = capturedPositions.size();
		int capturedStonesCount = Stone.BLACK == currentPlayerColor ? state.getBlackCaptured() : state.getWhiteCaptured();
		assertAll(
			() -> assertEquals(expectedCapturedStonesCount, capturedStonesCount, 
					"The number of removed stones is not correct: %d != %d.".formatted(expectedCapturedStonesCount, capturedStonesCount)), // check whether captured stones counter is incremented correctly
			() -> assertAll(
					IntStream.range(0, capturedPositions.size())
						.mapToObj(i -> () -> {
							int x = capturedPositions.get(i).x;
							int y = capturedPositions.get(i).y;
							assertEquals(BoardSpace.EMPTY, state.board[y][x], "Captured stone at position x: %d y: %d wasn't removed".formatted(x, y));
					}) // check whether all encircled(captured) position is correctly handled (set to BoardSpace.EMPTY)
			)
		);
	}
	
	
	static Stream<Arguments> provideStatesForLibertyCheck() {
	    return Stream.of(
	        Arguments.of("""
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,B,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	""", new Point(2, 2), Stone.BLACK, List.of(new Point(2, 1), new Point(3, 2), new Point(2, 3), new Point(1, 2)), Set.of(new Point(2, 2))),
	        Arguments.of("""
		        _,_,_,_,_,_,_,_,_
		        _,_,W,_,_,_,_,_,_
		        _,_,B,_,_,_,_,_,_
		        _,_,_,_,_,_,_,_,_
		        _,_,_,_,_,_,_,_,_
		        _,_,_,_,_,_,_,_,_
		        _,_,_,_,_,_,_,_,_
		        _,_,_,_,_,_,_,_,_
		        _,_,_,_,_,_,_,_,_
		        """, new Point(2, 2), Stone.BLACK, List.of(new Point(3, 2), new Point(2, 3), new Point(1, 2)), Set.of(new Point(2, 2))),
	        Arguments.of("""
			    _,_,_,_,_,_,_,_,_
			    _,_,B,B,_,_,_,_,_
			    W,W,W,W,B,_,_,_,_
			    _,_,_,W,B,_,_,_,_
			    _,_,_,B,_,_,_,_,_
			    _,_,_,_,_,_,_,_,_
			    _,_,_,_,_,_,_,_,_
			    _,_,_,_,_,_,_,_,_
			    _,_,_,_,_,_,_,_,_
			    """, new Point(2, 2), Stone.WHITE, List.of(new Point(0, 1), new Point(1, 1), new Point(0, 3), new Point(1, 3), new Point(2, 3)),
			    	Set.of(new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2), new Point(3, 3)))
	    );
	}
	@DisplayName("Testing, that \"getLiberties\" method finds the correct liberties, and the correct group of stones.")
	@ParameterizedTest
	@MethodSource("provideStatesForLibertyCheck")
	public void testGetLiberties(String gameState, Point p, Stone playerColor, List<Point> expectedLiberties, Set<Point> expectedScannedPoints) {
		GoState state = GoStateParser.parseGoState(gameState);
		Set<Point> scannedPoints = new HashSet<>();
		Point[] liberties = state.getLiberties(playerColor, p, scannedPoints);
		
		assertAll(
				() -> assertTrue(expectedLiberties.containsAll(Arrays.asList(liberties))),
				() -> assertTrue(expectedScannedPoints.equals(scannedPoints))
		);
	}
	
	
	@DisplayName("Testing, that \"makeMove\" correctly handles the end of the game (both player pass)")
	@Test
	public void testMakeMoveEndGame() {
		GoState state = new GoState(9); // boardSize here doesn't matter
		
		assertAll(
				() -> assertFalse(state.makeMove(null)), // The first pass doesn't end the game 
				() -> assertTrue(state.makeMove(null))   // The second pass ends the game
		);
	}
	
	
	static Stream<Arguments> provideIllegalMoveSequence() {
	    return Stream.of(
	        Arguments.of(9, List.of(new Point(0, 0), new Point(0, 0))), // move to already taken position
	        Arguments.of(9, List.of(new Point(0, 0), new Point(-5, 0))), // move outside of the board's boundaries
	        Arguments.of(13, List.of(new Point(1, 0), new Point(4, 0), new Point(0, 1), new Point(0, 0))), // suicidal move
	        Arguments.of(19, List.of(new Point(18, 0), new Point(16, 9), new Point(2, 3), new  Point(18, 0))) // move to already taken position
	    );
	} // only the last move is illegal
	
	@DisplayName("Testing, that \"makeMove\" doesn't allow invalid moves (returns false, game state doesn't change)")
	@ParameterizedTest
	@MethodSource("provideIllegalMoveSequence")
	public void testMakeMoveInvalidMove(int boardSize, List<Point> illegalMoveSequence) {
		GoState state = new GoState(boardSize);
		illegalMoveSequence.stream()
			.limit(illegalMoveSequence.size() - 1)
			.forEach(state::makeMove);
		
		var prevStatesBeforeIllegalMove = new HashSet<>(state.getPrevStates());
		boolean illegalMoveResult = state.makeMove(illegalMoveSequence.getLast()); // only the last move is illegal in the provided move sequence
		assertAll(
				() -> assertFalse(illegalMoveResult), // makeMove returned false for the illegal move
				() -> assertTrue(prevStatesBeforeIllegalMove.equals(state.getPrevStates())) // the game state is unchanged
		);
	}
	
	
	static Stream<Arguments> provideValidMoveSequence() {
	    return Stream.of(
	        Arguments.of(9, List.of(new Point(0, 0), new Point(0, 4), new Point(6, 4))),
	        Arguments.of(13, List.of(new Point(1, 0), new Point(4, 0), new Point(0, 1), new Point(0, 7), new Point(12, 7))),
	        Arguments.of(19, List.of(new Point(18, 0), new Point(16, 9), new Point(2, 3), new  Point(18, 5), new Point(0, 0)))
	    );
	} // each move is legal
	
	@DisplayName("Testing, that \"makeMove\" handles valid moves correctly (prevStates, changed stone placed)")
	@ParameterizedTest
	@MethodSource("provideValidMoveSequence")
	public void testMakeMoveValidMove(int boardSize, List<Point> validMoveSequence) {
		GoState state = new GoState(boardSize);
		
		validMoveSequence.stream()
			.forEach(pos -> {
				var prevStatesBeforeMove = new HashSet<>(state.getPrevStates());
				state.makeMove(pos); // valid move
				assertAll(
					() -> assertFalse(prevStatesBeforeMove.equals(state.getPrevStates())), // State changed
					() -> assertTrue(state.board[pos.y][pos.x] != BoardSpace.EMPTY) // Stone placed
				);
		});
	}
	
	
	static Stream<Arguments> provideAlmostCapturedGoStates() {
	    return Stream.of(
	        Arguments.of("""
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,B,_,_,_,_,_
	        	_,_,B,W,B,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	""", new Point(3, 3), Stone.BLACK, 1, 0),
	        Arguments.of("""
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,W,_,_,_,_,_,_,_,_,_
	        	_,_,W,B,W,_,_,_,_,_,_,_,_
	        	_,_,W,B,_,W,_,_,_,_,_,_,_
	        	_,_,W,W,W,W,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_,_,_,_,_
	        	""", new Point(4, 4), Stone.WHITE, 0, 2),
	        Arguments.of("""
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,_,_,_
	        	_,_,_,_,_,_,B,W,B
	        	_,_,_,_,_,_,B,W,W
	        	_,_,_,_,_,_,B,W,W
	        	""", new Point(7, 5), Stone.BLACK, 5, 0)
	    );
	}
	@DisplayName("Testing, that \"makeMove\" handles captured stones correctly.")
	@ParameterizedTest
	@MethodSource("provideAlmostCapturedGoStates")
	public void testMakeMoveCapturedStones(String gameState, Point capturingMove, Stone currentPlayerColor, int expectedBlackCaptured, int expcetedWhiteCaptured) {
		GoState state = GoStateParser.parseGoState(gameState);
		state.turn = currentPlayerColor;
		state.makeMove(capturingMove); // encircle(capture) oppenent's stones
		
		assertAll(
				() -> assertEquals(expectedBlackCaptured, state.getBlackCaptured()),
				() -> assertEquals(expcetedWhiteCaptured, state.getWhiteCaptured())
		);
	}

	
	static Stream<Arguments> provideGoStates() {
	    return Stream.of(
	        Arguments.of(9, List.of(new Point(0, 0), new Point(0, 4), new Point(6, 4))),
	        Arguments.of(13, List.of(new Point(1, 0), new Point(4, 0), new Point(0, 1), new Point(0, 7), new Point(12, 7))),
	        Arguments.of(19, List.of(new Point(18, 0), new Point(16, 9), new Point(2, 3), new  Point(18, 5), new Point(0, 0)))
	    );
	}
	

	
	/*
	  _,W,_,_,_,_,_,_,_
	  W,_,_,_,_,_,_,_,_
	  _,_,B,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	  _,_,_,_,_,_,_,_,_
	*/
	@Test
	public void testSaveLoadGame() {
		GoState originalState = new GoState(9);
		GoState readedState;
		
		originalState.makeMove(new Point(0, 0));
		originalState.makeMove(new Point(1, 0));
		originalState.makeMove(new Point(2, 2));
		originalState.makeMove(new Point(0, 1));
		
		try {
			File file = new File("testSerialization.go");
			originalState.saveGame(file);
			
			readedState = GoState.loadGame(file);
			assertAll(
				() -> assertTrue(readedState.equals(originalState)), // this checks the "board" and "turn" fields, as the assignment required
				() -> assertEquals(readedState.getBlackCaptured(), originalState.getBlackCaptured()),
				() -> assertEquals(readedState.getWhiteCaptured(), originalState.getWhiteCaptured())
			);		
		} catch (IllegalArgumentException ex) {
			fail(ex.getMessage());
		}
	}
}

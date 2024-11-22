package gogame;

import java.util.stream.IntStream;

/**
 * Utility class, that provide methods to parse string represented GoStates.
 */
public class GoStateParser {
	/**
	 * Parses a GoState represented as a string (for testing purposes). 
	 * The board spaces are separated by commas. "_" indicates empty space, "B" a black rock, "W" a white rock.
	 * @param input is a GoState represented as a string.
	 * @return the GoState that the input described.
	 */
	public static GoState parseGoState(String input) {
		String lines[] = input.split("\n");
		GoState state = new GoState(lines.length);
		IntStream.range(0, lines.length)
			.forEach(i -> {
				 lines[i] = lines[i].replace(",", ""); // removing the commas
				 IntStream.range(0, lines[i].length())
					.forEach(j -> state.board[i][j] = parseLetter(lines[i].charAt(j)));
			}); // parsing the GoState
		return state;
	}
	
	/**
	 * Gets a letter (that is representing a BoardSpace), and converts it to a BoardSpace.
	 * @param letter that is representing a BoradSpace: empty/black/white.
	 * @return the BoardSpace that the input letter is representing.
	 */
	private static BoardSpace parseLetter(char letter) {
		return switch(letter) {
			case '_' -> BoardSpace.EMPTY;
			case 'B' -> BoardSpace.BLACK;
			case 'W' -> BoardSpace.WHITE;
			default -> throw new IllegalArgumentException("Invalid field: %c".formatted(letter));
		};
	}
}

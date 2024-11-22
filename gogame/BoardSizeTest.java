package gogame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static gogame.BoardSize.fromString;
public class BoardSizeTest {
	@ParameterizedTest
	@CsvSource({
		"NINE, 9x9",
		"THIRTEEN, 13x13",
		"NINETEEN, 19x19"
	})
	public void testFromString(BoardSize expected, String input) {
		assertEquals(expected, fromString(input));
	}
	
	@ParameterizedTest
	@CsvSource({
		"10x10",
		"5X5",
		"someDummyTxt",
		"a"
	})
	public void testFailingFromString(String input) {
		assertThrows(IllegalArgumentException.class, () -> fromString(input));
	}
}

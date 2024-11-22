package gogame;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

class NeighborPointsArgumentProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext ec) {
		return Stream.of(
				Arguments.of(9, new Point(0, 0), List.of(new Point(1, 0), new Point(0, 1))), // upper left corner
				Arguments.of(9, new Point(8, 0), List.of(new Point(8, 1), new Point(7, 0))), // upper right corner 
				Arguments.of(9, new Point(0, 8), List.of(new Point(0, 7), new Point(1, 8))), // bottom left corner
				Arguments.of(9, new Point(8, 8), List.of(new Point(8, 7), new Point(7, 8))), // bottom right corner
				
				Arguments.of(9, new Point(5, 0), List.of(new Point(6, 0), new Point(5, 1), new Point(4, 0))), // top edge
				Arguments.of(9, new Point(8, 2), List.of(new Point(8, 1), new Point(8, 3), new Point(7, 2))), // right edge
				Arguments.of(9, new Point(4, 8), List.of(new Point(4, 7), new Point(5, 8), new Point(3, 8))), // bottom edge
				Arguments.of(9, new Point(0, 6), List.of(new Point(0, 5), new Point(1, 6), new Point(0, 7))), // left edge
				
				
				Arguments.of(9, new Point(5, 5), List.of(new Point(5, 4), new Point(6, 5), new Point(5, 6), new Point(4,5))), // inside table
				Arguments.of(9, new Point(7, 4), List.of(new Point(7, 3), new Point(8, 4), new Point(7, 5), new Point(6, 4))) // inside table
		);
	}
}

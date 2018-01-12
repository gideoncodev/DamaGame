package dama.model.board;

public class BoardUtils {

	public static final boolean[] FIRST_COLUMN = initColumn(0);
	public static final boolean[] LAST_COLUMN = initColumn(7);

	public static final int NUM_TILES = 64;
	public static final int NUM_TILES_PER_ROW = 8;

	private BoardUtils() {
		throw new RuntimeException("Error, used for utils only!");
	}

	private static boolean[] initColumn(int columnNumber) {

		final boolean[] column = new boolean[NUM_TILES];

		do {
			column[columnNumber] = true;
			columnNumber += NUM_TILES_PER_ROW;
		} while(columnNumber < NUM_TILES);

		return column;
		
	}

	public static boolean isValidTileCoordinate(final int coordinate) {
		return coordinate >= 0 && coordinate < NUM_TILES;
	}
}
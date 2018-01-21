package dama.model.board;

import com.google.common.collect.ImmutableList;

import java.util.*;

public class BoardUtils {

	public static final List<Boolean> FIRST_COLUMN = initColumn(0);
	public static final List<Boolean> LAST_COLUMN = initColumn(7);

	public static final List<Boolean> FIRST_ROW = initRow(0);
	public static final List<Boolean> SECOND_ROW = initRow(8);
	public static final List<Boolean> THIRD_ROW = initRow(16);
	public static final List<Boolean> FOURTH_ROW = initRow(24);
	public static final List<Boolean> FIFTH_ROW = initRow(32);
	public static final List<Boolean> SIXTH_ROW = initRow(40);
	public static final List<Boolean> SEVENTH_ROW = initRow(48);
	public static final List<Boolean> EIGHTH_ROW = initRow(56);

	public static final int NUM_TILES = 64;
	public static final int NUM_TILES_PER_ROW = 8;

	private BoardUtils() {
		throw new RuntimeException("Error, used for utils only!");
	}

	private static List<Boolean> initColumn(int columnNumber) {

		final Boolean[] column = new Boolean[NUM_TILES];

		for(int i = 0; i < column.length; i++) {
            column[i] = false;
        }

		do {
			column[columnNumber] = true;
			columnNumber += NUM_TILES_PER_ROW;
		} while(columnNumber < NUM_TILES);

		return ImmutableList.copyOf(column);
		
	}

	private static List<Boolean> initRow(int rowNumber){
		final Boolean[] row = new Boolean[NUM_TILES];

		for(int i = 0; i < row.length; i++) {
            row[i] = false;
        }

		do {
			row[rowNumber] = true;
			rowNumber++;
		} while(rowNumber % NUM_TILES_PER_ROW != 0);

		return ImmutableList.copyOf(row);
	}

	public static boolean isValidTileCoordinate(final int coordinate) {
		return coordinate >= 0 && coordinate < NUM_TILES;
	}

	public static int getCoordinateAtPosition(final int coordinate) {
		return 0;
	}
}
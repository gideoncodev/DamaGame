package dama.model.board;

import dama.model.pieces.Piece;

import java.util.*;

public abstract class Tile {

	protected final int tileCoordinate;

	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

	private static final Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {

		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

		for(int i = 0; i < 64; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}

		return Collections.unmodifiableMap(emptyTileMap);
	}

	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}

	private Tile(int tileCoordinate) {
		this.tileCoordinate = tileCoordinate;
	}

	public abstract boolean isTileOccupied();

	public abstract Piece getPiece();
}
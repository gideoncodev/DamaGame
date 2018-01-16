package dama.model.board;

import dama.model.pieces.Piece;

import com.google.common.collect.ImmutableMap;

import java.util.*;

public abstract class Tile {

	protected final int tileCoordinate;

	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

	private static final Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {

		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

		for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}

		return ImmutableMap.copyOf(emptyTileMap);
	}

	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}

	private Tile(final int tileCoordinate) {
		this.tileCoordinate = tileCoordinate;
	}

	public abstract boolean isTileOccupied();

	public abstract Piece getPiece();

	public int getTileCoordinate() {
		return this.tileCoordinate;
	}
	
	private static final class EmptyTile extends Tile {
	
		private EmptyTile(final int tileCoordinate) {
			super(tileCoordinate);
		}

		@Override
		public String toString() {
			return "-";
		}

		@Override
		public boolean isTileOccupied() {
			return false;
		}

		@Override
		public Piece getPiece() {
			return null;
		}
	}

	private static final class OccupiedTile extends Tile {

		private final Piece pieceOnTile;

		private OccupiedTile(final int tileCoordinate,
							final Piece pieceOnTile) {
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}

		@Override
		public String toString() {
			return this.pieceOnTile.getPieceAlliance().isBlack() ? this.pieceOnTile.toString().toLowerCase() : this.pieceOnTile.toString();
		}

		@Override
		public boolean isTileOccupied() {
			return true;
		}

		@Override
		public Piece getPiece() {
			return this.pieceOnTile;
		}
	}
}
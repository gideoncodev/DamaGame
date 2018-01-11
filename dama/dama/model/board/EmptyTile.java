package dama.model.board;

import dama.model.pieces.Piece;

public static final class EmptyTile extends Tile {
	
	private EmptyTile(final int tileCoordinate) {
		super(tileCoordinate);
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
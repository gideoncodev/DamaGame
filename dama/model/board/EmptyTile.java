package dama.model.board;

import dama.model.pieces.Piece;

public final class EmptyTile extends Tile {
	
	public EmptyTile(final int tileCoordinate) {
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
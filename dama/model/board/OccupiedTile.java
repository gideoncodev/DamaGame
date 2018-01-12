package dama.model.board;

import dama.model.pieces.Piece;

public final class OccupiedTile extends Tile {

	private final Piece pieceOnTile;

	private OccupiedTile(final int tileCoordinate, final Piece pieceOnTile) {
		super(tileCoordinate);
		this.pieceOnTile = pieceOnTile;
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
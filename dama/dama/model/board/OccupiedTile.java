package dama.model.board;

import dama.model.pieces.Piece;

public static final class OccupiedTile extends Tile {

	private final Piece pieceOnTile;

	private OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
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
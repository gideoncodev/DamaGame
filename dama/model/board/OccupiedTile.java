package dama.model.board;

import dama.model.pieces.Piece;

public final class OccupiedTile extends Tile {

	private final Piece pieceOnTile;

	public OccupiedTile(final int tileCoordinate,
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
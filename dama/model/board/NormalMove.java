package dama.model.board;

import dama.model.pieces.Piece;

public final class NormalMove extends Move {

	NormalMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
		super(board, movedPiece, destinationCoordinate);
	}

}
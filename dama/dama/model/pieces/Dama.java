package dama.model.pieces;

import dama.model.board.Move;
import dama.model.board.Board;

import java.util.List;;

public class Dama extends Piece {

	Dama(final int piecePosition, final Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance);
	}

	@Override
	public List<Move> calculateLegalMoves(Board board) {
		return null;
	}
	
}
package dama.model.pieces;

import dama.model.board.NormalMove;
import dama.model.board.AttackMove;
import dama.model.board.Board;
import dama.model.board.BoardUtils;
import dama.model.board.Tile;

import java.util.*;

public class KingDama extends Piece {

	private static final int[] CANDIDATE_MOVE_COORDINATES = {-7, -9, 9, 7};

	KingDama(final int piecePosition, final Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {

		final List<Move> legalMoves = new ArrayList<>();

		for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
			int candidateDestinationCoordinate = this.piecePosition;
			while(BoardUtils.isValidTileCoordinate()) 
		}

	}
}
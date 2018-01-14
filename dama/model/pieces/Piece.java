package dama.model.pieces;

import dama.model.Alliance;
import dama.model.board.Move;
import dama.model.board.Board;

import java.util.Collection;

public abstract class Piece {
	
	protected final PieceType pieceType;
	protected final int piecePosition;
	protected final Alliance pieceAlliance;

	Piece(final PieceType pieceType,
		  final int piecePosition,
		  final Alliance pieceAlliance) {
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
	}

	public int getPiecePosition() {
		return this.piecePosition;
	}

	public Alliance getPieceAlliance() {
		return this.pieceAlliance;
	}

	public PieceType getPieceType() {
		return this.pieceType;
	}

	public abstract Collection<Move> calculateLegalMoves(final Board board);

	public enum PieceType {

		DAMA("D"),
		KINGDAMA("KD");

		private final String pieceName;

		PieceType(final String pieceName) {
			this.pieceName = pieceName;
		}

		@Override
		public String toString() {
			return this.pieceName;
		}
	}
}
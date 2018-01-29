package dama.model.pieces;

import dama.model.Alliance;
import dama.model.board.Move;
import dama.model.board.Board;

import java.util.Collection;

public abstract class Piece {
	
	protected final PieceType pieceType;
	protected final int piecePosition;
	protected final Alliance pieceAlliance;
	private final int cachedHashCode;

	Piece(final PieceType pieceType,
		  final int piecePosition,
		  final Alliance pieceAlliance) {
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		this.cachedHashCode = computeHashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if(!(other instanceof Piece)){
			return false;
		}
		final Piece otherPiece = (Piece) other;
		return piecePosition == otherPiece.getPiecePosition() &&
			   pieceType == otherPiece.getPieceType() &&
			   pieceAlliance == otherPiece.getPieceAlliance();
	}

	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}

	private int computeHashCode() {
		int result = pieceType.hashCode();
		result = 31 * result + pieceAlliance.hashCode();
		result = 31 * result + piecePosition;
		return result;
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

	public abstract Piece movePiece(final Move move);

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
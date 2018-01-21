package dama.model.board;

import dama.model.pieces.Piece;
import dama.model.board.Board.Builder;
import dama.model.board.BoardUtils;

public abstract class Move {

	protected final Board board;
	protected final Piece movedPiece;
	protected final int destinationCoordinate;

	public static final Move NULL_MOVE = new NullMove();

	private Move(final Board board,
				final Piece movedPiece,
				final int destinationCoordinate) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
	}

	private Move(final Board board,
				final int destinationCoordinate) {
		this.board = board;
		this.movedPiece = null;
		this.destinationCoordinate = destinationCoordinate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + this.destinationCoordinate;
		result = prime * result + this.movedPiece.hashCode();
		result = prime * result + this.movedPiece.getPiecePosition();
		
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}

		if(!(other instanceof Move)) {
			return false;
		}

		final Move otherMove = (Move) other;
		return this.getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
			   this.getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
			   this.getMovedPiece().equals(otherMove.getMovedPiece());
	}

	public int getCurrentCoordinate(){
		return this.movedPiece.getPiecePosition();
	}

	public int getDestinationCoordinate() {
		return this.destinationCoordinate;
	}

	public Piece getMovedPiece(){
		return this.movedPiece;
	}

	public boolean isAttack() {
		return false;
	}

	public Piece getAttackedPiece() {
		return null;
	}

	public Board execute() {
		final Builder builder = new Builder();

		for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
			if(!this.movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}

		for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
		}

		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());


		return builder.build();
	}

	public static final class NormalMove extends Move {

		public NormalMove(final Board board,
						  final Piece movedPiece,
						  final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}

		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof NormalMove && super.equals(other);
		}

		@Override
		public String toString() {
			return this.movedPiece.getPieceType().toString() + BoardUtils.getCoordinateAtPosition(this.destinationCoordinate);
		}

	}

	public static final class AttackMove extends Move {

		final Piece attackedPiece;

		public AttackMove(final Board board,
						  final Piece movedPiece,
						  final int destinationCoordinate,
						  final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}

		@Override
		public int hashCode() {
			return this.attackedPiece.hashCode() + super.hashCode();
		}

		@Override
		public boolean equals(final Object other) {
			if(this == other) {
				return true;
			}

			if(!(other instanceof AttackMove)) {
				return false;
			}

			final AttackMove otherAttackMove = (AttackMove) other;
			return super.equals(otherAttackMove) &&
				   this.getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
		}

		@Override
		public boolean isAttack() {
			return true;
		}

		@Override
		public Piece getAttackedPiece() {
			return this.attackedPiece;
		}

		@Override
		public Board execute() {
			final Builder builder = new Builder();

			for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}

			for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}

			builder.setPiece(this.movedPiece.movePiece(this));
			builder.removePiece(this.attackedPiece);
			builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());


			return builder.build();
		}

	}

	public static final class NullMove extends Move {

		public NullMove() {
			super(null, null, -1);
		}

		@Override
		public Board execute(){
			throw new RuntimeException("Cannot execute NULL MOVE!");
		}

	}

	public static class MoveFactory {
		private MoveFactory() {
			throw new RuntimeException("Cannot create instance!");
		}

		public static Move createMove(final Board board,
									  final int currentCoordinate,
									  final int destinationCoordinate) {
			for(final Move move : board.getAllLegalMoves()) {
				if(move.getCurrentCoordinate() == currentCoordinate &&
				   move.getDestinationCoordinate() == destinationCoordinate) {
				   	return move;
				}
			}
			return NULL_MOVE;
		}
	}
	
}
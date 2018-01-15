package dama.model.board;

import dama.model.pieces.Piece;

public abstract class Move {

	final Board board;
	final Piece movedPiece;
	final int destinationCoordinate;

	public static final Move NULL_MOVE = new NullMove();

	private Move(final Board board,
				final Piece movedPiece,
				final int destinationCoordinate) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + this.destinationCoordinate;
		result = prime * result + this.movedPiece.hashCode();
		
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
		return this.getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
			   this.getMovedPiece().equals(otherMove.getMovedPiece());
	}

	public int getCurrentCoordinate(){
		this.movedPiece.getPiecePosition();
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
		final Board.Builder builder = new Board.Builder();

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
			return super.equals(otherAttackMove) && this.getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
		}

		@Override
		public boolean isAttack() {
			return true;
		}

		@Override
		public Piece getAttackedPiece() {
			return this.attackedPiece;
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
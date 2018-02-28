package dama.model.board;

import dama.model.pieces.Piece;
import dama.model.board.Board.Builder;
import dama.model.board.BoardUtils;
import dama.model.pieces.Dama;
import dama.model.pieces.KingDama;

import java.util.List;
import java.util.ArrayList;

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

	public Board getBoard() {
		return this.board;
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

	public List<Piece> getAttackedPieces() {
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

	public static class NormalMove extends Move {

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
			return "Normal Move";
		}

	}

	public static final class AttackMove extends Move {

		final List<Piece> attackedPieces;

		public AttackMove(final Board board,
						  final Piece movedPiece,
						  final int destinationCoordinate,
						  final List<Piece> attackedPieces) {
			super(board, movedPiece, destinationCoordinate);
			this.attackedPieces = new ArrayList<>();
			this.attackedPieces.addAll(attackedPieces);
		}

		@Override
		public int hashCode() {
			return this.attackedPieces.hashCode() + super.hashCode();
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
				   this.getAttackedPieces().equals(otherAttackMove.getAttackedPieces());
		}

		@Override
		public String toString() {
			return "Attack Move";
		}

		@Override
		public boolean isAttack() {
			return true;
		}

		@Override
		public List<Piece> getAttackedPieces() {
			return this.attackedPieces;
		}

		@Override
		public Board execute() {
			final Builder builder = new Builder();

			for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece)){
					builder.setPiece(piece);
				}
			}

			for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
				if(!this.attackedPieces.contains(piece)){
					builder.setPiece(piece);
				}
			}

			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

			return builder.build();
		}

	}

	public static final class AdditionalAttackMove extends Move {

		final List<Piece> attackedPieces;

		public AdditionalAttackMove(final Board board,
						  final Piece movedPiece,
						  final int destinationCoordinate,
						  final List<Piece> attackedPieces) {
			super(board, movedPiece, destinationCoordinate);
			this.attackedPieces = new ArrayList<>();
			this.attackedPieces.addAll(attackedPieces);
		}

		@Override
		public int hashCode() {
			return this.attackedPieces.hashCode() + super.hashCode();
		}

		@Override
		public boolean equals(final Object other) {
			if(this == other) {
				return true;
			}

			if(!(other instanceof AttackMove)) {
				return false;
			}

			final AdditionalAttackMove otherAttackMove = (AdditionalAttackMove) other;
			return super.equals(otherAttackMove) &&
				   this.getAttackedPieces().equals(otherAttackMove.getAttackedPieces());
		}

		@Override
		public String toString() {
			return "Additional Attack Move";
		}

		@Override
		public boolean isAttack() {
			return true;
		}

		@Override
		public List<Piece> getAttackedPieces() {
			return this.attackedPieces;
		}

		@Override
		public Board execute() {
			final Builder builder = new Builder();

			for(final Piece piece : this.board.getActivePieces(this.movedPiece.getPieceAlliance())) {
				if(!this.movedPiece.equals(piece)){
					builder.setPiece(piece);
				}
			}

			for(final Piece piece : this.board.getActivePieces(this.movedPiece.getPieceAlliance().opposite())) {
				if(!this.attackedPieces.contains(piece)){
					builder.setPiece(piece);
				}
			}

			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setMoveMaker(this.movedPiece.getPieceAlliance());

			return builder.build();
		}

	}

	public static final class DamaPromotion extends NormalMove {

		final Move decoratedMove;
		final Piece promotedDama;

		public DamaPromotion(final Move decoratedMove) {
			super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
			this.decoratedMove = decoratedMove;
			this.promotedDama = (Dama) decoratedMove.getMovedPiece();
		}

		@Override
		public int hashCode() {
			return this.decoratedMove.hashCode() + (31 * promotedDama.hashCode());
		}

		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof DamaPromotion && super.equals(other);
		}

		@Override
		public boolean isAttack() {
			return this.decoratedMove.isAttack();
		}

		@Override
		public List<Piece> getAttackedPieces() {
			return this.decoratedMove.getAttackedPieces();
		}

		@Override
		public Board execute() {
			final Board damaMovedBoard = this.decoratedMove.execute();
			final Builder builder = new Builder();

			for(final Piece piece : damaMovedBoard.getCurrentPlayer().getActivePieces()) {
				builder.setPiece(piece);
			}

			for(final Piece piece : damaMovedBoard.getCurrentPlayer().getOpponent().getActivePieces()) {
				if(!this.promotedDama.equals(piece)) {
					builder.setPiece(piece);
				}
			}

			builder.setPiece(new KingDama(this.destinationCoordinate, this.promotedDama.getPieceAlliance()));
			builder.setMoveMaker(damaMovedBoard.getCurrentPlayer().getAlliance());

			return builder.build();
		}
	}

	public static final class NullMove extends Move {

		public NullMove() {
			super(null, 65);
		}

		@Override
		public Board execute(){
			throw new RuntimeException("Cannot execute NULL MOVE!");
		}

		@Override
		public int getCurrentCoordinate() {
			return -1;
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
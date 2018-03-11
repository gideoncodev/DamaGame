package dama.model.player;

import dama.model.board.Board;
import dama.model.board.Move;
import dama.model.board.MoveTransition;
import dama.model.board.MoveStatus;
import dama.model.pieces.Piece;
import dama.model.Alliance;

import com.google.common.collect.ImmutableList;

import java.util.*;

public abstract class Player {

	protected final Board board;
	protected final Collection<Move> legalMoves;
	private final boolean isGameOver;
	private final boolean isDraw;

	Player(final Board board,
		   final Collection<Move> playerMoves) {
		this.board = board;
		this.legalMoves = playerMoves;
		this.isGameOver = this.getActivePieces().isEmpty();
		this.isDraw = this.getActivePieces().isEmpty();
	}

	public Collection<Move> getLegalMoves() {
		return this.legalMoves;
	}

	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}

	public boolean isGameOver() {
		return this.isGameOver;
	}

	public boolean isDraw() {
		return this.isDraw;
	}

	public boolean hasAttackMoves() {
		for(final Move move : this.legalMoves) {
			if(move.isAttack()) {
				return true;
			}
		}
		return false;
	}

	public MoveTransition makeMove(final Move move) {
		if(!isMoveLegal(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}

		final Board transitionBoard = move.execute();
		
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}

	public abstract Collection<Piece> getActivePieces();
	public abstract Alliance getAlliance();
	public abstract Player getOpponent();
	
}
package dama.model.player;

import dama.model.board.Board;
import dama.model.board.Move;
import dama.model.pieces.Piece;
import dama.model.Alliance;

import java.util.*;

public abstract class Player {

	protected final Board board;
	protected final Collection<Move> legalMoves;
	private final boolean isGameOver;

	Player(final Board board,
		   final Collection<Move> playerMoves,
		   final Collection<Move> opponentMoves) {
		this.board = board;
		this.legalMoves = playerMoves;
		this.isGameOver = !Player.calculateOpponentMoves(opponentMoves);
	}

	private static Collection<Move> calculateOpponentMoves(final Collection<Move> moves) {
		final List<Move> attackMoves = new ArrayList<>();
		for(final Move move : moves) {
			
		}
	}

	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}

	public boolean isGameOver() {
		return this.isGameOver;
	}

	public MoveTransition makeMove(final Move move) {
		return null;
	}

	public abstract Collection<Piece> getActivePieces();

	public abstract Alliance getAlliance();

	public abstract Player getOpponent();
	
}
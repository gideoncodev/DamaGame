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

	Player(final Board board,
		   final Collection<Move> playerMoves,
		   final Collection<Move> opponentMoves) {
		this.board = board;
		this.legalMoves = playerMoves;
		this.isGameOver = Player.calculateOpponentMoves(opponentMoves).isEmpty();
	}

	public Collection<Move> getLegalMoves() {
		return this.legalMoves;
	}

	private static Collection<Move> calculateOpponentMoves(final Collection<Move> moves) {
		final List<Move> attackMoves = new ArrayList<>();
		for(final Move move : moves) {
			attackMoves.add(move);
		}
		return ImmutableList.copyOf(attackMoves);
	}

	// static Collection<Move> calculateAttacksOnTile(final int tile,
 //                                                   final Collection<Move> moves) {
 //        final List<Move> attackMoves = new ArrayList<>();
 //        for (final Move move : moves) {
 //            if (tile == move.getDestinationCoordinate()) {
 //                attackMoves.add(move);
 //            }
 //        }
 //        return ImmutableList.copyOf(attackMoves);
 //    }

	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}

	public boolean isGameOver() {
		return this.isGameOver;
	}

	// TODO check for stalemate status 
	public boolean isStaleMate() {
		return false;
	}

	public boolean hasMoves() {
		return false;
	}

	public MoveTransition makeMove(final Move move) {

		if(!isMoveLegal(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}

		final Board transitionBoard = move.execute();

		// final Collection<Move> damaAttacks = Player.calculateOpponentMoves(transitionBoard.getCurrentPlayer().getOpponent().getLegalMoves());

		// if(!damaAttacks.isEmpty()) {
		// 	return new MoveTransition(this.board, move, MoveStatus.LEAVE_PLAYER_IN_CHECK);
		// }

		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}

	public abstract Collection<Piece> getActivePieces();

	public abstract Alliance getAlliance();

	public abstract Player getOpponent();
	
}
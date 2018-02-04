package dama.model.player.ai;

import dama.model.board.Board;
import dama.model.board.Move;
import dama.model.board.MoveTransition;

public class MiniMax implements MoveStrategy {

	private final BoardEvaluator boardEvaluator;

	public MiniMax() {
		this.boardEvaluator = new StandardBoardEvaluator();
	}

	@Override
	public String toString() {
		return "MiniMax";
	}

	@Override
	public Move execute(Board board, int depth) {

		final long startTime = System.currentTimeMillis();

		Move bestMove = null;

		int highestSeenValue = Integer.MIN_VALUE;
		int lowestSeenValue = Integer.MAX_VALUE;
		int currentValue;

		System.out.println(board.getCurrentPlayer() + "THINKING with depth = " + depth);

		int numMoves = board.getCurrentPlayer().getLegalMoves().size();

		for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				currentValue = board.getCurrentPlayer().getAlliance().isWhite() ? 
							   this.min(moveTransition.getTransitionBoard(), depth -1) :
							   this.max(moveTransition.getTransitionBoard(), depth -1);

				if(board.getCurrentPlayer().getAlliance().isWhite() &&
				   currentValue >= highestSeenValue) {
				   	highestSeenValue = currentValue;
				   	bestMove = move;
				} else if(board.getCurrentPlayer().getAlliance().isBlack() &&
						  currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue;
					bestMove = move;
				}
			}
		}

		final long executionTime = System.currentTimeMillis() - startTime;

		return move;
	}

	public int min(final Board board, final int depth) {
		if(depth == 0 /* || gameOver*/) {
			return this.boardEvaluator.evaluate(board, depth);
		}

		int lowestSeenValue = Integer.MAX_VALUE;

		for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = this.max(moveTransition.getTransitionBoard(), depth -1);
				if(currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue;
				}
			}
		}
		return lowestSeenValue;
	}

	public int max(final Board board, final int depth) {
		if(depth == 0 /* || gameOver*/) {
			return this.boardEvaluator.evaluate(board, depth);
		}

		int highestSeenValue = Integer.MIN_VALUE;

		for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = this.min(moveTransition.getTransitionBoard(), depth -1);
				if(currentValue >= highestSeenValue) {
					highestSeenValue = currentValue;
				}
			}
		}
		return highestSeenValue;
	}

}
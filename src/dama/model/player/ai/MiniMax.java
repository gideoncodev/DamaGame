package dama.model.player.ai;

import dama.model.Alliance;
import dama.model.board.Board;
import dama.model.board.Move;
import dama.model.board.MoveTransition;

public class MiniMax implements MoveStrategy {

	private final BoardEvaluator boardEvaluator;
	private final int searchDepth;

	public MiniMax(final int searchDepth) {
		this.toString();
		this.boardEvaluator = StandardBoardEvaluator.get();
		this.searchDepth = searchDepth;
	}

	@Override
	public String toString() {
		return "MiniMax";
	}

	@Override
	public Move execute(Board board) {

		final long startTime = System.currentTimeMillis();

		Move bestMove = null;

		int highestSeenValue = Integer.MIN_VALUE;
		int lowestSeenValue = Integer.MAX_VALUE;
		int currentValue;

		System.out.println(board.getCurrentPlayer() + " THINKING with depth = " + this.searchDepth);

		int numMoves = board.getCurrentPlayer().getLegalMoves().size();

		for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				currentValue = board.getCurrentPlayer().getAlliance().isWhite() ? 
							   this.min(move, moveTransition.getTransitionBoard(), this.searchDepth - 1) :
							   this.max(move, moveTransition.getTransitionBoard(), this.searchDepth - 1);

				if(board.getCurrentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue) {
				   	highestSeenValue = currentValue;
				   	bestMove = move;
				   	if(moveTransition.getTransitionBoard().getPlayer(Alliance.BLACK).isGameOver()) break;
				} else if(board.getCurrentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue;
					bestMove = move;
				   	if(moveTransition.getTransitionBoard().getPlayer(Alliance.WHITE).isGameOver()) break;
				}
			}
		}

		final long executionTime = System.currentTimeMillis() - startTime;

		return bestMove;
	}

	public int min(final Move moved, final Board board, final int depth) {
		if(depth == 0 || isEndGameScenario(board) || moved.isAttack()) {
			return this.boardEvaluator.evaluate(moved, board, depth);
		}

		int lowestSeenValue = Integer.MAX_VALUE;

		for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = this.max(move, moveTransition.getTransitionBoard(), depth -1);
				if(currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue;
				}
			}
		}
		return lowestSeenValue;
	}

	public int max(final Move moved, final Board board, final int depth) {
		if(depth == 0 || isEndGameScenario(board) || moved.isAttack()) {
			return this.boardEvaluator.evaluate(moved, board, depth);
		}

		int highestSeenValue = Integer.MIN_VALUE;

		for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = this.min(move, moveTransition.getTransitionBoard(), depth -1);
				if(currentValue >= highestSeenValue) {
					highestSeenValue = currentValue;
				}
			}
		}
		return highestSeenValue;
	}

	private static boolean isEndGameScenario(final Board board) {
		return board.getCurrentPlayer().isGameOver();
	}
}
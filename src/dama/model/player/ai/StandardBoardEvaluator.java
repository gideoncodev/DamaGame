package dama.model.player.ai;

import dama.model.board.Board;
import dama.model.board.Move;
import dama.model.Alliance;
import dama.model.player.Player;
import dama.model.pieces.Piece;

public final class StandardBoardEvaluator implements BoardEvaluator {
	private static final int GAME_OVER_BONUS = 500;
	private static final int DEPTH_BONUS = 100;
	private static final int ATTACK_MULTIPLIER = 5;

	private final static StandardBoardEvaluator INSTANCE = new StandardBoardEvaluator();

	private StandardBoardEvaluator() {
	}

	public static StandardBoardEvaluator get() {
		return INSTANCE;
	}

	@Override
	public int evaluate(final Board board,
						final int depth) {
		return this.scorePlayer(board.getPlayer(Alliance.WHITE), depth) - 
			   this.scorePlayer(board.getPlayer(Alliance.BLACK), depth);
	}

	private int scorePlayer(final Player player,
							final int depth) {
		return StandardBoardEvaluator.pieceValue(player) +
			   StandardBoardEvaluator.mobility(player) +
			   StandardBoardEvaluator.gameOver(player, depth) +
			   StandardBoardEvaluator.attacks(player);
	}

	private static int pieceValue(final Player player) {
		int pieceValueScore = 0;
		for(final Piece piece : player.getActivePieces()) {
			pieceValueScore += piece.getPieceValue();
		}

		return pieceValueScore;
	}

	private static int attacks(final Player player) {
		int attackScore = 0;
		for(final Move move : player.getLegalMoves()) {
			if(move.isAttack()) {
				attackScore++;
			}
		}

		return attackScore * ATTACK_MULTIPLIER;
	}

	private static int mobility(final Player player) {
		return player.getLegalMoves().size();
	}

	private static int gameOver(final Player player,
						 final int depth) {
		return player.getOpponent().isGameOver() ? GAME_OVER_BONUS * StandardBoardEvaluator.depthBonus(depth) : 0;
	}

	private static int depthBonus(final int depth) {
		return (depth == 0) ? 1 : (DEPTH_BONUS * depth);
	}

}
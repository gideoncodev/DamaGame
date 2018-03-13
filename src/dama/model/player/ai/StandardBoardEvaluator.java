package dama.model.player.ai;

import dama.model.board.Board;
import dama.model.board.Move;
import dama.model.Alliance;
import dama.model.player.Player;
import dama.model.pieces.Piece;

public final class StandardBoardEvaluator implements BoardEvaluator {
	private static final int GAME_OVER_BONUS = 10000;
	private static final int DEPTH_BONUS = 100;
	private static final int ATTACK_MULTIPLIER = 5000000;
	private static final int DRAW_BONUS = 1000;

	private final static StandardBoardEvaluator INSTANCE = new StandardBoardEvaluator();

	private StandardBoardEvaluator() {
	}

	public static StandardBoardEvaluator get() {
		return INSTANCE;
	}

	@Override
	public int evaluate(final Move move,
						final Board board,
						final int depth) {
		return this.scorePlayer(move, board.getPlayer(Alliance.WHITE), depth) - 
			   this.scorePlayer(move, board.getPlayer(Alliance.BLACK), depth);
	}

	private int scorePlayer(final Move move,
							final Player player,
							final int depth) {
		return StandardBoardEvaluator.pieceValue(player) +
			   StandardBoardEvaluator.mobility(player) +
			   StandardBoardEvaluator.gameOver(player, depth) +
			   StandardBoardEvaluator.moveAttacks(move) + 
			   StandardBoardEvaluator.drawGame(player, depth);
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
				attackScore += move.getMovedPiece().getPieceValue();
				for(final Piece piece : move.getAttackedPieces()) {
					attackScore += piece.getPieceValue();
				}
			}
		}

		return attackScore * ATTACK_MULTIPLIER;
	}

	private static int moveAttacks(final Move move) {
		int attackScore = 0;
		if(move.isAttack()) {
			for(final Piece piece : move.getAttackedPieces()) {
				attackScore += piece.getPieceValue();
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

	private static int drawGame(final Player player,
								final int depth) {
		return player.isDraw() ? DRAW_BONUS * StandardBoardEvaluator.depthBonus(depth) : 0;
	}

	private static int depthBonus(final int depth) {
		return (depth == 0) ? 1 : (DEPTH_BONUS * depth);
	}

}
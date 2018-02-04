package dama.model.player.ai;

import dama.model.board.Board;
import dama.model.Alliance;
import dama.model.player.Player;
import dama.model.pieces.Piece;

public final class StandardBoardEvaluator implements BoardEvaluator {
	@Override
	public int evaluate(final Board board,
						final int depth) {
		return this.scorePlayer(board, board.getPlayer(Alliance.WHITE), depth) - 
			   this.scorePlayer(board, board.getPlayer(Alliance.BLACK), depth);
	}

	private int scorePlayer(final Board board,
							final Player player,
							final int depth) {
		return  pieceValue(player);
	}

	private static int pieceValue(final Player player) {
		int pieceValueScore = 0;
		for(final Piece piece : player.getActivePieces()) {
			pieceValueScore += piece.getPieceValue();
		}

		return pieceValueScore;
	}
}
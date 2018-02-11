package dama.model.player.ai;

import dama.model.board.Board;

public interface BoardEvaluator {
	int evaluate(Board board, int depth);
}
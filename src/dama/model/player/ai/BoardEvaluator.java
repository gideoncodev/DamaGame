package dama.model.player.ai;

import dama.model.board.Board;
import dama.model.board.Move;

public interface BoardEvaluator {
	int evaluate(Move move, Board board, int depth);
}
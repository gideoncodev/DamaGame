package dama.model.player.ai;

import dama.model.board.Board;
import dama.model.board.Move;

public interface MoveStrategy {

	Move execute(Board board, int depth);

}
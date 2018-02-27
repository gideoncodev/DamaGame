package dama.view;

import dama.model.board.Board;
import dama.model.board.Move;

import java.util.Map;
import java.util.HashMap;

public class MoveLog {
	private final Map<Move, Board> movesBoards;

	MoveLog() {
		this.movesBoards = new HashMap<>();
	}
}
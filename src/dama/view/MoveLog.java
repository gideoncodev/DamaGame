package dama.view;

import dama.model.board.Board;
import dama.model.board.Move;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class MoveLog {
	private final Map<Move, Board> undoMapMoves;
	private final Map<Move, Board> redoMapMoves;
	private final List<Move> allMoves;
	private final List<Move> undoListMoves;
	private final List<Move> redoListMoves;

	MoveLog() {
		this.undoMapMoves = new HashMap<>();
		this.redoMapMoves = new HashMap<>();
		this.allMoves = new ArrayList<>();
		this.undoListMoves = new ArrayList<>();
		this.redoListMoves = new ArrayList<>();
	}

	public List<Move> getUndoListMoves() {
		return this.undoListMoves;
	}

	public List<Move> getRedoListMoves() {
		return this.redoListMoves;
	}

	public Move removeUndoListMoves(final int key) {
		return this.undoListMoves.remove(key);
	}

	public Move removeRedoListMoves(final int key) {
		return this.redoListMoves.remove(key);
	}

	public Board removeUndoMapMoves(final Move move) {
		return this.undoMapMoves.remove(move);
	}

	public Board removeRedoMapMoves(final Move move) {
		return this.redoMapMoves.remove(move);
	}

	public void addUndoMoves(final Move move, final Board board) {
		this.undoListMoves.add(move);
		this.undoMapMoves.put(move, board);
	}

	public void addRedoMoves(final Move move, final Board board) {
		this.redoListMoves.add(move);
		this.redoMapMoves.put(move, board);
	}

	public void clear() {
		this.undoMapMoves.clear();
		this.redoMapMoves.clear();
		this.undoListMoves.clear();
		this.redoListMoves.clear();
	}
}
package dama.model.board;

import dama.model.board.Board;
import dama.model.board.Move;
import dama.model.pieces.Piece;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class MoveLog {
	private final Map<Move, Board> undoMapMoves;
	private final Map<Move, Board> redoMapMoves;
	private final List<Piece> attackedPieces;
	private final List<Move> undoListMoves;
	private final List<Move> redoListMoves;

	public MoveLog() {
		this.undoMapMoves = new HashMap<>();
		this.redoMapMoves = new HashMap<>();
		this.attackedPieces = new ArrayList<>();
		this.undoListMoves = new ArrayList<>();
		this.redoListMoves = new ArrayList<>();
	}

	public List<Move> getUndoMoves() {
		return this.undoListMoves;
	}

	public List<Move> getRedoMoves() {
		return this.redoListMoves;
	}

	public List<Piece> getAttackedPieces() {
		return this.attackedPieces;
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
		if(move.isAttack()) {
			this.attackedPieces.addAll(move.getAttackedPieces());
		}
		this.undoListMoves.add(move);
		this.undoMapMoves.put(move, board);
	}

	public void addRedoMoves(final Move move, final Board board) {
		if(move.isAttack()) {
			for(final Piece piece : move.getAttackedPieces()) {
				this.attackedPieces.remove(piece);
			}
		}
		this.redoListMoves.add(move);
		this.redoMapMoves.put(move, board);
	}

	public void clearRedoMoves(){
		this.redoMapMoves.clear();
		this.redoListMoves.clear();
	}

	public void clear() {
		this.attackedPieces.clear();
		this.undoMapMoves.clear();
		this.redoMapMoves.clear();
		this.undoListMoves.clear();
		this.redoListMoves.clear();
	}
}
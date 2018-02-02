package dama.model.player;

import dama.model.board.Board;
import dama.model.board.Move;
import dama.model.pieces.Piece;
import dama.model.Alliance;

import java.util.*;

public class BlackPlayer extends Player {
	public BlackPlayer(final Board board,
					   final Collection<Move> whiteStandardLegalMoves,
					   final Collection<Move> blackStandardLegalMoves) {
		super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces(){
		return this.board.getActivePieces(Alliance.BLACK);
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

	@Override
	public Player getOpponent() {
		return this.board.getPlayer(Alliance.WHITE);
	}

	@Override
	public String toString() {
		return Alliance.BLACK.toString();
	}
}
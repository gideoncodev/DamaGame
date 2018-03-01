package dama.model.player;

import dama.model.board.Board;
import dama.model.board.Move;
import dama.model.pieces.Piece;
import dama.model.Alliance;

import java.util.*;

public class WhitePlayer extends Player {
	public WhitePlayer(final Board board,
					   final Collection<Move> whiteStandardLegalMoves) {
		super(board, whiteStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces(){
		return this.board.getActivePieces(Alliance.WHITE);
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		return this.board.getPlayer(Alliance.BLACK);
	}

	@Override
	public String toString() {
		return Alliance.WHITE.toString();
	}
}
package dama.model.pieces;

import dama.model.board.Move;
import dama.model.board.Move.*;
import dama.model.board.Board;
import dama.model.board.BoardUtils;
import dama.model.board.Tile;
import dama.model.Alliance;

import com.google.common.collect.ImmutableList;

import java.util.*;

public class Dama extends Piece {

	private static final int[] CANDIDATE_MOVE_COORDINATES = {7, 9};

	public Dama(final int piecePosition,
				final Alliance pieceAlliance) {
		super(PieceType.DAMA, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();

		for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
			if(isFirstColumnExclusion(this.piecePosition, candidateCoordinateOffset) ||
				isLastColumnExclusion(this.piecePosition, candidateCoordinateOffset)) {
				continue;
			}

			final int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirections() * candidateCoordinateOffset);

			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if(!candidateDestinationTile.isTileOccupied()) {
					legalMoves.add(new NormalMove(board, this, candidateDestinationCoordinate));
				} else {
					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
					final int attackCandidateDestinationCoordinate = candidateDestinationCoordinate + 
																	 (this.pieceAlliance.getDirections() * candidateCoordinateOffset);
					if(this.pieceAlliance != pieceAlliance) {
						System.out.println("Piece current alliance: " + this.pieceAlliance.allianceToString());
						System.out.println("Destination alliance: " + pieceAlliance.allianceToString());
						System.out.println("Attack Move : " + attackCandidateDestinationCoordinate);
						legalMoves.add(new AttackMove(board, this, attackCandidateDestinationCoordinate , pieceAtDestination));
					}
				}

			}

		}

		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public Dama movePiece(Move move) {
		return new Dama(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
	}

	@Override
	public String toString() {
		return Piece.PieceType.DAMA.toString();
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN.get(currentPosition) && candidateOffset == 9;
	}

	private static boolean isLastColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.LAST_COLUMN.get(currentPosition) && candidateOffset == 7;
	}

}
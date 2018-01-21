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
	private static final int[] CANDIDATE_ADDITIONAL_ATTACK_MOVE_COORDINATES = {-9, -7, 7, 9};

	public Dama(final int piecePosition,
				final Alliance pieceAlliance) {
		super(PieceType.DAMA, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();

		for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {

			if(this.pieceAlliance.isWhite() &&
			   ((BoardUtils.FIRST_COLUMN.get(this.piecePosition) && candidateCoordinateOffset == 9) ||
			   	(BoardUtils.LAST_COLUMN.get(this.piecePosition) && candidateCoordinateOffset == 7))) {
				continue;
			}

			if(this.pieceAlliance.isBlack() &&
			   ((BoardUtils.FIRST_COLUMN.get(this.piecePosition) && candidateCoordinateOffset == 7) ||
			   	(BoardUtils.LAST_COLUMN.get(this.piecePosition) && candidateCoordinateOffset == 9))) {
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
					if(this.pieceAlliance != pieceAlliance) {
						if(!BoardUtils.FIRST_COLUMN.get(pieceAtDestination.getPiecePosition()) &&
						   !BoardUtils.LAST_COLUMN.get(pieceAtDestination.getPiecePosition())) {
						   	final int attackCandidateDestinationCoordinate = candidateDestinationCoordinate + 
																		 	 (this.pieceAlliance.getDirections() * candidateCoordinateOffset);
							if(BoardUtils.isValidTileCoordinate(attackCandidateDestinationCoordinate)) {
								final Tile candidateAttackDestinationTile = board.getTile(attackCandidateDestinationCoordinate);

								if(!candidateAttackDestinationTile.isTileOccupied()) {
									legalMoves.add(new AttackMove(board, this, attackCandidateDestinationCoordinate , pieceAtDestination));
								}
								//check for additional attack move candidate
								for(final int candidateAdditionalAttackOffset : CANDIDATE_ADDITIONAL_ATTACK_MOVE_COORDINATES) {
									//TODO decide to have a while loop in order to add the additional attack move
								}

							}
						}
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

}
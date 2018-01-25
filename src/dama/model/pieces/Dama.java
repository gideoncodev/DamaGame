package dama.model.pieces;

import dama.model.board.Move;
import dama.model.board.Move.*;
import dama.model.board.Board;
import dama.model.board.Board.Builder;
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

			final int candidateCoordinate = this.piecePosition + (this.pieceAlliance.getDirections() * candidateCoordinateOffset);

			if(BoardUtils.isValidTileCoordinate(candidateCoordinate)) {
				final Tile candidateDestinationTile = board.getTile(candidateCoordinate);
				if(!candidateDestinationTile.isTileOccupied()) {
					legalMoves.add(new NormalMove(board, this, candidateCoordinate));
				} else {
					final Piece candidateAttackPiece = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = candidateAttackPiece.getPieceAlliance();
					final int attackCandidateDestinationCoordinate = candidateCoordinate + 
																	 	 (this.pieceAlliance.getDirections() * candidateCoordinateOffset);
					final Tile candidateAttackDestinationTile = board.getTile(attackCandidateDestinationCoordinate);

					if(this.pieceAlliance != pieceAlliance && !BoardUtils.isTileOnTheEdge(candidateAttackPiece.getPiecePosition())) {
						if(BoardUtils.isValidTileCoordinate(attackCandidateDestinationCoordinate) &&
						   !candidateAttackDestinationTile.isTileOccupied()) {
								legalMoves.add(new AttackMove(board, this, attackCandidateDestinationCoordinate, candidateAttackPiece));
							//check for additional attack moves
							List<Move> legalAttackMoves = new ArrayList<>();
							boolean isFirstTransition = true;
							do {
								if(isFirstTransition) {
									List<Piece> candidateAttackedPieces = new ArrayList<>();
									candidateAttackedPieces.add(candidateAttackPiece);
									Move move = new AdditionalAttackMove(board, this, attackCandidateDestinationCoordinate, candidateAttackedPieces);
									Board transitionBoard = move.execute();
									AttackDama attackDama = new AttackDama(attackCandidateDestinationCoordinate, this.pieceAlliance, candidateAttackedPieces);

									final List<Move> legalAttackMoves.addAll(attackDama.calculateLegalMoves(transitionBoard));
									for(final Move move : )
									isFirstTransition = false;
								} else {

								}
								

								System.out.println(transitionBoard);
							} while(!legalAttackMoves.isEmpty());
							
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
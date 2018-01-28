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

	private static final int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};

	public Dama(final int piecePosition,
				final Alliance pieceAlliance) {
		super(PieceType.DAMA, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();

		for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {

			if(isFirstColumnExclusion(this.pieceAlliance, this.piecePosition, candidateCoordinateOffset) ||
				isLastColumnExclusion(this.pieceAlliance, this.piecePosition, candidateCoordinateOffset)) continue;

			final int candidateCoordinate = this.piecePosition + (this.pieceAlliance.getDirections() * candidateCoordinateOffset);
			if(BoardUtils.isValidTileCoordinate(candidateCoordinate)) {
				final Tile candidateDestinationTile = board.getTile(candidateCoordinate);
				if(!candidateDestinationTile.isTileOccupied()) {
					if(candidateCoordinateOffset == 7 || candidateCoordinateOffset == 9) {
						legalMoves.add(new NormalMove(board, this, candidateCoordinate));
					}
				} else {
					final Piece candidateAttackPiece = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = candidateAttackPiece.getPieceAlliance();
					final int attackCandidateDestinationCoordinate = candidateCoordinate + 
																	 	 (this.pieceAlliance.getDirections() * candidateCoordinateOffset);

					if(this.pieceAlliance != pieceAlliance && BoardUtils.isValidTileCoordinate(attackCandidateDestinationCoordinate)) {
						final Tile candidateAttackDestinationTile = board.getTile(attackCandidateDestinationCoordinate);
						if(!BoardUtils.isTileOnTheEdge(candidateAttackPiece.getPiecePosition()) &&
						   !candidateAttackDestinationTile.isTileOccupied()) {

						   	List<Piece> candidateAttackedPieces = new ArrayList<>();
							candidateAttackedPieces.add(candidateAttackPiece);

							legalMoves.add(new AttackMove(board, this, attackCandidateDestinationCoordinate, candidateAttackedPieces));
							//check for additional attack moves
							List<Move> legalAttackMoves = new ArrayList<>();
							Move checkMove = new AdditionalAttackMove(board, this, attackCandidateDestinationCoordinate, candidateAttackedPieces);
							AttackDama checkAttackDama = new AttackDama(attackCandidateDestinationCoordinate, this.pieceAlliance, candidateAttackedPieces);
							legalAttackMoves.addAll(checkAttackDama.calculateLegalMoves(checkMove.execute()));

							while(!legalAttackMoves.isEmpty()) {
								final List<Move> addLegalAttackMoves = new ArrayList<>();
								addLegalAttackMoves.addAll(legalAttackMoves);
								legalAttackMoves.clear();

								for(final Move move : addLegalAttackMoves) {
									legalMoves.add(new AttackMove(board, this, move.getDestinationCoordinate(), move.getAttackedPieces()));
									AttackDama attackDama = new AttackDama(move.getDestinationCoordinate(), this.pieceAlliance, move.getAttackedPieces());
									legalAttackMoves.addAll(attackDama.calculateLegalMoves(move.execute()));
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

	private static boolean isFirstColumnExclusion(final Alliance currentAlliance, final int currentPosition, final int candidateOffset) {
		return (currentAlliance.isBlack()) ? BoardUtils.FIRST_COLUMN.get(currentPosition) && (candidateOffset == -9 || candidateOffset == 7) :
												BoardUtils.FIRST_COLUMN.get(currentPosition) && (candidateOffset == -7 || candidateOffset == 9);
	}

	private static boolean isLastColumnExclusion(final Alliance currentAlliance, final int currentPosition, final int candidateOffset) {
		return (currentAlliance.isBlack()) ? BoardUtils.LAST_COLUMN.get(currentPosition) && (candidateOffset == -7 || candidateOffset == 9) :
												BoardUtils.LAST_COLUMN.get(currentPosition) && (candidateOffset == -9 || candidateOffset == 7);
	}

}
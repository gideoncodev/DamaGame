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

			int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirections() * candidateCoordinateOffset);
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if(!candidateDestinationTile.isTileOccupied()) {
					if(candidateCoordinateOffset == 7 || candidateCoordinateOffset == 9) {
						if(this.pieceAlliance.isDamaPromotionTile(candidateDestinationCoordinate)) {
							legalMoves.add(new DamaPromotion(new NormalMove(board, this, candidateDestinationCoordinate)));
						} else {
							legalMoves.add(new NormalMove(board, this, candidateDestinationCoordinate));
						}
					}
				} else {
					final Piece candidateAttackPiece = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = candidateAttackPiece.getPieceAlliance();
					candidateDestinationCoordinate += (this.pieceAlliance.getDirections() * candidateCoordinateOffset);

					if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
						final Tile candidateAttackDestinationTile = board.getTile(candidateDestinationCoordinate);
						if(candidateAttackDestinationTile.isTileOccupied()) continue;
						if(this.pieceAlliance != pieceAlliance && 
						   !BoardUtils.isTileOnTheEdge(candidateAttackPiece.getPiecePosition()) &&
						   !candidateAttackDestinationTile.isTileOccupied()) {

						   	final List<Piece> candidateAttackedPieces = new ArrayList<>();
							candidateAttackedPieces.add(candidateAttackPiece);

							if(this.pieceAlliance.isDamaPromotionTile(candidateDestinationCoordinate)) {
								legalMoves.add(new DamaPromotion(new AttackMove(board, this, candidateDestinationCoordinate, candidateAttackedPieces)));
							} else {
								legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, candidateAttackedPieces));
							}
							//check for additional attack moves
							final List<Move> legalAttackMoves = new ArrayList<>();
							final Move checkMove = new AdditionalAttackMove(board, this, candidateDestinationCoordinate, candidateAttackedPieces);
							final AttackDama checkAttackDama = new AttackDama(candidateDestinationCoordinate, this.pieceAlliance, candidateAttackedPieces);
							legalAttackMoves.addAll(checkAttackDama.calculateLegalMoves(checkMove.execute()));

							while(!legalAttackMoves.isEmpty()) {
								final List<Move> addLegalAttackMoves = new ArrayList<>();
								addLegalAttackMoves.addAll(legalAttackMoves);
								legalAttackMoves.clear();
								for(final Move move : addLegalAttackMoves) {
									if(this.pieceAlliance.isDamaPromotionTile(move.getDestinationCoordinate())) {
										legalMoves.add(new DamaPromotion(new AttackMove(board, this, move.getDestinationCoordinate(), move.getAttackedPieces())));
									} else {
										legalMoves.add(new AttackMove(board, this, move.getDestinationCoordinate(), move.getAttackedPieces()));
									}
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
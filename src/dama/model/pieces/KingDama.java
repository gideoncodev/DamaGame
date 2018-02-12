package dama.model.pieces;

import dama.model.board.Move;
import dama.model.board.Move.*;
import dama.model.board.Board;
import dama.model.board.BoardUtils;
import dama.model.board.Tile;
import dama.model.Alliance;

import com.google.common.collect.ImmutableList;

import java.util.*;

public class KingDama extends Piece {

	private static final int[] CANDIDATE_MOVE_COORDINATES = { -9, -7, 7, 9 };

	public KingDama(final int piecePosition,
					final Alliance pieceAlliance) {
		super(PieceType.KINGDAMA, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();

		for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {

			final List<Piece> attackedPieces = new ArrayList<>();
			int candidateDestinationCoordinate = this.piecePosition;

			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
				   isLastColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) break;

				candidateDestinationCoordinate += candidateCoordinateOffset;

				final List<Move> legalAttackMoves = new ArrayList<>();

				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
					if(!candidateDestinationTile.isTileOccupied()) {
						if(!hasPreviousAttackMove(legalMoves, (candidateDestinationCoordinate - candidateCoordinateOffset))) {
							legalMoves.add(new NormalMove(board, this, candidateDestinationCoordinate));
						} else {
							legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, attackedPieces));

							final Move checkMove = new AdditionalAttackMove(board, this, candidateDestinationCoordinate, attackedPieces);
							final AttackKingDama checkAttackKingDama = new AttackKingDama(candidateDestinationCoordinate,
																						  this.pieceAlliance, attackedPieces,
																						  this.getMoveCoordinates(candidateCoordinateOffset));
							
							legalAttackMoves.addAll(checkAttackKingDama.calculateLegalMoves(checkMove.execute()));

						}
					} else {
						if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
				   		   isLastColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) break;

						final Piece candidateAttackPiece = candidateDestinationTile.getPiece();
						final Alliance pieceAlliance = candidateAttackPiece.getPieceAlliance();
						candidateDestinationCoordinate += candidateCoordinateOffset;

						if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
							final Tile candidateAttackDestinationTile = board.getTile(candidateDestinationCoordinate);
							if(candidateAttackDestinationTile.isTileOccupied() || this.pieceAlliance == pieceAlliance) break;
							if(this.pieceAlliance != pieceAlliance &&
							   !BoardUtils.isTileOnTheEdge(candidateAttackPiece.getPiecePosition()) &&
						   	   !candidateAttackDestinationTile.isTileOccupied()) {
						   	   	attackedPieces.add(candidateAttackPiece);
								legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, attackedPieces));

								final Move checkMove = new AdditionalAttackMove(board,
																				new AttackKingDama(this.piecePosition,
																								   this.pieceAlliance,
																								   attackedPieces,
																								   this.getMoveCoordinates(candidateDestinationCoordinate)),
																				candidateDestinationCoordinate, attackedPieces);
								final AttackKingDama checkAttackKingDama = new AttackKingDama(candidateDestinationCoordinate,
																							  this.pieceAlliance, attackedPieces,
																							  this.getMoveCoordinates(candidateCoordinateOffset));
								
								legalAttackMoves.addAll(checkAttackKingDama.calculateLegalMoves(checkMove.execute()));
							}
						}
					}
					while(!legalAttackMoves.isEmpty()) {
						final List<Move> addLegalAttackMoves = new ArrayList<>();
						addLegalAttackMoves.addAll(legalAttackMoves);
						legalAttackMoves.clear();
						for(final Move move : addLegalAttackMoves) {
							legalMoves.add(new AttackMove(board, this, move.getDestinationCoordinate(), move.getAttackedPieces()));
							int[] moveCoordinates = this.getMoveCoordinates(move.getDestinationCoordinate() - move.getCurrentCoordinate());
							AttackKingDama attackKingDama = new AttackKingDama(move.getDestinationCoordinate(), this.pieceAlliance, move.getAttackedPieces(), moveCoordinates);
							legalAttackMoves.addAll(attackKingDama.calculateLegalMoves(move.execute()));
						}
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public KingDama movePiece(Move move) {
		return new KingDama(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
	}

	@Override
	public String toString() {
		return Piece.PieceType.KINGDAMA.toString();
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN.get(currentPosition) && (candidateOffset == -9 || candidateOffset == 7);
	}

	private static boolean isLastColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.LAST_COLUMN.get(currentPosition) && (candidateOffset == -7 || candidateOffset == 9);
	}

	private boolean hasPreviousAttackMove(final List<Move> legalMoves, final int destinationCoordinate) {
		for(final Move move : legalMoves) {
			if(move.getDestinationCoordinate() == destinationCoordinate && move instanceof AttackMove) return true;
		}
		return false;
	}

	private int[] getMoveCoordinates(final int coordinateOffset) {
		return (Math.abs(coordinateOffset) == 7) ? new int[] {-9, 9} : new int[] {-7, 7};
	}
}
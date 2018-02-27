package dama.model.pieces;

import dama.model.board.Move;
import dama.model.board.Move.*;
import dama.model.board.Board;
import dama.model.board.BoardUtils;
import dama.model.board.Tile;
import dama.model.Alliance;

import com.google.common.collect.ImmutableList;

import java.util.*;

public class AttackKingDama extends Piece {

	private List<Piece> attackedPieces;
	private int[] candidateMoveCoordinates;

	public AttackKingDama(final int piecePosition,
						  final Alliance pieceAlliance,
						  final List<Piece> attackedPieces,
						  final int[] candidateMoveCoordinates) {
		super(PieceType.KINGDAMA, piecePosition, pieceAlliance);
		this.attackedPieces = new ArrayList<>();
		this.attackedPieces.addAll(attackedPieces);
		this.candidateMoveCoordinates = candidateMoveCoordinates;
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for(final int candidateCoordinateOffset : this.candidateMoveCoordinates) {
			
			int candidateDestinationCoordinate = this.piecePosition;

			final List<Piece> addedPieces = new ArrayList<>();
			addedPieces.addAll(this.attackedPieces);

			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
				   isLastColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) break;

				candidateDestinationCoordinate += candidateCoordinateOffset;

				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

					if(!candidateDestinationTile.isTileOccupied()) {
						if(hasPreviousAttackMove(legalMoves, (candidateDestinationCoordinate - candidateCoordinateOffset))) {
							legalMoves.add(new AdditionalAttackMove(board, this, candidateDestinationCoordinate, addedPieces));
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
								addedPieces.add(candidateAttackPiece);
								legalMoves.add(new AdditionalAttackMove(board, this, candidateDestinationCoordinate, addedPieces));
							}
						}
					}
				}
			}
		}
		
		return ImmutableList.copyOf(legalMoves);

	}

	@Override
	public AttackKingDama movePiece(Move move) {
		int[] moveCoordinates = this.getMoveCoordinates(move.getDestinationCoordinate() - move.getCurrentCoordinate());
		return new AttackKingDama(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), move.getAttackedPieces(), moveCoordinates);
	}

	@Override
	public String toString() {
		return "Attack " + Piece.PieceType.KINGDAMA.toString();
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN.get(currentPosition) && (candidateOffset == -9 || candidateOffset == 7);
	}

	private static boolean isLastColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.LAST_COLUMN.get(currentPosition) && (candidateOffset == -7 || candidateOffset == 9);
	}

	private boolean hasPreviousAttackMove(final List<Move> legalMoves, final int destinationCoordinate) {
		for(final Move move : legalMoves) {
			if(move.getDestinationCoordinate() == destinationCoordinate && move instanceof AdditionalAttackMove) return true;
		}
		return false;
	}

	private int[] getMoveCoordinates(final int coordinateOffset) {
		return (Math.abs(coordinateOffset) == 7) ? new int[] {-9, 9} : new int[] {-7, 7};
	}
}
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

	private List<Piece> attackedPieces;

	private static final int[] CANDIDATE_MOVE_COORDINATES = { -9, -7, 7, 9 };

	public KingDama(final int piecePosition,
					final Alliance pieceAlliance) {
		super(PieceType.KINGDAMA, piecePosition, pieceAlliance);
		this.attackedPieces = new ArrayList<>();
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {

		final List<Move> legalMoves = new ArrayList<>();

		for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
			
			int candidateDestinationCoordinate = this.piecePosition;

			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
				   isLastColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) break;

				candidateDestinationCoordinate += candidateCoordinateOffset;
				//TODO: refactor for all valid moves 
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

					if(!candidateDestinationTile.isTileOccupied()) {
						if(!hasPreviousAttackMove(legalMoves, (candidateDestinationCoordinate - candidateCoordinateOffset))) {
							legalMoves.add(new NormalMove(board, this, candidateDestinationCoordinate));
						} else {
							legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, this.attackedPieces));
						}
					} else {
						// if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) break;
						if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
				   		   isLastColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) break;
						final Piece candidateAttackPiece = candidateDestinationTile.getPiece();
						final Alliance pieceAlliance = candidateAttackPiece.getPieceAlliance();
						candidateDestinationCoordinate += candidateCoordinateOffset;

						if(this.pieceAlliance != pieceAlliance &&
						   BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
							final Tile candidateAttackDestinationTile = board.getTile(candidateDestinationCoordinate);
							if(!BoardUtils.isTileOnTheEdge(candidateAttackPiece.getPiecePosition()) &&
						   	   !candidateAttackDestinationTile.isTileOccupied()) {
								this.attackedPieces.add(candidateAttackPiece);
								legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, this.attackedPieces));
							}
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
}
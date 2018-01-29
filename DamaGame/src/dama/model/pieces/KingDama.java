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

			if(!(isFirstColumnExclusion(this.piecePosition, candidateCoordinateOffset) &&
				isLastColumnExclusion(this.piecePosition, candidateCoordinateOffset))) continue;
			
			int candidateDestinationCoordinate = this.piecePosition;

			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				candidateDestinationCoordinate += candidateCoordinateOffset;
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

					if(!candidateDestinationTile.isTileOccupied()) {
						legalMoves.add(new NormalMove(board, this, candidateDestinationCoordinate));
					} else {
						final Piece pieceAtDestination = candidateDestinationTile.getPiece();
						final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

						if(this.pieceAlliance != pieceAlliance) {
							List<Piece> candidateAttackedPieces = new ArrayList<>();
							candidateAttackedPieces.add(pieceAtDestination);
							legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, candidateAttackedPieces));
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
}
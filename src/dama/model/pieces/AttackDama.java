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

public class AttackDama extends Piece {

	private static final int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};
	private List<Piece> attackedPieces;

	public AttackDama(final int piecePosition,
				final Alliance pieceAlliance,
				final List<Piece> attackedPieces) {
		super(PieceType.DAMA, piecePosition, pieceAlliance);
		this.attackedPieces = new ArrayList<>();
		this.attackedPieces.addAll(attackedPieces);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();

		for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {

			if(!(isFirstColumnExclusion(this.piecePosition, candidateCoordinateOffset) &&
				isLastColumnExclusion(this.piecePosition, candidateCoordinateOffset))) continue;

			final int candidateCoordinate = this.piecePosition + (this.pieceAlliance.getDirections() * candidateCoordinateOffset);
			final Tile candidateDestinationTile = board.getTile(candidateCoordinate);
			if(BoardUtils.isValidTileCoordinate(candidateCoordinate) && candidateDestinationTile.isTileOccupied()) {
				final Piece candidateAttackPiece = candidateDestinationTile.getPiece();
				final Alliance alliance = candidateAttackPiece.getPieceAlliance();
				final int attackCandidateDestinationCoordinate = candidateCoordinate +
																 (this.pieceAlliance.getDirections() * candidateCoordinateOffset);
				final Tile candidateAttackDestinationTile = board.getTile(attackCandidateDestinationCoordinate);

				if(this.pieceAlliance != pieceAlliance && !BoardUtils.isTileOnTheEdge(candidateAttackPiece.getPiecePosition())) {
					if(BoardUtils.isValidTileCoordinate(attackCandidateDestinationCoordinate) &&
					   !candidateAttackDestinationTile.isTileOccupied()) {
					   	this.attackedPieces.add(candidateAttackPiece);
						legalMoves.add(new AdditionalAttackMove(board, this, attackCandidateDestinationCoordinate, this.attackedPieces));
					}
				}
			}
		}

		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public AttackDama movePiece(Move move) {
		return new AttackDama(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), move.getAttackedPieces());
	}

	@Override
	public String toString() {
		return Piece.PieceType.DAMA.toString();
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN.get(currentPosition) && (candidateOffset == -9 || candidateOffset == 7);
	}

	private static boolean isLastColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.LAST_COLUMN.get(currentPosition) && (candidateOffset == -7 || candidateOffset == 9);
	}
}
package dama.view;

import dama.model.board.Board;
import dama.model.board.BoardUtils;
import dama.model.board.Tile;
import dama.model.board.Move;
import dama.model.pieces.Piece;
import dama.model.pieces.Dama;
import dama.model.pieces.KingDama;
import dama.controller.board.TileEventHandler;
import dama.view.GameBoard.BoardPane;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.Cursor;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;


public class TileIcon extends Pane {

	private final int tileId;
	private BoardPane boardPane;
	private Collection<Move> selectedPieceLegalMoves;
	private boolean isTileSelected;

	private final static int TILE_SIZE = 90;

	public TileIcon(final BoardPane boardPane,
					final int tileId) {
		this.boardPane = boardPane;
		this.tileId = tileId;
		this.isTileSelected = false;
		this.selectedPieceLegalMoves = new ArrayList<>();
		this.setPrefSize(TILE_SIZE, TILE_SIZE);
		this.setTileColor(this.boardPane.getBoard());
		this.setTileEmpty();
		this.setTilePiece(this.boardPane.getBoard());
		this.setOnMouseClicked(new TileEventHandler(this));
	}

	private final void setTileColor(final Board board) {
		final boolean light = ((this.tileId / BoardUtils.NUM_TILES_PER_ROW) + (this.tileId % BoardUtils.NUM_TILES_PER_ROW)) % 2 == 0;
		if(this.isTileSelected && this.isTileHasMoves(board)) {
			this.setStyle("-fx-background-color: #9F0000;");
		} else {
			this.setStyle(light ? "-fx-background-color: #FEB;" : "-fx-background-color: #582;");
		}
	}

	private void setTilePiece(final Board board) {
		if(board.getTile(this.tileId).isTileOccupied()) {
			this.setCursor(Cursor.HAND);
			if(board.getTile(this.tileId).getPiece().getPieceType().isKingDama()) {
				PieceIcon pieceIcon = new PieceIcon(board.getTile(this.tileId).getPiece().getPieceAlliance(),
													this.isTileHasMoves(board) && !this.isTileSelected);
				CrownIcon crownIcon = new CrownIcon();
				this.getChildren().addAll(pieceIcon, crownIcon);
			} else {
				this.getChildren().add(new PieceIcon(board.getTile(this.tileId).getPiece().getPieceAlliance(),
													 this.isTileHasMoves(board) && !this.isTileSelected));
			}
		}
	}

	private void highlightLegalMoves(final Board board) {
		for(final Move move : this.pieceLegalMoves(board)) {
			if(move.getDestinationCoordinate() == this.tileId) {
				this.selectedPieceLegalMoves.add(move);
				this.setCursor(Cursor.HAND);
				this.getChildren().add(new PieceHighlightIcon(this.boardPane.getSelectedPiece().getPieceAlliance()));
			}
		}
	}

	private Collection<Move> pieceLegalMoves(final Board board) {
		if(this.boardPane.getSelectedPiece() != null &&
		   this.boardPane.getSelectedPiece().getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
			return this.boardPane.getSelectedPiece().calculateLegalMoves(board);
		}
		return Collections.emptyList();
	}

	private boolean isTileHasMoves(final Board board) {
		boolean tileHasMoves = false;
		if(board.getTile(this.tileId).isTileOccupied()) {
			if(board.getCurrentPlayer().getAlliance() == board.getTile(this.tileId).getPiece().getPieceAlliance()) {
				tileHasMoves = !board.getTile(this.tileId).getPiece().calculateLegalMoves(board).isEmpty();
			}
		}
		return tileHasMoves;
	}

	private void setTileEmpty() {
		this.getChildren().clear();
		this.setCursor(Cursor.DEFAULT);
	}

	public void drawTile(final Board board) {
		this.isTileSelected = board.getTile(this.tileId).isTileOccupied() &&
						      board.getCurrentPlayer().getAlliance() == board.getTile(this.tileId).getPiece().getPieceAlliance() &&
						      this.boardPane.getSelectedPiece() != null &&
					   	      this.boardPane.getSelectedPiece().getPiecePosition() == this.tileId;
		this.setTileColor(board);
		this.setTileEmpty();
		this.setTilePiece(board);
		this.highlightLegalMoves(board);
	}

	public int getTileId() {
		return this.tileId;
	}

	public BoardPane getBoardPane() {
		return this.boardPane;
	}

	public Collection<Move> getSelectedPieceLegalMoves() {
		return this.selectedPieceLegalMoves;
	}
	
}
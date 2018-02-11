package dama.view;

import dama.model.board.Board;
import dama.model.board.BoardUtils;
import dama.model.board.Tile;
import dama.model.pieces.Piece;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends BorderPane {

	private final BoardPane boardPane;
	private Board damaBoard;

	public GameBoard() {
		this.boardPane = new BoardPane(Board.createStandardBoard());
		this.setCenter(this.boardPane);
	}

	public class BoardPane extends GridPane {

		private final List<TileIcon> boardTiles;
		
		private Board board;
		private Tile sourceTile;
		private Tile destinationTile;
		private Piece selectedPiece;

		BoardPane(final Board board) {
			this.board = board;
			this.boardTiles = new ArrayList<>();
			for(int i = 0; i < 64; i++) {
				final TileIcon tileIcon = new TileIcon(this, i);
				this.boardTiles.add(tileIcon);
				this.add(tileIcon, (i % 8), (i / 8));
			}
		}

		public void drawBoard(final Board board) {
			this.getChildren().clear();
			for(final TileIcon tileIcon : this.boardTiles) {
				tileIcon.drawTile(board);
				this.add(tileIcon, (tileIcon.getTileId() % 8), (tileIcon.getTileId() / 8));
			}
		}

		public Board getBoard() {
			return this.board;
		}

		public Tile getSourceTile() {
			return this.sourceTile;
		}

		public Tile getDestinationTile() {
			return this.destinationTile;
		}

		public Piece getSelectedPiece() {
			return this.selectedPiece;
		}

		public void setBoard(final Board board) {
			this.board = board;
		}

		public void setSourceTile(final Tile sourceTile) {
			this.sourceTile = sourceTile;
		}

		public void setDestinationTile(final Tile destinationTile) {
			this.destinationTile = destinationTile;
		}

		public void setSelectedPiece(final Piece selectedPiece) {
			this.selectedPiece = selectedPiece;
		}
	}
}
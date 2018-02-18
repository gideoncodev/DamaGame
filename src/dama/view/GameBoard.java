package dama.view;

import dama.model.Alliance;
import dama.model.board.Board;
import dama.model.board.BoardUtils;
import dama.model.board.Move;
import dama.model.board.Tile;
import dama.model.pieces.Piece;
import dama.model.player.ai.MiniMax;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends BorderPane {

	private final BoardPane boardPane;
	private final PlayerType whitePlayerType = PlayerType.HUMAN;
	private final PlayerType blackPlayerType = PlayerType.COMPUTER;

	private ObjectProperty<Board> boardProperty = new SimpleObjectProperty<>();
	private Board damaBoard;

	private static final GameBoard INSTANCE = new GameBoard();

	private GameBoard() {
		this.boardPane = new BoardPane(Board.createStandardBoard());
		this.boardProperty.addListener(new ChangeListener<Board>() {
			@Override
			public void changed(final ObservableValue ov,
								final Board oldValue,
								final Board newValue) {
				final AIThink aiThink = new AIThink();
				Thread th = new Thread(aiThink);
				th.setDaemon(true);
				th.start();
			}
		});
		this.setCenter(this.boardPane);
	}

	public static GameBoard get() {
		return INSTANCE;
	}

	public BoardPane getBoardPane() {
		return this.boardPane;
	}

	public ObjectProperty<Board> getBoardProperty() {
		return this.boardProperty;
	}

	public boolean isAIPlayer(final Alliance alliance) {
		return alliance.isBlack() ? blackPlayerType.isComputer() : whitePlayerType.isComputer();
	}

	public enum PlayerType {
		HUMAN("Human") {
			@Override
			public boolean isComputer() {
				return false;
			}
			@Override
			public boolean isHuman() {
				return true;
			}
		},
		COMPUTER("Computer") {
			@Override
			public boolean isComputer() {
				return true;
			}
			@Override
			public boolean isHuman() {
				return false;
			}
		};

		private final String playerType;

		PlayerType(final String playerType) {
			this.playerType = playerType;
		}

		@Override
		public String toString() {
			return this.playerType;
		}

		public abstract boolean isComputer();
		public abstract boolean isHuman();
	}

	private static class AIThink extends Task<Move> {
		@Override
		protected Move call() throws Exception {
			final MiniMax miniMax = new MiniMax(4);
			final Move bestMove = miniMax.execute(GameBoard.get().getBoardPane().getBoard());
			return bestMove;
		}

		@Override
		protected void succeeded() {
			final Move bestMove = getValue();
			GameBoard.get().getBoardPane().setBoard(GameBoard.get().getBoardPane().getBoard().getCurrentPlayer().makeMove(bestMove).getTransitionBoard());
			GameBoard.get().getBoardPane().drawBoard(GameBoard.get().getBoardPane().getBoard());
		}
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
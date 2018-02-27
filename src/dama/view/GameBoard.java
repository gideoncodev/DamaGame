package dama.view;

import dama.model.Alliance;
import dama.model.board.Board;
import dama.model.board.BoardUtils;
import dama.model.board.Move;
import dama.model.board.Tile;
import dama.model.pieces.Piece;
import dama.model.player.ai.MiniMax;
import dama.model.player.ai.AlphaBetaPruning;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.Service;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Cursor;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public class GameBoard extends BorderPane {

	private final BoardPane boardPane;
	private final PlayerType whitePlayerType = PlayerType.HUMAN;
	private final PlayerType blackPlayerType = PlayerType.COMPUTER;

	private ObjectProperty<Board> boardProperty = new SimpleObjectProperty<>();

	private static final GameBoard INSTANCE = new GameBoard();

	private GameBoard() {
		this.boardPane = new BoardPane(Board.createStandardBoard());
		this.boardProperty.addListener(new ChangeListener<Board>() {
			@Override
			public void changed(final ObservableValue ov,
								final Board oldBoard,
								final Board newBoard) {
				if(GameBoard.get().getBoardProperty().getValue().getCurrentPlayer().isGameOver()) {
					final Alert gameOverAlert = new Alert(AlertType.CONFIRMATION, "Would you like to start a New Game?", ButtonType.YES, ButtonType.NO);
					gameOverAlert.setTitle("GAME OVER!");
					gameOverAlert.setHeaderText(newBoard.getCurrentPlayer().getOpponent() + " WINS!!!");
					gameOverAlert.initStyle(StageStyle.UNDECORATED);
					Optional<ButtonType> result = gameOverAlert.showAndWait();
					if(result.get() == ButtonType.YES) {

					} else {
						Platform.exit();
					}
				}

				if(GameBoard.get().isAIPlayer(newBoard.getCurrentPlayer().getAlliance())) {					
					final Thread th = new Thread(new AIThink(newBoard));
					th.setDaemon(true);
					th.start();
				}
			}
		});
		this.setTop(new DamaMenu());
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

	protected enum PlayerType {
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
		private Board board;

		AIThink(final Board board) {
			this.board = board;
		}

		@Override
		protected Move call() throws Exception {
			final AlphaBetaPruning alphaBetaPruning = new AlphaBetaPruning(4);
			final Move bestMove = alphaBetaPruning.execute(this.board);
			return bestMove;
		}

		@Override
		protected void succeeded() {
			final Move bestMove = getValue();
			GameBoard.get().getBoardPane().setBoard(this.board.getCurrentPlayer().makeMove(bestMove).getTransitionBoard());
			GameBoard.get().getBoardPane().drawBoard(GameBoard.get().getBoardPane().getBoard());
			if(GameBoard.get().getBoardPane().getBoard().getCurrentPlayer().isGameOver()) {
				final Alert gameOverAlert = new Alert(AlertType.CONFIRMATION, "Would you like to start a New Game?", ButtonType.YES, ButtonType.NO);
				gameOverAlert.setTitle("GAME OVER!");
				gameOverAlert.setHeaderText(GameBoard.get().getBoardPane().getBoard().getCurrentPlayer().getOpponent() + " WINS!!!");
				gameOverAlert.initStyle(StageStyle.UNDECORATED);
				Optional<ButtonType> result = gameOverAlert.showAndWait();
				if(result.get() == ButtonType.YES) {

				} else {
					Platform.exit();
				}
			}
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
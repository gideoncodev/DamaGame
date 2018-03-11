package dama.view.board;

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
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
	private final TakenPiecesPane whiteTakenPiecesPane;
	private final TakenPiecesPane blackTakenPiecesPane;
	private final MoveLog moveLog;
	private final PlayerType whitePlayerType = PlayerType.HUMAN;
	private final PlayerType blackPlayerType = PlayerType.COMPUTER;

	private ObjectProperty<Board> boardProperty = new SimpleObjectProperty<>();

	private final static GameBoard INSTANCE = new GameBoard();

	private GameBoard() {
		this.boardPane = new BoardPane(Board.createStandardBoard());
		this.whiteTakenPiecesPane = new TakenPiecesPane(Alliance.WHITE);
		this.blackTakenPiecesPane = new TakenPiecesPane(Alliance.BLACK);
		this.whiteTakenPiecesPane.getPlayerProfile().update(this.boardPane.getBoard());
		this.blackTakenPiecesPane.getPlayerProfile().update(this.boardPane.getBoard());
		this.boardProperty.addListener(new ChangeListener<Board>() {
			@Override
			public void changed(final ObservableValue ov,
								final Board oldBoard,
								final Board newBoard) {
				if(GameBoard.get().getBoardProperty().getValue().getCurrentPlayer().isGameOver() || 
			   	   GameBoard.get().getBoardPane().getBoard().getCurrentPlayer().getLegalMoves().isEmpty()) {
					createGameOverAlert();
				}

				if(GameBoard.get().isAIPlayer(newBoard.getCurrentPlayer().getAlliance())) {					
					final Thread th = new Thread(new AIThink(newBoard));
					th.setDaemon(true);
					th.start();
				}
			}
		});
		this.moveLog = new MoveLog();
		this.setCenter(InitialGame.get());
	}

	public static GameBoard get() {
		return INSTANCE;
	}

	public BoardPane getBoardPane() {
		return this.boardPane;
	}

	public TakenPiecesPane getWhiteTakenPiecesPane() {
		return this.whiteTakenPiecesPane;
	}

	public TakenPiecesPane getBlackTakenPiecesPane() {
		return this.blackTakenPiecesPane;
	}

	public ObjectProperty<Board> getBoardProperty() {
		return this.boardProperty;
	}

	public MoveLog getMoveLog() {
		return this.moveLog;
	}
	public void updateBoard() {
		this.setTop(new DamaMenu());
		this.setLeft(this.blackTakenPiecesPane);
		this.setRight(this.whiteTakenPiecesPane);
		this.setCenter(this.boardPane);
		this.getScene().getWindow().sizeToScene();
	}

	public void newGameBoard() {
		GameBoard.get().getBoardPane().setBoard(Board.createStandardBoard());
		GameBoard.get().getMoveLog().clear();
		GameBoard.get().getBoardPane().updateComputerMove(null);
		GameBoard.get().getBoardPane().setSourceTile(null);
		GameBoard.get().getBoardPane().setDestinationTile(null);
		GameBoard.get().getBoardPane().setSelectedPiece(null);
		GameBoard.get().getWhiteTakenPiecesPane().getPlayerProfile().update(GameBoard.get().getBoardPane().getBoard());
		GameBoard.get().getBlackTakenPiecesPane().getPlayerProfile().update(GameBoard.get().getBoardPane().getBoard());
		GameBoard.get().getWhiteTakenPiecesPane().getTakenPieces().draw(GameBoard.get().getMoveLog().getAttackedPieces());
		GameBoard.get().getBlackTakenPiecesPane().getTakenPieces().draw(GameBoard.get().getMoveLog().getAttackedPieces());
		GameBoard.get().getBoardPane().drawBoard(GameBoard.get().getBoardPane().getBoard());
	}

	public boolean isAIPlayer(final Alliance alliance) {
		return alliance.isBlack() ? blackPlayerType.isComputer() : whitePlayerType.isComputer();
	}

	public boolean hasAIPlayer() {
		return blackPlayerType.isComputer() || whitePlayerType.isComputer();
	}

	private static void createGameOverAlert() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				new GameOverStage();
			}
		});
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
			System.out.println(bestMove);
			System.out.println(GameBoard.get().getBoardPane().getBoard());
			GameBoard.get().getMoveLog().addUndoMoves(bestMove, GameBoard.get().getBoardPane().getBoard());
			GameBoard.get().getBoardPane().updateComputerMove(bestMove);
			GameBoard.get().getBoardPane().setBoard(this.board.getCurrentPlayer().makeMove(bestMove).getTransitionBoard());

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					GameBoard.get().getWhiteTakenPiecesPane().getPlayerProfile().update(GameBoard.get().getBoardPane().getBoard());
					GameBoard.get().getBlackTakenPiecesPane().getPlayerProfile().update(GameBoard.get().getBoardPane().getBoard());
					GameBoard.get().getWhiteTakenPiecesPane().getTakenPieces().draw(GameBoard.get().getMoveLog().getAttackedPieces());
					GameBoard.get().getBlackTakenPiecesPane().getTakenPieces().draw(GameBoard.get().getMoveLog().getAttackedPieces());
					GameBoard.get().getBoardPane().drawBoard(GameBoard.get().getBoardPane().getBoard());
					if(GameBoard.get().getBoardProperty().getValue().getCurrentPlayer().isGameOver() || 
				   	   GameBoard.get().getBoardPane().getBoard().getCurrentPlayer().getLegalMoves().isEmpty()) {
						createGameOverAlert();
					}
				}
			});
		}
	}

	public class BoardPane extends GridPane {

		private final List<TileIcon> boardTiles;
		
		private Board board;
		private Tile sourceTile;
		private Tile destinationTile;
		private Piece selectedPiece;
		private Move computerMove;

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

		public Move getComputerMove() {
			return this.computerMove;
		}

		public void updateComputerMove(final Move move) {
			this.computerMove = move;
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

	private static class InitialGame extends Pane {

		private final static Rectangle2D SCREEN_BOUNDS = Screen.getPrimary().getVisualBounds();
		private final static int TILE_SIZE = (int) (SCREEN_BOUNDS.getHeight() - 100) / 8;
		private final static int INIT_WIDTH = TILE_SIZE * 12;
		private final static int INIT_HEIGHT = TILE_SIZE * 8;

		private final static InitialGame INSTANCE = new InitialGame();

		private InitialGame() {
			this.setPrefSize(INIT_WIDTH, INIT_HEIGHT);
			this.setStyle("-fx-background-color: #373739;");
			this.addLabels();
		}

		public static InitialGame get() {
			return INSTANCE;
		}

		private void addLabels() {
			final Label damaLabel = new Label("DAMA");
			damaLabel.setTextFill(Color.valueOf("#373739"));
			damaLabel.setStyle("-fx-font-size: 100; -fx-font-family: Georgia; -fx-font-weight: bolder;");
			final Label addedLabel = new Label("Player VS Computer");
			addedLabel.setTextFill(Color.valueOf("#373739"));
			addedLabel.setStyle("-fx-font-size: 50; -fx-font-family: Georgia; -fx-font-weight: bold; -fx-font-style: italic;");
			final Button playButton = new Button("Play");
			playButton.setTextFill(Color.valueOf("#373739"));
			playButton.setCursor(Cursor.HAND);
			playButton.setOnMouseEntered(e -> {
				playButton.setTextFill(Color.valueOf("#72716B"));
				playButton.setStyle("-fx-font-size: 40; -fx-font-family: Georgia; -fx-background-color: #C4F2F4; -fx-border-color: #C4F2F4; -fx-border-width: 5; -fx-border-radius: 50; -fx-background-radius: 50;");
			});
			playButton.setOnMouseExited(e -> {
				playButton.setTextFill(Color.valueOf("#C4F2F4"));
				playButton.setStyle("-fx-font-size: 40; -fx-font-family: Georgia; -fx-background-color: #373739; -fx-border-color: #C4F2F4; -fx-border-width: 5; -fx-border-radius: 50; -fx-background-radius: 50;");
			});
			playButton.setOnMouseClicked(e -> {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						GameBoard.get().updateBoard();
					}
				});
			});
			this.getChildren().addAll(damaLabel, addedLabel, playButton);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					int damaLabelY = (INIT_HEIGHT / 2) - 200;
					int damaLabelX = (INIT_WIDTH / 2) - ((int) damaLabel.getWidth() / 2);
					damaLabel.setTextFill(Color.valueOf("#C8EC31"));
					damaLabel.relocate(damaLabelX, damaLabelY);
					int addedLabelY = (INIT_HEIGHT / 2) - 50;
					int addedLabelX = (INIT_WIDTH / 2) - ((int) addedLabel.getWidth() / 2);
					addedLabel.setTextFill(Color.valueOf("#EBE7DB"));
					addedLabel.relocate(addedLabelX, addedLabelY);
					int playButtonY = (INIT_HEIGHT / 2) + 50;
					int playButtonX = (INIT_WIDTH / 2) - (int) (playButton.getWidth() * 1.5);
					playButton.setTextFill(Color.valueOf("#C4F2F4"));
					playButton.setStyle("-fx-font-size: 40; -fx-font-family: Georgia; -fx-background-color: #373739; -fx-border-color: #C4F2F4; -fx-border-width: 5; -fx-border-radius: 50; -fx-background-radius: 50;");
					playButton.relocate(playButtonX, playButtonY);
				}
			});
		}

	}
}
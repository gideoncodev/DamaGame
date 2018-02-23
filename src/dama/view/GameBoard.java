package dama.view;

import dama.model.Alliance;
import dama.model.board.Board;
import dama.model.board.BoardUtils;
import dama.model.board.Move;
import dama.model.board.Tile;
import dama.model.pieces.Piece;
import dama.model.player.ai.MiniMax;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.Service;
import javafx.stage.Popup;
import javafx.stage.Stage;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GameBoard extends BorderPane {

	private final BoardPane boardPane;
	private final PlayerType whitePlayerType = PlayerType.HUMAN;
	private final PlayerType blackPlayerType = PlayerType.COMPUTER;
	private final Map<Board, Move> blackAIBestMoves = new HashMap<>();
	private final Map<Board, Move> whiteAIBestMoves = new HashMap<>();
	private final ObjectProperty<Board> blackBoardAIUpdate = new SimpleObjectProperty<>();

	private ObjectProperty<Board> boardProperty = new SimpleObjectProperty<>();
	private boolean loadMoves = false;
	private int numThreads = 0;

	private static final GameBoard INSTANCE = new GameBoard();
	private static final int MAX_THREAD = 20;

	private GameBoard() {
		this.boardPane = new BoardPane(Board.createStandardBoard());
		if(this.hasAIPlayer()) {
			if(this.whitePlayerType.isComputer()) {
				this.startWhitePlayerAIService();
			} else {
				this.startBlackPlayerAIService();
			}
		}
		this.blackBoardAIUpdate.addListener(new ChangeListener<Board>() {
			@Override
			public void changed(final ObservableValue ov,
								final Board oldBoard,
								final Board newBoard) {
				if(GameBoard.get().getNumThreads() < MAX_THREAD) {
					GameBoard.get().addNumThreads();
					final Thread th = new Thread(new AIThink(newBoard));
					th.setDaemon(true);
					th.start();
				}
			}
		});
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
					gameOverAlert.showAndWait();
				}

				if(GameBoard.get().isAIPlayer(newBoard.getCurrentPlayer().getAlliance())) {
					final Move bestMove = GameBoard.get().getBlackAIBestMoves().get(newBoard);
					System.out.println(bestMove);
					System.out.println(bestMove.getCurrentCoordinate());
					System.out.println(bestMove.getDestinationCoordinate());
					GameBoard.get().getBoardPane().setBoard(newBoard.getCurrentPlayer().makeMove(bestMove).getTransitionBoard());
					GameBoard.get().getBoardPane().drawBoard(GameBoard.get().getBoardPane().getBoard());
				}



				// final AIThink aiThink = new AIThink();
				// Thread th = new Thread(aiThink);
				// th.setDaemon(true);
				// th.start();
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

	public ObjectProperty<Board> getBlackBoardAIUpdate() {
		return this.blackBoardAIUpdate;
	}

	public Map<Board, Move> getBlackAIBestMoves() {
		return this.blackAIBestMoves;
	}

	public int getNumThreads() {
		return this.numThreads;
	}

	public boolean isAIPlayer(final Alliance alliance) {
		return alliance.isBlack() ? blackPlayerType.isComputer() : whitePlayerType.isComputer();
	}

	public boolean getLoadMoves() {
		return this.loadMoves;
	}

	public void setLoadMoves(final boolean loadMoves){
		this.loadMoves = loadMoves;
	}

	public void addNumThreads() {
		this.numThreads++;
	}

	public void removeNumThreads() {
		this.numThreads--;
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

	private boolean hasAIPlayer() {
		return whitePlayerType.isComputer() || blackPlayerType.isComputer();
	}

	private void startBlackPlayerAIService() {
		final List<Board> checkBoards = new ArrayList<>();

		//Loading stage
		final Stage loadingStage = new Stage();
		loadingStage.initModality(Modality.APPLICATION_MODAL);
		loadingStage.initStyle(StageStyle.UNDECORATED);


	    final HBox loadingLayout = new HBox(10);

	    loadingLayout.setStyle("-fx-background-color: GREEN; -fx-padding: 10");
	    loadingLayout.setCursor(Cursor.WAIT);

	    final Label loadingLabel = new Label("Waiting for AI Moves ... ");
	    loadingLabel.setStyle("-fx-font-size: 30px; -fx-padding: 20; -fx-text-fill: WHITE; -fx-background-color: GREEN;");
	    loadingLayout.getChildren().add(loadingLabel);

	    loadingStage.setScene(new Scene(loadingLayout));

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				loadingStage.show();
			}
		});

		new Service<List<Board>>() {
			@Override
			protected void succeeded() {
				if(!GameBoard.get().getLoadMoves()) {
					GameBoard.get().setLoadMoves(true);
					// checkBoards.clear();
					// checkBoards.addAll(getValue());
					// this.restart();	
				}

				if(GameBoard.get().getBlackAIBestMoves().size() > 1000) {
					loadingStage.hide();
				}

				checkBoards.clear();
				checkBoards.addAll(getValue());
				this.restart();				
			}

			@Override
			protected Task<List<Board>> createTask() {
				return new Task<List<Board>>() {
					@Override
					protected List<Board> call() throws Exception {
						final List<Board> boards = new ArrayList<>();
						if(!GameBoard.get().getLoadMoves()) {
							final Board aiBoard = Board.createStandardBoard();
							for(final Move legalMove : aiBoard.getCurrentPlayer().getLegalMoves()) {
								final Board transitionBoard = aiBoard.getCurrentPlayer().makeMove(legalMove).getTransitionBoard();
								GameBoard.get().getBlackAIBestMoves().put(transitionBoard, null);
								System.out.println(transitionBoard);
								GameBoard.get().getBlackBoardAIUpdate().setValue(transitionBoard);
								boards.add(transitionBoard);
							}
						} else {
							for(final Board board : checkBoards) {
								if(GameBoard.get().getBlackAIBestMoves().containsKey(board)){
									if(GameBoard.get().getBlackAIBestMoves().get(board) != null) {
										final Move move = GameBoard.get().getBlackAIBestMoves().get(board);
										final Board aiBoard = board.getCurrentPlayer().makeMove(move).getTransitionBoard();
										System.out.println("AI Board");
										System.out.println(aiBoard);
										for(final Move legalMove : aiBoard.getCurrentPlayer().getLegalMoves()) {
											final Board transitionBoard = aiBoard.getCurrentPlayer().makeMove(legalMove).getTransitionBoard();
											System.out.println("Transition Board");
											System.out.println(transitionBoard);
											GameBoard.get().getBlackAIBestMoves().put(transitionBoard, null);
											GameBoard.get().getBlackBoardAIUpdate().setValue(transitionBoard);
											boards.add(transitionBoard);
										}
									} else {
										boards.add(board);
									}
								}
							}
						}
						return boards;
					}
				};
			}

		}.start();
	}

	private void startWhitePlayerAIService() {
		Board aiBoard = Board.createStandardBoard();

		new Service<Move>() {
			@Override
			protected void succeeded() {
				System.out.println(getValue());
			}

			@Override
			protected Task<Move> createTask() {
				return new Task<Move>() {
					@Override
					protected Move call() throws Exception {

						return null;
					}
				};
			}

		}.start();
	}

	private static class AIThink extends Task<Move> {
		private Board board;

		AIThink(final Board board) {
			this.board = board;
		}

		@Override
		protected Move call() throws Exception {
			final MiniMax miniMax = new MiniMax(4);
			final Move bestMove = miniMax.execute(this.board);
			return bestMove;
		}

		@Override
		protected void succeeded() {
			GameBoard.get().getBlackAIBestMoves().put(this.board, getValue());
			GameBoard.get().removeNumThreads();
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
package dama.view;

import dama.controller.menu.FileMenuListener.*;
import dama.controller.menu.PreferencesMenuListener.*;
import dama.controller.board.TileListener;
import dama.model.board.BoardUtils;
import dama.model.board.Board;
import dama.model.board.Move;
import dama.model.board.Tile;
import dama.model.pieces.Piece;
import dama.model.Alliance;

import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;

public class GameBoard {

	private final JFrame gameFrame;
	private final TakenPiecesPanel takenPiecesPanel;
	private final BoardPanel boardPanel;
	private final MoveLog moveLog;

	private Board damaBoard;

	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	private BoardDirection boardDirection;

	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(700,600);
	private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
	private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
	private final static String defaultPieceImagePath = "src/dama/view/images/";

	private final Color lightTileColor = Color.decode("#FFEEBB");
    private final Color darkTileColor = Color.decode("#558822");

	public GameBoard() {

		final JMenuBar damaMenuBar = this.populateMenuBar();

		this.gameFrame = new JFrame("Dama");
		this.gameFrame.setLayout(new BorderLayout());
		this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.gameFrame.setJMenuBar(damaMenuBar);

		this.damaBoard = Board.createStandardBoard();
		this.boardPanel = new BoardPanel();
		this.boardDirection = BoardDirection.NORMAL;
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.moveLog = new MoveLog();
		this.takenPiecesPanel = new TakenPiecesPanel();
		this.gameFrame.add(this.takenPiecesPanel, BorderLayout.EAST);

		this.gameFrame.pack();
		this.gameFrame.setPreferredSize(OUTER_FRAME_DIMENSION);
		this.gameFrame.setVisible(true);
	}

	public JFrame getGameFrame() {
		return this.gameFrame;
	}

	public BoardPanel getBoardPanel() {
		return this.boardPanel;
	}

	public Board getDamaBoard() {
		return this.damaBoard;
	}

	public BoardDirection getBoardDirection() {
		return this.boardDirection;
	}

	public Tile getSourceTile() {
		return this.sourceTile;
	}

	public Tile getDestinationTile() {
		return this.destinationTile;
	}

	public Piece getHumanMovedPiece() {
		return this.humanMovedPiece;
	}

	public TakenPiecesPanel getTakenPiecesPanel() {
		return this.takenPiecesPanel;
	}

	public MoveLog getMoveLog() {
		return this.moveLog;
	}

	public void setSourceTile(final Tile sourceTile) {
		this.sourceTile = sourceTile;
	}

	public void setDestinationTile(final Tile destinationTile) {
		this.destinationTile = destinationTile;
	}

	public void setHumanMovedPiece(final Piece humanMovedPiece) {
		this.humanMovedPiece = humanMovedPiece;
	}

	public void setBoardDirection(final BoardDirection boardDirection) {
		this.boardDirection = boardDirection;
	}

	public void setDamaBoard(final Board damaBoard) {
		this.damaBoard = damaBoard;
	}

	private JMenuBar populateMenuBar() {
		final JMenuBar damaMenuBar = new JMenuBar();
		damaMenuBar.add(this.createFileMenu());
		damaMenuBar.add(this.createPreferencesMenu());
		return damaMenuBar;
	}

	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openFile = new JMenuItem("Open");
		final JMenuItem exitMenu = new DamaMenuItem("Exit");

		openFile.addActionListener(new OpenListener());
		exitMenu.addActionListener(new ExitListener());

		fileMenu.add(openFile);
		fileMenu.addSeparator();
		fileMenu.add(exitMenu);

		return fileMenu;
	}

	private JMenu createPreferencesMenu() {
		final JMenu preferencesMenu = new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = new DamaMenuItem("Flip Board");
		flipBoardMenuItem.addActionListener(new FlipBoardListener());
		preferencesMenu.add(flipBoardMenuItem);
		return preferencesMenu;
	}

	public class DamaMenuItem extends JMenuItem {
		DamaMenuItem(final String name) {
			super(name);
		}

		public JFrame getParentFrame() {
			return GameBoard.this.getGameFrame();
		}

		public BoardPanel getParentBoardPanel() {
			return GameBoard.this.getBoardPanel();
		}

		public Board getParentDamaBoard() {
			return GameBoard.this.getDamaBoard();
		}

		public BoardDirection getParentBoardDirection() {
			return GameBoard.this.getBoardDirection();
		}
		public void setParentBoardDirection(final BoardDirection boardDirection) {
			GameBoard.this.setBoardDirection(boardDirection);
		}

	}

	public enum BoardDirection {
		NORMAL {
			@Override
			List<TilePanel> traverse(final List<TilePanel> boardTiles) {
				return boardTiles;
			}

			@Override
			public BoardDirection opposite() {
				return FLIPPED;
			}
		},
		FLIPPED {
			@Override
			List<TilePanel> traverse(final List<TilePanel> boardTiles) {
				return Lists.reverse(boardTiles);
			}

			@Override
			public BoardDirection opposite() {
				return NORMAL;
			}
		};

		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
		public abstract BoardDirection opposite();
	}

	public class BoardPanel extends JPanel {

		final List<TilePanel> boardTiles;

		BoardPanel(){
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();

			for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
				final TilePanel tilePanel = new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				this.add(tilePanel);
			}
			this.setPreferredSize(BOARD_PANEL_DIMENSION);
			this.validate();
		}

		public void drawBoard(final Board board) {
			this.removeAll();
			for(final TilePanel tilePanel : GameBoard.this.boardDirection.traverse(this.boardTiles)) {
				tilePanel.drawTile(board);
				this.add(tilePanel);
			}
			this.validate();
			this.repaint();
		}
	}

	public static class MoveLog {
		private final List<Move> moves;

		MoveLog() {
			this.moves = new ArrayList<>();
		}

		public List<Move> getMoves() {
			return this.moves;
		}

		public void addMove(final Move move) {
			this.moves.add(move);
		}

		public int size() {
			return this.moves.size();
		}

		public void clear() {
			this.moves.clear();
		}

		public Move removeMove(final int moveIndex) {
			return this.moves.remove(moveIndex);
		}
	}

	public class TilePanel extends JPanel {
		private final int tileId;
		private BoardPanel boardPanel;
		private Collection<Move> humanPieceLegalMoves;

		TilePanel(final BoardPanel boardPanel,
				  final int tileId) {
			super(new GridLayout(1,1));
			this.tileId = tileId;
			this.boardPanel = boardPanel;
			this.humanPieceLegalMoves = new ArrayList<>();
			this.setPreferredSize(TILE_PANEL_DIMENSION);
			this.assignTileColor();
			this.assignTilePieceIcon(damaBoard);
			this.highlightLegalMoves(damaBoard);
			this.addMouseListener(new TileListener());
			this.validate();
		}

		public final int getTileId() {
			return this.tileId;
		}

		public Collection<Move> getHumanPieceLegalMoves() {
			return this.humanPieceLegalMoves;
		}

		public Tile getFrameSourceTile() {
			return GameBoard.this.getSourceTile();
		}

		public Tile getFrameDestinationTile() {
			return GameBoard.this.getDestinationTile();
		}

		public Piece getFrameHumanMovedPiece() {
			return GameBoard.this.getHumanMovedPiece();
		}

		public void setFrameSourceTile(final Tile sourceTile) {
			GameBoard.this.setSourceTile(sourceTile);
		}

		public void setFrameDestinationTile(final Tile destinationTile) {
			GameBoard.this.setDestinationTile(destinationTile);
		}

		public void setFrameHumanMovedPiece(final Piece humanMovedPiece) {
			GameBoard.this.setHumanMovedPiece(humanMovedPiece);
		}

		public Board getFrameDamaBoard() {
			return GameBoard.this.getDamaBoard();
		}

		public BoardPanel getBoardPanel() {
			return this.boardPanel;
		}

		public TakenPiecesPanel getFrameTakenPiecesPanel() {
			return GameBoard.this.getTakenPiecesPanel();
		}

		public MoveLog getFrameMoveLog() {
			return GameBoard.this.getMoveLog();
		}

		public void setFrameDamaBoard(final Board board) {
			GameBoard.this.damaBoard = board;
		}

		public BoardDirection getFrameBoardDirection() {
			return GameBoard.this.getBoardDirection();
		}
		public void setFrameBoardDirection(final BoardDirection boardDirection) {
			GameBoard.this.setBoardDirection(boardDirection);
		}

		public void drawTile(final Board board) {
			this.assignTileColor();
			this.assignTilePieceIcon(board);
			this.highlightLegalMoves(board);
		}

		private void highlightLegalMoves(final Board board) {
			for(final Move move : pieceLegalMoves(board)) {
				if(move.getDestinationCoordinate() == this.tileId) {
					this.humanPieceLegalMoves.add(move);
					this.add(new DamaIconHighlightLegalMoves(this.getFrameHumanMovedPiece().getPieceAlliance()));
				}
			}
		}

		private Collection<Move> pieceLegalMoves(final Board board) {
			if(this.getFrameHumanMovedPiece() != null &&
			   this.getFrameHumanMovedPiece().getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
				return this.getFrameHumanMovedPiece().calculateLegalMoves(board);
			}
			return Collections.emptyList();
		}

		public void assignTilePieceIcon(final Board board) {
			this.removeAll();
			if(board.getTile(this.tileId).isTileOccupied()) {
				this.add(new DamaIconLabel(board.getTile(this.tileId).getPiece().getPieceAlliance()));
			}
		}

		private class DamaIconLabel extends JLabel {
			private Alliance alliance;

			DamaIconLabel(final Alliance alliance) {
				super();
				this.alliance = alliance;
			}

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Color pieceColor = this.alliance.isBlack() ? Color.decode("#470507") : Color.decode("#FFF9F4");
				g.setColor(pieceColor);
				g.fillOval(7, 7, this.getSize().width - 14, this.getSize().height - 14);
			}
		}

		private class DamaIconHighlightLegalMoves extends JLabel {
			private Alliance alliance;

			DamaIconHighlightLegalMoves(final Alliance alliance) {
				super();
				this.alliance = alliance;
			}

			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				Color pieceColor = this.alliance.isBlack() ? Color.decode("#470507") : Color.decode("#FFF9F4");
				g2.setColor(pieceColor);
				g2.setStroke(new BasicStroke(3));
				g2.drawOval(7, 7, this.getSize().width - 14, this.getSize().height - 14);
			}
		}

		public void assignTileColor() {
			 if (BoardUtils.FIRST_ROW.get(this.tileId) ||
                BoardUtils.THIRD_ROW.get(this.tileId) ||
                BoardUtils.FIFTH_ROW.get(this.tileId) ||
                BoardUtils.SEVENTH_ROW.get(this.tileId)) {
                this.setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if(BoardUtils.SECOND_ROW.get(this.tileId) ||
                      BoardUtils.FOURTH_ROW.get(this.tileId) ||
                      BoardUtils.SIXTH_ROW.get(this.tileId)  ||
                      BoardUtils.LAST_ROW.get(this.tileId)) {
                this.setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
		}
	}
}
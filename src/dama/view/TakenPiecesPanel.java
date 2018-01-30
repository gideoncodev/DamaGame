package dama.view;

import dama.view.GameBoard.MoveLog;
import dama.model.pieces.Piece;
import dama.model.board.Move;
import dama.model.Alliance;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class TakenPiecesPanel extends JPanel {

	private final JPanel northPanel;
	private final JPanel southPanel;

	private static final Color PANEL_COLOR = Color.decode("#4BC246");
	private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(130, 80);
	private static final Dimension PANEL_TAKEN_PIECES_DIMENSION = new Dimension(60, 40);
	private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

	public TakenPiecesPanel() {
		super(new BorderLayout());
		this.setBackground(PANEL_COLOR);
		this.setBorder(PANEL_BORDER);
		this.northPanel = new JPanel(new GridLayout(12, 2, 3, 3));
		this.southPanel = new JPanel(new GridLayout(12, 2, 3, 3));
		this.northPanel.setSize(PANEL_TAKEN_PIECES_DIMENSION);
		this.southPanel.setSize(PANEL_TAKEN_PIECES_DIMENSION);
		this.northPanel.add(new TakenPiecesIcon(Alliance.BLACK));
		this.northPanel.add(new TakenPiecesIcon(Alliance.BLACK));
		this.northPanel.add(new TakenPiecesIcon(Alliance.BLACK));
		this.northPanel.add(new TakenPiecesIcon(Alliance.BLACK));
		this.northPanel.validate();
		this.northPanel.repaint();
		this.northPanel.setBackground(Color.decode("#4BC246"));
		this.southPanel.setBackground(Color.decode("#4BC246"));
		this.add(this.northPanel, BorderLayout.NORTH);
		this.add(this.southPanel, BorderLayout.SOUTH);
		this.setPreferredSize(TAKEN_PIECES_DIMENSION);
	}

	public void redo(final MoveLog moveLog) {
		this.northPanel.removeAll();
		this.southPanel.removeAll();

		final List<Piece> whiteTakenPieces = new ArrayList<>();
		final List<Piece> blackTakenPieces = new ArrayList<>();

		for(final Move move : moveLog.getMoves()) {
			if(move.isAttack()) {
				// final Piece takenPiece = move.getAttackedPiece();
				// if(takenPiece.getPieceAlliance().isWhite()) {
				// 	whiteTakenPieces.add(takenPiece);
				// } else if(takenPiece.getPieceAlliance().isBlack()) {
				// 	blackTakenPieces.add(takenPiece);
				// } else {
				// 	throw new RuntimeException("Should not reach here!!");
				// }
			}
		}

		for(final Piece takenPiece : whiteTakenPieces) {
			this.southPanel.add(new TakenPiecesIcon(Alliance.WHITE));
		}

		for(final Piece takenPiece : blackTakenPieces) {
			this.northPanel.add(new TakenPiecesIcon(Alliance.BLACK));
		}

		this.validate();
	}

	private class TakenPiecesIcon extends JLabel {
		private Alliance alliance;

		TakenPiecesIcon(final Alliance alliance) {
			super();
			this.alliance = alliance;
		}

		@Override
		public void paintComponent(Graphics g) {
			Color pieceColor = this.alliance.isBlack() ? Color.BLACK : Color.WHITE;
			g.setColor(pieceColor);
			g.fillOval(0, 0, this.getWidth(), this.getHeight());
			super.paintComponent(g);
		}
	}

}
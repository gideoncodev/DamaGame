package dama.controller.board;

import dama.view.GameBoard;
import dama.view.GameBoard.*;
import dama.model.board.Move;
import dama.model.board.MoveTransition;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;

public class TileListener extends MouseAdapter {
	@Override
	public void mouseClicked(final MouseEvent e) {
		final TilePanel tilePanel = (TilePanel) e.getSource();
		if(SwingUtilities.isLeftMouseButton(e)) {
			if(tilePanel.getFrameSourceTile() == null) {
				tilePanel.setFrameSourceTile(tilePanel.getFrameDamaBoard().getTile(tilePanel.getTileId()));
				tilePanel.setFrameHumanMovedPiece(tilePanel.getFrameSourceTile().getPiece());
				if(tilePanel.getFrameHumanMovedPiece() == null) {
					tilePanel.setFrameSourceTile(null);
				}
			} else {
				tilePanel.setFrameDestinationTile(tilePanel.getFrameDamaBoard().getTile(tilePanel.getTileId()));
				final Move move = Move.MoveFactory.createMove(tilePanel.getFrameDamaBoard(),
															  tilePanel.getFrameSourceTile().getTileCoordinate(),
															  tilePanel.getFrameDestinationTile().getTileCoordinate());
				final MoveTransition transition = tilePanel.getFrameDamaBoard().getCurrentPlayer().makeMove(move);
				if(transition.getMoveStatus().isDone()) {
					tilePanel.setFrameDamaBoard(transition.getTransitionBoard());
				}
				tilePanel.setFrameSourceTile(null);
				tilePanel.setFrameDestinationTile(null);
				tilePanel.setFrameHumanMovedPiece(null);
			}

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					tilePanel.getBoardPanel().drawBoard(tilePanel.getFrameDamaBoard());
				}
			});
		}
	}
}
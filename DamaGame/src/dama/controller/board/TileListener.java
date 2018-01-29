package dama.controller.board;

import dama.view.GameBoard;
import dama.view.GameBoard.*;
import dama.model.board.Move;
import dama.model.board.MoveTransition;
import dama.model.board.Tile;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
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
				boolean validTile = false;
				final Tile secondTile = tilePanel.getFrameDamaBoard().getTile(tilePanel.getTileId());

				for(final Move move : tilePanel.getHumanPieceLegalMoves()) {
					if(move.getDestinationCoordinate() == secondTile.getTileCoordinate()) {
						validTile = true;
						break;
					}
				}

				if(secondTile.isTileOccupied()) {
					tilePanel.setFrameSourceTile(tilePanel.getFrameDamaBoard().getTile(tilePanel.getTileId()));
					tilePanel.setFrameHumanMovedPiece(tilePanel.getFrameSourceTile().getPiece());
					if(tilePanel.getFrameHumanMovedPiece() == null) {
						tilePanel.setFrameSourceTile(null);
					}
				} else if(!validTile) {
					tilePanel.setFrameSourceTile(null);
					tilePanel.setFrameDestinationTile(null);
					tilePanel.setFrameHumanMovedPiece(null);
				} else {
					tilePanel.setFrameDestinationTile(tilePanel.getFrameDamaBoard().getTile(tilePanel.getTileId()));
					final Move move = Move.MoveFactory.createMove(tilePanel.getFrameDamaBoard(),
																  tilePanel.getFrameSourceTile().getTileCoordinate(),
																  tilePanel.getFrameDestinationTile().getTileCoordinate());
					final MoveTransition transition = tilePanel.getFrameDamaBoard().getCurrentPlayer().makeMove(move);
					if(transition.getMoveStatus().isDone()) {
						tilePanel.setFrameDamaBoard(transition.getTransitionBoard());
						tilePanel.getFrameMoveLog().addMove(move);
						// tilePanel.setFrameBoardDirection(tilePanel.getFrameBoardDirection().opposite());
					}
					tilePanel.setFrameSourceTile(null);
					tilePanel.setFrameDestinationTile(null);
					tilePanel.setFrameHumanMovedPiece(null);
				}
			}

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// tilePanel.getFrameTakenPiecesPanel().redo(tilePanel.getFrameMoveLog());
					tilePanel.getBoardPanel().drawBoard(tilePanel.getFrameDamaBoard());
				}
			});
		}
	}
}
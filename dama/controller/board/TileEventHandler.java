package dama.controller.board;

import dama.view.board.TileIcon;
import dama.view.board.GameBoard;
import dama.model.board.Move;
import dama.model.board.MoveTransition;
import static dama.model.board.Move.MoveFactory;
import dama.model.board.Tile;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.event.EventHandler;

public class TileEventHandler implements EventHandler<MouseEvent> {
	final TileIcon tileIcon;

	public TileEventHandler(final TileIcon tileIcon) {
		this.tileIcon = tileIcon;
	}

	@Override
	public void handle(final MouseEvent mouseEvent) {
		if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
			if(GameBoard.get().getBoardPane().getSourceTile() == null) {
				GameBoard.get().getBoardPane().setSourceTile(GameBoard.get().getBoardPane().getBoard().getTile(this.tileIcon.getTileId()));
				GameBoard.get().getBoardPane().setSelectedPiece(GameBoard.get().getBoardPane().getSourceTile().getPiece());
				if(GameBoard.get().getBoardPane().getSelectedPiece() == null) {
					GameBoard.get().getBoardPane().setSourceTile(null);
				} else {
					if(GameBoard.get().getBoardPane().getSelectedPiece().getPieceAlliance() !=
					   GameBoard.get().getBoardPane().getBoard().getCurrentPlayer().getAlliance()){
						GameBoard.get().getBoardPane().setSourceTile(null);
						GameBoard.get().getBoardPane().setSelectedPiece(null);
					}
				}
			} else {
				boolean validTile = false;
				final Tile secondTile = GameBoard.get().getBoardPane().getBoard().getTile(this.tileIcon.getTileId());

				for(final Move move : this.tileIcon.getSelectedPieceLegalMoves()) {
					if(move.getDestinationCoordinate() == secondTile.getTileCoordinate()) {
						validTile = true;
						break;
					}
				}

				if(secondTile.isTileOccupied()) {
					GameBoard.get().getBoardPane().setSourceTile(secondTile);
					GameBoard.get().getBoardPane().setSelectedPiece(GameBoard.get().getBoardPane().getSourceTile().getPiece());
					if(GameBoard.get().getBoardPane().getSelectedPiece() == null) {
						GameBoard.get().getBoardPane().setSourceTile(null);
					} else {
						if(GameBoard.get().getBoardPane().getSelectedPiece().getPieceAlliance() !=
						   GameBoard.get().getBoardPane().getBoard().getCurrentPlayer().getAlliance()){
							GameBoard.get().getBoardPane().setSourceTile(null);
							GameBoard.get().getBoardPane().setSelectedPiece(null);
						}
					}
				} else if(!validTile) {
					GameBoard.get().getBoardPane().setSourceTile(null);
					GameBoard.get().getBoardPane().setDestinationTile(null);
					GameBoard.get().getBoardPane().setSelectedPiece(null);
				} else {
					GameBoard.get().getBoardPane().setDestinationTile(secondTile);
					final Move move = MoveFactory.createMove(GameBoard.get().getBoardPane().getBoard(),
															 GameBoard.get().getBoardPane().getSourceTile().getTileCoordinate(),
															 GameBoard.get().getBoardPane().getDestinationTile().getTileCoordinate());
					final MoveTransition moveTransition = GameBoard.get().getBoardPane().getBoard().getCurrentPlayer().makeMove(move);
					if(moveTransition.getMoveStatus().isDone()) {
						GameBoard.get().getMoveLog().addUndoMoves(move, GameBoard.get().getBoardPane().getBoard());
						GameBoard.get().getMoveLog().clearRedoMoves();
						GameBoard.get().getBoardPane().setBoard(moveTransition.getTransitionBoard());
					}
					GameBoard.get().getBoardPane().setSourceTile(null);
					GameBoard.get().getBoardPane().setDestinationTile(null);
					GameBoard.get().getBoardPane().setSelectedPiece(null);
				}
			}

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					GameBoard.get().getBoardPane().drawBoard(GameBoard.get().getBoardPane().getBoard());
					GameBoard.get().getBoardProperty().setValue(GameBoard.get().getBoardPane().getBoard());
				}
			});
		}
	}
}
package dama.controller.board;

import dama.view.TileIcon;
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
			if(this.tileIcon.getBoardPane().getSourceTile() == null) {
				this.tileIcon.getBoardPane().setSourceTile(this.tileIcon.getBoardPane().getBoard().getTile(this.tileIcon.getTileId()));
				this.tileIcon.getBoardPane().setSelectedPiece(this.tileIcon.getBoardPane().getSourceTile().getPiece());
				if(this.tileIcon.getBoardPane().getSelectedPiece() == null) {
					this.tileIcon.getBoardPane().setSourceTile(null);
				} else {
					if(this.tileIcon.getBoardPane().getSelectedPiece().getPieceAlliance() !=
					   this.tileIcon.getBoardPane().getBoard().getCurrentPlayer().getAlliance()){
						this.tileIcon.getBoardPane().setSourceTile(null);
						this.tileIcon.getBoardPane().setSelectedPiece(null);
					}
				}
			} else {
				boolean validTile = false;
				final Tile secondTile = this.tileIcon.getBoardPane().getBoard().getTile(this.tileIcon.getTileId());

				for(final Move move : this.tileIcon.getSelectedPieceLegalMoves()) {
					if(move.getDestinationCoordinate() == secondTile.getTileCoordinate()) {
						validTile = true;
						break;
					}
				}

				if(secondTile.isTileOccupied()) {
					this.tileIcon.getBoardPane().setSourceTile(secondTile);
					this.tileIcon.getBoardPane().setSelectedPiece(this.tileIcon.getBoardPane().getSourceTile().getPiece());
					if(this.tileIcon.getBoardPane().getSelectedPiece() == null) {
						this.tileIcon.getBoardPane().setSourceTile(null);
					} else {
						if(this.tileIcon.getBoardPane().getSelectedPiece().getPieceAlliance() !=
						   this.tileIcon.getBoardPane().getBoard().getCurrentPlayer().getAlliance()){
							this.tileIcon.getBoardPane().setSourceTile(null);
							this.tileIcon.getBoardPane().setSelectedPiece(null);
						}
					}
				} else if(!validTile) {
					this.tileIcon.getBoardPane().setSourceTile(null);
					this.tileIcon.getBoardPane().setDestinationTile(null);
					this.tileIcon.getBoardPane().setSelectedPiece(null);
				} else {
					this.tileIcon.getBoardPane().setDestinationTile(secondTile);
					final Move move = MoveFactory.createMove(this.tileIcon.getBoardPane().getBoard(),
															 this.tileIcon.getBoardPane().getSourceTile().getTileCoordinate(),
															 this.tileIcon.getBoardPane().getDestinationTile().getTileCoordinate());
					final MoveTransition moveTransition = this.tileIcon.getBoardPane().getBoard().getCurrentPlayer().makeMove(move);
					if(moveTransition.getMoveStatus().isDone()) {
						this.tileIcon.getBoardPane().setBoard(moveTransition.getTransitionBoard());
					}
					this.tileIcon.getBoardPane().setSourceTile(null);
					this.tileIcon.getBoardPane().setDestinationTile(null);
					this.tileIcon.getBoardPane().setSelectedPiece(null);
				}
			}

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					tileIcon.getBoardPane().drawBoard(tileIcon.getBoardPane().getBoard());
				}
			});
		}
	}
}
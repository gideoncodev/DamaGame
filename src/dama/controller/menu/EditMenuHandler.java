package dama.controller.menu;

import dama.model.board.Board;
import dama.model.board.Move;
import dama.view.board.GameBoard;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class EditMenuHandler {
	public static class RedoMenu implements EventHandler<ActionEvent> {
		@Override
		public void handle(final ActionEvent e) {
			if(!GameBoard.get().getMoveLog().getRedoMoves().isEmpty()) {
				if(GameBoard.get().hasAIPlayer()) {
					for(int i = 0; i < 2; i++) {
						final Move move = GameBoard.get().getMoveLog().removeRedoListMoves(GameBoard.get().getMoveLog().getRedoMoves().size() - 1);
						final Board board = GameBoard.get().getMoveLog().removeRedoMapMoves(move);
						GameBoard.get().getMoveLog().addUndoMoves(move, GameBoard.get().getBoardPane().getBoard());
						GameBoard.get().getBoardPane().setBoard(board);
					}
				} else {
					final Move move = GameBoard.get().getMoveLog().removeRedoListMoves(GameBoard.get().getMoveLog().getRedoMoves().size() - 1);
					final Board board = GameBoard.get().getMoveLog().removeRedoMapMoves(move);
					GameBoard.get().getMoveLog().addUndoMoves(move, GameBoard.get().getBoardPane().getBoard());
					GameBoard.get().getBoardPane().setBoard(board);
				}
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						GameBoard.get().getWhiteTakenPiecesPane().getPlayerProfile().update(GameBoard.get().getBoardPane().getBoard());
						GameBoard.get().getBlackTakenPiecesPane().getPlayerProfile().update(GameBoard.get().getBoardPane().getBoard());
						GameBoard.get().getWhiteTakenPiecesPane().getTakenPieces().draw(GameBoard.get().getMoveLog().getAttackedPieces());
						GameBoard.get().getBlackTakenPiecesPane().getTakenPieces().draw(GameBoard.get().getMoveLog().getAttackedPieces());
						GameBoard.get().getBoardPane().drawBoard(GameBoard.get().getBoardPane().getBoard());
					}
				});
			}
		}
	}

	public static class UndoMenu implements EventHandler<ActionEvent> {
		@Override
		public void handle(final ActionEvent e) {
			if(!GameBoard.get().getMoveLog().getUndoMoves().isEmpty()) {
				if(GameBoard.get().hasAIPlayer()) {
					for(int i = 0; i < 2; i++) {
						final Move move = GameBoard.get().getMoveLog().removeUndoListMoves(GameBoard.get().getMoveLog().getUndoMoves().size() - 1);
						final Board board = GameBoard.get().getMoveLog().removeUndoMapMoves(move);
						GameBoard.get().getMoveLog().addRedoMoves(move, GameBoard.get().getBoardPane().getBoard());
						GameBoard.get().getBoardPane().setBoard(board);
					}
				} else {
					final Move move = GameBoard.get().getMoveLog().removeUndoListMoves(GameBoard.get().getMoveLog().getUndoMoves().size() - 1);
					final Board board = GameBoard.get().getMoveLog().removeUndoMapMoves(move);
					GameBoard.get().getMoveLog().addRedoMoves(move, GameBoard.get().getBoardPane().getBoard());
					GameBoard.get().getBoardPane().setBoard(board);
				}
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						GameBoard.get().getWhiteTakenPiecesPane().getPlayerProfile().update(GameBoard.get().getBoardPane().getBoard());
						GameBoard.get().getBlackTakenPiecesPane().getPlayerProfile().update(GameBoard.get().getBoardPane().getBoard());
						GameBoard.get().getWhiteTakenPiecesPane().getTakenPieces().draw(GameBoard.get().getMoveLog().getAttackedPieces());
						GameBoard.get().getBlackTakenPiecesPane().getTakenPieces().draw(GameBoard.get().getMoveLog().getAttackedPieces());
						GameBoard.get().getBoardPane().drawBoard(GameBoard.get().getBoardPane().getBoard());
					}
				});
			}
		}
	}
}
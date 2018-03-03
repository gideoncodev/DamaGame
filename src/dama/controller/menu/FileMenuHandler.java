package dama.controller.menu;

import dama.view.board.GameBoard;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class FileMenuHandler {
	public static class NewHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(final ActionEvent e) {
			GameBoard.get().newGameBoard();
		}
	}

	public static class ExitHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(final ActionEvent e) {
			Platform.exit();
		}
	}
}
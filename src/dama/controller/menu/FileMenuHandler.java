package dama.controller.menu;

import dama.view.GameBoard;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseButton;
import javafx.event.EventHandler;

public class FileMenuHandler {
	public static class NewHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(final ActionEvent e) {
			System.out.println(e.getEventType().getSuperType());
			System.out.println("New Game");
			GameBoard.get().newGameBoard();
		}
	}

	public static class ExitHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(final ActionEvent e) {
			System.out.println("Exit");
			Platform.exit();
		}
	}
}
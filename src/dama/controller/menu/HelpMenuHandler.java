package dama.controller.menu;

import dama.view.board.GameBoard;
import dama.view.board.AboutDama;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class HelpMenuHandler {
	public static class AboutDamaHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(final ActionEvent e) {
			new AboutDama();
		}
	}
}
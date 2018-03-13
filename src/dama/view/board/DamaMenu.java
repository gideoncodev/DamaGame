package dama.view.board;

import dama.controller.menu.FileMenuHandler.NewHandler;
import dama.controller.menu.FileMenuHandler.ExitHandler;
import dama.controller.menu.EditMenuHandler.RedoHandler;
import dama.controller.menu.EditMenuHandler.UndoHandler;
import dama.controller.menu.HelpMenuHandler.AboutDamaHandler;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;

public class DamaMenu extends MenuBar {

	public DamaMenu() {
		this.setStyle("-fx-font-size: 18; -fx-font-family: Georgia;");
		this.getMenus().addAll(createFileMenu(), createEditMenu(), createAboutMenu());
	}

	private Menu createEditMenu() {
		final Menu editMenu = new Menu("Edit");
		final MenuItem redoMove = new MenuItem("Redo Move");
		redoMove.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
		redoMove.setOnAction(new RedoHandler());
		final MenuItem undoMove = new MenuItem("Undo Move");
		undoMove.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
		undoMove.setOnAction(new UndoHandler());
		editMenu.getItems().addAll(redoMove, undoMove);
		return editMenu;
	}
	private Menu createFileMenu() {
		final Menu fileMenu = new Menu("File");
		final MenuItem newGame = new MenuItem("New Game");
		newGame.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
		newGame.setOnAction(new NewHandler());
		final MenuItem exitGame = new MenuItem("Exit");
		exitGame.setOnAction(new ExitHandler());
		fileMenu.getItems().addAll(newGame, new SeparatorMenuItem(), exitGame);
		return fileMenu;
	}

	private Menu createAboutMenu() {
		final Menu helpMenu = new Menu("Help");
		final MenuItem aboutMenu = new MenuItem("About Dama");
		aboutMenu.setOnAction(new AboutDamaHandler());
		helpMenu.getItems().add(aboutMenu);
		return helpMenu;
	}
}
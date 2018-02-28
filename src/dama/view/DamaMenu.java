package dama.view;

import dama.controller.menu.FileMenuHandler.NewHandler;
import dama.controller.menu.FileMenuHandler.ExitHandler;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;

public class DamaMenu extends MenuBar {

	public DamaMenu() {
		this.getMenus().addAll(createFileMenu(),createEditMenu());
	}

	private Menu createEditMenu() {
		final Menu editMenu = new Menu("Edit");
		final MenuItem redoMove = new MenuItem("Redo Move");
		redoMove.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
		final MenuItem undoMove = new MenuItem("Undo Move");
		undoMove.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
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
}
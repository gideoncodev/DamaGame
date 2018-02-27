package dama.view;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class DamaMenu extends MenuBar {

	public DamaMenu() {
		this.getMenus().addAll(createFileMenu(),createEditMenu());
	}

	private Menu createEditMenu() {
		final Menu editMenu = new Menu("Edit");
		final MenuItem redoMove = new MenuItem("Redo Move");
		final MenuItem undoMove = new MenuItem("Undo Move");
		editMenu.getItems().addAll(redoMove, undoMove);
		return editMenu;
	}
	private Menu createFileMenu() {
		final Menu fileMenu = new Menu("File");
		final MenuItem newGame = new MenuItem("New Game");
		final MenuItem exitGame = new MenuItem("Exit");
		fileMenu.getItems().addAll(newGame, new SeparatorMenuItem(), exitGame);
		return fileMenu;
	}
}
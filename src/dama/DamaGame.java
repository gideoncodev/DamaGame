package dama;

import dama.model.board.Board;
import dama.view.board.GameBoard;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class DamaGame extends Application {

	@Override 
	public void start(final Stage stage) throws Exception {
		stage.setTitle("Dama");
		stage.setResizable(false);
		stage.sizeToScene();
		stage.setScene(new Scene(GameBoard.get()));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}

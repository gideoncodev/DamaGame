package dama.view.board;

import dama.view.board.GameBoard;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;

public class GameOverStage extends Stage {

	public GameOverStage() {
		this.initStyle(StageStyle.TRANSPARENT);
		this.initOwner(GameBoard.get().getScene().getWindow());
		this.initModality(Modality.WINDOW_MODAL);
		this.setScene(new Scene(new GameOverParentPane(this)));
		this.sizeToScene();
		this.show();
	}

	private static class GameOverParentPane extends BorderPane {
		private final GameOverHeader winningHeader;
		private final GameOverInfo additionalInfo;

		public GameOverParentPane(final Stage stage) {
			this.winningHeader = new GameOverHeader();
			this.additionalInfo = new GameOverInfo(stage);
			this.setStyle("-fx-background-color: #373739;");
			this.setTop(this.winningHeader);
			BorderPane.setMargin(this.winningHeader, new Insets(20, 20, 10, 20));
			this.setCenter(this.additionalInfo);
			BorderPane.setMargin(this.additionalInfo, new Insets(0, 20, 0, 20));
		}
	}

	private static class GameOverHeader extends BorderPane {
		private final static String WINNING_IMAGE_URL = "dama/view/image/winner.png";

		private final Image winningImage = new Image(WINNING_IMAGE_URL);
		private final Label winningLabel;
		private final Circle winningIcon;

		GameOverHeader() {
			this.winningLabel = new Label();
			this.winningIcon = new Circle();
			this.createWinningLabel();
			this.createWinningIcon();
			this.setCenter(this.winningLabel);
			BorderPane.setAlignment(this.winningLabel, Pos.CENTER_LEFT);
			this.setRight(this.winningIcon);
			BorderPane.setAlignment(this.winningIcon, Pos.CENTER_LEFT);
		}

		private void createWinningLabel() {
			this.winningLabel.setText(GameBoard.get().getBoardPane().getBoard().getCurrentPlayer().getOpponent() + " Player WINS!!!");
			this.winningLabel.setTextFill(Color.valueOf("#EBE7DB"));
			this.winningLabel.setStyle("-fx-font-size: 35; -fx-font-family: Tahoma;");
		}

		private void createWinningIcon() {
			this.winningIcon.setRadius(50);
			this.winningIcon.setFill(new ImagePattern(this.winningImage));
		}
	}

	private static class GameOverInfo extends BorderPane {
		
		private final Label additionalInfo;
		private final HBox additionalButtons;

		GameOverInfo(final Stage stage) {
			this.additionalInfo = new Label();
			this.additionalButtons = new HBox();
			this.createAdditionalInfo();
			this.createButtons(stage);
			this.setCenter(this.additionalInfo);
			BorderPane.setAlignment(this.additionalInfo, Pos.TOP_LEFT);
			this.setRight(this.additionalButtons);
			BorderPane.setAlignment(this.additionalButtons, Pos.BOTTOM_CENTER);
			BorderPane.setMargin(this.additionalButtons, new Insets(70, 0, 20, 50));
		}

		private void createAdditionalInfo() {
			this.additionalInfo.setText("Would you like to start a New Game?");
			this.additionalInfo.setTextFill(Color.valueOf("#EBE7DB"));
			this.additionalInfo.setStyle("-fx-font-size: 18; -fx-font-family: Tahoma;");
		}

		private void createButtons(final Stage stage) {
			final Button yesButton = new Button("Yes");
			yesButton.setCursor(Cursor.HAND);
			yesButton.setTextFill(Color.valueOf("#EBE7DB"));
			yesButton.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma; -fx-background-color: #373739; -fx-border-color: #EBE7DB; -fx-border-width: 3; -fx-border-radius: 50; -fx-background-radius: 50;");
			final Button noButton = new Button("No");
			noButton.setCursor(Cursor.HAND);
			noButton.setTextFill(Color.valueOf("#EBE7DB"));
			noButton.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma; -fx-background-color: #373739; -fx-border-color: #EBE7DB; -fx-border-width: 3; -fx-border-radius: 50; -fx-background-radius: 50;");

			//Actions
			yesButton.setOnMouseClicked(e -> {
				stage.close();
				GameBoard.get().newGameBoard();
			});
			yesButton.setOnMouseEntered(e -> {
				yesButton.setTextFill(Color.valueOf("#373739"));
				yesButton.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma; -fx-background-color: #EBE7DB; -fx-border-color: #EBE7DB; -fx-border-width: 3; -fx-border-radius: 50; -fx-background-radius: 50;");
			});
			yesButton.setOnMouseExited(e -> {
				yesButton.setTextFill(Color.valueOf("#EBE7DB"));
				yesButton.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma; -fx-background-color: #373739; -fx-border-color: #EBE7DB; -fx-border-width: 3; -fx-border-radius: 50; -fx-background-radius: 50;");
			});

			noButton.setOnMouseClicked(e -> Platform.exit());
			noButton.setOnMouseEntered(e -> {
				noButton.setTextFill(Color.valueOf("#373739"));
				noButton.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma; -fx-background-color: #EBE7DB; -fx-border-color: #EBE7DB; -fx-border-width: 3; -fx-border-radius: 50; -fx-background-radius: 50;");
			});
			noButton.setOnMouseExited(e -> {
				noButton.setTextFill(Color.valueOf("#EBE7DB"));
				noButton.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma; -fx-background-color: #373739; -fx-border-color: #EBE7DB; -fx-border-width: 3; -fx-border-radius: 50; -fx-background-radius: 50;");
			});

			this.additionalButtons.setPadding(new Insets(10));
			this.additionalButtons.setSpacing(15);
			this.additionalButtons.getChildren().addAll(yesButton, noButton);
		}
	}

}
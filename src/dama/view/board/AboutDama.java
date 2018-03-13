package dama.view.board;

import dama.view.board.GameBoard;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

public class AboutDama extends Stage {

	public AboutDama() {
		this.initStyle(StageStyle.TRANSPARENT);
		this.initOwner(GameBoard.get().getScene().getWindow());
		this.initModality(Modality.WINDOW_MODAL);
		this.setScene(new Scene(new AboutPane(this)));
		this.sizeToScene();
		final double mainStageX = this.getOwner().getX() + this.getOwner().getWidth()/2d;
		final double mainStageY = this.getOwner().getY() + this.getOwner().getHeight()/2d;
		this.setOnShowing(e -> {
			hide();
		});
		this.setOnShown(e -> {
			setX(mainStageX - getWidth()/2d);
			setY(mainStageY - getHeight()/2d);
			show();
		});
		this.show();
	}

	private static class AboutPane extends BorderPane {
		private final Group closeButton = new Group();
		private final Pane aboutDama = new Pane();

		public AboutPane(final Stage stage) {
			this.setStyle("-fx-background-color: #373739;");
			this.createCloseButton();
			this.setTop(this.closeButton);
			BorderPane.setAlignment(this.closeButton, Pos.CENTER_RIGHT);
			BorderPane.setMargin(this.closeButton, new Insets(10));
			this.closeButton.setOnMouseClicked(e -> stage.close());
			this.createAboutDama();
			this.setCenter(this.aboutDama);
			BorderPane.setAlignment(this.aboutDama, Pos.CENTER);
			BorderPane.setMargin(this.aboutDama, new Insets(50));
		}

		private void createCloseButton() {
	        Circle circleBtn = new Circle();
	        circleBtn.setRadius(15);
	        circleBtn.setFill(Color.TRANSPARENT);
	        circleBtn.setStroke(Color.valueOf("#72716B"));
	        circleBtn.setStrokeWidth(3);
	        Line line1 = new Line(-5.0, 5.0, 5.0, -5.0);
	        Line line2 = new Line(-5.0, -5.0, 5.0, 5.0);
	        line1.setStrokeWidth(3);
	        line2.setStrokeWidth(3);
	        line1.setStroke(Color.valueOf("#72716B"));
        	line2.setStroke(Color.valueOf("#72716B"));
	        circleBtn.setOnMouseEntered(e -> {
	        	circleBtn.setStroke(Color.valueOf("#EBE7DB"));
	        	line1.setStroke(Color.valueOf("#EBE7DB"));
	        	line2.setStroke(Color.valueOf("#EBE7DB"));
	        });
	        circleBtn.setOnMouseExited(e -> {
	        	circleBtn.setStroke(Color.valueOf("#72716B"));
	        	line1.setStroke(Color.valueOf("#72716B"));
	        	line2.setStroke(Color.valueOf("#72716B"));
	        });
	        line1.setOnMouseEntered(e -> {
	        	circleBtn.setStroke(Color.valueOf("#EBE7DB"));
	        	line1.setStroke(Color.valueOf("#EBE7DB"));
	        	line2.setStroke(Color.valueOf("#EBE7DB"));
	        });
	        line2.setOnMouseEntered(e -> {
	        	circleBtn.setStroke(Color.valueOf("#EBE7DB"));
	        	line1.setStroke(Color.valueOf("#EBE7DB"));
	        	line2.setStroke(Color.valueOf("#EBE7DB"));
	        });
	        this.closeButton.setCursor(Cursor.HAND);
	        this.closeButton.getChildren().addAll(circleBtn, line1, line2);
		}

		private void createAboutDama() {
			final Label dama = new Label("DAMA");
			dama.setTextFill(Color.valueOf("#EBE7DB"));
			dama.setStyle("-fx-font-size: 60; -fx-font-family: Georgia;");
			final Label description = new Label("(Filipino Checkers)");
			description.setTextFill(Color.valueOf("#EBE7DB"));
			description.setStyle("-fx-font-size: 20; -fx-font-family: Georgia;");
			final Label description2 = new Label("Player VS Computer");
			description2.setTextFill(Color.valueOf("#EBE7DB"));
			description2.setStyle("-fx-font-size: 20; -fx-font-family: Georgia;");

			final Label createdBy = new Label("Created by: Gideon M. Arces Jr.");
			createdBy.setTextFill(Color.valueOf("#EBE7DB"));
			createdBy.setStyle("-fx-font-size: 20; -fx-font-family: Georgia;");
			final Label createYear = new Label("SY: 2017 - 2018");
			createYear.setTextFill(Color.valueOf("#EBE7DB"));
			createYear.setStyle("-fx-font-size: 20; -fx-font-family: Georgia;");
			final Label createSchool = new Label("Visayas State University");
			createSchool.setTextFill(Color.valueOf("#EBE7DB"));
			createSchool.setStyle("-fx-font-size: 20; -fx-font-family: Georgia;");

			this.aboutDama.setPrefSize(500, 250);
			this.aboutDama.getChildren().addAll(dama, 
												description,
												description2,
												createdBy,
												createYear,
												createSchool);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					dama.relocate((aboutDama.getWidth() / 2) - (dama.getWidth() / 2), -60);
					description.relocate((aboutDama.getWidth() / 2) - (description.getWidth() / 2), 0);
					description2.relocate((aboutDama.getWidth() / 2) - (description2.getWidth() / 2), 40);
					createdBy.relocate((aboutDama.getWidth() / 2) - (createdBy.getWidth() / 2), 160);
					createYear.relocate((aboutDama.getWidth() / 2) - (createYear.getWidth() / 2), 185);
					createSchool.relocate((aboutDama.getWidth() / 2) - (createSchool.getWidth() / 2), 210);
				}
			});
		}
	}

}
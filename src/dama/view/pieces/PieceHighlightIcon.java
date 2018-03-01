package dama.view.pieces;

import dama.model.Alliance;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PieceHighlightIcon extends Circle {
	final Alliance pieceAlliance;
	public PieceHighlightIcon(final int tileSize, final Alliance pieceAlliance) {
		this.pieceAlliance = pieceAlliance;
		this.setRadius((tileSize / 2) - 10);
		this.relocate(10, 10);
		this.setFill(Color.valueOf("#582"));
		this.setStroke(this.pieceAlliance.isBlack() ? Color.valueOf("#1E1E1E") : Color.valueOf("#FFF9F4"));
		this.setStrokeWidth(2);
	}
}
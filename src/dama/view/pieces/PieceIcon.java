package dama.view.pieces;

import dama.model.Alliance;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PieceIcon extends Circle {
	final Alliance pieceAlliance;
	public PieceIcon(final int tileSize, final Alliance pieceAlliance, final boolean pieceHasLegalMoves) {
		this.pieceAlliance = pieceAlliance;
		this.setRadius((tileSize / 2) - 10);
		this.relocate(10, 10);
		this.setFill(this.pieceAlliance.isBlack() ? Color.valueOf("#1E1E1E") : Color.valueOf("#FFF9F4"));
		if(pieceHasLegalMoves) {
			this.setStroke(Color.valueOf("#EE4035"));
			this.setStrokeWidth(2);
		}
	}
}
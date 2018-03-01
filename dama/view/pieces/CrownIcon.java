package dama.view.pieces;

import dama.model.board.Board;
import dama.model.board.BoardUtils;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class CrownIcon extends Polygon {
	public CrownIcon(final int tileSize) {
		final double tileCenter = tileSize / 2;
		final double crownSize = tileCenter - 10.0;
		this.getPoints().addAll(new Double[]{
							    tileCenter - (crownSize / 2), tileCenter - (crownSize / 2),
							    tileCenter - (crownSize / 4), tileCenter,
							    tileCenter, tileCenter - (crownSize / 2),
							    tileCenter + (crownSize / 4), tileCenter,
							    tileCenter + (crownSize / 2), tileCenter - (crownSize / 2),
							    tileCenter + (crownSize / 4), tileCenter + (crownSize / 2),
							    tileCenter - (crownSize / 4), tileCenter + (crownSize / 2) });
		this.setFill(Color.valueOf("#FFD700"));
	}
}
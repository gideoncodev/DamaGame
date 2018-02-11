package dama.view;

import dama.model.board.Board;
import dama.model.board.BoardUtils;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class CrownIcon extends Polygon {
	public CrownIcon() {
		this.getPoints().addAll(new Double[]{
							    25.0, 27.0,
							    35.0, 47.0,
							    45.0, 27.0,
							    55.0, 47.0,
							    65.0, 27.0,
							    55.0, 63.0,
							    35.0, 63.0 });
		this.setFill(Color.valueOf("#FFD700"));
	}
}
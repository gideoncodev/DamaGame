package dama.view.board;

import dama.model.Alliance;
import dama.model.pieces.Piece;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class TakenPiecesPane extends AnchorPane {
	private final Alliance alliance;

	public TakenPiecesPane(final Alliance alliance) {
		this.alliance = alliance;
		this.setupPane();
	}

	private void setupPane() {
		if(this.alliance.isWhite()) {
			// this.setBottomAchor();
			// this.setTopAnchor();
		} else {
			// this.setBottomAchor();
			// this.setTopAnchor();
		}
	}

	public void draw(final List<Piece> pieces) {

	}

	private static class TakenPieces extends GridPane {

	}

	private static class TilePiece extends Pane {
		
	}

}
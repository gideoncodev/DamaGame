package dama.view.board;

import dama.model.Alliance;
import dama.model.pieces.Piece;
import dama.model.board.Board;

import javafx.stage.Screen;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class TakenPiecesPane extends BorderPane {
	private final Alliance alliance;
	private final int tileSize;
	private final TakenPieces takenPieces;
	private final PlayerProfile playerProfile;

	private final static Rectangle2D SCREEN_BOUNDS = Screen.getPrimary().getVisualBounds();
	private final static int TAKEN_PIECES_TILE_SIZE = (int) (SCREEN_BOUNDS.getHeight() - 100) / 8;

	public TakenPiecesPane(final Alliance alliance) {
		this.alliance = alliance;
		this.tileSize = TAKEN_PIECES_TILE_SIZE;
		this.takenPieces = new TakenPieces(this.tileSize, this.alliance);
		this.playerProfile = new PlayerProfile(this.tileSize, this.alliance);
		this.setStyle("-fx-background-color: #808080;");
		this.setupPane();
	}

	private void setupPane() {
		if(this.alliance.isWhite()) {
			this.setBottom(this.playerProfile);
			this.setTop(this.takenPieces);
		} else {
			this.setBottom(this.takenPieces);
			this.setTop(this.playerProfile);
		}
	}

	public TakenPieces getTakenPieces() {
		return this.takenPieces;
	}

	public PlayerProfile getPlayerProfile() {
		return this.playerProfile;
	}

	public class TakenPieces extends GridPane {
		private final int tileSize;
		private final Alliance takenPiecesAlliance;
		private final List<TilePiece> takenTilePieces;

		public TakenPieces(final int tileSize, final Alliance alliance) {
			this.tileSize = tileSize;
			this.takenPiecesAlliance = alliance;
			this.takenTilePieces = new ArrayList<>();
			for(int i = 0; i < 12; i++) {
				final TilePiece tilePiece = new TilePiece(this.tileSize);
				this.takenTilePieces.add(tilePiece);
				this.add(tilePiece, (i % 2), (i / 2));
			}
		}

		public void draw(final List<Piece> pieces) {
			this.getChildren().clear();
			final List<Piece> attackedPieces = new ArrayList<>();
			for(final Piece piece : pieces) {
				if(piece.getPieceAlliance() != this.takenPiecesAlliance) attackedPieces.add(piece);
			}

			for(final TilePiece tilePiece : this.takenTilePieces) {
				int key = this.takenTilePieces.indexOf(tilePiece);
				boolean hasToAssignPiece = !pieces.isEmpty() && !attackedPieces.isEmpty() && attackedPieces.size() > key;
				if(hasToAssignPiece) {
					tilePiece.drawTile(attackedPieces.get(key));
				} else {
					tilePiece.drawEmptyTile();
				}
			}

			if(this.takenPiecesAlliance.isWhite()) Collections.reverse(this.takenTilePieces);

			for(final TilePiece tilePiece : this.takenTilePieces) {
				int key = this.takenTilePieces.indexOf(tilePiece);
				this.add(tilePiece, (key % 2), (key / 2));
			}
		}

	}

	public class PlayerProfile extends Pane {
		private final static String PLAYER_URL = "dama/view/image/player.png";

		private final Image playerImage = new Image(PLAYER_URL);
		private final Circle profile;
		private final int tileSize;
		private final Alliance alliance;

		public PlayerProfile(final int tileSize, final Alliance alliance) {
			this.tileSize = tileSize;
			this.alliance = alliance;
			this.profile = new Circle();
			this.profile.setFill(new ImagePattern(playerImage));
			this.profile.setRadius(this.tileSize - 10);
			this.profile.relocate(10, 10);
			this.getChildren().add(this.profile);
		}

		public void update(final Board board) {
			if(board.getCurrentPlayer().getAlliance() == this.alliance) {
				this.profile.setStroke(Color.valueOf("#EE4035"));
				this.profile.setStrokeWidth(2);
			} else {
				this.profile.setStroke(null);
				this.profile.setStrokeWidth(0);
			}
		}
	}

	public class TilePiece extends Pane {
		private final int tileSize;

		public TilePiece(final int tileSize) {
			this.tileSize = tileSize;
			this.setPrefSize(this.tileSize, this.tileSize);
		}

		public void drawTile(final Piece piece) {
			this.setTileEmpty();
			this.setTilePiece(piece);
		}

		public void drawEmptyTile() {
			this.setTileEmpty();
		}

		private void setTileEmpty() {
			this.getChildren().clear();
		}

		private void setTilePiece(final Piece piece) {
			final Color pieceColor = piece.getPieceAlliance().isBlack() ? Color.valueOf("#1E1E1E") : Color.valueOf("#FFF9F4");
			final Circle pieceIcon = new Circle();
			pieceIcon.setFill(pieceColor);
			pieceIcon.setRadius((this.tileSize / 2) - 10);
			pieceIcon.relocate(10, 10);
			this.getChildren().add(pieceIcon);
			if(piece.getPieceType().isKingDama()) {
				final double tileCenter = this.tileSize / 2;
				final double crownSize = tileCenter - 10.0;
				final Polygon kingPieceIcon = new Polygon();
				kingPieceIcon.setFill(Color.valueOf("#FFD700"));
				kingPieceIcon.getPoints().addAll(new Double[]{
							    tileCenter - (crownSize / 2), tileCenter - (crownSize / 2),
							    tileCenter - (crownSize / 4), tileCenter,
							    tileCenter, tileCenter - (crownSize / 2),
							    tileCenter + (crownSize / 4), tileCenter,
							    tileCenter + (crownSize / 2), tileCenter - (crownSize / 2),
							    tileCenter + (crownSize / 4), tileCenter + (crownSize / 2),
							    tileCenter - (crownSize / 4), tileCenter + (crownSize / 2)
							});
				this.getChildren().add(kingPieceIcon);
			}
		}
	}

}
package dama.model.board;

import dama.model.Alliance;
import dama.model.pieces.Piece;
import dama.model.pieces.Dama;
import dama.model.player.Player;
import dama.model.player.WhitePlayer;
import dama.model.player.BlackPlayer;

import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableList;

import java.util.*;

public final class Board {

	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;

	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;

	private Board(final Builder builder) {
		this.gameBoard = createGameBoard(builder);
		this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
		this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);

		final Collection<Move> whiteStandardLegalMove = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandardLegalMove = calculateLegalMoves(this.blackPieces);

		this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMove);
		this.blackPlayer = new BlackPlayer(this, blackStandardLegalMove);
		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
	}
	
	public Tile getTile(final int tileCoordinate) {
		return this.gameBoard.get(tileCoordinate);
	}

	private static List<Tile> createGameBoard(final Builder builder) {
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];

		for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}

		return ImmutableList.copyOf(tiles);
	}

	@Override
	public String toString(){
		final StringBuilder builder = new StringBuilder();

		for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
			final String tileText = this.gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
				builder.append("\n");
			}
		}

		return builder.toString();
	}

	public Player getCurrentPlayer() {
		return this.currentPlayer;		
	}

	public Player getPlayer(final Alliance alliance) {
		return alliance.isBlack() ? this.blackPlayer : this.whitePlayer;
	}

	public Collection<Piece> getActivePieces(final Alliance alliance) {
		return alliance.isBlack() ? this.blackPieces : this.whitePieces;
	}

	private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
		final List<Piece> activePieces = new ArrayList<>();

		for(final Tile tile : gameBoard) {
			if(tile.isTileOccupied()) {
				final Piece piece = tile.getPiece();
				if(piece.getPieceAlliance() == alliance) {
					activePieces.add(piece);
				}
			}
		}

		return ImmutableList.copyOf(activePieces);
	}

	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
		final List<Move> legalMoves = new ArrayList<>();

		for(final Piece piece : pieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}

		return ImmutableList.copyOf(legalMoves);
	}

	public Iterable<Move> getAllLegalMoves() {
		return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
	}

	public static Board createStandardBoard() {
		final Builder builder = new Builder();

		// Black Layout
		builder.setPiece(new Dama(1, Alliance.BLACK));
		builder.setPiece(new Dama(3, Alliance.BLACK));
		builder.setPiece(new Dama(5, Alliance.BLACK));
		builder.setPiece(new Dama(7, Alliance.BLACK));
		builder.setPiece(new Dama(8, Alliance.BLACK));
		builder.setPiece(new Dama(10, Alliance.BLACK));
		builder.setPiece(new Dama(12, Alliance.BLACK));
		builder.setPiece(new Dama(14, Alliance.BLACK));
		builder.setPiece(new Dama(17, Alliance.BLACK));
		builder.setPiece(new Dama(19, Alliance.BLACK));
		builder.setPiece(new Dama(21, Alliance.BLACK));
		builder.setPiece(new Dama(23, Alliance.BLACK));

		// White Layout
		builder.setPiece(new Dama(40, Alliance.WHITE));
		builder.setPiece(new Dama(42, Alliance.WHITE));
		builder.setPiece(new Dama(44, Alliance.WHITE));
		builder.setPiece(new Dama(46, Alliance.WHITE));
		builder.setPiece(new Dama(49, Alliance.WHITE));
		builder.setPiece(new Dama(51, Alliance.WHITE));
		builder.setPiece(new Dama(53, Alliance.WHITE));
		builder.setPiece(new Dama(55, Alliance.WHITE));
		builder.setPiece(new Dama(56, Alliance.WHITE));
		builder.setPiece(new Dama(58, Alliance.WHITE));
		builder.setPiece(new Dama(60, Alliance.WHITE));
		builder.setPiece(new Dama(62, Alliance.WHITE));

		builder.setMoveMaker(Alliance.WHITE);

		return builder.build();
	}

	public static class Builder {

		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;

		public Builder() {
			this.boardConfig = new HashMap<>();
		}

		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}

		public Builder setMoveMaker(final Alliance nextMoveMaker) {
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}

		public Board build() {
			return new Board(this);
		}
	}
}
package dama.model;

import dama.model.player.Player;
import dama.model.player.WhitePlayer;
import dama.model.player.BlackPlayer;
import dama.model.board.BoardUtils;

public enum Alliance {

	WHITE {
		@Override
		public int getDirections() {
			return -1;
		}

		@Override
		public boolean isWhite() {
			return true;
		}

		@Override
		public boolean isBlack() {
			return false;
		}

		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer,
								   final BlackPlayer blackPlayer) {
			return whitePlayer;
		}

		@Override
		public String toString() {
			return "WHITE";
		}

		@Override
		public Alliance opposite() {
			return BLACK;
		}

		@Override
		public boolean isDamaPromotionTile(int position) {
			return BoardUtils.FIRST_ROW.get(position);
		}
	},

	BLACK {
		@Override
		public int getDirections() {
			return 1;
		}

		@Override
		public boolean isWhite() {
			return false;
		}

		@Override
		public boolean isBlack() {
			return true;
		}

		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer,
								   final BlackPlayer blackPlayer) {
			return blackPlayer;
		}

		@Override
		public String toString() {
			return "BLACK";
		}

		@Override
		public Alliance opposite() {
			return WHITE;
		}

		@Override
		public boolean isDamaPromotionTile(int position) {
			return BoardUtils.LAST_ROW.get(position);
		}
	};

	public abstract int getDirections();
	public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
    public abstract String allianceToString();
    public abstract Alliance opposite();
    public abstract boolean isDamaPromotionTile(int position);
}
package dama.model;

import dama.model.player.Player;
import dama.model.player.WhitePlayer;
import dama.model.player.BlackPlayer;

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
		public String allianceToString() {
			return "WHITE";
		}

		@Override
		public Alliance opposite() {
			return BLACK;
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
		public String allianceToString() {
			return "BLACK";
		}

		@Override
		public Alliance opposite() {
			return WHITE;
		}
	};

	public abstract int getDirections();
	public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
    public abstract String allianceToString();
    public abstract Alliance opposite();
}
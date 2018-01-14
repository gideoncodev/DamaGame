package dama;

import dama.model.board.Board;

public class DamaGame {

	public static void main(String[] args) {
		Board board = Board.createStandardBoard();

		System.out.println(board);
	}

}
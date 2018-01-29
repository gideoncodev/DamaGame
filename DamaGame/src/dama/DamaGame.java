package dama;

import dama.model.board.Board;
import dama.view.GameBoard;

import javax.swing.*;

public class DamaGame {

	public static void main(String[] args) {
		Board board = Board.createStandardBoard();

		System.out.println(board);

		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GameBoard();
            }
        });
	}

}
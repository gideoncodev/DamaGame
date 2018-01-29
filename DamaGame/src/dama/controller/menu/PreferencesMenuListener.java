package dama.controller.menu;

import dama.view.GameBoard;
import dama.view.GameBoard.*;

import java.awt.event.*;
import javax.swing.*;

public class PreferencesMenuListener {
	public static class FlipBoardListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final DamaMenuItem flipBoardMenu = (DamaMenuItem) e.getSource();
			flipBoardMenu.setParentBoardDirection(flipBoardMenu.getParentBoardDirection().opposite());
			flipBoardMenu.getParentBoardPanel().drawBoard(flipBoardMenu.getParentDamaBoard());
			System.out.println("Click the Flip Board Menu Item!");
		}
	}
}
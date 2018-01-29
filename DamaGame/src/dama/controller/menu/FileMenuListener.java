package dama.controller.menu;

import dama.view.GameBoard;
import dama.view.GameBoard.*;

import java.awt.event.*;
import javax.swing.*;

public class FileMenuListener {
	public static class OpenListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Click the Open Menu Item!");
		}
	}

	public static class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final DamaMenuItem exitMenu = (DamaMenuItem) e.getSource();
			exitMenu.getParentFrame().dispose();
			System.out.println("Click the Exit Menu Item!");
			System.exit(0);
		}
	}
}
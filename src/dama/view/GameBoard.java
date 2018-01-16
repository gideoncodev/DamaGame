package dama.view;

import java.awt.*;
import javax.swing.*;
import dama.controller.FileMenuListener;

public class GameBoard {

	private final JFrame gameFrame;

	private static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);

	public GameBoard() {

		final JMenuBar damaMenuBar = new JMenuBar();

		this.gameFrame = new JFrame("Dama");
		this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.populateMenuBar(damaMenuBar);
		this.gameFrame.setJMenuBar(damaMenuBar);
		this.gameFrame.pack();
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.gameFrame.setVisible(true);
	}

	private void populateMenuBar(JMenuBar menuBar) {
		menuBar.add(this.createFileMenu());
	}

	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openFile = new JMenuItem("Open");

		openFile.addActionListener(new FileMenuListener());

		fileMenu.add(openFile);

		return fileMenu;
	}
}
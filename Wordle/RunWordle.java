package Wordle;

import java.awt.*;
import javax.swing.*;
public class RunWordle implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Wordle");
        frame.setLocation(0, 0);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        control_panel.add(undo);

        final JFrame framePop = new JFrame("Instructions");

        // pop up
        framePop.setSize(400, 600);

        JLabel lHow = new JLabel("How To Play: ");
        lHow.setHorizontalAlignment(SwingConstants.LEFT);
        JTextArea textHow = new JTextArea(
                """
                        Guess the word in 6 tries\s
                        You can enter the words using your computer keyboard.
                        Each guess must be a valid 5 letter word.
                        The color of the tiles will show how close
                        Green means the letter is in the
                        word and in the correct spot
                        Yellow means the letter is in the word
                        but in the wrong spot
                        Gray means the letter is not in the word."""
        );

        JPanel p = new JPanel();

        p.setBackground(Color.WHITE);
        p.add(lHow);
        p.add(textHow);

        framePop.add(p);
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        framePop.show();

        // Start the game
        board.loadGame();
    }
}
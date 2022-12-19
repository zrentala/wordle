package Wordle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameBoard extends JPanel {
    private final Wordle wordle; // model for the game
    private final JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 500; // will change later
    public static final int BOARD_HEIGHT = 600;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        wordle = new Wordle(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                Box[][] b = wordle.getBoard();
                int row = wordle.getRow();
                int col = wordle.getCol();
                int keyCode = e.getKeyCode();
                if (!(row >= wordle.getNumTurns()) && col <= b[row].length && col >= 0) {
                    if (keyCode == KeyEvent.VK_ENTER && wordle.getCol() == 5) {
                        wordle.checkRound(b[wordle.getRow()]);
                    } else if (keyCode == KeyEvent.VK_BACK_SPACE /*
                                                                  * || keyCode == KeyEvent.VK_DELETE
                                                                  */) {
                        // check if position works
                        wordle.deleteLetter();
                        // System.out.println(wordle.getCol() + " delete");
                    } else {
                        // checks if ASCII
                        char c = e.getKeyChar();
                        if (c >= 'a' && c <= 'z') {
                            // check if box works or not
                            wordle.inputLetter(c);
                        }
                    }
                }
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        wordle.reset();
        status.setText("Turn 1/6");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Loads the game to saved state.
     */
    public void loadGame() {
        wordle.loadGame();
        status.setText("Turn " + wordle.getRow() + "/6");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void undo() {
        if (wordle.getRow() >= wordle.getNumTurns()) {
            status.setText("Game is finished. Cannot undo. Reset to play again");
            repaint();
        } else if (wordle.getRow() != 0 && !wordle.isWin()) {
            wordle.undo();
            status.setText("Turn " + wordle.getRow() + "/6");
            repaint();
        } else {
            status.setText("Cannot undo");
            repaint();
        }

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (wordle.isWin()) {
            status.setText("You win! Reset to play again.");
        } else if (wordle.getRow() == wordle.getNumTurns()) {
            char[] target = wordle.getTarget();
            StringBuilder result = new StringBuilder();
            for (char c : target) {
                result.append(c);
            }
            status.setText(
                    "You lose! The word was " + result + ". Reset to play again."
            );
        } else {
            switch (wordle.getRow()) {
                case 1:
                    status.setText("Turn 1/6");
                    break;
                case 2:
                    status.setText("Turn 2/6");
                    break;
                case 3:
                    status.setText("Turn 3/6");
                    break;
                case 4:
                    status.setText("Turn 4/6");
                    break;
                case 5:
                    status.setText("Turn 5/6");
                    break;
                case 6:
                    status.setText("Turn 6/6");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Draws the game board.
     *
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Box[][] board = wordle.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Color[] colorArray = setColor(board[i][j].getState());
                drawBox(g, colorArray, board[i][j].getLetter(), j * 100 + 10, i * 100 + 10);
            }
        }
    }

    /**
     * helper function to draw each box in the baord
     * 
     * @param g          graphics context
     * @param colorArray color of the box to be drawn based on its state
     * @param input      letter of the box
     * @param posX       X position in canvas
     * @param posY       Y position in canvas
     */
    private static void drawBox(Graphics g, Color[] colorArray, char input, int posX, int posY) {
        g.setColor(colorArray[1]);
        g.fillRect(posX, posY, 80, 80);
        g.setColor(colorArray[0]);
        g.drawRect(posX, posY, 80, 80);
        g.setColor(colorArray[2]);
        g.setFont(new Font("HelveticaNeue", Font.PLAIN, 50));
        String toDraw = String.valueOf(input);
        if (input == '0') {
            toDraw = "";
        }
        g.drawString(toDraw, posX + 27, posY + 57);
    }

    // sets state and colors
    private static Color[] setColor(int state) {
        Color[] colorArray = new Color[3];// order: border, fill, letter color
        Color yellow = new Color(200,182,83);
        Color gray = new Color(120,124,127);
        Color green = new Color(108,169,101);
        switch (state) {
            case 0:
                colorArray[0] = gray;
                colorArray[1] = Color.WHITE;
                colorArray[2] = Color.BLACK;
                break;
            case 1:
                colorArray[0] = gray;
                colorArray[1] = gray;
                colorArray[2] = Color.WHITE;
                break;
            case 2:
                colorArray[0] = yellow;
                colorArray[1] = yellow;
                colorArray[2] = Color.WHITE;
                break;
            case 3:
                colorArray[0] = green;
                colorArray[1] = green;
                colorArray[2] = Color.WHITE;
                break;
            default:
                break;
        }
        return colorArray;
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

}

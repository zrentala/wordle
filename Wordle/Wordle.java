package Wordle;

import java.io.*;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Wordle {
    // FIELDS
    private Box[][] board;
    private int row;
    private int col;
    private int numTurns;
    private boolean win;
    private char[] target;
    private TreeMap<Character, Integer> freq; // accounts for multiple letters in one word
    private Set<String> words = new TreeSet<>();
    private static final String WORD_FILE = "files\\valid-wordle-words.txt";
    private String saveFile = "files\\save_wordle.txt";

    // CONSTRUCTOR AND GAME SET UP
    /**
     * Constructor sets up game state.
     */
    public Wordle() {
        // reset();
        // this will be changed later if debugging
        loadGame();

    }

    /**
     * FOR DEBUGGING PURPOSES!!!
     * Constructor sets up game state, with fixed word and control over whether to
     * reload game or reset
     */
    public Wordle(String fixed, boolean load) {
        if (load) {
            this.saveFile = "files\\save_game_fixed.txt";
            loadGame();
        } else {
            resetFixedWord(fixed);
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        this.row = 0;
        this.col = 0;
        this.numTurns = 6;
        this.win = false;
        this.words = loadWords(
                new File(
                        "files\\sgb-words.txt"
                )
        );
        this.target = stringToCharArray(this.chooseWord()); // WE WILL CHANGE LATER
        this.freq = letterFreq(target);
        this.board = createBoard();
        saveGame();
    }

    /**
     * FOR DEBUGGING PURPOSES!!!
     * reset (re-)sets the game state to start a new game with a fixed letter
     */
    public void resetFixedWord(String fixed) {
        this.row = 0;
        this.col = 0;
        this.numTurns = 6;
        this.win = false;
        this.words = loadWords(new File(WORD_FILE));
        this.target = stringToCharArray(fixed);
        this.freq = letterFreq(target);
        this.board = createBoard();
        this.saveFile = "files\\save_game_fixed.txt";
        saveGame();
    }

    /**
     * loads game by reading txt file and assigning fields to Wordle class
     * How fields are ordered in txt file:
     * row, col, numTurns, win, words, target, freq (letter then frequency), board
     * (letter then state)
     */
    public void loadGame() {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(saveFile)
            );
            this.row = Integer.parseInt(br.readLine());
            this.col = Integer.parseInt(br.readLine());
            this.numTurns = Integer.parseInt(br.readLine());
            String winS = br.readLine();
            this.win = !winS.equals("0");
            this.words = loadWords(new File(WORD_FILE));
            this.target = stringToCharArray(br.readLine());
            // resets frequency
            this.freq = new TreeMap<>();

            // recreate frequency based on
            for (char c : target) {
                char[] curr = stringToCharArray(br.readLine());
                String freqS = "" + curr[1];
                this.freq.put(c, Integer.parseInt(freqS));
            }

            // recreate Board
            Box[][] loadB = createBoard();
            for (Box[] boxes : loadB) {
                for (Box box : boxes) {
                    char[] curr = stringToCharArray(br.readLine());
                    box.setLetter(curr[0]);
                    String state = "" + curr[1];
                    box.setState(Integer.parseInt(state));
                }
            }
            this.board = loadB;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * saves game by writing txt file by fields in the Wordle class
     * How fields are ordered in txt file:
     * row, col, numTurns, win, words, target, freq (letter then frequency), board
     * (letter then state)
     */
    public void saveGame() {
        try {
            BufferedWriter bw = (new BufferedWriter(
                    new FileWriter(saveFile)
            ));
            // write fields
            bw.write(this.row + '0');
            bw.newLine();
            bw.write('0');
            bw.newLine();
            bw.write(this.numTurns + '0');
            bw.newLine();
            if (this.win) {
                bw.write(1 + '0');
            } else {
                bw.write('0');
            }
            bw.newLine();
            bw.write(target);
            bw.newLine();

            // write frequency
            for (char c : target) {
                bw.write(c);
                bw.write(this.freq.get(c) + '0');
                bw.newLine();
            }

            // write board
            for (Box[] boxes : board) {
                for (Box b : boxes) {
                    bw.write(b.getLetter());
                    bw.write(b.getState() + '0');
                    bw.newLine();
                }
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CONSTRUCTOR HELPERS
    /**
     * creates a default board where all boxes have state 0 and no letters
     * 
     * @return Box[][] of a default board
     */
    private Box[][] createBoard() {
        Box[][] result = new Box[numTurns][5];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = new Box(0, '0');
            }
        }

        return result;
    }

    /**
     * reads a file that holds all the valid input words and target words
     * 
     * @param input File to be read
     * @return set of Strings of valid words, will become words Set
     */
    public Set<String> loadWords(File input) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(input));
            Set<String> result = new TreeSet<>();
            String curr = br.readLine();
            while (curr != null) {
                result.add(curr);
                curr = br.readLine();
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * helper function that chooses word that will be the target from the words Set
     * 
     * @return String of the target word
     */
    private String chooseWord() {
        int rand = (int) (Math.random() * this.words.size());
        Iterator<String> wordsIter = words.iterator();
        String result = "";
        for (int i = 0; i < this.words.size(); i++) {
            String curr = wordsIter.next();
            if (i == rand) {
                result = curr;
                break;
            }
        }
        return result;
    }

    /**
     * creates a TreeMap that maps the target word's letters to their frequency
     * 
     * @param input char array that represents target word
     * @return TreeMap that will be the freq field
     */
    public static TreeMap<Character, Integer> letterFreq(char[] input) {
        TreeMap<Character, Integer> result = new TreeMap<>();
        for (char c : input) {
            if (result.containsKey(c)) {
                result.put(c, result.get(c) + 1);
            } else {
                result.put(c, 1);
            }
        }
        return result;
    }

    // CHANGES GAME STATE AND CHECKS WIN STATE
    /**
     * inputLetter updates the next box in the board to input the letter the user
     * 
     * @param input the letter to be inputted into the board, pre-checked to be
     *              ASCII
     *
     */
    public void inputLetter(char input) {
        if (!this.win) {
            if (col + 1 <= board[row].length) { // check to see if you are at the end of the array
                board[row][col].setLetter(input);
                col++;
            } else {
                board[row][board[row].length - 1].setLetter(input);
            }
        }
    }

    /**
     * deleteLetter updates the previous box in the board to delete its letter (by
     * replacing it with '0')
     *
     */
    public void deleteLetter() {
        if (!this.win) {
            if (col == 0) { // check to see if you are at the beginning of the array
                board[row][col].setLetter('0');
            } else {
                col--;
                board[row][col].setLetter('0');
            }
        }
    }

    /**
     * checkRound compares the user's input word to the target word and updates the
     * board accordingly.
     * Steps:
     * 1) check if length of input and length of target word is the same
     * 2) checks if valid word in words Set, if it is not a valid word, deletes the
     * input word from the board
     * 3) compares input and target words, update states of Boxes, this will be done
     * in passes because if you check each condition all at once, a bug occurs where
     * in a word like target word lemur, the word enter would have states of
     * e2n1t1(e2)r3 where it should be e2n1t(1(e1)r3, since there is only 1 'e'.
     * 3a) sets all Boxes to 1
     * 3b) second pass, sets state to 3 (green) if letter is in target and in right
     * position
     * 3c) sets state to 2 (yellow) if letter is in word, but not already state 3
     * and frequency is not 0
     * 4) resets frequencies, increments current row, resets column to 0
     * 5) checks to see if you have won
     * 6) saves game
     * 
     * @param input Box array representing the current turn's array/word
     */
    public void checkRound(Box[] input) {
        // 1) checks length of input array
        int length = input.length;
        if (length != this.target.length) {
            throw new IllegalArgumentException();
        }

        // 2) checks if valid word in words Set, if not valid word, deletes input
        String s = boxArrayToString(input);
        if (!words.contains(s)) {
            deleteRow(input);
            this.col = 0;
            return;
        }

        // 3) compares input and target words, updates state of Boxes
        // 3a) first pass, set all Boxes' state to 1
        for (Box b : input) {
            b.setState(1);
        }

        // 3b) second pass, sets state to 3 (green) if letter is in target and in right
        // position
        for (int i = 0; i < length; i++) {
            Box b = input[i];
            char c = b.getLetter();
            if (c == target[i]) {
                b.setState(3);
                this.freq.put(c, this.freq.get(c) - 1);
            }
        }

        // 3c) sets state to 2 (yellow) if letter is in word, but not state 3 and
        // frequency is not 0
        for (Box b : input) {
            char c = b.getLetter();
            if (b.getState() != 3 && this.freq.containsKey(c) && this.freq.get(c) != 0) {
                b.setState(2);
                this.freq.put(c, this.freq.get(c) - 1);
            }
        }

        // 4) resets frequencies, increments current row, resets column to 0
        this.freq = letterFreq(target);
        row++;
        this.col = 0;

        // 5) checks to see if you have won
        this.win = this.checkWinner(input);

        // 6) saves game
        this.saveGame();
    }

    /**
     * checkWinner is a helper function that checks if the player has won after
     * inputting a valid letter
     * does so by checking if the state for any of the Boxes is not 3.
     * 
     * @param input Box[] representing input word
     * @return boolean on whether the player won
     */
    private boolean checkWinner(Box[] input) {
        for (Box b : input) {
            if (b.getState() != 3) {
                return false;
            }
        }
        return true;
    }

    /**
     * undoes the previous turn by "erasing" the letters of the previous turn
     * does so by calling deleteRow on the input row (if the user has inputted any
     * letters)
     * and on the previously submitted row. Decrements row and resets column to 0.
     */
    public void undo() {
        boolean hasLetters = false;
        for (Box b : board[row]) {
            if (b.getLetter() != '0') {
                hasLetters = true;
                break;
            }
        }
        if (hasLetters) {
            deleteRow(board[row]);
        }
        deleteRow(board[row - 1]);
        col = 0;
        row--;
        saveGame();
    }

    /**
     * deleteRow is a helper function that deletes row desired by resetting Boxes to
     * default state
     * 
     * @param input Box array representing row to be deleted
     */
    private void deleteRow(Box[] input) {
        for (Box b : input) {
            b.setState(0);
            b.setLetter('0');
        }
    }

    // STATIC HELPERS
    /**
     * converts a String to a char array
     * 
     * @param input String to be converted
     * @return char array that was converted
     */
    public static char[] stringToCharArray(String input) {
        int length = input.length();
        char[] result = new char[length];
        for (int i = 0; i < length; i++) {
            result[i] = input.charAt(i);
        }
        return result;
    }

    /**
     * Converts a Box array to a string, will be used in word verification
     * 
     * @param input Box array of the word to be verified
     * @return String of Box[]'s word
     */
    private static String boxArrayToString(Box[] input) {
        StringBuilder result = new StringBuilder();
        for (Box box : input) {
            result.append(box.getLetter());
        }
        return result.toString();
    }

    // GETTERS
    public Box[][] getBoard() {
        return board;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getNumTurns() {
        return numTurns;
    }

    public boolean isWin() {
        return win;
    }

    public char[] getTarget() {
        return target;
    }

    public TreeMap<Character, Integer> getFreq() {
        return freq;
    }

    public Set<String> getWords() {
        return words;
    }

    // MAIN FUNCTION + MAIN HELPERS
    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + row + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j].getLetter() + " " + board[i][j].getState());
                if (j < board[i].length) {
                    System.out.print(" | ");
                }
            }
            if (i < numTurns) {
                System.out.println("\n---------");
            }
        }
    }

    public static void main(String[] args) {
        Wordle w = new Wordle();
        w.reset();

        w.inputLetter('u');
        w.inputLetter('r');
        w.inputLetter('g');
        w.inputLetter('e');
        w.inputLetter('s');
        Box[][] b = w.getBoard();
        w.checkRound(b[0]);
        w.printGameState();

        w.inputLetter('i');
        w.inputLetter('l');
        w.inputLetter('l');
        w.inputLetter('e');
        w.inputLetter('d');
        w.checkRound(b[1]);
        w.printGameState();

        w.inputLetter('y');
        w.deleteLetter();
        w.inputLetter('t');
        w.inputLetter('e');
        w.inputLetter('l');
        w.inputLetter('l');
        w.inputLetter('s');
        w.checkRound(b[2]);
        w.printGameState();

        w.inputLetter('u');
        w.inputLetter('r');
        w.inputLetter('g');
        w.inputLetter('e');
        w.inputLetter('s');
        w.checkRound(b[3]);
        w.printGameState();

        w.inputLetter('u');
        w.inputLetter('r');
        w.inputLetter('g');
        w.inputLetter('e');
        w.inputLetter('s');
        w.checkRound(b[4]);
        w.printGameState();

        w.inputLetter('i');
        w.inputLetter('n');
        w.inputLetter('p');
        w.inputLetter('u');
        w.inputLetter('t');
        w.checkRound(b[5]);

        w.printGameState();
        if (w.win) {
            System.out.println("you win");
        } else {
            System.out.println("you lose");
        }
    }
}

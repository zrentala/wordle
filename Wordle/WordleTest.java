package Wordle;

import org.junit.jupiter.api.*;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class WordleTest {
    // stringToStringArray tests
    @Test
    public void testEmptyToCharArray() {
        String c = "";
        char[] expected = {};
        char[] actual = Wordle.stringToCharArray(c);
        assertEquals(0, actual.length);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testOneCharToStringArray() {
        String c = "a";
        char[] expected = { 'a' };
        char[] actual = Wordle.stringToCharArray(c);
        assertEquals(1, actual.length);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFiveCharsToStringArray() {
        String s = "abcde";
        char[] expected = { 'a', 'b', 'c', 'd', 'e' };
        char[] actual = Wordle.stringToCharArray(s);
        assertEquals(5, actual.length);
        assertArrayEquals(expected, actual);
    }

    // letterFreq tests
    @Test
    public void testEmptyLetterFreq() {
        char[] input = Wordle.stringToCharArray("");
        TreeMap<Character, Integer> expected = new TreeMap<>();
        assertEquals(expected, Wordle.letterFreq(input));
    }

    @Test
    public void testUniqueLettersLetterFreq() {
        char[] input = Wordle.stringToCharArray("input");
        TreeMap<Character, Integer> expected = new TreeMap<>();
        for (char c : input) {
            expected.put(c, 1);
        }
        assertEquals(expected, Wordle.letterFreq(input));
    }

    @Test
    public void testDupLettersLetterFreq() {
        char[] input = Wordle.stringToCharArray("beret");
        TreeMap<Character, Integer> expected = new TreeMap<>();
        expected.put('b', 1);
        expected.put('e', 2);
        expected.put('r', 1);
        expected.put('t', 1);
        assertEquals(expected, Wordle.letterFreq(input));
    }

    // Constructor/reset test, I am using a fixed word for this
    @Test
    public void testConstructor() {
        Wordle wordle = new Wordle("input", false);
        Box[][] b = wordle.getBoard();
        // checks if board is created correctly, by checking fields of each Box
        for (Box[] boxes : b) {
            for (Box box : boxes) {
                assertEquals('0', box.getLetter());
                assertEquals(0, box.getState());
            }
        }
        assertEquals(0, wordle.getRow());
        assertEquals(0, wordle.getCol());
        assertEquals(6, wordle.getNumTurns());
        String expectedTarg = "input";
        char[] actualTarg = wordle.getTarget();
        assertFalse(wordle.isWin());
        for (int i = 0; i < actualTarg.length; i++) {
            assertEquals(expectedTarg.charAt(i), actualTarg[i]);
        }
        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertTrue(wordle.getWords().size() > 0);
    }

    // inputLetter tests
    @Test
    public void testInputLetterOneTime() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('i');
        Box[][] b = wordle.getBoard();
        assertEquals(1, wordle.getCol());
        assertEquals(0, wordle.getRow());
        assertEquals('i', b[0][0].getLetter());
        assertEquals(0, b[0][0].getState());
    }

    @Test
    public void testInputLetterMultipleTimes() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('i');
        wordle.inputLetter('b');
        wordle.inputLetter('c');
        Box[][] b = wordle.getBoard();
        assertEquals(3, wordle.getCol());
        assertEquals(0, wordle.getRow());
        assertEquals('i', b[0][0].getLetter());
        assertEquals(0, b[0][0].getState());
        assertEquals('b', b[0][1].getLetter());
        assertEquals(0, b[0][1].getState());
        assertEquals('c', b[0][2].getLetter());
        assertEquals(0, b[0][2].getState());
    }

    @Test
    public void testInputLetterAtEndOfLine() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('i');
        wordle.inputLetter('b');
        wordle.inputLetter('c');
        wordle.inputLetter('d');
        wordle.inputLetter('e');
        wordle.inputLetter('f');
        Box[][] b = wordle.getBoard();
        assertEquals(5, wordle.getCol());
        assertEquals(0, wordle.getRow());
        assertEquals('i', b[0][0].getLetter());
        assertEquals(0, b[0][0].getState());
        assertEquals('b', b[0][1].getLetter());
        assertEquals(0, b[0][1].getState());
        assertEquals('c', b[0][2].getLetter());
        assertEquals(0, b[0][2].getState());
        assertEquals('d', b[0][3].getLetter());
        assertEquals(0, b[0][3].getState());
        assertEquals('f', b[0][4].getLetter());
        assertEquals(0, b[0][4].getState());
    }

    @Test
    public void testInputLetterWinIsTrue() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('i');
        wordle.inputLetter('n');
        wordle.inputLetter('p');
        wordle.inputLetter('u');
        wordle.inputLetter('t');
        Box[][] b = wordle.getBoard();
        assertEquals(5, wordle.getCol());
        assertEquals(0, wordle.getRow());
        wordle.checkRound(b[0]);
        assertTrue(wordle.isWin());
        wordle.inputLetter('i');
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());
        assertEquals('0', b[1][0].getLetter());
        assertEquals(0, b[1][0].getState());
    }

    // deleteLetter test
    @Test
    public void testDeleteLetterOnce() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('i');
        wordle.inputLetter('b');
        wordle.inputLetter('c');
        wordle.inputLetter('d');
        wordle.deleteLetter();
        Box[][] b = wordle.getBoard();
        assertEquals(3, wordle.getCol());
        assertEquals(0, wordle.getRow());
        assertEquals('i', b[0][0].getLetter());
        assertEquals(0, b[0][0].getState());
        assertEquals('b', b[0][1].getLetter());
        assertEquals(0, b[0][1].getState());
        assertEquals('c', b[0][2].getLetter());
        assertEquals(0, b[0][2].getState());
        assertEquals('0', b[0][3].getLetter());
        assertEquals(0, b[0][3].getState());
        assertEquals('0', b[0][4].getLetter());
        assertEquals(0, b[0][4].getState());
    }

    @Test
    public void testDeleteLetterAtEnd() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('i');
        wordle.inputLetter('b');
        wordle.inputLetter('c');
        wordle.inputLetter('d');
        wordle.inputLetter('e');
        wordle.deleteLetter();
        Box[][] b = wordle.getBoard();
        assertEquals(4, wordle.getCol());
        assertEquals(0, wordle.getRow());
        assertEquals('i', b[0][0].getLetter());
        assertEquals(0, b[0][0].getState());
        assertEquals('b', b[0][1].getLetter());
        assertEquals(0, b[0][1].getState());
        assertEquals('c', b[0][2].getLetter());
        assertEquals(0, b[0][2].getState());
        assertEquals('d', b[0][3].getLetter());
        assertEquals(0, b[0][3].getState());
        assertEquals('0', b[0][4].getLetter());
        assertEquals(0, b[0][4].getState());
    }

    @Test
    public void testDeleteLetterAtBeginning() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('i');
        wordle.deleteLetter();
        Box[][] b = wordle.getBoard();
        assertEquals(0, wordle.getCol());
        assertEquals(0, wordle.getRow());
        assertEquals('0', b[0][0].getLetter());
        assertEquals(0, b[0][0].getState());
    }

    // checkRound tests
    @Test
    public void testCheckRoundInvalidWord() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('a');
        wordle.inputLetter('b');
        wordle.inputLetter('c');
        wordle.inputLetter('d');
        wordle.inputLetter('e');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        for (Box b : board[0]) {
            assertEquals(0, b.getState());
        }
        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(0, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    @Test
    public void testCheckRoundInvalidLength() {
        Wordle wordle = new Wordle("input", false);
        Box[] line = new Box[6];
        assertThrows(IllegalArgumentException.class, () -> wordle.checkRound(line));
    }

    @Test
    public void testCheckRoundNoneCorrect() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('b');
        wordle.inputLetter('l');
        wordle.inputLetter('a');
        wordle.inputLetter('c');
        wordle.inputLetter('k');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        for (Box b : board[0]) {
            assertEquals(1, b.getState());
        }
        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    @Test
    public void testCheckRoundInCorrectSpot() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('k');
        wordle.inputLetter('n');
        wordle.inputLetter('a');
        wordle.inputLetter('c');
        wordle.inputLetter('k');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(1, board[0][0].getState());
        assertEquals(3, board[0][1].getState());
        assertEquals(1, board[0][2].getState());
        assertEquals(1, board[0][3].getState());
        assertEquals(1, board[0][4].getState());

        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    @Test
    public void testCheckRoundCorrectLetterWrongSpot() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('t');
        wordle.inputLetter('e');
        wordle.inputLetter('a');
        wordle.inputLetter('s');
        wordle.inputLetter('e');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(2, board[0][0].getState());
        assertEquals(1, board[0][1].getState());
        assertEquals(1, board[0][2].getState());
        assertEquals(1, board[0][3].getState());
        assertEquals(1, board[0][4].getState());

        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    @Test
    public void testCheckRoundCorrect() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('i');
        wordle.inputLetter('n');
        wordle.inputLetter('p');
        wordle.inputLetter('u');
        wordle.inputLetter('t');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(3, board[0][0].getState());
        assertEquals(3, board[0][1].getState());
        assertEquals(3, board[0][2].getState());
        assertEquals(3, board[0][3].getState());
        assertEquals(3, board[0][4].getState());

        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());
        assertTrue(wordle.isWin());
    }

    @Test
    public void testCheckRoundMultipleSameInputLetterWrongPlace() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('c');
        wordle.inputLetter('h');
        wordle.inputLetter('i');
        wordle.inputLetter('l');
        wordle.inputLetter('i');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(1, board[0][0].getState());
        assertEquals(1, board[0][1].getState());
        assertEquals(2, board[0][2].getState());
        assertEquals(1, board[0][3].getState());
        assertEquals(1, board[0][4].getState());

        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    @Test
    public void testCheckRoundMultipleSameInputLetterCorrect() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('i');
        wordle.inputLetter('c');
        wordle.inputLetter('i');
        wordle.inputLetter('e');
        wordle.inputLetter('r');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(3, board[0][0].getState());
        assertEquals(1, board[0][1].getState());
        assertEquals(1, board[0][2].getState());
        assertEquals(1, board[0][3].getState());
        assertEquals(1, board[0][4].getState());

        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    @Test
    public void testCheckRoundMultipleSameInputLetterSecondCorrect() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('p');
        wordle.inputLetter('u');
        wordle.inputLetter('p');
        wordle.inputLetter('p');
        wordle.inputLetter('y');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(1, board[0][0].getState());
        assertEquals(2, board[0][1].getState());
        assertEquals(3, board[0][2].getState());
        assertEquals(1, board[0][3].getState());
        assertEquals(1, board[0][4].getState());

        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    @Test
    public void testCheckRoundDuplicateLettersOneCorrectOneWrongPlace() {
        Wordle wordle = new Wordle("enter", false);
        wordle.inputLetter('b');
        wordle.inputLetter('e');
        wordle.inputLetter('v');
        wordle.inputLetter('e');
        wordle.inputLetter('l');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(1, board[0][0].getState());
        assertEquals(2, board[0][1].getState());
        assertEquals(1, board[0][2].getState());
        assertEquals(3, board[0][3].getState());
        assertEquals(1, board[0][4].getState());

        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('e', 2);
        expectedMap.put('n', 1);
        expectedMap.put('r', 1);
        expectedMap.put('t', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    @Test
    public void testCheckRoundDuplicateLettersOneCorrectBothCorrect() {
        Wordle wordle = new Wordle("enter", false);
        wordle.inputLetter('e');
        wordle.inputLetter('a');
        wordle.inputLetter('s');
        wordle.inputLetter('e');
        wordle.inputLetter('l');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(3, board[0][0].getState());
        assertEquals(1, board[0][1].getState());
        assertEquals(1, board[0][2].getState());
        assertEquals(3, board[0][3].getState());
        assertEquals(1, board[0][4].getState());

        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('e', 2);
        expectedMap.put('n', 1);
        expectedMap.put('r', 1);
        expectedMap.put('t', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    @Test
    public void testCheckRoundDuplicateLettersOneCorrectBothWrongPlace() {
        Wordle wordle = new Wordle("enter", false);
        wordle.inputLetter('s');
        wordle.inputLetter('e');
        wordle.inputLetter('e');
        wordle.inputLetter('m');
        wordle.inputLetter('s');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(1, board[0][0].getState());
        assertEquals(2, board[0][1].getState());
        assertEquals(2, board[0][2].getState());
        assertEquals(1, board[0][3].getState());
        assertEquals(1, board[0][4].getState());

        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('e', 2);
        expectedMap.put('n', 1);
        expectedMap.put('r', 1);
        expectedMap.put('t', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    // these tests test the frequency map I have to make sure the frequencies
    // correctly reset every turn

    @Test
    public void testCheckRoundMultipleTimesNoneCorrect() {
        Wordle wordle = new Wordle("enter", false);
        wordle.inputLetter('l');
        wordle.inputLetter('i');
        wordle.inputLetter('c');
        wordle.inputLetter('k');
        wordle.inputLetter('s');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);

        wordle.inputLetter('f');
        wordle.inputLetter('i');
        wordle.inputLetter('l');
        wordle.inputLetter('c');
        wordle.inputLetter('h');
        wordle.checkRound(board[1]);
        assertEquals(1, board[1][0].getState());
        assertEquals(1, board[1][1].getState());
        assertEquals(1, board[1][2].getState());
        assertEquals(1, board[1][3].getState());
        assertEquals(1, board[1][4].getState());

        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('e', 2);
        expectedMap.put('n', 1);
        expectedMap.put('r', 1);
        expectedMap.put('t', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(2, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    @Test
    public void testCheckRoundMultipleTimesOverlappingCorrectLetters() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('i');
        wordle.inputLetter('r');
        wordle.inputLetter('a');
        wordle.inputLetter('t');
        wordle.inputLetter('e');
        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);

        wordle.inputLetter('f');
        wordle.inputLetter('a');
        wordle.inputLetter('u');
        wordle.inputLetter('l');
        wordle.inputLetter('t');
        wordle.checkRound(board[1]);
        assertEquals(1, board[1][0].getState());
        assertEquals(1, board[1][1].getState());
        assertEquals(2, board[1][2].getState());
        assertEquals(1, board[1][3].getState());
        assertEquals(3, board[1][4].getState());

        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertEquals(0, wordle.getCol());
        assertEquals(2, wordle.getRow());
        assertFalse(wordle.isWin());
    }

    // undo tests
    @Test
    public void testUndoOneTime() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('e');
        wordle.inputLetter('n');
        wordle.inputLetter('t');
        wordle.inputLetter('e');
        wordle.inputLetter('r');

        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());

        wordle.undo();
        assertEquals(0, wordle.getCol());
        assertEquals(0, wordle.getRow());

        assertEquals(0, board[0][0].getState());
        assertEquals(0, board[0][1].getState());
        assertEquals(0, board[0][2].getState());
        assertEquals(0, board[0][3].getState());
        assertEquals(0, board[0][4].getState());
    }

    @Test
    public void testUndoWithLettersInNextInput() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('e');
        wordle.inputLetter('n');
        wordle.inputLetter('t');
        wordle.inputLetter('e');
        wordle.inputLetter('r');

        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());

        wordle.inputLetter('e');
        wordle.inputLetter('n');
        wordle.inputLetter('t');

        wordle.undo();
        assertEquals(0, wordle.getCol());
        assertEquals(0, wordle.getRow());

        assertEquals(0, board[0][0].getState());
        assertEquals(0, board[0][1].getState());
        assertEquals(0, board[0][2].getState());
        assertEquals(0, board[0][3].getState());
        assertEquals(0, board[0][4].getState());
    }

    @Test
    public void testUndoTwice() {
        Wordle wordle = new Wordle("input", false);
        wordle.inputLetter('e');
        wordle.inputLetter('n');
        wordle.inputLetter('t');
        wordle.inputLetter('e');
        wordle.inputLetter('r');

        Box[][] board = wordle.getBoard();
        wordle.checkRound(board[0]);
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());

        wordle.inputLetter('e');
        wordle.inputLetter('n');
        wordle.inputLetter('t');
        wordle.inputLetter('e');
        wordle.inputLetter('r');
        wordle.checkRound(board[1]);

        wordle.undo();
        assertEquals(0, wordle.getCol());
        assertEquals(1, wordle.getRow());

        assertEquals(0, board[1][0].getState());
        assertEquals(0, board[1][1].getState());
        assertEquals(0, board[1][2].getState());
        assertEquals(0, board[1][3].getState());
        assertEquals(0, board[1][4].getState());

        wordle.undo();
        assertEquals(0, wordle.getCol());
        assertEquals(0, wordle.getRow());

        assertEquals(0, board[0][0].getState());
        assertEquals(0, board[0][1].getState());
        assertEquals(0, board[0][2].getState());
        assertEquals(0, board[0][3].getState());
        assertEquals(0, board[0][4].getState());
    }

    // saveGame and loadGame test, can only test them together
    @Test
    public void testIOOneLine() {
        Wordle wordle1 = new Wordle("input", false);
        wordle1.inputLetter('p');
        wordle1.inputLetter('r');
        wordle1.inputLetter('i');
        wordle1.inputLetter('n');
        wordle1.inputLetter('t');

        Box[][] board1 = wordle1.getBoard();
        // calls saveGame
        wordle1.checkRound(board1[0]);

        // creates new game
        Wordle wordle = new Wordle("input", true);

        Box[][] board = wordle.getBoard();

        // checks first line
        assertEquals(2, board[0][0].getState());
        assertEquals(1, board[0][1].getState());
        assertEquals(2, board[0][2].getState());
        assertEquals(2, board[0][3].getState());
        assertEquals(3, board[0][4].getState());

        assertEquals('p', board[0][0].getLetter());
        assertEquals('r', board[0][1].getLetter());
        assertEquals('i', board[0][2].getLetter());
        assertEquals('n', board[0][3].getLetter());
        assertEquals('t', board[0][4].getLetter());

        // checks rest of board
        for (int i = 1; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                assertEquals(0, board[i][j].getState());
                assertEquals('0', board[i][j].getLetter());
            }
        }

        assertEquals(1, wordle.getRow());
        assertEquals(0, wordle.getCol());
        assertEquals(6, wordle.getNumTurns());
        String expectedTarg = "input";
        char[] actualTarg = wordle.getTarget();
        assertFalse(wordle.isWin());
        for (int i = 0; i < actualTarg.length; i++) {
            assertEquals(expectedTarg.charAt(i), actualTarg[i]);
        }
        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertTrue(wordle.getWords().size() > 0);

    }

    @Test
    public void testIOMultipleLines() {

        Wordle wordle1 = new Wordle("input", false);
        wordle1.inputLetter('p');
        wordle1.inputLetter('r');
        wordle1.inputLetter('i');
        wordle1.inputLetter('n');
        wordle1.inputLetter('t');

        Box[][] board1 = wordle1.getBoard();
        // calls saveGame
        wordle1.checkRound(board1[0]);

        wordle1.inputLetter('t');
        wordle1.inputLetter('e');
        wordle1.inputLetter('n');
        wordle1.inputLetter('t');
        wordle1.inputLetter('s');
        wordle1.checkRound(board1[1]);

        // creates new game
        Wordle wordle = new Wordle("input", true);

        Box[][] board = wordle.getBoard();

        // checks first line
        assertEquals(2, board[0][0].getState());
        assertEquals(1, board[0][1].getState());
        assertEquals(2, board[0][2].getState());
        assertEquals(2, board[0][3].getState());
        assertEquals(3, board[0][4].getState());

        assertEquals('p', board[0][0].getLetter());
        assertEquals('r', board[0][1].getLetter());
        assertEquals('i', board[0][2].getLetter());
        assertEquals('n', board[0][3].getLetter());
        assertEquals('t', board[0][4].getLetter());

        // checks second line
        assertEquals(2, board[1][0].getState());
        assertEquals(1, board[1][1].getState());
        assertEquals(2, board[1][2].getState());
        assertEquals(1, board[1][3].getState());
        assertEquals(1, board[1][4].getState());

        assertEquals('t', board[1][0].getLetter());
        assertEquals('e', board[1][1].getLetter());
        assertEquals('n', board[1][2].getLetter());
        assertEquals('t', board[1][3].getLetter());
        assertEquals('s', board[1][4].getLetter());

        // checks rest of board
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                assertEquals(0, board[i][j].getState());
                assertEquals('0', board[i][j].getLetter());
            }
        }

        assertEquals(2, wordle.getRow());
        assertEquals(0, wordle.getCol());
        assertEquals(6, wordle.getNumTurns());
        String expectedTarg = "input";
        char[] actualTarg = wordle.getTarget();
        assertFalse(wordle.isWin());
        for (int i = 0; i < actualTarg.length; i++) {
            assertEquals(expectedTarg.charAt(i), actualTarg[i]);
        }
        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertTrue(wordle.getWords().size() > 0);

    }

    @Test
    public void testIOLoadInMiddleOfRound() {
        Wordle wordle1 = new Wordle("input", false);
        wordle1.inputLetter('p');
        wordle1.inputLetter('r');
        wordle1.inputLetter('i');
        wordle1.inputLetter('n');
        wordle1.inputLetter('t');

        Box[][] board1 = wordle1.getBoard();
        // calls saveGame
        wordle1.checkRound(board1[0]);

        wordle1.inputLetter('t');
        wordle1.inputLetter('e');
        wordle1.inputLetter('n');
        wordle1.checkRound(board1[1]);

        // creates new game
        Wordle wordle = new Wordle("input", true);

        Box[][] board = wordle.getBoard();

        // checks first line
        assertEquals(2, board[0][0].getState());
        assertEquals(1, board[0][1].getState());
        assertEquals(2, board[0][2].getState());
        assertEquals(2, board[0][3].getState());
        assertEquals(3, board[0][4].getState());

        assertEquals('p', board[0][0].getLetter());
        assertEquals('r', board[0][1].getLetter());
        assertEquals('i', board[0][2].getLetter());
        assertEquals('n', board[0][3].getLetter());
        assertEquals('t', board[0][4].getLetter());

        // checks second line
        assertEquals(0, board[1][0].getState());
        assertEquals(0, board[1][1].getState());
        assertEquals(0, board[1][2].getState());
        assertEquals(0, board[1][1].getState());
        assertEquals(0, board[1][2].getState());

        assertEquals('0', board[1][0].getLetter());
        assertEquals('0', board[1][1].getLetter());
        assertEquals('0', board[1][2].getLetter());
        assertEquals('0', board[1][3].getLetter());
        assertEquals('0', board[1][4].getLetter());

        // checks rest of board
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                assertEquals(0, board[i][j].getState());
                assertEquals('0', board[i][j].getLetter());
            }
        }

        assertEquals(1, wordle.getRow());
        assertEquals(0, wordle.getCol());
        assertEquals(6, wordle.getNumTurns());
        String expectedTarg = "input";
        char[] actualTarg = wordle.getTarget();
        assertFalse(wordle.isWin());
        for (int i = 0; i < actualTarg.length; i++) {
            assertEquals(expectedTarg.charAt(i), actualTarg[i]);
        }
        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertTrue(wordle.getWords().size() > 0);

    }

    @Test
    public void testIOWins() {
        Wordle wordle1 = new Wordle("input", false);
        wordle1.inputLetter('i');
        wordle1.inputLetter('n');
        wordle1.inputLetter('p');
        wordle1.inputLetter('u');
        wordle1.inputLetter('t');

        Box[][] board1 = wordle1.getBoard();
        // calls saveGame
        wordle1.checkRound(board1[0]);

        // creates new game
        Wordle wordle = new Wordle("input", true);

        Box[][] board = wordle.getBoard();

        // checks first line
        assertEquals(3, board[0][0].getState());
        assertEquals(3, board[0][1].getState());
        assertEquals(3, board[0][2].getState());
        assertEquals(3, board[0][3].getState());
        assertEquals(3, board[0][4].getState());

        assertEquals('i', board[0][0].getLetter());
        assertEquals('n', board[0][1].getLetter());
        assertEquals('p', board[0][2].getLetter());
        assertEquals('u', board[0][3].getLetter());
        assertEquals('t', board[0][4].getLetter());

        // checks rest of board
        for (int i = 1; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                assertEquals(0, board[i][j].getState());
                assertEquals('0', board[i][j].getLetter());
            }
        }

        assertEquals(1, wordle.getRow());
        assertEquals(0, wordle.getCol());
        assertEquals(6, wordle.getNumTurns());
        String expectedTarg = "input";
        char[] actualTarg = wordle.getTarget();
        assertTrue(wordle.isWin());
        for (int i = 0; i < actualTarg.length; i++) {
            assertEquals(expectedTarg.charAt(i), actualTarg[i]);
        }
        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertTrue(wordle.getWords().size() > 0);

    }

    @Test
    public void testIOLoadContinuePlaying() {
        Wordle wordle1 = new Wordle("input", false);
        wordle1.inputLetter('p');
        wordle1.inputLetter('r');
        wordle1.inputLetter('i');
        wordle1.inputLetter('n');
        wordle1.inputLetter('t');

        Box[][] board1 = wordle1.getBoard();
        // calls saveGame
        wordle1.checkRound(board1[0]);
        Wordle wordle = new Wordle("input", true);

        assertEquals(1, wordle.getRow());
        assertEquals(0, wordle.getCol());

        wordle.inputLetter('t');
        wordle.inputLetter('e');
        wordle.inputLetter('n');

        Box[][] board = wordle.getBoard();

        // checks first line
        assertEquals(2, board[0][0].getState());
        assertEquals(1, board[0][1].getState());
        assertEquals(2, board[0][2].getState());
        assertEquals(2, board[0][3].getState());
        assertEquals(3, board[0][4].getState());

        assertEquals('p', board[0][0].getLetter());
        assertEquals('r', board[0][1].getLetter());
        assertEquals('i', board[0][2].getLetter());
        assertEquals('n', board[0][3].getLetter());
        assertEquals('t', board[0][4].getLetter());

        // checks second line
        assertEquals(0, board[1][0].getState());
        assertEquals(0, board[1][1].getState());
        assertEquals(0, board[1][2].getState());
        assertEquals(0, board[1][1].getState());
        assertEquals(0, board[1][2].getState());

        assertEquals('t', board[1][0].getLetter());
        assertEquals('e', board[1][1].getLetter());
        assertEquals('n', board[1][2].getLetter());
        assertEquals('0', board[1][3].getLetter());
        assertEquals('0', board[1][4].getLetter());

        // checks rest of board
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                assertEquals(0, board[i][j].getState());
                assertEquals('0', board[i][j].getLetter());
            }
        }

        assertEquals(1, wordle.getRow());
        assertEquals(3, wordle.getCol());
        assertEquals(6, wordle.getNumTurns());
        String expectedTarg = "input";
        char[] actualTarg = wordle.getTarget();
        assertFalse(wordle.isWin());
        for (int i = 0; i < actualTarg.length; i++) {
            assertEquals(expectedTarg.charAt(i), actualTarg[i]);
        }
        TreeMap<Character, Integer> expectedMap = new TreeMap<>();
        expectedMap.put('i', 1);
        expectedMap.put('n', 1);
        expectedMap.put('p', 1);
        expectedMap.put('t', 1);
        expectedMap.put('u', 1);
        assertEquals(expectedMap, wordle.getFreq());
        assertTrue(wordle.getWords().size() > 0);

    }

}

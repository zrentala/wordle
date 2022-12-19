package Wordle;

public class Box {
    private char letter;
    private int state; // 0 not inputted, 1 incorrect, 2 correct letter wrong spot, 3 correct
                       // position

    public Box(int state, char letter) {
        this.letter = letter;
        this.state = state;
    }

    // getters and setters
    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}

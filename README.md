Wordle Project

My word list is from: https://gist.github.com/dracos/dd0668f281e685bad51479e5acaadb93

How to Play:

Implementation:

2D Array:
I used 2D arrays to implement the board and store the game state. I used a 2D of my custom class Box which stored a letter and a state for each
position in the game. A more in depth description of Box can be found below. A 2D array was used because we know from the start how many letters long each word is
and how many turns each game had from the beginning, even if I designed the game so that those variables are modifiable (which I decided to not implement). My Box 
class was used as the 2D array's type because it ensured that only 1 letter and state could be stored in 1 position in the array. Using the 2D Array, I implemented
methods to input and delete letters from the board; a method to compare a user's input word to the target word;
and a method to undo the user's recent turn. Each of these methods are appropriate for 2D arrays because they take advantage of the finite, pre-determined size of 
the array. I split the player's turn into 2 phases: input letter phase, and check word phase. I also kept track of the current row and column of the board in my Class Wordle's
fields. For the input and delete letters methods, I iterate through columns of the current row, once they get to the last column of the row they can check their word. 
For the checkRound and undo, I iterate through the columns. If the player guesses the right word or the last row is reached, the game ends with the user winning or losing.

Collections:
I used a TreeSet to model the collection of all of the valid words that can be a target or input word.
I used a Set because the items are stored without order and without duplicates and the Set of words should be accessed without order and should not have duplicates.
I use it to validate that the user's input word is in the Set. I also randomly chose the target word everytime reset() is called from this Set.

I also used a TreeMap<Character, Integer> that stores the frequency of each letter in the target word. This was done to deal with duplicate letters when checking the input word.
This is appropriate beause I need to map each character to its frequency and to store each character only once.
Logic: If the letter is in the target and not in the right position, the Box's state becomes 2 and the frequency for that letter is decremented/
If the letter is in the target and the right position, the Box's state becomes 3 and the frequency is decremented.
If the input letter is not in the TreeMap or the frequency is 0 (becomes the letter is a duplicate) then the Box's state is 1 and the frequency stays the same.
The frequency is reset after checking every input letter to not mess up the rest of the board's check.

File I/O:
After every input word check, I save the game's state to a txt file using BufferedWriter. I intentionally only saved the game after checking the word
because I think the game should be split up by turn. Whenever the game is turned off and runs again, the previous game will load so that the user can continue as 
if nothing ever happened. I saved the game by saving the fields of the game: the board (the exact state it is in after checking the input word), the current row, 
the current column (which should be 0), the maximum number of turns, the win state, and the target word. The frequency Map, the Set of words, and file locations
are recreate when loaded.

Classes:
Box: This is type in the 2D array used to store the letter and state of each position. The letter is a char and the state is the level of correctness that 
letter is compared to the target word: 0 not inputted, 1 incorrect, 2 correct letter wrong spot, 3 correct position. 
The state also determines the color of the box: 0: gray border, white fill, black text, 1: gray border and fill, white text. 2: yellow border and fill, white text,
3: green border and fill, white text. The Box class does not draw the Box. In the View (GameBoard), the Box is drawn, so none of the color data is stored
in the Box class.

Wordle: This is the Model of the game. It controls and creates the board, fields that keep track of the progression of the game, valid words, and how to save and load the game.
An entire game can be run in the main method without interacting with the UI elements.

GameBoard: This class stores the model as a field and acts as a controller and view to interact with the model.
Controller aspects: There is a Key Listener that listens for the user's keyboard inputs and inputs the associated letters into the model. The controller also
allows the user to delete letters, and enter their word to be checked against the target word. It also interacts with the top level elements such as undo, reset, and loadGame.
View Aspects: This class also draws the model and updates the status (turn #, and win state). paintComponent draws the model by drawing each Box by getting the letter and 
state of the Box and using it to draw the letter with the state's associated colors.

RunWordle: This class sets up the top-level frame and widgets for the GUI such as panels for buttons (reset, undo) (also acts as Controller), the status bar, and a 
pop up that describes the game, gives instructions, and cool stuff I implemented.

Game: Main method run to start and run the game.

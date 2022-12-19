=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: zrentala
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Array: I used 2D arrays to implement the board and store the game state. I used a 2D of my custom class Box which stored a letter and a state for each
position in the game. A more in depth description of Box can be found below. A 2D array was used because we know from the start how many letters long each word is
and how many turns each game had from the beginning, even if I designed the game so that those variables are modifiable (which I decided to not implement). My Box 
class was used as the 2D array's type because it ensured that only 1 letter and state could be stored in 1 position in the array. Using the 2D Array, I implemented
methods to input and delete letters from the board; a method to compare a user's input word to the target word;
and a method to undo the user's recent turn. Each of these methods are appropriate for 2D arrays because they take advantage of the finite, pre-determined size of 
the array. I split the player's turn into 2 phases: input letter phase, and check word phase. I also kept track of the current row and column of the board in my Class Wordle's
fields. For the input and delete letters methods, I iterate through columns of the current row, once they get to the last column of the row they can check their word. 
For the checkRound and undo, I iterate through the columns. If the player guesses the right word or the last row is reached, the game ends with the user winning or losing.

  2. Collections and Maps: I used a TreeSet to model the collection of all of the valid words that can be a target or input word.
I used a Set because the items are stored without order and without duplicates and the Set of words should be accessed without order and should not have duplicates.
I use it to validate that the user's input word is in the Set. I also randomly chose the target word everytime reset() is called from this Set.

I also used a TreeMap<Character, Integer> that stores the frequency of each letter in the target word. This was done to deal with duplicate letters when checking the input word.
This is appropriate beause I need to map each character to its frequency and to store each character only once.
Logic: If the letter is in the target and not in the right position, the Box's state becomes 2 and the frequency for that letter is decremented/
If the letter is in the target and the right position, the Box's state becomes 3 and the frequency is decremented.
If the input letter is not in the TreeMap or the frequency is 0 (becomes the letter is a duplicate) then the Box's state is 1 and the frequency stays the same.
The frequency is reset after checking every input letter to not mess up the rest of the board's check.

  3. File I/O: After every input word check, I save the game's state to a txt file using BufferedWriter. I intentionally only saved the game after checking the word
because I think the game should be split up by turn. Whenever the game is turned off and runs again, the previous game will load so that the user can continue as 
if nothing ever happened. I saved the game by saving the fields of the game: the board (the exact state it is in after checking the input word), the current row, 
the current column (which should be 0), the maximum number of turns, the win state, and the target word. The frequency Map, the Set of words, and file locations
are recreate when loaded.

  4. JUnit Testable Component: I made my model (Wordle class) JUnit testable by using getters and setters for my private fields so that they can be tested as the model updates.
I tested some helper methods, input and delete letters, the constructor, check round, undo, load, and save.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

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


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
I think the biggest design struggle was to decide whether to save the letters as a 1 char String where I maintain that invariant
or to save it as a char. I was going to save it as a String because you can have empty String but not an empty char so I was worried
to have NullPointerExceptions. I ended up saving the letter as a char and keeping empty letters '0', but to not draw '0' in the View.
Maintaining the invariant would be too time consuming and convoluted.

Another stumbling block was implemented inputLetter and deleteLetter and controlling the current column. I orginally set my column to be only be able to go from
0,1,2,3,4 but this caused issues where the column wouldn't update with the board correctly. Letting the column go up to 5 (even though it is out of bounds for the array)
solved this problem.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
I think there was good separartion of functionality. The model was completely separated from the view and the controller
in such a way that the model could play a game without interacting with the controller or view. The controller and view were separated into the 
top level that drew and controlled the frame, buttons, status, and pop ups, and lower level that drew the game state, and interacted with the model
to control the game state and status.

I also think the private state was well encapsulated since all of the fields were private and could only be accessed or manipulated through getters and setters.
While the getters could have created a copy of the fields, this would made giving inputs to the game very difficult and convoluted.

I think that I refactored my code fairly well while writing the game. I made helper functions anytime code was reused or if it would make the code easier to read.
========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
I did not use many resources outside of Office Hours besides Java's documentation of Swing, Collections, and tutorials on pop ups.

My word list is from: https://gist.github.com/dracos/dd0668f281e685bad51479e5acaadb93
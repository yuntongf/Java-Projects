/**********************************************************************
 * CIS 110 Project: Sudoku game
 **********************************************************************/

Name: Yuntong Fu
PennKey: yuntongf
Hours to complete assignment (optional): 12

/**********************************************************************
 *  Have you enter all help, collaboration, and outside resources
 *  in your help log?  If not, do so now.  (And in future, make sure
 *  you make your help log entries as you go, not at the end!)
 *
 *  If you did not get any help in outside of TA office hours,
 *  and did not use any materials outside of the standard
 *  course materials and piazza, write the following statement below:
 *  "I did not receive any help outside of TA office hours.  I
 *  did not collaborate with anyone, and I did not use any
 *  resources beyond the standard course materials."
 **********************************************************************/

I did not receive any help outside of TA office hours.  I
 did not collaborate with anyone, and I did not use any
 resources beyond the standard course materials.

/**********************************************************************
 *  Instructions for how to run my prorgram           
 **********************************************************************/
The class that needs to be run is Sudoku.java. 

The execution is Sudoku [puzzle text file name] [size].

For example, if I want to run a 9*9 sudoku with the initial puzzle 
provided in the file sudokuExample.txt. The execution will be:

java Sudoku sudokuExample.txt 9

/**********************************************************************
 *  Additional features I added                                        
 **********************************************************************/
 I added a 6*6 option. To play, just enter in the command line the text file
 name and the size 6.
 
 In my submission I included a file named sudokuExampleSix.txt, which contains a 
 6*6 sudoku puzzle. To play, enter in the command line:

 java Sudoku sudokuExampleSix.txt 6

 Inaddition, I also implemented a clear bar feature. To use the feature, the 
 player simply clicks on the clear bar at the bottom right of the game board, 
 and the game board will return to its initial state before there were any user 
 input.

/**********************************************************************
 *  A brief description of each file and its purpose                                       
 **********************************************************************/
Cell.java: the lower level class that contains fields and methods of individual
cells, which is the fundamental building block of this game. For example, to paint
a whole row, the higher level class Board will call the paint method in Cell class
for each cell in that row.

Board.java: the organizing class for the game. It puts together a 2D array of cells
and one of its main functions is to handle input by determining if an input is 
contradictory to any values in the row, column and box it belongs to. This class 
calls functions from the Cell class and organizes them into methods on rows,
columns, boxes. (For example, the paintRow method). It contains most methods 
that will be called in the Sudoku class.

Sudoku.java: the highest level class that mainly parses in values from command 
line arguments and manages the state of the game based on user input from mouse 
and keyboard.

I don't know if this will be helpful, but I included some files just so that 
the TA can use the files to test features of the game such as 6*6 sudoku and 
printing victory message:

SudokuExampleSix.txt: an example text file that contains a 6*6 sudoku puzzle.

testVictory.txt: a text file that helps test printing the victory message.


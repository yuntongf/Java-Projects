/*
* Name: Yuntong Fu
* PennKey: yuntongf
*
* Execution: java Sudoku [puzzle text file name] [size] (for example if the puzzle 
is 6*6 the int size would be 6)
*
*/
public class Sudoku {
    /**
    * Inputs: a string representing the filename of the text file that contains 
    * the inital values and and int indicating the size of the puzzle 
    * Outputs: a 2D int array that contains given values and values to be filled 
    * by the player (with temporary value 10)
    * Description: read values from the puzzle file that contains all given 
    * values and returns an int 2D array
    */
    public static int[][] readPuzzle(String file, int size) {
        // read puzzle text file
        In inStream = new In(file);
        String input = inStream.readAll();
        
        // check value and report invalid input
        int countRow = 1;
        int countCol = 0;
        for (int i = 0; i < input.length(); i++) {
            char inputChar = input.charAt(i);
            if (inputChar != '\n') {
                // input int has to be 1-9 inclusive or blank or \n
                if (inputChar != '1' && inputChar != '2' && inputChar != '3' &&
                inputChar != '4' && inputChar != '5' && inputChar != '6' &&
                inputChar != '7' && inputChar != '8' &&
                inputChar != '9' && inputChar != ' ') {
                    throw new IllegalArgumentException("Invalid input!!");
                }
                countCol++;
                } else {
                // the number of rows has to equal the given size
                if (countCol != size) {
                    throw new IllegalArgumentException("Invalid number of " +
                    "columns!");
                }
                countRow++;
                countCol = 0;
            }
        }
        // the number of rows has to equal the given size
        if (countRow != size) {
            throw new IllegalArgumentException("Invalid number of rows!");
        }
        
        // read in values from the file to an int 2D array
        int[][] puzzle = new int[size][size];
        int row = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '\n') {
                row++;
                continue;
            }
            if (input.charAt(i) == ' ') { // replace blank temporarily with 10
                puzzle[row][i % (size + 1)] = 10; 
                } else {
                puzzle[row][i % (size + 1)] 
                = Character.getNumericValue(input.charAt(i));
            }
        }
        return puzzle;
    }
    
    public static void main(String[] args) {
        // reset scale to make it easier to work with the 2D array
        PennDraw.setXscale(0, 1);
        PennDraw.setYscale(1, 0);
        
        // parse in values from the txt file
        int size = Integer.parseInt(args[1]);
        int[][] puzzle = Sudoku.readPuzzle(args[0], size);

        Board b = new Board(size, puzzle);
        b.draw();
        
        // two modes to manage the state of the game
        boolean keyboardListening = false;
        boolean mouseListening = true;
        
        PennDraw.enableAnimation(50);

        Cell c = null;
        while (!b.checkSolved()) { // keep the game running until puzzle is solved
            /* As the game starts, the player will click on a cell. As soon as 
            mouse is clicked on a cell, the game transitions to keyboard 
            listening mode to wait for the player to input value through keyboard.*/
            if (PennDraw.mousePressed()) {
                mouseListening = false;
            }

            // check if the clear bar is clicked; if so, clear the game
            boolean clearClicked = b.clearClicked(PennDraw.mouseX(),
            PennDraw.mouseY());
            if (!mouseListening && clearClicked) {
                b.clear();
                keyboardListening = true;
                mouseListening = true;
            }

            if (!mouseListening) {
                b.redraw(); // redraw the game to clear any painted region
                c = b.currCell(PennDraw.mouseX(), PennDraw.mouseY());
                if (c != null) { // check the mouse is clicking on a cell
                    c.select();
                }
                keyboardListening = true;
                /* The game is mouse listening at the same time because the player 
                may want to change to select another cell to input values*/
                mouseListening = true;
            }
            
            /* In keyboard listening mode, we wait for the next keyboard input. As
            soon as a valid charracter input is typed we change the status*/
            if (keyboardListening && PennDraw.hasNextKeyTyped() &&
            c != null && !c.getFixed()) {
                char inputChar = PennDraw.nextKeyTyped();
                /* if the input int is not between 1-9 inclusive, draw error 
                message in red at the bottom of game board */
                if (inputChar != '1' && inputChar != '2' && inputChar != '3' &&
                inputChar != '4' && inputChar != '5' && inputChar != '6' &&
                inputChar != '7' && inputChar != '8' && inputChar != '9') {
                    PennDraw.setPenColor(PennDraw.RED);
                    PennDraw.text(b.getCells()[size / 2][size / 2].getX(),
                    b.getCells()[size - 1][size - 1].getY() + b.getCellWidth(), 
                    "Invalid keyboard input!");
                    PennDraw.setPenColor(PennDraw.BLACK);
                } else {
                    // if input is valid, draw the value and handle input
                    c.setVal(Character.getNumericValue(inputChar));
                    b.redraw();
                    b.handleInput(c);
                    /* the player cannot change the value through keyboard 
                    after one input*/
                    keyboardListening = false;
                }
            }
            PennDraw.advance();
        }
        PennDraw.disableAnimation();
        
        // if the game is solved, print vistory message
        b.victoryMessage();
    }
}

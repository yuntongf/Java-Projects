/*
* Name: Yuntong Fu
* PennKey: yuntongf
*
* Execution: N/A
* Description: this class constructs a sudoku board based on a given size and
* a puzzle. The class organizes cells (from the Cell class) into a game board
* and implements methods such as paintRow, mark problem value, currCell,
* checkSolved, victoryMessage etc. More detailed descriptions please see the
* class headers.
*
*/

public class Board {
    private int size;
    private Cell[][] cells;
    private boolean solved;
    private double cellWidth;
    
    public Board(int s, int[][] puzzle) {
        size = s;
        solved = false;
        cells = new Cell[size][size];
        cellWidth = 1.0 / (size + 2);
        
        // check if size if valid
        if (size % 3 != 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid size!");
        }
        
        // check if puzzle length fits the size of the game board
        if (puzzle.length != size || puzzle[0].length != size) {
            throw new IllegalArgumentException("The given puzzle does not fit" +
            " the size of game board!");
        }
        
        // check if the puzzle 2D array is ragged
        int length = puzzle[0].length;
        for (int j = 1; j < puzzle.length; j++) {
            if (puzzle[j].length != length) {
                throw new IllegalArgumentException("The puzzle needs to have" +
                "equal to the length for each row!");
            }
        }
        
        /* check if all values are between 1-9 inclusive or 10 (which represents
        missing vlaues) */
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (puzzle[i][j] != 1 && puzzle[i][j] != 2 && puzzle[i][j] != 3 &&
                puzzle[i][j] != 4 && puzzle[i][j] != 5 && puzzle[i][j] != 6 &&
                puzzle[i][j] != 7 && puzzle[i][j] != 8 && puzzle[i][j] != 9 &&
                puzzle[i][j] != 10) {
                    throw new IllegalArgumentException("argument must be + " +
                    "between 1-9 inclusive!");
                }
            }
        }
        
        // check if there are repeated values in any rows
        boolean noRepeatRow = true;
        for (int i = 0; i < size; i++) {
            int[] tempRow = puzzle[i];
            noRepeatRow = noRepeatRow && checkPuzzleHelper(tempRow);
        }
        if (!noRepeatRow) {
            throw new IllegalArgumentException("Puzzle cannot have repeated " +
            "values in the same row!");
        }
        
        // check if there are repeated values in any columns
        for (int i = 0; i < size; i++) {
            boolean noRepeatCol = true;
            int[] tempCol = new int[size];
            for (int j = 0; j < size; j++) {
                tempCol[j] = puzzle[i][j];
                noRepeatCol = noRepeatCol && checkPuzzleHelper(tempCol);
            }
            if (!noRepeatRow) {
                throw new IllegalArgumentException("Puzzle cannot have " +
                "repeated values in the same column!");
            }
        }
        
        // check if there are repeated values in any boxes
        
        /* first, we build a 2D array where each element is the box of its
        corresponding value in the puzzle 2D array */
        int[][] temp = new int[size][size];
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[0].length; j++) {
                /* give the cell a box value based on its columns and rows,
                based on the assumption that we have at most 9 boxes, which
                is valid because we need only consider 6*6 and 9*9 game board*/
                
                if (j >= 0 && j <= 2 && i >= 0 && i <= 2) {
                    temp[i][j] = 0;
                }
                
                if (j >= 3 && j <= 5 && i >= 0 && i <= 2) {
                    temp[i][j] = 1;
                }
                
                if (j >= 6 && j <= 8 && i >= 0 && i <= 2) {
                    temp[i][j] = 2;
                }
                
                if (j >= 0 && j <= 2 && i >= 3 && i <= 5) {
                    temp[i][j] = 3;
                }
                
                if (j >= 3 && j <= 5 && i >= 3 && i <= 5) {
                    temp[i][j] = 4;
                }
                
                if (j >= 6 && j <= 8 && i >= 3 && i <= 5) {
                    temp[i][j] = 5;
                }
                
                if (j >= 0 && j <= 2 && i >= 6 && i <= 8) {
                    temp[i][j] = 6;
                }
                
                if (j >= 3 && j <= 5 && i >= 6 && i <= 8) {
                    temp[i][j] = 7;
                }
                
                if (j >= 6 && j <= 8 && i >= 6 && i <= 8) {
                    temp[i][j] = 8;
                }
            }
        }
        
        // for 4*4 there are 2*2 boxes, for 9*9 there are 3*3 boxes
        for (int i = 0; i < (size / 3) * (size / 3); i++) {
            boolean noRepeatBox = true;
            int[] tempBox = new int[9];
            int index = 0;
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    // put all values in puzzle that have the same box in tempBox
                    if (temp[j][k] == i) {
                        tempBox[index] = puzzle[j][k];
                    }
                }
                noRepeatBox = noRepeatBox && checkPuzzleHelper(tempBox);
            }
            if (!noRepeatRow) {
                throw new IllegalArgumentException("Puzzle cannot have " +
                "repeated values in the same column!");
            }
        }
        
        // import values from int 2D array to cells 2D array
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i, j);
                cells[i][j].setCellWidth(cellWidth);
                cells[i][j].setVal(puzzle[i][j]);
                if (cells[i][j].getVal() != 10) {
                    cells[i][j].setCorrect(true);
                    cells[i][j].setFixed(true);
                }
            }
        }
    }

    /**
    * Inputs: N/A
    * Outputs: a int that is the size of the game board
    * Description: getter function for the size of a board
    */
    public int getSize() {
        return size;
    }

    /**
    * Inputs: an int representing the size of the game board
    * Outputs: N/A
    * Description: setter function for the size of the game board
    */
    public void setSize(int size) {
        this.size = size;
        return;
    }

    /**
    * Inputs: N/A
    * Outputs: a Cell 2D array representing all values on the board
    * Description: getter function for cells of a board
    */
    public Cell[][] getCells() {
        return cells;
    }

    /**
    * Inputs: a Cell 2D array representing all values on the board
    * Outputs: N/A
    * Description: setter function for the cells of the game board
    */
    public void setCells(Cell[][] cells) {
        this.cells = cells;
        return;
    }

    /**
    * Inputs: N/A
    * Outputs: a boolean that represents if the puzzle has been solved
    * Description: getter function for the field 'solved' of a board
    */
    public boolean getSolved() {
        return solved;
    }

    /**
    * Inputs: a boolean that represents if the puzzle has been solved
    * Outputs: N/A
    * Description: setter function for the field 'solved' of a board
    */
    public void setSolved(boolean solved) {
        this.solved = solved;
        return;
    }

    /**
    * Inputs: N/A
    * Outputs: a double that is the cellWidth
    * Description: getter function for the cellWidth
    */
    public double getCellWidth() {
        return cellWidth;
    }

    /**
    * Inputs: a double that is the cellWidth
    * Outputs: N/A
    * Description: setter function for the cellWidth
    */
    public void setCellWidth(double cellWidth) {
        this.cellWidth = cellWidth;
        return;
    }
    
    /**
    * Inputs: an int array representing an array of values from the game board
    * Outputs: a boolean showing if the array contains repeated value
    * Description: the function takes an int array and checks if there are any
    * repeated values in the array. This is a helper function to check if the
    * input puzzle is valid
    */
    private boolean checkPuzzleHelper(int[] intArray) {
        for (int i = 0; i < intArray.length; i++) {
            int temp = intArray[i];
            for (int j = 0; j < intArray.length; j++) {
                if (i != j) {
                    if (intArray[i] == intArray[j] &&
                    intArray[i] != 10) {
                        return false;
                    }
                }
            }
            
        }
        return true;
        
    }
    
    /**
    * Inputs: N/A
    * Outputs: no output as it is a void function, but draws the board including
    * all values in the cells on the PennDraw Canvas
    * Description: draws all cells on the PennDraw canvas and also draw the clear
    * bar
    */
    public void draw() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j].draw();
            }
        } 
        drawClearBar(); // draw the clear bar
    }
    
    /**
    * Inputs: an int representing the target row number
    * Outputs: no output as it is a void function but will paint all cells
    * in the given row yellow on the PennDraw canvas
    * Description: paint all cells in the given row yellow on the PennDraw canvas
    */
    public void paintRow(int row) {
        for (int i = 0; i < this.cells[0].length; i++) {
            this.cells[row][i].paint();
        }
    }
    
    /**
    * Inputs: an int representing the target column number
    * Outputs: no output as it is a void function but will paint all cells in
    * the given column yellow on the PennDraw canvas
    * Description: paint all cells in the given column yellow on the
    * PennDraw canvas
    */
    public void paintCol(int col) {
        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i][col].paint();
        }
    }
    
    /**
    * Inputs: an int representing the target box number
    * Outputs: no output as it is a void function but will paint all cells in
    * the given box yellow on the PennDraw canvas
    * Description: paint all cells in the given box yellow on the PennDraw canvas
    */
    public void paintBox(int box) {
        for (int i = 0; i < this.cells.length; i++) {
            for (int j = 0; j < this.cells[0].length; j++) {
                if (this.cells[i][j].getBox() == box) {
                    this.cells[i][j].paint();
                }
            }
        }
    }
    
    // given a list of cells, check if there are cells that have repeated values
    /**
    * Inputs: a Cell array
    * Outputs: a boolean representing if the input array contains cells that
    * have the same value
    * Description: given a list of cells, check if there are cells that have
    * repeated values; used as a helper function of handleInput() which ckecks
    * if a input does not violate the rule (no repeating values in row,
    * column and box)
    */
    private boolean handleInputHelper(Cell[] cellArray) {
        for (int i = 0; i < cellArray.length; i++) {
            Cell temp = cellArray[i];
            for (int j = 0; j < cellArray.length; j++) {
                if (i != j) {
                    if (cellArray[i].getVal() == cellArray[j].getVal() &&
                    cellArray[i].getVal() != 10) {
                        return false;
                    }
                }
            }
            
        }
        return true;
    }
    
    /**
    * Inputs: a Cell
    * Outputs: no output as it is a void function, but paint the row, column 
    * and box and have repeating values
    * Description: check if an input cell violates the the rule (no repeating
    * values in row, column and box). If so, paints the row, column, and box that
    * have the repeating value, and marks the contradictory values; uses the
    * helper function handleInputHelper()
    */
    public void handleInput(Cell c) {
        // check if there are repeated values in c's row
        Cell[] tempRow = this.cells[c.getRow()];
        boolean fitRow = handleInputHelper(tempRow);
        if (!fitRow) {
            this.paintRow(c.getRow());
            this.markProblemValue(tempRow, c);
        }
        
        // check if there are repeated values in c's col
        Cell[] tempCol = new Cell[size];
        for (int i = 0; i < size; i++) {
            tempCol[i] = cells[i][c.getCol()];
        }
        boolean fitCol = handleInputHelper(tempCol);
        if (!fitCol) {
            this.paintCol(c.getCol());
            this.markProblemValue(tempCol, c);
        }
        
        // check if there are repeated values in c's box
        Cell[] tempBox = new Cell[9];
        int index = 0;
        for (int i = 0; i < this.cells.length; i++) {
            for (int j = 0; j < this.cells[0].length; j++) {
                if (this.cells[i][j].getBox() == c.getBox()) {
                    tempBox[index] = this.cells[i][j];
                    index++;
                }
            }
        }
        boolean fitBox = handleInputHelper(tempBox);
        if (!fitBox) {
            this.paintBox(c.getBox());
            this.markProblemValue(tempBox, c);
        }
        
        // if fit all row, col, and box, the value is correct
        if (fitRow && fitCol && fitBox) {
            c.setCorrect(true);
            } else {
            // if input is wrong, paint the current cell red
            c.select();
            c.markValue();
        }
        
        return;
    }
    
    /**
    * Inputs: a Cell array, a cell
    * Outputs: no outputs as it is a void function, but marks the contradictory 
    * values red on the PennDraw canvas
    * Description: marks the contradictory values (repeating values) in the
    * given array red on the PennDraw canvas
    */
    public void markProblemValue(Cell[] cellArray, Cell c) {
        for (int i = 0; i < cellArray.length; i++) {
            if (cellArray[i].getVal() == c.getVal()) {
                cellArray[i].markValue();
            }
        }
    }
    
    /**
    * Inputs: coordinates of the mouse
    * Outputs: the current cell that the mouse is at
    * Description: pinpoint cell given mouse input coordinates
    */
    public Cell currCell(double x, double y) {
        int col = (int) (x / cellWidth) - 1;
        int row = (int) (y / cellWidth) - 1;
        
        if (row > 8 || row < 0 || col > 8 || col < 0) {
            return null; // return null if the mouse is clicked outside the cells
        }
        return cells[row][col];
    }
    
    /**
    * Inputs: N/A
    * Outputs: no outputs as it is a void function but draws a clear bar on 
    * PennDraw canvas
    * Description: drawa a gray clear bar at the bottom right corner of the
    * game board
    */
    public void drawClearBar() {
        // draw the bar in gray
        PennDraw.setPenColor(PennDraw.GRAY);
        PennDraw.filledRectangle(cells[size - 1][size - 1].getX(),
        this.cells[size - 1][size - 1].getY() + cellWidth, cellWidth,
        cellWidth / 4);
        PennDraw.setPenColor(PennDraw.BLACK);
        
        // draw text in the clear bar
        PennDraw.setPenColor(PennDraw.WHITE);
        PennDraw.text(cells[size - 1][size - 1].getX(),
        this.cells[size - 1][size - 1].getY() + cellWidth, "clear");
        PennDraw.setPenColor(PennDraw.BLACK);
    }
    
    /**
    * Inputs: x and y coordinates of the mouse
    * Outputs: a boolean representing if the mouse is in the clear bar
    * Description: determines if the mouse is curretnly at the clear bar
    */
    public boolean clearClicked(double x, double y) {
        return x < cells[size - 1][size - 1].getX() + cellWidth &&
        x > cells[size - 1][size - 1].getX() - cellWidth &&
        y > cells[size - 1][size - 1].getY() + cellWidth * 3 / 4 &&
        y < cells[size - 1][size - 1].getY() + cellWidth * 5 / 4;
    }
    
    /**
    * Inputs: N/A
    * Outputs: no output as it is a void function but clears everything
    * draws the game board again on the PennDraw canvas
    * Description: clears everything on the PennDraw canvas by covering
    * everything with a white square, and redraws the game board
    */
    public void redraw() {
        // clear the screen with square
        PennDraw.setPenColor(PennDraw.WHITE);
        PennDraw.filledSquare(0.5, 0.5, 0.5);
        PennDraw.setPenColor(PennDraw.BLACK);
        draw();
    }
    
    /**
    * Inputs: N/A
    * Outputs: turns the board to its initial state before the player played
    * Description: clear all history input and returns the game board to its
    * initial state; implemented when clear bar is clicked
    */
    public void clear() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (!cells[i][j].getFixed()) {
                    // change every non-fixed value to 10, denoting no value input
                    cells[i][j].setVal(10);
                }
            }
        }
        redraw();
    }
    
    //
    /**
    * Inputs: N/A
    * Outputs: a boolean that represents if the puzzle has been solved
    * Description: check if the puzzle has been solved by checking the
    * field 'correct' of each cell in the game board. If all values have
    * their correct evaluates to true, that means the puzzle has been solved
    */
    public boolean checkSolved() {
        boolean solved = true;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                // check if every cell has correct value
                solved = solved && cells[i][j].getCorrect();
            }
        }
        return solved;
    }
    
    // what happens after the winner has won?
    /**
    * Inputs: N/A
    * Outputs: no output as it is a void function, but draws a victory
    * message on the PennDraw canvas
    * Description: draws a victory message in the PennDraw canvas
    */
    public void victoryMessage() {
        PennDraw.setPenColor(PennDraw.PINK);
        PennDraw.filledRectangle(0.5, 0.5, 0.35, 0.1);
        PennDraw.setPenColor(PennDraw.WHITE);
        PennDraw.text(0.5, 0.5, "Congrats! You have solved the puzzle!");
    }
}

/*
* Name: Yuntong Fu
* PennKey: yuntongf
*
* Execution: N/A
* Description: This class constructs the basic cell; includes methods like
* initialize, wrongGuessColor
*
*/

public class Cell {
    //dividing by Board.size + 2 because we want to leave edges outside the grid
    private double cellWidth;
    private int col;
    private int row;
    private int box; // a box is a 3*3 arrangement of cells
    private int val; // value of the cell
    // whether the value of the cell is not contradictory with existing values
    private boolean correct; 
    private boolean fixed; // whether the cell's is given in the first place

    public Cell(int row, int col) {
        // check row and column number assuming we have at most 9 rows or columns
        if (row != 0 && row != 1 && row != 2 && row != 3 && row != 4 && row != 5 && 
        row != 6 && row != 7 && row != 8) {
            throw new IllegalArgumentException("Invalid row number!");
        }

        if (col != 0 && col != 1 && col != 2 && col != 3 && col != 4 && col != 5 && 
        col != 6 && col != 7 && col != 8) {
            throw new IllegalArgumentException("Invalid column number!");
        }

        this.col = col;
        this.row = row;

        /* give the cell a box value based on its columns and rows,
        based on the assumption that we have at most 9 boxes, which
        is valid because we need only consider 6*6 and 9*9 game board*/
        if (this.col >= 0 && this.col <= 2 && this.row >= 0 && this.row <= 2) {
            this.box = 0;
        }
        
        if (this.col >= 3 && this.col <= 5 && this.row >= 0 && this.row <= 2) {
            this.box = 1;
        }
        
        if (this.col >= 6 && this.col <= 8 && this.row >= 0 && this.row <= 2) {
            this.box = 2;
        }
        
        if (this.col >= 0 && this.col <= 2 && this.row >= 3 && this.row <= 5) {
            this.box = 3;
        }
        
        if (this.col >= 3 && this.col <= 5 && this.row >= 3 && this.row <= 5) {
            this.box = 4;
        }
        
        if (this.col >= 6 && this.col <= 8 && this.row >= 3 && this.row <= 5) {
            this.box = 5;
        }
        
        if (this.col >= 0 && this.col <= 2 && this.row >= 6 && this.row <= 8) {
            this.box = 6;
        }
        
        if (this.col >= 3 && this.col <= 5 && this.row >= 6 && this.row <= 8) {
            this.box = 7;
        }
        
        if (this.col >= 6 && this.col <= 8 && this.row >= 6 && this.row <= 8) {
            this.box = 8;
        }
        
        this.val = 0;
        this.correct = false;
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
    * Inputs: N/A
    * Outputs: an int representing the row number
    * Description: getter function for the row number
    */
    public int getRow() {
        return row;
    }

    /**
    * Inputs: an int representing the row number 
    * Outputs: N/A
    * Description: setter function for the row number
    */
    public void setRow(int row) {
        this.row = row;
        return;
    }

    /**
    * Inputs: N/A
    * Outputs: an int representing the column number
    * Description: getter function for the column number
    */
    public int getCol() {
        return col;
    }

    /**
    * Inputs: an int representing the column number 
    * Outputs: N/A
    * Description: setter function for the column number
    */
    public void setCol(int col) {
        this.col = col;
        return;
    }

    /**
    * Inputs: N/A
    * Outputs: an int representing the box number
    * Description: getter function for the box number
    */
    public int getBox() {
        return box;
    }

    /**
    * Inputs: an int representing the box number 
    * Outputs: N/A
    * Description: setter function for the box number
    */
    public void setBox(int box) {
        this.box = box;
        return;
    }

    /**
    * Inputs: N/A
    * Outputs: an int representing the value of the cell
    * Description: getter function for the value of the cell
    */
    public int getVal() {
        return val;
    }

    /**
    * Inputs: an int representing the value of the cell
    * Outputs: N/A
    * Description: setter function for the value of the cell
    */
    public void setVal(int val) {
        this.val = val;
        return;
    }

    /**
    * Inputs: N/A
    * Outputs: an int representing if the value of the cell is not contradictory
    * Description: getter function for the field 'correct' of the cell
    */
    public boolean getCorrect() {
        return correct;
    }

    /**
    * Inputs: a boolean representing if the value of the cell is not contradictory
    * Outputs: N/A
    * Description: setter function for the field 'correct' of the cell
    */
    public void setCorrect(boolean correct) {
        this.correct = correct;
        return;
    }
    
    /**
    * Inputs: N/A
    * Outputs: an int representing if the value of the cell is fixed 
    * Description: getter function for the field 'fixed' of the cell
    */
    public boolean getFixed() {
        return fixed;
    }

    /**
    * Inputs: a boolean representing if the value of the cell is fixed 
    * Outputs: N/A
    * Description: setter function for the field 'fixed' of the cell
    */
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
        return;
    }
    
    /**
    * Inputs: N/A
    * Outputs: a double that represents the x coordinate of the center of a cell
    * Description: get x coordinate of the center of a cell
    */
    public double getX() {
        double x = (cellWidth / 2) + (col + 1) * cellWidth;
        return x;
    }
    
    /**
    * Inputs: N/A
    * Outputs: a double that represents the y coordinate of the center of a cell
    * Description: get y coordinate of the center of a cell
    */
    public double getY() {
        double y = (cellWidth / 2) + (row + 1) * cellWidth;
        return y;
    }
    
    /**
    * Inputs: N/A
    * Outputs: no outputs in the terminal but draws value of cell and and
    * four edges around it in the PennDraw canvas
    * Description: draw edges of a cell and draw its value at the center of the
    * cell
    */
    public void draw() {
        // draw black edges around a cell
        PennDraw.line(getX() - (cellWidth / 2),
        getY() - (cellWidth / 2),
        getX() - (cellWidth / 2),
        getY() + (cellWidth / 2));
        
        PennDraw.line(getX() + (cellWidth / 2),
        getY() - (cellWidth / 2),
        getX() + (cellWidth / 2),
        getY() + (cellWidth / 2));
        
        PennDraw.line(getX() - (cellWidth / 2),
        getY() + (cellWidth / 2),
        getX() + (cellWidth / 2),
        getY() + (cellWidth / 2));
        
        PennDraw.line(getX() - (cellWidth / 2),
        getY() - (cellWidth / 2),
        getX() + (cellWidth / 2),
        getY() - (cellWidth / 2));
        
        // draw value in the cell
        if (val != 10) {
            String value = String.valueOf(val);
            PennDraw.text(this.getX(), this.getY(), value);
        }
    }
    
    /**
    * Inputs: N/A
    * Outputs: no outputs in the terminal, but will paint color in the cell
    * Description: paint a cell yellow while not covering its value
    */
    public void paint() {
        PennDraw.setPenColor(PennDraw.YELLOW);
        PennDraw.filledSquare(this.getX(), this.getY(), cellWidth / 2);
        
        // draw the number and the lines around cell again to avoid being covered
        PennDraw.setPenColor(PennDraw.BLACK);
        this.draw();
    }
    
    /**
    * Inputs: N/A
    * Outputs: no outputs in the terminal but marks the value of a cell red
    * on the PennDraw canvas
    * Description: marks the value of a cell red on the PennDraw canvas
    */
    public void markValue() {
        PennDraw.setPenColor(PennDraw.RED);
        if (val != 10) {
            String value = String.valueOf(val);
            PennDraw.text(this.getX(), this.getY(), value);
        }
        PennDraw.setPenColor(PennDraw.BLACK);
    }
    
    /**
    * Inputs: N/A
    * Outputs: no outputs in the terminal but paints the background of the
    * given selected cell light gray
    * Description: changes the color of the background of the given selected cell
    * to light gray
    */
    public void select() {
        if (!fixed) {
            PennDraw.setPenColor(211, 211, 211);
            PennDraw.filledSquare(this.getX(), this.getY(), cellWidth / 2);
            
            // draw the number and the lines around cell again
            PennDraw.setPenColor(PennDraw.BLACK);
            this.draw();
        }
    }
}

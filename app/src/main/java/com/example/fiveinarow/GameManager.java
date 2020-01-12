package com.example.fiveinarow;

/**
 * This game manager hold the logic of this game
 */
public class GameManager {

    public static int BOARD_SIZE = 13; //define the board size

    public static int NO_WINNER=0;
    public static int WHITE_WIN=1;
    public static int BLACK_WIN=2;
    public static int TIE = 3;


    private int[][] board; //hold the 2D array representation of the board
    private Stone[] stones;

    private int currentMove; //indicate which stone is going now: 1 for white, and 2 for black, start with white
    private boolean gameEnded; //true if game ended, false otherwise

    public GameManager() {
        gameEnded = false; //start new game
        //initialize a game board
        board = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i=0;i<GameManager.BOARD_SIZE;i++) {
            for (int j=0;j<GameManager.BOARD_SIZE;j++) {
                board[i][j]=0;
            }
        }
        //initialize the stone base on board
        this.stones = new Stone[BOARD_SIZE*BOARD_SIZE];
        for (int row = 0; row<BOARD_SIZE;row++) {
            for (int col=0;col<BOARD_SIZE;col++) {
                stones[row*BOARD_SIZE+col] = new Stone(board[row][col]);
            }
        }
        //white stone go first
        currentMove = Stone.WHITE;
    }

    /**
     * Get the current move
     * @return get the current move: 1 for white, and 2 for black
     */
    public int getCurrentMove() {
        return currentMove;
    }

    /**
     * Update the stones according to the board
     */
    private void updateStones() {
        for (int row = 0; row<BOARD_SIZE;row++) {
            for (int col=0;col<BOARD_SIZE;col++) {
                stones[row*BOARD_SIZE+col].setColor(board[row][col]);
            }
        }
    }

    /**
     * Get the stones array based on the board
     * @return stones array from the 2D array representation of the board
     */
    public Stone[] getStones() {
        return stones;
    }

    /**
     * update the board status
     * @param row the row index where the update is occurred
     * @param col the column index where the update is occurred
     */
    public void updateBoard(int row, int col) {
        //do nothing if the user clicked something already set or game ended
        if (board[row][col]!=0 || gameEnded) {
            return;
        }
        board[row][col] = currentMove; //update the targeted value
        //toggle the currentMove
        currentMove = (currentMove==Stone.WHITE) ? (Stone.BLACK) : (Stone.WHITE);
        updateStones(); //update the stones
    }

    /**
     * Reset the board into empty
     */
    public void resetBoard() {
        gameEnded = false; //restart the game
        currentMove = Stone.WHITE; //white go first
        for (int i=0;i<BOARD_SIZE;i++) {
            for (int j=0;j<BOARD_SIZE;j++) {
                board[i][j] = Stone.EMPTY; //set each item to empty
            }
        }
        updateStones(); //update the stones
    }

    /**
     * Check the 2D int array board to see whether it was NO_WINNER(0), WHITE_WIN(1), BLACK_WIN(2), or TIE(3)
     * @return NO_WINNER(0), WHITE_WIN(1), BLACK_WIN(2), or TIE(3)
     */
    public int checkWinner() {
        //run a for loop to check row, column, and right down and left down diagonal
        for (int i=0;i<GameManager.BOARD_SIZE-5;i++) {
            for (int j=0; j<GameManager.BOARD_SIZE-5;j++) {
                //check for row
                if (board[i][j]==board[i][j+1] && board[i][j+1]==board[i][j+2] && board[i][j+2]==board[i][j+3] && board[i][j+3]==board[i][j+4]) {
                    gameEnded= board[i][j]!=GameManager.NO_WINNER; //check whether game ended or not
                    if (gameEnded)
                        return board[i][j];
                }
                //check for column
                if (board[i][j]==board[i+1][j] && board[i+1][j]==board[i+2][j] && board[i+2][j]==board[i+3][j] && board[i+3][j]==board[i+4][j]) {
                    gameEnded= board[i][j]!=GameManager.NO_WINNER; //check whether game ended or not
                    if (gameEnded)
                        return board[i][j];
                }
                //check for right down diagonal
                if (board[i][j]==board[i+1][j+1] && board[i+1][j+1]==board[i+2][j+2] && board[i+2][j+2]==board[i+3][j+3] && board[i+3][j+3]==board[i+4][j+4]) {
                    gameEnded= board[i][j]!=GameManager.NO_WINNER; //check whether game ended or not
                    if (gameEnded)
                        return board[i][j];
                }
                //check for left down diagonal
                if (board[i][j+4]==board[i+1][j+3] && board[i+1][j+3]==board[i+2][j+2] && board[i+2][j+2]==board[i+3][j+1] && board[i+3][j+1]==board[i+4][j]) {
                    gameEnded= board[i][j+4]!=GameManager.NO_WINNER; //check whether game ended or not
                    if (gameEnded)
                        return board[i][j+4];
                }
            }
        }

        //check for TIE: count for white(13*13/2+1) and black(13*13/2)
        //loop to get the white and black stones count
        int whiteCount=0;
        int blackCount=0;
        for (int i=0;i<BOARD_SIZE;i++) {
            for (int j=0;j<BOARD_SIZE;j++) {
                if (board[i][j]==Stone.WHITE) { //count one more white stone
                    whiteCount++;
                }
                if (board[i][j]==Stone.BLACK) { //count one more black stone
                    blackCount++;
                }
            }
        }
        if (whiteCount == 3*13/2+1 && blackCount == 13*13/2) { //check for TIE situation
            gameEnded=true; //game ended
            return TIE;
        }

        //if none of the above happen, then no winner
        return NO_WINNER;
    }
}

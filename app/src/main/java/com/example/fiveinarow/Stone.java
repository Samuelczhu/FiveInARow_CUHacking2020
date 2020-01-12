package com.example.fiveinarow;

import com.example.fiveinarow.R;

/**
 * This class describe a stone object. The stone object can be empty, white, and black
 */
public class Stone {
    public static int EMPTY = 0;
    public static int WHITE = 1;
    public static int BLACK = 2;

    private int color;
    private int imageID;


    public Stone(int color){
        this.color = color;
        if (color == WHITE){
            imageID = R.drawable.cell_white;
        } else if (color == BLACK){
            imageID = R.drawable.cell_black;
        } else {
            imageID = R.drawable.cell_empty;
        }
    }

    //getters
    public int getColor() {
        return color;
    }

    public int getImageID() {
        return imageID;
    }

    //setter

    public void setColor(int color) {
        this.color = color;
        if (color == WHITE){
            imageID = R.drawable.cell_white;
        } else if (color == BLACK){
            imageID = R.drawable.cell_black;
        } else {
            imageID = R.drawable.cell_empty;
        }
    }
}

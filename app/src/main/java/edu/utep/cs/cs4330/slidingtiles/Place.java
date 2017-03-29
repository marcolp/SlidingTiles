package edu.utep.cs.cs4330.slidingtiles;

/**
 * Created by marcolopez on 2/2/17.
 */

public class Place {
    private int x;
    private int y;
    private int value;


    public Place(){
        this.x = -1;
        this.y = -1;
        this.value = -1;
    }

    public Place(int col, int row, int newValue){
        this.x = col;
        this.y = row;
        this.value = newValue;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

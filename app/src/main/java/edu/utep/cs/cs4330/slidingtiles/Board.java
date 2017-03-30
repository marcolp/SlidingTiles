package edu.utep.cs.cs4330.slidingtiles;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Marco Lopez
 * CS 5390 - Mobile Application Development
 * 2/14/2017
 *
 * A game board consisting of <code>size</code> * <code>size</code> places
 * where battleships can be placed. A place of the board is denoted
 * by a pair of 0-based indices (x, y), where x is a column index
 * and y is a row index. A place of the board can be shot at, resulting
 * in either a hit or miss.
 *
 * Implement Serializable to be able to pass through intent
 *
 * This is the model class in the MVC
 */
public class Board implements Parcelable{

    /**
     * Size of this board. This board has
     * <code>size*size </code> places.
     */
    private final int size;

    //2D matrix of place objects representing the board
    private ArrayList<Place> places;

    /** Create a new board of the given size. */
    public Board(int size) {
        this.size = size;
        this.places = new ArrayList<Place>();
        int count = 0;

        //initialize tiles
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                Place newPlace = new Place(col, row, count);
                count++;
                places.add(newPlace);
            }
        }

        places.get(0).setValue(1);
        places.get(1).setValue(2);
        places.get(2).setValue(3);
        places.get(3).setValue(4);
        places.get(4).setValue(5);
        places.get(5).setValue(6);
        places.get(6).setValue(7);
        places.get(7).setValue(8);
        places.get(8).setValue(9);
        places.get(9).setValue(10);
        places.get(10).setValue(12);
        places.get(11).setValue(11);
        places.get(12).setValue(13);
        places.get(13).setValue(14);
        places.get(14).setValue(0);
        places.get(15).setValue(15);

//        rearrange();
        boolean solvable = isSolvable();
        while(!solvable){
            Log.d("Rearranging","Configuration was not solvable, rearranging");
            rearrange();
            solvable = isSolvable();
        }
    }

    protected Board(Parcel in) {
        size = in.readInt();
    }

    public static final Creator<Board> CREATOR = new Creator<Board>() {
        @Override
        public Board createFromParcel(Parcel in) {
            return new Board(in);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };

    /** Return the size of this board. */
    public int getSize() {
        return size;
    }


    // Suggestions:
    // 1. Consider using the Observer design pattern so that a client,
    //    say a BoardView, can observe changes on a board, e.g.,
    //    hitting a place, sinking a ship, and game over.
    //
    // 2. Introduce methods including the following:
    //    public boolean placeShip(Ship ship, int x, int y, boolean dir)
    //    public void hit(Place place)
    //    public Place at(int x, int y)
    //    public Place[] places()
    //    public int numOfShots()
    //    public boolean isGameOver()
    //    ...


    /**
     * Determines whether the current configuration of the board is solvable or not.
     * Using logic from this website: https://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
     *
     * @return true if solvable, false otherwise.
     */
    public boolean isSolvable(){

        //The amount of times each tile has a value smaller than itself ahead of it
        int inversions = 0;

        for(Place currentPlace : places){

            //Don't look for inversions on the empty tile
            if(currentPlace.getValue() == 0) continue;

            int index = places.indexOf(currentPlace) + 1 ;

            while(index < places.size()){
                //Don't compare with the empty tile
                if(places.get(index).getValue() == 0) {
                    index++;
                    continue;
                }

                if(currentPlace.getValue() > places.get(index).getValue()) inversions++;
                index++;
            }
        }

        boolean evenWidth = false; //Identifies whether the width of the board is of even size
        if(size % 2 == 0) evenWidth = true;

        boolean evenInversions = false; //Identifies whether the number of inversions on the board is even
        if(inversions % 2 == 0) evenInversions = true;

        //Identifies whether the empty tile is on an even row starting from the bottom (e.g.: the last row is odd, second to last is even etc)
        boolean evenRow = false;

        //If there is an even number of rows, then the row indexes (0,1,2,3,etc) will be the same odd or even as starting from the bottom up.
        //We don't care if they are different numbers, we only care whether the numbers are even or odd
        if(evenWidth) {
            if(getEmptyTile().getY() % 2 == 0) evenRow = true;
        }

        //If there is an odd number or rows, then the row indexes + 1 will be the same odd or even as starting from the bottom up.
        //We don't care if they are different numbers, we only care whether the numbers are even or odd
        else{
            if(getEmptyTile().getY()+1 % 2 == 0) evenRow = true;
        }


        //If the board is of odd length
        if(!evenWidth) {

            //If there is an even number of inversions the game is solvable
            if(evenInversions) return true;

            else return false;
        }

        //If the board is of even length
        else {

            //If empty tile on even row AND odd number of inversions
            if(evenRow && !evenInversions) {
                return true;
            }

            //If empty tile is on odd row AND even number of inversions
            else if(!evenRow && evenInversions) {
                return true;
            }

            else return false;
        }
    }

    //Look through places and return the Place with value 0
    public Place getEmptyTile(){
        for(Place currentPlace : places){
            if(currentPlace.getValue() == 0) return currentPlace;
        }

        return null;
    }

    public ArrayList<Place> places(){
        return this.places;
    }

    /**
     * Rearanges the values of the board's tiles
     */
    public void rearrange(){
        //array containing all values of the board
        int [] values = new int [places.size()];
        for(int i = 0; i < places.size(); i++){
            values[i] = i;
        }

        //shuffle the array
        shuffleArray(values);

        //Assign new values to tiles
        for(int j = 0; j < places.size(); j++){
            places.get(j).setValue(values[j]);
        }
    }

    private static void shuffleArray(int[] array)
    {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Return a place object based on the coordinates
     * @param x
     * @param y
     * @return
     */
    public Place getPlace(int x, int y){
        for(Place currentPlace : places){
            if(currentPlace.getX() == x && currentPlace.getY() == y) {
                return currentPlace;
            }
        }
        return null;
    }

    /**
     * Check if the current board state represents a solved game by
     * comparing each tile value to the next. If the value of the next tile
     * is not greater than the previous by exactly 1 then it is not solved.
     *
     * @return
     */
    public boolean isSolved(){
        int tempValue = -1;

        for(int i = 0; i < places.size()-1; i++){
            Place currentPlace = new Place();

            currentPlace = places.get(i);

            if(tempValue == -1) {
                tempValue = currentPlace.getValue();
                continue;
            }

            else if(currentPlace.getValue() == tempValue + 1){
                tempValue = currentPlace.getValue();
                continue;
            }

            else return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(size);
    }
}

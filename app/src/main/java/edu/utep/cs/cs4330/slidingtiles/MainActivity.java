package edu.utep.cs.cs4330.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

/** Marco Lopez
 * CS 5390 - Mobile Application Development
 * 2/14/2017
 *
 * This is the controller in the MVC
 */
public class MainActivity extends AppCompatActivity{

    BoardView tileBoardView;
    Board tileBoard;
    final int boardSize = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Main Activity", "This is the onCreate method");

        setContentView(R.layout.activity_main);

        tileBoard = new Board(boardSize);

        tileBoardView = (BoardView) findViewById(R.id.boardView);
        tileBoardView.setFirstActivity(true);

        tileBoardView.setBoard(tileBoard);

    }
}

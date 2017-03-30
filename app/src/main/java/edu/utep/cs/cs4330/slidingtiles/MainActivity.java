package edu.utep.cs.cs4330.slidingtiles;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/** Marco Lopez
* CS 5390 - Mobile Application Development
* 2/14/2017
*
* This is the controller in the MVC
*/
public class MainActivity extends FragmentActivity implements NoticeDialogFragment.NoticeDialogListener{

BoardView tileBoardView;
Board tileBoard;
TextView numMovesTextView;
final int boardSize = 4;
int numMoves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Main Activity", "This is the onCreate method");

        setContentView(R.layout.activity_main);

        numMovesTextView = (TextView) findViewById(R.id.numMoves);

        if(savedInstanceState != null){
            numMoves = savedInstanceState.getInt("moves");
            tileBoard = savedInstanceState.getParcelable("board");
            numMovesTextView.setText("Number of moves: "+numMoves);
        }

        else tileBoard = new Board(boardSize);


        tileBoardView = (BoardView) findViewById(R.id.boardView);
        tileBoardView.setFirstActivity(true);
        tileBoardView.setBoard(tileBoard);
        tileBoardView.addBoardTouchListener(new BoardView.BoardTouchListener() {

            @Override
            public void onTouch(int x, int y) {

                Place placeTouched = tileBoard.getPlace(x, y);

                if(isNeighbor(placeTouched)){
                    numMoves++;
                    numMovesTextView.setText("Number of moves: "+numMoves);

                    tileBoard.getEmptyTile().setValue(placeTouched.getValue());

                    placeTouched.setValue(0);

                    if(tileBoard.isSolved()) {
                        tileBoardView.createGameOverDialog("You Win!");
                        tileBoardView.gameOverDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                                System.exit(0);
                            }
                        });
                    }
                }

                else {
                    toast(String.format("Touched: %d, %d", x, y));
                }
            }
        });
    }
        public void showNoticeDialog() {
            // Create an instance of the dialog fragment and show it
            DialogFragment dialog = new NoticeDialogFragment();
            dialog.show(getFragmentManager(), "newGameAlert");
        }

        // The dialog fragment receives a reference to this Activity through the
        // Fragment.onAttach() callback, which it uses to call the following methods
        // defined by the NoticeDialogFragment.NoticeDialogListener interface
        @Override
        public void onDialogPositiveClick(DialogFragment dialog) {
            // User touched the dialog's positive button
            tileBoard.rearrange();
            numMoves = 0;
            numMovesTextView.setText("Number of moves: "+numMoves);
            tileBoardView.redraw();
        }

        @Override
        public void onDialogNegativeClick(DialogFragment dialog) {
            // User touched the dialog's negative button
        }

        /**
     * Check if the currentPlace is adjacent to the empty tile
     * @param currentPlace
     * @return
     */
    private boolean isNeighbor(Place currentPlace){
        Place emptyTile = tileBoard.getEmptyTile();
        int emptyTileX = emptyTile.getX();
        int emptyTileY = emptyTile.getY();

        int currentPlaceX = currentPlace.getX();
        int currentPlaceY = currentPlace.getY();

        //Check to the right
        if(emptyTileX == currentPlaceX + 1 && emptyTileY == currentPlaceY) return true;

        //Check down
        else if(emptyTileX == currentPlaceX && emptyTileY == currentPlaceY + 1) return true;

        //Check to the left
        else if(emptyTileX == currentPlaceX - 1 && emptyTileY == currentPlaceY) return true;

        //Check up
        else if(emptyTileX == currentPlaceX && emptyTileY == currentPlaceY - 1) return true;

        return false;
    }

    /**
     * Method called when new game button called
     * @param view
     */
    public void newGame(View view){
        showNoticeDialog();
    }

    /** Show a toast message. */
    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelable("board", tileBoard);
        outState.putInt("moves", numMoves);
        super.onSaveInstanceState(outState);
    }
}

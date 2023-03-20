package pl.wsei.pam.lab03;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {

    private int[][] icons = new int[4][4];
    private Handler handler;
    private int pairCounter = 0;
    private List<Integer> clicedTiles = new ArrayList<>();
    private List<ImageButton> clickedButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        fillBoardRandomly();
        handler = new Handler(getMainLooper());
    }

    private void fillBoardRandomly(){
        icons[0][0] = R.drawable.fig_1;
        icons[0][1] = R.drawable.fig_2;
        icons[0][2] = R.drawable.fig_3;
        icons[0][3] = R.drawable.fig_4;
        icons[1][0] = R.drawable.fig_5;
        icons[1][1] = R.drawable.fig_6;
        icons[1][2] = R.drawable.fig_7;
        icons[1][3] = R.drawable.fig_8;
        icons[2][0] = R.drawable.fig_1;
        icons[2][1] = R.drawable.fig_2;
        icons[2][2] = R.drawable.fig_3;
        icons[2][3] = R.drawable.fig_4;
        icons[3][0] = R.drawable.fig_5;
        icons[3][1] = R.drawable.fig_6;
        icons[3][2] = R.drawable.fig_7;
        icons[3][3] = R.drawable.fig_8;
    }

    public void clickButton(View view){
        ImageButton v = (ImageButton) view;
        String tag = (String) v.getTag();
        int row = Integer.parseInt(tag.split(" ")[0]);
        int col = Integer.parseInt(tag.split(" ")[1]);
        v.setImageDrawable(getResources().getDrawable(icons[row][col]));
        Log.i("BOARD", row + " " + col);
        clicedTiles.add(icons[row][col]);
        clickedButtons.add(v);
        handler.postDelayed(() -> {
            if (clicedTiles.size() == 2){
                if ((int) clicedTiles.get(0) == clicedTiles.get(1)){
                    pairCounter++;
                    Log.i("BOARD", "Equals " + pairCounter);
                    for (ImageButton btn: clickedButtons) {
                        btn.setEnabled(false);
                    }
                } else {
                    clicedTiles.clear();
                    for (ImageButton btn: clickedButtons) {
                        btn.setImageDrawable(getResources().getDrawable(R.drawable.deck));
                    }
                }
                clicedTiles.clear();
                clickedButtons.clear();
            }
            if (pairCounter == 8){
                finishActivityAndReturnResult(8);
            }
        }, 1000);

    }

    private void finishActivityAndReturnResult(int points){
        Intent intent = new Intent();
        intent.putExtra("points", points);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
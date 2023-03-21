package pl.wsei.pam.lab03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
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

    private String lastClicked = "";

    private ArrayList<String> revealed = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        if (savedInstanceState != null){
            icons[0] = savedInstanceState.getIntArray("iconsRow0");
            icons[1] = savedInstanceState.getIntArray("iconsRow1");
            icons[2] = savedInstanceState.getIntArray("iconsRow2");
            icons[3] = savedInstanceState.getIntArray("iconsRow3");
            revealed = savedInstanceState.getStringArrayList("revealedCards");
            GridLayout root = findViewById(R.id.board_activity_root);
            for(int i = 0; i < root.getChildCount(); i++){
                ImageButton button = (ImageButton) root.getChildAt(i);
                String tag = (String) button.getTag();
                if (revealed.contains(tag)){
                    int row = Integer.parseInt(tag.split(" ")[0]);
                    int col = Integer.parseInt(tag.split(" ")[1]);
                    button.setImageDrawable(getResources().getDrawable( icons[row][col],getTheme()));
                    //znaleziony button należy odkryć ustawiając mu ikonę pobraną z icons o współrzednych w tagu'
                }
            }
        } else {
            fillBoardRandomly();
        }
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
        v.setImageDrawable(getResources().getDrawable(icons[row][col], getTheme()));
        Log.i("BOARD", row + " " + col);
        clicedTiles.add(icons[row][col]);
        clickedButtons.add(v);
        handler.postDelayed(() -> {
            if (clicedTiles.size() == 2){
                if ((int) clicedTiles.get(0) == clicedTiles.get(1)){
                    pairCounter++;
                    revealed.add((String) clickedButtons.get(0).getTag());
                    revealed.add((String) clickedButtons.get(1).getTag());
                    //
                    for (ImageButton btn: clickedButtons) {
                        btn.setEnabled(false);
                    }
                } else {
                    clicedTiles.clear();
                    for (ImageButton btn: clickedButtons) {
                        btn.setImageDrawable(getResources().getDrawable(R.drawable.deck, getTheme()));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Czy na pewno chcesz wyjść z gry?")
                .setPositiveButton("Tak", (dialog, i) -> {
                    super.onBackPressed();
                })
                .setNegativeButton("Nie", (dialog, i) -> {
                })
                .create()
                .show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i("BOARD", "SAVE");
        super.onSaveInstanceState(outState);
        outState.putIntArray("iconsRow0", icons[0]);
        outState.putIntArray("iconsRow1", icons[1]);
        outState.putIntArray("iconsRow2", icons[2]);
        outState.putIntArray("iconsRow3", icons[3]);
        outState.putStringArrayList("revealedCards", revealed);

    }
}
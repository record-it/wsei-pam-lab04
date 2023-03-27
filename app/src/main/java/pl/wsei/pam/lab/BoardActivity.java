package pl.wsei.pam.lab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardActivity extends AppCompatActivity {
    Random random = new Random();
    public static final String ICONS_ROW_0 = "iconsRow0";
    public static final String ICONS_ROW_1 = "iconsRow1";
    public static final String ICONS_ROW_2 = "iconsRow2";
    public static final String ICONS_ROW_3 = "iconsRow3";
    public static final String REVEALED_CARDS = "revealedCards";
    private int[][] icons = new int[4][4];
    private Handler handler;
    private int pairCounter = 0;
    private List<ImageButton> clickedButtons = new ArrayList<>();
    private boolean isBlockedUI = false;
    GridLayout root;
    private ArrayList<String> revealed = new ArrayList<>();

    private boolean isSound = true;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_board);
        root = findViewById(R.id.board_activity_root);
        if (bundle != null) {
            restoreInstanceState(bundle);
        } else {
            fillBoardRandomly();
        }
        handler = new Handler(getMainLooper());
    }
    private void fillBoardRandomly() {
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
    public void clickButton(View view) {
        ImageButton v = (ImageButton) view;
        String tag = (String) v.getTag();
        int row = Integer.parseInt(tag.split(" ")[0]);
        int col = Integer.parseInt(tag.split(" ")[1]);
        v.setImageDrawable(getResources().getDrawable(icons[row][col], getTheme()));
        v.setTag(R.integer.image_id, icons[row][col]);
        clickedButtons.add(v);
        if (clickedButtons.size() == 2) {
            isBlockedUI = true;
            if ((int) clickedButtons.get(0).getTag(R.integer.image_id) == (int) clickedButtons.get(1).getTag(R.integer.image_id)) {
                pairCounter++;
                revealed.add((String) clickedButtons.get(0).getTag());
                revealed.add((String) clickedButtons.get(1).getTag());
                for (ImageButton btn : clickedButtons) {
                    btn.setEnabled(false);
                }
            } else {

                for (ImageButton btn : clickedButtons) {
                    handler.postDelayed(() ->
                    btn.setImageDrawable(getResources().getDrawable(R.drawable.deck, getTheme())),
                    1000
                    );
                }
            }
            clickedButtons.clear();
        }
        if (pairCounter == 8) {
            finishActivityAndReturnResult(8);
        }
    }
    private void finishActivityAndReturnResult(int points) {
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
        outState.putIntArray(ICONS_ROW_0, icons[0]);
        outState.putIntArray(ICONS_ROW_1, icons[1]);
        outState.putIntArray(ICONS_ROW_2, icons[2]);
        outState.putIntArray(ICONS_ROW_3, icons[3]);
        outState.putStringArrayList(REVEALED_CARDS, revealed);
    }

    private void restoreInstanceState(Bundle bundle) {
        icons[0] = bundle.getIntArray(ICONS_ROW_0);
        icons[1] = bundle.getIntArray(ICONS_ROW_1);
        icons[2] = bundle.getIntArray(ICONS_ROW_2);
        icons[3] = bundle.getIntArray(ICONS_ROW_3);
        revealed = bundle.getStringArrayList(REVEALED_CARDS);
        for (int i = 0; i < root.getChildCount(); i++) {
            ImageButton button = (ImageButton) root.getChildAt(i);
            String tag = (String) button.getTag();
            if (revealed.contains(tag)) {
                int row = Integer.parseInt(tag.split(" ")[0]);
                int col = Integer.parseInt(tag.split(" ")[1]);
                button.setImageDrawable(getResources().getDrawable(icons[row][col], getTheme()));
                button.setAlpha(0.0f);
            }
        }
    }
}
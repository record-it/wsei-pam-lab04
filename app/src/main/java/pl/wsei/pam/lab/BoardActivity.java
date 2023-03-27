package pl.wsei.pam.lab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.GridLayout;
import android.widget.ImageButton;

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
    private String lastClicked = "";

    private boolean blocked = false;
    GridLayout root;
    private ArrayList<String> revealed = new ArrayList<>();
    private MediaPlayer completionPlayer;
    private MediaPlayer negativePLayer;

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
        completionPlayer = MediaPlayer.create(this, R.raw.completion);
        negativePLayer = MediaPlayer.create(this, R.raw.negative_guitar);
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
//        if (blocked) {
//            return;
//        }
        ImageButton v = (ImageButton) view;
        if (clickedButtons.size() == 1 && clickedButtons.get(0) == v) {
            return;
        }
        String tag = (String) v.getTag();
        int row = Integer.parseInt(tag.split(" ")[0]);
        int col = Integer.parseInt(tag.split(" ")[1]);
        v.setImageDrawable(getResources().getDrawable(icons[row][col], getTheme()));
        v.setImageAlpha(255);
        v.setTag(R.integer.image_id, icons[row][col]);
        clickedButtons.add(v);
        if (clickedButtons.size() == 2) {
            blocked = true;
            if ((int) clickedButtons.get(0).getTag(R.integer.image_id) == (int) clickedButtons.get(1).getTag(R.integer.image_id)) {
                pairCounter++;
                revealed.add((String) clickedButtons.get(0).getTag());
                revealed.add((String) clickedButtons.get(1).getTag());
                for (ImageButton btn : clickedButtons) {
                    btn.setEnabled(false);
                    animatePairedButton(btn, () -> {
                        blocked = false;
                    });
                }
                clickedButtons.clear();
            } else {

                for (ImageButton btn : clickedButtons) {
                    animateNotPairedButton(btn, () -> {
                        handler.post(() -> {
                            btn.setImageDrawable(getResources().getDrawable(R.drawable.deck, getTheme()));
                            blocked = false;
                        });
                    });
                }
                clickedButtons.clear();
            }
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
            }
        }
    }

    private void disableButton() {
        for (int i = 0; i < root.getChildCount(); i++) {
            View view = root.getChildAt(i);
            view.setEnabled(true);
        }
    }

    private void enableButtons() {
        for (int i = 0; i < root.getChildCount(); i++) {
            View view = root.getChildAt(i);
            view.setEnabled(true);
        }
    }

    private void animatePairedButton(ImageButton button, Runnable action) {
        completionPlayer.start();
        AnimatorSet set = new AnimatorSet();
        button.setPivotX(random.nextFloat() * 200);
        button.setPivotY(random.nextFloat() * 200);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(button, "rotation", 1080);
        ObjectAnimator scallingX = ObjectAnimator.ofFloat(button, "scaleX", 1, 4);
        ObjectAnimator scallingY = ObjectAnimator.ofFloat(button, "scaleY", 1, 4);
        ObjectAnimator fade = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f);
        set.setDuration(2000);
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(rotation, scallingX, scallingY, fade);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                button.setScaleX(1);
                button.setScaleY(1);
                button.setAlpha(0.0f);
                action.run();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
        set.start();
    }

    private void animateNotPairedButton(ImageButton button, Runnable action) {
        negativePLayer.start();
        ObjectAnimator rotation1 = ObjectAnimator.ofFloat(button, "rotation", 0, 30, -30, 0, 20, -20, 0, 10, -10, 0);
        ObjectAnimator fade = ObjectAnimator.ofInt(button, "imageAlpha", 255, 0);

        AnimatorSet set = new AnimatorSet();
        rotation1.setInterpolator(new DecelerateInterpolator());
        set.setDuration(1000);
        set.playSequentially(rotation1, fade);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                action.run();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);
        return !blocked;
    }
}
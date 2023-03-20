package pl.wsei.pam.lab03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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

    private ArrayList<String> revealed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
    }


    private void finishActivityAndReturnResult(int points){
        Intent intent = new Intent();
        intent.putExtra("points", points);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
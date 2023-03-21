package pl.wsei.pam.lab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class BoardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
    }

    public void clickButton(View view){

    }

    private void finishActivityAndReturnResult(int points){
        Intent intent = new Intent();
        intent.putExtra("points", points);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
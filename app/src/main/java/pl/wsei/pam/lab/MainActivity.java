package pl.wsei.pam.lab;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            int points = result.getData().getIntExtra("points", 0);
            Log.i("MAIN", "points " + points);
        });
    }

    public void onClikStar44Board(View w) {
        Intent intent = new Intent(this, BoardActivity.class);
        launcher.launch(intent);
    }
}
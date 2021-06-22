package pl.tenk.bullseye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SoundDetails extends AppCompatActivity {

    Button buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_details);

        buttonBack = findViewById(R.id.button_back);

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(SoundDetails.this, MainActivity.class);
            startActivity(intent);
        });


    }
}
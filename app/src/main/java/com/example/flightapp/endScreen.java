package com.example.flightapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class endScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endscreen);
        Log.d("transition","endscreen");
        int score =getIntent().getIntExtra("score",0);
        TextView txt=findViewById(R.id.score);
        txt.setText("Score "+score);

        Button btn =findViewById(R.id.replay);
        btn.setOnClickListener((View v)->{
            Intent intnt = new Intent(this, MainActivity.class);
            startActivity(intnt);
        });

    }
}

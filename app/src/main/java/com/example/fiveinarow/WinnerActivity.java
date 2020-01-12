package com.example.fiveinarow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class WinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        //get the intent to set the views
        Intent intent = getIntent();
        String winnerName = intent.getStringExtra(MainActivity.KEY_WINNER);
        String winnerColor = intent.getStringExtra(MainActivity.KEY_WINNER_COLOR);

        //find views
        ImageView winColorDisplay = (ImageView) findViewById(R.id.win_color_display);
        TextView winnerNameDisplay = (TextView) findViewById(R.id.winner_name_display);

        //set the winner name
        winnerNameDisplay.setText(winnerName);
        //set the winner color
        if (winnerColor.equals(MainActivity.BLACK)) {
            winColorDisplay.setImageResource(R.drawable.black_circle);
        } else {
            winColorDisplay.setImageResource(R.drawable.white_circle);
        }
    }
}

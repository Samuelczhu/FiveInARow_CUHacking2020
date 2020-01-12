package com.example.fiveinarow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {

    public static String KEY_PLAYER1 = "player_1";
    public static String KEY_PLAYER2 = "player_2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //find the views
        Button startButton = (Button) findViewById(R.id.start_button);
        final EditText player1 = (EditText) findViewById(R.id.player1_name);
        final EditText player2 = (EditText) findViewById(R.id.player2_name);

        //click the start button and start the game
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                intent.putExtra(KEY_PLAYER1, player1.getText().toString());
                intent.putExtra(KEY_PLAYER2, player2.getText().toString());
                StartActivity.this.startActivity(intent);
            }
        });
    }
    //create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //responding to the menu click action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info: //info menu item clicked
                //start the information activity
                Intent intent = new Intent(StartActivity.this,InfoActivity.class);
                StartActivity.this.startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

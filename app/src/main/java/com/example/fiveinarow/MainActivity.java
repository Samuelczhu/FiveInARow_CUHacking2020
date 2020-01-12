package com.example.fiveinarow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This app is a Five in a Row game. This app is completed in 2 days in CUHacking 2020. Team: Johnny, Samuel, Abdullah
 * Speech Integration: Johnny
 * UI design: Samuel
 * Game Logic: Johnny, Samuel, Abdullah
 * @author Johnny, Samuel, Abdullah
 * @version 2020/1/12
 */
public class MainActivity extends AppCompatActivity {

    public static String BLACK = "BLACK";
    public static String WHITE = "WHITE";
    public static String KEY_WINNER = "winner";
    public static String KEY_WINNER_COLOR = "winner_color";

    private String player1Name;
    private String player2Name;

    private Stone[] stones; //hold the stone object
    private GameManager gameManager;

    //android views
    private GridView gameBoard;
    private ImageView moveColor;
    private Button resetButton;
    private ImageView voiceButton;

    //code work for the speech detecter
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO =1;
    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecognizer;
    public int i;
    public int j;

    //the stoneAdapter
    private StonesAdapter stonesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the intent from StartActivity for player names
        Intent intent = getIntent();
        player1Name = intent.getStringExtra(StartActivity.KEY_PLAYER1);
        player2Name = intent.getStringExtra(StartActivity.KEY_PLAYER2);

        //find views by ids
        gameBoard = (GridView) findViewById(R.id.board);
        moveColor = (ImageView) findViewById(R.id.moveColor);
        resetButton = (Button) findViewById(R.id.reset_button);
        TextView player1NameDisplay = (TextView) findViewById(R.id.player1_name_display);
        TextView player2NameDisplay = (TextView) findViewById(R.id.player2_name_display);
        voiceButton = (ImageView) findViewById(R.id.voice_button);

        //set the player name
        player1NameDisplay.setText(player1Name);
        player2NameDisplay.setText(player2Name);

        //start a new gameManager
        gameManager = new GameManager();

        //get the stones array
        stones = gameManager.getStones();

        //set the stone adapter
        stonesAdapter = new StonesAdapter(this, stones);
        gameBoard.setAdapter(stonesAdapter);
        //set the onclick listener for the grid
        gameBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //update the board in gameManager
                gameManager.updateBoard(position/GameManager.BOARD_SIZE, position%GameManager.BOARD_SIZE);
                //update the moving indicator
                if (gameManager.getCurrentMove()==Stone.WHITE)
                    moveColor.setImageResource(R.drawable.white_circle);
                else
                    moveColor.setImageResource(R.drawable.black_circle);
                //update the stones
                stonesAdapter.notifyDataSetChanged(); //notify adapter changed

                checkWinner(); //check for winner
            }
        });

        //set the onclick for the reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reset the board in gameManager into empty board
                gameManager.resetBoard();
                //update the moving indicator
                if (gameManager.getCurrentMove()==Stone.WHITE)
                    moveColor.setImageResource(R.drawable.white_circle);
                else
                    moveColor.setImageResource(R.drawable.black_circle);
                stonesAdapter.notifyDataSetChanged(); //notify adapter changed
            }
        });

        //onclick listener for voice button
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED){

                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.RECORD_AUDIO)){

                    } else{
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO},MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
                    }
                } else {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    mySpeechRecognizer.startListening(intent);
                }
            }
        });

        //method have to call for speed detector
        initializeSpeechRecognizer();
        initializeTextToSpeech();
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
                Intent intent = new Intent(MainActivity.this,InfoActivity.class);
                MainActivity.this.startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //check for winner
    private void checkWinner() {
        //check for winner
        if (gameManager.checkWinner()==GameManager.WHITE_WIN) { //white win! Congratulation to white
            //TODO: delete this toast after debugged
            Toast toast = Toast.makeText(getApplicationContext(), "White win", Toast.LENGTH_SHORT);
            toast.show();
            //start a new intent for white winner
            Intent winnerIntent = new Intent(MainActivity.this,WinnerActivity.class);
            winnerIntent.putExtra(KEY_WINNER, player1Name);
            winnerIntent.putExtra(KEY_WINNER_COLOR,WHITE);
            MainActivity.this.startActivity(winnerIntent);
        } else if (gameManager.checkWinner()==GameManager.BLACK_WIN) { //black win! Congratulation to black
            //start a new intent for black winner
            Intent winnerIntent = new Intent(MainActivity.this,WinnerActivity.class);
            winnerIntent.putExtra(KEY_WINNER, player2Name);
            winnerIntent.putExtra(KEY_WINNER_COLOR,BLACK);
            MainActivity.this.startActivity(winnerIntent);
        } else if (gameManager.checkWinner()==GameManager.TIE) { //game TIE after long match
            //start a new intent for TIE
            Intent tieIntent = new Intent(MainActivity.this,TieActivity.class);
            MainActivity.this.startActivity(tieIntent);
        }
    }


    //initialize the speed recognizer
    private void initializeSpeechRecognizer(){
        if(SpeechRecognizer.isRecognitionAvailable(this)) {
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {

                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processResult(results.get(0));
                }


                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    //the most important part
    private void processResult(String command) {
        command = command.toLowerCase();
        System.out.println(command);
        if(command.contains("what")){
            if(command.contains("your name")){
                speak("My name is U w U");

            }
            if(command.contains("time")){
                Date now = new Date();
                String time = DateUtils.formatDateTime(this,now.getTime(),DateUtils.FORMAT_SHOW_TIME);
                speak("It is "+time);
            }
        }

        if(command.contains("play")){
            String[] arr = command.split(" ");
            String str = arr[arr.length-1];
            int tempi = str.charAt(0)-97;
            int tempj = Integer.parseInt(str.substring(1))-1;
            if(tempi<13&&tempj<13&&tempi>0&&tempj>0){
                i = tempi;
                j = tempj;
                System.out.println("i = "+i+", j = "+j);
                //update the board in gameManager
                gameManager.updateBoard(i, j);
                //update the moving indicator
                if (gameManager.getCurrentMove()==Stone.WHITE)
                    moveColor.setImageResource(R.drawable.white_circle);
                else
                    moveColor.setImageResource(R.drawable.black_circle);
                //update the stones
                stonesAdapter.notifyDataSetChanged(); //notify adapter changed
                checkWinner(); //check for winner
            }
            else{
                speak("Try again?");
            }
        }
    }

    //initialize speech: "Let's go"
    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size() == 0){
                    Toast.makeText(MainActivity.this, "There is no TTS engine on your device",Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    myTTS.setLanguage(Locale.US);
                    speak("Lets GO");
                }
            }
        });
    }

    private void speak(String message){
        myTTS.speak(message, TextToSpeech.QUEUE_FLUSH,null,null);
    }
    @Override
    protected void onPause(){
        super.onPause();
        myTTS.shutdown();
    }
    @Override
    protected void onResume(){
        super.onResume();
        initializeSpeechRecognizer();
        initializeTextToSpeech();
    }

}

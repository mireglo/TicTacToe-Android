package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static MediaPlayer bgMusicPlayer;
    boolean playing = true;
    ImageButton audioButton;
    int currentLoc = 0;

    int p1Score = 0;
    int p2Score = 0;

    TextView p1ScoreText;
    TextView p2ScoreText;

    TextView turnText;
    int turn = 1;

    Button [][] tttButtons = new Button[3][3];
    String [][] tttBoard = new String[3][3];
    Button resetButton;

    @Override
    protected void onStop(){
        super.onStop();
        bgMusicPlayer.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bgMusicPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bgMusicPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bgMusicPlayer = MediaPlayer.create(this, R.raw.bg_music);
        bgMusicPlayer.start();

        audioButton = findViewById(R.id.audioButton);

        tttButtons[0][0] = findViewById(R.id.buttonTopLeft);
        tttButtons[0][1] = findViewById(R.id.buttonTopMiddle);
        tttButtons[0][2] = findViewById(R.id.buttonTopRight);
        tttButtons[1][0] = findViewById(R.id.buttonMiddleLeft);
        tttButtons[1][1] = findViewById(R.id.buttonMiddle);
        tttButtons[1][2] = findViewById(R.id.buttonMiddleRight);
        tttButtons[2][0] = findViewById(R.id.buttonBottomLeft);
        tttButtons[2][1] = findViewById(R.id.buttonBottomMiddle);
        tttButtons[2][2] = findViewById(R.id.buttonBottomRight);

        resetButton = findViewById(R.id.resetButton);

        p1ScoreText = findViewById(R.id.player1TextView);
        p2ScoreText = findViewById(R.id.player2TextView);
        turnText = findViewById(R.id.turnTextView);

        resetBoard(resetButton);

        setScore();

    }

    public void resetBoard(View v){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                tttButtons[i][j].setText(" ");
                tttBoard[i][j] = " ";
            }
        }
        turnText.setText(getString(R.string.p1Turn));
        turn = 1;
        resetButton.setVisibility(View.INVISIBLE);
    }

    private void setScore(){
        String p1String = getString(R.string.p1) + p1Score;
        String p2String = getString(R.string.p2) + p2Score;

        p1ScoreText.setText(p1String);
        p2ScoreText.setText(p2String);
    }

    private boolean checkWinner(int row, int col, String player){

        if (tttButtons[row][0].getText().equals(player) &&
            tttButtons[row][1].getText().equals(player) &&
            tttButtons[row][2].getText().equals(player)){
            return true;
        }

        if (tttButtons[0][col].getText().equals(player) &&
            tttButtons[1][col].getText().equals(player) &&
            tttButtons[2][col].getText().equals(player)){
            return true;
        }

        if (tttButtons[0][0].getText().equals(player) &&
            tttButtons[1][1].getText().equals(player) &&
            tttButtons[2][2].getText().equals(player)){
            return true;
        }

        if (tttButtons[0][2].getText().equals(player) &&
            tttButtons[1][1].getText().equals(player) &&
            tttButtons[2][0].getText().equals(player)){
            return true;
        }

        return false;
    }

    public void boardClick(View v){
        Button b = (Button) v;

        boolean found = false;
        int row = 0, col = 0;

        for(int i = 0; i < 3 && !found; i++){
            for(int j = 0; j < 3 && !found; j++){
                if (b == tttButtons[i][j]){
                    row = i;
                    col = j;
                    found = true;
                }
            }
        }

        // Uncomment below line to get button position as a word.
        //Toast.makeText(this, getResources().getResourceEntryName(v.getId()), Toast.LENGTH_SHORT).show();

        // Uncomment below line to get button position as a set of two numbers row and column.
        //Toast.makeText(this, ""+row+" "+col, Toast.LENGTH_SHORT).show();

        if (turn % 2 == 1 && b.getText().toString() == " " && turn < 10){
            b.setText("X");
            tttBoard[row][col] = "X";
            turnText.setText(getString(R.string.p2Turn));
            turn++;

            if(checkWinner(row, col, "X")){
                turn = 20;
                p1Score++;
                turnText.setText(R.string.p1Won);
                resetButton.setVisibility(View.VISIBLE);
                setScore();
            }

        }
        else if (turn % 2 == 0 && b.getText().toString() == " " && turn < 10){
            b.setText("O");
            tttBoard[row][col] = "O";
            turnText.setText(getString(R.string.p1Turn));
            turn++;

            if(checkWinner(row, col, "O")){
                turn = 20;
                p2Score++;
                turnText.setText(R.string.p2Won);
                resetButton.setVisibility(View.VISIBLE);
                setScore();
            }
        }

        if (turn == 10){
            turnText.setText(R.string.pDraw);
            resetButton.setVisibility(View.VISIBLE);
        }
    }

    public void toggleAudio (View v){
        if (bgMusicPlayer.isPlaying()){
            ((ImageButton) v).setImageResource(R.drawable.muted);
            bgMusicPlayer.stop();
            playing = false;
        }
        else{
            ((ImageButton) v).setImageResource(R.drawable.unmuted);
            bgMusicPlayer = MediaPlayer.create(this, R.raw.bg_music);
            bgMusicPlayer.start();
            bgMusicPlayer.seekTo(currentLoc);
            playing = true;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("p1Score", p1Score);
        outState.putInt("p2Score", p2Score);
        outState.putInt("turn", turn);

        outState.putStringArray("tttRow0", tttBoard[0]);
        outState.putStringArray("tttRow1", tttBoard[1]);
        outState.putStringArray("tttRow2", tttBoard[2]);

        outState.putInt("musicPosition", bgMusicPlayer.getCurrentPosition());
        outState.putBoolean("playing", playing);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        p1Score = savedInstanceState.getInt("p1Score");
        p2Score = savedInstanceState.getInt("p2Score");
        turn = savedInstanceState.getInt("turn");

        tttBoard[0] = savedInstanceState.getStringArray("tttRow0");
        tttBoard[1] = savedInstanceState.getStringArray("tttRow1");
        tttBoard[2] = savedInstanceState.getStringArray("tttRow2");

        currentLoc = savedInstanceState.getInt("musicPosition");
        bgMusicPlayer.seekTo(currentLoc);
        boolean mPlay = savedInstanceState.getBoolean("playing");
        if(!mPlay){
            toggleAudio(audioButton);
        }

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                tttButtons[i][j].setText(tttBoard[i][j]);
            }
        }

        if (turn >= 10){
            resetButton.setVisibility(View.VISIBLE);
        }

        setScore();
    }
}
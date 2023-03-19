package com.example.myapplication;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.nio.channels.InterruptedByTimeoutException;

public class MainActivity extends AppCompatActivity {
    private TextView leftTime;
    private TextView rightTime;
    private Button playBtn;
    private SeekBar positionBar;
    private SeekBar volumeBar;
    private MediaPlayer mediaPlayer;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playBtn=findViewById(R.id.playBtn);
        leftTime=findViewById(R.id.leftTime);
        rightTime=findViewById(R.id.rightTime);
        mediaPlayer =MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(0.5f,0.5f);
        totalTime = mediaPlayer.getDuration();




        volumeBar=findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                         float volumeNum= progress/100f;
                         mediaPlayer.setVolume(volumeNum, volumeNum );
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        positionBar=findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            mediaPlayer.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        new Thread(new Runnable(){
            @Override
            public void run(){
                while (mediaPlayer!=null){
                    try{
                        Message msg=new Message();
                        msg.what= mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){

                    }
                }
            }

        }).start();
    }
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            int currentPosition = msg.what;
            positionBar.setProgress(currentPosition);
            String left = createTimeLabel(currentPosition);
            leftTime.setText(left);
            String right = createTimeLabel(totalTime- currentPosition);
            rightTime.setText("-" + right);
        }
    };
    public String createTimeLabel(int time ){
        String timeLabel="";
        int min = time / 1000 /60;
        int sec = time / 1000 % 60;
        timeLabel = min +";";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }
    public void playBtnClick(View view){

        if  (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            playBtn.setBackgroundResource(R.drawable.images);

        }else{
            mediaPlayer.pause();
            playBtn.setBackgroundResource(R.drawable.images2);
        }

    }

}
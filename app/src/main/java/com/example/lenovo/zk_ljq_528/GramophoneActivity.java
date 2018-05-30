package com.example.lenovo.zk_ljq_528;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.lenovo.zk_ljq_528.R.*;

public class GramophoneActivity extends AppCompatActivity {

    private MediaPlayer mp;//mediaPlayer对象
    private Button play,pause,stop;//播放 暂停/继续 停止 按钮
  //  private TextView hint;//显示当前播放状态
    private boolean isPause=false;//是否暂停

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_gramophone);

        pause = (Button) findViewById(R.id.button2);
        stop = (Button) findViewById(R.id.button3);
       // hint = (TextView) findViewById(R.id.hint);
      //  hint.setTextSize(20);
        mp = MediaPlayer.create(GramophoneActivity.this, R.raw.knife);//创建mediaplayer对象

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                // TODO Auto-generated method stub
                play();//重新开始播放
            }
        });



        final GramophoneView gramophoneView = (GramophoneView)findViewById(id.gramophone_view);
        final Button button = (Button)findViewById(id.btn_play_pause);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gramophoneView.getPlaying()){
                    button.setText("点击播放");

                    play();

                    if(isPause){
                        pause.setText("暂停");
                        isPause=false;
                    }
                }
                gramophoneView.setPlaying(!gramophoneView.getPlaying());
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(mp.isPlaying()&&!isPause){
                    mp.pause();
                    isPause=true;
                    gramophoneView.setPlaying(!gramophoneView.getPlaying());
                    pause.setText("继续");
                  //  hint.setText("暂停播放音频...");
                    button.setEnabled(true);
                }else{
                    mp.start();
                    pause.setText("暂停");
                  //  hint.setText("继续播放音频...");
                    isPause=false;
                    button.setEnabled(false);
                }
            }
        });


    }


    private void play(){
        try{
            mp.reset();
            mp=MediaPlayer.create(GramophoneActivity.this, R.raw.knife);//重新设置要播放的音频
            mp.start();//开始播放
          //  hint.setText("正在播放音频...");
            play.setEnabled(false);
            pause.setEnabled(true);
            stop.setEnabled(true);
        }catch(Exception e){
            e.printStackTrace();//输出异常信息
        }
    }
}
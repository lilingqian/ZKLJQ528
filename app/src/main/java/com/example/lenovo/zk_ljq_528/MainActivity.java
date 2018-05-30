package com.example.lenovo.zk_ljq_528;

import android.app.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private MediaPlayer mp;//mediaPlayer对象
    private Button play,pause,stop;//播放 暂停/继续 停止 按钮
    private TextView hint;//显示当前播放状态
    private boolean isPause=false;//是否暂停
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.button1);
        pause = (Button) findViewById(R.id.button2);
        stop = (Button) findViewById(R.id.button3);
        hint = (TextView) findViewById(R.id.hint);
        hint.setTextSize(20);
        mp = MediaPlayer.create(MainActivity.this, R.raw.knife);//创建mediaplayer对象

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                // TODO Auto-generated method stub
                play();//重新开始播放
            }
        });


        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play();
                if(isPause){
                    pause.setText("暂停");
                    isPause=false;
                }
            }
        });

    }

    private void play(){
        try{
            mp.reset();
            mp=MediaPlayer.create(MainActivity.this, R.raw.knife);//重新设置要播放的音频
            mp.start();//开始播放
            hint.setText("正在播放音频...");
            play.setEnabled(false);
            pause.setEnabled(true);
            stop.setEnabled(true);
        }catch(Exception e){
            e.printStackTrace();//输出异常信息
        }
    }


}

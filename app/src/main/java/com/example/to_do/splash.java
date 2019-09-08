package com.example.to_do;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class splash extends AppCompatActivity {

    ImageView imageView;
    TextView t1,t2;
    private static final float ROTATE_FROM = 0.0f;
    private static final float ROTATE_TO = -10.0f * 360.0f;// 3.141592654f * 32.0f;
    Animation fromabove, fl,fr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash);

        t1=(TextView)findViewById(R.id.text1);
        t2=(TextView)findViewById(R.id.text2);
        imageView=(ImageView)findViewById(R.id.icon);

        getSupportActionBar().hide();
        animations();
        logolaucher lc=new logolaucher();
        lc.start();
    }
    private  class logolaucher extends Thread
    {
        public void run()
        {
            try
            {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent=new Intent(splash.this,MainActivity.class);
            startActivity(intent);
            splash.this.finish();
        }
    }

    public void animations(){
      /*  Animation anim= AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        RelativeLayout RL = (RelativeLayout) findViewById(R.id.RL);
        RL.clearAnimation();
        RL.startAnimation(anim);*/

//        Animation anim_2= AnimationUtils.loadAnimation(this,R.anim.translate);
//        anim_2.reset();
//        imageView.clearAnimation();
//        imageView.startAnimation(anim_2);

//        fromabove=AnimationUtils.loadAnimation(this,R.anim.fromabove);
//        imageView.setAnimation(fromabove);

        fl=AnimationUtils.loadAnimation(this,R.anim.fromleft);
        t1.setAnimation(fl);

        fr=AnimationUtils.loadAnimation(this,R.anim.fromright);
        t2.setAnimation(fr);

        iconrotation();

    }
 public void iconrotation() {
     RotateAnimation r1;
     r1 = new RotateAnimation(ROTATE_FROM, ROTATE_TO, Animation.RELATIVE_TO_SELF,
                            0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
     r1.setDuration((long)2* 1000);
     r1.setRepeatCount(0);
     imageView.startAnimation(r1);


 }

}


package com.mustafa.maraeialshamalia.Ui.splashScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.introScreens.IntroActivity;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_splash );

        SharedPreferences sharedPreferences = getSharedPreferences ( "app_language", Context.MODE_PRIVATE );
        String language = sharedPreferences.getString ( "language","en" );

        setLocale ( this,language );
        ImageView logo = findViewById(R.id.logo);

        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide );

        logo.startAnimation ( slideAnimation );

        Thread thread = new Thread ()
        {
            @Override
            public void run()
            {
                super.run ();

                try
                {
                    sleep ( 3000 );
                    finish ();
                    Intent startIntroActivity = new Intent (getApplicationContext (), IntroActivity.class );
                    startActivity ( startIntroActivity );

                } catch (InterruptedException e)
                {
                    e.printStackTrace ();
                }
            }
        };

        thread.start ();
    }

    private void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
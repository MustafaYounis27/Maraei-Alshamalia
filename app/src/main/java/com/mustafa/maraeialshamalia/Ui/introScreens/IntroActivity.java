package com.mustafa.maraeialshamalia.Ui.introScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.HomeActivity;

public class IntroActivity extends AppCompatActivity
{

    @Override
    protected void onStart() {
        super.onStart ();

        SharedPreferences sharedPreferences = getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        String token = sharedPreferences.getString ( "token", null );

        if(token != null)
        {
            startActivity ( new Intent (getApplicationContext (), HomeActivity.class ) );
            finish ();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_intro );

        loadFragment();
    }

    private void loadFragment()
    {
        getSupportFragmentManager ()
                .beginTransaction ()
                .add ( R.id.fragment_container, new IntroFragment () )
                .commit ();
    }
}
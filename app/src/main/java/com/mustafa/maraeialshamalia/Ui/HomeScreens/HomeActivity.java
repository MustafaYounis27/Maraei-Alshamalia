package com.mustafa.maraeialshamalia.Ui.HomeScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Auth.Auth;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.Auth.RegisterActivity;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    private ImageView profileImage;
    private TextView profileName;
    private boolean isAr = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_home );

        setupDrawer ();

        SharedPreferences sharedPreferences = getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        String token = sharedPreferences.getString ( "token", null );
        getProfile(token);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getProfile(String token)
    {
        RetrofitClient.getInstance ().getProfile ( token ).enqueue ( new Callback<Auth> ()
        {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response)
            {
                if(response.isSuccessful ())
                    if(response.code () == 200)
                        if(response.body () != null)
                            if(response.body ().getKey ().equals ( "success" ))
                            {
                                Picasso.get ()
                                        .load ( response.body ().getData ().getAvatar () )
                                        .placeholder ( R.mipmap.profile )
                                        .error ( R.mipmap.profile )
                                        .into ( profileImage );
                                profileName.setText ( response.body ().getData ().getName () );
                            }else if(response.body ().getKey ().equals ( "exit" ))
                                {
                                    loggingOut ();
                                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t)
            {

            }
        } );
    }


    private void setupDrawer() {
        drawerLayout = findViewById ( R.id.drawer_layout );

        NavController navController = Navigation.findNavController ( this, R.id.nav_host_fragment );
        NavigationView navView = findViewById ( R.id.nav_view );
        NavigationUI.setupWithNavController ( navView, navController );

        View header = navView.getHeaderView ( 0 );

        ImageView logout = header.findViewById ( R.id.logout );
        ImageView back = header.findViewById ( R.id.back );
        profileImage = header.findViewById ( R.id.profile_image);
        profileName = header.findViewById ( R.id.profile_name );

        back.setOnClickListener ( v -> drawerLayout.close () );

        logout.setOnClickListener ( v -> loggingOut () );
    }

    private void loggingOut()
    {
        RetrofitClient.getInstance ().logout ().enqueue ( new Callback<Auth> () {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if(response.isSuccessful ())
                    if(response.code () == 200)
                        if(response.body () != null)
                            if(response.body ().getKey ().equals ( "success" ))
                            {
                                Toast.makeText ( HomeActivity.this, response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                SharedPreferences sharedPreferences = getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
                                sharedPreferences.edit ().remove ( "token" ).apply ();
                                startActivity ( new Intent (getApplicationContext (), RegisterActivity.class ) );
                                finish ();
                            }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {

            }
        } );
    }

    public void openDrawer(View view)
    {
        drawerLayout.open ();
    }
}
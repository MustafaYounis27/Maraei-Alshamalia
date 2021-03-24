package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Auth.Auth;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.Auth.RegisterActivity;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.HomeActivity;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguageFragment extends Fragment
{
    private View mView;
    private RadioButton arLang, enLang;
    private TextView arabicText, englishText;
    private SharedPreferences sharedPreferences, sp;
    private String language, token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_language, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        sharedPreferences = getContext ().getSharedPreferences ( "app_language", Context.MODE_PRIVATE );
        language = sharedPreferences.getString ( "language","en" );

        sp = requireActivity ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        token = sp.getString ( "token", null );

        initViews();
    }

    private void setLang(String language)
    {
        if(language.equals ( "en" ))
        {
            enLang.setChecked ( true );
            arLang.setChecked ( false );
        }
        else
            {
                arLang.setChecked ( true );
                enLang.setChecked ( false );
            }
    }

    private void initViews()
    {
        enLang = mView.findViewById ( R.id.english );
        arLang = mView.findViewById ( R.id.arabic );

        arabicText = mView.findViewById ( R.id.arabic_txt );
        englishText = mView.findViewById ( R.id.english_txt );

        setLang(language);

        englishText.setOnClickListener ( v ->
        {
            language = "en";
            changeLanguage ();
        } );

        arabicText.setOnClickListener ( v ->
        {
            language = "ar";
            changeLanguage ();
        } );

        enLang.setOnCheckedChangeListener ( (buttonView, isChecked) ->
        {
            if(isChecked)
            {
                language = "en";
                changeLanguage ();
            }
        } );

        arLang.setOnCheckedChangeListener ( (buttonView, isChecked) ->
        {
            if (isChecked)
            {
                language = "ar";
                changeLanguage ();
            }
        } );

        openNotification ();
        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( v -> requireActivity ().onBackPressed () );
    }

    private void openNotification()
    {
        ImageView notification = mView.findViewById ( R.id.notification );
        notification.setOnClickListener ( v -> {
            NavController navController = Navigation.findNavController ( getActivity (), R.id.nav_host_fragment );
            navController.navigate ( R.id.action_languageFragment_to_notificationFragment );
        } );
    }

    private void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void changeLanguage()
    {
        RetrofitClient.getInstance ().changeLanguage ( token,language ).enqueue ( new Callback<Auth> ()
        {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if (response.body ().getKey ().equals ( "exit" ))
                            {
                                loggingOut ();
                            }else if(response.body ().getKey ().equals ( "success" ))
                            {
                                sharedPreferences.edit ().putString ( "language", language ).apply ();
                                setLocale ( getActivity (),language );
                                enLang.setChecked ( false );
                                arLang.setChecked ( false );
                                startActivity ( new Intent ( getContext (), HomeActivity.class ) );
                                requireActivity ().finish ();
                            }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t)
            {

            }
        } );
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
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                sp.edit ().remove ( "token" ).apply ();
                                startActivity ( new Intent (getContext (), RegisterActivity.class ) );
                                requireActivity ().finish ();
                            }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {

            }
        } );
    }
}

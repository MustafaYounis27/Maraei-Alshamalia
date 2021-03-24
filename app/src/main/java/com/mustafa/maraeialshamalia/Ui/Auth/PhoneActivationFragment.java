package com.mustafa.maraeialshamalia.Ui.Auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Auth.Auth;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneActivationFragment extends Fragment
{
    private View mView;
    private EditText activationCodeField;
    private int userId;

    public PhoneActivationFragment(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_phone_activation, null);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        Button button = mView.findViewById ( R.id.next );
        activationCodeField = mView.findViewById ( R.id.activation_code);
        button.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                int code = Integer.parseInt ( activationCodeField.getText ().toString () );

                RetrofitClient.getInstance ().activeCode ( userId,code ).enqueue ( new Callback<Auth> ()
                {
                    @Override
                    public void onResponse(Call<Auth> call, Response<Auth> response)
                    {
                        if(response.isSuccessful ())
                            if(response.code () == 200)
                                if(response.body () != null)
                                    if(response.body ().getKey ().equals ( "success" ))
                                    {
                                        SharedPreferences sharedPreferences = requireActivity ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
                                        sharedPreferences.edit ().putString ( "token", response.body ().getData ().getToken () ).apply ();

                                        startActivity ( new Intent (getContext (), HomeActivity.class) );
                                        requireActivity ().finish ();
                                    }else
                                        Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                    }

                    @Override
                    public void onFailure(Call<Auth> call, Throwable t)
                    {
                        Toast.makeText ( getContext (), t.getMessage (), Toast.LENGTH_SHORT ).show ();
                    }
                } );
            }
        } );
    }
}

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Auth.Auth;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.Auth.ResetPassword.ForgetPasswordFragment;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment
{
    private View mView;
    private EditText emailField, passwordField;
    private TextView forgetPassword, signUp;
    private Button signIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_login, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        initViews();
    }

    private void initViews()
    {
        emailField = mView.findViewById ( R.id.email_field );
        passwordField = mView.findViewById ( R.id.password_field );

        forgetPassword = mView.findViewById ( R.id.forget_password );
        signUp = mView.findViewById ( R.id.sign_up );

        signIn = mView.findViewById ( R.id.sign_in );

        onClicks();
    }

    private void onClicks()
    {
        signUp.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                requireActivity ().onBackPressed ();
            }
        } );

        signIn.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                if(!isValid ( emailField ))
                    return;
                if(!isValid ( passwordField ))
                    return;

                completeLogin ( emailField.getText ().toString (),passwordField.getText ().toString () );
            }
        } );

        forgetPassword.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                requireActivity ()
                        .getSupportFragmentManager ()
                        .beginTransaction ()
                        .replace ( R.id.fragment_container, new ForgetPasswordFragment () )
                        .addToBackStack ( null )
                        .commit ();
            }
        } );
    }

    private void completeLogin(String email, String password)
    {
        RetrofitClient.getInstance ().login ( email,password,"5555","android" ).enqueue ( new Callback<Auth> ()
        {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response)
            {
                if(response.isSuccessful ())
                {
                    if(response.code ()==200 && response.body () != null)
                    {
                        if(response.body ().getData ()!=null)
                        {
                            int user_id = response.body ().getData ().getUserId ();

                            if(response.body ().getKey ().equals ( "needActive" ))
                            {
                                requireActivity ()
                                        .getSupportFragmentManager ()
                                        .beginTransaction ()
                                        .replace ( R.id.fragment_container, new PhoneActivationFragment (user_id) )
                                        .addToBackStack ( null )
                                        .commit ();
                            }else if(response.body ().getKey ().equals ( "success" ))
                            {
                                SharedPreferences sharedPreferences = requireActivity ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
                                sharedPreferences.edit ().putString ( "token", response.body ().getData ().getToken () ).apply ();

                                startActivity ( new Intent (getContext (), HomeActivity.class ) );
                                requireActivity ().finish ();
                            }else
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                        }else
                            {
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                            }
                    }else
                        {
                            Toast.makeText ( getContext (), "not 200", Toast.LENGTH_SHORT ).show ();
                        }
                }
                else
                    {
                        Toast.makeText ( getContext (), "not success", Toast.LENGTH_SHORT ).show ();
                    }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t)
            {

            }
        } );
    }

    private boolean isValid(View view)
    {
        EditText editText = (EditText) view;
        String value = editText.getText ().toString ();

        if(value.isEmpty ())
        {
            Toast.makeText ( getContext (), "يرجى ادخال " + editText.getHint (), Toast.LENGTH_SHORT ).show ();
            editText.requestFocus ();
            return false;
        }
        else
            return true;
    }
}

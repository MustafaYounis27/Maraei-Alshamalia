package com.mustafa.maraeialshamalia.Ui.Auth.ResetPassword;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetCodeFragment extends Fragment
{
    private View mView;
    private final int userId;
    private EditText resetCodeField;

    public ResetCodeFragment(int userId)
    {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_reset_code, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        Button next = mView.findViewById ( R.id.next );
        resetCodeField = mView.findViewById ( R.id.reset_code_field );

        next.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                int code = Integer.parseInt ( resetCodeField.getText ().toString () );
                requireActivity ()
                        .getSupportFragmentManager ()
                        .beginTransaction ()
                        .replace ( R.id.fragment_container, new resetPasswordFragment (userId,code ))
                        .addToBackStack ( null )
                        .commit ();
            }
        } );

        TextView resendCode = mView.findViewById ( R.id.resend_code );

        resendCode.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                RetrofitClient.getInstance ().resendCode ( userId ).enqueue ( new Callback<Auth> ()
                {
                    @Override
                    public void onResponse(Call<Auth> call, Response<Auth> response)
                    {
                        if(response.isSuccessful ())
                            if(response.code () == 200)
                                if(response.body () != null)
                                    Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                    }

                    @Override
                    public void onFailure(Call<Auth> call, Throwable t)
                    {

                    }
                } );
            }
        } );
    }
}

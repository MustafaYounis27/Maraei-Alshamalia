package com.mustafa.maraeialshamalia.Ui.Auth.ResetPassword;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordFragment extends Fragment
{
    private View mView;
    private EditText phoneField;
    private Button next;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_forget_password, null );
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
        phoneField = mView.findViewById ( R.id.phone_field );
        next = mView.findViewById ( R.id.next );

        next.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                String phone = phoneField.getText ().toString ();

                RetrofitClient.getInstance ().forgetPassword ( phone ).enqueue ( new Callback<Auth> ()
                {
                    @Override
                    public void onResponse(Call<Auth> call, Response<Auth> response)
                    {
                        if (response.isSuccessful ())
                        {
                            if (response.code () == 200 && response.body () != null)
                            {
                                if (response.body ().getKey ().equals ( "success" ))
                                {
                                    Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();

                                    requireActivity ()
                                            .getSupportFragmentManager ()
                                            .beginTransaction ()
                                            .add ( R.id.fragment_container, new ResetCodeFragment (response.body ().getData ().getUserId ()) )
                                            .addToBackStack ( null )
                                            .commit ();
                                }
                            }
                        }
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

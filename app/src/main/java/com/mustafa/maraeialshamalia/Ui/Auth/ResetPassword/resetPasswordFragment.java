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

public class resetPasswordFragment extends Fragment
{
    private View mView;
    private EditText passwordField, confirmPasswordField;
    private int code, userId;

    public resetPasswordFragment(int userId, int code)
    {
        this.userId = userId;
        this.code = code;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_reset_password, null);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        Button confirm = mView.findViewById ( R.id.confirm );

        passwordField = mView.findViewById ( R.id.password_field );
        confirmPasswordField = mView.findViewById ( R.id.confirm_password_field );

        confirm.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                String password = passwordField.getText ().toString ();
                String confirmPassword = confirmPasswordField.getText ().toString ();
                if(password.isEmpty ())
                {
                   passwordField.requestFocus ();
                   Toast.makeText ( getContext (), "يرجى ادخال كلمة المرور", Toast.LENGTH_SHORT ).show ();
                   return;
                }

                if(!confirmPassword.equals ( password ))
                {
                    if(confirmPassword.isEmpty ())
                    {
                        confirmPasswordField.requestFocus ();
                        Toast.makeText ( getContext (), "يرجى تأكيد كلمة المرور", Toast.LENGTH_SHORT ).show ();
                    }
                    else
                        {
                            passwordField.requestFocus ();
                            Toast.makeText ( getContext (), "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT ).show ();
                        }
                    return;
                }

                resetPassword(userId,code,password);
            }
        } );
    }

    private void resetPassword(int userId, int code, String password)
    {
        RetrofitClient.getInstance ().updatePassword ( userId, code, password ).enqueue ( new Callback<Auth> () {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response)
            {
                if(response.isSuccessful ())
                    if(response.code () == 200)
                        if(response.body () != null)
                            if(response.body ().getKey ().equals ( "success" ))
                            {
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                requireActivity ().getSupportFragmentManager ().popBackStack ();
                                requireActivity ().getSupportFragmentManager ().popBackStack ();
                                requireActivity ().onBackPressed ();
                            }else
                                {
                                    Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                    requireActivity ().onBackPressed ();
                                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t)
            {

            }
        } );
    }
}

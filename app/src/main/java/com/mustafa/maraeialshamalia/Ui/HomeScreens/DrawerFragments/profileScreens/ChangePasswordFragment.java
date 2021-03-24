package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments.profileScreens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.internal.$Gson$Preconditions;
import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Auth.Auth;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.Auth.RegisterActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment
{
    private View mView;
    private EditText oldPasswordField, newPasswordField, confirmNewPasswordField;
    private ImageView showOldPassword, showNewPassword, showConfirmNewPassword;
    private Button saveChangesBtn;
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_change_password, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        token = sharedPreferences.getString ( "token", null );

        initViews();
    }

    private void initViews()
    {
        oldPasswordField = mView.findViewById ( R.id.old_password_field );
        newPasswordField = mView.findViewById ( R.id.new_password_field );
        confirmNewPasswordField = mView.findViewById ( R.id.confirm_new_password_field );
        saveChangesBtn = mView.findViewById ( R.id.save_changes_btn );
        oldPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmNewPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        showOldPassword = mView.findViewById ( R.id.show_old_password );
        showNewPassword = mView.findViewById ( R.id.show_new_password );
        showConfirmNewPassword = mView.findViewById ( R.id.show_confirm_new_password );

        openNotification ();
        onClicks();
    }

    private void openNotification()
    {
        ImageView notification = mView.findViewById ( R.id.notification );
        notification.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                NavController navController = Navigation.findNavController ( getActivity (), R.id.nav_host_fragment );
                navController.navigate ( R.id.action_profileFragment_to_notificationFragment );
            }
        } );
    }

    private void onClicks()
    {
        saveChangesBtn.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                String oldPassword = oldPasswordField.getText ().toString ();
                String newPassword = newPasswordField.getText ().toString ();
                String confirmNewPassword = confirmNewPasswordField.getText ().toString ();

                chickValid(oldPassword,newPassword,confirmNewPassword);
            }
        } );

        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                requireActivity ().onBackPressed ();
            }
        } );

        showOldPassword.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                if(oldPasswordField.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
                    //Show Password
                    oldPasswordField.setTransformationMethod( HideReturnsTransformationMethod.getInstance());
                else
                    //Hide Password
                    oldPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        } );

        showNewPassword.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                if(newPasswordField.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
                    //Show Password
                    newPasswordField.setTransformationMethod( HideReturnsTransformationMethod.getInstance());
                else
                    //Hide Password
                    newPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        } );

        showConfirmNewPassword.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                if(confirmNewPasswordField.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
                    //Show Password
                    confirmNewPasswordField.setTransformationMethod( HideReturnsTransformationMethod.getInstance());
                else
                    //Hide Password
                    confirmNewPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        } );
    }

    private void chickValid(String oldPassword, String newPassword, String confirmNewPassword)
    {
        if (oldPassword.isEmpty ())
        {
            oldPasswordField.requestFocus ();
            Toast.makeText ( getContext (), "برجاء ادخال كلمة المرور القديمة", Toast.LENGTH_SHORT ).show ();
            return;
        }

        if (newPassword.isEmpty ())
        {
            newPasswordField.requestFocus ();
            Toast.makeText ( getContext (), "برجاء ادخال كلمة المرور الجديدة", Toast.LENGTH_SHORT ).show ();
            return;
        }

        if (!confirmNewPassword.equals ( newPassword ))
        {
            newPasswordField.requestFocus ();
            Toast.makeText ( getContext (), "كلمة المرور الجديدةغير متطابقة", Toast.LENGTH_SHORT ).show ();
            return;
        }

        resetPassword(oldPassword,newPassword);

    }

    private void resetPassword(String oldPassword, String newPassword)
    {
        RetrofitClient.getInstance ().resetPassword ( token,oldPassword,newPassword ).enqueue ( new Callback<Auth> ()
        {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                        {
                            Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();

                            if (response.body ().getKey ().equals ( "success" ))
                                requireActivity ().onBackPressed ();
                            else
                                {
                                    if(response.body ().getData () != null)
                                    {
                                        startActivity ( new Intent (getContext (), RegisterActivity.class ) );
                                        requireActivity ().finish ();
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
}

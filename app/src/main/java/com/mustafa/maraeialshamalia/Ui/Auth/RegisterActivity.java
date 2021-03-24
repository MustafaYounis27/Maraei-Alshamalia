package com.mustafa.maraeialshamalia.Ui.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Auth.Auth;
import com.mustafa.maraeialshamalia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity
{
    private EditText usernameField, emailField, phoneField, passwordField, confirmPasswordField;
    private CheckBox agree;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_register );

        initViews();
    }

    private void initViews()
    {
        usernameField = findViewById ( R.id.username_field );
        emailField = findViewById ( R.id.email_field );
        phoneField = findViewById ( R.id.phone_field );
        passwordField = findViewById ( R.id.password_field );
        confirmPasswordField = findViewById ( R.id.confirm_password_field );

        agree = findViewById ( R.id.agree );
    }

    public void back(View view)
    {
        onBackPressed ();
    }

    public void next(View view)
    {
        String username = usernameField.getText ().toString ();
        String email = emailField.getText ().toString ();
        String phone = phoneField.getText ().toString ();
        String password = passwordField.getText ().toString ();
        String confirmPassword = confirmPasswordField.getText ().toString ();

        boolean isAgree = agree.isChecked ();

        if(!isValid ( usernameField ))
            return;
        if(!isValid ( emailField ))
            return;
        if(!isValid ( phoneField ))
            return;
        if(!isValid ( passwordField ))
            return;
        if(!isValid ( confirmPasswordField ))
            return;

        if(!isAgree)
        {
            Toast.makeText ( this, "يرجى الموافقة على الشروط والاحكام", Toast.LENGTH_SHORT ).show ();
            return;
        }

        if(!confirmPassword.equals ( password ))
        {
            Toast.makeText ( this, "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT ).show ();
            passwordField.requestFocus ();
            return;
        }

        completeRegistration(username,email,phone,password);
    }

    private void completeRegistration(String username, String email, String phone, String password)
    {
        RetrofitClient.getInstance().register(username,email,phone,password).enqueue ( new Callback<Auth> () {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if(response.isSuccessful ()){
                    if(response.code ()==200){
                        if(response.body ().getData ()!=null){
                            int user_id=response.body ().getData ().getUserId ();
                            Toast.makeText ( RegisterActivity.this, ""+user_id, Toast.LENGTH_SHORT ).show ();

                            getSupportFragmentManager ()
                                    .beginTransaction ()
                                    .replace ( R.id.fragment_container, new PhoneActivationFragment (user_id) )
                                    .addToBackStack ( null )
                                    .commit ();

                        }else{
                            Toast.makeText ( RegisterActivity.this, "null", Toast.LENGTH_SHORT ).show ();
                        }
                    }else{
                        Toast.makeText ( RegisterActivity.this, "not 200", Toast.LENGTH_SHORT ).show ();
                    }
                }
                else{
                    Toast.makeText ( RegisterActivity.this, "not success", Toast.LENGTH_SHORT ).show ();
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Toast.makeText ( RegisterActivity.this, "fail", Toast.LENGTH_SHORT ).show ();
            }
        } );

    }

    private boolean isValid(View view)
    {
        EditText editText = (EditText) view;
        String value = editText.getText ().toString ();

        if(value.isEmpty ())
        {
            Toast.makeText ( this, "يرجى ادخال " + editText.getHint (), Toast.LENGTH_SHORT ).show ();
            editText.requestFocus ();
            return false;
        }
        else
            return true;
    }

    public void signIn(View view)
    {
        getSupportFragmentManager ()
                .beginTransaction ()
                .add ( R.id.fragment_container, new LoginFragment () )
                .addToBackStack ( null )
                .commit ();
    }
}
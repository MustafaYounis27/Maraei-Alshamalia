package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments;

import android.os.Bundle;
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

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Auth.Auth;
import com.mustafa.maraeialshamalia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsFragment extends Fragment
{
    private View mView;
    private EditText nameField, emailField, messageField;
    private Button sendBtn;
    private ImageView facebook, whatsapp, twitter, instagram;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_contact_us, null );
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
        nameField = mView.findViewById ( R.id.name_field );
        emailField = mView.findViewById ( R.id.email_field );
        messageField = mView.findViewById ( R.id.message_field );
        sendBtn = mView.findViewById ( R.id.send_btn );
        facebook = mView.findViewById ( R.id.facebook );
        whatsapp = mView.findViewById ( R.id.whatsapp );
        twitter = mView.findViewById ( R.id.twitter );
        instagram = mView.findViewById ( R.id.instagram );

        onClicks();
        openNotification ();
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
                navController.navigate ( R.id.action_contactUsFragment_to_notificationFragment );
            }
        } );
    }

    private void onClicks()
    {
        sendBtn.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                String name = nameField.getText ().toString ();
                String email = emailField.getText ().toString ();
                String message = messageField.getText ().toString ();

                checkValid(name,email,message);
            }
        } );

        facebook.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

            }
        } );

        whatsapp.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

            }
        } );

        twitter.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

            }
        } );

        instagram.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

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
    }

    private void checkValid(String name, String email, String message)
    {
        if(name.isEmpty ())
        {
            nameField.requestFocus ();
            Toast.makeText ( getContext (), "برجاء كتابة الاسم", Toast.LENGTH_SHORT ).show ();
            return;
        }

        if(email.isEmpty ())
        {
            emailField.requestFocus ();
            Toast.makeText ( getContext (), "برجاء كتابة البريد الالكترونى", Toast.LENGTH_SHORT ).show ();
            return;
        }

        if(message.isEmpty ())
        {
            messageField.requestFocus ();
            Toast.makeText ( getContext (), "برجاء كتابة رسالتك", Toast.LENGTH_SHORT ).show ();
            return;
        }

        completeSend(name,email,message);
    }

    private void completeSend(String name, String email, String message)
    {
        RetrofitClient.getInstance ().contactUs ( name, email, message ).enqueue ( new Callback<Auth> ()
        {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t)
            {
                Toast.makeText ( getContext (), t.getMessage (), Toast.LENGTH_SHORT ).show ();
            }
        } );
    }
}

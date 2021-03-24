package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments.profileScreens;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mustafa.maraeialshamalia.Data.RealPathUtil;
import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Auth.Auth;
import com.mustafa.maraeialshamalia.Models.Auth.Data;
import com.mustafa.maraeialshamalia.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment
{
    private ImageView back;
    private View mView;
    private CircleImageView profileImage;
    private EditText nameField, emailField, phoneField;
    private ImageView editName, editEmail, editPhone;
    private Button changePasswordBtn, saveChangesBtn;
    private String token;
    private Data data;
    Uri image_uri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_proile, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        token = sharedPreferences.getString ( "token", null );

        getProfile(token);

    }

    private void getProfile(String token)
    {
        RetrofitClient.getInstance ().getProfile ( token ).enqueue ( new Callback<Auth> ()
        {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if (response.body ().getKey ().equals ( "success" ))
                            {
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                data = response.body ().getData ();
                                initViews();
                            }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t)
            {

            }
        } );
    }

    private void initViews()
    {
        profileImage = mView.findViewById ( R.id.profile_image );
        nameField = mView.findViewById ( R.id.name_field );
        emailField = mView.findViewById ( R.id.email_field );
        phoneField = mView.findViewById ( R.id.phone_field );
        editName = mView.findViewById ( R.id.edit_name );
        editEmail = mView.findViewById ( R.id.edit_email );
        editPhone = mView.findViewById ( R.id.edit_phone );
        changePasswordBtn = mView.findViewById ( R.id.change_password_btn );
        saveChangesBtn = mView.findViewById ( R.id.save_changes_btn );

        nameField.setText ( data.getName () );
        emailField.setText ( data.getEmail () );
        phoneField.setText ( data.getPhone () );
        Picasso.get ().load ( data.getAvatar () ).into ( profileImage );

        onClicks();
        openNotification ();
        profileImage.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 505);
            }
        } );
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == 505 && resultCode == requireActivity ().RESULT_OK && data != null) {
            image_uri = data.getData ();
            Picasso.get ().load ( image_uri ).into ( profileImage );
        }
    }

    private void openNotification()
    {
        ImageView notification = mView.findViewById ( R.id.notification );
        notification.setOnClickListener ( v -> {
            NavController navController = Navigation.findNavController ( getActivity (), R.id.nav_host_fragment );
            navController.navigate ( R.id.action_profileFragment_to_notificationFragment );
        } );
    }

    private void onClicks()
    {
        saveChangesBtn.setOnClickListener ( v -> {
            checkValid ();
            Toast.makeText ( getContext (), "save", Toast.LENGTH_SHORT ).show ();
        } );

        changePasswordBtn.setOnClickListener ( v -> {
            NavController navController = Navigation.findNavController ( getActivity (), R.id.nav_host_fragment );
            navController.navigate ( R.id.action_profileFragment_to_changePasswordFragment );
        } );

        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( v -> requireActivity ().onBackPressed () );

        editName.setOnClickListener ( v -> {
            nameField.setEnabled ( true );
            nameField.requestFocus ();
        } );

        editEmail.setOnClickListener ( v -> {
            emailField.setEnabled ( true );
            emailField.requestFocus ();
        } );

        editPhone.setOnClickListener ( v -> {
            phoneField.setEnabled ( true );
            phoneField.requestFocus ();
        } );
    }

    private void checkValid()
    {
        String name = nameField.getText ().toString ();
        String email = emailField.getText ().toString ();
        String phone = phoneField.getText ().toString ();

        if(!name.isEmpty () && !email.isEmpty () && !phone.isEmpty () && token != null)
            updateProfile(name,email,phone);
    }

    private void updateProfile(String name, String email, String phone)
    {
        File file = new File( RealPathUtil.getRealPath(getContext(), image_uri));
        RequestBody requestFile = RequestBody.create( MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        RetrofitClient.getInstance ().updateProfile ( body, token, name, email, phone ).enqueue ( new Callback<Auth> () {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if (response.body ().getKey ().equals ( "success" )) {
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();

                                nameField.setText ( response.body ().getData ().getName () );
                                emailField.setText ( response.body ().getData ().getEmail () );
                                phoneField.setText ( response.body ().getData ().getPhone () );
                            } else
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                        else
                            Toast.makeText ( getContext (), "null", Toast.LENGTH_SHORT ).show ();
                    else
                        Toast.makeText ( getContext (), "100", Toast.LENGTH_SHORT ).show ();
                else
                    Toast.makeText ( getContext (), "not success", Toast.LENGTH_SHORT ).show ();
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Toast.makeText ( getContext (), t.getMessage (), Toast.LENGTH_SHORT ).show ();
            }
        } );
    }
}

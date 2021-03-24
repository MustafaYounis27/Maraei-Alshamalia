package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Product.Product;
import com.mustafa.maraeialshamalia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsFragment extends Fragment
{
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_terms, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "app_language", Context.MODE_PRIVATE );
        String language = sharedPreferences.getString ( "language","en" );

        getTerms(language);
        openNotification ();
        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( v -> requireActivity ().onBackPressed () );
    }

    private void openNotification()
    {
        ImageView notification = mView.findViewById ( R.id.notification );
        notification.setOnClickListener ( v -> {
            NavController navController = Navigation.findNavController ( getActivity (), R.id.nav_host_fragment );
            navController.navigate ( R.id.action_termsFragment_to_notificationFragment );
        } );
    }

    private void getTerms(String language)
    {
        RetrofitClient.getInstance ().getTerms (language).enqueue ( new Callback<Product> ()
        {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if(response.body ().getKey ().equals ( "success" ))
                            {
                                TextView termsText = mView.findViewById ( R.id.terms_field );
                                termsText.setText ( response.body ().getData ().getDesc () );
                            }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t)
            {

            }
        } );
    }
}

package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import com.google.gson.internal.$Gson$Preconditions;
import com.mustafa.maraeialshamalia.R;

public class WalletFragment extends Fragment
{
    private View mView;
    private Button chargeBalance;
    private TextView balance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_wallet, null );
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
        balance = mView.findViewById ( R.id.balance );
        chargeBalance = mView.findViewById ( R.id.charge_balance );

        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                requireActivity ().onBackPressed ();
            }
        } );

        chargeBalance.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                NavController navController = Navigation.findNavController ( requireActivity (), R.id.nav_host_fragment );
                navController.navigate ( R.id.action_walletFragment_to_chargingFragment );
            }
        } );
    }
}

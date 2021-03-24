package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Offers.Data;
import com.mustafa.maraeialshamalia.Models.Offers.Offers;
import com.mustafa.maraeialshamalia.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersFragment extends Fragment
{
    private View mView;
    private RecyclerView offersRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_offers, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        initViews();
        initRecycler();
        getOffers();
    }

    private void getOffers()
    {
        RetrofitClient.getInstance ().getOffers ().enqueue ( new Callback<Offers> ()
        {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response)
            {
                if(response.isSuccessful ())
                    if(response.code () == 200)
                        if(response.body () != null)
                            if(response.body ().getKey ().equals ( "success" ))
                            {
                                Toast.makeText ( getContext (), response.body ().getKey (), Toast.LENGTH_SHORT ).show ();
                                offersRecycler.setAdapter ( new OffersAdapter ( response.body ().getData () ) );
                            }else
                                Toast.makeText ( getContext (), response.body ().getKey (), Toast.LENGTH_SHORT ).show ();
                            else
                            Toast.makeText ( getContext (), "null", Toast.LENGTH_SHORT ).show ();
                            else
                                Toast.makeText ( getContext (), "100", Toast.LENGTH_SHORT ).show ();
                            else
                                Toast.makeText ( getContext (), "fail", Toast.LENGTH_SHORT ).show ();

            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t)
            {
                Toast.makeText ( getContext (), t.getMessage (), Toast.LENGTH_SHORT ).show ();
            }
        } );
    }

    private void initViews()
    {
        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                requireActivity ().onBackPressed ();
            }
        } );

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
                navController.navigate ( R.id.action_offersFragment_to_notificationFragment );
            }
        } );
    }

    private void initRecycler()
    {
        offersRecycler = mView.findViewById ( R.id.offer_recycler );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( getContext (), RecyclerView.VERTICAL, false );
        offersRecycler.setLayoutManager ( layoutManager );
    }

    class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersVH>
    {
        List<Data> offers;
        public OffersAdapter(List<Data> offers)
        {
            this.offers = offers;
        }

        @NonNull
        @Override
        public OffersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.offer_item, parent, false );
            return new OffersVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull OffersVH holder, int position)
        {
            Data offer = offers.get ( position );

            holder.cardView.setOnClickListener ( new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    NavController navController = Navigation.findNavController ( requireActivity (), R.id.nav_host_fragment );
                    Bundle bundle = new Bundle ();
                    bundle.putInt ( "product_id", offer.getProductId () );
                    navController.navigate ( R.id.action_offersFragment_to_productFragment, bundle);
                }
            } );
        }

        @Override
        public int getItemCount()
        {
            return offers.size ();
        }

        class OffersVH extends RecyclerView.ViewHolder
        {
            ImageView imageView;
            CardView cardView;

            public OffersVH(@NonNull View itemView)
            {
                super ( itemView );

                imageView = itemView.findViewById ( R.id.image );
                cardView = itemView.findViewById ( R.id.offer );
            }
        }
    }
}

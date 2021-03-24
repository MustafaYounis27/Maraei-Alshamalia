package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.mustafa.maraeialshamalia.Models.Favourites.Data;
import com.mustafa.maraeialshamalia.Models.Favourites.Favourites;
import com.mustafa.maraeialshamalia.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesFragment extends Fragment
{
    private View mView;
    private RecyclerView favouriteRecycler;
    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_favourites, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        token = sharedPreferences.getString ( "token", null );

        initViews();
        initRecycler();
        getFavourites(token);
    }

    private void getFavourites(String token)
    {
        RetrofitClient.getInstance ().getFavourites ( token ).enqueue ( new Callback<Favourites> ()
        {
            @Override
            public void onResponse(Call<Favourites> call, Response<Favourites> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if (response.body ().getKey ().equals ( "success" ))
                            {
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                favouriteRecycler.setAdapter ( new FavouriteAdapter (response.body ().getData ()) );
                            }else
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                            else
                                Toast.makeText ( getContext (), "null", Toast.LENGTH_SHORT ).show ();
            }

            @Override
            public void onFailure(Call<Favourites> call, Throwable t)
            {
                Toast.makeText ( getContext (), t.getMessage (), Toast.LENGTH_LONG ).show ();
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
                NavController navController = Navigation.findNavController ( requireActivity (), R.id.nav_host_fragment );
                navController.navigate ( R.id.action_favouritesFragment_to_notificationFragment );
            }
        } );
    }

    private void initRecycler()
    {
        favouriteRecycler = mView.findViewById ( R.id.favourite_recycler );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( getContext (), RecyclerView.VERTICAL, false );
        favouriteRecycler.setLayoutManager ( layoutManager );
    }

    class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteVH>
    {
        List<Data> favourites;

        public FavouriteAdapter(List<Data> favourites) {
            this.favourites = favourites;
        }

        @NonNull
        @Override
        public FavouriteAdapter.FavouriteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.favourite_item, parent, false );
            return new FavouriteVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull FavouriteAdapter.FavouriteVH holder, int position)
        {
            Data favouriteProduct = favourites.get ( position );

            holder.productName.setText ( favouriteProduct.getName () );
            holder.rate.setRating ( favouriteProduct.getAvgRate () );
            holder.avgRate.setText ( String.valueOf ( favouriteProduct.getAvgRate () ) );
            holder.minPrice.setText ( "ابتداء من " + favouriteProduct.getMinPrice ()+" ر.س" );

            holder.details.setOnClickListener ( new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    NavController navController = Navigation.findNavController ( requireActivity (), R.id.nav_host_fragment );
                    Bundle bundle = new Bundle ();
                    bundle.putInt ( "product_id", favouriteProduct.getId () );
                    navController.navigate ( R.id.action_favouritesFragment_to_productFragment, bundle);
                }
            } );

            holder.delete.setOnClickListener ( new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    RetrofitClient.getInstance ().toggleFavouriteState ( token,favouriteProduct.getId () ).enqueue ( new Callback<Favourites> ()
                    {
                        @Override
                        public void onResponse(Call<Favourites> call, Response<Favourites> response)
                        {
                            if (response.isSuccessful ())
                                if (response.code () == 200)
                                    if (response.body () != null)
                                    {
                                        Toast.makeText ( getContext (), "تم الازالة من المفضلة", Toast.LENGTH_SHORT ).show ();
                                        getFavourites ( token );
                                    }
                        }

                        @Override
                        public void onFailure(Call<Favourites> call, Throwable t)
                        {

                        }
                    } );
                }
            } );
        }

        @Override
        public int getItemCount()
        {
            return favourites.size ();
        }

        class FavouriteVH extends RecyclerView.ViewHolder
        {
            ImageView image;
            TextView productName, avgRate, minPrice, details, delete;
            RatingBar rate;

            public FavouriteVH(@NonNull View itemView)
            {
                super ( itemView );

                image = itemView.findViewById ( R.id.favourite_image );
                productName = itemView.findViewById ( R.id.product_name );
                avgRate = itemView.findViewById ( R.id.avg_rate );
                minPrice = itemView.findViewById ( R.id.min_price );
                rate = itemView.findViewById ( R.id.rate );
                details = itemView.findViewById ( R.id.details );
                delete = itemView.findViewById ( R.id.delete );
            }
        }
    }
}

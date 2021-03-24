package com.mustafa.maraeialshamalia.Ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Search.Datum;
import com.mustafa.maraeialshamalia.Models.Search.Search;
import com.mustafa.maraeialshamalia.Models.Section.Data;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens.SectionFragment;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments.ProductFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment
{
    private View mView;
    private RecyclerView searchRecycler;
    private EditText searchField;
    private ImageView search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate ( R.layout.fragment_search, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        initViews();
        initRecycler ();
    }

    private void initViews()
    {
        searchField = mView.findViewById ( R.id.search_field );
        search = mView.findViewById ( R.id.search );

        search.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String search = searchField.getText ().toString ();

                getSearch(search);
            }
        } );
    }

    private void getSearch(String search)
    {
        RetrofitClient.getInstance ().search ( search ).enqueue ( new Callback<Search> ()
        {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if(response.body ().getKey ().equals ( "success" ))
                            {
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                searchRecycler.setAdapter ( new ProductAdapter ( response.body ().getData ().getData () ) );
                            }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t)
            {

            }
        } );
    }

    private void initRecycler()
    {
        searchRecycler = mView.findViewById ( R.id.search_recycler );
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager ( getContext (), 2, RecyclerView.VERTICAL,false );
        searchRecycler.setLayoutManager ( layoutManager );
    }

    class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductVH>
    {
        List<Datum> products;

        public ProductAdapter(List<Datum> products) {
            this.products = products;
        }

        @NonNull
        @Override
        public ProductAdapter.ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.product_section, parent, false );
            return new ProductAdapter.ProductVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull ProductAdapter.ProductVH holder, int position)
        {
            Datum product = products.get ( position );

            holder.sectionName.setText ( product.getSectionName () );
            holder.productName.setText ( product.getSectionName () );
            holder.rate.setRating ( product.getAvgRate () );
            holder.avgRate.setText ( String.valueOf ( product.getAvgRate () ) );
            holder.minPrice.setText ( "ابتداء من " + product.getMinPrice ()+" ر.س" );

            holder.openProduct.setOnClickListener ( new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    requireActivity ()
                            .getSupportFragmentManager ()
                            .beginTransaction ()
                            .replace ( R.id.nav_host_fragment, new ProductFragment (product.getId ()) )
                            .addToBackStack ( null )
                            .commit ();
                }
            } );

            Picasso.get ()
                    .load ( product.getImage () )
                    .placeholder ( R.mipmap.productimage )
                    .error ( R.mipmap.productimage )
                    .into ( holder.image );
        }

        @Override
        public int getItemCount()
        {
            return products.size ();
        }

        class ProductVH extends RecyclerView.ViewHolder
        {
            ImageView image;
            TextView sectionName, productName, avgRate, minPrice;
            RatingBar rate;
            CardView openProduct;

            public ProductVH(@NonNull View itemView)
            {
                super ( itemView );

                image = itemView.findViewById ( R.id.image );
                sectionName = itemView.findViewById ( R.id.section_name );
                productName = itemView.findViewById ( R.id.product_name );
                avgRate = itemView.findViewById ( R.id.avg_rate );
                minPrice = itemView.findViewById ( R.id.min_price );
                rate = itemView.findViewById ( R.id.rate );
                openProduct = itemView.findViewById ( R.id.open_product );
            }
        }
    }
}

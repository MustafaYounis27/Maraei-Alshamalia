package com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Section.Data;
import com.mustafa.maraeialshamalia.Models.Section.Section;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments.NotificationFragment;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments.ProductFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectionFragment extends Fragment
{
    private View mView;
    private TextView sectionName;
    private ImageView filter;
    private RecyclerView productRecycler;

    private final int sectionId;
    private String priceSort = "desc", language;

    public SectionFragment(int sectionId)
    {
        this.sectionId = sectionId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_section, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "app_language", Context.MODE_PRIVATE );
        language = sharedPreferences.getString ( "language","en" );

        getSection ();
        initViews();
        initRecycler();
    }

    private void getSection()
    {
        RetrofitClient.getInstance ().getSection ( language,sectionId,priceSort ).enqueue ( new Callback<Section> ()
        {
            @Override
            public void onResponse(Call<Section> call, Response<Section> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if(response.body ().getKey ().equals ( "success" ))
                            {
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                productRecycler.setAdapter ( new ProductAdapter ( response.body ().getData () ) );
                            }

            }

            @Override
            public void onFailure(Call<Section> call, Throwable t)
            {

            }
        } );
    }

    private void initViews()
    {
        sectionName = mView.findViewById ( R.id.section_name );
        filter = mView.findViewById ( R.id.filter );

        filter.setOnClickListener ( (View.OnClickListener) v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog ( getContext (), R.style.BottomSheetDialogTheme );

            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.filter_dialog, (LinearLayout)mView.findViewById ( R.id.bottom_container ) );

            bottomSheetDialog.setContentView ( view );
            bottomSheetDialog.show ();

            view.findViewById ( R.id.asc ).setOnClickListener ( v12 -> {
                priceSort = "asc";
                getSection ();
                bottomSheetDialog.dismiss ();
            } );

            view.findViewById ( R.id.desc ).setOnClickListener ( v1 -> {
                priceSort = "desc";
                getSection ();
                bottomSheetDialog.dismiss ();
            } );
        } );

        back();
        openNotification ();
    }

    private void back()
    {
        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( v -> requireActivity ().onBackPressed () );
    }

    private void openNotification()
    {
        ImageView notification = mView.findViewById ( R.id.notification );
        notification.setOnClickListener ( v -> requireActivity ()
                .getSupportFragmentManager ()
                .beginTransaction ()
                .replace ( R.id.nav_host_fragment, new NotificationFragment () )
                .addToBackStack ( null )
                .commit () );
    }

    private void initRecycler()
    {
        productRecycler = mView.findViewById ( R.id.product_recycler );
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager ( getContext (), 2, RecyclerView.VERTICAL,false );
        productRecycler.setLayoutManager ( layoutManager );
    }

    class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductVH>
    {
        List<Data> products;

        public ProductAdapter(List<Data> products) {
            this.products = products;
        }

        @NonNull
        @Override
        public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.product_section, parent, false );
            return new ProductVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull ProductVH holder, int position)
        {
            Data product = products.get ( position );

            holder.sectionName.setText ( product.getSectionName () );
            sectionName.setText ( product.getSectionName () );
            holder.productName.setText ( product.getSectionName () );
            holder.rate.setRating ( product.getAvgRate () );
            holder.avgRate.setText ( String.valueOf ( product.getAvgRate () ) );
            holder.minPrice.append ( " " + product.getMinPrice () );

            holder.openProduct.setOnClickListener ( v -> requireActivity ()
                    .getSupportFragmentManager ()
                    .beginTransaction ()
                    .replace ( R.id.nav_host_fragment, new ProductFragment (product.getId ()) )
                    .addToBackStack ( null )
                    .commit () );

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

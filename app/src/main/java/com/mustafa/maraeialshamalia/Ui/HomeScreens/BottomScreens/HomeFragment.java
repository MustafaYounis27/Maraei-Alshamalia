package com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Home.Home;
import com.mustafa.maraeialshamalia.Models.Home.HomeProduct;
import com.mustafa.maraeialshamalia.Models.Home.Section;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments.ProductFragment;
import com.mustafa.maraeialshamalia.Ui.SearchFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment
{
    private View mView;
    private Home homeItems;
    private RecyclerView sectionsRecycler, sliderRecycler;
    private String language;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_home, null);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "app_language", Context.MODE_PRIVATE );
        language = sharedPreferences.getString ( "language","en" );

        initRecycler();

        ImageView openSearch = mView.findViewById ( R.id.open_search );
        openSearch.setOnClickListener ( v ->
                requireActivity ()
                        .getSupportFragmentManager ()
                        .beginTransaction ()
                        .replace ( R.id.nav_host_fragment, new SearchFragment () )
                        .addToBackStack ( null )
                        .commit ()
        );
        getHomeData();
    }

    private void getHomeData()
    {
        RetrofitClient.getInstance ().getHomeItems (language).enqueue ( new Callback<Home> ()
        {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response)
            {
                if(response.isSuccessful ())
                    if(response.code () == 200)
                        if(response.body () != null)
                            if(response.body ().getKey ().equals ( "success" ))
                            {
                                homeItems = response.body ();
                                sectionsRecycler.setAdapter ( new SectionAdapter ( homeItems.getData ().getSections () ) );
                                sliderRecycler.setAdapter ( new SliderAdapter (homeItems.getData ().getSlider ()) );
                            }else
                                Toast.makeText ( getContext (), response.body ().getKey (), Toast.LENGTH_SHORT ).show ();
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t)
            {

            }
        } );
    }

    private void initRecycler()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( getContext (), RecyclerView.HORIZONTAL, false );
        sliderRecycler = mView.findViewById ( R.id.recycler );
        sliderRecycler.setLayoutManager ( layoutManager );

        RecyclerView.LayoutManager sectionsLayoutManager = new LinearLayoutManager ( getContext (), RecyclerView.VERTICAL, false );
        sectionsRecycler = mView.findViewById ( R.id.sections_recycler );
        sectionsRecycler.setLayoutManager ( sectionsLayoutManager );
    }

    class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionVH>
    {
        List<Section> sections;

        public SectionAdapter(List<Section> sections)
        {
            this.sections = sections;
        }

        @NonNull
        @Override
        public SectionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.section_item, parent, false );
            return new SectionVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull SectionVH holder, int position)
        {
            Section section = sections.get ( position );

            holder.sectionName.setText ( section.getSectionName () );

            holder.productRecycler.setAdapter ( new ItemAdapter (section.getHomeProducts ()) );

            holder.openSection.setOnClickListener ( v -> requireActivity ()
                    .getSupportFragmentManager ()
                    .beginTransaction ()
                    .replace ( R.id.nav_host_fragment, new SectionFragment ( section.getSectionId () ) )
                    .addToBackStack ( null )
                    .commit () );
        }

        @Override
        public int getItemCount()
        {
            return sections.size ();
        }

        class SectionVH extends RecyclerView.ViewHolder
        {
            RecyclerView productRecycler;
            TextView sectionName, openSection;

            public SectionVH(@NonNull View itemView)
            {
                super ( itemView );

                productRecycler = itemView.findViewById ( R.id.product_recycler );

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( getContext (), RecyclerView.HORIZONTAL, false );
                productRecycler.setLayoutManager ( layoutManager );

                sectionName = itemView.findViewById ( R.id.section_name );
                openSection = itemView.findViewById ( R.id.open_section );
            }
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemVH>
    {
        List<HomeProduct> homeProducts;

        public ItemAdapter(List<HomeProduct> homeProducts)
        {
            this.homeProducts = homeProducts;
        }

        @NonNull
        @Override
        public ItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.product_item, parent, false );
            return new ItemVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull ItemVH holder, int position)
        {
            HomeProduct homeProduct = homeProducts.get ( position );

            holder.rate.setRating ( homeProduct.getAvgRate () );
            holder.productName.setText ( homeProduct.getName () );
            holder.avgRate.setText ( String.valueOf ( homeProduct.getAvgRate () ) );
            holder.minPrice.append ( " " + homeProduct.getMinPrice () );

            holder.openProduct.setOnClickListener ( v -> requireActivity ()
                    .getSupportFragmentManager ()
                    .beginTransaction ()
                    .replace ( R.id.nav_host_fragment, new ProductFragment (homeProduct.getId ()) )
                    .addToBackStack ( null )
                    .commit () );

            Picasso.get ()
                    .load ( homeProduct.getImage () )
                    .placeholder ( R.mipmap.productimage )
                    .error ( R.mipmap.productimage )
                    .into ( holder.image );
        }

        @Override
        public int getItemCount()
        {
            return homeProducts.size ();
        }

        class ItemVH extends RecyclerView.ViewHolder
        {
            ImageView image;
            TextView productName, avgRate, minPrice;
            RatingBar rate;
            CardView openProduct;

            public ItemVH(@NonNull View itemView)
            {
                super ( itemView );
                image = itemView.findViewById ( R.id.image );
                productName = itemView.findViewById ( R.id.product_name );
                avgRate = itemView.findViewById ( R.id.avg_rate );
                minPrice = itemView.findViewById ( R.id.min_price );
                rate = itemView.findViewById ( R.id.rate );
                openProduct = itemView.findViewById ( R.id.open_product );
            }
        }
    }

    class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderVH>
    {
        List<String> sliders;

        public SliderAdapter(List<String> sliders) {
            this.sliders = sliders;
        }

        @NonNull
        @Override
        public SliderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.ads_item, parent, false );
            return new SliderVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull SliderVH holder, int position)
        {
            Picasso.get ()
                    .load ( sliders.get ( position ) )
                    .into ( holder.slid );
        }

        @Override
        public int getItemCount()
        {
            return sliders.size ();
        }

        class SliderVH extends RecyclerView.ViewHolder
        {
            ImageView slid;

            public SliderVH(@NonNull View itemView)
            {
                super ( itemView );

                slid = itemView.findViewById ( R.id.slid );
            }
        }
    }
}

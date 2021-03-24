package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Cart.Cart;
import com.mustafa.maraeialshamalia.Models.Favourites.Data;
import com.mustafa.maraeialshamalia.Models.Favourites.Favourites;
import com.mustafa.maraeialshamalia.Models.Product.Charity;
import com.mustafa.maraeialshamalia.Models.Product.Cut;
import com.mustafa.maraeialshamalia.Models.Product.Encapsulation;
import com.mustafa.maraeialshamalia.Models.Product.Head;
import com.mustafa.maraeialshamalia.Models.Product.Product;
import com.mustafa.maraeialshamalia.Models.Product.Size;
import com.mustafa.maraeialshamalia.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment
{
    private View mView;
    private int product_id;
    private ImageView productImage, toggleFavourite;
    private TextView productName, avgRate, quantityText, mincedQuantityText;
    private Button normalBtn, sadaqahBtn, addToCartBtn;
    private ImageButton quantityMinusBtn, quantityPlusBtn, mincedQuantityMinusBtn, mincedQuantityPlusBtn;
    private LinearLayout cutLin, headLin, encapsulationLin, charityLin, amountLin;
    private RadioButton alive, slaughtered;
    private RadioGroup radioGroup;
    private RatingBar rate;
    private Spinner sizesSpinner, cutsSpinner, headsSpinner, encapsulationsSpinner, charitiesSpinner;

    private List<Size> sizes;
    private List<Cut> cuts;
    private List<Head> heads;
    private List<Encapsulation> encapsulations;
    private List<Charity> charities;
    private final List<String> sizesItems = new ArrayList<> ();
    private final List<String> cutsItems = new ArrayList<> ();
    private final List<String> headsItems = new ArrayList<> ();
    private final List<String> encapsulationsItems = new ArrayList<> ();
    private final List<String> charitiesItems = new ArrayList<> ();
    private String itemType, orderType = "normal", notes = null;
    private int quantity = 1, sizesId, cutsId, headsId, encapsulationId, charityId, mincedQuantity = 0;
    private String token;

    private List<Data> favourites = new ArrayList<> ();
    private boolean isFavourite = false;

    public ProductFragment(int product_id)
    {
        this.product_id = product_id;
    }

    public ProductFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_product, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences1 = getContext ().getSharedPreferences ( "app_language", Context.MODE_PRIVATE );
        String language = sharedPreferences1.getString ( "language","en" );

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        token = sharedPreferences.getString ( "token", null );

        if(getArguments () != null)
            product_id = getArguments ().getInt ( "product_id" );

        initViews();
        getProduct(language);
        isFav();
    }

    private void isFav()
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
                                favourites = response.body ().getData ();
                                for(int i = 0 ; i < favourites.size () ; i++)
                                {
                                    if(favourites.get ( i ).getId () == product_id)
                                    {
                                        isFavourite = true;
                                        break;
                                    }
                                }
                            }
            }

            @Override
            public void onFailure(Call<Favourites> call, Throwable t)
            {

            }
        } );
    }

    private void initViews()
    {
        toggleFavourite = mView.findViewById ( R.id.favourite );

        cutLin = mView.findViewById ( R.id.cut_lin );
        headLin = mView.findViewById ( R.id.head_lin );
        encapsulationLin = mView.findViewById ( R.id.encapsulation_lin );
        charityLin = mView.findViewById ( R.id.charity_lin );
        amountLin = mView.findViewById ( R.id.amount_lin );

        productImage = mView.findViewById ( R.id.product_image );
        productName = mView.findViewById ( R.id.product_name );

        sadaqahBtn = mView.findViewById ( R.id.sadaqah );
        normalBtn = mView.findViewById ( R.id.normal );

        quantityText = mView.findViewById ( R.id.quantity );
        mincedQuantityText = mView.findViewById ( R.id.minced_quantity );

        quantityMinusBtn = mView.findViewById ( R.id.quantity_minus );
        quantityPlusBtn = mView.findViewById ( R.id.quantity_plus );
        mincedQuantityMinusBtn = mView.findViewById ( R.id.minced_quantity_minus );
        mincedQuantityPlusBtn = mView.findViewById ( R.id.minced_quantity_plus );

        addToCartBtn = mView.findViewById ( R.id.add_to_cart );

        avgRate = mView.findViewById ( R.id.avg_rate );
        rate = mView.findViewById ( R.id.rate );

        alive = mView.findViewById ( R.id.alive );
        slaughtered = mView.findViewById ( R.id.slaughtered );

        radioGroup = mView.findViewById ( R.id.radio_group );
        radioGroup.setOnCheckedChangeListener ( (group, checkedId) -> setItemType (checkedId) );

        setOrderType ();
        setItemType ( R.id.slaughtered );
        onClicks();
    }

    private void onClicks()
    {
        sadaqahBtn.setOnClickListener ( v -> {
            orderType = "sadaqah";
            setOrderType();
        } );

        normalBtn.setOnClickListener ( v -> {
            orderType = "normal";
            setOrderType ();
        } );

        addToCartBtn.setOnClickListener ( v -> addItemToCart() );

        quantityMinusBtn.setOnClickListener ( v -> {
            if(quantity == 1)
            {
                Toast.makeText ( getContext (), "الكمية لا يمكن ان تكون اقل من 1", Toast.LENGTH_SHORT ).show ();
            }else
                {
                    quantity--;
                    quantityText.setText ( String.valueOf ( quantity ) );
                }
        } );

        quantityPlusBtn.setOnClickListener ( v -> {
            quantity++;
            quantityText.setText ( String.valueOf ( quantity ) );
        } );

        mincedQuantityMinusBtn.setOnClickListener ( v -> {
            if(mincedQuantity == 0)
            {
                Toast.makeText ( getContext (), "الكمية لا يمكن ان تكون اقل من 0", Toast.LENGTH_SHORT ).show ();
            }else
            {
                mincedQuantity--;
                mincedQuantityText.setText ( String.valueOf ( mincedQuantity ) );
            }
        } );

        mincedQuantityPlusBtn.setOnClickListener ( v -> {
            mincedQuantity++;
            mincedQuantityText.setText ( String.valueOf ( mincedQuantity ) );
        } );

        toggleFavourite.setOnClickListener ( v -> RetrofitClient.getInstance ().toggleFavouriteState ( token,product_id ).enqueue ( new Callback<Favourites> ()
        {
            @Override
            public void onResponse(Call<Favourites> call, Response<Favourites> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                        {
                            if(isFavourite)
                            {
                                Toast.makeText ( getContext (), "تم الازالة من المفضلة", Toast.LENGTH_SHORT ).show ();
                                isFavourite = false;
                            }
                            else
                                {
                                    Toast.makeText ( getContext (), "تم الاضافة الى المفضلة", Toast.LENGTH_SHORT ).show ();
                                    isFavourite = true;
                                }
                        }
            }

            @Override
            public void onFailure(Call<Favourites> call, Throwable t)
            {

            }
        } ) );

        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( v -> requireActivity ().onBackPressed () );


    }

    private void addItemToCart()
    {
        if(charityLin.getVisibility () == View.GONE)
        {
            RetrofitClient.getInstance ().addItemToCart ( token,product_id, orderType, itemType, quantity, sizesId, cutsId, encapsulationId, headsId, mincedQuantity, notes ).enqueue ( new Callback<Cart> ()
            {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response)
                {
                    if (response.isSuccessful ())
                        if (response.code () == 200)
                            if (response.body () != null)
                                if(response.body ().getKey ().equals ( "success" ))
                                {
                                    Toast.makeText ( getContext (), response.body ().getData ().getProduct_name (), Toast.LENGTH_SHORT ).show ();
                                    Toast.makeText ( getContext (), String.valueOf ( response.body ().getData ().getTotal_cart () ), Toast.LENGTH_SHORT ).show ();
                                }else
                                    Toast.makeText ( getContext (), response.body ().getKey (), Toast.LENGTH_SHORT ).show ();
                                else
                                    Toast.makeText ( getContext (), "null", Toast.LENGTH_SHORT ).show ();
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t)
                {

                }
            } );
        }else
            {
                RetrofitClient.getInstance ().addItemToCart ( token,product_id, orderType, itemType, quantity, sizesId, cutsId, encapsulationId, headsId, charityId, mincedQuantity, notes ).enqueue ( new Callback<Cart> ()
                {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response)
                    {
                        if (response.isSuccessful ())
                            if (response.code () == 200)
                                if (response.body () != null)
                                    if(response.body ().getKey ().equals ( "success" ))
                                    {
                                        Toast.makeText ( getContext (), response.body ().getData ().getProduct_name (), Toast.LENGTH_SHORT ).show ();
                                        Toast.makeText ( getContext (), response.body ().getData ().getTotal_cart ()+"", Toast.LENGTH_SHORT ).show ();
                                    }else
                                        Toast.makeText ( getContext (), response.body ().getKey (), Toast.LENGTH_SHORT ).show ();
                                    else
                                        Toast.makeText ( getContext (), "null", Toast.LENGTH_SHORT ).show ();
                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t)
                    {

                    }
                } );
            }
    }

    private void setOrderType()
    {
        if(orderType.equals ( "sadaqah" ))
        {
            charityLin.setVisibility ( View.VISIBLE );

            sadaqahBtn.setBackground ( getResources ().getDrawable ( R.drawable.selected_button ) );
            sadaqahBtn.setTextColor ( getResources ().getColor ( R.color.white ) );
            normalBtn.setBackground ( getResources ().getDrawable ( R.drawable.un_selected_button ));
            normalBtn.setTextColor ( getResources ().getColor ( R.color.gray ) );
        }else
            {
                charityLin.setVisibility ( View.GONE );

                normalBtn.setBackground ( getResources ().getDrawable ( R.drawable.selected_button ) );
                normalBtn.setTextColor ( getResources ().getColor ( R.color.white ) );
                sadaqahBtn.setBackground ( getResources ().getDrawable ( R.drawable.un_selected_button ));
                sadaqahBtn.setTextColor ( getResources ().getColor ( R.color.gray ) );
            }
    }

    private void setItemType(int checkedId)
    {
        if(checkedId == R.id.alive)
        {
            itemType = "alive";
            alive.setChecked ( true );
            slaughtered.setChecked ( false );

            cutLin.setVisibility ( View.GONE );
            encapsulationLin.setVisibility ( View.GONE );
            headLin.setVisibility ( View.GONE );
            amountLin.setVisibility ( View.GONE );
        }else
            {
                itemType = "slaughtered";
                alive.setChecked ( false );
                slaughtered.setChecked ( true );

                cutLin.setVisibility ( View.VISIBLE );
                encapsulationLin.setVisibility ( View.VISIBLE );
                headLin.setVisibility ( View.VISIBLE );
                amountLin.setVisibility ( View.VISIBLE );
            }
    }

    private void initSpinners()
    {
        sizesSpinner = mView.findViewById ( R.id.sizes );
        cutsSpinner = mView.findViewById ( R.id.cuts );
        headsSpinner = mView.findViewById ( R.id.heads );
        encapsulationsSpinner = mView.findViewById ( R.id.encapsulations );
        charitiesSpinner = mView.findViewById ( R.id.charities );

        ArrayAdapter<String> sizesAdapter = new ArrayAdapter<> ( getContext (), android.R.layout.simple_spinner_dropdown_item, sizesItems);
        ArrayAdapter<String> cutsAdapter = new ArrayAdapter<> ( getContext (), android.R.layout.simple_spinner_dropdown_item, cutsItems);
        ArrayAdapter<String> headsAdapter = new ArrayAdapter<> ( getContext (), android.R.layout.simple_spinner_dropdown_item, headsItems);
        ArrayAdapter<String> encapsulationsAdapter = new ArrayAdapter<> ( getContext (), android.R.layout.simple_spinner_dropdown_item, encapsulationsItems);
        ArrayAdapter<String> charitiesAdapter = new ArrayAdapter<> ( getContext (), android.R.layout.simple_spinner_dropdown_item, charitiesItems);

        sizesSpinner.setAdapter ( sizesAdapter );
        cutsSpinner.setAdapter ( cutsAdapter );
        headsSpinner.setAdapter ( headsAdapter );
        encapsulationsSpinner.setAdapter ( encapsulationsAdapter );
        charitiesSpinner.setAdapter ( charitiesAdapter );

        onSpinnerSelected();
    }

    private void onSpinnerSelected()
    {
        sizesSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                sizesId = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                sizesId = 1;
            }
        } );

        cutsSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                cutsId = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                cutsId = 1;
            }
        } );

        headsSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                headsId = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                headsId = 1;
            }
        } );

        encapsulationsSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                encapsulationId = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                encapsulationId = 1;
            }
        } );

        charitiesSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                charityId = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                charityId = 1;
            }
        } );
    }

    private void getProduct(String language)
    {
        RetrofitClient.getInstance ().getProduct ( language,product_id ).enqueue ( new Callback<Product> ()
        {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if (response.body ().getKey ().equals ( "success" ))
                            {
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();

                                sizes = response.body ().getData ().getSizes ();
                                cuts = response.body ().getData ().getCuts ();
                                heads= response.body ().getData ().getHeads ();
                                encapsulations = response.body ().getData ().getEncapsulations ();
                                charities = response.body ().getData ().getCharities ();
                                prepare ();
                                Picasso.get ().load ( response.body ().getData ().getImage () ).into ( productImage );
                                productName.setText ( response.body ().getData ().getName () );
                                avgRate.setText ( String.valueOf ( response.body ().getData ().getAvgRate () ) );
                                rate.setRating ( response.body ().getData ().getAvgRate () );
                            }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t)
            {

            }
        } );
    }

    private void prepare()
    {
        for (int i = 0 ; i < sizes.size () ; i++)
        {
            sizesItems.add ( sizes.get ( i ).getName () );
        }

        for (int i = 0 ; i < cuts.size () ; i++)
        {
            cutsItems.add ( cuts.get ( i ).getName () );
        }

        for (int i = 0 ; i < heads.size () ; i++)
        {
            headsItems.add ( heads.get ( i ).getName () );
        }

        for (int i = 0 ; i < encapsulations.size () ; i++)
        {
            encapsulationsItems.add ( encapsulations.get ( i ).getName () );
        }

        for (int i = 0 ; i < charities.size () ; i++)
        {
            charitiesItems.add ( charities.get ( i ).getName () );
        }

        initSpinners (  );
    }
}

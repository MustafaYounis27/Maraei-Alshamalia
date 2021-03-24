package com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens.Orders;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.OrderDetails.OrderDetails;
import com.mustafa.maraeialshamalia.Models.OrderDetails.ProductsData;
import com.mustafa.maraeialshamalia.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsFragment extends Fragment
{
    private View mView;
    private TextView orderNumberText, orderDateText, paymentMethodText, totalText;
    private ImageView progressImage;
    private RecyclerView productsRecycler;
    private String language;

    private final int order_id;

    public OrderDetailsFragment(int order_id) {
        this.order_id = order_id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_order_details, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "app_language", Context.MODE_PRIVATE );
        language = sharedPreferences.getString ( "language","en" );

        initViews();
        initRecycler();
        getOrderDetails();
    }

    private void getOrderDetails()
    {
        RetrofitClient.getInstance ().getOrderDetails ( language,order_id ).enqueue ( new Callback<OrderDetails> ()
        {
            @Override
            public void onResponse(Call<OrderDetails> call, Response<OrderDetails> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if (response.body ().getKey ().equals ( "success" ))
                            {
                                orderNumberText.append ( response.body ().getData ().getOrderData ().getOrderNum () );
                                orderDateText.append ( response.body ().getData ().getOrderData ().getDeliveryDate () );
                                paymentMethodText.append ( response.body ().getData ().getOrderData ().getPayTypeName () );
                                totalText.append ( String.valueOf ( response.body ().getData ().getOrderData ().getOrderTotal () ) );
                                productsRecycler.setAdapter ( new ProductsAdapter (response.body ().getData ().getProductsData ()) );
                            }
            }

            @Override
            public void onFailure(Call<OrderDetails> call, Throwable t)
            {

            }
        } );
    }

    private void initViews()
    {
        orderNumberText = mView.findViewById ( R.id.order_num );
        orderDateText = mView.findViewById ( R.id.order_date );
        paymentMethodText = mView.findViewById ( R.id.pay_method );
        totalText = mView.findViewById ( R.id.total_price );
        progressImage = mView.findViewById ( R.id.progress_wait );
    }

    private void initRecycler()
    {
        productsRecycler = mView.findViewById ( R.id.products_recycler );
        RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager ( getContext (), RecyclerView.VERTICAL, false );
        productsRecycler.setLayoutManager ( layoutManager );
    }

    class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsVH>
    {
        List<ProductsData> products;

        public ProductsAdapter(List<ProductsData> products)
        {
            this.products = products;
        }

        @NonNull
        @Override
        public ProductsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.order_details_item, parent, false );
            return new ProductsVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull ProductsVH holder, int position)
        {
            ProductsData product = products.get ( position );

            holder.productNameText.setText ( product.getProductName () );
            holder.priceText.setText ( String.valueOf ( product.getProductTotal () ) );
            holder.amountText.append ( String.valueOf ( product.getProductQuantity () ) );
            holder.sizeText.append ( product.getSizeName () );
        }

        @Override
        public int getItemCount()
        {
            return products.size ();
        }

        class ProductsVH extends RecyclerView.ViewHolder
        {
            TextView productNameText, priceText, amountText, sizeText;
            ImageView productImage;

            public ProductsVH(@NonNull View itemView)
            {
                super ( itemView );

                productNameText = itemView.findViewById ( R.id.product_name );
                priceText = itemView.findViewById ( R.id.product_price );
                amountText = itemView.findViewById ( R.id.amount );
                sizeText = itemView.findViewById ( R.id.size );
                productImage = itemView.findViewById ( R.id.image );
            }
        }
    }
}

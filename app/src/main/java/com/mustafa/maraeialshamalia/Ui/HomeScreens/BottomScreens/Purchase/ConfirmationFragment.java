package com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens.Purchase;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Cart.Cart;
import com.mustafa.maraeialshamalia.Models.Cart.CartItem;
import com.mustafa.maraeialshamalia.Models.OrderDetails.OrderDetails;
import com.mustafa.maraeialshamalia.Models.OrderDetails.ProductsData;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens.Orders.OrderDetailsFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationFragment extends Fragment
{
    private View mView;
    private String token;
    private RecyclerView recyclerView;
    private TextView totalText, deliveryText;
    private Button next;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_confirmation, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences1 = getContext ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        token = sharedPreferences1.getString ( "token", null );

        if(getArguments () != null)
            bundle = getArguments ();

        initViews();
        initRecycler ();
        getOrderDetails ();
    }

    private void initViews()
    {
        totalText = mView.findViewById ( R.id.total_price );
        deliveryText = mView.findViewById ( R.id.delivery_price );
        next = mView.findViewById ( R.id.next );

        onClicks();
    }

    private void onClicks()
    {
        next.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController ( getActivity (), R.id.nav_host_fragment );
                navController.navigate ( R.id.action_confirmationFragment_to_paymentFragment, bundle );
            }
        } );

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
                navController.navigate ( R.id.action_confirmationFragment_to_notificationFragment );
            }
        } );
    }

    private void getOrderDetails()
    {
        RetrofitClient.getInstance ().getCartItem ( token ).enqueue ( new Callback<Cart> ()
        {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response)
            {
                deliveryText.setText ( String.valueOf ( response.body ().getData ().getDeliveryCharge () ) );
                int total = 0;
                List<CartItem> cartItems = response.body ().getData ().getCartItems ();
                for (int i = 0 ; i < cartItems.size () ; i++)
                {
                    total += cartItems.get ( i ).getTotal ();
                }

                bundle.putInt ( "total", total );
                bundle.putInt ( "cartItems", cartItems.size () );

                totalText.setText ( String.valueOf ( total ) );

                recyclerView.setAdapter ( new ProductsAdapter ( cartItems ) );
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t)
            {

            }
        } );
    }

    private void initRecycler()
    {
        recyclerView = mView.findViewById ( R.id.products_recycler );
        RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager ( getContext (), RecyclerView.VERTICAL, false );
        recyclerView.setLayoutManager ( layoutManager );
    }

    class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsVH>
    {
        List<CartItem> products;

        public ProductsAdapter(List<CartItem> products)
        {
            this.products = products;
        }

        @NonNull
        @Override
        public ProductsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.order_details_item, parent, false );
            return new ProductsAdapter.ProductsVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull ProductsAdapter.ProductsVH holder, int position)
        {
            CartItem product = products.get ( position );

            holder.productNameText.setText ( product.getProductName () );
            holder.priceText.setText ( String.valueOf ( product.getTotal () ) );
            holder.amountText.append ( String.valueOf ( product.getQuantity () ) );
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

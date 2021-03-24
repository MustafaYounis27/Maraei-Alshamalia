package com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mustafa.maraeialshamalia.Models.Cart.EditItem;
import com.mustafa.maraeialshamalia.Models.Favourites.Favourites;
import com.mustafa.maraeialshamalia.Models.Home.Section;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments.NotificationFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment
{
    private View mView;
    private RecyclerView CartRecycler;
    private String token;
    private Button next;
    private List<CartItem> cartItems = new ArrayList<> ();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_cart, null);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = requireActivity ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        token = sharedPreferences.getString ( "token", null );

        initRecycler ();
        openNotification ();

        next = mView.findViewById ( R.id.next );

        getCartItems(token);

        next.setOnClickListener ( v -> {
            NavController navController = Navigation.findNavController ( getActivity (), R.id.nav_host_fragment );
            navController.navigate ( R.id.action_homePageFragment_to_deliverDetailsFragment );
        } );
    }

    private void getCartItems(String token)
    {
        Toast.makeText ( getContext (), "get", Toast.LENGTH_SHORT ).show ();
        RetrofitClient.getInstance ().getCartItem ( token ).enqueue ( new Callback<Cart> ()
        {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response)
            {
                if(response.isSuccessful ())
                    if(response.code () == 200)
                        if(response.body () != null)
                            if(response.body ().getKey ().equals ( "success" ))
                            {
                                Toast.makeText ( getContext (), response.body ().getKey (), Toast.LENGTH_SHORT ).show ();
                                cartItems = response.body ().getData ().getCartItems ();

                                if (cartItems.size () != 0)
                                    next.setVisibility ( View.VISIBLE );
                                else
                                    next.setVisibility ( View.GONE );

                                CartRecycler.setAdapter ( new CartAdapter (cartItems) );
                            }else
                                Toast.makeText ( getContext (), response.body ().getKey (), Toast.LENGTH_SHORT ).show ();
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t)
            {

            }
        } );
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
                navController.navigate ( R.id.action_homePageFragment_to_notificationFragment );
            }
        } );
    }

    private void initRecycler()
    {
        CartRecycler = mView.findViewById ( R.id.recycler );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( getContext (), RecyclerView.VERTICAL, false );
        CartRecycler.setLayoutManager ( layoutManager );
    }

    class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemVH>
    {
        List<CartItem> cartItems;

        public CartAdapter(List<CartItem> cartItems)
        {
            this.cartItems = cartItems;
        }

        @NonNull
        @Override
        public CartAdapter.ItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.cart_item, parent, false );
            return new CartAdapter.ItemVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull CartAdapter.ItemVH holder, int position)
        {
            CartItem cartItem = cartItems.get ( position );

            holder.productName.setText ( cartItem.getProductName () );
            int quantity = cartItem.getQuantity ();
            holder.quantity.setText ( String.valueOf ( quantity ) );
            holder.totalPrice.setText ( "ر.س" + cartItem.getTotal () );

            holder.minus.setOnClickListener ( new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    editCartQuantity ( cartItem.getId (),2 );
                }
            } );

            holder.plus.setOnClickListener ( new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    edit(quantity,cartItem,holder,1);
                }
            } );

            holder.favourite.setOnClickListener ( new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    RetrofitClient.getInstance ().toggleFavouriteState ( token,cartItem.getId () ).enqueue ( new Callback<Favourites> ()
                    {
                        @Override
                        public void onResponse(Call<Favourites> call, Response<Favourites> response)
                        {
                            if (response.isSuccessful ())
                                if (response.code () == 200)
                                    if (response.body () != null)
                                    {
                                        Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                        holder.favourite.setText ( "   ازالة من المفضلة" );
                                    }
                        }

                        @Override
                        public void onFailure(Call<Favourites> call, Throwable t)
                        {

                        }
                    } );
                }
            } );

            holder.delete.setOnClickListener ( new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    RetrofitClient.getInstance ().removeFromCart ( token,cartItem.getId () ).enqueue ( new Callback<Cart> ()
                    {
                        @Override
                        public void onResponse(Call<Cart> call, Response<Cart> response)
                        {
                            if (response.isSuccessful ())
                                if (response.code () == 200)
                                    if (response.body () != null)
                                    {
                                        Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                        getCartItems ( token );
                                    }
                        }

                        @Override
                        public void onFailure(Call<Cart> call, Throwable t)
                        {

                        }
                    } );
                }
            } );
        }

        @Override
        public int getItemCount()
        {
            return cartItems.size ();
        }

        class ItemVH extends RecyclerView.ViewHolder
        {
            TextView totalPrice, quantity, productName, favourite, delete;
            ImageButton plus, minus;

            public ItemVH(@NonNull View itemView)
            {
                super ( itemView );

                totalPrice = itemView.findViewById ( R.id.total_price );
                quantity = itemView.findViewById ( R.id.quantity );
                productName = itemView.findViewById ( R.id.product_name );
                plus = itemView.findViewById ( R.id.plus );
                minus = itemView.findViewById ( R.id.minus );
                favourite = itemView.findViewById ( R.id.favourite );
                delete = itemView.findViewById ( R.id.delete );
            }
        }
    }

    private void edit(int quantity, CartItem cartItem, CartAdapter.ItemVH holder, int operation)
    {
        if(operation == 0)
        {
            if (quantity == 1) {
                Toast.makeText ( getContext (), "الكمية لا يمكن ان تكون اقل من 1", Toast.LENGTH_SHORT ).show ();
            } else {
                quantity--;
                holder.quantity.setText ( String.valueOf ( quantity ) );
                cartItem.setQuantity ( quantity );
                editCartQuantity ( cartItem.getId (), quantity );
            }
        }else
            {
                quantity++;
                holder.quantity.setText ( String.valueOf ( quantity ) );
                cartItem.setQuantity ( quantity );
                //editCartQuantity(cartItem.getId (),quantity);
            }
    }

    private void editCartQuantity(int id,int quantity)
    {

    }
}

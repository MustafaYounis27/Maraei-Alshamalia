package com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens.Orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Orders.Orders;
import com.mustafa.maraeialshamalia.Models.Orders.OrdersItem;
import com.mustafa.maraeialshamalia.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment
{
    private View mView;
    private RecyclerView recyclerView;
    List<OrdersItem> currentOrders = new ArrayList<> ();
    List<OrdersItem> finishedOrders = new ArrayList<> ();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_orders, null);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = requireActivity ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        String token = sharedPreferences.getString ( "token", null );

        initRecycler ();
        getOrders(token);
        setTabs();
        openNotification ();
    }

    private void getOrders(String token)
    {
        RetrofitClient.getInstance ().getOrders ( token,"current" ).enqueue ( new Callback<Orders> ()
        {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response)
            {
                if(response.isSuccessful ())
                    if(response.code () == 200)
                        if(response.body () != null)
                            if(response.body ().getKey ().equals ( "success" ))
                            {
                                currentOrders = response.body ().getData ().getData ();
                                recyclerView.setAdapter ( new OrdersAdapter ( currentOrders ) );
                                Toast.makeText ( getContext (), "current", Toast.LENGTH_SHORT ).show ();
                            }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t)
            {

            }
        } );

        RetrofitClient.getInstance ().getOrders ( token,"finished" ).enqueue ( new Callback<Orders> ()
        {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response)
            {
                if(response.isSuccessful ())
                    if(response.code () == 200)
                        if(response.body () != null)
                            if(response.body ().getKey ().equals ( "success" ))
                            {
                                finishedOrders = response.body ().getData ().getData ();
                                Toast.makeText ( getContext (), "finished", Toast.LENGTH_SHORT ).show ();
                            }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t)
            {

            }
        } );
    }

    private void setTabs()
    {
        TabLayout tableLayout = mView.findViewById ( R.id.tab_layout );

        tableLayout.addTab ( tableLayout.newTab ().setText ( getResources ().getString ( R.string.current ) ) );
        tableLayout.addTab ( tableLayout.newTab ().setText ( getResources ().getString ( R.string.finished ) ) );

        tableLayout.addOnTabSelectedListener ( new TabLayout.OnTabSelectedListener ()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if(tab.getPosition () == 0)
                    recyclerView.setAdapter ( new OrdersAdapter ( currentOrders ) );
                else
                    recyclerView.setAdapter ( new OrdersAdapter ( finishedOrders ) );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        } );
    }

    private void openNotification()
    {
        ImageView notification = mView.findViewById ( R.id.notification );
        notification.setOnClickListener ( v -> {
            NavController navController = Navigation.findNavController ( getActivity (), R.id.nav_host_fragment );
            navController.navigate ( R.id.action_homePageFragment_to_notificationFragment );
        } );
    }

    private void initRecycler()
    {
        recyclerView = mView.findViewById ( R.id.recycler );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( getContext (), RecyclerView.VERTICAL, false );
        recyclerView.setLayoutManager ( layoutManager );
    }

    class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ItemVH>
    {
        List<OrdersItem> orders;

        public OrdersAdapter(List<OrdersItem> orders)
        {
            this.orders = orders;
        }

        @NonNull
        @Override
        public OrdersAdapter.ItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.orders_item, parent, false );
            return new OrdersAdapter.ItemVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull OrdersAdapter.ItemVH holder, int position)
        {
            OrdersItem order = orders.get ( position );

            holder.orderNum.setText ( String.valueOf ( order.getOrderNum () ) );
            holder.totalPrice.append ( " " + order.getOrderTotal () );
            holder.countProduct.append ( " " + order.getCountProducts () );

            holder.openOrder.setOnClickListener ( v ->
                    requireActivity ()
                            .getSupportFragmentManager ()
                            .beginTransaction ()
                            .replace ( R.id.nav_host_fragment, new OrderDetailsFragment ( order.getId () ) )
                            .addToBackStack ( null )
                            .commit () );
        }

        @Override
        public int getItemCount()
        {
            return orders.size ();
        }

        class ItemVH extends RecyclerView.ViewHolder
        {
            TextView orderNum, totalPrice, countProduct;
            LinearLayout openOrder;

            public ItemVH(@NonNull View itemView)
            {
                super ( itemView );

                orderNum = itemView.findViewById ( R.id.order_num );
                totalPrice = itemView.findViewById ( R.id.total_price );
                countProduct = itemView.findViewById ( R.id.count_product );
                openOrder = itemView.findViewById ( R.id.open_order );
            }
        }
    }
}

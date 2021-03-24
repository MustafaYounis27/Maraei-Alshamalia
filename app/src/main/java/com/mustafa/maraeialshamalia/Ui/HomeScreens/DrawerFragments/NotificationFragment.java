package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Notifications.Notifications;
import com.mustafa.maraeialshamalia.Models.Notifications.NotifyData;
import com.mustafa.maraeialshamalia.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment
{
    private View mView;
    private RecyclerView notificationRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_notification, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "myToken", Context.MODE_PRIVATE );
        String token = sharedPreferences.getString ( "token", null );

        SharedPreferences sharedPreferences1 = getContext ().getSharedPreferences ( "app_language", Context.MODE_PRIVATE );
        String language = sharedPreferences1.getString ( "language","en" );

        initRecycler();
        initViews();
        getNotification(language,token);
    }

    private void getNotification(String language, String token)
    {
        RetrofitClient.getInstance ().getNotifications ( language, token ).enqueue ( new Callback<Notifications> ()
        {
            @Override
            public void onResponse(Call<Notifications> call, Response<Notifications> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if (response.body ().getKey ().equals ( "success" ))
                            {
                                Toast.makeText ( getContext (), response.body ().getMsg (), Toast.LENGTH_SHORT ).show ();
                                notificationRecycler.setAdapter ( new NotificationAdapter (response.body ().getData ().getData ()) );
                            }
            }

            @Override
            public void onFailure(Call<Notifications> call, Throwable t)
            {

            }
        } );
    }

    private void initViews()
    {
        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( v -> requireActivity ().onBackPressed () );
    }

    private void initRecycler()
    {
        notificationRecycler = mView.findViewById ( R.id.notification_recycler );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( getContext (), RecyclerView.VERTICAL, false );
        notificationRecycler.setLayoutManager ( layoutManager );
    }

    class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationVH>
    {
        List<NotifyData> notifications;

        public NotificationAdapter(List<NotifyData> notifications)
        {
            this.notifications = notifications;
        }

        @NonNull
        @Override
        public NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.notification_item, parent, false );
            return new NotificationVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationVH holder, int position)
        {
            NotifyData notification = notifications.get ( position );

            holder.notification.setText ( notification.getMessage () );
        }

        @Override
        public int getItemCount()
        {
            return notifications.size ();
        }


        class NotificationVH extends RecyclerView.ViewHolder
        {
            TextView notification;

            public NotificationVH(@NonNull View itemView)
            {
                super ( itemView );

                notification = itemView.findViewById ( R.id.notification_field );
            }
        }
    }
}
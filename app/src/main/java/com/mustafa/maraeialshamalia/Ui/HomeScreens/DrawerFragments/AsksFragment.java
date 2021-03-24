package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mustafa.maraeialshamalia.Data.RetrofitClient;
import com.mustafa.maraeialshamalia.Models.Asks.Asks;
import com.mustafa.maraeialshamalia.Models.Asks.Data;
import com.mustafa.maraeialshamalia.R;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsksFragment extends Fragment
{
    private View mView;
    private RecyclerView asksRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_asks,null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ( "app_language", Context.MODE_PRIVATE );
        String language = sharedPreferences.getString ( "language","en" );

        initRecycler();
        getAsks(language);
        openNotification ();
        ImageView back = mView.findViewById ( R.id.back );
        back.setOnClickListener ( v -> requireActivity ().onBackPressed () );
    }

    private void getAsks(String language)
    {
        RetrofitClient.getInstance ().getAsks (language).enqueue ( new Callback<Asks> ()
        {
            @Override
            public void onResponse(Call<Asks> call, Response<Asks> response)
            {
                if (response.isSuccessful ())
                    if (response.code () == 200)
                        if (response.body () != null)
                            if (response.body ().getKey ().equals ( "success" ))
                            {
                                asksRecycler.setAdapter ( new AsksAdapter ( response.body ().getData () ) );
                            }
            }

            @Override
            public void onFailure(Call<Asks> call, Throwable t)
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
                navController.navigate ( R.id.action_asksFragment_to_notificationFragment );
            }
        } );
    }

    private void initRecycler()
    {
        asksRecycler = mView.findViewById ( R.id.asks_recycler );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager ( getContext (), RecyclerView.VERTICAL, false );
        asksRecycler.setLayoutManager ( layoutManager );
    }

    class AsksAdapter extends RecyclerView.Adapter<AsksAdapter.AsksVH>
    {
        List<Data> asks;

        public AsksAdapter(List<Data> asks)
        {
            this.asks = asks;
        }

        @NonNull
        @Override
        public AsksVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from ( getContext () ).inflate ( R.layout.asks_item, parent, false );
            return new AsksVH ( view );
        }

        @Override
        public void onBindViewHolder(@NonNull AsksVH holder, int position)
        {
            Data ask = asks.get ( position );

            holder.askField.setText ( ask.getAsk () );
            holder.answerField.setText ( ask.getAnswer () );

            holder.dropAnswer.setOnClickListener ( v -> {
                if(holder.answerField.getVisibility () == View.GONE)
                {
                    holder.answerField.setVisibility ( View.VISIBLE );
                    holder.dropAnswer.setImageResource ( R.mipmap.dropup );
                    holder.askField.setTextColor ( getResources ().getColor ( R.color.red ) );
                }
                else
                    {
                        holder.answerField.setVisibility ( View.GONE );
                        holder.dropAnswer.setImageResource ( R.mipmap.drop );
                        holder.askField.setTextColor ( getResources ().getColor ( R.color.gray ) );
                    }
            } );
        }

        @Override
        public int getItemCount()
        {
            return asks.size ();
        }

        class AsksVH extends RecyclerView.ViewHolder
        {
            TextView askField, answerField;
            ImageView dropAnswer;

            public AsksVH(@NonNull View itemView)
            {
                super ( itemView );

                askField = itemView.findViewById ( R.id.ask_field );
                answerField = itemView.findViewById ( R.id.answer_field );
                dropAnswer = itemView.findViewById ( R.id.drop );
            }
        }
    }
}

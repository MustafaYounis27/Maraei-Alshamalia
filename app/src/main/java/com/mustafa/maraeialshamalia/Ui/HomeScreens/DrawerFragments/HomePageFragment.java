package com.mustafa.maraeialshamalia.Ui.HomeScreens.DrawerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens.CartFragment;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens.HomeFragment;
import com.mustafa.maraeialshamalia.Ui.HomeScreens.BottomScreens.Orders.OrdersFragment;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment
{
    private View mView;

    private HomePagesAdapter homePagesAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    List<String> names = new ArrayList<> ();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_home_pages, null);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );


        initViewPager ();

        ImageView imageView = mView.findViewById ( R.id.image );
        imageView.setOnClickListener ( v -> tabLayout.selectTab ( tabLayout.getTabAt ( 1 ) ) );
    }

    private void initViewPager()
    {
        viewPager = mView.findViewById(R.id.pager);

        names.add ( getResources ().getString ( R.string.home ) );
        names.add ( getResources ().getString ( R.string.cart ) );
        names.add ( getResources ().getString ( R.string.orders ) );

        homePagesAdapter = new HomePagesAdapter ( this, new HomeFragment (), new CartFragment (), new OrdersFragment ());

        viewPager.setAdapter( homePagesAdapter );
        viewPager.setUserInputEnabled ( false );

        tabLayout = mView.findViewById ( R.id.tab_layout );

        new TabLayoutMediator ( tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy ()
        {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position)
            {
                tab.setText ( names.get ( position ) );
                if(position == 0)
                    tab.setIcon ( R.mipmap.homeon );
                else if(position == 2)
                    tab.setIcon ( R.mipmap.orderoff );
                else
                    tab.setIcon ( R.drawable.ic_launcher_foreground );
            }
        }
        ).attach();

        tabLayout.addOnTabSelectedListener ( new TabLayout.OnTabSelectedListener () {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if(tab.getPosition () == 0)
                {
                    tab.setIcon ( R.mipmap.homeon );
                    tabLayout.getTabAt ( 2 ).setIcon ( R.mipmap.orderoff );
                }
                else if(tab.getPosition () == 2)
                    {
                        tab.setIcon ( R.mipmap.orderson );
                        tabLayout.getTabAt ( 0 ).setIcon ( R.mipmap.homeoff );
                    }
                else
                    {
                        tabLayout.getTabAt ( 0 ).setIcon ( R.mipmap.homeoff );
                        tabLayout.getTabAt ( 2 ).setIcon ( R.mipmap.orderoff );
                    }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );
    }

    public static class HomePagesAdapter extends FragmentStateAdapter
    {
        Fragment fragment1, fragment2, fragment3;
        public HomePagesAdapter(Fragment fragment, Fragment fragment1, Fragment fragment2, Fragment fragment3)
        {
            super(fragment);
            this.fragment1 = fragment1;
            this.fragment2 = fragment2;
            this.fragment3 = fragment3;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position)
        {
            if(position == 0)
                return fragment1;
            else if(position == 1)
                return fragment2;
            else
                return fragment3;
        }

        @Override
        public int getItemCount()
        {
            return 3;
        }
    }
}

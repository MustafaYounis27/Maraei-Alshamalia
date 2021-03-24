package com.mustafa.maraeialshamalia.Ui.introScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.mustafa.maraeialshamalia.R;
import com.mustafa.maraeialshamalia.Ui.Auth.RegisterActivity;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class IntroFragment extends Fragment
{
    View mView;
    DotsIndicator dotsIndicator;

    IntroAdapter introAdapter;
    ViewPager2 viewPager;
    TextView next, previous;
    Button loginBtn, registerBtn;

    List<Fragment> fragments = new ArrayList<> ();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate ( R.layout.fragment_intro, null );
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        fragments.add ( new IntroPagesFragment (R.mipmap.on) );
        fragments.add ( new IntroPagesFragment (R.mipmap.onii) );
        fragments.add ( new IntroPagesFragment (R.mipmap.oni) );

        initViews();

        initViewPager();

        onClicks();
    }

    private void initViewPager()
    {
        introAdapter = new IntroAdapter ( this, fragments );

        viewPager.setAdapter( introAdapter );
        viewPager.setUserInputEnabled ( false );

        dotsIndicator.setViewPager2 ( viewPager );
    }

    private void initViews()
    {
        viewPager = mView.findViewById(R.id.pager);

        next = mView.findViewById ( R.id.next );
        previous = mView.findViewById ( R.id.previous );

        loginBtn = mView.findViewById ( R.id.login_btn );
        registerBtn = mView.findViewById ( R.id.register_btn );

        dotsIndicator = mView.findViewById ( R.id.dots_indicator );
    }

    private void onClicks()
    {
        next.setOnClickListener ( v -> {
            if(viewPager.getCurrentItem () == fragments.size ()-2)
            {
                viewPager.setCurrentItem ( viewPager.getCurrentItem () + 1 );
                isLastIntro ();
            }
            else
                viewPager.setCurrentItem ( viewPager.getCurrentItem ()+1 );
        } );

        previous.setOnClickListener ( v -> {
            viewPager.setCurrentItem ( fragments.size ()-1 );
            isLastIntro ();
        } );

        loginBtn.setOnClickListener ( v -> startActivity ( new Intent (getActivity (), RegisterActivity.class ) ) );

        registerBtn.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {

            }
        } );
    }

    public void isLastIntro()
    {
        next.setVisibility ( View.GONE );
        previous.setVisibility ( View.GONE );

        loginBtn.setVisibility ( View.VISIBLE );
        registerBtn.setVisibility ( View.VISIBLE );
    }

    public static class IntroAdapter extends FragmentStateAdapter
    {
        List<Fragment> fragments;
        public IntroAdapter(Fragment fragment, List<Fragment> fragments)
        {
            super(fragment);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position)
        {
            return fragments.get ( position );
        }

        @Override
        public int getItemCount()
        {
            return fragments.size ();
        }
    }
}

package com.dhilasadrah.catalogmovie.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dhilasadrah.catalogmovie.R;
import com.dhilasadrah.catalogmovie.fragment.Favorite;
import com.dhilasadrah.catalogmovie.fragment.NowPlaying;
import com.dhilasadrah.catalogmovie.fragment.Upcoming;

public class TabAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public TabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int i) {
        String[] title = new String[]{
                context.getString(R.string.now_playing),
                context.getString(R.string.upcoming),
                context.getString(R.string.favorite)
        };
        return title[i];
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        switch (i) {
            case 0:
                fragment = new NowPlaying();
                break;
            case 1:
                fragment = new Upcoming();
                break;
            case 2:
                fragment = new Favorite();
                break;
            default:
                fragment = null;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
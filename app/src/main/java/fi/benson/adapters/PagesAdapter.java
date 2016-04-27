package fi.benson.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fi.benson.views.fragments.Browse;
import fi.benson.views.fragments.Profile;
import fi.benson.views.fragments.Search;


/**
 * Created by bkamau on 3/7/16.
 * on 10:58
 */
public class PagesAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagesAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Browse browseTab = new Browse();
                return browseTab;
            case 1:
                Search searchTab = new Search();
                return searchTab;
            case 2:
                Profile profileTab = new Profile();
                return profileTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
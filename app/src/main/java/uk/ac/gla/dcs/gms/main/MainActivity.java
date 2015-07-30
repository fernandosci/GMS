package uk.ac.gla.dcs.gms.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import uk.ac.gla.dcs.gms.lms.CustomMapFragment;
import uk.ac.gla.dcs.gms.lms.R;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, GMSMainFragmentCallbacks {

    public static Activity instance = null;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.main_fragment_navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.main_fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        //might remove these lines
        fragmentManager = getSupportFragmentManager();
        instance = this;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        CustomMapFragment customMapFragment;
        Bundle bundle;

        String[] titles = getResources().getStringArray(R.array.main_titles);

        mTitle = titles[position];

//        switch (position) {
//            case 0:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, PlaceholderFragment.newInstance(position))
//                        .commit();
//                break;
//            case 1:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, PlaceholderFragment.newInstance(position))
//                        .commit();
//                break;
//            case 2:
//                bundle = new Bundle();
//                bundle.putDouble(CustomMapFragment.LAT, 43.1);
//                bundle.putDouble(CustomMapFragment.LNG, -87.9);
//                customMapFragment = new CustomMapFragment();
//                customMapFragment.setArguments(bundle);
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, customMapFragment)
//                        .commit();
//                restoreActionBar();
//                break;
//            case 3:
//                bundle = new Bundle();
//                bundle.putDouble(CustomMapFragment.LAT, 51.470087);
//                bundle.putDouble(CustomMapFragment.LNG, -0.452046);
//                customMapFragment = new CustomMapFragment();
//                customMapFragment.setArguments(bundle);
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, customMapFragment)
//                        .commit();
//                restoreActionBar();
//                break;
//            case 4:
//                bundle = new Bundle();
//                bundle.putDouble(CustomMapFragment.LAT, 55.873399);
//                bundle.putDouble(CustomMapFragment.LNG, -4.289192);
//                customMapFragment = new CustomMapFragment();
//                customMapFragment.setArguments(bundle);
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, customMapFragment)
//                        .commit();
//                restoreActionBar();
//                break;
//            case 5:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, PlaceholderFragment.newInstance(position))
//                        .commit();
//                break;
//        }
    }

    @Override
    public void onSectionAttached(String section) {
        String[] titles = getResources().getStringArray(R.array.main_titles);
       // mTitle = titles[section];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
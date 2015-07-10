package uk.ac.gla.dcs.gms.lms;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

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
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

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

        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position))
                        .commit();
                break;
            case 2:
                bundle = new Bundle();
                bundle.putDouble(CustomMapFragment.LAT, 43.1);
                bundle.putDouble(CustomMapFragment.LNG, -87.9);
                customMapFragment = new CustomMapFragment();
                customMapFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, customMapFragment)
                        .commit();
                restoreActionBar();
                break;
            case 3:
                bundle = new Bundle();
                bundle.putDouble(CustomMapFragment.LAT, 51.470087);
                bundle.putDouble(CustomMapFragment.LNG, -0.452046);
                customMapFragment = new CustomMapFragment();
                customMapFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, customMapFragment)
                        .commit();
                restoreActionBar();
                break;
            case 4:
                bundle = new Bundle();
                bundle.putDouble(CustomMapFragment.LAT, 55.873399);
                bundle.putDouble(CustomMapFragment.LNG, -4.289192);
                customMapFragment = new CustomMapFragment();
                customMapFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, customMapFragment)
                        .commit();
                restoreActionBar();
                break;
            case 5:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        String[] titles = getResources().getStringArray(R.array.main_titles);
        mTitle = titles[number];
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



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements AbsListView.OnScrollListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle bundle = getArguments();
            int sectionNumber = bundle.getInt(ARG_SECTION_NUMBER);

            View rootView;
            Log.v("new", Integer.toString(sectionNumber));

            if (sectionNumber == 0 || sectionNumber == 1) {
                rootView = inflater.inflate(R.layout.daily_summary, container, false);
                mHandler = new Handler();

                //inflate the progress bar from the footer, it is wrapped in a FrameLayout since
                //ListViews don't shrink in height when a child is set to visibility gone, but
                //a FrameLayout with height of wrap_content will
                View footer = inflater.inflate(R.layout.progress_bar_footer, null);
                progressBar = (ProgressBar) footer.findViewById(R.id.progressBar);

                listView = (ListView) rootView.findViewById(R.id.listView);
                listView.addFooterView(footer);

                //Adding images to the list view
                Integer coin = R.drawable.bonus_coin;
                Integer tree = R.drawable.bonus_tree;

                Pair<Integer, Integer> p1 = new Pair<>(coin, coin);
                Pair<Integer, Integer> p2 = new Pair<>(coin, tree);
                Pair<Integer, Integer> p3 = new Pair<>(tree, coin);
                Pair<Integer, Integer> p4 = new Pair<>(tree, tree);

                ArrayList<Pair> vet = new ArrayList<>();

                for (int i=0; i<15; i++) {
                    vet.add(new Pair<>(p1.first, p1.second));
                    vet.add(new Pair<>(p2.first, p2.second));
                    vet.add(new Pair<>(p3.first, p3.second));
                    vet.add(new Pair<>(p4.first, p4.second));
                }

                adapter = new ImageScroller(instance, R.layout.row_layout, vet, 10, 5);
                listView.setAdapter(adapter);
                listView.setOnScrollListener(this); //listen for a scroll movement to the bottom
                progressBar.setVisibility((20 < vet.size()) ? View.VISIBLE : View.GONE);

            }
            else
                rootView = inflater.inflate(R.layout.feedback, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }

        //Test for list view

        private ListView listView;
        private ProgressBar progressBar;
        private ImageScroller adapter;
        private Handler mHandler;

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch(item.getItemId()){
                //case R.id.action_reset:
                    //adapter.reset(); //reset the adapter to its initial configuration
                    //listView.setSelection(0); //go to the top
                    //return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(firstVisibleItem + visibleItemCount == totalItemCount && !adapter.endReached() && !hasCallback){ //check if we've reached the bottom
                mHandler.postDelayed(showMore, 300);
                hasCallback = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        private boolean hasCallback;
        private Runnable showMore = new Runnable(){
            public void run(){
                boolean noMoreToShow = adapter.showMore(); //show more views and find out if
                progressBar.setVisibility(noMoreToShow? View.GONE : View.VISIBLE);
                hasCallback = false;
            }
        };
    }


}

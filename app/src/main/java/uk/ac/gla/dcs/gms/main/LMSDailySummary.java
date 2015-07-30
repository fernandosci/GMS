package uk.ac.gla.dcs.gms.main;

import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import uk.ac.gla.dcs.gms.lms.ImageScrollerAdapter;
import uk.ac.gla.dcs.gms.lms.R;

/**
 * Created by ito.
 */
public class LMSDailySummary extends GMSMainFragment implements AbsListView.OnScrollListener{

    private ListView listView;
    private ProgressBar progressBar;
    private ImageScrollerAdapter adapter;
    private Handler mHandler;

    public LMSDailySummary() {
    }

    public static LMSDailySummary newInstance(String section){
        LMSDailySummary dailySummary = new LMSDailySummary();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, section);
        dailySummary.setArguments(args);
        return dailySummary;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView;

        rootView = inflater.inflate(R.layout.lms_daily_summary, container, false);

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

        adapter = new ImageScrollerAdapter(inflater.getContext(), R.layout.row_layout, vet, 10, 5);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this); //listen for a scroll movement to the bottom
        progressBar.setVisibility((20 < vet.size()) ? View.VISIBLE : View.GONE);





        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem + visibleItemCount == totalItemCount && !adapter.endReached() && !hasCallback){ //check if we've reached the bottom
            mHandler.postDelayed(showMore, 300);
            hasCallback = true;
        }

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

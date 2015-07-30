package uk.ac.gla.dcs.gms.main;

import android.content.Context;
import android.content.Intent;
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
import java.util.HashMap;

import uk.ac.gla.dcs.gms.api.http.HTTPProgressStatus;
import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;
import uk.ac.gla.dcs.gms.api.lms.LMSImageRequestParamBuilder;
import uk.ac.gla.dcs.gms.api.lms.LMSSession;
import uk.ac.gla.dcs.gms.lms.ImageScrollerAdapter;
import uk.ac.gla.dcs.gms.lms.R;
import uk.ac.gla.dcs.gms.lms.SingleViewActivity;
import uk.ac.gla.dcs.gms.utils.ErrorsUtils;

/**
 * Created by ito.
 */
public class LMSDailySummary extends GMSMainFragment implements AbsListView.OnScrollListener {

    private ListView listView;
    private ProgressBar progressBar;
    private ImageScrollerAdapter adapter;
    private Handler mHandler;
    private ArrayList<Pair<String, String>> imgList;
    private LocalListener localListener;
    private boolean hasCallback;
    private LMSSession session;
    private Context context;
    private int skip;

    public LMSDailySummary() {
    }

    public static LMSDailySummary newInstance(String section, LMSSession session) {
        LMSDailySummary dailySummary = new LMSDailySummary();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, section);
        dailySummary.setArguments(args);
        dailySummary.session = session;
        return dailySummary;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        localListener = new LocalListener();
        imgList = new ArrayList<>();
        hasCallback = false;
        session = null;
        skip = 0;
        context = inflater.getContext();

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

        adapter = new ImageScrollerAdapter(inflater.getContext(), R.layout.row_layout, imgList, localListener);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this); //listen for a scroll movement to the bottom

        LMSImageRequestParamBuilder builder = new LMSImageRequestParamBuilder();
        builder.setLimit(10).setPersonal(true);
        session.getImages(localListener, 0, builder.toString());

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
        // Sample calculation to determine if the last
        // item is fully visible.
        final int lastItem = firstVisibleItem + visibleItemCount;
        if (lastItem == totalItemCount && !hasCallback) {
            mHandler.postDelayed(localListener, 300);
            hasCallback = true;
        }

    }

    private class LocalListener implements View.OnClickListener, HTTPResponseListener, Runnable {
        @Override
        public void onClick(View v) {

            Bundle bundle = new Bundle();
            bundle.putString(ImageScrollerAdapter.ARG_IMG_URL, (String) v.getTag());
            Intent intent = new Intent(context, SingleViewActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

        @Override
        public void onResponse(int requestCode, boolean successful, HashMap<String, Object> data, Exception exception) {

            if (successful) {

                ArrayList<String> img_urls = (ArrayList<String>) data.get("img_urls");
                Pair<String, String> pair;

                for (int c = 0; c < img_urls.size(); c+=2) {

                    if (c < img_urls.size() - 1)
                        pair = new Pair<>(img_urls.get(c), img_urls.get(c + 1));
                    else
                        pair = new Pair<>(img_urls.get(c), "");

                    imgList.add(pair);
                }

                progressBar.setVisibility((20 < imgList.size()) ? View.VISIBLE : View.GONE);
                adapter.notifyDataSetChanged();
            } else {
                ErrorsUtils.processHttpResponseError(context, data, exception);
            }
        }

        @Override
        public void onProgress(int requestCode, HTTPProgressStatus progressStatus, HashMap<String, Object> newdata) {

        }

        @Override
        public void run() {
            skip++;
            LMSImageRequestParamBuilder builder = new LMSImageRequestParamBuilder();
            builder.setLimit(10).setPersonal(true).setSkip(skip);
            session.getImages(localListener, 1, builder.toString());
            hasCallback = false;
        }
    }
}

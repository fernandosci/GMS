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
import java.util.HashSet;

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
    private HashSet<String> imgList;
    private ArrayList<Pair<String, String>> imgViewList;
    private LocalListener localListener;
    private boolean hasCallback;
    private LMSSession session;
    private Context context;

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
        imgList = new HashSet();
        imgViewList = new ArrayList<>();
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

        adapter = new ImageScrollerAdapter(inflater.getContext(), R.layout.row_layout, imgViewList, localListener);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this); //listen for a scroll movement to the bottom

        hasCallback = true;
        LMSImageRequestParamBuilder builder = new LMSImageRequestParamBuilder();
        builder.setLimit(10).setPersonal(true);
        session.getImages(localListener, 0, builder.toString());

        return rootView;
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

                ArrayList<String> img_urls = ( ArrayList<String>) data.get("img_urls");
                String p1 = null, p2 = null;

                boolean parity = true;
                for (int c = 0; c < img_urls.size(); c++) {

                    String s = img_urls.get(c);
                    if (!imgList.contains(s)) {
                        imgList.add(s);

                        if (parity) {
                            p1 = s;
                            p2 = null;
                            parity = !parity;
                        } else {
                            p2 = s;
                            parity = !parity;
                        }
                    }

                    if ((p1 != null && p2 != null) || (p1 != null && c == img_urls.size()-1)) {
                        if (p2 == null)
                            p2 = "";
                        imgViewList.add(new Pair<String, String>(p1,p2));
                        p1 = null;
                        p2 = null;
                    }
                }

                progressBar.setVisibility((20 < imgViewList.size()) ? View.VISIBLE : View.GONE);
                adapter.notifyDataSetChanged();

            } else {
                ErrorsUtils.processHttpResponseError(context, data, exception);
            }
            hasCallback = false;
        }

        @Override
        public void onProgress(int requestCode, HTTPProgressStatus progressStatus, HashMap<String, Object> newdata) {

        }

        @Override
        public void run() {
            LMSImageRequestParamBuilder builder = new LMSImageRequestParamBuilder();
            builder.setLimit(10).setPersonal(true).setSkip(imgList.size());
            session.getImages(localListener, 1, builder.toString());
        }
    }
}

package uk.ac.gla.dcs.gms.lms;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import uk.ac.gla.dcs.gms.api.GMS;
import uk.ac.gla.dcs.gms.api.GMSException;
import uk.ac.gla.dcs.gms.api.http.HTTPProgressStatus;
import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;
import uk.ac.gla.dcs.gms.api.lms.LMSImageRequestParamBuilder;
import uk.ac.gla.dcs.gms.api.lms.LMSSession;
import uk.ac.gla.dcs.gms.main.BackPressedListener;
import uk.ac.gla.dcs.gms.main.GMSMainFragment;
import uk.ac.gla.dcs.gms.utils.ErrorsUtils;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by ito.
 */
public class LMSDailySummary extends GMSMainFragment implements AbsListView.OnScrollListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private ListView listView;
    private ProgressBar progressBar;
    private ImageScrollerAdapter adapter;
    private Handler mHandler;
    private HashSet<String> imgList;
    private ArrayList<Pair<String, String>> imgViewList;
    private LocalListener localListener;
    private boolean hasCallback;
    private Context context;
    private Button button;
    private Calendar begin, end;
    private LMSSession lmsSession;

    public static LMSDailySummary newInstance(String section) {
        LMSDailySummary dailySummary = new LMSDailySummary();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, section);
        dailySummary.setArguments(args);
        return dailySummary;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        imgList = new HashSet();
        imgViewList = new ArrayList<>();
        context = inflater.getContext();

        View rootView;

        rootView = inflater.inflate(R.layout.lms_daily_summary, container, false);
        localListener = new LocalListener(rootView);

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

        try {
            lmsSession = GMS.getInstance().getLMSSession();
        } catch (GMSException e) {
            e.printStackTrace();
            lmsSession = null;
            //todo handle error...
        }

        LMSImageRequestParamBuilder builder = new LMSImageRequestParamBuilder(context);
        builder.setLimit(10).setPersonal(true);


        lmsSession.getImages(localListener, 0, builder.toString());

        button = (Button) rootView.findViewById(R.id.lms_daily_summary_button);
        button.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == button.getId()) {
            Calendar calendar = Calendar.getInstance();
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH);
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(context, this, year, month, day).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        LMSImageRequestParamBuilder builder = new LMSImageRequestParamBuilder(context);

        begin = Calendar.getInstance();
        end = Calendar.getInstance();

        begin.set(year, monthOfYear+1, dayOfMonth, 0, 1);
        end.set(year, monthOfYear+1, dayOfMonth, 23, 59);
        Log.v("Bruno", Integer.toString(dayOfMonth));

        imgList.clear();
        imgViewList.clear();
        adapter.notifyDataSetChanged();

        builder.setLimit(10)
                .setSkip(imgList.size())
                .setInterval(begin, end)
                .setPersonal(true);

        lmsSession.getImages(localListener, 0, builder.toString());

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

        private Animator mCurrentAnimator;
        private View parent;
        private Integer mShortAnimationDuration;

        public LocalListener(View view) {
            parent = view;
            mShortAnimationDuration = getResources().getInteger(R.integer.config_shortAnimTime);
        }

        @Override
        public void onClick(View v) {
            v.buildDrawingCache();
            Bitmap bitmap = v.getDrawingCache();
            zoomImageFromThumb(v, bitmap);
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
            LMSImageRequestParamBuilder builder = new LMSImageRequestParamBuilder(context);
            builder.setLimit(10).setPersonal(true).setSkip(imgList.size());
            lmsSession.getImages(localListener, 1, builder.toString());
        }

        private void zoomImageFromThumb(final View thumbView, Bitmap bitmap) {
            // If there's an animation in progress, cancel it
            // immediately and proceed with this one.
            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }

            // Load the high-resolution "zoomed-in" image.
            final ImageView expandedImageView = (ImageView) parent.findViewById(R.id.lms_daily_summary_expanded_image);
            expandedImageView.setImageBitmap(bitmap);
            PhotoViewAttacher mAtaccher;
            mAtaccher = new PhotoViewAttacher(expandedImageView);

            // Calculate the starting and ending bounds for the zoomed-in image.
            // This step involves lots of math. Yay, math.
            final Rect startBounds = new Rect();
            final Rect finalBounds = new Rect();
            final Point globalOffset = new Point();

            // The start bounds are the global visible rectangle of the thumbnail,
            // and the final bounds are the global visible rectangle of the container
            // view. Also set the container view's offset as the origin for the
            // bounds, since that's the origin for the positioning animation
            // properties (X, Y).
            thumbView.getGlobalVisibleRect(startBounds);
            parent.getGlobalVisibleRect(finalBounds, globalOffset);
            startBounds.offset(-globalOffset.x, -globalOffset.y);
            finalBounds.offset(-globalOffset.x, -globalOffset.y);

            // Adjust the start bounds to be the same aspect ratio as the final
            // bounds using the "center crop" technique. This prevents undesirable
            // stretching during the animation. Also calculate the start scaling
            // factor (the end scaling factor is always 1.0).
            float startScale;
            if ((float) finalBounds.width() / finalBounds.height()
                    > (float) startBounds.width() / startBounds.height()) {
                // Extend start bounds horizontally
                startScale = (float) startBounds.height() / finalBounds.height();
                float startWidth = startScale * finalBounds.width();
                float deltaWidth = (startWidth - startBounds.width()) / 2;
                startBounds.left -= deltaWidth;
                startBounds.right += deltaWidth;
            } else {
                // Extend start bounds vertically
                startScale = (float) startBounds.width() / finalBounds.width();
                float startHeight = startScale * finalBounds.height();
                float deltaHeight = (startHeight - startBounds.height()) / 2;
                startBounds.top -= deltaHeight;
                startBounds.bottom += deltaHeight;
            }

            // Hide the thumbnail and show the zoomed-in view. When the animation
            // begins, it will position the zoomed-in view in the place of the
            // thumbnail.
            thumbView.setAlpha(0f);
            expandedImageView.setVisibility(View.VISIBLE);

            // Set the pivot point for SCALE_X and SCALE_Y transformations
            // to the top-left corner of the zoomed-in view (the default
            // is the center of the view).
            expandedImageView.setPivotX(0f);
            expandedImageView.setPivotY(0f);

            // Construct and run the parallel animation of the four translation and
            // scale properties (X, Y, SCALE_X, and SCALE_Y).
            AnimatorSet set = new AnimatorSet();
            set
                    .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                            startBounds.left, finalBounds.left))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                            startBounds.top, finalBounds.top))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                            startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                    View.SCALE_Y, startScale, 1f));
            set.setDuration(mShortAnimationDuration);
            set.setInterpolator(new DecelerateInterpolator());
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCurrentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mCurrentAnimator = null;
                }
            });
            set.start();
            mCurrentAnimator = set;

            // Upon clicking the zoomed-in image, it should zoom back down
            // to the original bounds and show the thumbnail instead of
            // the expanded image.
            final float startScaleFinal = startScale;

            //Back Pressed
            BackPressedListener backPressedListener =
                    new MyBackPressedLister(expandedImageView, mCurrentAnimator,
                            startBounds, startScaleFinal, mShortAnimationDuration,
                            thumbView);
            IntentFilter intentFilter = new IntentFilter(BackPressedListener.BROADCAST_EVENT);
            context.registerReceiver(backPressedListener, intentFilter);

        }
    }

    private class MyBackPressedLister extends BackPressedListener {

        private ImageView expandedImageView;
        private Animator mCurrentAnimator;
        private Rect startBounds;
        private float startScaleFinal;
        private Integer mShortAnimationDuration;
        private View thumbView;

        public MyBackPressedLister(ImageView imageView, Animator animator, Rect startBounds,
                                   float startScaleFinal, Integer mShortAnimationDuration,
                                   View view) {
            expandedImageView = imageView;
            mCurrentAnimator = animator;
            this.startBounds = startBounds;
            this.startScaleFinal = startScaleFinal;
            this.mShortAnimationDuration = mShortAnimationDuration;
            thumbView = view;
        }

        @Override
        public boolean onBackPressed() {
            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            AnimatorSet set = new AnimatorSet();
            set.play(ObjectAnimator
                    .ofFloat(expandedImageView, View.X, startBounds.left))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.Y, startBounds.top))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_X, startScaleFinal))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_Y, startScaleFinal));
            set.setDuration(mShortAnimationDuration);
            set.setInterpolator(new DecelerateInterpolator());
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    thumbView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    thumbView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }
            });
            set.start();
            mCurrentAnimator = set;
            return true;
        }
    }
}

package uk.ac.gla.dcs.gms.lms;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

@SuppressWarnings("deprecation")
public class SingleViewActivity extends ActionBarActivity implements View.OnClickListener {

    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image);

        Bundle bundle = getIntent().getExtras();

        imageView = (ImageView) findViewById(R.id.singleview_iview);
        textView = (TextView) findViewById(R.id.single_image_tview_tags);

        String url;
        Integer resource = bundle.getInt(ImageScrollerAdapter.ARG_IMG_URL);
        if (resource == 0) {
            url = bundle.getString(ImageScrollerAdapter.ARG_IMG_URL);
            Picasso.with(getApplicationContext()).load(url).into(imageView);
        }
        else {
            Picasso.with(getApplicationContext()).load(resource).into(imageView);
        }
        textView.setText("Test");

        imageView.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.single_image_tview_tags || v.getId() == R.id.singleview_iview) {
            finish();
        }
    }
}

package uk.ac.gla.dcs.gms.lms;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class SingleViewActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image);

        Bundle bundle = getIntent().getExtras();

        ((ImageView)findViewById(R.id.singleview_iview)).setImageResource(bundle.getInt(ImageScrollerAdapter.ARG_IMG_URL));
        ((TextView)findViewById(R.id.single_image_tview_tags)).setText("Teste");

        findViewById(R.id.singleview_iview).setOnClickListener(this);
        findViewById(R.id.single_image_tview_tags).setOnClickListener(this);
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

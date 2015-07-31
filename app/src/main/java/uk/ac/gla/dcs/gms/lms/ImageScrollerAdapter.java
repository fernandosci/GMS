package uk.ac.gla.dcs.gms.lms;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageScrollerAdapter extends ArrayAdapter<Pair<String,String>> {

    public static final String ARG_IMG_URL = "ARG_IMG_URL";

    private int count, stepNumber;
    private LayoutInflater inflater;
    private View.OnClickListener clickListener;

    public ImageScrollerAdapter(Context context, int resource, ArrayList<Pair<String, String>> objects, View.OnClickListener clickListener) {
        super(context, resource, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.clickListener = clickListener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.row_layout,  parent,false);
        }

        Pair<String, String> t = getItem(position);

        if (t != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.row_layout_iview);
            ImageView imageView2 = (ImageView) view.findViewById(R.id.row_layout_iview2);

            imageView.setOnClickListener(clickListener);
            imageView.setTag(t.first);
            imageView2.setOnClickListener(clickListener);
            imageView2.setTag(t.second);

            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
            if (!t.first.isEmpty())
                Picasso.with(getContext()).load( t.first).resize(metrics.widthPixels/2, 500).into(imageView);
            if (!t.second.isEmpty()) {
                Picasso.with(getContext()).load( t.second).resize(metrics.widthPixels/2, 500).into(imageView2);
            }



            ((TextView) view.findViewById(R.id.textView)).setText("");
        }

        return view;
    }
}

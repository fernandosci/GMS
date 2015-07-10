package uk.ac.gla.dcs.gms.lms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ImageScroller extends ArrayAdapter<Pair> implements View.OnClickListener {

    public static final String IMG_ID = "img_id";

    private ArrayList<Pair> items;
    private int count, stepNumber;

    public ImageScroller(Context context, int resource, ArrayList<Pair> objects, int startingItems, int stepNumber) {
        super(context, resource, objects);
        items = objects;
        count = startingItems;
        this.stepNumber = stepNumber;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater)MainActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row_layout, null);
        }

        Pair t = items.get(position);

        if (t != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.row_layout_iview);
            ImageView imageView2 = (ImageView) view.findViewById(R.id.row_layout_iview2);
           // ((TextView)view.findViewById(R.id.textView)).setText(Integer.toString(position));
             ((TextView)view.findViewById(R.id.textView)).setText("");
            if (imageView != null) {
                imageView.setImageResource((Integer) t.first);
                imageView.setTag(t.first);
                imageView.setOnClickListener(this);
            }
            if (imageView2 != null) {
                imageView2.setImageResource((Integer) t.second);
                imageView2.setTag(t.second);
                imageView2.setOnClickListener(this);
            }
        }

        return view;
    }

    @Override
    public int getCount() {
        return count;
    }

    public boolean endReached(){
        return count == items.size();
    }

    public boolean showMore(){
        if(count == items.size()) {
            return true;
        }
        else{
            count = Math.min(count + stepNumber, items.size()); //don't go past the end
            notifyDataSetChanged(); //the count size has changed, so notify the super of the change
            return endReached();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.row_layout_iview || v.getId() == R.id.row_layout_iview2) {
            Bundle bundle = new Bundle();
            bundle.putInt(IMG_ID, (Integer) v.getTag());

            Intent intent = new Intent(MainActivity.instance, SingleViewActivity.class);
            intent.putExtras(bundle);
            MainActivity.instance.startActivity(intent);
            //Utils.shortToast(MainActivity.instance.getApplicationContext(), "Aqui?" + Integer.toString((Integer) v.getTag()));
        }
    }
}

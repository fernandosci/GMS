package uk.ac.gla.dcs.gms.lms;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginScreen();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_button) {
            setContentView(R.layout.register_screen);
            findViewById(R.id.cancel).setOnClickListener(this);
        }
        else if (v.getId() == R.id.cancel || v.getId() == R.id.back) {
            setContentView(R.layout.activity_main);
            loginScreen();
        }
        else if (v.getId() == R.id.login_button) {
            setContentView(R.layout.daily_summary);

            findViewById(R.id.back).setOnClickListener(this);

            /*((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView2)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView3)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView4)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView5)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView6)).setImageResource(R.drawable.bonus_coin);*/
        }
    }

    private void loginScreen() {
        //((ImageView)findViewById(R.id.logo)).setImageResource(R.drawable.bonus_coin);

        TextView textView = (TextView)(findViewById(R.id.description));
        textView.setText("Description Description Description...");

        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
    }
}


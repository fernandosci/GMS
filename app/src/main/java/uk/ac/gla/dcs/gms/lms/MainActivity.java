package uk.ac.gla.dcs.gms.lms;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
            findViewById(R.id.next).setOnClickListener(this);

            ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView2)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView3)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView4)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView5)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView6)).setImageResource(R.drawable.bonus_coin);
        }
        else if (v.getId() == R.id.next) {
            setContentView(R.layout.feedback);

            findViewById(R.id.feedback_button).setOnClickListener(this);
        }
        else if (v.getId() == R.id.feedback_button) {
            displayOption(R.id.feedback_rgrp_question1);
            displayOption(R.id.feedback_rgrp_question2);
            displayOption(R.id.feedback_rgrp_question3);
            displayOption(R.id.feedback_rgrp_question4);
            displayOption(R.id.feedback_rgrp_question5);
        }
    }

    private void loginScreen() {
        ((ImageView)findViewById(R.id.logo)).setImageResource(R.drawable.bonus_coin);

        TextView textView = (TextView)(findViewById(R.id.description));
        textView.setText("Description Description Description...");

        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
    }

    private void displayOption(int group) {
        RadioGroup radioGroup = (RadioGroup) findViewById(group);
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        Integer idx = radioGroup.indexOfChild(radioButton);
        Toast.makeText(getApplicationContext(), idx.toString(), Toast.LENGTH_SHORT).show();
    }
}


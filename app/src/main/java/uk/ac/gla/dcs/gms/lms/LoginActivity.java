package uk.ac.gla.dcs.gms.lms;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    final static int REGISTER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ((ImageView)findViewById(R.id.login_iview_logo)).setImageResource(R.drawable.bonus_coin);

        findViewById(R.id.login_btn_register).setOnClickListener(this);
        findViewById(R.id.login_btn).setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REGISTER) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = intent.getExtras();
                String email = (String) bundle.get(RegisterActivity.EMAIL);

                ((EditText)findViewById(R.id.login_edittxt_email)).setText(email);
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Register Canceled", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, REGISTER);
        }
        else if (v.getId() == R.id.login_btn || v.getId() == R.id.feedback_back) {
            startActivity(new Intent(this, MainActivity.class));

            /*setContentView(R.layout.daily_summary);

            findViewById(R.id.back).setOnClickListener(this);
            findViewById(R.id.next).setOnClickListener(this);

            ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView2)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView3)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView4)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView5)).setImageResource(R.drawable.bonus_coin);
            ((ImageView)findViewById(R.id.imageView6)).setImageResource(R.drawable.bonus_coin);*/
        }
        /*else if (v.getId() == R.id.next || v.getId() == R.id.single_image_btn_back) {
            setContentView(R.layout.feedback);

            findViewById(R.id.feedback_button).setOnClickListener(this);
            findViewById(R.id.feedback_back).setOnClickListener(this);
            findViewById(R.id.feedback_next).setOnClickListener(this);
        }
        else if (v.getId() == R.id.feedback_next) {
            setContentView(R.layout.single_image);

            ((ImageView)findViewById(R.id.singleview_iview)).setImageResource(R.drawable.bonus_coin);

            findViewById(R.id.single_image_btn_back).setOnClickListener(this);
            findViewById(R.id.single_image_btn_next).setOnClickListener(this);

            findViewById(R.id.single_image_tview_tags).setVisibility(View.INVISIBLE);
        }
        else if (v.getId() == R.id.single_image_btn_next) {
            startActivity(new Intent(this, MapsActivity.class));
        }
        else if (v.getId() == R.id.feedback_button) {
            displayOption(R.id.feedback_rgrp_question1);
            displayOption(R.id.feedback_rgrp_question2);
            displayOption(R.id.feedback_rgrp_question3);
            displayOption(R.id.feedback_rgrp_question4);
            displayOption(R.id.feedback_rgrp_question5);
        }*/
    }

    private void displayOption(int group) {
        RadioGroup radioGroup = (RadioGroup) findViewById(group);
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        Integer idx = radioGroup.indexOfChild(radioButton);
        Toast.makeText(getApplicationContext(), idx.toString(), Toast.LENGTH_SHORT).show();
    }
}


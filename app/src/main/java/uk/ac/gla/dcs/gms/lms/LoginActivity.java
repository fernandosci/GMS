package uk.ac.gla.dcs.gms.lms;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
        else if (v.getId() == R.id.login_btn) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}


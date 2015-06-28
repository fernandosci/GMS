package uk.ac.gla.dcs.gms.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends ActionBarActivity implements View.OnClickListener {

    final static String EMAIL = "RegisterActivity.email";
    final static String FIRSTNAME = "RegisterActivity.FirstName";
    final static String LASTNAME = "RegisterActivity.LastName";
    final static String PASSWORD = "RegisterActivity.Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        findViewById(R.id.register_btn).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED, null);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_btn) {

            String email = ((EditText)findViewById(R.id.register_edittxt_email)).getText().toString();
            String firstName = ((EditText)findViewById(R.id.register_edittxt_first_name)).getText().toString();
            String lastName = ((EditText)findViewById(R.id.register_edittxt_last_name)).getText().toString();
            String password1 = ((EditText)findViewById(R.id.register_edittxt_password)).getText().toString();
            String password2 = ((EditText)findViewById(R.id.register_edittxt_confirm_password)).getText().toString();

            if (password1.equals(password2)) { //make other password tests
                //send details to server
                // if success return to login screen with email

                Bundle bundle = new Bundle();
                bundle.putString(EMAIL, email);

                Intent intent = new Intent();
                intent.putExtras(bundle);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "Passwords must match", Toast.LENGTH_LONG).show();
            }

        }
    }
}

package uk.ac.gla.dcs.gms.lms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import uk.ac.gla.dcs.gms.Utils;
import uk.ac.gla.dcs.gms.api.APIResponse;
import uk.ac.gla.dcs.gms.api.LMSRegisterRequest;

@SuppressWarnings("deprecation")
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
        findViewById(R.id.register_tview_secret_question).setOnClickListener(this);

        ((EditText)findViewById(R.id.register_edittxt_last_name)).setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            findViewById(R.id.register_tview_secret_question).performClick();
                            return true;
                        }
                        return false;
                    }
                }
        );
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
                LMSRegisterRequest request = new LMSRegisterRequest(getApplicationContext()) {
                    @Override
                    protected void onPostExecute(APIResponse apiResponse) {
                        Utils.shortToast(getApplicationContext(), apiResponse.getRawResponse());
                    }
                };

                request.execute(getResources().getString(R.string.username),getResources().getString(R.string.password),"email","answer","question");//(username + ":" + password + ":" + email + ":" + answer + ":" + question)

                // if success return to login screen with email

                Bundle bundle = new Bundle();
                bundle.putString(EMAIL, email);

                Intent intent = new Intent();
                intent.putExtras(bundle);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            else {
                Utils.longToast(getApplicationContext(), "Passwords must match");
            }

        }
        else if (v.getId() == R.id.register_tview_secret_question) {
            final String [] questions = new String[2];
            questions[0] = "Question 1?";
            questions[1] = "Question 2?";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.register_str_secret_question);
            builder.setItems(questions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((TextView)findViewById(R.id.register_tview_secret_question)).setText(questions[which]);
                    findViewById(R.id.register_edittxt_answer).requestFocus();
                }
            });
            builder.show();
        }
    }
}

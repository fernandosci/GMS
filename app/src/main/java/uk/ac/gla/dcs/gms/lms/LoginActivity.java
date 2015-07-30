package uk.ac.gla.dcs.gms.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;

import uk.ac.gla.dcs.gms.Utils;
import uk.ac.gla.dcs.gms.api.CredentialAdapter;
import uk.ac.gla.dcs.gms.api.GMS;
import uk.ac.gla.dcs.gms.api.GMSException;
import uk.ac.gla.dcs.gms.api.http.HTTPProgressStatus;
import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;
import uk.ac.gla.dcs.gms.main.MainActivity;

@SuppressWarnings("deprecation")
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private final static int CODE_REGISTER = 0;
    private static final int STARTUP = 1000;
    private LocalLoginResponseListener httpResponseListener;


    private EditText editTxtEmail;
    private EditText editTxtPassword;
    private ImageView imgViewLogo;

    private Button btnRegister;
    private Button btnLogin;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainf_login);
        init();

        httpResponseListener = new LocalLoginResponseListener();

        try {
            GMS.getInstance().isLoggedIn(httpResponseListener, STARTUP);
        } catch (GMSException e) {
            e.printStackTrace();
        }
    }

    private void init() {

        editTxtEmail = (EditText) findViewById(R.id.login_edittxt_email);
        editTxtPassword = (EditText) findViewById(R.id.login_edittxt_password);
        imgViewLogo = (ImageView) findViewById(R.id.login_iview_logo);
        btnRegister = (Button) findViewById(R.id.login_btn_register);
        btnLogin = (Button) findViewById(R.id.login_btn);
        progressBar = (ProgressBar) findViewById(R.id.login_pbar);

        imgViewLogo.setImageResource(R.drawable.bonus_coin);

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == CODE_REGISTER) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = intent.getExtras();
                String email = bundle.getString(RegisterActivity.KEY_EMAIL);
                editTxtEmail.setText(email);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Register Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, CODE_REGISTER);
        } else if (v.getId() == R.id.login_btn) {

            if (editTxtEmail.getText().toString().length() == 0) {
                Utils.shortToast(getApplicationContext(), "Please input your e-mail.");
            } else if (editTxtPassword.getText().toString().length() == 0) {
                Utils.shortToast(getApplicationContext(), "Please input your password.");
            } else {
                btnLogin.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                try {
                    GMS.getInstance().loginWithCredentials(httpResponseListener, 0, CredentialAdapter.getLocalCredentialAdapter(editTxtEmail.getText().toString(), editTxtPassword.getText().toString()));
                } catch (GMSException e) {
                    e.printStackTrace();
                    Utils.shortToast(this, e.getMessage());
                }

            }
        }
    }

    private void login() {
        btnLogin.setEnabled(true);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private class LocalLoginResponseListener implements HTTPResponseListener {
        @Override
        public void onResponse(int requestCode, boolean successful, HashMap<String, Object> data, Exception exception) {
            if (btnLogin != null) {
                btnLogin.setEnabled(true);
            }

            if (successful)
                login();
            else {
                {
                    if (requestCode != STARTUP) {
                        if (data.containsKey(getString(R.string.lms_api_StandardFieldErrorsKey)))
                        {
                            String[] errors = (String[]) data.get(getString(R.string.lms_api_StandardFieldErrorsKey));

                            Utils.shortToast(getApplicationContext(),errors[0]);
                        }else if (exception!= null) {
                            exception.printStackTrace();

                            Utils.shortToast(getApplicationContext(), exception.getMessage());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }

        @Override
        public void onProgress(int requestCode, HTTPProgressStatus progressStatus, HashMap<String, Object> newdata) {

        }
    }
}

